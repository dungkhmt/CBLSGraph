package localsearch.domainspecific.graphs.invariants;

import java.util.HashSet;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Graph;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class RemovableEdgesVarRootedTree implements GInvariant {
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
        //System.out.println(name() + "::initPropagate");
        removableEdges.clear();
        for (Node v : vt.getNodes()) {
            if (v != vt.root() && vt.getAdj().get(v).size() == 1) {
                Edge e = vt.getFatherEdge(v);
                removableEdges.add(e);
            }
        }
    }

    @Override
    public void propagateAddEdge(VarGraph vt, Edge e) {
        // TODO Auto-generated method stub
    	// DO NOTHING
    
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

        if (vt.isNull()) {
            removableEdges.add(e);
            return;
        }

        Node leaf = e.getBegin();
        Node other = e.getEnd();
        VarRootedTree vrt = (VarRootedTree) vt;
        if (vrt.getFatherNode(other) == leaf) {
            leaf = e.getEnd();
            other = e.getBegin();
        }
        removableEdges.add(e);
        if (vt.getAdj().get(other).size() == 2 && other != vrt.root()) {
            removableEdges.remove(vrt.getFatherEdge(other));
        }
		
	}

	@Override
	public void propagateRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
        System.out.println(name() + "::propagateRemoveEdgeVarRootedTree(" + e.toString() + ")");
        if (vt != vt) {
            return;
        }

        Node fv = e.getBegin();
        Node cv = e.getEnd();
        VarRootedTree vrt = (VarRootedTree) vt;
        if (vt.contains(cv)) {
            fv = e.getEnd();
            cv = e.getBegin();
        }

        removableEdges.remove(e);

        Graph lub = vt.getLUB();
        if (lub.getAdj().get(fv).size() == 1 && fv != vrt.root()) {
            removableEdges.add(vrt.getFatherEdge(fv));
        }
		
	}

	@Override
	public void propagateReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei,
			Edge eo) {
		// TODO Auto-generated method stub
        System.out.println(name() + "::propagateReplaceEdge(" + eo.toString() + ", " + ei.toString() + ")");
        if (this.vt != vt) {
            return;
        }

        VarRootedTree vrt = (VarRootedTree) vt;

        Node u1 = ei.getBegin();
        Node v1 = ei.getEnd();
        Node u2 = eo.getBegin();
        Node v2 = eo.getEnd();
        if (vrt.getFatherNode(u1) == v1) {
            u1 = ei.getEnd();
            v1 = ei.getBegin();
        }
        if (vrt.dominate(v1, u2)) {
            u2 = eo.getEnd();
            v2 = eo.getBegin();
        }

        if (u1 != u2) {
            if (u1 != vrt.root() && vt.getAdj(u1).size() == 2) {
                removableEdges.remove(vrt.getFatherEdge(u1));
            }
            if (vt.getAdj(u2).size() == 1 && u2 != vrt.root()) {
                removableEdges.add(vrt.getFatherEdge(u2));
            }
        } else {
            if (vt.getAdj(u1).size() == 1 && u1 != vrt.root()) {
                System.out.println(name() + "::propagateReplaceEdge: this case cannot be happened, but let it for test purpose");
                System.exit(-1);
                removableEdges.remove(eo);
                removableEdges.add(ei);
            }
        }
        if (v1 != v2) {
            if (vt.getAdj(v1).size() == 2) {
                for (Edge e : vt.getAdj(v1)) {
                    Node other = e.otherNode(v1);
                    if (vrt.getFatherNode(other) == v1) {
                        removableEdges.remove(e);
                    }
                }
            }
            if (vt.getAdj(v2).size() == 1) {
                removableEdges.add(vrt.getFatherEdge(v2));
            }
        } else {
            if (vt.getAdj(v1).size() == 1) {
                removableEdges.remove(eo);
                removableEdges.add(ei);
            }
        }
		
	}

	@Override
	public void propagateNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		// TODO Auto-generated method stub
		//System.out.println(name() + "::propagateNodeOptVarRootedTree NOT IMPLEMENTED YET");
		//assert(false);
        if (vt != this.vt) {
            System.out.println(name() + "::propagateNodeOptVarRootedTree (v, u) = (" + v + ", " + u + ")");
            System.exit(-1);
        }
        removableEdges.clear();
        for (Edge e : vt.getEdges()) {
            if (vt.getAdj(e.getBegin()).size() == 1 || vt.getAdj(e.getEnd()).size() == 1) {
                removableEdges.add(e);
            }
        }
	}

	@Override
	public void propagateSubTreeOptVarRootedTree(VarRootedTree vt, Node v,
			Node u) {
		// TODO Auto-generated method stub
		//System.out.println(name() + "::propagateSubTreeOptVarRootedTree NOT IMPLEMENTED YET");
		//assert(false);
        if (vt != this.vt) {
            System.out.println(name() + "::propagateSubTreeOptVarRootedTree (v, u) = (" + v + ", " + u + ")");
            System.exit(-1);
        }
        removableEdges.clear();
        for (Edge e : vt.getEdges()) {
            if (vt.getAdj(e.getBegin()).size() == 1 || vt.getAdj(e.getEnd()).size() == 1) {
                removableEdges.add(e);
            }
        }
	}

}
