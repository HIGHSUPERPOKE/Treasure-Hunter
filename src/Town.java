/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private String townTreasure;
    private boolean townSearched;
    private String[] treasure;
    private TreasureHunter treasureHunter;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, TreasureHunter treasureHunter) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.treasureHunter = treasureHunter;

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        townSearched = false;
        treasure = new String[3];
        townTreasure();
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item;
            }
            hunter.setDug(false);
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        printMessage = shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (treasureHunter.isHasSword()){
            System.out.println("the brawler, seeing your sword, realizes he picked a losing fight and gives you his gold");
            hunter.changeGold((int) (Math.random() * 10 + 1));
        } else {
            if (toughTown) {
                noTroubleChance = 0.66;
            } else {
                noTroubleChance = 0.33;
            }
            if (Math.random() > noTroubleChance) {
                printMessage = "You couldn't find any trouble";
            } else {
                printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
                int goldDiff = (int) (Math.random() * 10) + 1;
                if (Math.random() > noTroubleChance) {
                    printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold." + Colors.RESET;
                    printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + " gold.";
                    hunter.changeGold(goldDiff);
                } else {
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + Colors.RESET;
                    printMessage += "\nYou lost the brawl and pay " + goldDiff + " gold.";
                    hunter.changeGold(-goldDiff);
                }
            }
        }
    }
    public String infoString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < .166) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < .332) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < .498) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < .664) {
            return new Terrain("Desert", "Water");
        } else if (rnd < .83){
            return new Terrain("Jungle", "Machete");
        }else{
            return new Terrain("Marsh", "Boot");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }

    public void setTownSearched(boolean searched){
        townSearched = searched;
    }

    public boolean isTownSearched() {
        return townSearched;
    }

    public String getTownTreasure(){
        return townTreasure;
    }

    public void townTreasure(){
        double rnd = Math.random();
        if(rnd < .25){
            townTreasure = "crown";
        }else if(rnd < .5){
            townTreasure = "gem";
        }else if(rnd < .75){
            townTreasure = "trophy";
        }else{
            townTreasure = "dust";
        }
    }
}
