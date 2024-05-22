package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.Fish;
import model.Fisher;
import model.Inventory;
import model.Regulation;
import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {

    private JsonReader() {
        //do nothing
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: reads source file as string and returns it
    private static String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //EFFECTS: read regulation from the file
    public static Regulation readRegulation(String source) throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRegulation(jsonObject);
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: parses regulation from JSON object and returns it
    private static Regulation parseRegulation(JSONObject jsonObject) {
        int violationTimes = jsonObject.getInt("violationTimes");
        int capturedAmount = jsonObject.getInt("capturedAmount");
        Double unpaidFine = jsonObject.getDouble("unpaidFine");
        return new Regulation(violationTimes, capturedAmount, unpaidFine);
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //EFFECTS: read the fisher file
    public static Fisher readFisher(String source) throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseFisher(jsonObject);
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    //EFFECTS: parses fisher from JSON object and returns it
    private static Fisher parseFisher(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Double balance = jsonObject.getDouble("balance");
        boolean fishingRod = jsonObject.getBoolean("rod");
        boolean fishingNet = jsonObject.getBoolean("net");
        boolean fishingLicense = jsonObject.getBoolean("license");

        return new Fisher(name, balance, fishingRod, fishingNet, fishingLicense);
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: reads inventory from file and returns it;
    // throws IOException if an error occurs reading data from file
    public static Inventory readInventory(String source) throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseInventory(jsonObject);
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: parses inventory from JSON object and returns it
    private static Inventory parseInventory(JSONObject jsonObject) {
        Inventory fs = new Inventory();
        addFishStorage(fs, jsonObject);
        return fs;
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: fs
    // EFFECTS: parses fishStorage from JSON object and adds them to the game
    private static void addFishStorage(Inventory fs, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("fishStorage");
        for (Object json : jsonArray) {
            JSONObject nextThingy = (JSONObject) json;
            addSingleFish(fs, nextThingy);
        }
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: fs
    // EFFECTS: parses fish from JSON object and adds it to the storage
    private static void addSingleFish(Inventory fs, JSONObject jsonObject) {
        String breed = jsonObject.getString("breed");
        Double size = jsonObject.getDouble("size");
        Double maxSize = jsonObject.getDouble("maxSize");
        Double unitPrice = jsonObject.getDouble("unitPrice");
        Fish fish = new Fish(breed, size, maxSize, unitPrice);
        fs.addFish(fish);
    }
}
