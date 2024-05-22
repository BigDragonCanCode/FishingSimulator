package model;

//In the market player can buy license, tools, and sell retained fish etc.
public class Market {
    private Fisher fisher;
    private Inventory inventory;
    private Regulation reg;

    //EFFECTS: create the market with given fisher, inventory, and regulation.
    public Market(Fisher fisher, Inventory inventory, Regulation reg) {
        this.fisher = fisher;
        this.inventory = inventory;
        this.reg = reg;
        EventLog.getInstance().logEvent(new Event("Set up a market."));
    }

    //MODIFIES: this
    //EFFECTS: buy tools if the fisher has enough money and unpaid fine cleared (for license only),
    //if the required tool is not in store, return a message to tell the fisher.
    public String buy(String tool) {
        if (tool.equals("fishing rod")) {
            return tryFishingRod();
        } else if (tool.equals("fishing net")) {
            return tryFishingNet();
        } else if (tool.equals("license")) {
            return tryFishingLicense();
        }
        return "The required product is not in store now\n";
    }

    //MODIFIES: this
    //EFFECTS: buy fishing rod if has enough money, return a message to tell whether the transaction succeeded or not.
    public String tryFishingRod() {
        if (fisher.spend(200.00)) {
            fisher.setFishingRod(true);
            EventLog.getInstance().logEvent(new Event("bought a fishing rod."));
            return "You bought a fishing rod by $200\n";
        }
        EventLog.getInstance().logEvent(new Event("buying failed."));
        return "You don't have enough money";
    }

    //MODIFIES: this
    //EFFECTS: buy fishing net if has enough money, return a message to tell whether the transaction succeeded or not.
    public String tryFishingNet() {
        if (fisher.spend(500.00)) {
            fisher.setFishingNet(true);
            EventLog.getInstance().logEvent(new Event("bought a fishing net."));
            return "You bought a fishing net by $500\n";
        }
        EventLog.getInstance().logEvent(new Event("buying failed."));
        return "You don't have enough money";
    }

    //MODIFIES: this
    //EFFECTS: buy license if has enough money and no unpaid fine,
    //return a message to tell whether the transaction succeeded or not.
    public String tryFishingLicense() {
        if (reg.getUnPaidFine() > 0) {
            EventLog.getInstance().logEvent(new Event("buying failed."));
            return "Clear the fine before you buy a new one.\n";
        }
        if (fisher.spend(120.00)) {
            fisher.setFishingLicense(true);
            EventLog.getInstance().logEvent(new Event("bought a license."));
            return "You bought a fishing license by $120\n";
        }
        EventLog.getInstance().logEvent(new Event("buying failed."));
        return "You don't have enough money";
    }

    //REQUIRES: fishInd has a matching fish
    //EFFECTS: add the calculated price to the balance.
    public String sell(int fishInd) {
        Fish fish = inventory.getFish(fishInd);
        double price = fish.calculatePrice();
        EventLog.getInstance().logEvent(new Event("fish sold."));
        return fisher.earn(price);
    }
}
