package localsearch.domainspecific.graphs.invariants;

import java.util.HashMap;
import java.util.HashSet;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Graph;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class ReplacingEdgesVarRootedTree implements GInvariant {
    private LSGraphManager mgr;
    private VarGraph[] varGraphs;
    private VarRootedTree vt;
    private HashSet<Edge> replacingEdges;
    private int id;

    public ReplacingEdgesVarRootedTree(VarRootedTree vt) {
        // TODO Auto-generated constructor stub
        this.vt = vt;
        this.mgr = vt.getLSGraphManager();
        replacingEdges = new HashSet<Edge>();
        varGraphs = new VarGraph[1];
        varGraphs[0] = vt;
        mgr.post(this);
    }

    public String name() {
        return "ReplacingEdgesVarRootedTree";
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

    @Override
    public void setID(int id) {
        // TODO Auto-generated method stub
        this.id = id;
    }

    @Override
    public int getID() {
        // TODO Auto-generated method stub
        return id;
    }

    public HashSet<Edge> getEdges() {
        return replacingEdges;
    }

    @Override
    public void initPropagate() {
        // TODO Auto-generated method stub
        Graph lub = vt.getLUB();
        for (Node u : vt.getNodes()) {
            for (Edge e : lub.getAdj(u)) {
                Node v = e.otherNode(u);
                if (vt.contains(v) && !vt.contains(e)) {
                    replacingEdges.add(e);
                }
            }
        }
    }

    @Override
    public void propagateAddEdge(VarGraph vt, Edge e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void propagateRemoveEdge(VarGraph vt, Edge e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void propagateReplaceEdge(VarGraph vt, Edge eo, Edge ei) {
        // TODO Auto-generated method stub
    }

	@Override
	public void propagateAddEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
        System.out.println(name() + "::propagateAddEdgeVarRootedTree(" + e.toString() + ")");
        if (this.vt != vt) {
            return;
        }

        Node leaf = e.getBegin();
        Node other = e.getEnd();

        VarRootedTree vrt = (VarRootedTree) vt;
        if (vrt.getFatherNode(other) == leaf) {
            leaf = e.getEnd();
            other = e.getBegin();
        }

        Graph lub = vt.getLUB();
        HashSet<Edge> incidents = lub.getAdj(leaf);
        for (Edge ei : incidents) {
            Node v = ei.otherNode(leaf);
            if (vt.contains(v) && ei != e) {
                replacingEdges.add(ei);
            }
        }
		
	}

	@Override
	public void propagateRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
        System.out.println(name() + "::propagateRemoveEdgeVarRooted(" + e.toString() + ")");
        if (this.vt != vt)
            return;
        Node fv = e.getBegin();
        Node cv = e.getEnd();

        if (vt.contains(cv)) {
            fv = e.getEnd();
            cv = e.getBegin();
        }

        Graph lub = vt.getLUB();
        for (Edge ei : lub.getAdj(cv)) {
            Node other = ei.otherNode(cv);
            if (vt.contains(other)) {
                replacingEdges.remove(ei);
            }
        }
		
	}

	@Override
	public void propagateReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei,
			Edge eo) {
		// TODO Auto-generated method stub
        System.out.println(name() + "::propagateReplaceEdgeVarRootedTree(" + eo.toString() + "," + ei.toString() + ")");
        if (this.vt != vt)
            return;
        replacingEdges.remove(ei);
        replacingEdges.add(eo);
		
	}

	@Override
	public void propagateNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		// TODO Auto-generated method stub
		System.out.println(name() + "::propagateNodeOptVarRootedTree NOT IMPLEMENTED YET");
		assert(false);

	}

	@Override
	public void propagateSubTreeOptVarRootedTree(VarRootedTree vt, Node v,
			Node u) {
		// TODO Auto-generated method stub
		System.out.println(name() + "::propagateSubTreeOptVarRootedTree NOT IMPLEMENTED YET");
		assert(false);

	}

}
