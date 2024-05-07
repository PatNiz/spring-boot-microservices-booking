package com.niziolekp.inventoryservice.util;

import com.niziolekp.inventoryservice.model.Inventory;
import com.niziolekp.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final InventoryRepository inventoryRepository;
    @Override
    public void run(String... args) throws Exception {
        Inventory inventory = new Inventory();
        inventory.setCode("croatia");
        inventory.setQuantity(100);

        Inventory inventory1 = new Inventory();
        inventory1.setCode("malta");
        inventory1.setQuantity(4);

        inventoryRepository.save(inventory);
        inventoryRepository.save(inventory1);
    }
}
