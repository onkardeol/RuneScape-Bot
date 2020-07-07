package PestControl;

import PestControl.Nodes.Minigame;
import PestControl.Nodes.Start;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.PassableObstacle;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;



@ScriptManifest(
        category = Category.COMBAT,
        name = "Pest Control",
        author = "Venom",
        version = 0
)
public class PestControl extends AbstractScript {
    private Node[] nodes;

    public boolean moved = false;

    public final Area WAITING_AREA = new Area(2638,2655,2657,2638,0);
    public final Area INTERMEDIATE_BOAT = new Area(11680,694,11683,688);

    public final Tile INTERMEDIATE_BOAT_TILE = new Tile(2640,2644,0);



    @Override
    public void onStart() {

        PassableObstacle gateOpen = new PassableObstacle("Gate", "Open", null, null, null);
        getWalking().getAStarPathFinder().addObstacle(gateOpen);

        PassableObstacle gateClosed = new PassableObstacle("Gate", "Closed", null, null, null);
        getWalking().getAStarPathFinder().addObstacle(gateClosed);

        nodes = new Node[]{
                new Start(this),
                new Minigame(this)
        };


    }

    @Override
    public int onLoop() {
        if(getLocalPlayer().getHealthPercent() == 0)
            moved = false;
        for(Node node : nodes){
            if(node.validate()){
                node.execute();
            }
        }
        return Calculations.random(958,1012);
    }

    @Override
    public void onExit(){

    }
}