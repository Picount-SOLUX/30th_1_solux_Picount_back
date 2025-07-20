package com.solux.piccountbe.domain.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.solux.piccountbe.domain.category.dto.CreateOrUpdateCategoryRequestDto;
import com.solux.piccountbe.domain.category.dto.GetAllCategoryResponseDto;
import com.solux.piccountbe.domain.category.dto.GetCategoryResponseDto;
import com.solux.piccountbe.domain.category.entity.Category;
import com.solux.piccountbe.domain.category.entity.Type;
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

	private final MemberService memberService;
	private final CategoryRepository categoryRepository;

	public void createCategory(Long memberId, CreateOrUpdateCategoryRequestDto categoryRequestDto) {
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

		Category category = getCategoryById(memberId, categoryId);

		return new GetCategoryResponseDto(
			categoryId,
			category.getName(),
			category.getType(),
			category.getType().getLabel()
		);
	}

	public GetAllCategoryResponseDto getAllCategory(Long memberId, Type type) {
		Member member = memberService.getMemberById(memberId);
		List<GetCategoryResponseDto> categoryResponseDtoList = member.getCategories()
			.stream()
			.filter(cat -> type == null || cat.getType() == type)
			.map(a -> new GetCategoryResponseDto(
				a.getCategoryId(),
				a.getName(),
				a.getType(),
				a.getType().getLabel()
			))
			.collect(Collectors.toList());

		return new GetAllCategoryResponseDto(categoryResponseDtoList);
	}

	public void deleteCategory(Long memberId, Long categoryId) {

		Category category = getCategoryById(memberId, categoryId);

		categoryRepository.delete(category);
	}

	public void updateCategory(Long memberId, Long categoryId, CreateOrUpdateCategoryRequestDto updateRequestDto) {

		Category category = getCategoryById(memberId, categoryId);
		category.updateCategory(updateRequestDto.getCategoryName(), updateRequestDto.getType());
		categoryRepository.save(category);
	}

	public Category getCategoryById(Long memberId, Long categoryId) {

		Member member = memberService.getMemberById(memberId);
		Category category = categoryRepository.findById(categoryId).
			orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)
			);

		if (!category.getMember().equals(member)) {
			throw new CustomException(ErrorCode.CATEGORY_NOT_MATCH_MEMBER);
		}

		return category;
	}
}
