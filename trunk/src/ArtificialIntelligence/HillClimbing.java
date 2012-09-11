package ArtificialIntelligence;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//爬山法http://www.codeforge.cn/read/125426/pashan.java__html
/**
 * http://hi.baidu.com/mizzletown/item/b1ae89f78b775dd542c36aa8
 * http://blog.csdn.net/ximenchuixuezijin/article/details/5624297
 */
// function HillClimbing(problem) return 一个局部最优状态
// 输入：problem
// 局部变量：current, 一个节点
// neighbor，一个节点
// current = MakeNode(Initial-State(problem));
// loop do
// neighbor = a highest-valued successor of current ;
// if VALUE[neighbor] <= VALUE[current] then return STATE[current];
// current = neighbor ;
public class HillClimbing extends Frame implements ActionListener
{
	static HillClimbing	frm	= new HillClimbing();
	static Button		b;
	static Panel		pl;
	static Label		l;
	static TextField	tf;

	public static void main(String[] args)
	{
		frm.setTitle("最速下降法(爬山法)");
		b = new Button("Start");
		l = new Label("The Height is");
		tf = new TextField(20);
		pl = new Panel();
		pl.setLayout(new FlowLayout());
		pl.add(b);
		pl.add(l);
		pl.add(tf);
		frm.add(pl, "South");
		frm.setSize(700, 700);
		frm.setVisible(true);
		b.addActionListener(frm);
		frm.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}

	public void paint(Graphics g)
	{
		double x = 150, y = 500, l = 300;
		g.drawLine(150, 500, 150, 180);
		while (y >= 200) {
			if (y < 500 && y > 450) {
				g.setColor(new Color(255, 250, 0));
				g.fillRect(10, 160, 30, 10);
			}
			if (y < 450 && y > 400) {
				g.setColor(new Color(255, 200, 0));
				g.fillRect(10, 150, 30, 10);
			}
			if (y < 400 && y > 350) {
				g.setColor(new Color(255, 150, 0));
				g.fillRect(10, 140, 30, 10);
			}
			if (y < 350 && y > 300) {
				g.setColor(new Color(255, 100, 0));
				g.fillRect(10, 130, 30, 10);
			}
			if (y < 300 && y > 275) {
				g.setColor(new Color(255, 50, 0));
				g.fillRect(10, 120, 30, 10);
			}
			if (y < 275 && y > 250) {
				g.setColor(new Color(255, 0, 0));
				g.fillRect(10, 110, 30, 10);
			}
			if (y < 250 && y > 225) {
				g.setColor(new Color(200, 0, 0));
				g.fillRect(10, 100, 30, 10);
			}
			if (y < 225 && y > 205) {
				g.setColor(new Color(150, 0, 0));
				g.fillRect(10, 90, 30, 10);
			}
			if (y < 205 && y > 200) {
				g.setColor(new Color(50, 0, 0));
				g.fillRect(10, 80, 30, 10);
			}
			g.drawLine((int) x, (int) (y -= 2), (int) x + (int) (l -= 2), (int) y);
		}
		g.setColor(Color.black);
		g.drawString("" + (double) 1 / 2.0032, 40, 88);
		g.drawString("" + (double) 1 / 2.125, 40, 98);
		g.drawString("" + (double) 1 / 2.5, 40, 108);
		g.drawString("" + (double) 1 / 3.125, 40, 118);
		g.drawString("" + (double) 1 / 4, 40, 128);
		g.drawString("" + (double) 1 / 6.5, 40, 138);
		g.drawString("" + (double) 1 / 10, 40, 148);
		g.drawString("" + (double) 1 / 14.5, 40, 158);
		g.drawString("" + (double) 1 / 20, 40, 168);
		g.drawString("" + (double) 1 / 2, 150, 200);
		g.drawString("" + (double) 1 / 2.0032, 150, 210);
		g.drawString("" + (double) 1 / 2.125, 150, 225);
		g.drawString("" + (double) 1 / 2.5, 150, 250);
		g.drawString("" + (double) 1 / 3.125, 150, 275);
		g.drawString("" + (double) 1 / 4, 150, 300);
		g.drawString("" + (double) 1 / 6.5, 150, 350);
		g.drawString("" + (double) 1 / 10, 150, 400);
		g.drawString("" + (double) 1 / 14.5, 150, 450);
		g.drawString("" + (double) 1 / 20, 150, 500);
	}

	public void actionPerformed(ActionEvent h)
	{
		Graphics g = getGraphics();
		calculatePartial cP = new calculatePartial();
		double x = -3, y = -3, result, e = 0.01;
		while ((result = Math.sqrt(cP.calculateGrade(x, y, 0) * cP.calculateGrade(x, y, 0) + cP.calculateGrade(x, y, 1) * cP.calculateGrade(x, y, 1))) > e) {
			g.setColor(Color.green);
			g.drawString(".", 150 - (int) (100 * x), 200 - (int) (100 * x));
			x = x + 0.00999 * cP.calculateGrade(x, y, 0);
			y = y + 0.00999 * cP.calculateGrade(x, y, 1);
			tf.setText("" + 1 / (x * x + y * y + 2));
		}
		g.drawString("Max=" + 1 / (x * x + y * y + 2), 300, 200);
	}
}

class calculatePartial
{
	double	result, ac = 0.001;

	double calculateGrade(double x, double y, int isParteXorY)
	{
		if (isParteXorY == 0) {
			result = ((1 / ((x + ac) * (x + ac) + y * y + 2)) - (1 / (x * x + y * y + 2))) / ac;
		}
		if (isParteXorY == 1) {
			result = ((1 / (x * x + (y + ac) * (y + ac) + 2)) - (1 / (x * x + y * y + 2))) / ac;
		}
		return (result);
	}
}
