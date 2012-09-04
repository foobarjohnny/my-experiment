package com.telenav.cserver.backend.proxy.zagat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.proxy.HttpClientProxy;
import com.telenav.cserver.backend.proxy.HttpClientResponse;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.framework.cli.CliTransactionFactory;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;


@BackendProxy
@ThrottlingConf("ZagatProxy")
public class ZagatProxy extends HttpClientProxy {
	protected ZagatProxy(){
		
	}
	
	@Override
	public String getProxyConfType() {
		// TODO Auto-generated method stub
		return "ZAGAT";
	}

	@ProxyDebugLog
	@Throttling
	public ZagatResponse getZagatResponse(ZagatRequest request, TnContext tc)
	throws ThrottlingException {
		CliTransaction cli = CliTransactionFactory
		.getInstance(CliConstants.TYPE_MODULE);
		cli.setFunctionName("getZagatResponse");
		cli.addData("request", request.toString());
		String url = request.getUrl();
		ZagatResponse response = getZagatResponse(url);
		cli.addData("response", response.toString());
		cli.complete();
		return response;
	}

	public ZagatResponse getZagatResponse(String url)
	{
	ProcessResult processMethod = new ProcessResult() {
		public HttpClientResponse process(HttpMethod method)
		throws IOException {
			ZagatResponse response = new ZagatResponse();
			response.setStatusCode(method.getStatusCode());

			response.setEncoding(((HttpMethodBase)method).getResponseCharSet());
			byte[] bytes = method.getResponseBody();
			response.setInput(new ByteArrayInputStream(bytes));
			return response;
		}
	};

		ZagatResponse response = (ZagatResponse) send(url,
				processMethod);
		
		return response != null ? response : new ZagatResponse();
	}

	
}
