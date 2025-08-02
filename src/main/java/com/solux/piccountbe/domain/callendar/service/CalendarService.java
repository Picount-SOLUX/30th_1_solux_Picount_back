package com.solux.piccountbe.domain.callendar.service;

import com.solux.piccountbe.config.S3.S3UploadService;
import com.solux.piccountbe.domain.callendar.dto.*;
import com.solux.piccountbe.domain.callendar.entity.*;
import com.solux.piccountbe.domain.callendar.repository.*;
import com.solux.piccountbe.domain.category.entity.Category;
import com.solux.piccountbe.domain.category.repository.CategoryRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarService {

    private final CalendarEntryRepository calendarEntryRepository;
    private final IncomeDetailRepository incomeDetailRepository;
    private final ExpenseDetailRepository expenseDetailRepository;
    private final CalendarPhotoRepository calendarPhotoRepository;
    private final CategoryRepository categoryRepository;
    private final CalendarEmotionRepository emotionRepository;
    private final S3UploadService s3UploadService;

    // 달력 등록
    public void createEntry(CalendarRecordRequestDto request, MultipartFile[] photos, Member member) {
        LocalDate date = request.getEntryDate() != null ? request.getEntryDate() : LocalDate.now();

        CalendarEntry entry = calendarEntryRepository.findByMemberAndEntryDate(member, date)
                .orElseGet(() -> calendarEntryRepository.save(new CalendarEntry(member, date, null)));

        // 메모가 있다면 덮어쓰기
        if (request.getMemo() != null) {
            entry.setMemo(request.getMemo()); // setter 필요
        }

        // 수입 저장
        List<IncomeDto> incomeList = request.getIncomeList();
        if (incomeList != null && !incomeList.isEmpty()) {
            for (IncomeDto incomeDto : incomeList) {
                if (incomeDto.getCategoryId() == null) {
                    throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
                }
                if (incomeDto.getAmount() == null || incomeDto.getAmount() <= 0) {
                    throw new CustomException(ErrorCode.CALENDAR_INVALID_AMOUNT);
                }

                Category category = validateCategory(member, incomeDto.getCategoryId());
                IncomeDetail income = new IncomeDetail(entry, category, incomeDto.getAmount());
                incomeDetailRepository.save(income);
            }
        }

        // 지출 저장
        List<ExpenseDto> expenseList = request.getExpenseList();
        if (expenseList != null && !expenseList.isEmpty()) {
            for (ExpenseDto expenseDto : expenseList) {
                if (expenseDto.getCategoryId() == null) {
                    throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
                }
                if (expenseDto.getAmount() == null || expenseDto.getAmount() <= 0) {
                    throw new CustomException(ErrorCode.CALENDAR_INVALID_AMOUNT);
                }

                Category category = validateCategory(member, expenseDto.getCategoryId());
                ExpenseDetail expense = new ExpenseDetail(entry, category, expenseDto.getAmount());
                expenseDetailRepository.save(expense);
            }
        }

        // 사진 저장
        calendarPhotoRepository.deleteByCalendarEntry(entry);

        if (photos != null && photos.length > 0) {
            if (photos.length > 1) throw new CustomException(ErrorCode.CALENDAR_TOO_MANY_PHOTOS);
            MultipartFile file = photos[0];
            if (file.getSize() > 5 * 1024 * 1024) throw new CustomException(ErrorCode.CALENDAR_PHOTO_TOO_LARGE);

            try {
                String imageUrl = s3UploadService.saveFile(file);
                calendarPhotoRepository.save(new CalendarPhoto(entry, imageUrl, file.getSize() / (1024f * 1024f)));
            } catch (IOException e) {
                throw new CustomException(ErrorCode.CALENDAR_FILE_UPLOAD_FAIL, e.getMessage());
            }
        }
    }

    private void validateAmountAndCategory(Integer amount, Long categoryId) {
        if (categoryId == null) throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        if (amount == null || amount <= 0) throw new CustomException(ErrorCode.CALENDAR_INVALID_AMOUNT);
    }

    private Category validateCategory(Member member, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        if (!category.getMember().getMemberId().equals(member.getMemberId()))
            throw new CustomException(ErrorCode.CATEGORY_NOT_MATCH_MEMBER);
        return category;
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public CalendarRecordDetailResponseDto getCalendarDetail(Member member, LocalDate date) {
        CalendarEntry entry = calendarEntryRepository.findByMemberAndEntryDate(member, date)
                .orElseThrow(() -> new CustomException(ErrorCode.CALENDAR_NOT_FOUND));

        List<IncomeDetail> incomes = incomeDetailRepository.findAllByCalendarEntry(entry);
        List<ExpenseDetail> expenses = expenseDetailRepository.findAllByCalendarEntry(entry);
        List<CalendarPhoto> photos = calendarPhotoRepository.findAllByCalendarEntry(entry);

        return CalendarRecordDetailResponseDto.builder()
                .date(date)
                .memo(entry.getMemo())
                .incomes(
                        incomes.stream()
                                .map(i -> new CalendarRecordDetailResponseDto.IncomeDto(
                                        i.getCategory().getCategoryId(),
                                        i.getCategory().getName(),
                                        i.getAmount()
                                )).toList()
                )
                .expenses(
                        expenses.stream()
                                .map(e -> new CalendarRecordDetailResponseDto.ExpenseDto(
                                        e.getCategory().getCategoryId(),
                                        e.getCategory().getName(),
                                        e.getAmount()
                                )).toList()
                )
                .imageUrls(photos.stream().map(CalendarPhoto::getUrl).toList())
                .build();
    }

    // 요약 조회
    public CalendarMonthlySummaryResponseDto getMonthlySummary(Member member, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        // 감정 맵
        Map<LocalDate, EmotionType> emotionMap = emotionRepository
                .findAllByMemberAndEntryDateBetween(member, start, end)
                .stream()
                .collect(Collectors.toMap(CalendarEmotion::getEntryDate, CalendarEmotion::getEmotion));

        // 수입 맵
        Map<LocalDate, Integer> incomeMap = incomeDetailRepository
                .findDailyIncomeSums(member, start, end)
                .stream()
                .collect(Collectors.toMap(
                        row -> (LocalDate) row[0],
                        row -> ((Long) row[1]).intValue()
                ));

        // 지출 맵
        Map<LocalDate, Integer> expenseMap = expenseDetailRepository
                .findDailyExpenseSums(member, start, end)
                .stream()
                .collect(Collectors.toMap(
                        row -> (LocalDate) row[0],
                        row -> ((Long) row[1]).intValue()
                ));

        List<CalendarMonthlySummaryResponseDto.DailySummary> summaryList = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            summaryList.add(
                    CalendarMonthlySummaryResponseDto.DailySummary.builder()
                            .date(date)
                            .emotion(emotionMap.getOrDefault(date, null))
                            .incomeTotal(incomeMap.getOrDefault(date, 0))
                            .expenseTotal(expenseMap.getOrDefault(date, 0))
                            .build()
            );
        }

        return CalendarMonthlySummaryResponseDto.builder()
                .month(String.format("%04d-%02d", year, month))
                .summary(summaryList)
                .build();
    }

    // 수정하기
    @Transactional
    public void updateEntry(CalendarRecordUpdateRequestDto request, MultipartFile[] photo, Member member, LocalDate date) {
        CalendarEntry entry = calendarEntryRepository.findByMemberAndEntryDate(member, date)
                .orElseThrow(() -> new CustomException(ErrorCode.CALENDAR_NOT_FOUND));

        // 수입 수정 or 삭제
        if (request.getIncomeList() != null) {
            for (UpdateIncomeDto dto : request.getIncomeList()) {
                IncomeDetail income = incomeDetailRepository.findById(dto.getIncomeId())
                        .orElseThrow(() -> new CustomException(ErrorCode.CALENDAR_INCOME_NOT_FOUND));
                if (!income.getCalendarEntry().equals(entry)) {
                    throw new CustomException(ErrorCode.CALENDAR_FORBIDDEN);
                }
                if (dto.isDelete()) {
                    incomeDetailRepository.delete(income);
                } else {
                    Category category = validateCategory(member, dto.getCategoryId());
                    income.update(dto.getAmount(), category);
                }
            }
        }

        // 지출 수정 or 삭제
        if (request.getExpenseList() != null) {
            for (UpdateExpenseDto dto : request.getExpenseList()) {
                ExpenseDetail expense = expenseDetailRepository.findById(dto.getExpenseId())
                        .orElseThrow(() -> new CustomException(ErrorCode.CALENDAR_EXPENSE_NOT_FOUND));
                if (!expense.getCalendarEntry().equals(entry)) {
                    throw new CustomException(ErrorCode.CALENDAR_FORBIDDEN);
                }
                if (dto.isDelete()) {
                    expenseDetailRepository.delete(expense);
                } else {
                    Category category = validateCategory(member, dto.getCategoryId());
                    expense.update(dto.getAmount(), category);
                }
            }
        }

        // 메모 수정
        if (request.getMemo() != null) {
            entry.setMemo(request.getMemo());
        }

        // 사진 수정 (덮어쓰기)
        if (photo != null && photo.length > 0) {
            if (photo.length > 1) throw new CustomException(ErrorCode.CALENDAR_TOO_MANY_PHOTOS);
            calendarPhotoRepository.deleteByCalendarEntry(entry);

            MultipartFile file = photo[0];
            if (file.getSize() > 5 * 1024 * 1024) throw new CustomException(ErrorCode.CALENDAR_PHOTO_TOO_LARGE);

            try {
                String imageUrl = s3UploadService.saveFile(file);
                CalendarPhoto newPhoto = new CalendarPhoto(entry, imageUrl, file.getSize() / (1024f * 1024f));
                calendarPhotoRepository.save(newPhoto);
            } catch (IOException e) {
                throw new CustomException(ErrorCode.CALENDAR_FILE_UPLOAD_FAIL, e.getMessage());
            }
        }
    }

    // 오늘 출석체크 포인트 저장
    @Transactional
    public void updateTodayPoint(Member member, long point) {
        LocalDate today = LocalDate.now();

        CalendarEntry entry = calendarEntryRepository.findByMemberAndEntryDate(member, today)
                .orElseGet(() -> calendarEntryRepository.save(new CalendarEntry(member, today)));

        entry.setPoint(point);
    }
}
