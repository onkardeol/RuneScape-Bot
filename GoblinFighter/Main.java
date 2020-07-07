package GoblinFighter;


import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.PassableObstacle;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import javax.xml.crypto.dsig.spec.XPathFilterParameterSpec;
import java.awt.*;
import java.util.List;

@ScriptManifest(
        author = "Venom",
        description = "MobFighter",
        category = Category.COMBAT,
        version = 1,
        name = "Mob Fighter"
)
public class Main extends AbstractScript {
    public static final String COMBAT_WEAPON = "Staff of fire";
    public static final String COMBAT_SHIELD = "Adamant kiteshield";
    public static final String COMBAT_CHESTPLATE = "Adamant platebody";
    public static final String MOB = "Grizzly bear";
    public static final String FOOD = "Lobster";

    public static final int ATTACK_LVL = 99;
    public static final int STRENGTH_LVL = 99;
    public static final int DEFENCE_LVL = 99;
    private int currLevel = getSkills().getRealLevel(Skill.MAGIC);

    private boolean defCheck = false;
    private int breakCounter = 0;
    private int afkCounter = 0;
    private final Area combatArea = new Area(3123, 9850,3097,9828, 0);


    // Delay method to randomize actions
    public int getDelay(int min, int max){
        return (int)(Math.random() * (max - min) + 1) + min;
    }

    @Override
    public void onStart(){
        PassableObstacle gateOfWar = new PassableObstacle("Door", "Open", null, null, null);
        getWalking().getAStarPathFinder().addObstacle(gateOfWar);
        log("Starting Mob Fighter");
    }

    @Override
    public int onLoop() {

        if (breakCounter > getDelay(5412,6201)) {
            sleep(getDelay(124000, 273000));
            breakCounter = 0;
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

//        List<GroundItem> bones = getGroundItems().all();
//
//        for(int i = 0; i < bones.size();i++){
//            log(bones.get(i).toString());
//            if (bones.get(i).toString().equals("Bones")){
//                log("Found big bone");
//                bones.get(i).interact("Take");
//                int currInv = getInventory().getEmptySlots();
//                int finalCurrInv1 = currInv;
//                sleepUntil(()->getInventory().getEmptySlots() < finalCurrInv1, getDelay(5403,7023));
//                while (getInventory().contains("Big bones")) {
//                    getInventory().interact("Big bones", "Bury");
//                    currInv = getInventory().getEmptySlots();
//                    int finalCurrInv = currInv;
//                    sleepUntil(() -> getInventory().getEmptySlots() > finalCurrInv, getDelay(5012, 6032));
//                }
//            }
//        }


        if(getLocalPlayer().getHealthPercent() < 30 && !getInventory().contains(FOOD)){
            log("Ran out of food");
            stop();
            return -1;
        }

        if(getLocalPlayer().getHealthPercent() <=50){
            if(getInventory().contains(FOOD))
                getInventory().interact(FOOD, "Eat");
        }
        if (getLocalPlayer().getHealthPercent() == 0) {
            // do nothing
        } else if(getLocalPlayer().isInCombat()){
            // do nothing
        } else {
            if(getEquipment().isSlotEmpty(EquipmentSlot.WEAPON.getSlot())) {
                if (getInventory().contains(COMBAT_WEAPON)) {
                    getInventory().interact(COMBAT_WEAPON, "Wield");
                    if(getInventory().contains(COMBAT_SHIELD))
                        getInventory().interact(COMBAT_SHIELD, "Wield");
                    if(getInventory().contains(COMBAT_CHESTPLATE))
                        getInventory().interact(COMBAT_CHESTPLATE, "Wield");
                } else {
                    // Stop Script
                    log("No weapon equipped or in inventory, stopping script.");
                    stop();
                    return -1;
                }
            } else {
                NPC mob = getNpcs().closest(MOB);
//                List<NPC> areaNpcs = getNpcs().all(MOB);
//                //NPC mob = getNpcs().closest(MOB);
//
//                for(int i = 0; i < areaNpcs.size(); i++){
//                    log("Looping through mob list");
//                    if(areaNpcs.get(i).equals(MOB) && !areaNpcs.get(i).isInCombat()){
//                        mob = areaNpcs.get(i);
//                        log("MOB SELECTED");
//                    }
//
//                }
                if(getSkills().getRealLevel(Skill.MAGIC) > currLevel|| afkCounter > 300){
                    mob.interact("Attack");
                    getMouse().moveMouseOutsideScreen();
                    log("Attacking");
                    currLevel = getSkills().getRealLevel(Skill.MAGIC);
                    afkCounter = 0;
                }

                if (mob != null && !getLocalPlayer().isAnimating()) {
                    mob.interact("Attack");
                    getMouse().moveMouseOutsideScreen();
                    log("Attacking");

                }
            }
        }

        breakCounter++;
        afkCounter++;
        log(String.valueOf(breakCounter));

        return getDelay(500, 1000);
    }

    @Override
    public void onExit(){

    }

    @Override
    public void onPaint(Graphics graphics){

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
