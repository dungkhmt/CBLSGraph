package localsearch.domainspecific.graphs.constraints;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.invariants.GInvariant;
import localsearch.domainspecific.graphs.model.VarGraph;

public interface GConstraint extends GInvariant {
	public int getAddEdgeDelta(VarGraph vt, Edge e);
	public int getRemoveEdgeDelta(VarGraph vt, Edge e);
	public int getReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo);
	public int violations();
	
	
}
