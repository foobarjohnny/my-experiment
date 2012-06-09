import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Binary Search Tree
public class BinarySearchTreeNode
{
	protected int					key;
	protected BinarySearchTreeNode	left	= null;
	protected BinarySearchTreeNode	right	= null;
	protected BinarySearchTreeNode	parent	= null;
	private String					color	= "";

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

	public int getKey()
	{
		return key;
	}

	public void setKey(int key)
	{
		this.key = key;
	}

	public BinarySearchTreeNode getLeft()
	{
		return left;
	}

	public void setLeft(BinarySearchTreeNode leftChild)
	{
		this.left = leftChild;
	}

	public BinarySearchTreeNode getRight()
	{
		return right;
	}

	public void setRight(BinarySearchTreeNode rightChild)
	{
		this.right = rightChild;
	}

	public BinarySearchTreeNode getParent()
	{
		return parent;
	}

	public void setParent(BinarySearchTreeNode parent)
	{
		this.parent = parent;
	}

	public BinarySearchTreeNode getGrandParent()
	{
		return this.getParent().getParent();
	}

	public BinarySearchTreeNode getSibling()
	{
		if (this == this.getParent().getLeft()) {
			return this.getParent().getRight();
		}
		else {
			return this.getParent().getLeft();
		}
	}

	public BinarySearchTreeNode getUncle()
	{
		if (this.getParent() == this.getParent().getLeft()) {
			return this.getParent().getRight();
		}
		else {
			return this.getParent().getLeft();
		}
	}

}