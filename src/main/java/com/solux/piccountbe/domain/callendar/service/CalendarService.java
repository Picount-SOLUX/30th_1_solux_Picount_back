package com.solux.piccountbe.domain.callendar.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.solux.piccountbe.domain.callendar.dto.*;
import com.solux.piccountbe.domain.callendar.entity.*;
import com.solux.piccountbe.domain.callendar.repository.*;
import com.solux.piccountbe.domain.category.entity.Category;
import com.solux.piccountbe.domain.category.repository.CategoryRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarService {

    private final CalendarEntryRepository calendarEntryRepository;
    private final IncomeDetailRepository incomeDetailRepository;
    private final ExpenseDetailRepository expenseDetailRepository;
    private final CalendarPhotoRepository calendarPhotoRepository;
    private final CategoryRepository categoryRepository;

    public void createEntry(CalendarRecordRequestDto request, MultipartFile[] photos, Member member) {
        LocalDate date = request.getEntryDate() != null ? request.getEntryDate() : LocalDate.now();

        // 기존 엔트리 가져오거나 생성
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
        if (photos != null && photos.length > 0) {
            if (photos.length > 3) {
                throw new CustomException(ErrorCode.CALENDAR_TOO_MANY_PHOTOS);
            }

            // 기존 사진 모두 삭제 후 교체
            calendarPhotoRepository.deleteByCalendarEntry(entry);

            for (MultipartFile file : photos) {
                if (file.getSize() > 5 * 1024 * 1024) {
                    throw new CustomException(ErrorCode.CALENDAR_PHOTO_TOO_LARGE);
                }
                String path = saveFile(file);
                CalendarPhoto photo = new CalendarPhoto(entry, path, file.getSize() / (1024f * 1024f));
                calendarPhotoRepository.save(photo);
            }
        }
    }

    private String saveFile(MultipartFile photo) {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs(); // 폴더 생성
            if (!created) {
                throw new CustomException(ErrorCode.CALENDAR_FILE_UPLOAD_FAIL, "업로드 폴더 생성 실패");
            }
        }

        String uniqueFilename = UUID.randomUUID() + "_" + photo.getOriginalFilename();
        File destination = new File(uploadDir + uniqueFilename);

        try {
            photo.transferTo(destination);
            return uploadDir + uniqueFilename;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.CALENDAR_FILE_UPLOAD_FAIL, "파일 저장 중 오류: " + e.getMessage());
        }
    }

    private Category validateCategory(Member member, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(ErrorCode.CATEGORY_NOT_MATCH_MEMBER);
        }

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
                .imageUrls(
                        photos.stream()
                                .map(CalendarPhoto::getFilePath)
                                .toList()
                )
                .build();
    }
}
