package com.telenav.cserver.util;

import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.powermock.api.easymock.PowerMock;

import com.telenav.tnbrowser.util.DataHandler;
@RunWith(Parameterized.class)
public class TestUseMetricSystem {
	private String carrier;
	private String locale;
	private boolean result;
	
	public TestUseMetricSystem(String carrier, String locale, boolean result) {
		this.carrier = carrier;
		this.locale = locale;
		this.result = result;
	}
	
	@Parameters       
	public static Collection<?> prepareData() 
	{        
		Object[][] data = 
		{
				{"Telcel", "en_US", true}, 
				{"Telcel", "es_MX", true},
				{"NII", "en_US", true},
				{"NII", "es_MX", true},
				{"ATT", "en_US", false},
				{"ATT", "es_MX", false},
				{"SprintPCS", "en_US", false},
				{"SprintPCS", "es_MX", false},
				{"Verizon", "en_US", false},
				{"Verizon", "es_MX", false},
				{"Fido", "en_CA", true},
				{"Fido", "fr_CA", true},
				{"Rogers", "en_CA", true},
				{"Rogers", "fr_CA", true},
				{"T-Mobile-UK", "es_ES", true},
				{"T-Mobile-UK", "en_GB", true},
				{"T-Mobile-UK", "de_DE", true},
				{"T-Mobile-UK", "nl_NL", true},
				{"T-Mobile-UK", "it_IT", true},
				{"T-Mobile-UK", "fr_CA", true},
				{"T-Mobile-UK", "pt_PT", true},		
		};                
		return Arrays.asList(data);    
	} 
	
	@Test
	public void testIsUseMetricSystem() {
		DataHandler handler = EasyMock.createMock(DataHandler.class);
		
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER))
				.andReturn(carrier).anyTimes();
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_LOCALE))
				.andReturn(locale).anyTimes();

		
		EasyMock.replay(handler);
		assertEquals(result, TnUtil.isUseMetricSystem(handler));		
		
		PowerMock.verifyAll();
	}
}
