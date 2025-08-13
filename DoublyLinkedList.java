package playlist;

/**
 * Node for the Doubly Linked List.
 * @param <T> The type of data stored in the node.
 */
class Node<T> {
    T data;
    Node<T> prev;
    Node<T> next;

    public Node(T data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}

/**
 * Implements a generic Doubly Linked List.
 * Time complexities are annotated for each method.
 * Space Complexity: O(N) for storing N elements.
 */
public class DoublyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Adds an element to the end of the list.
     * Time Complexity: O(1)
     * Space Complexity: O(1) for new node
     * @param data The data to add.
     */
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    /**
     * Adds an element at a specific index.
     * Time Complexity: O(min(index, size - index)) - optimized to traverse from head or tail
     * Space Complexity: O(1) for new node
     * @param index The index where the element should be added.
     * @param data The data to add.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public void add(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            addLast(data);
            return;
        }
        if (index == 0) {
            addFirst(data);
            return;
        }

        Node<T> current = getNodeAtIndex(index); // O(min(index, size - index))
        Node<T> newNode = new Node<>(data);
        newNode.next = current;
        newNode.prev = current.prev;
        if (current.prev != null) {
            current.prev.next = newNode;
        }
        current.prev = newNode;
        size++;
    }

    /**
     * Adds an element to the beginning of the list.
     * Time Complexity: O(1)
     * Space Complexity: O(1) for new node
     * @param data The data to add.
     */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    /**
     * Deletes an element at a specific index.
     * Time Complexity: O(min(index, size - index))
     * Space Complexity: O(1)
     * @param index The index of the element to delete.
     * @return The data of the deleted element.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<T> nodeToDelete = getNodeAtIndex(index); // O(min(index, size - index))
        T data = nodeToDelete.data;

        if (nodeToDelete.prev != null) {
            nodeToDelete.prev.next = nodeToDelete.next;
        } else { // Deleting head
            head = nodeToDelete.next;
        }

        if (nodeToDelete.next != null) {
            nodeToDelete.next.prev = nodeToDelete.prev;
        } else { // Deleting tail
            tail = nodeToDelete.prev;
        }
        nodeToDelete.prev = null; // Help with garbage collection
        nodeToDelete.next = null; // Help with garbage collection
        size--;
        return data;
    }

    /**
     * Moves a song from one index to another.
     * This involves detaching the node and re-inserting it.
     * Time Complexity: O(N) due to finding nodes, O(1) for node swaps once found.
     * Space Complexity: O(1)
     * @param fromIndex The current index of the song.
     * @param toIndex The new index for the song.
     * @throws IndexOutOfBoundsException If either index is out of bounds.
     */
    public void move(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= size || toIndex < 0 || toIndex >= size) {
            throw new IndexOutOfBoundsException("Invalid index for move operation.");
        }
        if (fromIndex == toIndex) {
            return; // No movement needed
        }

        // Extract the node from its current position (O(min(index, size-index)))
        Node<T> nodeToMove = getNodeAtIndex(fromIndex);

        // Detach nodeToMove
        if (nodeToMove.prev != null) {
            nodeToMove.prev.next = nodeToMove.next;
        } else { // nodeToMove is head
            head = nodeToMove.next;
        }
        if (nodeToMove.next != null) {
            nodeToMove.next.prev = nodeToMove.prev;
        } else { // nodeToMove is tail
            tail = nodeToMove.prev;
        }
        size--; // Temporarily decrement size for insertion logic

        // Re-insert nodeToMove at the new position
        if (toIndex == size) { // New end of list (after removal, size is original_size - 1)
            addLast(nodeToMove.data); // This handles setting nodeToMove's prev/next and increments size
        } else if (toIndex == 0) { // New beginning of list
            addFirst(nodeToMove.data); // This handles setting nodeToMove's prev/next and increments size
        } else {
            Node<T> targetNode = getNodeAtIndex(toIndex); // Find target insertion point
            nodeToMove.next = targetNode;
            nodeToMove.prev = targetNode.prev;
            if (targetNode.prev != null) {
                targetNode.prev.next = nodeToMove;
            }
            targetNode.prev = nodeToMove;
            size++; // Increment size back
        }
    }


    /**
     * Reverses the order of the playlist.
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public void reverse() {
        if (head == null || head == tail) {
            return; // 0 or 1 element, nothing to reverse
        }

        Node<T> current = head;
        Node<T> temp = null;
        Node<T> newHead = null;

        while (current != null) {
            temp = current.prev; // Store prev
            current.prev = current.next; // Change prev to next
            current.next = temp;         // Change next to original prev

            if (current.prev == null) { // This means current is the new head after reversal
                newHead = current;
            }
            current = current.prev; // Move to the next node in the original list (which is now current.prev)
        }

        // Swap head and tail pointers
        temp = head; // Use temp to hold original head
        head = tail; // New head is original tail
        tail = temp; // New tail is original head
    }

    /**
     * Retrieves the data at a specific index.
     * Time Complexity: O(min(index, size - index))
     * Space Complexity: O(1)
     * @param index The index of the element to retrieve.
     * @return The data at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return getNodeAtIndex(index).data;
    }

    /**
     * Helper method to get the Node object at a specific index.
     * Optimizes traversal by starting from head or tail based on index.
     * Time Complexity: O(min(index, size - index))
     * Space Complexity: O(1)
     * @param index The index of the node to retrieve.
     * @return The Node at the specified index.
     */
    private Node<T> getNodeAtIndex(int index) {
        if (index < 0 || index >= size) {
            return null; // Or throw IndexOutOfBoundsException
        }
        Node<T> current;
        if (index < size / 2) { // Traverse from head
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else { // Traverse from tail
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }


    /**
     * Returns the current size of the list.
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * @return The number of elements in the list.
     */
    public int getSize() {
        return size;
    }

    /**
     * Checks if the list is empty.
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears all elements from the list.
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns a List containing all elements in the DoublyLinkedList.
     * Time Complexity: O(N)
     * Space Complexity: O(N) for the new List
     * @return A List of all elements.
     */
    public java.util.List<T> toList() {
        java.util.List<T> list = new java.util.ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
}