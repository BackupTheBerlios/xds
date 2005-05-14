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
        Rule r;
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

        public String toString() {
            if (children.isEmpty()) {
                return data.toString() + " (" + value + ") ";
            }

            String s = "";
            int[] o = r.getOrder();

            if (o == null) {
                for (Node n: children) {
                    s += n + " ";
                }
            } else {
                for (int x=0; x<o.length; x++)
                    s += children.elementAt(o[x]);
            }

            return s;
        }

        protected void addToStack(Stack<ExecuteStackItem> s) {
            if (children.isEmpty()) {
                if (data instanceof Terminal)
                    s.push(new ExecuteStackItem(data, value));
                return;
            }

            int[] o = r.getOrder();

            if (o == null) {
                for (Node n: children) {
                    n.addToStack(s);
                }
            } else {
                for (int x=0; x<o.length; x++)
                    children.elementAt(o[x]).addToStack(s);
            }
        }
    }

    protected Node root, current;

    public DerivationTree() {
        root = new Node(null);
        current = root;
    }

    public void addNode(T data, boolean close) {
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

        if (current == null)
            current = root;
        else
            current = f;
    }

    public void setValue(Object value) {
        if (!current.children.isEmpty())
            current.children.elementAt(0).value = value;
    }

    public void setRule(Rule r) {
        current.r = r;
    }

    public String toString() {
        return root.toString();
    }

    public Stack<ExecuteStackItem> getExecuteStack() {
        Stack<ExecuteStackItem> s = new Stack<ExecuteStackItem>();
        root.addToStack(s);
        return s;
    }
}
