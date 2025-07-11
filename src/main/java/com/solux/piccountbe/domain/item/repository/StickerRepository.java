package com.solux.piccountbe.domain.item.repository;

import com.solux.piccountbe.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.item.entity.Sticker;

import java.util.Optional;

public interface StickerRepository  extends JpaRepository<Sticker, Long> {
    Optional<Sticker> findByItem (Item item);
}