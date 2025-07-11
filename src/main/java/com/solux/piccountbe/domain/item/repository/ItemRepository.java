package com.solux.piccountbe.domain.item.repository;

import com.solux.piccountbe.domain.item.entity.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.item.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByCategory(ShopCategory category);
}
