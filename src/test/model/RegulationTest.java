package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegulationTest {
    private Regulation reg1;
    private Regulation reg2;
    private Fisher f1;

    @BeforeEach
    void setup() {
        f1 = new Fisher("fisher1");
        f1.setFishingLicense(true);
        reg1 = new Regulation(f1);
        reg1.addCaptureAmount(11);
        reg1.addFine();
        f1.spend(1801);

        reg2 = new Regulation(0, 0, 0.0);
    }

    @Test
    void ConstructorTest() {
        assertEquals(11, reg1.getCapturedAmount());
        assertEquals(200.0, reg1.getUnPaidFine());
        assertEquals(1, reg1.getViolationTimes());
        assertEquals(10, reg1.getDailyQuota());

        assertEquals(f1, reg1.getFisher());
        assertEquals(199.0, f1.getBalance());
        assertTrue(f1.hasFishingLicense());
    }

    @Test
    void payFineTest() {
        //no enough money
        assertEquals("You don't have enough money to pay.\n", reg1.payFine(200));
        assertEquals(200, reg1.getUnPaidFine());
        assertEquals(199.0, f1.getBalance());
        //able to pay, unpaid is not clear.
        assertEquals("Paid $199.0, $1.0 left to pay.\n", reg1.payFine(199));
        assertEquals(1, reg1.getUnPaidFine());
        assertEquals(0, f1.getBalance());
        //pay with the smallest amount
        f1.earn(100);
        assertEquals(100, f1.getBalance());
        assertEquals("Paid $0.01, $0.99 left to pay.\n",reg1.payFine(0.01));
        //clear unpaid amount
        assertEquals("Paid $0.99, $0.0 left to pay.\n",reg1.payFine(0.99));
    }

    @Test
    void addFine() {
        //over the limit
        assertTrue(reg1.addFine());
        assertEquals(400, reg1.getUnPaidFine());
        assertEquals(2, reg1.getViolationTimes());
        //amount < limit
        reg1.startNewDay();
        assertEquals(0, reg1.getCapturedAmount());
        assertFalse(reg1.addFine());
        assertEquals(400, reg1.getUnPaidFine());
        assertEquals(2, reg1.getViolationTimes());
        //amount == limit
        reg1.addCaptureAmount(10);
        assertEquals(10, reg1.getCapturedAmount());
        assertFalse(reg1.addFine());
        assertEquals(400, reg1.getUnPaidFine());
        assertEquals(2, reg1.getViolationTimes());
    }

    @Test
    void revokeLicenseTest() {
        // revoke time not reached.
        assertFalse(reg1.revokeLicense());
        assertTrue(f1.hasFishingLicense());
        assertEquals(1, reg1.getViolationTimes());

        // revoke time reached.
        reg1.addFine();
        reg1.addFine();
        reg1.addFine();
        reg1.addFine();
        assertEquals(5, reg1.getViolationTimes());
        assertTrue(reg1.revokeLicense());
        assertFalse(f1.hasFishingLicense());
        assertEquals(0, reg1.getViolationTimes());
    }

    @Test
    void isOverLimitTest() {
        //amount > quota
        assertTrue(reg1.isOverLimit());
        assertEquals(10, reg1.getDailyQuota());
        assertEquals(11, reg1.getCapturedAmount());
        // amount < quota
        reg1.startNewDay();
        assertFalse(reg1.isOverLimit());
        assertEquals(10, reg1.getDailyQuota());
        assertEquals(0, reg1.getCapturedAmount());
        //amount == quota
        reg1.addCaptureAmount(10);
        assertFalse(reg1.isOverLimit());
        assertEquals(10, reg1.getDailyQuota());
        assertEquals(10, reg1.getCapturedAmount());
    }

    @Test
    void addCaptureAmountTest() {
        //add single fish
        reg1.addCaptureAmount(1);
        assertEquals(12, reg1.getCapturedAmount());
        //add multiple fish
        reg1.addCaptureAmount(3);
        assertEquals(15, reg1.getCapturedAmount());
    }

    @Test
    void addFisherTest() {
        assertNull(reg2.getFisher());

        reg2.addFisher(f1);
        assertEquals(f1, reg2.getFisher());
    }
}
