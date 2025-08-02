package com.solux.piccountbe.mock.challenge;

import com.solux.piccountbe.domain.challenge.entity.Challenge;
import com.solux.piccountbe.domain.challenge.entity.Type;
import com.solux.piccountbe.domain.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(4)
public class ChallengeInitializer implements CommandLineRunner {

    private final ChallengeRepository challengeRepository;

    @Override
    public void run(String... args) {
        if (challengeRepository.count() == 0) {
            challengeRepository.saveAll(List.of(
                    new Challenge("출석 체크", 100, Type.ATTENDANCE),
                    new Challenge("일주일 연속 출석", 300, Type.ATTENDANCE7),
                    new Challenge("30일 연속 출석", 1000, Type.ATTENDANCE30),
                    new Challenge("방명록 1회 작성", 100, Type.GUESTBOOK),
                    new Challenge("무지출 사유 10일 이상", 500, Type.NO_SPENDING)
            ));
        }
    }
}

