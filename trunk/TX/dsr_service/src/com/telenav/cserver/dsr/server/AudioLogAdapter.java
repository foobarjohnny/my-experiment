package com.telenav.cserver.dsr.server;

import com.sun.grizzly.http.SocketChannelOutputBuffer;
import com.sun.grizzly.tcp.ActionCode;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyRequest;
import com.sun.grizzly.tcp.http11.GrizzlyResponse;
import com.sun.grizzly.util.buf.ByteChunk;
import com.telenav.audio.log.LoggingService;
import com.telenav.audio.log.dsr.AudioLogFactory;
import com.telenav.audio.orm.Persistable;
import com.telenav.audio.streaming.audiolog.AudioLog;
import com.telenav.audio.streaming.audiolog.AudioLogService;
import com.telenav.audio.streaming.common.DataPacket;
import com.telenav.audio.streaming.common.Util;
import com.telenav.audio.streaming.server.TeleNavStreamingFilter;
import com.telenav.cserver.dsr.util.DsrConfigure;
import com.telenav.j2me.datatypes.TxNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: llzhang
 * Date: 2009-9-2
 * Time: 16:43:03
 */
public class AudioLogAdapter extends GrizzlyAdapter {

    private static Logger logger = Logger.getLogger(AudioLogAdapter.class.getName());

    private static final String DSR_URL = "/dsr";
    private static final String LOG_URL = "/log";
    private static final String HEARTBEAT_URL = "/heartbeat";
    private static final String ABSWITCH_URL = "/abswitch";

    private static Map<String, DataPacket> connMap = new ConcurrentHashMap<String, DataPacket>();

    private static final int DUMMY_LENGTH = 32;
    private static final byte[] DUMMY_LENGTH_BYTES = Util.getIntBytes(-DUMMY_LENGTH);

    private static final int BUFFER_SIZE = 512;

    public void service(GrizzlyRequest req, GrizzlyResponse resp) {
        String url = req.getRequestURI().toLowerCase();
        logger.fine("url: " + url);
        if (isDsrRequest(url)) {
            handleHttpDsrRequest(req, resp);
        } else if (isAudioLogRequest(url)) {
            handleAudioLogRequest(req, resp);
        } else if (isHeartBeatRequest(url)) {
            handleHeartBeatRequest(req, resp);
        } else if (isABSwitchRequest(url)){
        	handleABSwitchConfig(req, resp);
        }
    }
    
    private void handleABSwitchConfig(GrizzlyRequest req, GrizzlyResponse resp) {
    	
    	int configValue = Integer.parseInt((req.getParameter("value")));
    	logger.fine("ConfigValue: "+req.getQueryString());
    	DsrConfigure.abSwitchPoint = configValue;
    	logger.fine("ABSwitch config value: "+DsrConfigure.abSwitchPoint);
    	try {
			resp.finishResponse();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		} 	
    }

    private void handleHeartBeatRequest(GrizzlyRequest req, GrizzlyResponse resp) {
        try {
            resp.setContentType("text/xml");
            final String heartbeatXml = HeartBeatUtil.getHeartbeatXml();
            resp.setContentLength(heartbeatXml.length());
            resp.getResponse().sendHeaders();

            ByteChunk bc = new ByteChunk();
            byte[] data = heartbeatXml.getBytes();
            bc.setBytes(data, 0, data.length);
            resp.getResponse().doWrite(bc);
            try {
                req.getRequest().action(ActionCode.ACTION_POST_REQUEST, null);
            } catch (Exception e) {
                logger.log(Level.SEVERE, null, e);
            }
            try {
                resp.getResponse().finish();
            } catch (Exception e) {
                logger.log(Level.SEVERE, null, e);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private void handleAudioLogRequest(GrizzlyRequest req, GrizzlyResponse resp) {
        int contentLength = req.getContentLength();
        logger.fine("contentLength: " + contentLength);
        byte[] wholeBs = readBytes(req);
        if (wholeBs == null || wholeBs.length == 0) {
            close(resp);
            logger.warning("wholeBs empty!");
            return;
        }
        TxNode logNode = TxNode.fromByteArray(wholeBs, 0);
        logger.fine("Log recieved : " + logNode);
        saveLog(logNode.childAt(0));
        close(resp);
    }

    private boolean isHeartBeatRequest(String url) {
        return url.indexOf(HEARTBEAT_URL) >= 0;
    }

    private boolean isAudioLogRequest(String url) {
        return url.indexOf(LOG_URL) >= 0;
    }

    private boolean isDsrRequest(String url) {
        return url.indexOf(DSR_URL) >= 0;
    }
    
    private boolean isABSwitchRequest(String url){
    	return url.indexOf(ABSWITCH_URL) >= 0;
    }

    private void handleHttpDsrRequest(GrizzlyRequest req, GrizzlyResponse resp) {
        String connInfo = ((SocketChannelOutputBuffer) resp.getResponse().getOutputBuffer()).getChannel().toString();

        DataPacket packet = connMap.get(connInfo);
        if (packet == null) {
            logger.log(Level.WARNING, "A new context for " + connInfo);
            packet = new DataPacket(TeleNavStreamingFilter.getPacketListenerFactory().createDataPacketArrivalListener(resp));
            connMap.put(connInfo, packet);
        }
        int size = req.getContentLength();
        logger.log(Level.FINE, "___Received_____" + size + " bytes_____" + req.toString());
        byte[] bs = readBytes(req);
        //do dsr and write back resp
        packet.decode(bs, 0, size);
        if (!packet.isSucc() || packet.isSessionComplete()) {
            //Fail to decode, cancel the selectionKey
            connMap.remove(connInfo);
        }
        try {
            byte[] retBs = new byte[DUMMY_LENGTH];
            resp.setContentLength(retBs.length + DUMMY_LENGTH_BYTES.length);
            ByteChunk bc = new ByteChunk();
            //write -DUMMY_LENGTH as length mean follow DUMMY_LENGTH bytes dummy data
            bc.append(DUMMY_LENGTH_BYTES, 0, DUMMY_LENGTH_BYTES.length);
            //write DUMMY_LENGTH bytes dummy data
            bc.append(retBs, 0, retBs.length);
            resp.getResponse().doWrite(bc);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "write http resp error", e);
        }
    }

    private void saveLog(TxNode logNode) {
        if (logNode == null || logNode.msgsSize() <= 1) {
            return;
        }

        for (int i = 1; i < logNode.msgsSize(); i++) {
            String logItem = logNode.msgAt(i);
            saveLog(logItem);
        }
    }

    private void saveLog(String logStr) {
        String[] slots = logStr.split("[|]");
        if (slots.length != 5) {
            return;
        }
        try {
            long transactionID = Long.parseLong(slots[1]);
            if (transactionID == -1) {
                return;
            }
            boolean isCancelled = Integer.parseInt(slots[4]) == 1;
            int selectedIndex = isCancelled ? 0 : Integer.parseInt(slots[2]);
            String selectedValue = slots[3];
            Persistable log = AudioLogFactory.getClientLog(transactionID, selectedIndex, selectedValue, isCancelled);
            LoggingService.instance.saveLog(log);
        } catch (NumberFormatException e) {
            logger.severe("Message parsing error! Msg = " + logStr);
        }
    }

    private void close(GrizzlyResponse resp) {
        try {
            resp.finishResponse();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private byte[] readBytes(GrizzlyRequest req) {
        try {
            byte[] bs = new byte[BUFFER_SIZE];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = req.getStream();
            int len = 0;
            while ((len = is.read(bs, 0, BUFFER_SIZE)) > 0) {
                baos.write(bs, 0, len);
            }
            return baos.toByteArray();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "readBytes", ex);
            return null;
        }
    }
}
