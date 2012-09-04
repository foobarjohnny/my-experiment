/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.misc.protocol.v20;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.misc.executor.v20.SentAddressResponse;
import com.telenav.cserver.misc.struts.datatype.Address;
import com.telenav.cserver.util.TnUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author pzhang
 * 
 * @version 1.0, 2009-5-25
 * copy and update by xfliu at 2011/12/6
 */
public class SentAddressResponseFormatter extends
        BrowserProtocolResponseFormatter {
    private static Logger log = Logger
            .getLogger(SentAddressResponseFormatter.class);

    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) throws Exception {
        SentAddressResponse response = (SentAddressResponse) executorResponse;
        DataHandler handler = new DataHandler(httpRequest);
        String action = TnUtil.filterLastPara(httpRequest.getParameter("action"));
        String path = "";
        String popupMessage = "";
        String popupMsgKey = "";


        // delete action
        if ("delete".equals(action)) {

            // prepare message
            popupMessage = "addressinfo.delete.msg";
            // refresh screen to sent address list screen
            path = "summary";
        }
        else if (("".equals(action))||("sync".equals(action))) {
            path = "summary";

            List<Address> list = response.getAddressList();
            if (list.size() == 0) {
                popupMessage = "You have not sent any address yet.";
                popupMsgKey = "addressinfo.no.sent.address";
            }
            
            JSONArray joList = new JSONArray();
            for (Address address : list) {
                JSONObject jo = address.toJSON();
                joList.put(jo);
            }

            TxNode node = new TxNode();
            node.addMsg(joList.toString());
            handler.setParameter("addressDataNode", node);
            
            httpRequest.setAttribute("addressList", list);
        }
        // address detail flow
        else if ("detail".equals(action)) {
            log.info("SendAddress.doAction() detail.");
            // get the address information base on the selected index
            path = "detail";
        }
        response.setPath(path);
        // prepare the data used by view
        httpRequest.setAttribute("popupMessage", popupMessage);
        httpRequest.setAttribute("popupMsgKey", popupMsgKey);
        httpRequest.setAttribute("DataHandler", handler);
    }
}
