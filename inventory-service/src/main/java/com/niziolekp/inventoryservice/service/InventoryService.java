package com.niziolekp.inventoryservice.service;

import com.niziolekp.inventoryservice.dto.InventoryResponse;
import com.niziolekp.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> code) {
        log.info("Checking Inventory");
        return inventoryRepository.findBycodeIn(code).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .code(inventory.getCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
