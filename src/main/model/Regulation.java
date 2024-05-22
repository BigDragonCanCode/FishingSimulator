package model;

import org.json.JSONObject;

//This class represents the fishing regulation, fisher will be punished by violating the regulation.
public class Regulation {
    private static final int DAILY_QUOTA = 10;
    private static final double FINE = 200.00;
    private int capturedAmount;
    private int violationTimes;
    private double unPaidFine;

    private Fisher fisher;

    //EFFECTS: sets up the regulation with the fisher, with all status set to 0.
    public Regulation(Fisher fisher) {
        violationTimes = 0;
        capturedAmount = 0;
        unPaidFine = 0.00;
        this.fisher = fisher;
        EventLog.getInstance().logEvent(new Event("Generated a Regulation item."));
    }

    //EFFECTS: construct the regulation without the fisher
    //         this constructor is designed for load function.
    public Regulation(int violationTimes, int capturedAmount, double unPaidFine) {
        this.violationTimes = violationTimes;
        this.capturedAmount = capturedAmount;
        this.unPaidFine = unPaidFine;
    }

    //REQUIRES: amount > 0, 2 decimal maximum.
    //MODIFIES: this
    //EFFECTS: if the fisher have enough money to pay the given amount, reduct the unPaidFine by given amount
    // in two decimal place and tell the fisher how much more needed to pay. Otherwise, tell the fisher they can't pay.
    public String payFine(double amount) {
        if (fisher.spend(amount)) {
            unPaidFine -= amount;
            unPaidFine = Double.parseDouble(String.format("%.2f", unPaidFine));
            return "Paid $" + amount + ", $" + unPaidFine + " left to pay.\n";
        }

        EventLog.getInstance().logEvent(new Event("Failed to pay fine."));
        return "You don't have enough money to pay.\n";
    }

    //MODIFIES: this
    //EFFECTS: is the daily quota is violated, add the fine and violation time to the fisher, return true.
    //Otherwise, return false.
    public boolean addFine() {
        if (isOverLimit()) {
            unPaidFine += FINE;
            violationTimes++;
            EventLog.getInstance().logEvent(new Event("violation times added by 1."));
            EventLog.getInstance().logEvent(new Event("fine added by $200."));;
            return true;
        }
        return false;
    }

    //REQUIRES: violationTimes <= 5
    //MODIFIES: this
    //EFFECTS: if the fisher violated the rule 5 times, take away their license,
    // and set the violation time to 0, return true. Otherwise, return false.
    public boolean revokeLicense() {
        if (violationTimes == 5) {
            fisher.setFishingLicense(false);
            violationTimes = 0;
            EventLog.getInstance().logEvent(new Event("license revoked."));
            return true;
        }
        return false;
    }

    //EFFECTS: return true if the limit is exceeded, otherwise false.
    public boolean isOverLimit() {
        return capturedAmount > DAILY_QUOTA;
    }

    //REQUIRES: num > 0
    //MODIFIES: this
    //EFFECTS: add the capture amount by num.
    public void addCaptureAmount(int num) {
        capturedAmount += num;
        EventLog.getInstance().logEvent(new Event("daily captured amount added by " + num + "."));
    }

    //MODIFIES: this
    //EFFECTS: reset the captured amount to 0.
    public void startNewDay() {
        capturedAmount = 0;
        EventLog.getInstance().logEvent(new Event("reset the daily captured amount to 0."));
    }

    public int getCapturedAmount() {
        return capturedAmount;
    }

    public double getUnPaidFine() {
        return unPaidFine;
    }

    public int getViolationTimes() {
        return violationTimes;
    }

    public int getDailyQuota() {
        return DAILY_QUOTA;
    }

    public Fisher getFisher() {
        return fisher;
    }

    //MODIFIES: this
    //EFFECTS: add the fisher to the regulation class
    public void addFisher(Fisher fisher) {
        this.fisher = fisher;
    }

    //Reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("violationTimes", violationTimes);
        json.put("capturedAmount", capturedAmount);
        json.put("unpaidFine", unPaidFine);
        return json;
    }
}
