package cz.vsb.pjp.project.grammar;

import java.util.Vector;
import java.util.Stack;

/**

 */
public class DerivationTree<T> {
    class Node {
        Vector<Node> children = new Vector<Node>(2);
        Node parent = null;
        T data;
        Object value;
        boolean opened = true;

        public Node(T data) {
            this.data = data;
        }

        public Node addChild(Node n, boolean close) {
            if (close)
                n.opened = false;

            n.parent = this;
            children.add(n);
            return n;
        }

        public Node freeChild() {
            Node f=null;
            for (Node n: children) {
                if (n.opened)
                    return n;
                if ((f = n.freeChild()) != null)
                    return f;
            }

            return null;
        }

        public void close() {
            opened = false;
        }

    }

    protected Node root, current;
    Stack<T> st=new Stack<T>();

    public DerivationTree() {
        root = new Node(null);
        current = root;
    }

    public void addNode(T data, boolean close) {
        if (close)
        st.push(data);
        if (current == null)
            return;
        current.addChild(new Node(data), close);
    }

    public void advance() {
        Node f=current;

        while (current != null && (f = current.freeChild()) == null) {
            current.close();
            current = current.parent;
        }

        current = f;
    }

    public void setValue(Object value) {
        if (!current.children.isEmpty())
            current.children.elementAt(0).value = value;
    }
}
