import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Zachary Zhu
 */

public class BSTStringSet implements SortedStringSet, Iterable<String> {

    /** Root node of the tree. */
    private Node _root;

    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        findAndPutParent(s, _root);
    }

    /** Helper function for put using root Node */
    public Node findAndPutParent(String str, Node n) {
        if (n == null) {
            _root = new Node(str);
            return null;
        } else if (str.compareTo(n.s) == 0) {
            return n;
        } else if (str.compareTo(n.s) > 0 && n.right == null) {
            n.right = new Node(str);
            return n;
        } else if (str.compareTo(n.s) < 0 && n.left == null) {
            n.left = new Node(str);
            return n;
        } else if (str.compareTo(n.s) > 0) {
            return findAndPutParent(str, n.right);
        } else {
            return findAndPutParent(str, n.left);
        }
    }

    /** Helper function for contains */
    public boolean containsHelper(String s, Node n) {
        if (n == null) {
            return false;
        } else if (n.getLabel().equals(s)) {
            return true;
        } else if (n.getLabel().compareTo(s) > 0) {
            return containsHelper(s, n.left);
        } else {
            return containsHelper(s, n.right);
        }
    }
    @Override
    public boolean contains(String s) {
        return containsHelper(s, _root);
    }

    @Override
    public List<String> asList() {
        List<String> listStrings = new ArrayList<String>();
        BSTIterator iter = new BSTIterator(_root);
        while (iter.hasNext()) {
            String nextElem = iter.next();
            listStrings.add(nextElem);
            //System.out.println(nextElem);
        }
        return listStrings;
    }

    // Tester for range iterator
    public void printRange() {
        BSTStringSetinOrderIterator rangeIter = new BSTStringSetinOrderIterator(_root, "ia", "zz");
        while (rangeIter.hasNext()) {
            String nextElem = rangeIter.next();
            System.out.println(nextElem);
        }
    }

    public String getBSTLabel() {
        return _root.getLabel();
    }

    /** Returns the left node. */
    public Node getLeftNode() {
        return _root.left;
    }

    /** Returns the right node. */
    public Node getRightNode() {
        return _root.right;
    }

    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
        /** Return the left node. */
        Node getLeft() {
            return left;
        }
        /** Return the right node. */
        Node getRight() {
            return right;
        }
        /** Get the label of the current node. */
        String getLabel() {
            return s;
        }
    }




    /** An iterator over BSTs. */
    // How can we restrict the range of values that it can iterate over?
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    // @Override was previously commented out
    @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTStringSetinOrderIterator(_root, low, high);
    }

    /** SECONDARY CLASS */
    private static class BSTStringSetinOrderIterator implements Iterator<String>  {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTStringSetinOrderIterator(Node node, String low, String high) {
            _low = low;
            _high = high;
            addTree(node);

        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            if (node != null) {
                if (node.s.compareTo(_high) >= 0) { // Case of too high a value
                    addTree(node.left);
                }

                else if (node.s.compareTo(_low) < 0) { // Case of too low a value
                    addTree(node.right);
                }

                else { // We are within the range
                    _toDo.push(node);
                    addTree(node.left);
                }
            }
        }

        private String _low;
        private String _high;

    }
}

