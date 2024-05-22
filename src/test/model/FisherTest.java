package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FisherTest {
    private Fisher fisher1;

    @BeforeEach
    void setUp() {
        fisher1 = new Fisher("fisher1");
    }

    @Test
    void ConstructorTest() {
        assertEquals("fisher1", fisher1.getName());
        assertEquals(2000.00, fisher1.getBalance());
        assertFalse(fisher1.hasFishingRod());
        assertFalse(fisher1.hasFishingNet());
        assertFalse(fisher1.hasFishingLicense());
    }

    @Test
    void earnTest() {
        //2 decimal
        assertEquals("$0.01 is added to your balance, now you have $2000.01 in total.\n",
                fisher1.earn(0.01));
        //1 decimal
        assertEquals("$0.1 is added to your balance, now you have $2000.11 in total.\n",
                fisher1.earn(0.1));
        //whole number
        assertEquals("$500.0 is added to your balance, now you have $2500.11 in total.\n",
                fisher1.earn(500));
    }

    @Test
    void spendTest() {
        //no enough money
        assertFalse(fisher1.spend(2000.01));
        assertEquals(2000.0, fisher1.getBalance());

        //just enough money
        assertTrue(fisher1.spend(2000));
        assertEquals(0.0, fisher1.getBalance());
    }

    @Test
    void spendInDecimalTest() {
        //2 decimal
        assertTrue(fisher1.spend(0.01));
        assertEquals(1999.99, fisher1.getBalance());
        //1 decimal
        assertTrue(fisher1.spend(0.1));
        assertEquals(1999.89, fisher1.getBalance());
        //whole number
        assertTrue(fisher1.spend(499));
        assertEquals(1500.89, fisher1.getBalance());

    }

    @Test
    void hasEnoughBalanceTest() {
        //no enough balance
        assertFalse(fisher1.hasEnoughBalance(2000.01));
        //just enough balance
        assertTrue(fisher1.hasEnoughBalance(2000));
        // enough balance
        assertTrue(fisher1.hasEnoughBalance(1500));
        // smallest amount boundary case
        assertTrue(fisher1.hasEnoughBalance(0.01));
    }

    @Test
    void setFishingRodTest() {
        fisher1.setFishingRod(true);
        assertTrue(fisher1.hasFishingRod());
        fisher1.setFishingRod(false);
        assertFalse(fisher1.hasFishingRod());
    }

    @Test
    void setFishingNetTest() {
        fisher1.setFishingNet(true);
        assertTrue(fisher1.hasFishingNet());
        fisher1.setFishingNet(false);
        assertFalse(fisher1.hasFishingNet());
    }

    @Test
    void setFishingLicenseTest() {
        fisher1.setFishingLicense(true);
        assertTrue(fisher1.hasFishingLicense());
        fisher1.setFishingLicense(false);
        assertFalse(fisher1.hasFishingLicense());
    }
}
