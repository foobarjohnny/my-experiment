package com.telenav.cserver.dsr.streaming.server;

import com.telenav.audio.streaming.Packet;
import com.telenav.audio.streaming.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: llzhang
 * Date: 5/13/11
 * Time: 6:00 PM
 * Telenav, CO.
 */
public class SocketClient {
	
	private static Logger logger = Logger.getLogger(SocketClient.class.getName());
	
    Socket socket;
    OutputStream out;
    InputStream in;

    private static final int INT_BYTES = 4;

    public SocketClient(String url, int port)
    						throws IOException
    {
        connect(url, port);
    }

    private void connect(String url, int port)
    					throws IOException
    {
    	
        socket = new Socket(url, port);
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }
    
    public void sendPacket(Packet.PacketType type, byte[] content)
										throws IOException
	{
    	sendPacket(type, content, 0, content.length);
	}


    public void sendPacket(Packet.PacketType type, byte[] content, int offset, int length)
    									throws IOException
    {
    	if (out != null)
    	{
            out.write(type.toByte());
            out.write(Utils.toBytes(length));
            out.write(content, offset, length);
            out.flush();
    	}
    	else
    		logger.log(Level.SEVERE, "Trying to send packet to DSR when not connected: "+type);
    }

    public byte[] getResponse() {
        byte[] response = null;

        try {
            byte[] respLengthBs = new byte[INT_BYTES];
            if (!fillBuffer(respLengthBs, in))
            	return null; // TODO do some better error handling here?
            	
            int respLength = Utils.getInt(respLengthBs, 0);
            response = new byte[respLength];
            if (!fillBuffer(response, in))
            	return null;
            
        } catch (IOException e) {
        	logger.log(Level.SEVERE, "Exception while reading DSR server response: ", e);
        }
        return response;
    }
    
    // PER: java.io.InputStream.read(...) contract
	private static final int EOF = -1;
    
	/**
	 * Blocks until it fills the buffer with buffer.length bytes from input stream.
	 * 
	 * Returns true if buffer was filled successfully, and false
	 * if end of file was reached.
	 * 
	 * @param buffer
	 * @return true if buffer was filled, or false if reached EOF
	 */
	static boolean fillBuffer(byte[] buffer, InputStream is)
								throws IOException
	{
		int offset = 0;
        int bytesRead = 0;
        int bytesToRead = buffer.length;
        while ((bytesRead = is.read(buffer, offset, bytesToRead)) != EOF)
        {
        	offset = offset + bytesRead;
        	if (offset == buffer.length)
        		return true;
        	else
        		bytesToRead = bytesToRead - offset;
        }
        
        return false;
	}

    public void disconnect() {
        try {
        	if (out != null)
        		out.close();
        	
        	if (in != null)
        		in.close();
        	
        	if (socket != null)
        		socket.close();
        } catch (IOException ignored) {
        }
    }

    public void setTimeout(int timeout){
    	try {
			socket.setSoTimeout(timeout);
		} catch (SocketException e) {
			return;
		}
    }

}
