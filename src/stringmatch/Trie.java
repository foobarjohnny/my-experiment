package stringmatch;

public class Trie
{
	public final static int	MAXK	= 26;
	private TrieNode		root;
	private TrieNode[]		que		= new TrieNode[1000];

	private TrieNode search(String str)
	{
		if (root == null) {
			return null;
		}
		TrieNode work = root;
		int loc = -1;
		for (int i = 0; i < str.length(); i++) {
			loc = str.charAt(i) - 'a';
			if (work.child[loc] == null) {
				return null;
			}
			work = work.child[loc];
		}
		return work;
	}

	public boolean find(String str)
	{
		TrieNode work = search(str);
		if (work == null) {
			return false;
		}
		return work.isWord();
	}

	public void insert(String str)
	{
		if (root == null) {
			root = new TrieNode();
		}
		TrieNode work = root;
		int loc = -1;
		for (int i = 0; i < str.length(); i++) {
			loc = str.charAt(i) - 'a';
			// System.out.println("loc" + loc);
			if (work.child[loc] == null) {
				work.child[loc] = new TrieNode();
			}
			work = work.child[loc];
		}
		if (work != root) {
			work.setWord(true);
			work.count++;
		}

	}

	public boolean del(String str)
	{
		TrieNode work = search(str);
		if (work == null) {
			return false;
		}
		if (work.count-- > 0) {
			work.setWord(false);
		}
		return true;
	}

	private void printNode(TrieNode node, StringBuffer buf)
	{
		if (node == null) {
			return;
		}
		for (int i = 0; i < MAXK; i++) {
			if (node.child[i] != null) {
				buf.append((char) (i + 'a'));
				if (node.child[i].isWord()) {
					System.out.print(buf + "  ");
				}
				printNode(node.child[i], buf);
				buf = buf.deleteCharAt(buf.length() - 1);
			}
		}
	}

	public void print()
	{
		TrieNode node = root;
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < MAXK; i++) {
			if (node.child[i] != null) {
				buf.append((char) (i + 'a'));
				if (node.child[i].isWord()) {
					System.out.print(buf + "  ");
				}
				printNode(node.child[i], buf);
				buf = buf.deleteCharAt(buf.length() - 1);
			}
		}
	}

	// AhoCorasick
	public int AC_Search(String msg)
	{
		int ans = 0;
		TrieNode ptr = root;
		for (int i = 0; i < msg.length(); i++) {
			int idx = msg.charAt(i) - 'a';
			while (ptr.child[idx] == null && ptr != root) {
				ptr = ptr.fail;
			}
			ptr = ptr.child[idx];
			if (ptr == null) {
				ptr = root;
			}
			TrieNode tmp = ptr;
			while (tmp != null && tmp.isWord() == true) {
				ans++;
				tmp = tmp.fail;
			}
		}
		return ans;
	}

	public void Build_AC_Automation()
	{
		int rear = 1, front = 0, i;
		que = new TrieNode[1000];
		que[0] = root;
		root.fail = null;
		while (rear != front) {
			// BFS
			TrieNode cur = que[front++];
			for (i = 0; i < 26; i++)
				if (cur.child[i] != null) {
					if (cur == root) {
						cur.child[i].fail = root;
					}
					else {
						TrieNode failNode = cur.fail;
						while (failNode != null) {
							if (failNode.child[i] != null) {
								cur.child[i].fail = failNode.child[i];
								if (failNode.child[i].isWord == true) {
									// cur.child[i].isWord = true;
								}
								break;
							}
							failNode = failNode.fail;
						}
						if (failNode == null) {
							cur.child[i].fail = root;
						}
					}
					que[rear++] = cur.child[i];
				}
		}
	}

	public static void main(String[] args)
	{
		String v[] = new String[] { "time", "month", "year", "day" };
		Trie trie = new Trie();
		for (String str : v) {
			trie.insert(str);
		}
		trie.del("time");
		trie.print();
	}
}

class TrieNode
{

	TrieNode[]	child;
	TrieNode	fail;
	char		ch;
	boolean		isWord;
	int			count;

	public TrieNode()
	{
		child = new TrieNode[Trie.MAXK];
		fail = null;
		count = 0;
		isWord = false;
	}

	public boolean isWord()
	{
		return isWord;
	}

	public void setWord(boolean isWord)
	{
		this.isWord = isWord;
	}
}