package com.solux.piccountbe.domain.category.service;

import org.springframework.stereotype.Service;

import com.solux.piccountbe.domain.category.dto.CreateCategoryRequestDto;
import com.solux.piccountbe.domain.category.dto.GetCategoryResponseDto;
import com.solux.piccountbe.domain.category.entity.Category;
import com.solux.piccountbe.domain.category.repository.CategoryRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.MemberService;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final MemberService memberService;

	public void createCategory(Long memberId, CreateCategoryRequestDto categoryRequestDto) {
		Member member = memberService.getMemberById(memberId);

		if (categoryRepository.existsByMemberAndTypeAndName(
			member,
			categoryRequestDto.getType(),
			categoryRequestDto.getCategoryName())
		) {
			throw new CustomException(ErrorCode.CATAEGORY_EXIST);
		}

		Category category = Category.builder()
			.member(member)
			.name(categoryRequestDto.getCategoryName())
			.type(categoryRequestDto.getType())
			.build();

		categoryRepository.save(category);
	}

	public GetCategoryResponseDto getCategory(Long memberId, Long categoryId) {

		Member member = memberService.getMemberById(memberId);
		Category category = categoryRepository.findById(categoryId).
			orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)
			);

		if (!category.getMember().equals(member)) {
			throw new CustomException(ErrorCode.CATEGORY_NOT_MATCH_MEMBER);
		}

		return new GetCategoryResponseDto(
			categoryId,
			category.getName(),
			category.getType(),
			category.getType().getLabel()
		);
	}
}
