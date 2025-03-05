package algorithm.circle;

public class CircleNode {
    public int data;
    public CircleNode prev;
    public CircleNode next;
    public CircleNode(int data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return this.data + " ";
    }
}
