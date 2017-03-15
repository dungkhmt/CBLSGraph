package localsearch.domainspecific.graphs.functions;

import java.util.ArrayList;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.LexMultiValues;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class LexMultiFunctions {
	private ArrayList<GFunction> functions;
	
	public LexMultiFunctions(){
		functions = new ArrayList<GFunction>();
	}
	public void add(GFunction f){
		functions.add(f);
	}
	
	public LexMultiValues getAddEdgeVarRootedTreeDelta(VarRootedTree vt, Edge e) {
		LexMultiValues eval = new LexMultiValues();
		for (GFunction f : functions) {
			eval.add(f.getAddEdgeVarRootedTree(vt, e));
		}
		return eval;
	}

	public LexMultiValues getRemoveEdgeVarRootedTreeDelta(VarRootedTree vt, Edge e) {
		LexMultiValues eval = new LexMultiValues();
		for (GFunction f : functions) {
			eval.add(f.getRemoveEdgeVarRootedTree(vt, e));
		}
		return eval;
	}

	public LexMultiValues getReplaceEdgeVarRootedTreeDelta(VarRootedTree vt, Edge ei, Edge eo){
		LexMultiValues eval = new LexMultiValues();
		for(GFunction f: functions){
			eval.add(f.getReplaceEdgeVarRootedTree(vt, ei, eo));
		}
		return eval;
	}

	public LexMultiValues getNodeOptVarRootedTreeDelta(VarRootedTree vt, Node v, Node u) {
		LexMultiValues eval = new LexMultiValues();
		for (GFunction f : functions) {
			eval.add(f.getNodeOptVarRootedTree(vt, v, u));
		}
		return eval;
	}

	public LexMultiValues getSubTreeOptVarRootedTreeDelta(VarRootedTree vt, Node v, Node u) {
		LexMultiValues eval = new LexMultiValues();
		for (GFunction f : functions) {
			eval.add(f.getSubTreeOptVarRootedTree(vt, v, u));
		}
		return eval;
	}
}
