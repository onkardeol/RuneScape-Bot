package NMZ.Nodes;

import NMZ.NMZ;
import NMZ.Node;
import org.dreambot.api.methods.Calculations;

public class Absorption extends Node {
    private final String aPotion1 = "Absorption (1)";
    private final String aPotion2 = "Absorption (2)";
    private final String aPotion3 = "Absorption (3)";
    private final String aPotion4 = "Absorption (4)";

    public Absorption(NMZ main) {
        super(main);
    }

    public boolean hasPots(){
        return c.getInventory().contains(aPotion1) || c.getInventory().contains(aPotion2) ||c.getInventory().contains(aPotion3) ||c.getInventory().contains(aPotion4);
    }

    @Override
    public boolean validate() {
        return c.absorptionPoints < 500 && hasPots();
    }

    @Override
    public int execute() {
//        if(!hasPots()){
//            c.log("Ran out of Absorption pots. Stopping script.");
//            c.stop();
//        }
        while(c.absorptionPoints < 900 && hasPots()){
            if(c.getInventory().contains(aPotion1))
                c.getInventory().interact(aPotion1, "Drink");
            if(c.getInventory().contains(aPotion2))
                c.getInventory().interact(aPotion2, "Drink");
            if(c.getInventory().contains(aPotion3))
                c.getInventory().interact(aPotion3, "Drink");
            if(c.getInventory().contains(aPotion4))
                c.getInventory().interact(aPotion4, "Drink");
            c.absorptionPoints += 50;
            c.sleepUntil(() -> !c.getLocalPlayer().isAnimating(), Calculations.random(580,950));
        }
        return 0;
    }
}
