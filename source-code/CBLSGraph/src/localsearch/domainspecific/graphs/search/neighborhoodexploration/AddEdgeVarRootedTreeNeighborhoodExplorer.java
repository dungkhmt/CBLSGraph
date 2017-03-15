package localsearch.domainspecific.graphs.search.neighborhoodexploration;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.LexMultiValues;
import localsearch.domainspecific.graphs.functions.LexMultiFunctions;
import localsearch.domainspecific.graphs.invariants.InsertableEdgesVarRootedTree;
import localsearch.domainspecific.graphs.model.VarRootedTree;
import localsearch.domainspecific.graphs.search.GNeighborhood;
import localsearch.domainspecific.graphs.search.moves.AddEdgeVarRootedTreeMove;

/**
 * Created by sev_user on 3/15/2017.
 */
public class AddEdgeVarRootedTreeNeighborhoodExplorer implements GNeighborhoodExplorer {

    private VarRootedTree vt;
    private LexMultiFunctions f;
    private InsertableEdgesVarRootedTree insertable;

    public AddEdgeVarRootedTreeNeighborhoodExplorer(InsertableEdgesVarRootedTree insertable, LexMultiFunctions f) {
        this.f = f;
        this.insertable = insertable;
        vt = (VarRootedTree) insertable.getVarGraphs()[0];
    }

    @Override
    public void exploreNeighborhood(GNeighborhood N) {
        for (Edge e : insertable.getEdges()) {
            LexMultiValues eval = f.getAddEdgeVarRootedTreeDelta(vt, e);
            N.submit(new AddEdgeVarRootedTreeMove(vt, e, eval));
        }
    }
}
