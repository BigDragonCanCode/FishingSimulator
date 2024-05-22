package model;

import org.json.JSONObject;

//We create a fisher with name, balance, and basic status to make sure they can go fishing.
public class Fisher {
    private String name;
    private double balance;
    private boolean fishingRod;
    private boolean fishingNet;
    private boolean fishingLicense;

    //EFFECTS: create a fisher with given name, $2000.00 balance (2 decimal place maximum), and no tools.
    public Fisher(String name) {
        this.name = name;
        balance = 2000.00;
        fishingRod = false;
        fishingNet = false;
        fishingLicense = false;
        EventLog.getInstance().logEvent(new Event("Generated fisher: " + name));
    }

    public Fisher(String name, double balance, boolean fishingRod, boolean fishingNet, boolean fishingLicense) {
        this.name = name;
        this.balance = balance;
        this.fishingRod = fishingRod;
        this.fishingNet = fishingNet;
        this.fishingLicense = fishingLicense;
        EventLog.getInstance().logEvent(new Event("Generated fisher: " + name));
    }

    //REQUIRES: amount > 0, 2 decimal place maximum
    //MODIFIES: this
    //EFFECTS: add money to balance, and return a confirming sentence.
    public String earn(double amount) {
        balance += amount;
        balance = Double.parseDouble(String.format("%.2f", balance));
        EventLog.getInstance().logEvent(new Event("Added money: $" + amount));
        return "$" + amount + " is added to your balance, now you have $"
                + getBalance() + " in total.\n";
    }

    //REQUIRES: amount > 0, 2 decimal place maximum
    //MODIFIES: this
    //EFFECTS: return true if transaction is successful, otherwise false.
    public boolean spend(double amount) {
        if (hasEnoughBalance(amount)) {
            balance -= amount;
            balance = Double.parseDouble(String.format("%.2f", balance));

            EventLog.getInstance().logEvent(new Event("spent money: $" + amount));
            return true;
        }
        return false;
    }

    //REQUIRES: amount > 0, 2 decimal place maximum
    //EFFECTS: return true if the account has balance greater than or equal to the required amount.
    public boolean hasEnoughBalance(double amount) {
        return balance >= amount;
    }

    //MODIFIES: this
    //EFFECTS: set the status of fishing rod by given value
    public void setFishingRod(boolean set) {
        fishingRod = set;
    }

    //MODIFIES: this
    //EFFECTS: set the status of fishing net by given value
    public void setFishingNet(boolean set) {
        fishingNet = set;
    }

    //MODIFIES: this
    //EFFECTS: set the status of fishing license by given value
    public void setFishingLicense(boolean set) {
        fishingLicense = set;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public boolean hasFishingRod() {
        return fishingRod;
    }

    public boolean hasFishingNet() {
        return fishingNet;
    }

    public boolean hasFishingLicense() {
        return fishingLicense;
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("balance", balance);
        json.put("rod", fishingRod);
        json.put("net", fishingNet);
        json.put("license", fishingLicense);
        return json;
    }
}
