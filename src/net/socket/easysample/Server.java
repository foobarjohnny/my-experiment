package net.socket.easysample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	private ServerSocket	ss;
	private Socket			socket;
	private BufferedReader	in;
	private PrintWriter		out;

	public Server()
	{
		try {
			ss = new ServerSocket(10000);

			while (true) {
				socket = ss.accept();
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				String line = in.readLine();
				System.out.println("server"+line);
				out.println("you input is :" + line);
				out.close();
				in.close();
				socket.close();
			}
		}
		catch (IOException e) {
		}
		finally {
			try {
				ss.close();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args)
	{
		new Server();
	}
}