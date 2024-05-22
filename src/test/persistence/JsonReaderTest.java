package persistence;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonWriterTest {

    @Test
    void testReaderNonExistentFile() {
        try {
            JsonReader.readFisher("./data/noSuchFile.json");
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }

        try {
            JsonReader.readRegulation("./data/noSuchFile.json");
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }

        try {
            JsonReader.readInventory("./data/noSuchFile.json");
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }
}