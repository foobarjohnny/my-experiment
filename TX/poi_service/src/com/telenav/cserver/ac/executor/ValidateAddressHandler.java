/**
 * (c) Copyright 2011 TeleNav.
 * 
 * All Rights Reserved.
 */
package com.telenav.cserver.ac.executor;

import com.telenav.cserver.framework.executor.ExecutorContext;

/**
 * ValidateAddressHandler
 * 
 * @author kwwang
 * @date 2011-6-30
 */
public interface ValidateAddressHandler
{
    String doValidateAddress(final ValidateAddressRequestACEWS req, final ValidateAddressResponseACEWS resp, final ExecutorContext context)
            throws Exception;
}
