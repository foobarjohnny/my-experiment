/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.telenav.cserver.dsr;

import java.io.InputStream;
import java.nio.channels.SelectableChannel;
import java.util.logging.LogManager;

import com.sun.grizzly.tcp.http11.GrizzlyResponse;
import com.telenav.audio.streaming.common.DataPacketArrivalListener;
import com.telenav.audio.streaming.server.DataPacketArrivalListenerFactory;

/**
 *
 * @author yueyulin Jun 27, 2008
 */
public class StreamPacketArrivalListenerFactory extends DataPacketArrivalListenerFactory{
		
    public StreamPacketArrivalListenerFactory() {
        LogManager manager = LogManager.getLogManager();
        InputStream is = null;
        try {
            is = StreamPacketArrivalListenerFactory.class.getResourceAsStream("/logging.properties");
            manager.readConfiguration(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
	}

	@Override
    public DataPacketArrivalListener createDataPacketArrivalListener(Object parameter) {
        if(parameter instanceof SelectableChannel){
            return new StreamPacketArrivalListener((SelectableChannel)parameter);
        }else if(parameter instanceof GrizzlyResponse){
            return new StreamPacketArrivalListener((GrizzlyResponse)parameter);
        }else{
            return null;
        }
    }
}
