package localsearch.domainspecific.graphs.tests;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.functions.DegreeOfNode;
import localsearch.domainspecific.graphs.functions.GCost;
import localsearch.domainspecific.graphs.functions.MaxNodeDegree;
import localsearch.domainspecific.graphs.invariants.InsertableEdgesVarRootedTree;
import localsearch.domainspecific.graphs.invariants.NodeDegree;
import localsearch.domainspecific.graphs.invariants.RemovableEdgesVarRootedTree;
import localsearch.domainspecific.graphs.invariants.ReplacingEdgesVarRootedTree;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarRootedTree;
import localsearch.domainspecific.graphs.model.VarUndirectedGraph;
import localsearch.domainspecific.graphs.utils.Utility;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by tung on 20/12/2016.
 */
public class TestGCost {
    public static void main(String Arg[]) {
        UndirectedGraph un = new UndirectedGraph();
        HashMap<Integer,Node> node = new HashMap<Integer,Node>();
        for (int i = 1; i <= 10; ++i) {
            Node v = new Node(i);
            node.put(i,v);
            un.addNode(v);
        }
        try {
            Scanner in = new Scanner(new File("source-code/CBLSGraph/data/t.txt"));
            int i = 1;
            while(in.hasNext()){
                int u = in.nextInt();
                int v = in.nextInt();
                int c = in.nextInt();
                double[] w = new double[1];
                w[0] = c;
                Edge e = new Edge(i++);
                e.setBegin(node.get(u));
                e.setEnd(node.get(v));
                e.setWeight(w);
                un.addEdge(e);
            }
            in.close();



            Node root = un.getNodeByID(1);
            un.print();
            LSGraphManager mgr = new LSGraphManager();
            VarRootedTree Vrt = new VarRootedTree(mgr,un,root);
            InsertableEdgesVarRootedTree I = new InsertableEdgesVarRootedTree(Vrt);
            RemovableEdgesVarRootedTree R = new RemovableEdgesVarRootedTree(Vrt);
            ReplacingEdgesVarRootedTree RPL = new ReplacingEdgesVarRootedTree(Vrt);
            System.out.println(R.toString());



            GCost Gc = new GCost(Vrt,0);
            NodeDegree ND = new NodeDegree(Vrt);
            MaxNodeDegree MND = new MaxNodeDegree(Vrt,un.getNodes(),ND);
            DegreeOfNode DON = new DegreeOfNode(Vrt,node.get(7),ND);
            mgr.close();
            for (int j = 1; j <= 6; ++j) {
                Vrt.addEdgePropagate(un.getEdgeByID(j));
            }
            Vrt.removeEdgePropagate(un.getEdgeByID(6));
            Vrt.removeEdgePropagate(un.getEdgeByID(4));
            Vrt.addEdgePropagate(un.getEdgeByID(6));
            Vrt.addEdgePropagate(un.getEdgeByID(7));

            System.out.println(DON.getValue());
            System.out.println("MaxNodeDegree is " + MND.getValue());
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
