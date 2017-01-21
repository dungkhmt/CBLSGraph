package localsearch.domainspecific.graphs.search.moves;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.LexMultiValues;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class ReplaceEdgeVarRootedTreeMove implements GMove {
	private VarRootedTree vt;
	private Edge ei;
	private Edge eo;
	private LexMultiValues eval;
	
	public ReplaceEdgeVarRootedTreeMove(VarRootedTree vt, Edge ei, Edge eo, LexMultiValues eval) {
		// TODO Auto-generated constructor stub
		this.vt = vt;
		this.ei = ei;
		this.eo = eo;
		this.eval = eval;
	}
	@Override
	public void move() {
		// TODO Auto-generated method stub
		vt.replaceEdgePropagate(eo, ei);
	}

	@Override
	public LexMultiValues evaluation() {
		// TODO Auto-generated method stub
		return eval;
	}

}
