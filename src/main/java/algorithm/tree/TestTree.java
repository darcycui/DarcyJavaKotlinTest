package algorithm.tree;

import algorithm.tree.node.TreeNode;
import algorithm.tree.traverse.TreeTraverse;

import java.util.List;

public class TestTree {
    public static void main(String[] args) {
        TreeNode root = initTree();
//        TreeTraverse.preOrderTraverse(root);
//        System.out.println();
//        TreeTraverse.preOrderTraverse2(root);
//        System.out.println(); // 5 3 2 1 4 6 8 9 7

//        TreeTraverse.inOrderTraverse(root);
//        System.out.println();
//        TreeTraverse.inOrderTraverse2(root);
//        System.out.println(); // 2 3 1 5 8 6 9 4 7
//
//        TreeTraverse.postOrderTraverse(root);
//        System.out.println(); // 2 1 3 8 9 6 7 4 5
//        TreeTraverse.postOrderTraverse2(root);
//        System.out.println();

//        TreeTraverse.levelOrderTraverse(root);
//        System.out.println(); // 5 3 4 2 1 6 7 8 9

        List<List<TreeNode>> list = TreeTraverse.zLevelOrderTraverse(root);
        System.out.println(list); // 5 4 3 2 1 6 7 9 8
    }

    /**
     *         5
     *      3     4
     *   2  1   6  7
     *        8 9
     */
    private static TreeNode initTree() {
        TreeNode t1 = new TreeNode(1, null, null);
        TreeNode t2 = new TreeNode(2, null, null);
        TreeNode t3 = new TreeNode(3, null, null);
        TreeNode t4 = new TreeNode(4, null, null);
        TreeNode t5 = new TreeNode(5, null, null);
        TreeNode t6 = new TreeNode(6, null, null);
        TreeNode t7 = new TreeNode(7, null, null);
        TreeNode t8 = new TreeNode(8, null, null);
        TreeNode t9 = new TreeNode(9, null, null);
        t5.left = t3;
        t5.right = t4;
        t3.left = t2;
        t3.right = t1;
        t4.left = t6;
        t4.right = t7;
        t6.left = t8;
        t6.right = t9;

        TreeNode root = t5;

        return root;
    }
}
