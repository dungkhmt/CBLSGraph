package localsearch.domainspecific.graphs.constraints;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public interface GConstraint {
	public int getAddEdgeDelta(VarRootedTree vt, Edge e);
	public int getRemoveEdgeDelta(VarRootedTree vt, Edge e);
	public int getReplaceEdgeDelta(VarRootedTree vt, Edge ei, Edge eo);
	public int violations();
	
	
}
