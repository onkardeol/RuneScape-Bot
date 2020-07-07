package NMZ;

public abstract class Node {
    protected final NMZ c;

    public Node(NMZ main){
        this.c = main;
    }

    public abstract boolean validate();
    public abstract int execute();
}
