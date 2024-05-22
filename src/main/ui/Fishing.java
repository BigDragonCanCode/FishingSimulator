package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

//the fishing game with inventory, market, fishing, fine functions,
// all model classes are used together to make the game working.
public class Fishing {
    private Fisher fisher;
    private Inventory inventory;
    private Scanner scanner;
    private String input;
    private Market market;
    private Regulation reg;

    //EFFECTS: start the game with inventory, regulation, market, fisher, and an input scanner.
    public Fishing(String name) {
        scanner = new Scanner(System.in);
        System.out.println("Do you want to load the previous saved game?: (yes / no)");
        input = scanner.nextLine();
        if (input.equals("yes")) {
            load();
        } else {
            fisher = new Fisher(name);
            inventory = new Inventory();
            reg = new Regulation(fisher);
        }
        market = new Market(fisher, inventory, reg);
        runGame();
        saveGame();
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void load() {
        try {
            fisher = JsonReader.readFisher("./data/fisher.json");
            inventory = JsonReader.readInventory("./data/inventory.json");
            reg = JsonReader.readRegulation("./data/regulation.json");
            reg.addFisher(fisher);
            System.out.println("Files loaded!");

        } catch (IOException e) {
            System.out.println("Unable to read from file.");
        }
    }

    // EFFECTS: saves the workroom to file
    private void saveGame() {
        System.out.println("Do you want to save the game before quitting?: (yes / no)");
        input = scanner.nextLine();
        if (input.equals("yes")) {
            try {
                JsonWriter.open("./data/fisher.json",
                                "./data/inventory.json",
                                "./data/regulation.json");
                JsonWriter.writeInventory(inventory);
                JsonWriter.writeFisher(fisher);
                JsonWriter.writeRegulation(reg);
                JsonWriter.close();
                System.out.println("Game sucessfully saved!");
            } catch (FileNotFoundException e) {
                System.out.println("Unable to write to file");
            }
        } else {
            System.out.println("You chose to not save the progress, goodbye!");
        }
    }



    //MODIFIES: this
    //EFFECTS: the main menu of the game with fishing, inventory, market, start a new day, quit functions.
    //Invalid inputs are realized.
    public void runGame() {
        printStatus();
        input = askingUserInput("what do you want to do? (fishing / inventory / market / new day / quit):");
        while (!input.equals("quit")) {
            if (input.equals("fishing")) {
                fishingHelper();
            } else if (input.equals("market")) {
                goToMarket();
            } else if (input.equals("inventory")) {
                openInventory();
            } else if (input.equals("new day")) {
                reg.startNewDay();
            } else {
                inputNotValid();
            }
            printStatus();
            input = askingUserInput("what do you want to do? (fishing / inventory / market / new day / quit): ");
        }
    }

    //EFFECTS: go fishing if the fisher has the license, otherwise return to main menu with a message.
    public void fishingHelper() {
        if (!fisher.hasFishingLicense()) {
            System.out.println("please buy a fishing license before proceed!\n");
        } else {
            goFishing();
        }
    }

    //MODIFIES: this
    //EFFECTS: choose different fishing methods or go back to main menu.
    public void goFishing() {
        input = askingUserInput("angling or net? (angling / net / main menu)");
        while (!input.equals("main menu")) {
            if (input.equals("angling")) {
                useFishingRod();
            } else if (input.equals("net")) {
                useFishingNet();
            } else {
                inputNotValid();
            }
            input = askingUserInput("angling or net? (angling / net / main menu)");
        }
    }

    //EFFECTS: if the fisher does not have a fishing rod, go back to main menu. Otherwise, the fisher can get 1 fish.
    public void useFishingRod() {
        if (!fisher.hasFishingRod()) {
            System.out.println("You don't have a fishing rod, please purchase one!\n");
        } else {
            trying(1);
        }
    }

    //EFFECTS: if the fisher does not have a fishing net, go back to main menu. Otherwise, the fisher can get 3 fish.
    public void useFishingNet() {
        if (!fisher.hasFishingNet()) {
            System.out.println("You don't have a fishing net, please purchase one!\n");
        } else {
            trying(3);
        }
    }

    //EFFECTS: every time this method is called, the fisher gets 1 fish, and they can look at the notification
    //to decide whether to retain the fish or return it back to the water.
    public void trying(int times) {
        for (int i = 0; i < times; i++) {
            Fish fish = new Fish();
            keepOrNot(fish);
            violationNotifier();
        }
    }

    //MODIFIES: this
    //EFFECTS: the decision of retain or return to water is made here. Daily capture amount is updated.
    //Invalid inputs are realized.
    public void keepOrNot(Fish fish) {
        input = askingUserInput("You just captured a fish, keep or not? (yes / no)");
        if (input.equals("yes")) {
            inventory.addFish(fish);
            System.out.println("Fish successfully added to your inventory!\n");
            reg.addCaptureAmount(1);
            checkViolation();

        } else if (input.equals("no")) {
            System.out.println("You returned the fish to the water!\n");
        } else {
            inputNotValid();
            keepOrNot(fish);
        }
    }

    //EFFECTS: this method prints notification of: whether the fisher violated the daily quota,
    //whether the license is revoked, how much fine is added, and how much unpaid fine does the fisher has in total.
    //Also, return to the main menu when the license is revoked.
    public void checkViolation() {
        if (reg.addFine()) {
            System.out.println("You have exceeded the daily quota, $200 fine has been added.");
            System.out.println("Unpaid fine is now: $" + reg.getUnPaidFine() + "\n");
        }
        if (reg.revokeLicense()) {
            System.out.println("There are 5 violation record under the current license,"
                    + " and the limit is reached. License is revoked.\n");
            runGame();
        }
    }

    //EFFECTS: print a notification about how many fish captured, and how many times the player has violated the rule.
    public void violationNotifier() {
        System.out.println("You have retained " + inventory.getFishAmount() + " fish today,"
                + " the daily quota is: " + reg.getDailyQuota());
        System.out.println("There are " + reg.getViolationTimes() + " violations under the current license\n");
    }

    //MODIFIES: this
    //EFFECTS: The main method for market. The fisher can buy different tools and license,
    // and sell retained fish, pay the fine, or return to the main menu.
    //Invalid inputs are realized.
    public void goToMarket() {
        input = askingUserInput("buying or selling? (buy / sell / pay fine / main menu)");
        while (!input.equals("main menu")) {
            if (input.equals("buy")) {

                input = askingUserInput("What do you want to buy? (fishing rod / fishing net / license)");
                System.out.println(market.buy(input));
            } else if (input.equals("sell")) {
                printFishStorage();
                input = askingUserInput("Enter the fish number which you want to sell: (cancel)");
                if (isValidFishNum(input)) {
                    System.out.println(market.sell(Integer.parseInt(input)));
                }
            } else if (input.equals("pay fine")) {
                payFine();
            } else {
                inputNotValid();
            }
            input = askingUserInput("buying or selling? (buy / sell / pay fine / main menu)");
        }
    }

    //REQUIRES: input is a number, and input > 0
    //MODIFIES: this
    //EFFECTS: pay the fine with given amount.
    public void payFine() {
        input = askingUserInput("Unpaid fine: $" + reg.getUnPaidFine() + ". How much do you want to pay?");
        reg.payFine(Double.parseDouble(input));
    }

    //EFFECTS: return false if the fish number cannot be found or the fisher decide to not sell. Otherwise, return true.
    public boolean isValidFishNum(String input) {
        if (input.equals("cancel")) {
            return false;
        } else if (!inventory.isFishInRange(Integer.parseInt(input))) {
            System.out.println("Fish number does not exist.\n");
            return false;
        }
        return true;
    }

    //MODIFIES: this
    //EFFECTS: the main method for inventory. The fisher can look at fish storage and return unwanted fish to water.
    //Invalid inputs are realized.
    public void openInventory() {
        input = askingUserInput("view fish / release fish / main menu");
        while (!input.equals("main menu")) {
            if (input.equals("view fish")) {
                printFishStorage();
            } else if (input.equals("release fish")) {
                printFishStorage();
                input = askingUserInput("Enter the fish number you want to release: (cancel)");
                if (isValidFishNum(input)) {
                    inventory.removeFish(Integer.parseInt(input));
                    System.out.println("You returned the fish to water!");
                }
            } else {
                inputNotValid();
            }
            input = askingUserInput("view fish / release fish / main menu");
        }
    }


    //EFFECTS: prints the fish storage in the format of :fish number, breed, size, unit price, final price.
    //The list is printed in time order.
    public void printFishStorage() {
        printDashedLine();
        System.out.println("Fish storage record:\n");
        for (int i = 0; i < inventory.getFishAmount(); i++) {
            Fish fish = inventory.getFish(i);
            System.out.println("Fish number: " + i + " | Fish breed: " + fish.getBreed()
                    + " | Size: " + fish.getSize() + " | Unit price:" + fish.getUnitPrice());
            System.out.println("Final price will be: $" + fish.calculatePrice() + "\n");
        }
        printDashedLine();
    }

    //EFFECTS: print the player data with name, balance, daily capture amount and whether the daily quota is reached.
    public void printStatus() {
        printDashedLine();
        System.out.println("Status: ");
        System.out.println("Name: " + fisher.getName() + " | Current balance: $" + fisher.getBalance()
                + " | # fish captured today: " + reg.getCapturedAmount());
        System.out.println("Is the player reached the daily quota?: " + reg.isOverLimit());
        System.out.println("Current violation times: " + reg.getViolationTimes());
        printDashedLine();

    }

    //EFFECTS: asking input with given question, and return the collected input.
    public String askingUserInput(String asking) {
        System.out.println(asking);
        return scanner.nextLine();
    }

    //EFFECTS: print the dashed line to make players easier to look at the notifications.
    public void printDashedLine() {
        System.out.println("-------------------------------------------");
    }

    //EFFECTS: tells the player the input cannot be understood by the program.
    public void inputNotValid() {
        System.out.println("Input not valid.\n");
    }
}