package graph;
import java.util.Arrays;
import java.util.LinkedList;
/**
 * http://blog.csdn.net/liguanxing/article/details/5665520
 * @author dysong
 *
 */
//Kosaraju算法 StrongConnectedComponent
//对正图DFS 然后再对逆序图DFS－－按照第一次的F[U]反向访问
//O(V+E)
public class Kosaraju
{
	LinkedList[]	mapa;
	LinkedList[]	mapt;
	int[]			order;
	int				tag;
	boolean[]		vst;
	int[]			id;
	int				n;
	int				numberOfSCC;

	/**
	 * 
	 * @param mapa
	 *            原图
	 * 
	 */
	public Kosaraju(LinkedList[] mapa)
	{
		this.mapa = mapa;
		n = mapa.length;
		this.mapt = new LinkedList[n];
		for (int i = 0; i < n; i++) {
			mapt[i] = new LinkedList();
		}
		/**
		 * 构建原图的逆图
		 */
		for (int i = 0; i < n; i++) {
			LinkedList ll = mapa[i];
			for (int j = 0; j < ll.size(); j++) {
				mapt[(Integer) ll.get(j)].add(i);
			}
		}
		calc();
	}

	/**
	 * 得到强连通分支数
	 * 
	 * @return
	 */
	public int getNumberOfSCC()
	{
		return numberOfSCC;
	}

	/**
	 * 得到原图当中每个点所属的强联通分支id
	 * 
	 * @return
	 */
	public int[] getIdOfAllPoints()
	{
		return id;
	}

	private void calc()
	{
		order = new int[n];
		tag = 0;
		vst = new boolean[n];
		for (int i = 0; i < n; i++)
			if (vst[i] == false) {
				vst[i] = true;
				dfsa(i);
			}
		id = new int[n];
		tag = 0;
		Arrays.fill(vst, false);
		for (int i = n - 1; i >= 0; i--) {
			if (vst[order[i]] == false) {
				vst[order[i]] = true;
				dfst(order[i]);
				tag++;
			}
		}
		numberOfSCC = tag;
	}

	private void dfsa(int ci)
	{
		for (int i = 0; i < mapa[ci].size(); i++) {
			int next = (Integer) mapa[ci].get(i);
			if (vst[next] == false) {
				vst[next] = true;
				dfsa(next);
			}
		}
		order[tag++] = ci;
	}

	private void dfst(int ci)
	{
		id[ci] = tag;
		for (int i = 0; i < mapt[ci].size(); i++) {
			int next = (Integer) mapt[ci].get(i);
			if (vst[next] == false) {
				vst[next] = true;
				dfst(next);
			}
		}
	}
}