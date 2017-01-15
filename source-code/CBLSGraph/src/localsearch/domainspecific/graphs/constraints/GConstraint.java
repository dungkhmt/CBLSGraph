package localsearch.domainspecific.graphs.constraints;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.invariants.GInvariant;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public interface GConstraint extends GInvariant {
	public int violations();
	
	public int getAddEdgeDelta(VarGraph vt, Edge e);
	public int getRemoveEdgeDelta(VarGraph vt, Edge e);
	public int getReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo);
	
	// add an edge to vt
	public int getAddEdgeVarRootedTree(VarRootedTree vt, Edge e);
	
	// remove the edge from vt
	public int getRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e);
	
	// add ei to vt will create a cycle, then remove edge edge eo of the cycle --> return a new VarRootedTree
	public int getReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei, Edge eo);
	
	// all children of v will be marked as children of parent p(v) of v
	// v will be inserted between u and parent p(u) of u
	public int getNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u);
	
	
	// remove subtree rooted at v from vt, 
	// and re-insert this subtree between u and parent p(u) of u
	public int getSubTreeOptVarRootedTree(VarRootedTree vt, Node v, Node u);

	
}
