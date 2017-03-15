package localsearch.domainspecific.graphs.search.neighborhoodexploration;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Graph;
import localsearch.domainspecific.graphs.core.LexMultiValues;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.functions.LexMultiFunctions;
import localsearch.domainspecific.graphs.model.VarRootedTree;
import localsearch.domainspecific.graphs.search.GNeighborhood;
import localsearch.domainspecific.graphs.search.moves.NodeOptVarRootedTreeMove;

/**
 * Created by sev_user on 3/15/2017.
 */
public class NodeOptVarRootedTreeNeighborhoodExplorer implements GNeighborhoodExplorer {

    private VarRootedTree vt;
    private LexMultiFunctions f;

    public NodeOptVarRootedTreeNeighborhoodExplorer(VarRootedTree vt, LexMultiFunctions f) {
        this.vt = vt;
        this.f = f;
    }

    private boolean check(Node v, Node u) {
        if (v != u && vt.getFatherNode(u) != v) {
            Graph g = vt.getLUB();
            if (g.getEdge(v, u) == null || g.getEdge(vt.getFatherNode(u), v) == null) {
                return false;
            }
            Node fv = vt.getFatherNode(v);
            for (Edge e : vt.getAdj(v)) {
                Node child = e.otherNode(v);
                if (child != fv) {
                    if (g.getEdge(fv, child) == null) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void exploreNeighborhood(GNeighborhood N) {
        for (Node v : vt.getNodes()) {
            if (v != vt.root()) {
                for (Node u : vt.getNodes()) {
                    if (u != vt.root() && check(v, u)) {
                        LexMultiValues eval = f.getNodeOptVarRootedTreeDelta(vt, v, u);
                        N.submit(new NodeOptVarRootedTreeMove(vt, v, u, eval));
                    }
                }
            }
        }
    }
}
