package tree;

//Fibonacci Heap Node
//斐波那契结点A
public class FibHeapNode
{
	int			key;	// 结点
	int			degree; // 度
	FibHeapNode	left;	// 左兄弟
	FibHeapNode	right;	// 右兄弟
	FibHeapNode	parent; // 父结点
	FibHeapNode	child;	// 第一个孩子结点
	boolean		marked; // 是否被删除第1个孩子

}