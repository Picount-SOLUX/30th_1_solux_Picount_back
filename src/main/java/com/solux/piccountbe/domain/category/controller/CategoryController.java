package com.solux.piccountbe.domain.category.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.category.dto.CreateCategoryRequestDto;
import com.solux.piccountbe.domain.category.dto.GetAllCategoryResponseDto;
import com.solux.piccountbe.domain.category.dto.GetCategoryResponseDto;
import com.solux.piccountbe.domain.category.entity.Category;
import com.solux.piccountbe.domain.category.entity.Type;
import com.solux.piccountbe.domain.category.service.CategoryService;
import com.solux.piccountbe.global.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
	private final CategoryService categoryService;

	@PostMapping
	public ResponseEntity<Response<Void>> createCategory(
		@RequestBody CreateCategoryRequestDto req,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Long memberId = userDetails.getMember().getMemberId();
		categoryService.createCategory(memberId, req);
		return ResponseEntity.ok(Response.success("카테고리 생성 완료", null));
	}

	@GetMapping({"/{categoryId}"})
	public ResponseEntity<Response<GetCategoryResponseDto>> getCategory(
		@PathVariable Long categoryId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Long memberId = userDetails.getMember().getMemberId();
		GetCategoryResponseDto res = categoryService.getCategory(memberId, categoryId);
		return ResponseEntity.ok(Response.success("단일 카테고리 조회 완료", res));
	}

	@GetMapping
	public ResponseEntity<Response<GetAllCategoryResponseDto>> getAllCategory(
		@RequestParam(value="type", required=false) Type type,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Long memberId = userDetails.getMember().getMemberId();
		GetAllCategoryResponseDto res = categoryService.getAllCategory(memberId, type);
		return ResponseEntity.ok(Response.success("전체 카테고리 조회 완료", res));
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Response<Void>> deleteCategory(
		@PathVariable Long categoryId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Long memberId = userDetails.getMember().getMemberId();
		categoryService.deleteCategory(memberId, categoryId);
		return ResponseEntity.ok(Response.success("카테고리 삭제 완료", null));
	}

}
