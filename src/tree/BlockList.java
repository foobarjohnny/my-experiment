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
//块状链表的经典之作！
//块状链表，即使用分块(Block)思想，将长度为 L 的线形表，分成 Math.sqrt(L) 块长为 Math.sqrt(L) 的小块，并使用双向链表技术连接起来（对于C++是可以使用list来实现，更高效一点）。
//这样就能在sqrt(L)的时间内完成许多维护操作。自己设定了维护操作 Balance，设置 Blacksize 的上下界([Math.sqrt(L)/2, Math.sqrt(L)*2])，对应的进行块的分裂(Break)和合并操作。
//需要注意的是由于需要记录当前的指针，所以在做分裂和合并操作的时候需要注意当前指针的维护。
/*
 * http://hi.baidu.com/wuyijia/blog/item/7085fa004423cd86e850cdeb.html
 * http://www.byvoid.com/blog/tag/%E5%9D%97%E7%8A%B6%E9%93%BE%E8%A1%A8/
 * http://www.nocow.cn/index.php/%E5%9D%97%E7%8A%B6%E9%93%BE%E8%A1%A8
 * http://dongxicheng.org/structure/blocklink/
 * 块状链表的意义

 有时候我们需要设计这样一种数据结构：它能快速在要求位置插入或者删除一段数据。
 先考虑两种简单的数据结构：数组和链表
 数组能够在O(1)的时间内找到所要执行操作的位置，但无论是插入或删除都要移动之后的所有数据，复杂度是O(n)的。
 链表能够在O(1)的时间内插入和删除一段数据，但是在寻找操作位置时，却要遍历整个链表，复杂度同样时O(n)的。
 这两种数据结构各有优缺点，我们尝试将两种数据结构融合成一个全新的数据结构：块状链表。
 为了体现块状链表的优越性，我们再举个例子：
 先搬道题目出来:
 编程实现一个文本编辑器的功能，操作有:移动光标，删除光标后XX个字符，在光标后添加一段文本，输出光标后XX个字符。
 首先，我们来讨论一下暴力写法。
 No.1:数组:
 用数组实现这些操作，我们看下总时间复杂度分析:
 移动光标:O(1*times(<=50000))
 删除光标后XX个字符:O(总字符数(<=2MB)*times(<=4000))
 在光标后添加一段文本:O(总字符数*times(<=4000))
 输出光标后XX个字符:O(P)
 //备注:P<=3MB, 输出光标后XX个字符在所有算法中复杂度相同,接下来将会略去
 在询问次数较多，并且添删字符较多是必定会超时
 No.2:链表:
 移动光标:O(n*times(<=50000))
 删除光标后XX个字符:O(总字符数)
 在光标后添加一段文本:O(总字符数)
 现在，轮到块状链表登场了:
 移动光标:O(sqrt(n)*times(<=50000))
 删除光标后XX个字符:O(sqrt(总字符数))
 在光标后添加一段文本:O(sqrt(总字符数))
 {均摊平衡树SplayTree可以做到所有操作均摊O(Log(总字符数))}
 [编辑] 块状链表的具体介绍

 块状链表是这样的：从总体上来看，维护一个链表，链表中的每个单元中包含一段数组，以及这个数组中的数据个数。
 每个链表中的数据连起来就是整个数据。
 设链表长度为a，每个单元中的数组长度是b。
 无论是插入或删除，在寻址时要遍历整个链表，复杂度是O(a)；
 对于插入操作，直接在链表中加入一个新单元，复杂度是O(1)；
 对于删除操作，会涉及多个连续的单元，如果一个单元中的所有数据均要删除，直接删除这个单元，复杂度是O(1)，如果只删除部分数据，则要移动数组中的数据，复杂度是O(b)。
 总的复杂度是O(a+b)。
 因为ab=n，取a=b=√n，则总的复杂度是O(√n)。

 问题是如何维护a和b大致等于√n？
 必须维持块状链表中每块的大小在区间[sqrt(n)/2,sqrt(n)*2]中，否则，块状链表会退化。维护可以通过块的合并与分裂完成，合并复杂度，同样是O(sqrt(n))

 这样，维护操作并不会使总复杂度增加。
 最终得到一个复杂度是O(√n)的数据结构。
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

// 之前类名是 Text
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

	// print 相当于GET
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
