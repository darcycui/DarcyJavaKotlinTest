package algorithm.aaa;

public class NodeWriteAgain1 {
    public static void main(String[] args) {
        ANode root = initANode();
        printANode(root);
//        ANode newRoot = reverseANode(root);
//        printANode(newRoot);
        ANode newRoot2 = reverseANode2(root);
        printANode(newRoot2);
    }

    private static ANode reverseANode(ANode root) {
        ANode pre = null;
        ANode current = root;
        ANode next = null;
        while (current != null) {
            next = current.next; // 要先记录下一个节点Node
            current.next = pre;
            pre = current;
            current = next;
        }
        return pre;
    }

    public static ANode reverseANode2(ANode current) {
        if (current == null || current.next == null) {
            return current;
        }
        ANode head = reverseANode2(current.next); // ANode(5)
        ANode next = head.next;
        next.next = current;
        current.next = null; // 这里current 指向 null
        return head;
    }


    public static void printANode(ANode root) {
        ANode p = root;
        while (p != null) {
            System.out.print(p.value);
            p = p.next;
        }
        System.out.println();
    }

    public static ANode initANode() {
        ANode root = new ANode(0);
        ANode a = new ANode(1);
        ANode b = new ANode(2);
        ANode c = new ANode(3);
        ANode d = new ANode(4);
        ANode e = new ANode(5);
        root.next = a;
        a.next = b;
        b.next = c;
        c.next = d;
        d.next = e;
        e.next = null;
        return root;
    }

    public static class ANode {
        public int value;
        public ANode next;

        public ANode(int value) {
            this.value = value;
        }
    }

}
