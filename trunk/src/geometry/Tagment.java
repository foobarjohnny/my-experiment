package geometry;


/**
 * http://softsurfer.com/Archive/algorithm_0109/algorithm_0109.htm
 * http://www.cnblogs.com/Booble/archive/2011/03/10/1980089.html
 * ��ס���������й�ϵ���д�ֱ�߶��й�ϵ
 * 
 * http://blog.csdn.net/zhang20072844/article/category/893217
 * http://www.csie.ntnu.edu.tw/~u91029/ConvexHull.html
 * 
 * @author SDY
 */
public class Tagment
{

	/*
	 * ��Ӷ������һ��p������һ���򵥶���ε�����,������ڷ����е�,����rp�������е�,lp�����е� ע�⣺p��һ��Ҫ�ڶ������
	 * ���붥����������ʱ������ ԭ ��:������ڶ�����ڿ϶�������;͹�������Ψһ�������е�,������ξͿ����ж����������е�;
	 * ���polygon��͹����Σ��е�ֻ������ֻҪ�ҵ��Ϳ���,���Ի�����㷨 ����ǰ�����λ���һ���㷨�������:���󰼶���ε�͹��,Ȼ����͹��������
	 */
	void pointtangentpoly(int vcount, Point polygon[], Point p, Point rp, Point lp)
	{
		LineSeg ep = new LineSeg();
		LineSeg en = new LineSeg();
		boolean blp, bln;
		rp = polygon[0];
		lp = polygon[0];
		for (int i = 1; i < vcount; i++) {
			ep.s = polygon[(i + vcount - 1) % vcount];
			ep.e = polygon[i];
			en.s = polygon[i];
			en.e = polygon[(i + 1) % vcount];
			blp = multiply(ep.e, p, ep.s) >= 0; // p is to the left of pre edge
			bln = multiply(en.e, p, en.s) >= 0; // p is to the left of next edge
			if (!blp && bln) {
				if (multiply(polygon[i], rp, p) > 0) {// polygon[i] is above rp
					rp = polygon[i];
				}
			}
			if (blp && !bln) {
				if (multiply(lp, polygon[i], p) > 0) { // polygon[i] is below lp
					lp = polygon[i];
				}
			}
		}
		return;
	}

	double multiply(Point sp, Point ep, Point op)
	{
		return ((sp.x - op.x) * (ep.y - op.y) - (ep.x - op.x) * (sp.y - op.y));
	}

	// // ��������polygon�ĺ˴��ڣ�����true�����غ��ϵ�һ��p.���㰴��ʱ�뷽������
	// bool core_exist(int vcount,POINT polygon[],POINT &p)
	// {
	// int i,j,k;
	// LINESEG l;
	// LINE lineset[MAXV];
	// for(i=0;i<vcount;i++)
	// {
	// lineset[i]=makeline(polygon[i],polygon[(i+1)%vcount]);
	// }
	// for(i=0;i<vcount;i++)
	// {
	// for(j=0;j<vcount;j++)
	// {
	// if(i == j)continue;
	// if(lineintersect(lineset[i],lineset[j],p))
	// {
	// for(k=0;k<vcount;k++)
	// {
	// l.s=polygon[k];
	// l.e=polygon[(k+1)%vcount];
	// if(multiply(p,l.e,l.s)>0)
	// //����ζ��㰴��ʱ�뷽�����У��˿϶���ÿ���ߵ��������
	// break;
	// }
	// if(k == vcount) //�ҵ���һ�����ϵĵ�
	// break;
	// }
	// }
	// if(j<vcount)
	// break;
	// }
	// if(i<vcount)
	// return true;
	// else
	// return false;

}
