package NMZ.Nodes;

import NMZ.NMZ;
import NMZ.Node;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;

public class PrayerPotion extends Node {
    private final String potion1 = "Prayer potion(1)";
    private final String potion2 = "Prayer potion(2)";
    private final String potion3 = "Prayer potion(3)";
    private final String potion4 = "Prayer potion(4)";

    public PrayerPotion(NMZ main) {
        super(main);
    }

    public boolean hasPots(){
        return c.getInventory().contains(potion1) || c.getInventory().contains(potion2) || c.getInventory().contains(potion3) || c.getInventory().contains(potion4);
    }

    @Override
    public boolean validate() {
        return c.getSkills().getBoostedLevels(Skill.PRAYER) < Calculations.random(25,32) && hasPots();
    }

    @Override
    public int execute() {
        if(!hasPots()){
            c.log("Ran out of Prayer pots. Stopping script.");
            c.stop();
        }
        if(hasPots()) {
            if (c.getInventory().contains(potion1))
                c.getInventory().interact(potion1, "Drink");
            if (c.getInventory().contains(potion2))
                c.getInventory().interact(potion2, "Drink");
            if (c.getInventory().contains(potion3))
                c.getInventory().interact(potion3, "Drink");
            if (c.getInventory().contains(potion4))
                c.getInventory().interact(potion4, "Drink");
        }
        return 0;
    }
}
