package localsearch.domainspecific.graphs.core;

public class Edge extends BasicGraphElement {

    private Node begin;
    private Node end;

    public Edge(int id) {
        super(id);
    }

    public void setBegin(Node begin) {
        this.begin = begin;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public Node getBegin() {
        return begin;
    }

    public Node getEnd() {
        return end;
    }

    public Node otherNode(Node u) {
        if (begin == u) {
            return end;
        }
        if (end == u) {
            return begin;
        }
        return null;
    }

    public boolean contains(Node u) {
        return begin == u || end == u;
    }

    public String toString() {
        return " (" + begin.getID() + ", " + end.getID() + ") " + getWeight();
    }
}
