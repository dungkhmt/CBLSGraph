package localsearch.domainspecific.graphs.search;

import java.util.ArrayList;

import localsearch.domainspecific.graphs.core.LexMultiValues;
import localsearch.domainspecific.graphs.search.moves.GMove;

public class GNeighborhood {
	private ArrayList<GMove> moves;
	private LexMultiValues eval;
	public void addMove(GMove m){
		moves.add(m);
	}
	public void submit(GMove m){
		if(m.evaluation().lt(eval)){
			clear();
			moves.add(m);
			eval = m.evaluation();
		}
	}
	public void clear(){
		moves.clear();
	}
}
