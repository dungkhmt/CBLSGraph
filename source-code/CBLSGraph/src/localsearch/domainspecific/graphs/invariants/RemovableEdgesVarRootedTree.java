package localsearch.domainspecific.graphs.invariants;

import java.util.HashSet;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.model.GraphInvariant;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class RemovableEdgesVarRootedTree implements GraphInvariant {
    private LSGraphManager mgr;
    private VarGraph[] varGraphs;
    private VarRootedTree vt;
    private HashSet<Edge> removableEdges;
    private int id;

    public RemovableEdgesVarRootedTree(VarRootedTree vt) {
        this.vt = vt;
        this.mgr = vt.getLSGraphManager();
        removableEdges = new HashSet<Edge>();
        varGraphs = new VarGraph[1];
        varGraphs[0] = vt;
        mgr.post(this);
    }

    public String name() {
        return "RemovableEdgesVarRootedTree";
    }

    @Override
    public VarGraph[] getVarGraphs() {
        // TODO Auto-generated method stub
        return varGraphs;
    }

    @Override
    public LSGraphManager getLSGraphManager() {
        // TODO Auto-generated method stub
        return mgr;
    }

    public HashSet<Edge> getEdges() {
        return removableEdges;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    @Override
    public void initPropagate() {
        // TODO Auto-generated method stub
        System.out.println(name() + "::initPropagate");
        removableEdges.clear();
        for (Node v : vt.getNodes()) {
            if (v != vt.root() && vt.getAdj().get(v).size() == 1) {
                Edge e = vt.getFatherEdge(v);
                removableEdges.add(e);
            }
        }
    }

    @Override
    public void propagateAddEdge(VarRootedTree vt, Edge e) {
        // TODO Auto-generated method stub
        System.out.println(name() + "::propagateAddEdge(" + e.toString() + ")");
        if (this.vt != vt) {
            return;
        }
        if (vt.contains(e.getEnd()) && vt.contains(e.getBegin())) {
            System.out.println(name() + "::propagateAddEdge" + e.toString() + " exception: two endpoints are belongs to the tree, this will create a cycle");
            System.exit(-1);
        }
        if (!vt.contains(e.getEnd()) && !vt.contains(e.getBegin())) {
            System.out.println(name() + "::propagateAddEdge" + e.toString() + " exception: two endpoints are not belongs to the tree, this will create two connected components");
            System.exit(-1);
        }

        if (vt.isNull()) {
            removableEdges.add(e);
            return;
        }

        Node leaf = e.getBegin();
        Node other = e.getEnd();
        if (vt.contains(leaf)) {
            leaf = e.getEnd();
            other = e.getBegin();
        }
        removableEdges.add(e);
        if (vt.getAdj().get(other).size() == 1 && other != vt.root()) {
            for (Edge ei : vt.getAdj().get(other)) {
                removableEdges.remove(ei);
            }
        }
    }

    @Override
    public void propagateRemoveEdge(VarRootedTree vt, Edge e) {
        // TODO Auto-generated method stub
        System.out.println(name() + "::propagateRemoveEdge(" + e.toString() + ")");
        if (vt != vt) {
            return;
        }

        Node fv = e.getBegin();
        Node cv = e.getEnd();
        if (!vt.contains(e)) {
            System.out.println(name() + "::propagateRemoveEdge(" + fv.getID() + "," + cv.getID() + ") -> exception: this edge does not belong to the tree");
            System.exit(-1);
        }
        if (vt.getFatherNode(cv) != fv) {
            fv = e.getEnd();
            cv = e.getBegin();
        }

        if (vt.getAdj().get(cv).size() != 1) {
            System.out.println(name() + "::propagateRemoveEdge(" + fv.getID() + "," + cv.getID() + ") -> exception: this edge is not a leaf of the tree");
            System.exit(-1);
        }

        removableEdges.remove(e);

        UndirectedGraph lub = vt.getLUB();
        if (lub.getAdj().get(fv).size() == 2 && fv != vt.root()) {
            for (Edge ei : vt.getAdj().get(fv)) {
                Node other = ei.otherNode(fv);
                if (other != cv) {
                    removableEdges.add(ei);
                }
            }
        }
    }

    @Override
    public void propagateReplaceEdge(VarRootedTree vt, Edge eo, Edge ei) {
        // TODO Auto-generated method stub
        System.out.println(name() + "::propagateReplaceEdge(" + eo.toString() + ", " + ei.toString() + ")");
        if (this.vt != vt) {
            return;
        }
        if (!vt.contains(eo) || vt.contains(ei)) {
            System.exit(-1);
        }

        Node u1 = ei.getBegin();
        Node v1 = ei.getEnd();
        Node u2 = eo.getBegin();
        Node v2 = eo.getEnd();
        Node r = vt.nca(u1, v1);
        if (vt.getFatherNode(u2) == v2) {
            u2 = eo.getEnd();
            v2 = eo.getBegin();
        }
        if (vt.dominate(v2, u1)) {
            u1 = ei.getEnd();
            v1 = ei.getBegin();
        }

        if (u1 != u2) {
            if (vt.getAdj(u1).size() == 1) {
                for (Edge fe : vt.getAdj(u1)) {
                    removableEdges.remove(fe);
                }
            }
            if (vt.getAdj(u2).size() == 2 && u2 != vt.root()) {
                for (Edge ej : vt.getAdj(u2)) {
                    Node other = ej.otherNode(u2);
                    if (other != v2) {
                        removableEdges.add(ej);
                    }
                }
            }
        } else {
            if (vt.getAdj(u1).size() == 1 && u1 != vt.root()) {
                System.out.println(name() + "::propagateReplaceEdge: this case cannot be happened, but let it for test purpose");
                System.exit(-1);
                removableEdges.remove(eo);
                removableEdges.add(ei);
            }
        }
        if (v1 != v2) {
            if (vt.getAdj(v1).size() == 1) {
                for (Edge fe : vt.getAdj().get(v1)) {
                    removableEdges.remove(fe);
                }
            }
            if (vt.getAdj(v2).size() == 2) {
                for (Edge ej : vt.getAdj().get(v2)) {
                    Node other = ej.otherNode(v2);
                    if (other != u2)
                        removableEdges.add(ej);
                }
            }
        } else {
            if (vt.getAdj(v1).size() == 1) {
                removableEdges.remove(eo);
                removableEdges.add(ei);
            }
        }

    }

}
