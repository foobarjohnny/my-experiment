/**
 * (c) Copyright 2011 TeleNav.
 * 
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.ace;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.apontador.apirequest.proxy.AddressCaptureProxy;
import com.apontador.apirequest.proxy.AddressStatus;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.proxy.AbstractProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * BRValidateAddressProxy
 * 
 * @author kwwang
 * @date 2011-6-27
 */
@BackendProxy
@ThrottlingConf("ValidateAddressProxy")
public class ValidateAddressProxy extends AbstractProxy{

	private AddressCaptureProxy proxy;

	protected ValidateAddressProxy() {
		proxy = new AddressCaptureProxy();
	}

	@ProxyDebugLog
	@Throttling
	public ValidateAddressResponse validateAddress(
			ValidateAddressRequest request, TnContext tc)
			throws ThrottlingException {
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("validateAddress");

		cli.addData("ValidateAddressRequest",
				ReflectionToStringBuilder.toString(request));

		ValidateAddressResponse resp = new ValidateAddressResponse();
		try {
			AddressStatus aceStatus = proxy.validate("br", request.getState(),
					request.getCity(), request.getStreet(),
					request.getZipCode(), request.getCorssStreeOrNumber(), 100,
					request.getSearchType());

			resp.setAddresses(aceStatus);

			cli.addData("validateStatusCode",
					String.valueOf(aceStatus.getStatus()));
			cli.addData("addressSize",
					String.valueOf(aceStatus.getAddresses() != null ? aceStatus
							.getAddresses().length : 0));

		} finally {
			cli.complete();
		}
		return resp;
	}

	@Override
	public String getProxyConfType() {
		return "";
	}
}
