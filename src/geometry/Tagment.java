package geometry;


/**
 * http://softsurfer.com/Archive/algorithm_0109/algorithm_0109.htm
 * http://www.cnblogs.com/Booble/archive/2011/03/10/1980089.html
 * 记住叉积与面积有关系，有垂直高度有关系
 * 
 * http://blog.csdn.net/zhang20072844/article/category/893217
 * http://www.csie.ntnu.edu.tw/~u91029/ConvexHull.html
 * 
 * @author SDY
 */
public class Tagment
{

	/*
	 * 求从多边形外一点p出发到一个简单多边形的切线,如果存在返回切点,其中rp点是右切点,lp是左切点 注意：p点一定要在多边形外
	 * 输入顶点序列是逆时针排列 原 理:如果点在多边形内肯定无切线;凸多边形有唯一的两个切点,凹多边形就可能有多于两个的切点;
	 * 如果polygon是凸多边形，切点只有两个只要找到就可以,可以化简此算法 如果是凹多边形还有一种算法可以求解:先求凹多边形的凸包,然后求凸包的切线
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

	// // 如果多边形polygon的核存在，返回true，返回核上的一点p.顶点按逆时针方向输入
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
	// //多边形顶点按逆时针方向排列，核肯定在每条边的左侧或边上
	// break;
	// }
	// if(k == vcount) //找到了一个核上的点
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
