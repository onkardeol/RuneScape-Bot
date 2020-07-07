package NMZ.Nodes;

import NMZ.NMZ;
import NMZ.Node;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;

public class PrayerFlick extends Node {

    public PrayerFlick(NMZ main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return c.prayerTick > Calculations.random(48,53) && c.getSkills().getBoostedLevels(Skill.PRAYER) != 0;
    }

    @Override
    public int execute() {
        c.getPrayer().flick(c.prayer, Calculations.random(87,232));
        c.sleep(Calculations.random(707,987));
        c.prayerTick = 0;
        return 0;
    }
}
