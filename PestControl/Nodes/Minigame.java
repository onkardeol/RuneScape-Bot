package PestControl.Nodes;

import PestControl.Node;
import PestControl.PestControl;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.PassableObstacle;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.List;

public class Minigame extends Node {

    private final String SQUIRE = "Squire";
    private Tile newPosition;

    public Minigame(PestControl main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return !c.WAITING_AREA.contains(c.getLocalPlayer()) && !c.getLocalPlayer().isInCombat();
    }

    @Override
    public int execute() {
        c.log(Boolean.toString(c.moved));
        NPC squire = c.getNpcs().closest(SQUIRE);
        NPC mob = null;
        List<NPC> areaNpcs = c.getNpcs().all();
        c.log(Integer.toString(areaNpcs.size()));

        if(c.getLocalPlayer().distance(squire) < 8){ //&& c.moved == false
            runToKnight();
            c.log("Running to knight");
            //c.moved = true;
        }

//        if(!c.getLocalPlayer().getTile().equals(newPosition))
//            c.getWalking().walk(newPosition);


        for(int i = 0; i < areaNpcs.size(); i++){
            if((areaNpcs.get(i).getHealthPercent() > 20) && areaNpcs.get(i).hasAction("Attack") && c.getLocalPlayer().distance(areaNpcs.get(i)) < 10){
                mob = areaNpcs.get(i);
                break;
            }

        }
        if (mob != null && !c.getLocalPlayer().isInCombat()) {
            mob.interact("Attack");
            c.sleepUntil(() ->!c.getLocalPlayer().isInCombat(), Calculations.random(532,932));
            c.log("Attacking");
        }
        return 0;
    }

    public void runToKnight(){
        int posX = c.getLocalPlayer().getX();
        int posY = c.getLocalPlayer().getY();
        c.log("Player Pos Tile: " + c.getLocalPlayer().getTile() + " X:" + c.getLocalPlayer().getX() + " Y:" + c.getLocalPlayer().getY());
        posY = posY  - (Calculations.random(23,25));
        newPosition = new Tile(posX,posY,0);
        c.log("New Player Pos Tile: " + c.getLocalPlayer().getTile() + " X:" + posX + " Y:" + posY);
        c.getWalking().walk(newPosition);
        c.sleepUntil(() -> c.getLocalPlayer().getTile().equals(newPosition), Calculations.random(1234,1943));
    }
}
