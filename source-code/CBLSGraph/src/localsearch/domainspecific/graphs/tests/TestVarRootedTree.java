package localsearch.domainspecific.graphs.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.functions.DiameterTree;
import localsearch.domainspecific.graphs.invariants.InsertableEdgesVarRootedTree;
import localsearch.domainspecific.graphs.invariants.RemovableEdgesVarRootedTree;
import localsearch.domainspecific.graphs.invariants.ReplacingEdgesVarRootedTree;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarRootedTree;
import localsearch.domainspecific.graphs.utils.Utility;
import java.util.HashSet;

public class TestVarRootedTree {

	public void test(){
		try{
			Scanner in = new Scanner(new File("source-code/CBLSGraph/data/g.txt"));
			HashMap<Integer, Node> m = new HashMap<Integer, Node>();
			UndirectedGraph ug = new UndirectedGraph();
			int n = 20;
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					if (i != j) {
						ug.addEdgeByID((i - 1) * n + j, i, j);
					}
				}
			}
//			while(in.hasNext()){
//				int vid = in.nextInt();
//				if(vid == -1) break;
//				Node v = new Node(vid);
//				m.put(vid, v);
//			}
//			int eid = -1;
//			while(in.hasNext()){
//				int uid = in.nextInt();
//				if(uid == -1) break;
//				int vid = in.nextInt();
//				eid++;
//				ug.addEdgeByID(eid,uid,vid);
//			}
//			in.close();
//			ug.print();
//

			LSGraphManager mgr = new LSGraphManager();
			Node r = ug.getNodeByID(1);
			VarRootedTree t = new VarRootedTree(mgr,ug,r);
			InsertableEdgesVarRootedTree I = new InsertableEdgesVarRootedTree(t);
			RemovableEdgesVarRootedTree R = new RemovableEdgesVarRootedTree(t);
			ReplacingEdgesVarRootedTree RPL = new ReplacingEdgesVarRootedTree(t);
			DiameterTree diameterTree = new DiameterTree(t);
			mgr.close(); // duoc goi o day
			
			//Edge eii = Utility.randomSelect(I.getEdges());
			//System.out.println("inseted edge = " + eii.toString());
			//mgr.addEdge(t.txt, eii);
			//t.txt.addEdgePropagate(eii);
			//t.txt.print();
			
			//if(true) return;
			
			
			t.print();
			System.out.println("Init I = " + Utility.setEdge2String(I.getEdges()) + ", R = " + 
			Utility.setEdge2String(R.getEdges()));
			
			java.util.Random rand = new java.util.Random();
			HashSet<Edge> RP = new HashSet<Edge>();
			for(int it = 0; it < 100000; it++){
				int choice = rand.nextInt(3);
				if(choice == 0){// add edge
					Edge e = Utility.randomSelect(I.getEdges());
					if(e != null){
						t.addEdgePropagate(e);
						System.out.println(it + ", addEdge(" + e.toString() + "), t.txt = "); t.print();
					}
				}else if(choice == 1){// remove edge
					Edge e = Utility.randomSelect(R.getEdges());
					if(e != null){
						t.removeEdgePropagate(e);
						System.out.println(it + ", removeEdge(" + e.toString() + "), t.txt = "); t.print();
					}
				}else if(choice == 2){// replace
					Edge ei = Utility.randomSelect(RPL.getEdges());
					if(ei != null){
						Utility.collectReplacedEdges(t, ei, RP);
						Edge eo = Utility.randomSelect(RP);
						if(eo != null){
							t.replaceEdgePropagate(eo, ei);
							System.out.println(it + ", replaceEdge(" + eo.toString() + "," + ei.toString() + "), t.txt = "); t.print();
						}
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestVarRootedTree T = new TestVarRootedTree();
		T.test();
	}

}
