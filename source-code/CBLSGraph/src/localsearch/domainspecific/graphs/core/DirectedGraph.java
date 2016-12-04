/*
 * authors: DungPQ
 * date 12/7/2015
 */
package localsearch.domainspecific.graphs.core;

import java.util.HashMap;
import java.util.ArrayList;

public class DirectedGraph extends Graph {
	private HashMap<Node, ArrayList<Edge>> incomingArcs;
	private HashMap<Node, ArrayList<Edge>> outgoingArcs;
	public DirectedGraph(){
		
	}
}
