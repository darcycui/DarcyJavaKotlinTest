package algorithm.aaa;

import java.util.*;

public class TreeWriteAgain1 {
    public static void main(String[] args) {
        ATree root = initATree();
        beforePrint2(root);
        println();
        middlePrint2(root);
        println();
        afterPrint2(root);
        println();
        levelPrint(root);
        println();
        List<List<ATree>> result = zLevelPrint(root);
        System.out.println("result:" +  result);
    }

    private static void println() {
        System.out.println();
    }

    /**
     *                  0
     *              1       2
     *            3   4   5   6
     *           7 8
     * 012345678
     */
    public static List<List<ATree>> zLevelPrint(ATree root) {
        List<List<ATree>> list = new ArrayList<>();
        boolean left2Right = true;
        Queue<ATree> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<ATree> levelList = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                ATree p = queue.poll();
                if (left2Right) {
                    levelList.add(p);
                } else {
                    levelList.addFirst(p);
                }
                System.out.print(p.value);
                if (p.left != null) {
                    queue.offer(p.left);
                }
                if (p.right != null) {
                    queue.offer(p.right);
                }
            }
            list.add(levelList);
            left2Right = !left2Right;
        }
        return list;
    }

    /**
     *                  0
     *              1       2
     *            3   4   5   6
     *           7 8
     * 012345678
     */
    public static void levelPrint(ATree root) {
        Queue<ATree> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            ATree p = queue.poll();
            System.out.print(p.value);
            if (p.left != null) {
                queue.offer(p.left);
            }
            if (p.right != null) {
                queue.offer(p.right);
            }
        }
    }

    /**
     *                  0
     *              1       2
     *            3   4   5   6
     *           7 8
     * 013784256
     */
    public static void beforePrint2(ATree root) {
        System.out.print(root.value);
        if (root.left != null) {
            beforePrint2(root.left);
        }
        if (root.right != null) {
            beforePrint2(root.right);
        }
    }

    /**
     *                  0
     *              1       2
     *            3   4   5   6
     *           7 8
     * 738140526
     */
    public static void middlePrint2(ATree root) {
        if (root.left != null) {
            middlePrint2(root.left);
        }
        System.out.print(root.value);
        if (root.right != null) {
            middlePrint2(root.right);
        }
    }

    /**
     *                  0
     *              1       2
     *            3   4   5   6
     *           7 8
     * 783415620
     */
    public static void afterPrint2(ATree root) {
        if (root.left != null) {
            afterPrint2(root.left);
        }
        if (root.right != null) {
            afterPrint2(root.right);
        }
        System.out.print(root.value);
    }

    public static ATree initATree() {
        ATree root = new ATree(0);
        ATree a = new ATree(1);
        ATree b = new ATree(2);
        ATree c = new ATree(3);
        ATree d = new ATree(4);
        ATree e = new ATree(5);
        ATree f = new ATree(6);
        ATree g = new ATree(7);
        ATree h = new ATree(8);
        root.left = a;
        root.right = b;
        a.left = c;
        a.right = d;
        b.left = e;
        b.right = f;
        c.left = g;
        c.right = h;
        return root;
    }

    public static class ATree {
        int value;
        ATree left;
        ATree right;

        public ATree(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" + value + '}';
        }
    }
}
