//************************************************************************
//************************************************************************

public class LinkedQueue implements QueueInterface, java.io.Serializable {

    private Node firstNode;
    private Node lastNode;
    private int length;// added to keep up with the length of the list
    //********************************************************************

    public LinkedQueue() {
        firstNode = null;
        lastNode = null;
        length = 0;// added to keep up with the length of the list
    }
    //********************************************************************

    public void enqueue(Object newEntry) {
        Node newNode = new Node(newEntry, null);
        if (isEmpty()) {
            firstNode = newNode;
        } else {
            lastNode.setNextNode(newNode);
        }
        lastNode = newNode;
        length++;// added to keep up with the length of the list
    }
//********************************************************************
//********************************************************************
// Method:       priorityEnqueue
// Description:  add mehtod from SortedLinkedList.java, adds methods into the queue in order of fuel left
// Parameters:   new Entry - new object to be added to the queue
// Returns:      boolean
// Calls:        Node()
//               getNodeBefore()
//               setNextNode()
//               getNextNode()
// Globals:      firstNode   

    public boolean priorityEnqueue(Comparable newEntry) {
        Node newNode = new Node(newEntry);
        Node nodeBefore = getNodeBefore(newEntry);

        if (isEmpty() || (nodeBefore == null)) {
            newNode.setNextNode(firstNode);
            firstNode = newNode;
        } else {
            Node nodeAfter = nodeBefore.getNextNode();
            newNode.setNextNode(nodeAfter);
            nodeBefore.setNextNode(newNode);
        }
        length++;// added to keep up with the length of the list
        return true;
    }
//********************************************************************
//********************************************************************
// Method:       getNodeBefore
// Description:  used by priorityEnque to get the node before the current node in order to nsert the new object
//               in the correct position
// Parameters:   Comparable anEntry 
// Returns:      none
// Calls:        compareTo()
//               getData()
//               getNextNode()
// Globals:      firstNode 

    private Node getNodeBefore(Comparable anEntry) {
        Node currentNode = firstNode;
        Node nodeBefore = null;

        while ((currentNode != null) && (anEntry.compareTo(currentNode.getData()) > 0)) {
            nodeBefore = currentNode;
            currentNode = currentNode.getNextNode();
        }
        return nodeBefore;
    }
    //**********************************************************************

    public Object dequeue() {
        Object front = null;
        if (!isEmpty()) {
            front = firstNode.getData();
            firstNode = firstNode.getNextNode();
            if (firstNode == null) {
                lastNode = null;
            }
        }
        length--;// added to keep up with the length of the list
        return front;
    }
    //********************************************************************

    public Object getFront() {
        Object front = null;
        if (!isEmpty()) {
            front = firstNode.getData();
        }
        return front;
    }
    //********************************************************************

    public boolean isEmpty() {
        return firstNode == null;
    }
    //********************************************************************

    public void clear() {
        firstNode = null;
        lastNode = null;
    }
    //********************************************************************	
    // method copied from the LList() class, needed so my for loop will work to compare if in a list
    public int getLength() {
        return length;
    }
    //********************************************************************
    //********************************************************************

    private class Node {

        private Object data;
        private Node next;

        private Node(Object dataPortion) {
            data = dataPortion;
            next = null;
        }

        private Node(Object dataPortion, Node nextNode) {
            data = dataPortion;
            next = nextNode;
        }

        private Object getData() {
            return data;
        }

        private void setData(Object newData) {
            data = newData;
        }

        private Node getNextNode() {
            return next;
        }

        private void setNextNode(Node nextNode) {
            next = nextNode;
        }
    }
    //********************************************************************
    //********************************************************************
}
//************************************************************************
//************************************************************************
