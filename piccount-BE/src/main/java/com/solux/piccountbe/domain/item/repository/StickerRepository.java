package com.solux.piccountbe.domain.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.item.entity.Sticker;

public interface StickerRepository  extends JpaRepository<Sticker, Long> {
}