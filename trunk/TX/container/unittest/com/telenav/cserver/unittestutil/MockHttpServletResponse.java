package com.telenav.cserver.unittestutil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletOutputStream;

public class MockHttpServletResponse extends org.apache.struts.mock.MockHttpServletResponse {
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
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

	@Override
	public void setContentLength(int length) {
		len = length;
	}

	@Override
	public void setHeader(String name, String value) {

	}

	@Override
	public void addHeader(String key, String value) {
		// do nothing
	}

	@Override
	public void setContentType(String type) {

	}

	@Override
	public void flushBuffer() {
		// do nothing
	}

	public int getLength() {
		return len;
	}

	public byte[] getOutputStreamBytes() {
		return baos.toByteArray();
	}

	public String getOutputStreamString() {
		return baos.toString();
	}

}
