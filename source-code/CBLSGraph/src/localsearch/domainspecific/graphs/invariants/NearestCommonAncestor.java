package localsearch.domainspecific.graphs.invariants;

import javax.transaction.xa.Xid;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

import java.util.HashMap;

public class NearestCommonAncestor implements GInvariant{

	private int id;
	private VarGraph[] varGraphs;
	private LSGraphManager mgr;
	private VarRootedTree vt;
	
	private int n;
	private int m;
	private int count;
	private int[] p;
	private int[] level;
	private int[][] rmq;
	private int[] log2;
	private int sizeBlock;
	private int[][][] table;
	private Node[] eulerTour;
	private HashMap<Node, Integer> pos;

	public NearestCommonAncestor(VarRootedTree vt) {
		this.vt = vt;
		this.mgr = vt.getLSGraphManager();
		varGraphs = new VarGraph[1];
		varGraphs[0] = vt;
		mgr.post(this);
	}
	
	public void initPropagate() {
		n = vt.getLUB().getNbrNodes();
		eulerTour = new Node[2 * n - 1];
		level = new int[2 * n - 1];
		
//		int maxId = 0;
//		for (Node u : vt.getLUB().getNodes()) {
//			maxId = Math.max(maxId, u.getID());
//		}
//		pos = new int[maxId + 1];
		pos = new HashMap<Node, Integer>();

		log2 = new int[2 * n];
		for (int i = 0, j = 0; i < log2.length; i++) {
			if (i == (1 << (j + 1))) {
				j++;
			}
			log2[i] = j;
		}
		
		sizeBlock = Math.max(1, (log2[2 * n - 1] + 1) / 2);
		table = new int[1 << (sizeBlock - 1)][sizeBlock][sizeBlock];
		int[] x = new int[sizeBlock];
		for (int t = 0; t < (1 << (sizeBlock - 1)); t++) {
			x[0] = 0;
			for (int i = 1; i < sizeBlock; i++) {
				x[i] = x[i - 1] + (((t >> (i - 1)) & 1) == 1 ? 1 : -1);
			}
			for (int i = 0; i < sizeBlock; i++) {
				table[t][i][i] = i;
			}
			for (int u = 1; u < sizeBlock; u++) {
				for (int i = 0; i + u < sizeBlock; i++) {
					int j = i + u;
					if (x[table[t][i][j - 1]] > x[table[t][i + 1][j]]) {
						table[t][i][j] = table[t][j][i] = table[t][i + 1][j];
					} else {
						table[t][i][j] = table[t][j][i] = table[t][i][j - 1];
					}
				}
			}
		}
		
		m = (2 * n - 2) / sizeBlock + 1;
		rmq = new int[m][log2[m] + 1];
		p = new int[m];

		makeNCA();
	}
	
	private void DFS(Node u, int l) {
		level[count] = l;
		pos.put(u, count);//pos[u.getID()] = count;
		eulerTour[count++] = u;
		Node fu = vt.getFatherNode(u);
		if (vt.isNull()) {
			return;
		}
		for (Edge e : vt.getAdj(u)) {
			Node v = e.otherNode(u);
			if (v != fu) {
				DFS(v, l + 1);
				level[count] = l;
				eulerTour[count++] = u;
			}
		}
	}
	
	public void makeNCA() {
		count = 0;
		DFS(vt.root(), 0);
		
		m = (count - 1) / sizeBlock + 1;
		for (int b = 0; b < m; b++) {
			rmq[b][0] = b * sizeBlock;
			int l = b * sizeBlock + 1;
			int r = Math.min(count, l + sizeBlock - 1);
			for (int i = l; i < r; i++) {
				if (level[i] < level[rmq[b][0]]) {
					rmq[b][0] = i;
				}
			}
			
			int x = 0;
			int y = 1;
			for (int i = l; i < r; i++) {
				if (level[i] > level[i - 1]) {
					x += y;
				}
				y <<= 1;
			}
			p[b] = x;
		}
		
		for (int i = 1, step = 1; i <= log2[m]; i++, step <<= 1) {
			for (int j = 0; j + 2 * step < m; j++) {
				if (level[rmq[j][i - 1]] < level[rmq[j + step][i - 1]]) {
					rmq[j][i] = rmq[j][i - 1];
				} else {
					rmq[j][i] = rmq[j + step][i - 1];
				}
			}
		}
	}
	
	public Node nca(Node u, Node v) {
		int i = pos.get(u);//pos[u.getID()];
		int j = pos.get(v);//pos[v.getID()];
		if (i > j) {
			//j = pos[u.getID()];
			//i = pos[v.getID()];
			int tmp = i;
			i = j; j = tmp;
		}
		int bi = i / sizeBlock;
		int bj = j / sizeBlock;
		int oi = i % sizeBlock;
		int oj = j % sizeBlock;
		if (bi == bj) {
			return eulerTour[bi * sizeBlock + table[p[bi]][oi][oj]];
		}
		int vi = bi * sizeBlock + table[p[bi]][oi][sizeBlock - 1];
		int vj = bj * sizeBlock + table[p[bj]][0][oj];
		int res = (level[vi] < level[vj]) ? vi : vj;
		if (bi + 1 < bj) {
			bi++;
			bj--;
			int b = log2[bj - bi + 1];
			if (level[rmq[bi][b]] > level[rmq[bj - (1 << b) + 1][b]]) {
				if (level[rmq[bj - (1 << b) + 1][b]] < level[res]) {
					res = rmq[bj - (1 << b) + 1][b]; 
				}
			} else {
				if (level[rmq[bi][b]] < level[res]) {
					res = rmq[bi][b];
				}
			}
		}
		return eulerTour[res];
	}

	@Override
	public VarGraph[] getVarGraphs() {
		return varGraphs;
	}

	@Override
	public LSGraphManager getLSGraphManager() {
		return mgr;
	}

	@Override
	public void setID(int id) {
		this.id = id;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void propagateAddEdge(VarGraph vt, Edge e) {
		makeNCA();
	}

	@Override
	public void propagateRemoveEdge(VarGraph vt, Edge e) {
		makeNCA();
	}

	@Override
	public void propagateReplaceEdge(VarGraph vt, Edge eo, Edge ei) {
		makeNCA();
	}

	@Override
	public void propagateAddEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei,
			Edge eo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagateSubTreeOptVarRootedTree(VarRootedTree vt, Node v,
			Node u) {
		// TODO Auto-generated method stub
		
	}
}
