import java.util.ArrayList;

enum NodeKind
{
	LN, BN
};

/**
 * Trie���
 */
class TrieNode
{

	char		key;
	TrieNode[]	points	= null;
	NodeKind	kind	= null;
}

/**
 * TrieҶ�ӽ��
 */
class LeafNode extends TrieNode
{

	LeafNode(char k)
	{
		super.key = k;
		super.kind = NodeKind.LN;
	}
}

/**
 * Trie�ڲ����
 */
class BranchNode extends TrieNode
{

	BranchNode(char k)
	{
		super.key = k;
		super.kind = NodeKind.BN;
		super.points = new TrieNode[27];
	}
}

/**
 * Trie��
 * 
 * @author heartraid
 */
/**
 * @author dysong
 *
 */
public class StandardTrie
{

	private TrieNode	root	= new BranchNode(' ');

	/**
	 * ��Tire�в����ַ���
	 */
	public void insert(String word)
	{

		// System.out.println("�����ַ�����"+word);
		// �Ӹ�������
		TrieNode curNode = root;
		// Ϊ�������ַ�������X�в�����һ����������һ������ǰ׺
		word = word + "$";
		// ��ȡÿ���ַ�
		char[] chars = word.toCharArray();
		// ����
		for (int i = 0; i < chars.length; i++) {
			// System.out.println("   ����"+chars[i]);
			if (chars[i] == '$') {
				curNode.points[26] = new LeafNode('$');
				// System.out.println("   �������,ʹ��ǰ���"+curNode.key+"�ĵ�26����ָ��ָ���ַ���$");
			}
			else {
				int pSize = chars[i] - 'a';
				if (curNode.points[pSize] == null) {
					curNode.points[pSize] = new BranchNode(chars[i]);
					// System.out.println("   ʹ��ǰ���"+curNode.key+"�ĵ�"+pSize+"����ָ��ָ���ַ�: "+chars[i]);
					curNode = curNode.points[pSize];
				}
				else {
					// System.out.println("   �����룬�ҵ���ǰ���"+curNode.key+"�ĵ�"+pSize+"����ָ���Ѿ�ָ���ַ�: "+chars[i]);
					curNode = curNode.points[pSize];
				}
			}
		}
	}

	/**
	 * Trie���ַ���ȫ��ƥ��
	 */
	/**
	 * @param word
	 * @return
	 */
	public boolean fullMatch(String word)
	{
		// System.out.print("�����ַ�����"+word+"\n����·����");
		// �Ӹ�������
		TrieNode curNode = root;
		// ��ȡÿ���ַ�
		char[] chars = word.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			int pSize = chars[i] - 'a';
			if (curNode.points[pSize] == null) {
				System.out.println(" ��ʧ�ܡ�");
				return false;
			}
			else {
				curNode = curNode.points[pSize];
				System.out.print(curNode.key + " -> ");
			}
		}
		if (curNode.points[26] != null) {
			System.out.print('$');
			System.out.println(" ���ɹ���");
			return true;
		}
		else {
			System.out.println(" ��ʧ�ܡ���ΪTrie�е�ĳ��ǰ׺");
			return false;
		}
	}

	/**
	 * �ȸ�����Tire��
	 */
	private void preRootTraverse(TrieNode curNode)
	{

		if (curNode != null) {
			System.out.print(curNode.key + " ");
			if (curNode.kind == NodeKind.BN) {
				for (TrieNode childNode : curNode.points)
					preRootTraverse(childNode);
			}
		}

	}

	/**
	 * �õ�Trie�����
	 */
	public TrieNode getRoot()
	{
		return this.root;
	}

	/**
	 * ����
	 */
	public static void main(String[] args)
	{

		StandardTrie trie = new StandardTrie();
		trie.insert("bear");
		trie.insert("bell");
		trie.insert("bid");
		trie.insert("bull");
		trie.insert("buy");
		trie.insert("sell");
		trie.insert("stock");
		trie.insert("stop");

		trie.preRootTraverse(trie.getRoot());

		trie.fullMatch("stoops");
	}
}