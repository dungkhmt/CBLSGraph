package localsearch.domainspecific.graphs.search.moves;

import localsearch.domainspecific.graphs.core.LexMultiValues;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.model.VarRootedTree;

/**
 * Created by sev_user on 3/15/2017.
 */
public class SubTreeOptVarRootedTreeMove implements GMove {

    private Node v;
    private Node u;
    private VarRootedTree vt;
    private LexMultiValues eval;

    public SubTreeOptVarRootedTreeMove(VarRootedTree vt, Node v, Node u, LexMultiValues eval) {
        this.v = v;
        this.u = u;
        this.vt = vt;
        this.eval = eval;
    }

    @Override
    public void move() {
        vt.subTreeOptVarRootedTreePropagate(v, u);
    }

    @Override
    public LexMultiValues evaluation() {
        return eval;
    }
}
