package persistence;

import model.Fisher;
import model.Inventory;
import model.Regulation;
import org.json.JSONObject;

import java.io.*;

// Represents a writer that writes JSON representation of workroom to file
public class JsonWriter {
    private static final int TAB = 4;
    private static PrintWriter fisherWriter;
    private static PrintWriter inventoryWriter;
    private static PrintWriter regWriter;

    private JsonWriter() {
        //do nothing
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public static void open(String f, String i, String r) throws FileNotFoundException {
        fisherWriter = new PrintWriter(f);
        inventoryWriter = new PrintWriter(i);
        regWriter = new PrintWriter(r);
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: writes JSON representation of inventory to file
    public static void writeInventory(Inventory fs) {
        JSONObject json = fs.toJson();
        inventoryWriter.print(json.toString(TAB));
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: writes JSON representation of regulation to file
    public static void writeRegulation(Regulation reg) {
        JSONObject json = reg.toJson();
        regWriter.print(json.toString(TAB));
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: writes JSON representation of fisher to file
    public static void writeFisher(Fisher fisher) {
        JSONObject json = fisher.toJson();
        fisherWriter.print(json.toString(TAB));
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: closes the writers
    public static void close() {
        fisherWriter.close();
        inventoryWriter.close();
        regWriter.close();
    }
}
