/*
 * authors: DungPQ
 * date 12/7/2015
 */
package localsearch.domainspecific.graphs.core;

public class SubRMQ {
	int[]							_A;
	//range							_R;
	int							_low;
	int							_up;
	int[][]						_rmq;
	int[][]						_rmqPos;
	int								_l;
	int								_count;
	int								_n;
	SubRMQ(int[] A){
		_A = A;
		//_R = _A.rng();
		_low = 1;//0;
		_up = _A.length-1;
		_count = _up - _low + 1;
		_n = (_count + 1)/2;	
		_l = (int)Math.ceil(log2(_n));
		_rmq = new int[_count+1][];
		for(int i = 0; i < _count; i++)
			_rmq[i] = new int[_l+1];
		_rmqPos = new int[_count+1][];
		for(int i = 0; i < _count; i++)
			_rmqPos[i] = new int[_l+1];
		
		//_rmq = new int[1.._count-1, 1.._l];
		//_rmqPos = new int[1.._count-1, 1.._l];
	}
	private double log2(int n){
		return Math.log(n)*1.0/Math.log(2);
	}
	public String name(){
		return "SubRMQ";
	}
	void initPropagation(){
		//System.out.println(name() + "::initPropagation, A.length = " + _A.length + ", count = " + _count);
		for(int i = 1; i <= _count-1; i++){
			if(_A[i] < _A[i+1]){
				_rmq[i][1] = _A[i];
				_rmqPos[i][1] = i;
			}else{
				_rmq[i][1] = _A[i+1];
				_rmqPos[i][1] = i+1;
			}
		}
		for(int j = 2; j <= _l; j++){
			for(int i = 1; i <= _count-1; i++){
				int k = (1 << (j-1))-1;
				if(i+k+1 <= _count-1){
					if(_rmq[i][j-1] < _rmq[i+k+1][j-1]){
						_rmq[i][j] = _rmq[i][j-1];
						_rmqPos[i][j] = _rmqPos[i][j-1];
					}else{
						_rmq[i][j] = _rmq[i+k+1][j-1];
						_rmqPos[i][j] = _rmqPos[i+k+1][j-1];					
					}
				}
			}
		}
	}
	void recompute(int size){
		_count = size;
		for(int i = 1; i <= _count-1; i++){
			if(_A[i] < _A[i+1]){
				_rmq[i][1] = _A[i];
				_rmqPos[i][1] = i;
			}else{
				_rmq[i][1] = _A[i+1];
				_rmqPos[i][1] = i+1;
			}
		}
		for(int j = 2; j <= _l; j++){
			for(int i = 1; i <= _count-1; i++){
				int k = (1 << (j-1))-1;
				if(i+k+1 <= _count-1){
					if(_rmq[i][j-1] < _rmq[i+k+1][j-1]){
						_rmq[i][j] = _rmq[i][j-1];
						_rmqPos[i][j] = _rmqPos[i][j-1];
					}else{
						_rmq[i][j] = _rmq[i+k+1][j-1];
						_rmqPos[i][j] = _rmqPos[i+k+1][j-1];					
					}
				}
			}
		}
	}
	int getMinValue(int i, int j){
		return _A[getMinIndex(i,j)];
	}
	int getMinIndex(int i, int j){
		if(i == j)
			return i;
				int k = (int)Math.floor(log2(j-i+1));
				int k2 = 1 << k;
				if(_rmq[i][k] < _rmq[j-k2+1][k]){
					return _rmqPos[i][k];
				}else{
					return _rmqPos[j-k2+1][k];
				}
	}
	void print(){
		for(int i = 1; i <= _count -1; i++)
			for(int j = 1; j <= _l; j++)
			System.out.print(_rmq[i][j] + " ");
		System.out.println();
		
	}
	private int max(int a, int b){
		return a > b ? a : b;
	}
	private int min(int a, int b){
		return a < b ? a : b;
	}
	void propagateUpdate(int aa, int bb){
		for(int i = max(1,aa-1); i <= min(bb,_count-1); i++){
			if(_A[i] < _A[i+1]){
				_rmq[i][1] = _A[i];
				_rmqPos[i][1] = i;
			}else{
				_rmq[i][1] = _A[i+1];
				_rmqPos[i][1] = i+1;
			}
		}
		for(int j = 2; j <= _l; j++){
			int a = 1;
			int b = _count;
			int pa = aa - (1 << j) + 1;
			int pb = b - (1 << j) + 1;
			int s = a > pa?a:pa;
			int e = bb < pb?bb:pb;
			for(int i = s; i <= e; i++){
				int k = (1 << (j-1))-1;
				if(i+k+1 <= _count-1){
					if(_rmq[i][j-1] < _rmq[i+k+1][j-1]){
						_rmq[i][j] = _rmq[i][j-1];
						_rmqPos[i][j] = _rmqPos[i][j-1];
					}else{
						_rmq[i][j] = _rmq[i+k+1][j-1];
						_rmqPos[i][j] = _rmqPos[i+k+1][j-1];					
					}
				}
			}
		}
	}
	void propagateUpdate(int a, int aa, int bb, int b){
		for(int i = aa-1; i <= bb; i++){
			if(_A[i] < _A[i+1]){
				_rmq[i][1] = _A[i];
				_rmqPos[i][1] = i;
			}else{
				_rmq[i][1] = _A[i+1];
				_rmqPos[i][1] = i+1;
			}
		}
		for(int j = 2; j <= _l; j++){
			int pa = aa - (1 << j) + 1;
			int pb = b - (1 << j) + 1;
			int s = a > pa?a:pa;
			int e = bb < pb?bb:pb;
			for(int i = s; i <= e; i++){
				int k = (1 << (j-1)) -1;
				if(i+k+1 <= _count-1){
					if(_rmq[i][j-1] < _rmq[i+k+1][j-1]){
						_rmq[i][j] = _rmq[i][j-1];
						_rmqPos[i][j] = _rmqPos[i][j-1];
					}else{
						_rmq[i][j] = _rmq[i+k+1][j-1];
						_rmqPos[i][j] = _rmqPos[i+k+1][j-1];					
					}
				}
			}
		}
	}
	void setValue(int i, int val){
		_A[i] = val;
	}
	int computeMin(int i, int j){
		int M = 2147483647;//System.getMAXINT();
		//forall(k in i..j)
		for(int k = i; k <= j; k++)
			if(M > _A[k])
				M = _A[k];
		return M;
	}
	public boolean verify(){
		System.out.println("SubRMQ::verify....");
		for(int i = 1; i <= _count-1; i++)
			for(int j = i+1; j <= _count; j++){
			int v = computeMin(i,j);
			int vv = getMinValue(i,j);
			int ind = getMinIndex(i,j);
			if(v != vv){
				System.out.println("SubRMQ::verify -> failure: getMinValue(" + i + "," + j + ") = " +
			vv + " but recompute v = " + v );
				return false;
			}
			if(v != _A[ind]){
				System.out.println("SubRMQ::verify -> failure: getMinValue(" + i + "," + j + ") = " +
			_A[ind] + " but recompute v = " + v );
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SubRMQ rmq = new SubRMQ(new int[10]);
		System.out.println(rmq.log2(9) + "," + Math.ceil(rmq.log2(9)) + "," + Math.floor(rmq.log2(9)));
	}

}
