package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    private Inventory inventory;
    private Fish f1;
    private Fish f2;

    @BeforeEach
    void setup() {
        inventory = new Inventory();
        f1 = new Fish();
        f2 = new Fish();
    }

    @Test
    void ConstructorTest() {
        assertEquals(emptyList(), inventory.getFishStorage());
        assertEquals(0, inventory.getFishAmount());
    }

    @Test
    void addFish() {
        //single fish
        inventory.addFish(f1);
        assertEquals(1, inventory.getFishAmount());
        assertEquals(f1, inventory.getFish(0));

        //multiple fish
        inventory.addFish(f2);
        assertEquals(2, inventory.getFishAmount());
        assertEquals(f1, inventory.getFish(0));
        assertEquals(f2, inventory.getFish(1));
    }

    @Test
    void removeFishTest() {
        //single fish in the list
        inventory.addFish(f1);
        assertEquals(1, inventory.getFishAmount());
        inventory.removeFish(0);
        assertEquals(0, inventory.getFishAmount());

        //remove the first of the two
        inventory.addFish(f1);
        inventory.addFish(f2);
        assertEquals(2, inventory.getFishAmount());
        assertEquals(f1, inventory.getFish(0));
        assertEquals(f2, inventory.getFish(1));
        inventory.removeFish(0);
        assertEquals(1, inventory.getFishAmount());
        assertEquals(f2, inventory.getFish(0));


    }

    @Test
    void isFishInRangeTest() {
        inventory.addFish(f1);
        inventory.addFish(f2);
        assertEquals(2, inventory.getFishAmount());
        assertEquals(f1, inventory.getFish(0));
        assertEquals(f2, inventory.getFish(1));

        assertFalse(inventory.isFishInRange(2));
        assertTrue(inventory.isFishInRange(0));
        assertTrue(inventory.isFishInRange(1));
    }
}
