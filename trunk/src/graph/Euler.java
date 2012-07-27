package graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/*
 * http://blog.163.com/zhoumhan_0351/blog/static/39954227200982051154725/
 * http://www.cppblog.com/RyanWang/archive/2009/02/04/73021.html
 * http://www.cnblogs.com/buptLizer/archive/2012/04/15/2450297.html
 * http://hi.baidu.com/ybq0460/blog/item/9f5e2272a445a7ea0bd18725.html
 * http://ideone.com/3lfbC
 */

public class Euler extends Tarjan
{

	// struct stack
	// {
	// int top , node[81];
	//
	// } T,F,A; //顶点的堆栈

	// int n;
	public static LinkedList<Integer>	eulerPath;

	boolean brigde(int i, int j)
	{
		int t, cur;
		for (cur = 1; cur <= vertexCnt; cur++) {
			InStack[cur] = 0;
		}
		// 如果度数是1表明就只有I,J这条边 那必然是桥了
		if (degree[i] == 1) {
			return false;
		}
		else {
			// 为什么要加Integer的强制转换，为什么Integer能够去掉里面的数字j? 不同的类呀，想想为什么
			gra[i].remove((Integer) j);
			gra[j].remove((Integer) i);
			InStack[i] = 1;
			t = i;
			sta.push(i);
			// 这是一个DFS
			while (!sta.isEmpty()) {
				t = sta.peek();
				int idx = 0;
				for (idx = 0; idx < gra[t].size(); idx++) {
					cur = gra[t].get(idx);
					if (degree[cur] > 0) {
						if (InStack[cur] == 0)
							InStack[cur] = 1;
						sta.push(cur);
						t = cur;
						break;
					}
				}

				// 没有下面的边了
				if (idx > vertexCnt) {
					sta.pop();
				}
			}
			for (cur = 1; cur <= vertexCnt; cur++) {
				if (degree[cur] > 0) {
					// 表明有点没访问到
					if (InStack[cur] == 0) {
						gra[i].push(j);
						gra[j].push(i);
						break;
					}
				}
			}
			if (cur > vertexCnt) {
				// 不为桥
				return false;
			}
			// 为桥
			return true;
		}
	}

	void Fleury(int cur) // Fleury算法
	{
		int next = 0, b = 0;
		if (eulerPath.size() <= edgeCnt + 1) {
			eulerPath.push(cur);
			for (int idx = 0; idx < gra[cur].size(); idx++) {
				next = gra[cur].get(idx);
				if (brigde(cur, next) == false) {
					b = 1;
					break;
				}
			}
		}
		if (b == 1) {
			gra[cur].remove((Integer) next);
			gra[next].remove((Integer) cur);
			degree[cur]--;
			degree[next]--;
			Fleury(next);
		}
	}

	void main()
	{

		int m, s, t, num, i, j;

		s = 0;
		if (BFSTest(1)) // 判断有无奇度点
		{
			for (i = 1; i <= vertexCnt; i++) {
				num = gra[i].size();
				if (num % 2 == 1) {
					s++;
					break;
				}
			}
		}

		if (s == 0) {
			Fleury(1);
			System.out.println("\n\t该图的一条欧拉回路：");
			System.out.println(eulerPath);
		}
		else {
			System.out.println("\n\t该图不存在欧拉回路!\n");
		}

	}

	public static boolean BFSTest(int cur)
	{
		InStack[cur] = 1;
		sta.push(cur);
		while (!sta.isEmpty()) {
			cur = sta.pop();
			for (int i = 0; i < gra[cur].size(); ++i) {
				int next = gra[cur].get(i);
				InStack[next] = 1;
				sta.push(next);
			}
		}

		for (int i = 1; i <= vertexCnt; i++)
			if (InStack[i] == 0) {
				return false;
			}
		return false;
	}

	// void Euler(Graph G,int x)
	// {
	// int m;
	// SqStack S;
	// InitStack(S);
	// DFS(G,S,x,0);
	// printf("该图的一个欧拉回路为：");
	// while(!StackEmpty(S))
	// {
	// GetTop(S,m);
	// printf("->v%d",m);
	// Pop(S);
	// }//while
	// }
	//
	// void InputM1(Graph &G)
	// {
	//
	// int h,z;
	// printf("Please input 顶点数和边数/n");
	// scanf("%d",&v);
	// scanf("%d",&e);
	// for(int i=0;i<v;i++)
	// for(int j=0;j<v;j++)
	// G[i][j]=0;
	//
	// printf("please int the 邻接矩阵的值(起点(数字) 终点(数字))：/n");
	// for(int i=0;i<e;i++)
	// {
	// scanf("%d",&h);
	// scanf("%d",&z);
	// G[h-1][z-1]=1;
	// G[z-1][h-1]=1;
	// }//for
	// }//InputM1
	//
	// int main()
	// {
	// int i,j,sum,k=0;
	// Graph G;
	// InputM1(G);
	// if(BFSTest(G)==0)
	// {
	// printf("该图不是连通图!/n");
	// exit(0);
	// }//if
	// for(i=0;i<v;i++)
	// {
	// sum=0;
	// for(j=0;j<v;j++)
	// sum+=G[i][j];
	// if(sum%2==1)
	// { k=1;
	// break;
	// }//if
	// }//for
	// if(k==1) printf("该图不存在欧拉回路！/n");
	// else
	// Euler(G,0); //从那个点出发
	// return 1;
	// }
}

// 顶点类
