package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    private Inventory fs;
    private Fisher f1;
    private Regulation reg1;

    @BeforeEach
    void setup() {
        fs = new Inventory();
        f1 = new Fisher("f1");
        reg1 = new Regulation(f1);
    }

    @Test
    void openNotExistTest() {
        try {
            JsonWriter.open("./tests/noFile.json",
                            "./tests/noFile.json",
                            "./tests/noFile.json");
            fail("The tests should not pass");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    void writeDefaultTest() {
        try {
            JsonWriter.open("./data/tests/writerFisherDefault.json",
                            "./data/tests/writerInventoryDefault.json",
                            "./data/tests/writerRegulationDefault.json");
        } catch (FileNotFoundException e) {
            fail("no such file");
        }

        JsonWriter.writeInventory(fs);
        JsonWriter.writeFisher(f1);
        JsonWriter.writeRegulation(reg1);
        JsonWriter.close();

        try {
            fs = JsonReader.readInventory("./data/tests/writerInventoryDefault.json");
            assertEquals(0, fs.getFishAmount());

            f1 = JsonReader.readFisher("./data/tests/writerFisherDefault.json");
            assertEquals("f1", f1.getName());
            assertEquals(2000, f1.getBalance());
            assertFalse(f1.hasFishingRod());
            assertFalse(f1.hasFishingNet());
            assertFalse(f1.hasFishingLicense());

            reg1 = JsonReader.readRegulation("./data/tests/writerRegulationDefault.json");
            assertEquals(0, reg1.getCapturedAmount());
            assertEquals(0, reg1.getUnPaidFine());
            assertEquals(0, reg1.getViolationTimes());
        } catch (IOException e) {
            fail("IO exception");
        }
    }

    @Test
    void writeChangeTest() {
        try {
            JsonWriter.open("./data/tests/writerFisherChange.json",
                    "./data/tests/writerInventoryChange.json",
                    "./data/tests/writerRegulationChange.json");
        } catch (FileNotFoundException e) {
            fail("no such file");
        }

        fs.addFish(new Fish());
        f1.setFishingNet(true);
        f1.spend(200);
        reg1.addCaptureAmount(11);
        reg1.addFine();

        JsonWriter.writeInventory(fs);
        JsonWriter.writeFisher(f1);
        JsonWriter.writeRegulation(reg1);
        JsonWriter.close();

        try {
            fs = JsonReader.readInventory("./data/tests/writerInventoryChange.json");
            assertEquals(1, fs.getFishAmount());

            f1 = JsonReader.readFisher("./data/tests/writerFisherChange.json");
            assertEquals("f1", f1.getName());
            assertEquals(1800, f1.getBalance());
            assertFalse(f1.hasFishingRod());
            assertTrue(f1.hasFishingNet());
            assertFalse(f1.hasFishingLicense());

            reg1 = JsonReader.readRegulation("./data/tests/writerRegulationChange.json");
            assertEquals(11, reg1.getCapturedAmount());
            assertEquals(200, reg1.getUnPaidFine());
            assertEquals(1, reg1.getViolationTimes());
        } catch (IOException e) {
            fail("IO exception");
        }
    }
}