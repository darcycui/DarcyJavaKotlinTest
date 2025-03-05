package algorithm.linkedlist;

import org.jetbrains.annotations.NotNull;

public class TestNode {
    public static void main(String[] args) {
        Node head = initNode();
        printNode(head);
//        printNode(reverseNode(head));
        printNode(reverseNode2(head));
    }

    private static @NotNull Node initNode() {
        Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        Node e = new Node(5);
        a.next = b;
        b.next = c;
        c.next = d;
        d.next = e;
        return a;
    }
    private static void printNode(Node head) {
        while (head != null) {
            System.out.print(head.data + "  ");
            head = head.next;
        }
        System.out.println();
    }

    private static Node reverseNode(Node head) {
        Node pre = null;
        Node cur = head;
        Node next = null;
        while (cur != null) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }
    private static Node reverseNode2(Node cur) {
        if (cur == null || cur.next == null){
            return cur;
        }
        Node head = reverseNode2(cur.next);
        Node next = cur.next;
        next.next = cur;
        cur.next = null;
        return head;
    }
}
