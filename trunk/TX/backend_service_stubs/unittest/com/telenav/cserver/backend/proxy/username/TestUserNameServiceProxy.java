package com.telenav.cserver.backend.proxy.username;

import junit.framework.TestCase;

import com.telenav.cserver.backend.StatusConstants;
import com.telenav.datatypes.user.management.v11.Attribute;
import com.telenav.datatypes.user.management.v11.AttributeValue;
import com.telenav.datatypes.user.management.v11.AttributeValueType;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.user.management.v11.GetAttributeResponseDTO;

public class TestUserNameServiceProxy extends TestCase {
	UserNameServiceProxy proxy;
	TnContext tnContext;
	
	public void setUp(){
		proxy = new UserNameServiceProxy();
		tnContext = new TnContext();
	}

	public void testSaveUserLocale(){
		Attribute att1 = new Attribute();
		att1.setName(UserNameServiceHelper.ATTRIBUTE_USER_OPTIN_UPDATE);
		att1.setType(AttributeValueType.STRING);
		AttributeValue attributeValue = new AttributeValue();
		attributeValue.setValue("true");
		att1.setValue(new AttributeValue[] {attributeValue});
		Attribute att2 = new Attribute();
		att2.setName(UserNameServiceHelper.ATTRIBUTE_USER_LOCALE_UPDATE);
		att2.setType(AttributeValueType.STRING);
		AttributeValue attributeValue2 = new AttributeValue();
		attributeValue2.setValue("en_GB");
		att2.setValue(new AttributeValue[] {attributeValue2});
		SaveUserAttributesResponse response = proxy.updateUserAttributesValue("9985413", new Attribute[]{att1,att2} , tnContext);
		assertEquals(StatusConstants.SUCCESS, response.getStatusCode());
	}
	
	public void testGetAttribute(){
		GetAttributeResponseDTO response = proxy.getAttribute("1031785238", UserNameServiceHelper.ATTRIBUTE_USER_LOCALE_UPDATE, tnContext);
		assertEquals(true, response.getAttribute()!=null);
		System.out.println(response.getAttribute().getValue()[0]);
		response = proxy.getAttribute("1031785238", UserNameServiceHelper.ATTRIBUTE_USER_OPTIN_UPDATE, tnContext);
		assertEquals(true, response.getAttribute()!=null);
		System.out.println(response.getAttribute().getValue()[0]);
	}
}
