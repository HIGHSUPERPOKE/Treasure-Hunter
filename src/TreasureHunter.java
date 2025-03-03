import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private boolean normalMode;
    private boolean samuraiMode;
    private String[] treasure;


    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        easyMode = false;
        normalMode = false;
        hardMode = false;
        samuraiMode = false;
        treasure = new String[3];
    }

    public boolean isSamuraiMode(){
        return samuraiMode;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = SCANNER.nextLine().toLowerCase();

        // set hunter instance variable
        hunter = new Hunter(name, 20, this);

        System.out.print("Easy Normal Hard: (e/n/h) ");
        String mode = SCANNER.nextLine().toLowerCase();
        if (mode.equals("h")) {
            hardMode = true;
        }
        if (mode.equals("test")) {
            hardMode = false;
            hunter.changeGold(80);
            hunter.fullKit();
        }
        if (mode.equals("e")){
            easyMode = true;
            hunter.changeGold(20);

        }
        if(mode.equals("n")){
            normalMode = true;
        }

        if(mode.equals("s")){
            samuraiMode = true;
        }

    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;

            // and the town is "tougher"
            toughness = 0.75;
        }
        if(easyMode) {
            markdown = 1.00;
            toughness =0.2;

        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown, this);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, this);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";
        while (!choice.equals("x")) {
            System.out.println();
            if(treasureIsFull()){
                System.out.println("Congratulations, you have found the last of the three treasures, you win!");
                break;
            }
            System.out.println(currentTown.getLatestNews());
            if(hunter.getGold() <= 0){
                System.out.println("Game Over");
                break;
            }
            System.out.println("***");
            System.out.println(hunter.infoString());
            if(!treasureIsEmpty()){
                System.out.println("Treasure found: " + treasureList());
            }
            System.out.println(currentTown.infoString());
            System.out.println("(B)uy something at the shop.");
            System.out.println("(S)ell something at the shop.");
            System.out.println("(E)xplore surrounding terrain.");
            System.out.println("(M)ove on to a different town.");
            System.out.println("(L)ook for trouble!");
            System.out.println("(D)ig for Gold.");
            System.out.println("(H)unt for treasure.");
            System.out.println("Give up the hunt and e(X)it.");
            System.out.println();
            System.out.print("What's your next move? ");
            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice);
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("e")) {
            System.out.println(currentTown.getTerrain().infoString());
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        }else if (choice.equals(("d"))) {
            hunter.digGold();
        } else if (choice.equals("h")){
            findTreasure(currentTown.getTownTreasure());
        }
        else if (choice.equals("x")) {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        } else {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }



    private int emptyPositionInTreasure() {
        for (int i = 0; i < treasure.length; i++) {
            if (treasure[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasItemInTreasure(String item) {
        for (String tmpItem : treasure) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }

    public void findTreasure(String item) {
        if(!currentTown.isTownSearched()) {
            if (!hasItemInTreasure(item) && !currentTown.getTownTreasure().equals("dust")) {
                int idx = emptyPositionInTreasure();
                treasure[idx] = item;
                System.out.println("you found a " + currentTown.getTownTreasure() + "!");
                currentTown.setTownSearched(true);
            } else if(hasItemInTreasure(item)) {
                System.out.println("you already have a " + currentTown.getTownTreasure() + "!");
                currentTown.setTownSearched(true);
            }else{
                System.out.println("you found dust");
                currentTown.setTownSearched(true);
            }
        }else{
            System.out.println("town is already searched");
        }
    }

    public String treasureList(){
        String list = "";
        for(int i = 0; i < treasure.length; i++){
            if(treasure[i] != null){
                list += treasure[i] + " ";
            }
        }
        return list;
    }

    public boolean treasureIsEmpty() {
        for (String string : treasure) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }

    private boolean treasureIsFull() {
        for (String string : treasure) {
            if(string == null){
                return false;
            }
        }
        return true;
    }

    public boolean isHasSword(){
        for(int i = 0; i < treasure.length; i++){
            if(treasure[i] != null){
                return true;
            }
        }
        return false;
    }
}