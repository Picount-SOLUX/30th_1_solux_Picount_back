package com.solux.piccountbe.domain.challenge.service;

import com.solux.piccountbe.domain.callendar.entity.CalendarEntry;
import com.solux.piccountbe.domain.callendar.repository.CalendarEntryRepository;
import com.solux.piccountbe.domain.callendar.service.CalendarService;
import com.solux.piccountbe.domain.challenge.dto.response.ChallengeResponseDto;
import com.solux.piccountbe.domain.challenge.dto.response.ChallengeRewardResponseDto;
import com.solux.piccountbe.domain.challenge.dto.response.MemberChallengeResponseDto;
import com.solux.piccountbe.domain.challenge.entity.Challenge;
import com.solux.piccountbe.domain.challenge.entity.MemberChallenge;
import com.solux.piccountbe.domain.challenge.entity.Status;
import com.solux.piccountbe.domain.challenge.entity.Type;
import com.solux.piccountbe.domain.challenge.repository.ChallengeRepository;
import com.solux.piccountbe.domain.challenge.repository.MemberChallengeRepository;
import com.solux.piccountbe.domain.friend.repository.GuestBookRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.MemberService;
import com.solux.piccountbe.domain.pointHistory.entity.PointHistory;
import com.solux.piccountbe.domain.pointHistory.entity.Reason;
import com.solux.piccountbe.domain.pointHistory.service.PointService;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final MemberChallengeRepository memberChallengeRepository;
    private final MemberService memberService;
    private final PointService pointService;
    private final CalendarService calendarService;
    private final ChallengeRepository challengeRepository;
    private final CalendarEntryRepository calendarEntryRepository;
    private final GuestBookRepository guestBookRepository;

    // 내 챌린지 현황 조회
    public List<MemberChallengeResponseDto> getMyChallengeStatus(Long memberId) {
        Member member = memberService.getMemberById(memberId);
        // 사용자 챌린지 현황 조회
        List<MemberChallenge> memberChallenges = memberChallengeRepository.findByMember(member);

        return memberChallenges.stream()
                .map(MemberChallengeResponseDto::from)
                .toList();
    }


    // 전체 챌린지 목록 조회
    public List<ChallengeResponseDto> getAllChallenges() {
        return challengeRepository.findAll().stream()
                .map(ChallengeResponseDto::from)
                .toList();
    }

    // 챌린지 보상 수령
    @Transactional
    public ChallengeRewardResponseDto completeChallenge(Member member, Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        MemberChallenge mc = memberChallengeRepository.findByMemberAndChallenge(member, challenge)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_CHALLENGE_NOT_FOUND));

        if (mc.getStatus() != Status.ONGOING) {
            throw new CustomException(ErrorCode.INVALID_CHALLENGE_STATUS);
        }

        mc.updateStatus(Status.COMPLETED);
        mc.setCompletedAt(LocalDate.now());
        memberChallengeRepository.save(mc);

        long reward = grantChallengeReward(member, challenge);

        return new ChallengeRewardResponseDto(challengeId, mc.getStatus(), reward);
    }

    // 포인트 지급
    private long grantChallengeReward(Member member, Challenge challenge) {
        long reward;
        Reason reason;

        switch (challenge.getType()) {
            case ATTENDANCE -> {
                List<Integer> options = List.of(50, 100, 150);
                reward = options.get(new Random().nextInt(options.size()));
                reason = Reason.ATTENDANCE;
                calendarService.updateTodayPoint(member, reward);
            }
            case ATTENDANCE7 -> {
                reward = 1000;
                reason = Reason.ATTENDANCE;
            }
            case ATTENDANCE30 -> {
                reward = 3000;
                reason = Reason.ATTENDANCE;
            }
            case GUESTBOOK -> {
                reward = 200;
                reason = Reason.GUESTBOOK;
            }
            case NO_SPENDING -> {
                reward = 2000;
                reason = Reason.NO_SPENDING;
            }
            default -> throw new CustomException(ErrorCode.INVALID_CHALLENGE_STATUS);
        }

        // 포인트 지급
        memberService.addPoint(member, reward);

        // 포인트 내역 저장
        pointService.saveChallengeRewardHistory(member, reward, reason);

        return reward;
    }


    // 챌린지 점검
    @Transactional
    public void evaluateAllChallenges(Long memberId) {
        Member member = memberService.getMemberById(memberId);

        // 일일 출석체크
        handleDailyAttendance(member);
        // 7일 연속 출석체크
        updateAttendance7Challenge(member);
        // 30일 연속 출석체크
        updateAttendance30Challenge(member);
        // 방명록 1회 작성
        updateGuestbookChallenge(member);
        // 10일 이상 무지출
        updateNoSpendingChallenge(member);
    }

    // 매일 챌린지 상태 리셋
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void resetCompletedDailyChallenges() {
        List<MemberChallenge> dailyCompletedChallenges = memberChallengeRepository
                .findAllByChallenge_TypeInAndStatus(List.of(Type.ATTENDANCE, Type.GUESTBOOK), Status.COMPLETED);

        for (MemberChallenge mc : dailyCompletedChallenges) {
            mc.updateStatus(Status.LOCKED);
        }

        memberChallengeRepository.saveAll(dailyCompletedChallenges);
    }

    // 일일 출석 체크
    private void handleDailyAttendance(Member member) {
        LocalDate today = LocalDate.now();

        boolean alreadyExists = calendarEntryRepository.existsByMemberAndEntryDate(member, today);
        if (!alreadyExists) {
            // CalendarEntry 생성
            calendarEntryRepository.save(new CalendarEntry(member, today));

            // MemberChallenge 상태 변경
            Challenge attendanceChallenge = challengeRepository.findByType(Type.ATTENDANCE)
                    .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

            MemberChallenge memberChallenge = memberChallengeRepository.findByMemberAndChallenge(member, attendanceChallenge)
                    .orElseGet(() -> {
                        MemberChallenge newChallenge = new MemberChallenge(member, attendanceChallenge, Status.LOCKED);
                        return memberChallengeRepository.save(newChallenge);
                    });

            if (memberChallenge.getStatus() != Status.COMPLETED) {
                memberChallenge.updateStatus(Status.ONGOING);
            }
        }
    }


    // 7일 연속 출석 - 30일과 로직 똑같음
    private void updateAttendance7Challenge(Member member) {
        Challenge challenge = challengeRepository.findByType(Type.ATTENDANCE7)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        MemberChallenge memberChallenge = memberChallengeRepository.findByMemberAndChallenge(member, challenge)
                .orElseGet(() -> memberChallengeRepository.save(new MemberChallenge(member, challenge, Status.LOCKED)));

        // 마지막 수령일 기준 다음 날부터 연속 출석 확인
        LocalDate baseDate = memberChallenge.getCompletedAt() != null
                ? memberChallenge.getCompletedAt().plusDays(1)
                : LocalDate.now().minusDays(6); // 챌린지 처음이면 최근 7일 검사

        List<LocalDate> targetDates = IntStream.range(0, 7)
                .mapToObj(baseDate::plusDays)
                .toList();

        List<LocalDate> rewardedDates = calendarEntryRepository
                .findAttendanceRewardDates(member, targetDates); // 일일 출석 보상 기준

        boolean isAll7Attended = rewardedDates.containsAll(targetDates);

        if (isAll7Attended) {
            memberChallenge.updateStatus(Status.ONGOING);
        } else {
            memberChallenge.updateStatus(Status.LOCKED);
        }

        memberChallengeRepository.save(memberChallenge);
    }

    // 30일 연속 출석
    private void updateAttendance30Challenge(Member member) {
        Challenge challenge = challengeRepository.findByType(Type.ATTENDANCE30)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        MemberChallenge memberChallenge = memberChallengeRepository.findByMemberAndChallenge(member, challenge)
                .orElseGet(() -> memberChallengeRepository.save(new MemberChallenge(member, challenge, Status.LOCKED)));

        // 마지막 수령일 기준 다음 날부터 연속 출석 확인
        LocalDate baseDate = memberChallenge.getCompletedAt() != null
                ? memberChallenge.getCompletedAt().plusDays(1)
                : LocalDate.now().minusDays(29); // 챌린지 처음이면 최근 7일 검사

        List<LocalDate> targetDates = IntStream.range(0, 29)
                .mapToObj(baseDate::plusDays)
                .toList();

        List<LocalDate> rewardedDates = calendarEntryRepository
                .findAttendanceRewardDates(member, targetDates);

        boolean isAll30Attended = rewardedDates.containsAll(targetDates);

        if (isAll30Attended) {
            memberChallenge.updateStatus(Status.ONGOING);
        } else {
            memberChallenge.updateStatus(Status.LOCKED);
        }

        memberChallengeRepository.save(memberChallenge);
    }

    // 방명록 1회 작성
    private void updateGuestbookChallenge(Member member) {
        Challenge challenge = challengeRepository.findByType(Type.GUESTBOOK)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        MemberChallenge memberChallenge = memberChallengeRepository.findByMemberAndChallenge(member, challenge)
                .orElseGet(() -> memberChallengeRepository.save(new MemberChallenge(member, challenge, Status.LOCKED)));

        if (memberChallenge.getStatus() == Status.COMPLETED) {
            return; // 오늘 이미 수령한 경우, 변경 안 함
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        boolean hasWrittenToday = guestBookRepository.existsByWriterAndIsDeletedFalseAndCreatedAtBetween(
                member, startOfDay, endOfDay
        );

        memberChallenge.updateStatus(hasWrittenToday ? Status.ONGOING : Status.LOCKED);
    }

    // 10일 이상 무지출 챌린지
    private void updateNoSpendingChallenge(Member member) {
        Challenge challenge = challengeRepository.findByType(Type.NO_SPENDING)
                .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

        MemberChallenge memberChallenge = memberChallengeRepository.findByMemberAndChallenge(member, challenge)
                .orElseGet(() -> memberChallengeRepository.save(new MemberChallenge(member, challenge, Status.LOCKED)));

        // completedAt 다음 날부터 무지출 일수 계산
        LocalDate baseDate = memberChallenge.getCompletedAt() != null
                ? memberChallenge.getCompletedAt().plusDays(1)
                : LocalDate.of(2025, 7, 20);  // 처음일 때

        int noSpendingDays = calendarEntryRepository.countNoSpendingDaysFromDate(member, baseDate);

        if (noSpendingDays >= 10) {
            memberChallenge.updateStatus(Status.ONGOING);
        } else {
            memberChallenge.updateStatus(Status.LOCKED);
        }
    }


}

