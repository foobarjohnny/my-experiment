package test;

import static test.TestImport.staticMethod;
//����̬���롱���ߡ���̬��Ա���롱
//Static Import���Ƴ�����ֱ��ɡ���̬���롱��
//���ǴӺ����Ͽ�������̬��Ա���롱�Ǹ�Ϊ���е��뷨��
//�������ǵ�����̬���롱��˵���Ƚϼ�̣����ƻ��ǻ���ǿ�����������
//
//������֮�����ڲ������κΰ�����ͽӿڣ��ǼȲ�����import����������Ҳ������import static�������ľ�̬��Ա�ġ�
//import static ����.���ӿ���.��̬��Ա��; enumҲ��������,����Ƚ��������
//�������������������ڲ��඼���Ե���
//import��import static Ҫ�����Ȩ�޹ҹ��� ��Ȼ���ܵ���
//
//Static Import����Ҳ֧��һ�ֲ�����һָ����̬��Ա���Ƶĵ��뷽ʽ����ʱ��Ҫ�����������﷨��
//import static ����.���ӿ���.*;
//ע�����ַ�ʽֻ��ָ���������������ĳ�Աʱ�����Ե�������ӿ��������ң������ǰ�������ӿ�������о�̬��Աȫ�����롣

public class TestImportStatic
{

	public static void a()
	{
		staticMethod();
	}

	public static void main(String[] args)
	{
	}

}
