package com.telenav.browser.movie.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;

public class MockHttpServletResponse extends
		org.apache.struts.mock.MockHttpServletResponse {
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private PrintWriter pw=new PrintWriter(baos);
	private int len;

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		ServletOutputStream sos = new ServletOutputStream() {

			@Override
			public void write(int b) throws IOException {
				baos.write(b);

			}
		};
		return sos;
	}

	public PrintWriter getWriter() throws IOException {
		return pw; 
	}

	@Override
	public void setContentLength(int length) {
		len = length;
	}

	@Override
	public void setHeader(String name, String value) {

	}

	@Override
	public void setContentType(String type) {

	}

	public int getLength() {
		return len;
	}

	public byte[] getOutputStreamBytes() {
		pw.flush();
		return baos.toByteArray();
	}

	public String getOutputStreamString() {
		pw.flush();
		return baos.toString();
	}
}
