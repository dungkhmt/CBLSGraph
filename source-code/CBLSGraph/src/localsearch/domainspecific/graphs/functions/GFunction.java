package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.invariants.GInvariant;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public interface GFunction extends GInvariant{
	public double getValue();
	
	public double getAddEdgeDelta(VarGraph vt, Edge e);
	public double getRemoveEdgeDelta(VarGraph vt, Edge e);
	public double getReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo);
	
	// add an edge to vt
	public double getAddEdgeVarRootedTree(VarRootedTree vt, Edge e);
	
	// remove the edge from vt
	public double getRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e);
	
	// add ei to vt will create a cycle, then remove edge edge eo of the cycle --> return a new VarRootedTree
	public double getReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei, Edge eo);
	
	// all children of v will be marked as children of parent p(v) of v
	// v will be inserted between u and parent p(u) of u
	public double getNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u);
	
	
	// remove subtree rooted at v from vt, 
	// and re-insert this subtree between u and parent p(u) of u
	public double getSubTreeOptVarRootedTree(VarRootedTree vt, Node v, Node u);

}
