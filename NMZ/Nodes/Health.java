package NMZ.Nodes;

import NMZ.NMZ;
import NMZ.Node;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;

public class Health extends Node {
    private int overloadHealth = 50;
    //private int guzzleRockCakeHealth = 1;

    private final String oPotion1 = "Overload (1)";
    private final String oPotion2 = "Overload (2)";
    private final String oPotion3 = "Overload (3)";
    private final String oPotion4 = "Overload (4)";

    private final String rPotion1 = "Super ranging (1)";
    private final String rPotion2 = "Super ranging (2)";
    private final String rPotion3 = "Super ranging (3)";
    private final String rPotion4 = "Super ranging (4)";

    private final String rockCake = "Dwarven rock cake";

    private int health = c.getSkills().getBoostedLevels(Skill.HITPOINTS);

    public Health(NMZ main) {
        super(main);
    }

    public boolean hasPots(){
        return c.getInventory().contains(oPotion1) || c.getInventory().contains(oPotion2) ||c.getInventory().contains(oPotion3) ||c.getInventory().contains(oPotion4);
    }

    public boolean hasRangePots(){
        return c.getInventory().contains(rPotion1) || c.getInventory().contains(rPotion2) ||c.getInventory().contains(rPotion3) ||c.getInventory().contains(rPotion4);
    }

    @Override
    public boolean validate() {
        return c.getSkills().getBoostedLevels(Skill.HITPOINTS) > 1 && hasPots(); //|| c.rangeTimer > 300;
    }

    @Override
    public int execute() {
        c.log("Reducing health.");
        c.log("Current Health:" + health);
        if(c.getSkills().getBoostedLevels(Skill.HITPOINTS) > overloadHealth && hasPots()) {
            if(c.getInventory().contains(oPotion1))
                c.getInventory().interact(oPotion1, "Drink");
            if(c.getInventory().contains(oPotion2))
                c.getInventory().interact(oPotion2, "Drink");
            if(c.getInventory().contains(oPotion3))
                c.getInventory().interact(oPotion3, "Drink");
            if(c.getInventory().contains(oPotion4))
                c.getInventory().interact(oPotion4, "Drink");
            c.sleep(Calculations.random(7302,10230));
        }else if(c.rangeTimer > 300 && hasRangePots()){
            if(c.getInventory().contains(rPotion1))
                c.getInventory().interact(rPotion1, "Drink");
            if(c.getInventory().contains(rPotion2))
                c.getInventory().interact(rPotion2, "Drink");
            if(c.getInventory().contains(rPotion3))
                c.getInventory().interact(rPotion3, "Drink");
            if(c.getInventory().contains(rPotion4))
                c.getInventory().interact(rPotion4, "Drink");
            c.rangeTimer = 0;
            c.sleep(Calculations.random(707,970));
        } else {
            c.getInventory().interact(rockCake, "Guzzle");
        }
        return 0;
    }
}
