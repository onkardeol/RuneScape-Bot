package PestControl.Nodes;

import PestControl.Node;
import PestControl.PestControl;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.PassableObstacle;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;

import static org.dreambot.api.methods.MethodProvider.log;

public class Start extends Node {
    public Start(PestControl main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return c.WAITING_AREA.contains(c.getLocalPlayer()) && !c.getLocalPlayer().getTile().equals(c.INTERMEDIATE_BOAT_TILE);
    }

    @Override
    public int execute() {
        log("Player is in waiting area, waiting for mini game to begin.");
        Tile intermediateBoatTile = new Tile(2643,2644,0);
        GameObject intermediateBoat = c.getGameObjects().getTopObjectOnTile(intermediateBoatTile);
        intermediateBoat.interact("Cross");
        c.sleep(Calculations.random(5433,7430));
        //c.moved = false;
        return 1000;
    }
}
