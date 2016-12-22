package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.model.GInvariant;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public interface GFunction extends GInvariant{
	public double getAddEdgeDelta(VarRootedTree vt, Edge e);
	public double getRemoveEdgeDelta(VarRootedTree vt, Edge e);
	public double getReplaceEdgeDelta(VarRootedTree vt, Edge ei, Edge eo);
	public double getValue();
}
