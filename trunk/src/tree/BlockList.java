package tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 Title            :        NOI 2003 editor
 Date             :        2007 - 6 - 12 
 Speed            :        O(K * Math.sqrt(L))
 Description      :        List with Block operation
 */
//��״����ľ���֮����
//��״������ʹ�÷ֿ�(Block)˼�룬������Ϊ L �����α��ֳ� Math.sqrt(L) �鳤Ϊ Math.sqrt(L) ��С�飬��ʹ��˫����������������������C++�ǿ���ʹ��list��ʵ�֣�����Чһ�㣩��
//����������sqrt(L)��ʱ����������ά���������Լ��趨��ά������ Balance������ Blacksize �����½�([Math.sqrt(L)/2, Math.sqrt(L)*2])����Ӧ�Ľ��п�ķ���(Break)�ͺϲ�������
//��Ҫע�����������Ҫ��¼��ǰ��ָ�룬�����������Ѻͺϲ�������ʱ����Ҫע�⵱ǰָ���ά����
/*
 * http://hi.baidu.com/wuyijia/blog/item/7085fa004423cd86e850cdeb.html
 * http://www.byvoid.com/blog/tag/%E5%9D%97%E7%8A%B6%E9%93%BE%E8%A1%A8/
 * http://www.nocow.cn/index.php/%E5%9D%97%E7%8A%B6%E9%93%BE%E8%A1%A8
 * http://dongxicheng.org/structure/blocklink/
 * ��״���������

 ��ʱ��������Ҫ�������һ�����ݽṹ�����ܿ�����Ҫ��λ�ò������ɾ��һ�����ݡ�
 �ȿ������ּ򵥵����ݽṹ�����������
 �����ܹ���O(1)��ʱ�����ҵ���Ҫִ�в�����λ�ã��������ǲ����ɾ����Ҫ�ƶ�֮����������ݣ����Ӷ���O(n)�ġ�
 �����ܹ���O(1)��ʱ���ڲ����ɾ��һ�����ݣ�������Ѱ�Ҳ���λ��ʱ��ȴҪ���������������Ӷ�ͬ��ʱO(n)�ġ�
 ���������ݽṹ������ȱ�㣬���ǳ��Խ��������ݽṹ�ںϳ�һ��ȫ�µ����ݽṹ����״����
 Ϊ�����ֿ�״�������Խ�ԣ������پٸ����ӣ�
 �Ȱ����Ŀ����:
 ���ʵ��һ���ı��༭���Ĺ��ܣ�������:�ƶ���꣬ɾ������XX���ַ����ڹ������һ���ı����������XX���ַ���
 ���ȣ�����������һ�±���д����
 No.1:����:
 ������ʵ����Щ���������ǿ�����ʱ�临�Ӷȷ���:
 �ƶ����:O(1*times(<=50000))
 ɾ������XX���ַ�:O(���ַ���(<=2MB)*times(<=4000))
 �ڹ������һ���ı�:O(���ַ���*times(<=4000))
 �������XX���ַ�:O(P)
 //��ע:P<=3MB, �������XX���ַ��������㷨�и��Ӷ���ͬ,������������ȥ
 ��ѯ�ʴ����϶࣬������ɾ�ַ��϶��Ǳض��ᳬʱ
 No.2:����:
 �ƶ����:O(n*times(<=50000))
 ɾ������XX���ַ�:O(���ַ���)
 �ڹ������һ���ı�:O(���ַ���)
 ���ڣ��ֵ���״����ǳ���:
 �ƶ����:O(sqrt(n)*times(<=50000))
 ɾ������XX���ַ�:O(sqrt(���ַ���))
 �ڹ������һ���ı�:O(sqrt(���ַ���))
 {��̯ƽ����SplayTree�����������в�����̯O(Log(���ַ���))}
 [�༭] ��״����ľ������

 ��״�����������ģ���������������ά��һ�����������е�ÿ����Ԫ�а���һ�����飬�Լ���������е����ݸ�����
 ÿ�������е����������������������ݡ�
 ��������Ϊa��ÿ����Ԫ�е����鳤����b��
 �����ǲ����ɾ������ѰַʱҪ���������������Ӷ���O(a)��
 ���ڲ��������ֱ���������м���һ���µ�Ԫ�����Ӷ���O(1)��
 ����ɾ�����������漰��������ĵ�Ԫ�����һ����Ԫ�е��������ݾ�Ҫɾ����ֱ��ɾ�������Ԫ�����Ӷ���O(1)�����ֻɾ���������ݣ���Ҫ�ƶ������е����ݣ����Ӷ���O(b)��
 �ܵĸ��Ӷ���O(a+b)��
 ��Ϊab=n��ȡa=b=��n�����ܵĸ��Ӷ���O(��n)��

 ���������ά��a��b���µ��ڡ�n��
 ����ά�ֿ�״������ÿ��Ĵ�С������[sqrt(n)/2,sqrt(n)*2]�У����򣬿�״������˻���ά������ͨ����ĺϲ��������ɣ��ϲ����Ӷȣ�ͬ����O(sqrt(n))

 ������ά������������ʹ�ܸ��Ӷ����ӡ�
 ���յõ�һ�����Ӷ���O(��n)�����ݽṹ��
 */

class Block
{
	int		maxn	= 5000;
	int		cnt;
	char[]	vec		= new char[maxn];
	Block	next, prev;

	Block(Block suf, Block frt)
	{
		cnt = 0;
		next = suf;
		prev = frt;
	}
}

// ֮ǰ������ Text
public class BlockList
{
	static Block	pre		= null, suf = null;
	static int		tot		= 1;
	static Block	currb	= new Block(null, null);
	static Block	ROOT	= currb;
	static int		currp	= 0;

	BlockList()
	{
		currp = 0;
		tot = 1;
		currb = ROOT = new Block(null, null);
		currb.cnt = 1;
	}

	public static void move(int k)
	{
		Move(k);
	}

	public static void insert(int size)
	{
		Insert(size);
		Balance();
	}

	public static void remove(int size)
	{
		Remove(size);
		Balance();
	}

	// print �൱��GET
	public static void print(int size)
	{
		Print(size);
	}

	public static void prev()
	{
		Prev();
	};

	public static void succ()
	{
		Succ();
	};

	public static void Move(int k)
	{
		currb = ROOT;
		while (k >= currb.cnt) {
			k -= currb.cnt;
			currb = currb.next;
		}
		currp = k;
	}

	public static char Get_char()
	{
		char c;
		while ((c = getchar()) == '\n')
			;
		return c;
	}

	private static char getchar()
	{
		return 'c';
	}

	public static void Break(Block curBlk, int curIdx)
	{
		pre = curBlk;
		if (curIdx >= curBlk.cnt - 1) {
			suf = curBlk.next;
		}
		else {
			Block tmp = new Block(curBlk.next, curBlk);
			if (currp > curIdx) {
				currb = tmp;
				currp -= curIdx + 1;
			}
			if (curBlk.next != null) {
				curBlk.next.prev = tmp;
			}

			suf = tmp;
			tmp.cnt = curBlk.cnt - curIdx - 1;
			System.arraycopy(curBlk.vec, curIdx + 1, tmp, 0, tmp.cnt);
			curBlk.cnt = curIdx + 1;
			curBlk.next = suf;
		}
	}

	public static int Balance()
	{
		Block curr = ROOT, tmp;
		pre = suf = null;
		int bt = (int) Math.floor(Math.sqrt(tot));
		while (curr != null) {
			while (curr.cnt < bt / 2 || curr.cnt > 2 * bt)
				if (curr.cnt > 2 * bt) {
					Break(curr, curr.cnt / 2);
					curr = pre;
				}
				else {
					if (curr.next == null)
						break;
					// memcpy(&(curr.vec[curr.amt]), &(curr.next.vec[0]),
					// curr.next.amt sizeof(curr.vec[0]));
					System.arraycopy(curr.next.vec, 0, curr.vec, curr.cnt, curr.next.cnt);
					tmp = curr.next;
					if (curr.next.next != null) {
						curr.next.next.prev = curr;
					}
					if (currb == tmp) {
						currp += curr.cnt;
						currb = curr;
					}
					curr.cnt += curr.next.cnt;
					curr.next = curr.next.next;
				}
			curr = curr.next;
		}
		return 1;
	}

	public static int Insert(int size)
	{
		tot += size;
		int bt = (int) Math.ceil(Math.sqrt(tot));
		pre = suf = null;
		Break(currb, currp);
		Block node = new Block(suf, pre), tmp;
		if (suf != null) {
			suf.prev = node;
		}
		pre.next = node;
		for (; size > 0; size--) {
			if (node.cnt > bt) {
				tmp = new Block(node.next, node);
				if (node.next != null) {
					node.next.prev = tmp;
				}
				node.next = tmp;
				node = tmp;
			}
			node.vec[node.cnt++] = Get_char();
		}
		return 1;
	}

	public static int Remove(int size)
	{
		tot -= size;
		pre = suf = null;
		Break(currb, currp);
		Block curr = suf, tmp = null;
		while (curr != null && size >= curr.cnt) {
			size -= curr.cnt;
			if (curr.next != null)
				curr.next.prev = curr.prev;
			if (curr.prev != null)
				curr.prev.next = curr.next;
			else
				ROOT = curr.next;
			tmp = curr;
			curr = curr.next;
		}
		if (curr == null)
			return 1;
		curr.cnt -= size;
		if (size > 0) {
			System.arraycopy(curr.vec, size, curr.vec, 0, curr.cnt);
		}
		return 0;
	}

	public static void Print(int size)
	{
		Block curr = currb;
		int cp = currp;
		for (; size > 0; size--) {
			if (cp >= curr.cnt - 1) {
				curr = curr.next;
				cp = -1;
			}
			System.out.print(curr.vec[++cp]);
		}
		System.out.print("\n");
	}

	public static void Prev()
	{
		currp--;
		if (currp < 0) {
			currb = currb.prev;
			currp = currb.cnt - 1;
		}
	}

	public static void Succ()
	{
		currp++;
		if (currp >= currb.cnt) {
			currb = currb.next;
			currp = 0;
		}
	}

	int	n, k;

	public static void main(String[] args)
	{
		try {
			// input 4I6G4
			BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
			int n;
			char s;
			n = sc.read() - '0';
			int k;
			for (int i = 0; i < n; i++) {
				s = (char) sc.read();
				switch (s) {
					case 'M':
						k = sc.read() - '0';
						move(k);
						break;
					case 'I':
						k = sc.read() - '0';
						insert(k);
						break;
					case 'D':
						k = sc.read() - '0';
						remove(k);
						break;
					case 'G':
						k = sc.read() - '0';
						print(k);
						break;
					case 'P':
						prev();
						break;
					case 'N':
						succ();
						break;
				}
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
