/*
 * authors: DungPQ
 * date 12/7/2015
 */
package localsearch.domainspecific.graphs.core;
import java.util.*;
public class DFS {
	private int				_count;
	private Node[]	_ET;
	private int[]			_level;
	private int[]			_ind;
	private HashMap<Node, Node>	_father;
	public DFS(Node[] ET, int[] level, int[] ind, HashMap<Node, Node> father){
		_ET = ET;
		_level = level;
		_ind = ind;
		_father = father;
	}
	public void performDFS(Node root, HashMap<Node, HashSet<Edge>> adjEdges){
		_count = 0;
		for(int i = 0; i < _level.length; i++)
			_level[i] = 0;
		for(int i = 0; i < _ind.length; i++)
			_ind[i] = 0;
		_father.put(root, root);
		dfs(root,adjEdges,1);
	}
	public int getSize(){
		return _count;
	}
	void dfs(Node v, HashMap<Node, HashSet<Edge>> adjEdges, int level){
		_count++;
			_ET[_count] = v;
			if(_ind[v.getID()]==0)
				_ind[v.getID()] = _count;
			_level[_count] = level;
			for(Edge e : adjEdges.get(v)){
				Node vi = e.otherNode(v);
				if(_father.get(vi) == null){
					_father.put(vi, v);
					dfs(vi, adjEdges, level+1);
					_count++;
					_ET[_count] = v;
					_level[_count] = level;
				}
			}
	}		

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
