package localsearch.domainspecific.graphs.search.neighborhoodexploration;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Graph;
import localsearch.domainspecific.graphs.core.LexMultiValues;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.functions.LexMultiFunctions;
import localsearch.domainspecific.graphs.model.VarRootedTree;
import localsearch.domainspecific.graphs.search.GNeighborhood;
import localsearch.domainspecific.graphs.search.moves.NodeOptVarRootedTreeMove;
import localsearch.domainspecific.graphs.search.moves.SubTreeOptVarRootedTreeMove;

/**
 * Created by sev_user on 3/15/2017.
 */
public class SubTreeOptVarRootedTreeNeighborhoodExplorer implements GNeighborhoodExplorer {

    private VarRootedTree vt;
    private LexMultiFunctions f;

    public SubTreeOptVarRootedTreeNeighborhoodExplorer(VarRootedTree vt, LexMultiFunctions f) {
        this.vt = vt;
        this.f = f;
    }

    @Override
    public void exploreNeighborhood(GNeighborhood N) {
        for (Node v : vt.getNodes()) {
            if (v != vt.root()) {
                for (Node u : vt.getNodes()) {
                    if (u != vt.root() && !vt.dominate(v, u)) {
                        LexMultiValues eval = f.getSubTreeOptVarRootedTreeDelta(vt, v, u);
                        N.submit(new SubTreeOptVarRootedTreeMove(vt, v, u, eval));
                    }
                }
            }
        }
    }
}
