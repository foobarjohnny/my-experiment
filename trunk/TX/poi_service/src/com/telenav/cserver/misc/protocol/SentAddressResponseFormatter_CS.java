/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.misc.protocol;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.misc.executor.SentAddressResponse;
import com.telenav.cserver.misc.struts.datatype.Address;
import com.telenav.j2me.core.datatypes.DataConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.protocol.constants.NodeTypeDefinitions;
/**
 * SentAddressResponseFormatter_CS
 * @author kwwang
 *
 */
public class SentAddressResponseFormatter_CS implements ProtocolResponseFormatter
{

    @Override
    public void format(Object object, ExecutorResponse[] responses) throws ExecutorException
    {
        SentAddressResponse response = (SentAddressResponse) responses[0];
        TxNode node = (TxNode) object;

        node.addMsg(response.getExecutorType());
        node.addValue(DataConstants.TYPE_SERVER_RESPONSE);
        node.addValue(response.getStatus());
        node.addMsg(response.getErrorMessage());

        if (response.getAddressList() != null && response.getAddressList().size() > 0)
        {
            for (Address addr : response.getAddressList())
            {
                TxNode address = new TxNode();
                address.addValue(NodeTypeDefinitions.TYPE_SENT_ADDRESS_NODE);
                address.addValue(addr.getCreateTime().getTime());
                address.addMsg(addr.getLabel());
                address.addMsg(addr.getStreet());
                address.addMsg(addr.getCity());
                address.addMsg(addr.getProvince());
                address.addMsg(addr.getPostalCode());
                address.addMsg(addr.getCountry());
                node.addChild(address);

                TxNode addrReceiver = new TxNode();
                addrReceiver.addValue(NodeTypeDefinitions.TYPE_ADDRESS_RECEIVER_NODE);
                address.addChild(addrReceiver);
                for (String sentAddrDisplayText : addr.getReceiverList())
                {
                    addrReceiver.addMsg(sentAddrDisplayText);
                }

            }
        }

    }

}
