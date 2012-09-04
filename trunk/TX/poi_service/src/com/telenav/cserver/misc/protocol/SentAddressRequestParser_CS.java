/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.misc.protocol;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.misc.executor.SentAddressRequest;
import com.telenav.j2me.datatypes.TxNode;
/**
 * SentAddressRequestParser_CS
 * @author kwwang
 *
 */
public class SentAddressRequestParser_CS implements ProtocolRequestParser
{

    @Override
    public ExecutorRequest[] parse(Object object) throws ExecutorException
    {
        SentAddressRequest request=new SentAddressRequest();
        TxNode node=(TxNode)object;
        request.setUserId(node.valueAt(0));
        return new ExecutorRequest[]{request};
    }

}
