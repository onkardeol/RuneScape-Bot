package NMZ;

import NMZ.Node;
import NMZ.Nodes.Absorption;
import NMZ.Nodes.Health;
import NMZ.Nodes.PrayerFlick;
import NMZ.Nodes.PrayerPotion;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.MessageListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.widgets.message.Message;

import java.awt.*;

@ScriptManifest(
        category = Category.COMBAT,
        name = "NMZ",
        author = "Venom",
        version = 1
)
public class NMZ extends AbstractScript implements MessageListener {
    private Node[] nodes;
    public int prayerTick = 54;
    public int rangeTimer = 0;
    public Prayer prayer = Prayer.RAPID_HEAL;

    private final Area NMZ_AREA = new Area(2601,3118,2611,3112,0);

    public int absorptionPoints = 0;

    private long start;

    @Override
    public void onStart(){
        nodes = new Node[]{
                new Absorption(this),
                new Health(this),
                new PrayerPotion(this),
                new PrayerFlick(this),

        };

        getSkillTracker().start(Skill.ATTACK);
        getSkillTracker().start(Skill.STRENGTH);
        getSkillTracker().start(Skill.DEFENCE);
        getSkillTracker().start(Skill.HITPOINTS);
        getSkillTracker().start(Skill.RANGED);
        start = System.currentTimeMillis();
    }

    @Override
    public int onLoop() {

        for(Node node : nodes){
            if(node.validate()){
                node.execute();
            }
        }

//        if(getSkills().getBoostedLevels(Skill.HITPOINTS) == 1) {
//            getPrayer().openTab();
//            //getPrayer().flick(prayer, Calculations.random(87,232));
//        }
        prayerTick++;
        rangeTimer++;
        log("Player Tick(Flick at 48-53): " + Integer.toString(prayerTick));

        if(NMZ_AREA.contains(getLocalPlayer())){
            stop();
            log("Died. Stopping script.");
            return 0;
        }

        return Calculations.random(950,1000);
    }

    @Override
    public void onPaint(final Graphics g) {
        g.drawString("Runtime: " + Timer.formatTime(System.currentTimeMillis() - start), 15, 55);
        g.drawString("Attack level (gained): " + getSkills().getRealLevel(Skill.ATTACK) + " (" + getSkillTracker().getGainedLevels(Skill.ATTACK) + ")", 15, 70);
        g.drawString("Attack experience gained (per hour): " + getSkillTracker().getGainedExperience(Skill.ATTACK) + " (" + getSkillTracker().getGainedExperiencePerHour(Skill.ATTACK) + ")", 15, 85);
        g.drawString("Strength level (gained): " + getSkills().getRealLevel(Skill.STRENGTH) + " (" + getSkillTracker().getGainedLevels(Skill.STRENGTH) + ")", 15, 100);
        g.drawString("Strength experience gained (per hour): " + getSkillTracker().getGainedExperience(Skill.STRENGTH) + " (" + getSkillTracker().getGainedExperiencePerHour(Skill.STRENGTH) + ")", 15, 115);
        g.drawString("Defence level (gained): " + getSkills().getRealLevel(Skill.DEFENCE) + " (" + getSkillTracker().getGainedLevels(Skill.DEFENCE) + ")", 15, 130);
        g.drawString("Defence experience gained (per hour): " + getSkillTracker().getGainedExperience(Skill.DEFENCE) + " (" + getSkillTracker().getGainedExperiencePerHour(Skill.DEFENCE) + ")", 15, 145);
        g.drawString("Hitpoints level (gained): " + getSkills().getRealLevel(Skill.HITPOINTS) + " (" + getSkillTracker().getGainedLevels(Skill.HITPOINTS) + ")", 15, 160);
        g.drawString("Hitpoints experience gained (per hour): " + getSkillTracker().getGainedExperience(Skill.HITPOINTS) + " (" + getSkillTracker().getGainedExperiencePerHour(Skill.HITPOINTS) + ")", 15, 175);
        g.drawString("Range level (gained): " + getSkills().getRealLevel(Skill.RANGED) + " (" + getSkillTracker().getGainedLevels(Skill.RANGED) + ")", 15, 190);
        g.drawString("Range experience gained (per hour): " + getSkillTracker().getGainedExperience(Skill.RANGED) + " (" + getSkillTracker().getGainedExperiencePerHour(Skill.RANGED) + ")", 15, 205);
        g.drawString("Absorption Points: " + absorptionPoints, 15, 220);
    }

    @Override
    public void onGameMessage(final Message message){
        if(message.getMessage().contains("hitpoints of damage")){
            absorptionPoints = Integer.parseInt(message.getMessage().split("have ")[1].split(" hitpoints")[0]);
        }
    }

    @Override
    public void onPlayerMessage(Message message) {

    }

    @Override
    public void onTradeMessage(Message message) {

    }

    @Override
    public void onPrivateInMessage(Message message) {

    }

    @Override
    public void onPrivateOutMessage(Message message) {

    }
}
