package Alch;

import PestControl.PestControl;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.magic.Spell;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;

import java.awt.*;

@ScriptManifest(
        category = Category.MAGIC,
        name = "Alch",
        author = "Venom",
        version = 1
)
public class Alch extends AbstractScript {
    private static final String NATURE_RUNE = "Nature rune";
    private static final String LAW_RUNE = "Law rune";
    private static final String AIR_RUNE = "Air rune";
    private static final String ALCH_ITEM = "Magic longbow";
    private static final Spell HIGH_ALCH = Normal.HIGH_LEVEL_ALCHEMY;
    private static final Spell CAM_TP = Normal.CAMELOT_TELEPORT;
    private long start;

    @Override
    public void onStart(){
        start = System.currentTimeMillis();
        getSkillTracker().start(Skill.MAGIC);
    }


    @Override
    public int onLoop() {
        if(!getInventory().contains(NATURE_RUNE) && !getInventory().contains(LAW_RUNE)){
            log("Insufficient runes, stopping script.");
            stop();
        }

        if(!getMagic().isSpellSelected() && getInventory().contains(NATURE_RUNE)){
            getMagic().castSpell(HIGH_ALCH);
            sleepUntil(() -> getTabs().isOpen(Tab.INVENTORY), Calculations.random(543,1045));
        }
        if(getInventory().contains(ALCH_ITEM))
            getInventory().get(ALCH_ITEM).interact();

        sleepUntil(() -> getTabs().isOpen(Tab.MAGIC), Calculations.random(495,643));

        if(getInventory().contains(LAW_RUNE) && getInventory().count(AIR_RUNE) > 5)
            getMagic().castSpell(CAM_TP);
        //sleepUntil(() ->  !getLocalPlayer().getTile().equals(playerPos), Calculations.random(1502,2021));
        sleep(Calculations.random(983,1203));

        return Calculations.random(1034,1234);
    }

    @Override
    public void onPaint(final Graphics g) {
        g.drawString("Runtime: " + Timer.formatTime(System.currentTimeMillis() - start), 15, 60);
        g.drawString("Mage level (gained): " + getSkills().getRealLevel(Skill.MAGIC) + " (" + getSkillTracker().getGainedLevels(Skill.MAGIC) + ")", 15, 75);
        g.drawString("Mage experience gained (per hour): " + getSkillTracker().getGainedExperience(Skill.MAGIC) + " (" + getSkillTracker().getGainedExperiencePerHour(Skill.MAGIC) + ")", 15, 90);
    }
}
