package com.niziolekp.inventoryservice.repository;

import com.niziolekp.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findBycodeIn(List<String> code);
}
