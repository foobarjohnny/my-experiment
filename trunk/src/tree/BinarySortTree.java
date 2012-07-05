package tree;
//Binary Search Tree
public class BinarySortTree
{
	private int				item;
	private BinarySortTree	lChild;
	private BinarySortTree	rChild;

	public BinarySortTree sortTree(int[] data)
	{
		BinarySortTree T = null;
		int length = data.length;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				insertBST(T, data[i]);
			}
		}
		return T;

	}

	public void insertBST(BinarySortTree T, int item)
	{
		BinarySortTree p, q;
		p = new BinarySortTree();
		p.setItem(item);
		p.setlChild(null);
		p.setrChild(null);
		if (T == null) {
			T = p;
		}
		else {
			q = T;
			while (true) {
				if (item < q.getItem()) {
					if (q.getlChild() != null) {
						q = q.getlChild();
					}
					else {
						q.setlChild(p);
						break;
					}
				}
				else {
					if (q.getrChild() != null) {
						q = q.getrChild();
					}
					else {
						q.setrChild(p);
						break;
					}
				}
			}
		}
	}

	public void insertBST_recursive(BinarySortTree T, int item)
	{
		BinarySortTree p;
		p = new BinarySortTree();
		p.setItem(item);
		p.setlChild(null);
		p.setrChild(null);
		if (T == null) {
			T = p;
		}
		else {
			if (item < T.getItem()) {
				insertBST_recursive(T.getlChild(), item);
			}
			else {
				insertBST_recursive(T.getrChild(), item);
			}
		}
	}

	public boolean deleteBSTByItem(BinarySortTree T, int item)
	{
		BinarySortTree current, parent;
		current = T;
		parent = T;
		while (true) {
			if (current == null) {
				return false;
			}
			if (current.getItem() == item) {
				return deleteBST(T, current, parent);
			}
			else {
				if (item < current.getItem()) {
					parent = current;
					current = current.getlChild();
				}
				else {
					parent = current;
					current = current.getrChild();
				}
			}
		}
	}

	public boolean deleteBST(BinarySortTree T, BinarySortTree current, BinarySortTree parent)
	{
		if (T == null || current == null || parent == null) {
			return false;
		}
		BinarySortTree newCur, newPar;
		if (current.getlChild() == null && current.getrChild() == null) {
			newCur = null;
		}
		else if (current.getlChild() != null && current.getrChild() == null) {
			newCur = current.getlChild();
		}
		else if (current.getlChild() == null && current.getrChild() != null) {
			newCur = current.getrChild();
		}
		else {
			newPar = current;
			newCur = newPar.getrChild();
			while (newCur.getlChild() != null) {
				newPar = newCur;
				newCur = newCur.getlChild();
			}
			if (newPar != current) {
				newPar.setlChild(newCur.getrChild());
				newCur.setrChild(current.getrChild());
				newCur.setlChild(current.getlChild());
			}
			else {
				newCur.setlChild(current.getlChild());
			}

		}
		if (T == current) {
			T = newCur;
		}
		else {
			if (parent.getlChild() == current) {
				parent.setlChild(newCur);
			}
			else {
				parent.setrChild(newCur);
			}
		}

		current.setlChild(null);
		current.setrChild(null);
		return true;
	}

	public BinarySortTree searchBST(BinarySortTree T, int item)
	{
		while (T != null) {
			if (T.getItem() == item) {
				return T;
			}
			else if (T.getItem() > item) {
				T = T.getlChild();
			}
			else {
				T = T.getrChild();
			}
		}
		return null;
	}

	public BinarySortTree searchBST_recursive(BinarySortTree T, int item)
	{
		if (T == null) {
			return null;
		}
		if (T.getItem() == item) {
			return T;
		}
		else if (T.getItem() > item) {
			return searchBST_recursive(T.getlChild(), item);
		}
		else {
			return searchBST_recursive(T.getrChild(), item);
		}
	}

	public int getItem()
	{
		return item;
	}

	public void setItem(int item)
	{
		this.item = item;
	}

	public BinarySortTree getlChild()
	{
		return lChild;
	}

	public void setlChild(BinarySortTree lChild)
	{
		this.lChild = lChild;
	}

	public BinarySortTree getrChild()
	{
		return rChild;
	}

	public void setrChild(BinarySortTree rChild)
	{
		this.rChild = rChild;
	}

	public static void main(String[] args)
	{
		BinarySortTree test = new BinarySortTree();
	}

}