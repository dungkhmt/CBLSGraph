package localsearch.domainspecific.graphs.constraints.Leq;

import localsearch.domainspecific.graphs.constraints.GConstraint;
import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.functions.GFunction;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class GLeqFunctionFunction implements GConstraint {
	private GFunction _f1;
	private GFunction _f2;
	private int _violations;
	public GLeqFunctionFunction(GFunction f1, GFunction f2){
		
	}
	
	@Override
	public int getAddEdgeDelta(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		double d1 = _f1.getAddEdgeDelta(vt, e);
		double d2 = _f2.getAddEdgeDelta(vt, e);
		double nv1 = _f1.getValue() + d1;
		double nv2 = _f2.getValue() + d2;
		int newViolations = nv1 <= nv2 ? 0 : (int)(Math.ceil(nv1 - nv2));
		return newViolations - _violations;
	}
	public int getRemoveEdgeDelta(VarRootedTree vt, Edge e){
		return -1;
	}
	public int getReplaceEdgeDelta(VarRootedTree vt, Edge ei, Edge eo){
		return -1;
	}
	public int violations(){
		return _violations;
	}
}
