package localsearch.domainspecific.graphs.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Random;
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
			UndirectedGraph lub = new UndirectedGraph();
			int n = 100;
			Random rand = new Random();
			for (int i = 1; i <= n; i++) {
				for (int j = i + 1; j <= n; j++) {
					if (i != j) {
						lub.addEdgeByID((i - 1) * n + j, i, j);
						Edge e = lub.getEdgeByID((i - 1) * n + j);
						e.setWeight(rand.nextInt(20) + 1);
					}
				}
			}


			LSGraphManager mgr = new LSGraphManager();
			VarRootedTree vt = new VarRootedTree(mgr, lub, lub.getNodeByID(1));
			DiameterTree diameterTree = new DiameterTree(vt, 0);
			InsertableEdgesVarRootedTree I = new InsertableEdgesVarRootedTree(vt);
			RemovableEdgesVarRootedTree R = new RemovableEdgesVarRootedTree(vt);
			ReplacingEdgesVarRootedTree RPL = new ReplacingEdgesVarRootedTree(vt);
			mgr.close(); // duoc goi o day
			
			//Edge eii = Utility.randomSelect(I.getEdges());
			//System.out.println("inseted edge = " + eii.toString());
			//mgr.addEdge(t.txt, eii);
			//t.txt.addEdgePropagate(eii);
			//t.txt.print();
			
			//if(true) return;
			
			
			//t.print();
			System.out.println("Init I = " + Utility.setEdge2String(I.getEdges()) + ", R = " + 
			Utility.setEdge2String(R.getEdges()));

			HashSet<Edge> RP = new HashSet<Edge>();
			for(int it = 0; it < 10000000; it++){
				int choice = rand.nextInt(5);
				if(choice == 0){// add edge
					Edge e = Utility.randomSelect(I.getEdges());
					if(e != null){
						System.out.println(it + ", addEdge(" + e.toString() + "), t.txt = "); //vt.print();
						double d = diameterTree.getAddEdgeVarRootedTree(vt, e);
						double oldV = diameterTree.getValue();
						vt.addEdgePropagate(e);
						double newV = diameterTree.getValue();
						if (Math.abs(newV - (oldV + d)) > 1e-6) {
							//vt.print();
							//diameterTree.print();
							System.out.println("e = " + e);
							System.out.println("oldV = " + oldV + ", delta = " + d + ", newV = " + newV);
							System.exit(-1);
						}

//						RPL.print();
//						R.print();
					}
				}else if(choice == 1){// remove edge
					Edge e = Utility.randomSelect(R.getEdges());
					if(e != null){
						System.out.println(it + ", removeEdge(" + e.toString() + "), t.txt = "); //vt.print();
						double d = diameterTree.getRemoveEdgeVarRootedTree(vt, e);
						double oldV = diameterTree.getValue();
						vt.removeEdgePropagate(e);
						double newV = diameterTree.getValue();
						if (Math.abs(newV - (oldV + d)) > 1e-6) {
							//vt.print();
							//diameterTree.print();
							System.out.println("e = " + e);
							System.out.println("oldV = " + oldV + ", delta = " + d + ", newV = " + newV);
							System.exit(-1);
						}

//						RPL.print();
//						R.print();
					}
				}else if(choice == 2){// replace
					Edge ei = Utility.randomSelect(RPL.getEdges());
					if(ei != null){
						Utility.collectReplacedEdges(vt, ei, RP);
						Edge eo = Utility.randomSelect(RP);
						if(eo != null){
							System.out.println(it + ", replaceEdge(" + ei.toString() + "," + eo.toString() + "), t.txt = "); //vt.print();
							double d = diameterTree.getReplaceEdgeVarRootedTree(vt, ei, eo);
							double oldV = diameterTree.getValue();
							vt.replaceEdgePropagate(ei, eo);
							double newV = diameterTree.getValue();
							if (Math.abs(newV - (oldV + d)) > 1e-6) {
								//vt.print();
								//diameterTree.print();
								System.out.println("ei = " + ei + ", eo = " + eo);
								System.out.println("oldV = " + oldV + ", delta = " + d + ", newV = " + newV);
								System.exit(-1);
							}

//							RPL.print();
//							R.print();
						}
					}
				} else if (choice == 3) {
					Node u = lub.getNodeByID(rand.nextInt(n) + 1);
					Node v = lub.getNodeByID(rand.nextInt(n) + 1);
					// NodeOptVarRootedTree
					if (vt.contains(u) && vt.contains(v) && v != u && vt.getFatherNode(u) != v && v != vt.root() && u != vt.root()) {
						System.out.println(it + ", nodeOpt(" + v + ", " + u + ")");
						double d = diameterTree.getNodeOptVarRootedTree(vt, v, u);
						double oldV = diameterTree.getValue();
						vt.nodeOptVarRootedTreePropagate(v, u);
						double newV = diameterTree.getValue();
						if (Math.abs(newV - (oldV + d)) > 1e-6) {
							//                        vt.print();
							//                        diameterTree.print();
							System.out.println("u = " + u + ", v = " + v);
							System.out.println("oldV = " + oldV + ", delta = " + d + ", newV = " + newV);
							System.exit(-1);
						}
						//break;
					}
				} else {
					Node u = lub.getNodeByID(rand.nextInt(n) + 1);
					Node v = lub.getNodeByID(rand.nextInt(n) + 1);
					if (vt.contains(u) && vt.contains(v) && v != u && !vt.dominate(v, u) && v != vt.root() && u != vt.root()) {
						System.out.println(it + ", subTreeOpt(" + v + ", " + u + ")");
						double d = diameterTree.getSubTreeOptVarRootedTree(vt, v, u);
						double oldV = diameterTree.getValue();
						vt.subTreeOptVarRootedTreePropagate(v, u);
						double newV = diameterTree.getValue();
						if (Math.abs(newV - (oldV + d)) > 1e-6) {
							//vt.print();
							//diameterTree.print();
							System.out.println("u = " + u + ", v = " + v);
							System.out.println("oldV = " + oldV + ", delta = " + d + ", newV = " + newV);
							System.exit(-1);
						}
//						break;
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
