/**  
 *   
 */
package ArtificialIntelligence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * http://nopainnogain.iteye.com/blog/852002 �˻�
 * ģ���˻��㷨��Դ�ڹ����˻�ԭ���������������ָߣ�������������ȴ
 * ������ʱ�������ڲ�������������Ϊ����״���������󣬶�������ȴʱ���ӽ���������ÿ���¶ȶ��ﵽƽ��̬
 * ������ڳ���ʱ�ﵽ��̬�����ܼ�Ϊ��С������Metropolis׼��
 * ���������¶�Tʱ����ƽ��ĸ���Ϊe-��E/(kT)������EΪ�¶�Tʱ�����ܣ���EΪ��ı���
 * ��kΪBoltzmann�������ù����˻�ģ������Ż����⣬������Eģ��ΪĿ�꺯��ֵf
 * ���¶�T�ݻ��ɿ��Ʋ���t�����õ�������Ż������ģ���˻��㷨���ɳ�ʼ��i�Ϳ��Ʋ�����ֵt��ʼ
 * ���Ե�ǰ���ظ��������½������Ŀ�꺯��������ܻ��������ĵ���������˥��tֵ
 * ���㷨��ֹʱ�ĵ�ǰ�⼴Ϊ���ý������Ž⣬���ǻ������ؿ��޵�����ⷨ��һ������ʽ����������̡��˻��������ȴ���ȱ�(Cooling
 * Schedule)���ƣ��������Ʋ����ĳ�ֵt����˥�����Ӧ�t��ÿ��tֵʱ�ĵ�������L��ֹͣ����S��
 */
/**
 * http://blog.csdn.net/yuanqingfei/article/details/36584
 * ����ɽ����SA��GA���������� ���ࣺ ��Academic Endless�� 2004-07-08 00:28 723���Ķ� ����(1) �ղ� �ٱ�
 * ��Դ��comp.ai.neural-nets
 * 
 * ��ע�⣬��Ŀǰ���۵�������ɽ���У���������ϣ�����￿�����������ɽ���������ܱ�֤��ɽ������������壬������һ���ǳ��ߵķ壬����ʹ�õķ�������ͼ�ҵ�ʵ��ȫ������ֵ
 * ��
 * 
 * ��SA��ģ���˻��㷨���У���������ˣ������������Ծ�˺ܳ�ʱ�䣬���ǣ������������˲����ŷ嶥��ȥ��
 * 
 * ��GA���Ŵ��㷨���У��кܶ�������ǽ��䵽ϲ������ɽ��������ط����������Աû����ʧ���򣩡���Щ���󲢲�֪�����Ǳ�����Ѱ����������壬��ÿ�����꣬
 * �����һЩ�߶Ƚϵ͵صط���ɱһЩ���󣬲�ϣ�������������Щ�Ƕ���أ���������������Ů��.
 */
/**
 * �˹������㷨- �Ż��㷨 http://blog.csdn.net/soso_blog/article/details/5815635
 * */
// α������:
// Simulated-Annealing()
// Create initial solution S
// repeat
// for i=1 to iteration-length do
// Generate a random transition from S to Si
// If ( C(S) <= C(Si) ) then
// S=Si
// else if( exp(C(S)-C(Si))/kt > random[0,1) ) then
// S=Si
// Reduce Temperature t
// until ( no change in C(S) )
//
// C(S): Cost or Loss function of Solution S
// �˻���TSP
public class SimulatedAnnealing
{

	private static double[][]	city;
	private static int[]		currPath;
	private static int[]		bestPath;
	private static double		shortesDistance;
	private static int			numOfCity	= 20;
	// trace item
	private static int			iterator	= 0;

	public void printInfo()
	{
		System.out.println("bestPath: " + Arrays.toString(bestPath));
		System.out.println("shortest distance: " + shortesDistance);
		System.out.println("iterator times: " + iterator);
	}

	private void init() throws IOException
	{
		city = new double[numOfCity][numOfCity];
		currPath = new int[numOfCity];
		bestPath = new int[numOfCity];
		shortesDistance = 0;
		loadCity();
		int lenth = currPath.length;
		for (int i = 0; i < lenth; i++) {
			currPath[i] = i;
		}
	}

	private void loadCity() throws IOException
	{
		// DistanceMatrix.csv" a file stores the distance info.
		File file = new File("/sdy-experiment.googlecode.com/svn/trunk/src/ArtificialIntelligence/DistanceMatrix.txt");
		inputGraph(file, city);
	}

	private void inputGraph(File file, double[][] city) throws IOException
	{
		BufferedReader in = new BufferedReader(new FileReader(file));
		String str = "";
		int length = 0;
		while ((str = in.readLine()) != null) {
			str = str.replaceAll(", ", ",");
			String[] line = str.split(",");
			for (int j = 0; j < numOfCity; j++)
				// ten cities
				city[length][j] = Double.parseDouble(line[j]);
			length++;
		}
	}

	/**
	 * key function
	 * 
	 * @throws IOException
	 */
	public void anneal() throws IOException
	{

		double temperature = 10000.0D;
		double deltaDistance = 0.0D;
		double coolingRate = 0.9999;
		double absoluteTemperature = 0.00001;

		init();

		double distance = getToatalDistance(currPath);

		int[] nextPath;
		Random random = new Random();
		while (temperature > absoluteTemperature) {
			nextPath = generateNextPath();
			deltaDistance = getToatalDistance(nextPath) - distance;

			if ((deltaDistance < 0) || (distance > 0 && Math.exp(-deltaDistance / temperature) > random.nextDouble())) {
				currPath = Arrays.copyOf(nextPath, nextPath.length);
				distance = deltaDistance + distance;
			}

			temperature *= coolingRate;
			iterator++;
			System.out.println("iterator: " + iterator + " path: " + Arrays.toString(currPath));
		}
		shortesDistance = distance;
		System.arraycopy(currPath, 0, bestPath, 0, currPath.length);

	}

	/**
	 * calculate total distance
	 * 
	 * @param currPath
	 * @return
	 */
	private double getToatalDistance(int[] currPath)
	{
		int length = currPath.length;
		double totalDistance = 0.0D;
		for (int i = 0; i < length - 1; i++) {
			totalDistance += city[currPath[i]][currPath[i + 1]];
		}
		totalDistance += city[currPath[length - 1]][0];

		return totalDistance;
	}

	/**
	 * swap two elements in the old array to genreate new array
	 * 
	 * @return
	 */
	private int[] generateNextPath()
	{
		int[] nextPath = Arrays.copyOf(currPath, currPath.length);
		Random random = new Random();
		int length = nextPath.length;
		int fistIndex = random.nextInt(length - 1) + 1;
		int secIndex = random.nextInt(length - 1) + 1;
		while (fistIndex == secIndex) {
			secIndex = random.nextInt(length - 1) + 1;
		}
		int tmp = nextPath[fistIndex];
		nextPath[fistIndex] = currPath[secIndex];
		nextPath[secIndex] = tmp;

		return nextPath;
	}

	public static void main(String[] args)
	{
		try {
			new SimulatedAnnealing().anneal();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}