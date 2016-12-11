/*
 * authors: DungPQ
 * date 12/7/2015
 */
package localsearch.domainspecific.graphs.model;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;

import java.util.HashMap;
import java.util.HashSet;

public class VarTree extends VarUndirectedGraph {

    public VarTree(LSGraphManager mgr, UndirectedGraph lub) {
        super(mgr, lub);
        this.lub = lub;
        Adj = new HashMap<Node, HashSet<Edge>>();
    }

}
