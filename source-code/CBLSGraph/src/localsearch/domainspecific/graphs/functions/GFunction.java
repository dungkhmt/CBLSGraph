package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.invariants.GInvariant;
import localsearch.domainspecific.graphs.model.VarGraph;

public interface GFunction extends GInvariant{
	public double getAddEdgeDelta(VarGraph vt, Edge e);
	public double getRemoveEdgeDelta(VarGraph vt, Edge e);
	public double getReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo);
	public double getValue();
}
