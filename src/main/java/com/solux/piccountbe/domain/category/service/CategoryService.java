package com.solux.piccountbe.domain.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.solux.piccountbe.domain.category.dto.AllCategoryResponseDto;
import com.solux.piccountbe.domain.category.dto.CategoryRequestDto;
import com.solux.piccountbe.domain.category.dto.CategoryResponseDto;
import com.solux.piccountbe.domain.category.dto.CreateCategoryRequestDto;
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

	public AllCategoryResponseDto createCategory(Long memberId, CreateCategoryRequestDto categoryRequestDto) {
		Member member = memberService.getMemberById(memberId);
		List<CategoryResponseDto> categoryResponseDtoList = new ArrayList<>();

		for (CategoryRequestDto categoryDto : categoryRequestDto.getCategories()) {

			if (categoryRepository.existsByMemberAndTypeAndName(
				member,
				categoryDto.getType(),
				categoryDto.getCategoryName())
			) {
				throw new CustomException(ErrorCode.CATAEGORY_EXIST);
			}

			Category category = Category.builder()
				.member(member)
				.name(categoryDto.getCategoryName())
				.type(categoryDto.getType())
				.build();

			categoryRepository.save(category);

			categoryResponseDtoList.add(CategoryResponseDto.builder()
				.categoryId(category.getCategoryId())
				.categoryName(category.getName())
				.type(category.getType())
				.typeLabel(category.getType().getLabel())
				.build());
		}

		return new AllCategoryResponseDto(categoryResponseDtoList);
	}

	public CategoryResponseDto getCategory(Long memberId, Long categoryId) {

		Category category = getCategoryById(memberId, categoryId);

		return CategoryResponseDto.builder()
			.categoryId(category.getCategoryId())
			.categoryName(category.getName())
			.type(category.getType())
			.typeLabel(category.getType().getLabel())
			.build();
	}

	public AllCategoryResponseDto getAllCategory(Long memberId, Type type) {
		Member member = memberService.getMemberById(memberId);
		List<CategoryResponseDto> categoryResponseDtoList = member.getCategories()
			.stream()
			.filter(cat -> type == null || cat.getType() == type)
			.map(a -> new CategoryResponseDto(
				a.getCategoryId(),
				a.getName(),
				a.getType(),
				a.getType().getLabel()
			))
			.collect(Collectors.toList());

		return new AllCategoryResponseDto(categoryResponseDtoList);
	}

	public void deleteCategory(Long memberId, Long categoryId) {

		Category category = getCategoryById(memberId, categoryId);

		categoryRepository.delete(category);
	}

	public CategoryResponseDto updateCategory(Long memberId, Long categoryId, CategoryRequestDto updateRequestDto) {

		Category category = getCategoryById(memberId, categoryId);
		category.updateCategory(updateRequestDto.getCategoryName(), updateRequestDto.getType());
		categoryRepository.save(category);

		return CategoryResponseDto.builder()
			.categoryId(category.getCategoryId())
			.categoryName(category.getName())
			.type(category.getType())
			.typeLabel(category.getType().getLabel())
			.build();
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
