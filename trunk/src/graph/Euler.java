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
	// } T,F,A; //����Ķ�ջ

	// int n;
	public static LinkedList<Integer>	eulerPath;

	boolean brigde(int i, int j)
	{
		int t, cur;
		for (cur = 1; cur <= vertexCnt; cur++) {
			InStack[cur] = 0;
		}
		// ���������1������ֻ��I,J������ �Ǳ�Ȼ������
		if (degree[i] == 1) {
			return false;
		}
		else {
			// ΪʲôҪ��Integer��ǿ��ת����ΪʲôInteger�ܹ�ȥ�����������j? ��ͬ����ѽ������Ϊʲô
			gra[i].remove((Integer) j);
			gra[j].remove((Integer) i);
			InStack[i] = 1;
			t = i;
			sta.push(i);
			// ����һ��DFS
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

				// û������ı���
				if (idx > vertexCnt) {
					sta.pop();
				}
			}
			for (cur = 1; cur <= vertexCnt; cur++) {
				if (degree[cur] > 0) {
					// �����е�û���ʵ�
					if (InStack[cur] == 0) {
						gra[i].push(j);
						gra[j].push(i);
						break;
					}
				}
			}
			if (cur > vertexCnt) {
				// ��Ϊ��
				return false;
			}
			// Ϊ��
			return true;
		}
	}

	void Fleury(int cur) // Fleury�㷨
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
		if (BFSTest(1)) // �ж�������ȵ�
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
			System.out.println("\n\t��ͼ��һ��ŷ����·��");
			System.out.println(eulerPath);
		}
		else {
			System.out.println("\n\t��ͼ������ŷ����·!\n");
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
	// printf("��ͼ��һ��ŷ����·Ϊ��");
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
	// printf("Please input �������ͱ���/n");
	// scanf("%d",&v);
	// scanf("%d",&e);
	// for(int i=0;i<v;i++)
	// for(int j=0;j<v;j++)
	// G[i][j]=0;
	//
	// printf("please int the �ڽӾ����ֵ(���(����) �յ�(����))��/n");
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
	// printf("��ͼ������ͨͼ!/n");
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
	// if(k==1) printf("��ͼ������ŷ����·��/n");
	// else
	// Euler(G,0); //���Ǹ������
	// return 1;
	// }
}

// ������
