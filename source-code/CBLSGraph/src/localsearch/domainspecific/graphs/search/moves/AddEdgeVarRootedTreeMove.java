package localsearch.domainspecific.graphs.search.moves;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.LexMultiValues;
import localsearch.domainspecific.graphs.model.VarRootedTree;

/**
 * Created by sev_user on 3/15/2017.
 */
public class AddEdgeVarRootedTreeMove implements GMove {

    private Edge e;
    private VarRootedTree vt;
    private LexMultiValues eval;

    public AddEdgeVarRootedTreeMove(VarRootedTree vt, Edge e, LexMultiValues eval) {
        this.e = e;
        this.vt = vt;
        this.eval = eval;
    }

    @Override
    public void move() {
        vt.addEdgePropagate(e);
    }

    @Override
    public LexMultiValues evaluation() {
        return eval;
    }
}
