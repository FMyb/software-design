import java.util.NoSuchElementException;

public class LinkedListNode<V> {
    private final Node<V> head;
    private final Node<V> tail;
    private int size;

    public LinkedListNode() {
        head = new Node<>(null);
        tail = new Node<>(null);
        head.setNext(tail);
        tail.setPrev(head);
        size = 0;
    }

    public V removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        V result = tail.prev().value();
        var prePrev = tail.prev().prev();
        prePrev.setNext(tail);
        tail.setPrev(prePrev);
        size--;
        return result;
    }

    public void removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        var nextNext = head.next().next();
        nextNext.setPrev(head);
        head.setNext(nextNext);
        size--;
    }

    public void removeNode(Node<V> node) {
        node.remove();
        size--;
    }

    public Node<V> addFirst(final V value) {
        Node<V> node = new Node<>(value);
        addFirst(node);
        return node;
    }

    public void addFirst(final Node<V> node) {
        node.setNext(head.next());
        node.setPrev(head);
        head.next().setPrev(node);
        head.setNext(node);
        size++;
    }

    public int size() {
        return size;
    }

    public static class Node<V> {
        private V value;
        private Node<V> next;
        private Node<V> prev;

        public Node(V value) {
            this.value = value;
            this.next = null;
            this.prev = null;
        }

        public V value() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<V> next() {
            return next;
        }

        public void setNext(Node<V> next) {
            this.next = next;
        }

        public Node<V> prev() {
            return prev;
        }

        public void setPrev(Node<V> prev) {
            this.prev = prev;
        }

        private void remove() {
            next.setPrev(prev);
            prev.setNext(next);
        }
    }
}
