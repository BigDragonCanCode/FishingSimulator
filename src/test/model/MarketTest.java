package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {
    private Fisher f1;
    private Inventory inventory;
    private Regulation reg1;
    private Market m1;
    private Fish fish1;

    @BeforeEach
    void setup() {
        f1 = new Fisher("fisher1");
        fish1 = new Fish();

        inventory = new Inventory();
        inventory.addFish(fish1);

        reg1 = new Regulation(f1);
        reg1.addCaptureAmount(11);

        m1 = new Market(f1, inventory, reg1);
    }

    @Test
    void ConstructorTest() {
        fisherSetup();
        inventorySetup();
        regulationSetup();
    }

    @Test
    void sellTest() {
        //the method runs in parameter order, so I switched the places of "actual" and "expected" value.
        assertEquals(m1.sell(0),"$" + fish1.calculatePrice() + " is added to your balance, now you have $"
                + f1.getBalance() + " in total.\n");
    }

    @Test
    void tryFishingRodTest() {
        //purchase succeeded
        assertEquals("You bought a fishing rod by $200\n", m1.tryFishingRod());
        assertEquals(1800.0, f1.getBalance());
        assertTrue(f1.hasFishingRod());

        //purchase failed
        f1.setFishingRod(false);
        assertFalse(f1.hasFishingRod());
        f1.spend(1800);
        assertEquals(0, f1.getBalance());
        assertEquals("You don't have enough money", m1.tryFishingRod());
    }

    @Test
    void tryFishingNetTest() {
        //purchase succeeded
        assertEquals("You bought a fishing net by $500\n", m1.tryFishingNet());
        assertEquals(1500.0, f1.getBalance());
        assertTrue(f1.hasFishingNet());

        //purchase failed
        f1.setFishingNet(false);
        assertFalse(f1.hasFishingNet());
        f1.spend(1500);
        assertEquals(0, f1.getBalance());
        assertEquals("You don't have enough money", m1.tryFishingNet());
    }

    @Test
    void tryFishingLicenseTest() {
        //purchase succeeded
        assertEquals("You bought a fishing license by $120\n", m1.tryFishingLicense());
        assertEquals(1880.0, f1.getBalance());
        assertTrue(f1.hasFishingLicense());

        //no enough money
        f1.setFishingLicense(false);
        assertFalse(f1.hasFishingLicense());
        f1.spend(1870.0);
        assertEquals(10, f1.getBalance());
        assertEquals("You don't have enough money", m1.tryFishingLicense());
        assertFalse(f1.hasFishingLicense());
        assertEquals(10, f1.getBalance());

        //unpaid fine > 0
        reg1.addFine();
        assertEquals(200, reg1.getUnPaidFine());
        assertEquals("Clear the fine before you buy a new one.\n", m1.buy("license"));
        assertFalse(f1.hasFishingLicense());
        assertEquals(10, f1.getBalance());
    }

    //the method calls in this method has already been tested above, so I will focus on only the branches in this test
    //with default value given by the @BeforeEach
    @Test
    void buyTest() {
        assertEquals("You bought a fishing rod by $200\n", m1.buy("fishing rod"));
        assertEquals("You bought a fishing net by $500\n", m1.buy("fishing net"));
        assertEquals("You bought a fishing license by $120\n", m1.buy("license"));
        assertEquals("The required product is not in store now\n", m1.buy("kkkk"));
    }

    @Test
    void fisherSetup() {
        assertEquals("fisher1", f1.getName());
        assertEquals(2000.00, f1.getBalance());
        assertFalse(f1.hasFishingRod());
        assertFalse(f1.hasFishingNet());
        assertFalse(f1.hasFishingLicense());
    }

    @Test
    void inventorySetup() {
        assertEquals(fish1, inventory.getFish(0));
        assertEquals(1, inventory.getFishAmount());
    }

    @Test
    void regulationSetup() {
        assertEquals(11, reg1.getCapturedAmount());
        assertEquals(0, reg1.getUnPaidFine());
        assertEquals(0, reg1.getViolationTimes());
        assertEquals(10, reg1.getDailyQuota());

        assertEquals(f1, reg1.getFisher());
        assertEquals(2000.0, f1.getBalance());
        assertFalse(f1.hasFishingLicense());
    }
}
