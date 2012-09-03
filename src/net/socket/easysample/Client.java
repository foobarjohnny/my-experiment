package net.socket.easysample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//http://tech.163.com/06/0410/09/2EBABUD20009159T_3.html
//http://hanzhengyang0126.blog.163.com/blog/static/11750394520123271073673/
/**
 * http://hi.baidu.com/qiaoyuetian/item/391159cc3c72c012b67a249b
 * http://blog.csdn.net/fw0124/article/details/7452695
 * http://www.bianceng.cn/Programming/Java/201101/22988.htm
 */
public class Client
{
	Socket			socket;
	BufferedReader	in;
	PrintWriter		out;

	public Client()
	{
		try {
			socket = new Socket("localhost", 10000);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader line = new BufferedReader(new InputStreamReader(System.in));

			out.println(line.readLine());
			line.close();
			String s = null;
			while ((s = in.readLine()) == null) {

			}
			System.out.println("client:" + s);
			out.close();
			in.close();
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		new Client();
	}
}