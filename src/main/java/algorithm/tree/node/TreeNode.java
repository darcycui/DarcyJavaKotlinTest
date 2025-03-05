package algorithm.tree.node;

public class TreeNode {
    public int data;
    public TreeNode left;
    public TreeNode right;
    public int height;

    public TreeNode(int data, TreeNode left, TreeNode right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return String.valueOf(this.data + " ");
    }
}
