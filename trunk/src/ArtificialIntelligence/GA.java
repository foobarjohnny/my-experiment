package ArtificialIntelligence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * ���Ŵ��㷨���Թ�
 * http://www.cnblogs.com/zhangchaoyang/articles/2214263.html
 * @author Orisun
 * 
 */
public class GA
{

	int				gene_len;		// ���򳤶�
	int				chrom_len;		// Ⱦɫ�峤��
	int				population;	// ��Ⱥ��С
	double			cross_ratio;	// ������
	double			muta_ratio;	// ������
	int				iter_limit;	// �������Ĵ���
	List<boolean[]>	individuals;	// �洢������Ⱥ��Ⱦɫ��

	//�ؼ�
	Labyrinth		labyrinth;
	int				width;			// �Թ�һ���ж��ٸ�����
	int				height;		// �Թ��ж�����

	public class BI
	{
		double		fitness;
		boolean[]	indv;

		public BI(double f, boolean[] ind)
		{
			fitness = f;
			indv = ind;
		}

		public double getFitness()
		{
			return fitness;
		}

		public boolean[] getIndv()
		{
			return indv;
		}
	}

	List<BI>	best_individual;	// �洢ÿһ����������ĸ���

	public GA(Labyrinth labyrinth)
	{
		this.labyrinth = labyrinth;
		this.width = labyrinth.map[0].length;
		this.height = labyrinth.map.length;
		chrom_len = 4 * (width + height);
		gene_len = 2;
		population = 20;
		cross_ratio = 0.83;
		muta_ratio = 0.002;
		iter_limit = 300;
		individuals = new ArrayList<boolean[]>(population);
		best_individual = new ArrayList<BI>(iter_limit);
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public double getCross_ratio()
	{
		return cross_ratio;
	}

	public List<BI> getBest_individual()
	{
		return best_individual;
	}

	public Labyrinth getLabyrinth()
	{
		return labyrinth;
	}

	public void setLabyrinth(Labyrinth labyrinth)
	{
		this.labyrinth = labyrinth;
	}

	public void setChrom_len(int chrom_len)
	{
		this.chrom_len = chrom_len;
	}

	public void setPopulation(int population)
	{
		this.population = population;
	}

	public void setCross_ratio(double cross_ratio)
	{
		this.cross_ratio = cross_ratio;
	}

	public void setMuta_ratio(double muta_ratio)
	{
		this.muta_ratio = muta_ratio;
	}

	public void setIter_limit(int iter_limit)
	{
		this.iter_limit = iter_limit;
	}

	// ��ʼ����Ⱥ
	public void initPopulation()
	{
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < population; i++) {
			int len = gene_len * chrom_len;
			boolean[] ind = new boolean[len];
			for (int j = 0; j < len; j++)
				ind[j] = r.nextBoolean();
			individuals.add(ind);
		}
	}

	// ����
	public void cross(boolean[] arr1, boolean[] arr2)
	{
		Random r = new Random(System.currentTimeMillis());
		int length = arr1.length;
		int slice = 0;
		do {
			slice = r.nextInt(length);
		} while (slice == 0);
		if (slice < length / 2) {
			for (int i = 0; i < slice; i++) {
				boolean tmp = arr1[i];
				arr1[i] = arr2[i];
				arr2[i] = tmp;
			}
		}
		else {
			for (int i = slice; i < length; i++) {
				boolean tmp = arr1[i];
				arr1[i] = arr2[i];
				arr2[i] = tmp;
			}
		}
	}

	// ����
	public void mutation(boolean[] individual)
	{
		int length = individual.length;
		Random r = new Random(System.currentTimeMillis());
		individual[r.nextInt(length)] ^= false;
	}

	// ���̷�ѡ����һ��,�����ص�����ߵ���Ӧ��ֵ
	public double selection()
	{
		boolean[][] next_generation = new boolean[population][]; // ��һ��
		int length = gene_len * chrom_len;
		for (int i = 0; i < population; i++)
			next_generation[i] = new boolean[length];
		double[] cumulation = new double[population];
		int best_index = 0;
		double max_fitness = getFitness(individuals.get(best_index));
		cumulation[0] = max_fitness;
		for (int i = 1; i < population; i++) {
			double fit = getFitness(individuals.get(i));
			cumulation[i] = cumulation[i - 1] + fit;
			// Ѱ�ҵ��������Ÿ���
			if (fit > max_fitness) {
				best_index = i;
				max_fitness = fit;
			}
		}
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < population; i++)
			next_generation[i] = individuals.get(findByHalf(cumulation, rand.nextDouble() * cumulation[population - 1]));
		// �ѵ��������Ÿ��弰����Ӧ�ȷŵ�best_individual��
		BI bi = new BI(max_fitness, individuals.get(best_index));
		// printPath(individuals.get(best_index));
		// System.out.println(max_fitness);
		best_individual.add(bi);
		// ��һ����Ϊ��ǰ��
		for (int i = 0; i < population; i++)
			individuals.set(i, next_generation[i]);
		return max_fitness;
	}

	// �۰����
	public int findByHalf(double[] arr, double find)
	{
		if (find < 0 || find == 0 || find > arr[arr.length - 1])
			return -1;
		int min = 0;
		int max = arr.length - 1;
		int medium = min;
		do {
			if (medium == (min + max) / 2)
				break;
			medium = (min + max) / 2;
			if (arr[medium] < find)
				min = medium;
			else if (arr[medium] > find)
				max = medium;
			else
				return medium;

		} while (min < max);
		return max;
	}

	// ������Ӧ��
	public double getFitness(boolean[] individual)
	{
		int length = individual.length;
		// ��¼��ǰ��λ��,��ڵ��ǣ�1,0��
		int x = 1;
		int y = 0;
		// ����Ⱦɫ���л����ָ����ǰ��
		for (int i = 0; i < length; i++) {
			boolean b1 = individual[i];
			boolean b2 = individual[++i];
			// 00������
			if (b1 == false && b2 == false) {
				if (x > 0 && labyrinth.map[y][x - 1] == true) {
					x--;
				}
			}
			// 01������
			else if (b1 == false && b2 == true) {
				if (x + 1 < width && labyrinth.map[y][x + 1] == true) {
					x++;
				}
			}
			// 10������
			else if (b1 == true && b2 == false) {
				if (y > 0 && labyrinth.map[y - 1][x] == true) {
					y--;
				}
			}
			// 11������
			else if (b1 == true && b2 == true) {
				if (y + 1 < height && labyrinth.map[y + 1][x] == true) {
					y++;
				}
			}
		}
		int n = Math.abs(x - labyrinth.x_end) + Math.abs(y - labyrinth.y_end) + 1;
		// if(n==1)
		// printPath(individual);
		return 1.0 / n;
	}

	// �����Ŵ��㷨
	public boolean run()
	{
		// ��ʼ����Ⱥ
		initPopulation();
		Random rand = new Random(System.currentTimeMillis());
		boolean success = false;
		while (iter_limit-- > 0) {
			// ������Ⱥ��˳��
			Collections.shuffle(individuals);
			for (int i = 0; i < population - 1; i += 2) {
				// ����
				if (rand.nextDouble() < cross_ratio) {
					cross(individuals.get(i), individuals.get(i + 1));
				}
				// ����
				if (rand.nextDouble() < muta_ratio) {
					mutation(individuals.get(i));
				}
			}
			// ��Ⱥ����
			if (selection() == 1) {
				success = true;
				break;
			}
		}
		return success;
	}

	// public static void main(String[] args) {
	// GA ga = new GA(8, 8);
	// if (!ga.run()) {
	// System.out.println("û���ҵ��߳��Թ���·��.");
	// } else {
	// int gen = ga.best_individual.size();
	// boolean[] individual = ga.best_individual.get(gen - 1).indv;
	// System.out.println(ga.getPath(individual));
	// }
	// }

	// ����Ⱦɫ���ӡ�߷�
	public String getPath(boolean[] individual)
	{
		int length = individual.length;
		int x = 1;
		int y = 0;
		LinkedList<String> stack = new LinkedList<String>();
		for (int i = 0; i < length; i++) {
			boolean b1 = individual[i];
			boolean b2 = individual[++i];
			if (b1 == false && b2 == false) {
				if (x > 0 && labyrinth.map[y][x - 1] == true) {
					x--;
					if (!stack.isEmpty() && stack.peek() == "��")
						stack.poll();
					else
						stack.push("��");
				}
			}
			else if (b1 == false && b2 == true) {
				if (x + 1 < width && labyrinth.map[y][x + 1] == true) {
					x++;
					if (!stack.isEmpty() && stack.peek() == "��")
						stack.poll();
					else
						stack.push("��");
				}
			}
			else if (b1 == true && b2 == false) {
				if (y > 0 && labyrinth.map[y - 1][x] == true) {
					y--;
					if (!stack.isEmpty() && stack.peek() == "��")
						stack.poll();
					else
						stack.push("��");
				}
			}
			else if (b1 == true && b2 == true) {
				if (y + 1 < height && labyrinth.map[y + 1][x] == true) {
					y++;
					if (!stack.isEmpty() && stack.peek() == "��")
						stack.poll();
					else
						stack.push("��");
				}
			}
		}
		StringBuilder sb = new StringBuilder(length / 4);
		Iterator<String> iter = stack.descendingIterator();
		while (iter.hasNext())
			sb.append(iter.next());
		return sb.toString();
	}
}