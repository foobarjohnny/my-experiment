package ArtificialIntelligence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * 遗传算法解决TSP问题 
 * http://zh.wikipedia.org/wiki/%E9%81%97%E4%BC%A0%E7%AE%97%E6%B3%95
 * http://1.onlysavior.sinaapp.com/?p=220
 */
public class GenericAlgorithm
{

	public static double[][]	distance;		// 距离
	public int					generationNum;	// 代数
	public int					m;				// 群体规模
	public static int			cityNum;		// 城市数目
	public double				pSelect	= 0.1;	// 掺杂比例
	public double				pCross	= 0.85; // 交叉比例
	public double				pm2		= 0.55; // 变异比例1
	public double				pm4		= 0.55; // 变异比例2

	public static GAResult randomGen()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cityNum; i++) {
			int r = (int) ((Math.random() * 1000) % cityNum);
			while (sb.indexOf("" + r) != -1) {
				r = (int) ((Math.random() * 1000) % cityNum);
			}
			sb.append("" + r);
		}

		return new GAResult(sb.toString());
	}

	public GAResult ga(boolean log)
	{
		if (this.distance == null) {
			throw new RuntimeException("distance can not be null");
		}

		Generation first = init(); // 初始化群体
		if (log) {
			System.out.println(first.toString());
		}

		for (int i = 0; i < generationNum; i++) {
			first = first.createGenerationAfterSelect(pSelect); // 选择群体
			first.findMin();
			if (log) {
				System.out.println("第" + i + "代中经过选择 ");
				System.out.println(first.toString());
				System.out.println();
			}
			first = first.oxCross(pCross, cityNum);
			first.findMin();
			if (log) {
				System.out.println("第" + i + "代中经过交叉 ");
				System.out.println(first.toString());
				System.out.println();
			}
			first = first.mutate(pm2, pm4, cityNum);
			first.findMin();
			if (log) {
				System.out.println("第" + i + "代中经过变异 ");
				System.out.println(first.toString());
				System.out.println();
			}
		}

		return first.getMinAtomicPath();

	}

	private Generation init()
	{
		Generation first = new Generation(m);

		GAResult gene;
		for (int i = 0; i < m; i++) {
			gene = randomGen();
			first.atomicPaths.add(gene);
		}
		return first;
	}

	public static void main(String[] args) throws IOException
	{
		GenericAlgorithm main = new GenericAlgorithm();
		GenericAlgorithm.distance = InitData.getCityDistance(InitData.getCityData("src/pr10"));
		GAResult.distance = GenericAlgorithm.distance;
		GenericAlgorithm.cityNum = 10;
		main.m = 50;
		main.generationNum = 50;

		System.out.println(main.ga(true));
	}
}

class InitData
{
	public static HashMap<Integer, City> getCityData(String filename) throws IOException
	{
		// 保存坐标信息
		HashMap<Integer, City> map = new HashMap<Integer, City>();
		// 读取文件
		BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
		for (String str = reader.readLine(); str != null; str = reader.readLine()) {
			// 读取的信息存入HaspMap中
			if (str.matches("([0-9]+)(\\s*)([0-9]+)(.?)([0-9]*)(\\s*)([0-9]+)(.?)([0-9]*)")) {
				String[] data = str.split("(\\s+)");
				City city = new City(Integer.parseInt(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]));
				map.put(city.getNumber(), city);
			}
		}

		return map;
	}

	public static double[][] getCityDistance(HashMap<Integer, City> map)
	{
		// 分配距离矩阵存储空间
		double[][] distance = new double[map.size() + 1][map.size() + 1];
		for (int i = 1; i < map.size() + 1; i++) {
			for (int j = 1; j < map.size() + 1; j++) {
				distance[i][j] = map.get(i).getDistance(map.get(j));
			}
		}

		return distance;
	}
}

class City
{
	private int		number;
	private double	x;
	private double	y;

	public City(int number, double x, double y)
	{
		this.setNumber(number);
		this.x = x;
		this.y = y;
	}

	public double getDistance(City city)
	{
		return sqrt(pow((x - city.x), 2) + pow((y - city.y), 2));
	}

	/********************** getter and setter **************************/
	public double getX()
	{
		return x;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public int getNumber()
	{
		return number;
	}
}

/**
 * 一代 User: onlysavior Date: 12-6-6 Time: 下午9:01
 */
class Generation
{
	public ArrayList<GAResult>	atomicPaths;					// 此代的所有个体
	private GAResult			minAtomicPath;					// 此代中最小代价的路径
	private double				minCost		= Double.MIN_VALUE; // 此代中的最小代价
	private double				averCost	= 0.0;				// 此代中的平均代价
	public int					m;								// 群体规模

	/**
	 * Constructor
	 * 
	 * @param m
	 *            种群规模
	 */
	public Generation(int m)
	{
		if (m <= 0) {
			throw new RuntimeException("种群规模不合法");
		}
		this.m = m;
		this.atomicPaths = new ArrayList<GAResult>(m);
	}

	/**
	 * 采用有掺杂个数的轮盘赌方式选择下一代 具体见WORD文档
	 * 
	 * @param pn
	 *            掺杂比例
	 * @return 选择后的下一代
	 */
	public Generation createGenerationAfterSelect(double pn)
	{
		Generation newGeneration = new Generation(m);

		// 减去掺杂比例后的选择算子
		double p = 1.0 - pn;
		// 选择概率
		double[] pSelects = new double[m];

		// 计算累积概率，
		double totalEval = 0.0;
		for (GAResult gaResult : atomicPaths) {
			totalEval += gaResult.cost;
		}

		// 计算选择概率
		pSelects[0] = p * (atomicPaths.get(0).cost / totalEval);
		for (int i = 1; i < m; i++) {
			pSelects[i] = pSelects[i - 1] + p * (atomicPaths.get(i).cost / totalEval);
		}

		// 轮盘赌法选择
		GAResult temp;
		for (int i = 0; i < m; i++) {
			GAResult gaResult = null;
			double r = Math.random();
			if (r <= pSelects[0] && r >= 0) {
				temp = this.atomicPaths.get(0);
				gaResult = new GAResult(temp.path);
			}
			else {
				for (int j = 0; j < m - 1; j++) {
					if (pSelects[j] < r && r <= pSelects[j + 1]) {
						temp = this.atomicPaths.get(j + 1);
						gaResult = new GAResult(temp.path);
					}
					else if (pSelects[m - 1] < r) {
						gaResult = GenericAlgorithm.randomGen(); // 新生成个体 TODO
					}
				}
			}
			if (i < atomicPaths.size()) {
				newGeneration.atomicPaths.add(i, gaResult);
			}
			else {
				newGeneration.atomicPaths.add(gaResult);
			}
		}

		return newGeneration;
	}

	/**
	 * 启发式交叉方式交叉产生新的子代 具体看论文
	 * 
	 * @param p
	 *            初始交叉概率
	 * @param cityNum
	 *            城市的数目
	 * @return 新的子代
	 */
	public Generation oxCross(double p, int cityNum)
	{
		// 交叉概率
		double pn = 0.0;

		Generation newGeneration = new Generation(m);
		Arrays.sort(this.atomicPaths.toArray());

		int r1, r2, r3; // 三个随机数
		GAResult f1, f2, f3; // 三个随机的父个体

		int counter = 0;
		for (int i = 0; i < m; i++) {
			// 随机选择3个父个体
			r1 = (int) (Math.random() * m);
			r2 = (int) (Math.random() * m);
			r3 = (int) (Math.random() * m);

			while (r1 == r2 || r1 == r3 || r2 == r3) {
				r1 = (int) (Math.random() * m);
				r2 = (int) (Math.random() * m);
				r3 = (int) (Math.random() * m);
			}

			f1 = this.atomicPaths.get(r1);
			f2 = this.atomicPaths.get(r2);
			f3 = this.atomicPaths.get(r3);

			// 三个父个体的cost平均值
			double fcostAver = (f1.cost + f2.cost + f3.cost) / 3.0;

			if (fcostAver <= getAverCost()) {
				pn = p;
			}
			else {
				pn = p * ((fcostAver - getMinCost()) / (getAverCost() - getMinCost()));
			}

			// 交叉开始，如果概率小于pn，则交叉
			double r = Math.random();
			GAResult son = null;
			if (r <= pn) {
				son = ox(f1, f2, f3, cityNum);
				// newGeneration.atomicPaths.set(i, son);
			}
			else {
				son = atomicPaths.get(counter);
				while (newGeneration.atomicPaths.contains(son) && counter < m) {
					++counter;
					son = atomicPaths.get(counter);
				}
				// newGeneration.atomicPaths.set(i, new GAResult(son.path));
			}

			if (i < newGeneration.atomicPaths.size()) {
				newGeneration.atomicPaths.add(i, son);
			}
			else {
				newGeneration.atomicPaths.add(son);
			}
		}

		return newGeneration;
	}

	/**
	 * 变异
	 * 
	 * @param pm2
	 *            变异概率参数1
	 * @param pm4
	 *            变异概率参数2
	 * @param cityNum
	 *            城市数目
	 * @return 下一代
	 */
	public Generation mutate(double pm2, double pm4, int cityNum)
	{
		int r; // 随机数
		GAResult ga;
		double p; // 变异概率
		for (int i = 0; i < m; i++) {
			r = (int) (Math.random() * 1000 % m);
			ga = atomicPaths.get(r);

			double fr = ga.cost;
			if (fr >= getAverCost()) {
				p = pm2 * ((fr - getMinCost()) / (getAverCost() - getMinCost()));
			}
			else {
				p = pm4;
			}

			// 变异开始
			double random = Math.random();
			if (random < p) {
				this.atomicPaths.set(i, ga.mutate(cityNum));
			}
		}

		return this;
	}

	public GAResult getMinAtomicPath()
	{
		if (this.minAtomicPath != null) {
			return this.minAtomicPath;
		}

		findMin();
		return this.minAtomicPath;
	}

	public double getMinCost()
	{
		if (this.minCost - Double.MIN_VALUE > 0) {
			return this.minCost;
		}

		findMin();
		this.minCost = this.minAtomicPath.cost;

		return this.minCost;
	}

	public double getAverCost()
	{
		if (this.averCost - 0.0 > 0) {
			return this.averCost;
		}

		caluteAverCost();
		return this.averCost;
	}

	public String toString()
	{
		return /* "paths are " + this.atomicPaths.toString() + */" minPath is" + this.minAtomicPath + "and the path costs " + getMinCost() + "/n the aver cost for this generation is" + getAverCost()
				+ "/n";
	}

	/**
	 * ox交叉，具体做法看WORD
	 * 
	 * @param f1
	 * @param f2
	 * @param f3
	 *            三个父代
	 * @param n
	 *            城市总数
	 * @return
	 */
	private GAResult ox(GAResult f1, GAResult f2, GAResult f3, int n)
	{
		StringBuilder sonPath = null;
		StringBuilder f1Path = new StringBuilder(f1.path);
		StringBuilder f2Path = new StringBuilder(f2.path);
		StringBuilder f3Path = new StringBuilder(f3.path);

		int r = (int) (Math.random() * 1000) % n;
		String rStr = "" + r;

		sonPath = rotate(f1Path, f2Path, f3Path, rStr, n);

		return new GAResult(sonPath.toString());
	}

	private StringBuilder rotate(StringBuilder sb1, StringBuilder sb2, StringBuilder sb3, String startStr, int cityNum)
	{
		StringBuilder sonPath = new StringBuilder();
		// sonPath.append(startStr);

		String findStr = startStr;
		while (sonPath.length() < cityNum) {
			sonPath.append(findStr);
			if (sonPath.length() >= cityNum) {
				break;
			}

			int sb1index = sb1.indexOf(findStr);
			int sb2index = sb2.indexOf(findStr);
			int sb3index = sb3.indexOf(findStr);
			sb1 = new StringBuilder(sb1.substring(sb1index) + sb1.substring(0, sb1index));
			sb2 = new StringBuilder(sb2.substring(sb2index) + sb2.substring(0, sb2index));
			sb3 = new StringBuilder(sb3.substring(sb3index) + sb3.substring(0, sb3index)); // TODO

			int start = Integer.parseInt(startStr);
			int endsb1 = Integer.parseInt("" + sb1.charAt(1));
			int endsb2 = Integer.parseInt("" + sb2.charAt(1));
			int endsb3 = Integer.parseInt("" + sb3.charAt(1));

			double dis1 = GenericAlgorithm.distance[start][endsb1];
			double dis2 = GenericAlgorithm.distance[start][endsb2];
			double dis3 = GenericAlgorithm.distance[start][endsb3];

			double min = Math.min(dis1, Math.min(dis2, dis3));
			if (min == dis1) {
				findStr = "" + sb1.charAt(1);
			}
			else if (min == dis2) {
				findStr = "" + sb2.charAt(1);
			}
			else {
				findStr = "" + sb3.charAt(1);
			}

			sb1 = new StringBuilder(sb1.substring(1));
			sb2 = new StringBuilder(sb2.substring(1));
			sb3 = new StringBuilder(sb3.substring(1));
		}

		return sonPath;
	}

	/**
	 * 找到此代中最小
	 */
	public void findMin()
	{
		GAResult min = this.atomicPaths.get(0);
		if (min == null) {
			throw new RuntimeException("代数据为空");
		}

		GAResult temp;
		for (int i = 1; i < atomicPaths.size(); i++) {
			temp = atomicPaths.get(i);
			if (min.cost > temp.cost) {
				min = temp;
			}
		}

		this.minAtomicPath = min;
	}

	/**
	 * 平均值
	 */
	private void caluteAverCost()
	{
		double totalCost = 0.0;
		for (GAResult ga : this.atomicPaths) {
			totalCost += ga.cost;
		}

		this.averCost = totalCost / this.atomicPaths.size();
	}
}

/**
 * 代表一次遗传运算的最终结果 User: onlysavior Date: 12-6-6 Time: 下午8:53
 */
class GAResult implements Comparable<GAResult>
{
	public static double[][]	distance;
	public String				path;		// 路径
	public double				cost;		// 开销
	public long					td;		// cost的倒数,用于选择算子的计算

	public GAResult(String path)
	{
		this.path = path;
		evalute();
	}

	/**
	 * 适应度函数
	 */
	public void evalute()
	{
		long totalDistance = 0L;

		// example 123456
		// totalDistance = d(1,6)+d(1,2)+d(2,3)+...d(5,6)
		totalDistance += distance[Integer.parseInt("" + (path.charAt(path.length() - 1)))][Integer.parseInt("" + (path.charAt(0)))];
		int start;
		int end;
		for (int i = 0; i < path.length() - 1; i++) {
			start = Integer.parseInt("" + path.charAt(i));
			end = Integer.parseInt("" + path.charAt(i + 1));

			totalDistance += distance[start][end];
		}

		// td = totalDistance
		this.td = totalDistance;

		// cost = 1 / totalDistance
		this.cost = totalDistance; // too small
	}

	/**
	 * 变异操作
	 * 
	 * @param cityNum
	 * @return
	 */
	public GAResult mutate(int cityNum)
	{
		int r1 = (int) ((Math.random() * 1000) % cityNum);
		int r2 = (int) ((Math.random() * 1000) % cityNum);

		while (r1 == r2) {
			r2 = (int) ((Math.random() * 1000) % cityNum);
		}
		char[] newPath = this.path.toCharArray();
		char temp = newPath[r1];
		newPath[r1] = newPath[r2];
		newPath[r2] = temp;

		return new GAResult(new String(newPath));
	}

	public String toString()
	{
		return "path is " + this.path + "  cost is " + this.cost + "/n";
	}

	public int compareTo(GAResult o)
	{
		if (this.cost < o.cost)
			return 1;
		if (this.cost > o.cost)
			return -1;
		return 0;
	}
}
// 1 87 7
// 2 91 38
// 3 83 46
// 4 71 44
// 5 64 60
// 6 68 69
// 7 83 69
// 8 87 76
// 9 74 78
// 10 71 71
// EOF