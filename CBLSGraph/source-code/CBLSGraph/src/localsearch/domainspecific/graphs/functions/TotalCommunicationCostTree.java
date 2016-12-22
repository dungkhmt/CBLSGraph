package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.constraints.GConstraint;
import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class TotalCommunicationCostTree implements GConstraint {
	
	public TotalCommunicationCostTree(VarRootedTree vt, int idxWeight){
		
	}
	@Override
	public int getAddEdgeDelta(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRemoveEdgeDelta(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getReplaceEdgeDelta(VarRootedTree vt, Edge ei, Edge eo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int violations() {
		// TODO Auto-generated method stub
		return 0;
	}

}
