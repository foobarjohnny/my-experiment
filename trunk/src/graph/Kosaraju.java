package graph;
import java.util.Arrays;
import java.util.LinkedList;
/**
 * http://blog.csdn.net/liguanxing/article/details/5665520
 * @author dysong
 *
 */
//Kosaraju�㷨 StrongConnectedComponent
//����ͼDFS Ȼ���ٶ�����ͼDFS�������յ�һ�ε�F[U]�������
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
	 *            ԭͼ
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
		 * ����ԭͼ����ͼ
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
	 * �õ�ǿ��ͨ��֧��
	 * 
	 * @return
	 */
	public int getNumberOfSCC()
	{
		return numberOfSCC;
	}

	/**
	 * �õ�ԭͼ����ÿ����������ǿ��ͨ��֧id
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