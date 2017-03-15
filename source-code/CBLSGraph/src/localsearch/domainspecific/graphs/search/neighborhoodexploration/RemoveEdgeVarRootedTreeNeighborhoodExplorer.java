package localsearch.domainspecific.graphs.search.neighborhoodexploration;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.LexMultiValues;
import localsearch.domainspecific.graphs.functions.LexMultiFunctions;
import localsearch.domainspecific.graphs.invariants.RemovableEdgesVarRootedTree;
import localsearch.domainspecific.graphs.model.VarRootedTree;
import localsearch.domainspecific.graphs.search.GNeighborhood;
import localsearch.domainspecific.graphs.search.moves.RemoveEdgeVarRootedTreeMove;

/**
 * Created by sev_user on 3/15/2017.
 */
public class RemoveEdgeVarRootedTreeNeighborhoodExplorer implements GNeighborhoodExplorer {

    private VarRootedTree vt;
    private LexMultiFunctions f;
    private RemovableEdgesVarRootedTree removable;

    public RemoveEdgeVarRootedTreeNeighborhoodExplorer(RemovableEdgesVarRootedTree removable, LexMultiFunctions f) {
        this.f = f;
        this.removable = removable;
        vt = (VarRootedTree) removable.getVarGraphs()[0];
    }

    @Override
    public void exploreNeighborhood(GNeighborhood N) {
        for (Edge e : removable.getEdges()) {
            LexMultiValues eval = f.getRemoveEdgeVarRootedTreeDelta(vt, e);
            N.submit(new RemoveEdgeVarRootedTreeMove(vt, e, eval));
        }
    }
}
