package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FishTest {
    private Fish f1;


    @BeforeEach
    void setup() {
        f1 = new Fish();
    }

    @Test
    void randomBreedTest() {
        assertTrue(0.00 <= f1.getSize() && f1.getSize() <= f1.getMaxSize());
    }

    @Test
    void setBreedTest() {
        f1.setBreed(0);
        assertEquals("Salmon", f1.getBreed());
        assertEquals(76, f1.getMaxSize());
        assertEquals(1.99, f1.getUnitPrice());

        f1.setBreed(1);
        assertEquals("Steelhead", f1.getBreed());
        assertEquals(86, f1.getMaxSize());
        assertEquals(3.99, f1.getUnitPrice());
    }

    @Test
    void randomSizeTest() {
        assertTrue(0.00 <= f1.getSize() && f1.getSize() <= f1.getMaxSize());
        assertTrue(0.00 <= f1.getSize() && f1.getSize() <= f1.getMaxSize());
        assertTrue(0.00 <= f1.getSize() && f1.getSize() <= f1.getMaxSize());
        assertTrue(0.00 <= f1.getSize() && f1.getSize() <= f1.getMaxSize());
        assertTrue(0.00 <= f1.getSize() && f1.getSize() <= f1.getMaxSize());
    }

    @Test
    void calculatePriceTest() {
        f1.setBreed(0);
        double expectedPrice = Double.parseDouble(String.format("%.2f", f1.getUnitPrice() * f1.getSize()));
        assertEquals(expectedPrice, f1.calculatePrice());
        f1.setBreed(1);
        expectedPrice = Double.parseDouble(String.format("%.2f", f1.getUnitPrice() * f1.getSize()));
        assertEquals(expectedPrice, f1.calculatePrice());
    }
}
