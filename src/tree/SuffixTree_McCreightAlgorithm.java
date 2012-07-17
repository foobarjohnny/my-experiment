package tree;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Build Suffix Tree using McCreight Algorithm
 * 
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/) Licensed under GPL
 * (http://www.opensource.org/licenses/gpl-license.php)
 * http://blog.csdn.net/ljsspace/article/details/6581850
 * @author ljs 2011-07-03
 * 
 */
public class SuffixTree_McCreightAlgorithm
{
	private class SuffixNode
	{
		private String				text;

		private List<SuffixNode>	children	= new LinkedList<SuffixNode>();

		private SuffixNode			link;
		private int					start;
		private int					end;
		private int					pathlen;

		public SuffixNode(String text, int start, int end, int pathlen)
		{
			this.text = text;
			this.start = start;
			this.end = end;
			this.pathlen = pathlen;
		}

		public SuffixNode(String text)
		{
			this.text = text;
			this.start = -1;
			this.end = -1;
			this.pathlen = 0;
		}

		public int getLength()
		{
			if (start == -1)
				return 0;
			else
				return end - start + 1;
		}

		public String getString()
		{
			if (start != -1) {
				return this.text.substring(start, end + 1);
			}
			else {
				return "";
			}
		}

		public boolean isRoot()
		{
			return start == -1;
		}

		public String getCoordinate()
		{
			return "[" + start + ".." + end + "/" + this.pathlen + "]";
		}

		public String toString()
		{
			return getString() + "(" + getCoordinate() + ",link:" + ((this.link == null) ? "N/A" : this.link.getCoordinate()) + ",children:" + children.size() + ")";
		}
	}

	private class State
	{
		private SuffixNode	u;			// parent(head)
		private SuffixNode	w;			// s(head[i-1])
		private SuffixNode	v;			// head[i-1]
		private int			j;			// the global index of text starting
										// from 0 to text.length()
		private boolean		finished;	// is this suffix insertion finished?
	}

	private SuffixNode	root;
	private String		text;

	public SuffixTree_McCreightAlgorithm(String text)
	{
		this.text = text;
	}

	// build a suffix-tree for a string of text
	private void buildSuffixTree() throws Exception
	{
		if (root == null) {
			root = new SuffixNode(text);
			root.link = root; // link to itself
		}

		SuffixNode u = root;
		SuffixNode v = root;
		State state = new State();

		for (int i = 0; i < text.length(); i++) {
			// process each suffix

			SuffixNode s = u.link;

			int uvLen = v.pathlen - u.pathlen;
			if (u.isRoot() && !v.isRoot()) {
				uvLen--;
			}
			int j = s.pathlen + i;

			// init state
			state.u = s;
			state.w = s; // if uvLen = 0
			state.v = s;
			state.j = j;
			state.finished = false;

			// execute fast scan
			if (uvLen > 0) {
				fastscan(state, s, uvLen, j);
			}

			// establish the suffix link with v
			SuffixNode w = state.w;
			v.link = w;

			// execute slow scan
			if (!state.finished) {
				j = state.j;
				state.u = w; // w must be an internal node when
								// state.finished=false, then it must have a
								// suffix link, so u can be updated.
				slowscan(state, w, j);
			}

			u = state.u;
			v = state.v;
		}
	}

	// slow scan until head(=state.v) is found
	private void slowscan(State state, SuffixNode currNode, int j)
	{
		boolean done = false;
		int keyLen = text.length() - j;
		for (int i = 0; i < currNode.children.size(); i++) {
			SuffixNode child = currNode.children.get(i);

			// use min(child.key.length, key.length)
			int childKeyLen = child.getLength();
			int len = childKeyLen < keyLen ? childKeyLen : keyLen;
			int delta = 0;
			for (; delta < len; delta++) {
				if (text.charAt(j + delta) != text.charAt(child.start + delta)) {
					break;
				}
			}
			if (delta == 0) {// this child doesn't match any character with the
								// new key
				// order keys by lexi-order
				if (text.charAt(j) < text.charAt(child.start)) {
					// e.g. child="e" (currNode="abc")
					// abc abc
					// / \ =========> / | \
					// e f insert "c^" c^ e f
					int pathlen = text.length() - j + currNode.pathlen;
					SuffixNode node = new SuffixNode(text, j, text.length() - 1, pathlen);
					currNode.children.add(i, node);
					// state.u = currNode; //currNode is already registered as
					// state.u, so commented out
					state.v = currNode;
					state.finished = true;
					done = true;
					break;
				}
				else { // key.charAt(0)>child.key.charAt(0)
						// don't forget to add the largest new key after
						// iterating all children
					continue;
				}
			}
			else {// current child's key partially matches with the new key
				if (delta == len) {
					if (keyLen > childKeyLen) { // suffix tree with ^ ending
												// can't have other two cases
						// e.g. child="ab"
						// ab ab
						// / \ ==========> / | \
						// e f insert "abc^" c^ e f
						// recursion
						state.u = child;
						j += childKeyLen;
						state.j = j;
						slowscan(state, child, j);
					}
				}
				else {// 0<delta<len

					// e.g. child="abc"
					// abc ab
					// / \ ==========> / \
					// e f insert "abd^" c d^
					// / \
					// e f
					// insert the new node: ab
					int nodepathlen = child.pathlen - (child.getLength() - delta);
					SuffixNode node = new SuffixNode(text, child.start, child.start + delta - 1, nodepathlen);
					node.children = new LinkedList<SuffixNode>();

					int tailpathlen = (text.length() - (j + delta)) + nodepathlen;
					SuffixNode tail = new SuffixNode(text, j + delta, text.length() - 1, tailpathlen);

					// update child node: c
					child.start += delta;
					if (text.charAt(j + delta) < text.charAt(child.start)) {
						node.children.add(tail);
						node.children.add(child);
					}
					else {
						node.children.add(child);
						node.children.add(tail);
					}
					// update parent
					currNode.children.set(i, node);

					// state.u = currNode; //currNode is already registered as
					// state.u, so commented out
					state.v = node;
					state.finished = true;
				}
				done = true;
				break;
			}
		}
		if (!done) {
			int pathlen = text.length() - j + currNode.pathlen;
			SuffixNode node = new SuffixNode(text, j, text.length() - 1, pathlen);
			currNode.children.add(node);
			// state.u = currNode; //currNode is already registered as state.u,
			// so commented out
			state.v = currNode;
			state.finished = true;
		}
	}

	// fast scan until w is found
	private void fastscan(State state, SuffixNode currNode, int uvLen, int j)
	{

		for (int i = 0; i < currNode.children.size(); i++) {
			SuffixNode child = currNode.children.get(i);

			if (text.charAt(child.start) == text.charAt(j)) {
				int len = child.getLength();
				if (uvLen == len) {
					// then we find w
					// uvLen = 0;
					// need slow scan after this child
					state.u = child;
					state.w = child;
					state.j = j + len;
				}
				else if (uvLen < len) {
					// branching and cut child short
					// e.g. child="abc",uvLen = 2
					// abc ab
					// / \ ================> / \
					// e f suffix part: "abd^" c d^
					// / \
					// e f

					// insert the new node: ab; child is now c
					int nodepathlen = child.pathlen - (child.getLength() - uvLen);
					SuffixNode node = new SuffixNode(text, child.start, child.start + uvLen - 1, nodepathlen);
					node.children = new LinkedList<SuffixNode>();

					int tailpathlen = (text.length() - (j + uvLen)) + nodepathlen;
					SuffixNode tail = new SuffixNode(text, j + uvLen, text.length() - 1, tailpathlen);

					// update child node: c
					child.start += uvLen;
					if (text.charAt(j + uvLen) < text.charAt(child.start)) {
						node.children.add(tail);
						node.children.add(child);
					}
					else {
						node.children.add(child);
						node.children.add(tail);
					}

					// update parent
					currNode.children.set(i, node);

					// uvLen = 0;
					// state.u = currNode; //currNode is already registered as
					// state.u, so commented out
					state.w = node;
					state.finished = true;
					state.v = node;

				}
				else {// uvLen>len
						// e.g. child="abc", uvLen = 4
						// abc
						// / \ ================>
						// e f suffix part: "abcdefg^"
						//
						//
						// jump to next node
					uvLen -= len;
					state.u = child;
					j += len;
					state.j = j;
					fastscan(state, child, uvLen, j);
				}
				break;
			}
		}
	}

	// for test purpose only
	public void printTree()
	{
		System.out.format("The suffix tree for S = %s is: %n", this.text);
		this.print(0, this.root);
	}

	private void print(int level, SuffixNode node)
	{
		for (int i = 0; i < level; i++) {
			System.out.format(" ");
		}
		System.out.format("|");
		for (int i = 0; i < level; i++) {
			System.out.format("-");
		}
		// System.out.format("%s(%d..%d/%d)%n",
		// node.getString(),node.start,node.end,node.pathlen);
		System.out.format("(%d,%d)%n", node.start, node.end);
		for (SuffixNode child : node.children) {
			print(level + 1, child);
		}
	}

	public static void main(String[] args) throws Exception
	{
		// test suffix-tree
		System.out.println("****************************");
		String text = "xbxb^"; // the last char must be unique!
		SuffixTree_McCreightAlgorithm stree = new SuffixTree_McCreightAlgorithm(text);
		stree.buildSuffixTree();
		stree.printTree();

		System.out.println("****************************");
		text = "mississippi^";
		stree = new SuffixTree_McCreightAlgorithm(text);
		stree.buildSuffixTree();
		stree.printTree();

		System.out.println("****************************");
		text = "GGGGGGGGGGGGCGCAAAAGCGAGCAGAGAGAAAAAAAAAAAAAAAAAAAAAA^";
		stree = new SuffixTree_McCreightAlgorithm(text);
		stree.buildSuffixTree();
		stree.printTree();

		System.out.println("****************************");
		text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ^";
		stree = new SuffixTree_McCreightAlgorithm(text);
		stree.buildSuffixTree();
		stree.printTree();

		System.out.println("****************************");
		text = "AAAAAAAAAAAAAAAAAAAAAAAAAA^";
		stree = new SuffixTree_McCreightAlgorithm(text);
		stree.buildSuffixTree();
		stree.printTree();

		System.out.println("****************************");
		text = "minimize"; // the last char e is different from other chars, so
							// it is ok.
		stree = new SuffixTree_McCreightAlgorithm(text);
		stree.buildSuffixTree();
		stree.printTree();

		System.out.println("****************************");
		// the example from McCreight's: A Space-Economical Suffix Tree
		// Construction Algorithm
		text = "bbbbbababbbaabbbbbc^";
		stree = new SuffixTree_McCreightAlgorithm(text);
		stree.buildSuffixTree();
		stree.printTree();
	}
}
