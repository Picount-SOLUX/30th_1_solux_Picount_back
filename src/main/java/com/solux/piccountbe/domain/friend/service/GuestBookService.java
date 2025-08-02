package com.solux.piccountbe.domain.friend.service;

import com.solux.piccountbe.domain.friend.dto.GuestBookRequestDto;
import com.solux.piccountbe.domain.friend.dto.GuestBookSummaryDto;
import com.solux.piccountbe.domain.friend.dto.GuestBookDetailDto;
import com.solux.piccountbe.domain.friend.dto.GuestbookMyResponseDto;
import com.solux.piccountbe.domain.friend.entity.GuestBook;
import com.solux.piccountbe.domain.friend.repository.GuestBookRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.MemberService;
import com.solux.piccountbe.global.exception.ErrorCode;
import com.solux.piccountbe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestBookService {

    private final GuestBookRepository guestBookRepository;
    private final MemberService memberService;

    //공통 접근 제어 로직
    private void validateAccess(Member viewer, Member owner) {
        if (!viewer.getMemberId().equals(owner.getMemberId()) && !owner.getIsMainVisible()) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "비공개 설정된 사용자입니다.");
        }
    }

    // 방명록 작성
    @Transactional
    public void createGuestbook(Member writer, GuestBookRequestDto requestDto) {
        Member owner = memberService.getMemberById(requestDto.getOwnerId());

        validateAccess(writer, owner); // 접근 제어

        GuestBook guestBook = new GuestBook(writer, owner, requestDto.getContent());
        guestBookRepository.save(guestBook);
    }

    // 요약 조회
    @Transactional(readOnly = true)
    public Page<GuestBookSummaryDto> getGuestBooks(Member viewer, Long ownerId, Pageable pageable) {
        Member owner = memberService.getMemberById(ownerId);

        validateAccess(viewer, owner); // 접근 제어

        return guestBookRepository.findByOwnerAndIsDeletedFalse(owner, pageable)
                .map(gb -> new GuestBookSummaryDto(
                        gb.getGuestbookId(),
                        gb.getWriter().getProfileImageUrl(),
                        gb.getContent(),
                        gb.getCreatedAt()
                ));
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public Page<GuestBookDetailDto> getGuestbookDetails(Member viewer, Long ownerId, Pageable pageable) {
        Member owner = memberService.getMemberById(ownerId);

        validateAccess(viewer, owner); // 접근 제어

        return guestBookRepository.findByOwnerAndIsDeletedFalse(owner, pageable)
                .map(gb -> new GuestBookDetailDto(
                        gb.getGuestbookId(),
                        gb.getWriter().getProfileImageUrl(),
                        gb.getWriter().getNickname(),
                        gb.getContent(),
                        gb.getCreatedAt()
                ));
    }

    // 내가 남긴 방명록 조회
    public Page<GuestbookMyResponseDto> getMyGuestbookPosts(Long memberId, Pageable pageable) {
        return guestBookRepository.findByWriterId(memberId, pageable)
                .map(gb -> new GuestbookMyResponseDto(
                        gb.getGuestbookId(),
                        gb.getOwner().getNickname(),
                        gb.getContent(),
                        gb.getCreatedAt()
                ));
    }

    // 내가 남긴 방명록 개별 삭제
    @Transactional
    public void deleteMyGuestbook(Long guestbookId, Long memberId) {
        GuestBook guestBook = guestBookRepository.findById(guestbookId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방명록이 존재하지 않습니다."));

        if (!guestBook.getWriter().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("본인이 작성한 방명록만 삭제할 수 있습니다.");
        }

        guestBook.setDeleted(true);
    }

    // 내가 남긴 방명록 전체 삭제
    @Transactional
    public void deleteAllMyGuestbooks(Long memberId) {
        List<GuestBook> guestBooks = guestBookRepository.findAllByWriter_MemberIdAndIsDeletedFalse(memberId);
        for (GuestBook gb : guestBooks) {
            gb.setDeleted(true);
        }
    }

}
