/*
 * authors: DungPQ
 * date 12/7/2015
 */

package localsearch.domainspecific.graphs.core;

public class QuickRMQ {
	int[]							_A;
	int								_low;
	int								_up;
	int								_n;
	int								_subSize;
	int[]							_B;		
	int[]							_indexB;
	int								_nbrBlocks;
	int[][][]						_table;
	int								_nbrNormalizedBlocks;
	int[]							_p;
	SubRMQ						_rmq;
	public QuickRMQ(int[] A){
		_low = 1;//0;
		_up = A.length-1;
		_n = _up - _low + 1;
		double k2 = log2(_n);
		int x = (int)Math.ceil(k2);
		if(x%2==1)
			x++;
		_subSize = x/2;
		_n = _up;
		_nbrBlocks = _n / _subSize;
		if(_nbrBlocks * _subSize < _n){
			_nbrBlocks++;
		}
		_n = _nbrBlocks * _subSize;
		_A = new int[_n+1];
		for(int i = 0; i <= _n; i++) _A[i] = 0;
		for(int i  = _low; i <= _up; i++)
			_A[i] = A[i];
		for(int i = _up+1; i <= _n; i++){
			_A[i] = _A[i-1] + 1;
		}
		_B = new int[_nbrBlocks+1];
		_indexB = new int[_nbrBlocks+1];
		_nbrNormalizedBlocks = 2^(_subSize-1);
		//_table = new int[0.._nbrNormalizedBlocks-1, 1.._subSize, 1.._subSize];
		_table = new int[_nbrNormalizedBlocks][][];
		for(int i = 0; i < _nbrNormalizedBlocks; i++){
			_table[i] = new int[_subSize+1][];
			for(int j = 1; j <= _subSize; j++)
				_table[i][j] = new int[_subSize+1];
		}
		_p = new int[_nbrBlocks+1];
		_rmq = new SubRMQ(_B);
		initPropagation();	
	}
	private double log2(int n){
		return Math.log(n)*1.0/Math.log(2);
	}
	public void initPropagation(){
		for(int i = 1; i <= _nbrBlocks; i++){
			int s = (i-1)*_subSize;
			int m = 2147463847;//System.getMAXINT();
			int im = -1;
			for(int j = 1; j <= _subSize; j++){
				if(m > _A[s + j]){
					m = _A[s+j];
					im = j;
				}
			}
			_B[i] = m;
			_indexB[i] = im;
		}
		_rmq.initPropagation();
		int K = _subSize - 1;
		int[] b = new int[K+1];
		for(int k = 0; k <= K; k++) b[k] = -1;
		int index = 0;
		//forall(i in 1.._subSize, j in i.._subSize){
		for(int i = 1; i <= _subSize; i++){
			for(int j = 1; j <= _subSize; j++)
			_table[index][i][j] = j;
		}
		boolean finished = false;
		while(!finished){
			int id = 1;
			while(id <= K){
				if(b[id] == 1){
					b[id] = -1;
					id++;
				}else{
					break;					
				}
			}
			if(id > K){
				finished = true;
			}else{
				b[id] = 1;
				index++;
				int a[] = new int [_subSize+1];
				a[1] = 0;
				for(int i = 2; i <= _subSize; i++)
					a[i] = a[i-1] + b[i-1];
				for(int i = 1; i <= _subSize; i++){
					_table[index][i][i] = i;
					for(int j = i+1; j <= _subSize; j++){
						if(a[_table[index][i][j-1]] > a[j])
							_table[index][i][j] = j;
						else
							_table[index][i][j] = _table[index][i][j-1];
					}					
				}
			}
		}
		computeP();
	}
	int getBlock(int i){
		int j = (i-1)/_subSize + 1;
		return j;
	}
	int getOffsetInBlock(int i){
		int j = i - (getBlock(i)-1) * _subSize;
		return j;
	}
	int getMinValue(int i, int j){
		return _A[getMinIndex(i,j)];
	}
	void computeP(){
		for(int i = 1; i <= _nbrBlocks; i++){
			int ind = 0;
			int x = 1;
			int l = (i-1)*_subSize + 1;
			int u = i*_subSize;
			for(int j = l+1; j <= u; j++){
				if(_A[j] - _A[j-1] == 1){
					ind += x;
				}
				x = x * 2;
			}
			_p[i] = ind;		
		}
	}
	void computeP(int i){
			int ind = 0;
			int x = 1;
			int l = (i-1)*_subSize + 1;
			int u = i*_subSize;
			for(int j = l+1; j <= u;  j++){
				if(_A[j] - _A[j-1] == 1){
					ind += x;
				}
				x = x * 2;
			}
			_p[i] = ind;		
	}
	int getNormalizedBlock(int i){
		return _p[i];
	}
	public String getName(){ return "QuickRMQ";}
	public int getMinIndex(int i, int j){
		int bi = getBlock(i);
		int bj = getBlock(j);
		int ii = getOffsetInBlock(i);
		int jj = getOffsetInBlock(j);
		if(bi == bj){
			int bii = getNormalizedBlock(bi);
			int ind = _table[bii][ii][jj];			
			int index = (bi-1)*_subSize + ind;
			return index;
		}
		int im = -1;
		int nbi = getNormalizedBlock(bi);
		int nbj = getNormalizedBlock(bj);
		int im1 = _table[nbi][ii][_subSize];
		im1 = (bi-1)*_subSize + im1;
		int im2 = _table[nbj][1][jj];
		im2 = (bj-1)*_subSize + im2;
		if(_A[im1] < _A[im2]){			
			im = im1;
		}else{
			im = im2;
		}
		if(bi+1 <= bj-1){
			int im3 = _rmq.getMinIndex(bi+1,bj-1);
			im3 = (im3-1)*_subSize + _indexB[im3];
			if(_A[im] > _A[im3])
				im = im3;
		}		
		return im;
	}
	public void propagateUpdate(int i, int j){
		int bi = getBlock(i);
		int bj = getBlock(j);
		for(int ii = bi; ii <= bj; ii++){
			int s = (ii-1)*_subSize;
			int M = 2147483647;//System.getMAXINT();
			int iM = -1;
			for(int jj = 1; jj <= _subSize; jj++){
				if(_A[s+jj] < M){
					M = _A[s+jj];
					iM = jj;
				}
			}
			_B[ii] = M;
			_indexB[ii] = iM;
			_rmq.setValue(ii,_B[ii]);
		}
		_rmq.propagateUpdate(bi,bj);
		for(int k = bi; k <= bj; k++)
			computeP(k);
	}
	public void setValue(int i, int val){
		_A[i] = val;
	}
	int computeMin(int i, int j){
		int M = 2147483647;//System.getMAXINT();
		for(int k = i; k <= j; k++)
			if(M > _A[k])
				M = _A[k];
		return M;
	}
	public void print(){
		System.out.print("_A = ");
		int k = 0;
		for(int i = 1; i <= _nbrBlocks; i++){
			System.out.print("(");
			for(int j = 1; j <= _subSize-1; j++){
				k++;
				System.out.print(_A[k] + ",");
			}
			k++;
			System.out.print(_A[k] + ")");
		}
		System.out.println();
		System.out.println("_B = " + _B);
		System.out.println("_indexB = " + _indexB);
	}
	public boolean verify(){
		System.out.println("QuickRMQ::verify….");
		if(!_rmq.verify())
			return false;
		//forall(i in 1.._n, j in i.._n){
		for(int i = 1; i <= _n; i++)
			for(int j = i; j <= _n; j++){
			int v = computeMin(i,j);
			int vv = getMinValue(i,j);
			int ind = getMinIndex(i,j);
			if(v != vv){
				System.out.println("QuickRMQ::verify -> failure: getMinValue(" + i + "," + j + ") = " +
			vv + " but recompute v = " + v + " index = " + ind);
				return false;
			}
			if(v != _A[ind]){
				System.out.println("QuickRMQ::verify -> failure: getMinValue(" + i + "," + j + ") = " +
			_A[ind] + " but recompute v = " + v);
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
