package algorithm.circle;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;

public class TestCircle {
    public static void main(String[] args) {
        CircleNode root = initNodeCircle();
        printNode(root);
        CircleNode last = circleM(root, 3);
        System.out.print("last==");
        printNode(last);

        int last2 = circleM2(3);
        System.out.print("last2==" + last2);
    }

    private static @NotNull CircleNode initNodeCircle() {
        CircleNode a = new CircleNode(1);
        CircleNode b = new CircleNode(2);
        CircleNode c = new CircleNode(3);
        CircleNode d = new CircleNode(4);
        CircleNode e = new CircleNode(5);
        CircleNode f = new CircleNode(6);
        CircleNode g = new CircleNode(7);
        a.next = b;
        b.prev = a;
        b.next = c;
        c.prev = b;
        c.next = d;
        d.prev = c;
        d.next = e;
        e.prev = d;
        e.next = f;
        f.next = g;
        f.prev = e;
        g.next = a;
        a.prev = g;
        return a;
    }

    private static void printNode(CircleNode root) {
        CircleNode p = root;
        do {
            System.out.print(p);
            p = p.next;
        } while (p != root);
        System.out.println();
    }

    private static void deleteNode(CircleNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node = null;
    }

    private static CircleNode circleM(CircleNode root, int m) {
        CircleNode current = root;
        while (current.next != current) {
            for (int i = 1; i < m; i++) {
                current = current.next;
            }
            System.out.println("删除: " + current);
            CircleNode next = current.next;
            deleteNode(current);
            current = next;
        }
        return current;
    }

    private static Integer circleM2(int m) {
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 1; i <= 7; i++) {
            queue.offer(i);
        }
        while (queue.size() > 1) {
            for (int i = 1; i < m; i++) {
                queue.offer(queue.poll());
            }
            int x = queue.poll();
            System.out.println("删除: " + x);
        }
        return queue.poll();
    }
}
