package com.telenav.cserver.matcher;

import static org.easymock.EasyMock.reportMatcher;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.me.JSONObject;

import com.telenav.cserver.common.resource.LoadOrders;
import com.telenav.cserver.common.resource.ResourceHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.txnode.ByteArrayWrapper;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;


public class MatchBox
{
	public static TxNode txNodeEquals(TxNode txNode) {
		  reportMatcher(new TxNodeMatcher(txNode));
		  return txNode;
	}
	
	public static UserProfile userProfileEquals(UserProfile userProfile) {
		  reportMatcher(new UserProfileMatcher(userProfile));
		  return userProfile;
	}
	
	public static TnContext tnContextEquals(TnContext tnContext) {
		  reportMatcher(new TnContextMatcher(tnContext));
		  return tnContext;
	}
	
	public static ExecutorResponse[] executorResponseEquals(ExecutorResponse[] a) {
		  reportMatcher(new ExecutorResponseArrMatcher(a));
		  return a;
	}
	public static LoadOrders loadOrdersEquals(LoadOrders loadOrders){
		reportMatcher(new LoadOrdersMatcher(loadOrders));
		return loadOrders;
	}
	public static ResourceHolder resourceHolderEquals(ResourceHolder resourceHolder){
		reportMatcher(new ResourceHolderMatcher(resourceHolder));
		return resourceHolder;
	}
	public static byte[] byteArrEquals(byte[] b){
		reportMatcher(new ByteArrMatcher(b));
		return b;
	}
	public static JSONObject JSONObjectEquals(JSONObject o){
		reportMatcher(new JSONObjectMatcher(o));
		return o;
	}
	
	public static ByteArrayWrapper ByteArrayWrapperEquals(ByteArrayWrapper o){
		reportMatcher(new ByteArrayWrapperMatcher(o));
		return o;
	}
	
	public static String StringEquals(String o){
		reportMatcher(new StringMatcher(o));
		return o;
	}
	
	public static HttpServletRequest httpServletRequestEquals(HttpServletRequest o){
		reportMatcher(new HttpServletRequestMatcher(o));
		return o;
	}
	
	public static HttpServletResponse httpServletResponseEquals(HttpServletResponse o){
		reportMatcher(new HttpServletResponseMatcher(o));
		return o;
	}
	
	public static OutputStream outputStreamEquals(OutputStream o){
		reportMatcher(new OutputStreamMatcher(o));
		return o;
	}
	
}
