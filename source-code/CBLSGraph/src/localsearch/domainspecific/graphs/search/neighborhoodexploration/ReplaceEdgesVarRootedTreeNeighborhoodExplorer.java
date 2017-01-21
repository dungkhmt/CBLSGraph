package localsearch.domainspecific.graphs.search.neighborhoodexploration;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.LexMultiValues;
import localsearch.domainspecific.graphs.core.Node; 
import localsearch.domainspecific.graphs.functions.LexMultiFunctions;
import localsearch.domainspecific.graphs.invariants.ReplacingEdgesVarRootedTree;
import localsearch.domainspecific.graphs.model.VarRootedTree;
import localsearch.domainspecific.graphs.search.GNeighborhood;
import localsearch.domainspecific.graphs.search.moves.ReplaceEdgeVarRootedTreeMove;

public class ReplaceEdgesVarRootedTreeNeighborhoodExplorer implements GNeighborhoodExplorer{

	private VarRootedTree vt;
	private LexMultiFunctions F;
	private ReplacingEdgesVarRootedTree replacingEdges;
	
	public ReplaceEdgesVarRootedTreeNeighborhoodExplorer(ReplacingEdgesVarRootedTree replacingEdges, 
			LexMultiFunctions F){
		this.vt = (VarRootedTree)replacingEdges.getVarGraphs()[0];
		this.replacingEdges = replacingEdges;
		this.F = F;
	}
	@Override
	public void exploreNeighborhood(GNeighborhood N) {
		// TODO Auto-generated method stub
		for(Edge ei: replacingEdges.getEdges()){
			Node u = ei.getBegin();
			Node v = ei.getEnd();
			
			Node uv = vt.nca(u, v);
			Node x = u;
			while(x != uv){
				Edge eo = vt.getFatherEdge(x);
				LexMultiValues eval = F.getReplaceEdgeVarRootedTreeDelta(vt, ei, eo);
				N.submit(new ReplaceEdgeVarRootedTreeMove(vt, ei, eo, eval));
				x = vt.getFatherNode(x);
			}
			x = v;
			while(x != uv){
				Edge eo = vt.getFatherEdge(x);
				LexMultiValues eval = F.getReplaceEdgeVarRootedTreeDelta(vt, ei, eo);
				N.addMove(new ReplaceEdgeVarRootedTreeMove(vt, ei, eo,eval));
				x = vt.getFatherNode(x);
			}
		}
	}
	
}
