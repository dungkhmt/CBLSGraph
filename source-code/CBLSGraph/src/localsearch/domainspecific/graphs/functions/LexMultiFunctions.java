package localsearch.domainspecific.graphs.functions;

import java.util.ArrayList;

import localsearch.domainspecific.graphs.core.Edge;
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
	
	public LexMultiValues getReplaceEdgeVarRootedTreeDelta(VarRootedTree vt, Edge ei, Edge eo){
		LexMultiValues eval = new LexMultiValues();
		for(GFunction f: functions){
			eval.add(f.getReplaceEdgeVarRootedTree(vt, ei, eo));
		}
		return eval;
	}
}
