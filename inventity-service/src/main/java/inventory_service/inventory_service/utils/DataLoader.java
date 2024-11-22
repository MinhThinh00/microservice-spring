package inventory_service.inventory_service.utils;

import inventory_service.inventory_service.model.Inventory;
import inventory_service.inventory_service.repository.InventoryRepository;
import org.hibernate.annotations.Comment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final InventoryRepository inventoryRepository;
    public DataLoader(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        Inventory inventory = new Inventory();
        inventory.setSkuCode("iphone_13");
        inventory.setQuantity(100);

        Inventory inventory1 = new Inventory();
        inventory1.setSkuCode("iphone_13_red");
        inventory1.setQuantity(0);

        inventoryRepository.save(inventory);
        inventoryRepository.save(inventory1);
    }
}
