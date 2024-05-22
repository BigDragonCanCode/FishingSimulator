package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//The retained fish are stored here.
public class Inventory {
    private ArrayList<Fish> fishStorage;

    //EFFECTS: create the inventory with no fish
    public Inventory() {
        fishStorage = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Generated an inventory with no items."));
    }

    //REQUIRES: fish cannot be null
    //MODIFIES: this
    //EFFECTS: retain a captured fish to the inventory.
    public void addFish(Fish fish) {
        fishStorage.add(fish);
        EventLog.getInstance().logEvent(new Event("Fish added to the inventory."));
    }

    //REQUIRES: ind is valid fish number
    //EFFECTS: return the fish to water.
    public void removeFish(int ind) {
        fishStorage.remove(getFish(ind));
        EventLog.getInstance().logEvent(new Event("Fish removed from the inventory."));
    }

    //REQUIRES: ind >= 0
    //EFFECTS: returns true if the inventory could find the fish using the given fish number, otherwise false.
    public boolean isFishInRange(int ind) {
        return ind < getFishAmount();
    }

    public Fish getFish(int ind) {
        return fishStorage.get(ind);
    }

    public int getFishAmount() {
        return fishStorage.size();
    }

    public ArrayList<Fish> getFishStorage() {
        return fishStorage;
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("fishStorage", fishStorageToJson());
        return json;
    }

    // EFFECTS: returns things in the fishStorage as a JSON array
    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private JSONArray fishStorageToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Fish fish : fishStorage) {
            jsonArray.put(fish.toJson());
        }

        return jsonArray;
    }
}
