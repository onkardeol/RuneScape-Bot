package PestControl;

public abstract class Node {
    protected final PestControl c;

    public Node(PestControl main){
        this.c = main;
    }

    public abstract boolean validate();
    public abstract int execute();
}
