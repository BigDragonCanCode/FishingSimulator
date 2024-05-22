package model;

import org.json.JSONObject;

import java.util.Random;

//The fish class provides access to breed, size, maximum size of the breed, and unit price of the breed.
//To be more realistic, the size is randomized by the algorithm.
public class Fish {
    private String breed;
    private double size;
    private double maxSize;
    private double unitPrice;
    private Random rand;

    //EFFECTS: create a fish with all information randomized.
    public Fish() {
        rand = new Random();
        randomBreed();
        EventLog.getInstance().logEvent(new Event("Generated fish: " + breed + ", size: " + size));
    }

    //EFFECTS: construct the fish object with given information
    //         designed for load function
    public Fish(String breed, double size, double maxSize, double unitPrice) {
        this.breed = breed;
        this.size = size;
        this.maxSize = maxSize;
        this.unitPrice = unitPrice;
        EventLog.getInstance().logEvent(new Event("Generated fish: " + breed + ", size: " + size));
    }

    //MODIFIES: this
    //EFFECTS: randomize the breed, and update the max size and unit price according to the breed.
    public void randomBreed() {
        int i = rand.nextInt(2);
        setBreed(i);
        randomSize();
    }

    //MODIFIES: this
    //EFFECTS: sets the breed-specific info
    public void setBreed(int i) {
        if (i == 0) {
            breed = "Salmon";
            maxSize = 76;
            unitPrice = 1.99;
        } else {
            breed = "Steelhead";
            unitPrice = 3.99;
            maxSize = 86;
        }
    }

    //MODIFIES: this
    //EFFECTS: return a fish size that is less than the max size.
    public void randomSize() {
        size = 1 + rand.nextDouble() * (maxSize - 1);
        size = Double.parseDouble(String.format("%.2f", size));
    }

    //EFFECTS: return the selling price in 2 decimal places.
    public double calculatePrice() {
        double price = unitPrice * size;
        return Double.parseDouble(String.format("%.2f", price));
    }

    public String getBreed() {
        return breed;
    }

    public double getSize() {
        return size;
    }

    public double getMaxSize() {
        return maxSize;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("breed", breed);
        json.put("size", size);
        json.put("maxSize", maxSize);
        json.put("unitPrice", unitPrice);
        return json;
    }
}
