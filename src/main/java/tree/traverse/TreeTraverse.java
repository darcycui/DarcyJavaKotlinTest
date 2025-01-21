package tree.traverse;

import tree.node.TreeNode;

public class TreeTraverse {
    public static TreeNode preOrderTraverse(TreeNode root) {
        if (root == null) {
            return root;
        }
        System.out.print(root.data + " ");
        preOrderTraverse(root.left);
        preOrderTraverse(root.right);
        return root;
    }

    public static TreeNode inOrderTraverse(TreeNode root) {
        if (root == null) {
            return root;
        }
        inOrderTraverse(root.left);
        System.out.print(root.data + " ");
        inOrderTraverse(root.right);
        return root;
    }

    public static TreeNode postOrderTraverse(TreeNode root) {
        if (root == null) {
            return root;
        }
        postOrderTraverse(root.left);
        postOrderTraverse(root.right);
        System.out.print(root.data + " ");
        return root;
    }
//
//    public static TreeNode levelOrderTraverse(TreeNode root) {
//
//    }
//
//    public static void nonRecursionTraverse(TreeNode root) {
//
//    }
}
