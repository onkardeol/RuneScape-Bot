package SandCrabFighter;


import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.PassableObstacle;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import javax.xml.crypto.dsig.spec.XPathFilterParameterSpec;
import java.awt.*;
import java.util.List;

@ScriptManifest(
        author = "Venom",
        description = "CrabFighter",
        category = Category.COMBAT,
        version = 1,
        name = "Sand Crab Fighter"
)
public class Main extends AbstractScript {

    public static final String MOB = "Sand Crab";
    public static final String FOOD = "Lobster";

    public static final int ATTACK_LVL = 65;
    public static final int STRENGTH_LVL = 65;
    public static final int DEFENCE_LVL = 70;

    private boolean defCheck = false;
    private int breakCounter = 0;
    private int breakCheck = 0;

    private final Area combatArea = new Area(1775,3469,1777,3467,0);
    private final Tile attackTile = new Tile(1751,3425,0);
    private final Area aggroArea = new Area(1776, 3424,1779,3416, 0);
    // Mainland
    //private final Tile attackTile = new Tile(1765,3468,0);
    //private final Area aggroArea = new Area(1761, 3501,1771,3499, 0);

    private long start;


    // Delay method to randomize actions
    public int getDelay(int min, int max){
        return (int)(Math.random() * (max - min) + 1) + min;
    }

    @Override
    public void onStart(){
        log("Starting Crab Fighter");
        getSkillTracker().start(true);
        start = System.currentTimeMillis();
    }

    public void newCounter(){
        breakCheck = getDelay(560, 565);
    }

    @Override
    public int onLoop() {
        if (breakCounter > breakCheck) {
            getWalking().walk(aggroArea.getRandomTile());
            if(aggroArea.contains(getLocalPlayer())){
                breakCounter = 0;
                newCounter();
                log("New Aggro Count: " + Integer.toString(breakCheck));
            }
        }

        if (getSkills().getRealLevel(Skill.ATTACK) >= ATTACK_LVL && getSkills().getRealLevel(Skill.STRENGTH) < STRENGTH_LVL && !getEquipment().isSlotEmpty(EquipmentSlot.WEAPON.getSlot())) {
            switchCombat(1, 593, 9, 4, "Strength");
        } else if (getSkills().getRealLevel(Skill.STRENGTH) >= STRENGTH_LVL && getSkills().getRealLevel(Skill.DEFENCE) < DEFENCE_LVL && !getEquipment().isSlotEmpty(EquipmentSlot.WEAPON.getSlot()) && defCheck == false) {
            switchCombat(1, 593, 17, 7, "Defence");
            defCheck = true;
        } else if (getSkills().getRealLevel(Skill.ATTACK) >= ATTACK_LVL && getSkills().getRealLevel(Skill.STRENGTH) >= STRENGTH_LVL && getSkills().getRealLevel(Skill.DEFENCE) >= DEFENCE_LVL) {
            log("Combat level achieved. Stopping script");
            stop();
            return -1;
        }

        if(getLocalPlayer().getHealthPercent() < 30 && !getInventory().contains(FOOD)){
            log("Ran out of food");
            getWalking().walk(aggroArea.getRandomTile());
            stop();
            return -1;
        }

//        if(attackTile != getLocalPlayer().getTile() && breakCounter <= breakCheck && !getLocalPlayer().isAnimating()){
//            getWalking().walk(combatArea.getCenter());
//        }


        if(getLocalPlayer().getHealthPercent() <=50){
            if(getInventory().contains(FOOD))
                getInventory().interact(FOOD, "Eat");
        } else if(!getLocalPlayer().getTile().equals(attackTile) && breakCounter <= breakCheck && !getLocalPlayer().getTile().equals(attackTile)){
            getWalking().walk(attackTile);
            sleep(Calculations.random(1042,1984));
        }

        breakCounter++;
        log(String.valueOf(breakCounter));

        return getDelay(995, 1050);
    }

    @Override
    public void onExit(){

    }

    @Override
    public void onPaint(final Graphics g) {
        g.drawString("Runtime: " + Timer.formatTime(System.currentTimeMillis() - start), 15, 45);
        g.drawString("Attack level (gained): " + getSkills().getRealLevel(Skill.ATTACK) + " (" + getSkillTracker().getGainedLevels(Skill.ATTACK) + ")", 15, 60);
        g.drawString("Attack experience gained (per hour): " + getSkillTracker().getGainedExperience(Skill.ATTACK) + " (" + getSkillTracker().getGainedExperiencePerHour(Skill.ATTACK) + ")", 15, 75);
        g.drawString("Strength level (gained): " + getSkills().getRealLevel(Skill.STRENGTH) + " (" + getSkillTracker().getGainedLevels(Skill.STRENGTH) + ")", 15, 90);
        g.drawString("Strength experience gained (per hour): " + getSkillTracker().getGainedExperience(Skill.STRENGTH) + " (" + getSkillTracker().getGainedExperiencePerHour(Skill.STRENGTH) + ")", 15, 105);
        g.drawString("Defence level (gained): " + getSkills().getRealLevel(Skill.DEFENCE) + " (" + getSkillTracker().getGainedLevels(Skill.DEFENCE) + ")", 15, 120);
        g.drawString("Defence experience gained (per hour): " + getSkillTracker().getGainedExperience(Skill.DEFENCE) + " (" + getSkillTracker().getGainedExperiencePerHour(Skill.DEFENCE) + ")", 15, 135);
        g.drawString("Hitpoints level (gained): " + getSkills().getRealLevel(Skill.HITPOINTS) + " (" + getSkillTracker().getGainedLevels(Skill.HITPOINTS) + ")", 15, 150);
        g.drawString("Hitpoints experience gained (per hour): " + getSkillTracker().getGainedExperience(Skill.HITPOINTS) + " (" + getSkillTracker().getGainedExperiencePerHour(Skill.HITPOINTS) + ")", 15, 165);
        g.drawString("Range level (gained): " + getSkills().getRealLevel(Skill.RANGED) + " (" + getSkillTracker().getGainedLevels(Skill.RANGED) + ")", 15, 180);
        g.drawString("Range experience gained (per hour): " + getSkillTracker().getGainedExperience(Skill.RANGED) + " (" + getSkillTracker().getGainedExperiencePerHour(Skill.RANGED) + ")", 15, 195);
    }

    public void switchCombat(int conf, int parent, int child, int gchild, String name) {
        if (getPlayerSettings().getConfig(43) != conf) {
            log("Switching combat: "+name);
            Widgets widget = getWidgets();
            getTabs().open(Tab.COMBAT);
            sleep(Calculations.random(600) + 325);
            if (gchild > 0) {
                if (widget.getWidgetChild(parent, child, gchild) != null) {
                    widget.getWidgetChild(parent, child, gchild).interact();
                    sleep(1000,1200);
                }
            } else {
                if (widget.getWidgetChild(parent, child) != null) {
                    widget.getWidgetChild(parent, child).interact();
                    sleep(1000,1200);
                }
            }
            sleep(Calculations.random(600) + 241);
            getTabs().open(Tab.INVENTORY);
        }
    }

}
