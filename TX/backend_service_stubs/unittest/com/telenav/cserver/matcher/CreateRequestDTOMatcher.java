/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.matcher;

import com.telenav.services.traffic.tims.v10.CreateRequestDTO;

/**
 * CreateRequestDTOMatcher.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2011-6-14
 *
 */
public class CreateRequestDTOMatcher extends AbstractMatcher<CreateRequestDTO>
{

    public CreateRequestDTOMatcher(CreateRequestDTO expected)
    {
        super(expected);
    }
    
    @Override
    boolean doMatch(CreateRequestDTO actual)
    {
        return    equals(expected, actual, new String[]{"localClientName","localClientVersion","localTransactionId","localContextString"})
               && (expected.getReport() != null && actual.getReport() != null ) ? 
                                   expected.getReport().length == actual.getReport().length :
                                  (( expected.getReport() == null && actual.getReport() == null) ? true : false);
    }
    
}
