package tree;

//Fibonacci Heap Node
//쳲��������A
public class FibHeapNode
{
	int			key;	// ���
	int			degree; // ��
	FibHeapNode	left;	// ���ֵ�
	FibHeapNode	right;	// ���ֵ�
	FibHeapNode	parent; // �����
	FibHeapNode	child;	// ��һ�����ӽ��
	boolean		marked; // �Ƿ�ɾ����1������

}