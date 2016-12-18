package localsearch.domainspecific.graphs.constraints;

import localsearch.domainspecific.graphs.constraints.Leq.GLeqFunctionFunction;
import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.functions.GFunction;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class GLeq implements GConstraint {
	
	private GConstraint _c;
	public GLeq(GFunction f1, GFunction f2){
		// f1 <= f2
		_c = new GLeqFunctionFunction(f1, f2);
	}

	@Override
	public int getAddEdgeDelta(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		return _c.getAddEdgeDelta(vt, e);
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