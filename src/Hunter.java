/**
 * Hunter Class<br /><br />
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Hunter {
    //instance variables
    private String hunterName;
    private String[] kit;
    private int gold;
    private boolean dug;
    private TreasureHunter treasureHunter;

    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param hunterName The hunter's name.
     * @param startingGold The gold the hunter starts with.
     */
    public Hunter(String hunterName, int startingGold, TreasureHunter treasure) {
        this.hunterName = hunterName;
        this.treasureHunter = treasure;
        if(treasureHunter.isSamuraiMode()) {
            kit = new String[8]; // only 5 possible items can be stored in kit
        }else{
            kit = new String[7];
        }
        gold = startingGold;
    }

    //Accessors
    public String getHunterName() {
        return hunterName;
    }

    public int getGold() {
        return gold;
    }

    public void setDug(boolean dug){
        this.dug = dug;
    }

    /**
     * Updates the amount of gold the hunter has.
     *
     * @param modifier Amount to modify gold by.
     */
    public void changeGold(int modifier) {
        gold += modifier;
        if (gold < 0) {
            gold = 0;
        }
    }

    public void digGold() {
        double rnd = Math.random();
        int rnd2 =(int) (Math.random() * 20) + 1;
        if (hasItemInKit("shovel")) {
            if(!dug) {
                if (rnd < .5) {
                    System.out.println("You dug up " + rnd2 + " gold");
                    gold += rnd2;
                    dug = true;
                } else {
                    System.out.println("You dug but only found dirt");
                }
            }else{
                System.out.println("You already dug gold for in this town");
            }
        }else{
            System.out.println("You cant dig for gold without a shovel");
        }
    }

    /**
     * Buys an item from a shop.
     *
     * @param item The item the hunter is buying.
     * @param costOfItem The cost of the item.
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem) {
        if(treasureHunter.isHasSword()){
            addItem(item);
            return true;
        }else {
            if (costOfItem == -10 || gold < costOfItem || hasItemInKit(item)) {
                return false;
            }
        }
        gold -= costOfItem;
        addItem(item);
        return true;
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item The item being sold.
     * @param buyBackPrice the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= 0 || !hasItemInKit(item)) {
            return false;
        }
        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    /**
     * Removes an item from the kit by setting the index of the item to null.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item) {
        int itmIdx = findItemInKit(item);

        // if item is found
        if (itmIdx >= 0) {
            kit[itmIdx] = null;
        }
    }

    /**
     * Checks to make sure that the item is not already in the kit.
     * If not, it assigns the item to an index in the kit with a null value ("empty" position).
     *
     * @param item The item to be added to the kit.
     * @return true if the item is not in the kit and has been added.
     */
    private boolean addItem(String item) {
        if (!hasItemInKit(item)) {
            int idx = emptyPositionInKit();
            kit[idx] = item;
            return true;
        }
        return false;
    }

    /**
     * Checks if the kit Array has the specified item.
     *
     * @param item The search item
     * @return true if the item is found.
     */
    public boolean hasItemInKit(String item) {
        for (String tmpItem : kit) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a printable representation of the inventory, which
     * is a list of the items in kit, with a space between each item.
     *
     * @return The printable String representation of the inventory.
     */
    public String getInventory() {
        String printableKit = "";
        String space = " ";

        for (String item : kit) {
            if (item != null) {
                printableKit += item + space;
            }
        }
        return printableKit;
    }

    /**
     * @return A string representation of the hunter.
     */
    public String infoString() {
        String str = hunterName + " has " + Colors.YELLOW + gold + Colors.RESET + " gold";
        if (!kitIsEmpty()) {
            str += " and " + Colors.PURPLE + getInventory() + Colors.RESET;
        }
        return str;
    }

    /**
     * Searches kit Array for the index of the specified value.
     *
     * @param item String to look for.
     * @return The index of the item, or -1 if not found.
     */
    private int findItemInKit(String item) {
        for (int i = 0; i < kit.length; i++) {
            String tmpItem = kit[i];

            if (item.equals(tmpItem)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if the kit is empty - meaning all elements are null.
     *
     * @return true if kit is completely empty.
     */
    private boolean kitIsEmpty() {
        for (String string : kit) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the first index where there is a null value.
     *
     * @return index of empty index, or -1 if not found.
     */
    private int emptyPositionInKit() {
        for (int i = 0; i < kit.length; i++) {
            if (kit[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public void fullKit() {
        String[] newkit = {"water", "rope", "machete", "horse", "boat", "boot"};
        kit = newkit;
    }


}
