package WoodCutter;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import java.awt.*;

@ScriptManifest(
        author = "Venom",
        description = "test",
        category = Category.WOODCUTTING,
        version = 3,
        name = "Wood Cutter"
)
public class Main extends AbstractScript {

    // Varrock Bank Area
    Area bankArea = new Area(3185,3447,3181,3433, 0);
    Area treeArea = new Area(3171,3418,3159,3400,0);

    // Delay method to randomize actions
    public int getDelay(int min, int max){
        return (int)(Math.random() * (max - min) + 1) + min;
    }

    public void chopTree(String treeName){
        GameObject tree = getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Oak") && gameObject.hasAction("Chop down"));
        if(tree != null)
            tree.interact("Chop down");
//        if(tree != null && tree.interact("Chop down")) {
//            int countLogs = getInventory().count("Logs");
//            sleepUntil(() -> getInventory().count("Logs") > countLogs, getDelay(12000, 16000));
//        }
    }

    public void goToBank(){
        NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));
        if(banker.interact("Bank")){
            if(sleepUntil(() -> getBank().isOpen(), getDelay(8000, 12000))){
                if(getBank().depositAllExcept(item -> item != null && item.getName().contains("axe"))){
                    if(sleepUntil(() -> !getInventory().isFull(), getDelay(8000,12000))){
                        if(getBank().close()){
                            sleepUntil(() -> !getBank().isOpen(), getDelay(8000,12000));

                        }
                    }
                }
            }
        }
    }

    @Override
    public void onStart(){
        log("Starting Bundcutter");
    }

    @Override
    public int onLoop(){

        // Cut down trees
        if(!getInventory().isFull()){
            if(treeArea.contains(getLocalPlayer()) && !getLocalPlayer().isAnimating()) {
                chopTree("Tree");
            } else {
                if(getWalking().walk(treeArea.getCenter())){
                    sleep(getDelay(3000, 6000));
                }
            }
        }

        // Go to bank
        if(getInventory().isFull()){
            if(bankArea.contains(getLocalPlayer())){
                goToBank();
            } else {
                if (getWalking().walk(bankArea.getCenter())) {
                    sleep(getDelay(3000, 6000));
                }
            }
        }

        return getDelay(500, 1000);
    }

    @Override
    public void onExit(){

    }

    @Override
    public void onPaint(Graphics graphics){

    }
}
