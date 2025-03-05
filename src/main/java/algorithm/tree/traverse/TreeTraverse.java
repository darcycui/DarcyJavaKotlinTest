package algorithm.tree.traverse;

import algorithm.tree.node.TreeNode;

import java.util.*;

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

    /**
     * --------5
     * ----3      4
     * --2  1   6  7
     * -------8 9
     */
    public static TreeNode preOrderTraverse2(TreeNode root) {
        if (root == null) {
            return null;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                System.out.print(cur);
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop().right;
            }
        }
        return null;
    }

    public static TreeNode inOrderTraverse2(TreeNode root) {
        if (root == null) {
            return null;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                System.out.print(cur);
                cur = stack.pop().right;
            }
        }
        return null;
    }

    public static TreeNode postOrderTraverse2(TreeNode root) {
        if (root == null) {
            return null;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        TreeNode preVisited = null;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.peek().right;
                if (cur != null && cur != preVisited) {
                    stack.push(cur);
                    cur = cur.left;
                } else {
                    preVisited = stack.pop();
                    System.out.print(preVisited);
                    cur = null;
                }
            }
        }
        return null;
    }

    public static TreeNode levelOrderTraverse(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode cur = root;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(cur);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.print(node);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return null;
    }

    public static List<List<TreeNode>> zLevelOrderTraverse(TreeNode root) {
        if (root == null) {
            return null;
        }
        List<List<TreeNode>> result = new ArrayList<>();
        TreeNode cur = root;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(cur);
        boolean leftToRight = true;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<TreeNode> currentLevel = new LinkedList<>();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                if (leftToRight) {
                    currentLevel.add(node);
                } else {
                    currentLevel.addFirst(node);
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            result.add(currentLevel);
            leftToRight = !leftToRight;
        }
        return result;
    }
}
