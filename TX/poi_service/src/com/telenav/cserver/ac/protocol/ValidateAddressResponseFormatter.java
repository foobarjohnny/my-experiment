package com.telenav.cserver.ac.protocol;

import java.util.List;

import com.telenav.cserver.ac.executor.ValidateAddressResponseACEWS;
import com.telenav.cserver.backend.commutealert.DataConverter;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;

public class ValidateAddressResponseFormatter implements ProtocolResponseFormatter
{
    
    @Override
    public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
    {
        TxNode node = (TxNode)formatTarget;
        
        ValidateAddressResponseACEWS response = (ValidateAddressResponseACEWS)responses[0];
        node.addMsg(response.getExecutorType());
        node.addMsg(response.getErrorMessage());
        node.addValue(DataConstants.TYPE_SERVER_RESPONSE);
        node.addValue(response.getStatus());
        node.addValue(response.getGeoCodeStatusCode());
        List<GeoCodedAddress> geoCodedAddressList = response.getAddresses();
        
        if (geoCodedAddressList != null) {
            for (GeoCodedAddress geoCodedAddress : geoCodedAddressList)
            {
                 node.addChild(toTxNode(geoCodedAddress));
            }
        }
    }
    
    public static TxNode toTxNode(GeoCodedAddress geoCodedAddress)
    {
        Stop stop = DataConverter.convertGeoCodedAddressToStop(geoCodedAddress);
        return stop.toTxNode();
    }

}
