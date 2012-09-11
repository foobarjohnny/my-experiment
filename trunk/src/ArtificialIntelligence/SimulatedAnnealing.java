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
 * http://nopainnogain.iteye.com/blog/852002 退火法
 * 模拟退火算法来源于固体退火原理，将固体加温至充分高，再让其徐徐冷却
 * ，加温时，固体内部粒子随温升变为无序状，内能增大，而徐徐冷却时粒子渐趋有序，在每个温度都达到平衡态
 * ，最后在常温时达到基态，内能减为最小。根据Metropolis准则
 * ，粒子在温度T时趋于平衡的概率为e-ΔE/(kT)，其中E为温度T时的内能，ΔE为其改变量
 * ，k为Boltzmann常数。用固体退火模拟组合优化问题，将内能E模拟为目标函数值f
 * ，温度T演化成控制参数t，即得到解组合优化问题的模拟退火算法：由初始解i和控制参数初值t开始
 * ，对当前解重复“产生新解→计算目标函数差→接受或舍弃”的迭代，并逐步衰减t值
 * ，算法终止时的当前解即为所得近似最优解，这是基于蒙特卡罗迭代求解法的一种启发式随机搜索过程。退火过程由冷却进度表(Cooling
 * Schedule)控制，包括控制参数的初值t及其衰减因子Δt、每个t值时的迭代次数L和停止条件S。
 */
/**
 * http://blog.csdn.net/yuanqingfei/article/details/36584
 * 对爬山法，SA和GA的形象描述 分类： ☆Academic Endless☆ 2004-07-08 00:28 723人阅读 评论(1) 收藏 举报
 * 来源：comp.ai.neural-nets
 * 
 * “注意，在目前讨论的所有爬山法中，袋鼠最有希望到达靠近它出发点的山顶。但不能保证该山顶是珠穆朗玛峰，或者是一个非常高的峰，各种使用的方法都试图找到实际全局最优值
 * 。
 * 
 * 在SA（模拟退火算法）中，袋鼠喝醉了，而且随机地跳跃了很长时间，但是，它渐渐清醒了并朝着峰顶跳去。
 * 
 * 在GA（遗传算法）中，有很多袋鼠，它们降落到喜马拉雅山脉地任意地方（如果飞行员没有迷失方向）。这些袋鼠并不知道它们被设想寻找珠穆朗玛峰，但每过几年，
 * 你就在一些高度较低地地方射杀一些袋鼠，并希望存活下来地那些是多产地，并在那里生儿育女”.
 */
/**
 * 人工智能算法- 优化算法 http://blog.csdn.net/soso_blog/article/details/5815635
 * */
// 伪码描述:
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
// 退火法求TSP
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