package com.telenav.cserver.common.resource;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ResourceFactory.class })
public class TestAbstractRuleMatchHolder {

	private static Element createElementRoot(String xmlPath) throws SAXException, IOException, ParserConfigurationException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream stream = cl.getResourceAsStream(xmlPath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		Document doc = factory.newDocumentBuilder().parse(stream);
		Element root = doc.getDocumentElement();
		return root;
	}

	private AbstractRuleMatchHolder holder = null;

	@Before
	public void setUp() {
		holder = new AbstractRuleMatchHolderImp();
	}

	@Test
	public void testCreateObject() throws SAXException, IOException, ParserConfigurationException {
		Element element = TestAbstractRuleMatchHolder.createElementRoot("resource/testAbstractRuleMatchHolder.xml");

		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(
				ResourceFactory.createResource(EasyMock.anyObject(AbstractRuleMatchHolder.class), EasyMock.anyObject(UserProfile.class),
						EasyMock.anyObject(TnContext.class))).andReturn(element);

		PowerMock.replayAll();

		ResourceContent content = holder.createObject("UnitTest", UnittestUtil.createUserProfile(), UnittestUtil.createTnContext());
		Assert.assertNotNull(content);

		PowerMock.verifyAll();
	}

	class AbstractRuleMatchHolderImp extends AbstractRuleMatchHolder {

	}
}
