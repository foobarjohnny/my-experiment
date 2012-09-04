package com.telenav.cserver.backend.proxy.adjuggler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;

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
@ThrottlingConf("AdjugglerProxy")
public class AdjugglerProxy extends HttpClientProxy {
	protected AdjugglerProxy() {

	}

	@Override
	public String getProxyConfType() {
		return "ADJUGGLER";
	}

	@ProxyDebugLog
	@Throttling
	public AdjugglerResponse getAdjugglerInfo(AdjugglerRequest request, TnContext tc)
			throws ThrottlingException {
		CliTransaction cli = CliTransactionFactory
				.getInstance(CliConstants.TYPE_MODULE);
		cli.setFunctionName("getAdjugglerInfo");
		cli.addData("request", request.toString());
		String url = request.getUrl();
		AdjugglerResponse response = getAdjugglerInfo(url);
		cli.addData("response", response.toString());
		cli.complete();
		return response;
	}

	protected Map<String, Object> getExtendHttpClientParams() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("http.protocol.allow-circular-redirects", true);

		return paramsMap;
	}

	protected AdjugglerResponse getAdjugglerInfo(String url) {
		ProcessResult processMethod = new ProcessResult() {
			public HttpClientResponse process(HttpMethod method)
					throws IOException {
				AdjugglerResponse resonse = new AdjugglerResponse();
				resonse.setStatusCode(method.getStatusCode());
				byte[] bytes = method.getResponseBody();
				String jsonStr = new String(bytes);
				resonse.setJsonStr(jsonStr);

				return resonse;
			}
		};

		AdjugglerResponse response = (AdjugglerResponse) send(url,
				processMethod);
		return response != null ? response : new AdjugglerResponse();
	}

}
