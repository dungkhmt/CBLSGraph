package localsearch.domainspecific.graphs.constraints;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class TreeEdgeDisjoint implements GConstraint {
	
	public TreeEdgeDisjoint(VarRootedTree vt1, VarRootedTree vt2){
		
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
