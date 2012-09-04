/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.protocol.v20;

import com.telenav.cserver.ac.executor.v20.ValidateAddressRequestACEWS;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.j2me.datatypes.TxNode;

/**
 * ValidateAddressRequestParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Dec 7, 2010
 * copy and update by xfliu at 2011/12/6
 *
 */
public class ValidateAddressRequestParser implements ProtocolRequestParser
{
    

    @Override
    public ExecutorRequest[] parse(Object object) throws ExecutorException
    {
        ExecutorRequest[] requests = new ExecutorRequest[1];
        ValidateAddressRequestACEWS request = new ValidateAddressRequestACEWS();
        requests[0] = request;

        TxNode node = (TxNode)object;
        request.setFirstLine(node.msgAt(1));
        request.setLastLine(node.msgAt(2));
        request.setStreet1(node.msgAt(3));
        request.setStreet2(node.msgAt(4));
        request.setCity(node.msgAt(5));
        request.setState(node.msgAt(6));
        request.setCountry(processCountry(node.msgAt(7)));
        request.setZip(node.msgAt(8));

        //TODO need talk with client guys
        request.setSuite(node.msgAt(9));
        request.setSublocality(node.msgAt(10));
        request.setLocality(node.msgAt(11));
        request.setLocale(node.msgAt(12));
        request.setSubStreet(node.msgAt(13));
        request.setBuildingName(node.msgAt(14));
        request.setAddressId(node.msgAt(15));
        return requests;
    }
    
    private String processCountry(String country)
    {
        String result = country; 
        if ("USA".equalsIgnoreCase(country)) {
            result = "US";
        }
        if ("CAN".equalsIgnoreCase(country)) {
            result = "CA";
        }
        if ("MEX".equalsIgnoreCase(country)) {
            result = "MX";
        }
        
        if ("CHN".equalsIgnoreCase(country)) {
            result = "CN";
        }
        return result;
    }
    

}
