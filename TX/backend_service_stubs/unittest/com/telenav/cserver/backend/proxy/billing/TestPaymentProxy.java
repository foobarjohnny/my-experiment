package com.telenav.cserver.backend.proxy.billing;

import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import junit.framework.TestCase;

import com.telenav.billing2.common.dataTypes.ClientProfile;
import com.telenav.billing2.common.dataTypes.CredentialType;
import com.telenav.billing2.common.dataTypes.Property;
import com.telenav.billing2.common.dataTypes.UserCredential;
import com.telenav.billing2.ws.datatypes.payment.ConfirmPurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.DeclinePurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.PreparePurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.PurchaseItem;
import com.telenav.billing2.ws.datatypes.payment.PurchaseResponse;
import com.telenav.cserver.backend.util.NonEmptyConverter;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestPaymentProxy extends TestCase { 

	private Logger logger = Logger.getLogger(TestPaymentProxy.class);
	private PaymentProxy proxy = null;

	private static final String STATUS_SUCESS = "0000";
	private static final String NO_ORDER_FOUND = "0401";
	private static final String GENERAL_BILLING_ERROR = "0400";
	private static final String DUPLICATE_REQUEST_ERROR = "3400";
	
	@Override
	protected void setUp() throws Exception {
		proxy = new TestablePaymentProxy();
	}

	public void testPreparePurchase4Success() throws ThrottlingException {
		String testPTN = createRandomTestPTN();
		PurchaseResponse response = proxy.preparePurchase(
				createPreparePurchaseRequest(testPTN), new TnContext());
		assertNotNull("response shoud not be null", response);
		assertTrue("status code should be " + STATUS_SUCESS
				+ "but is " + response.getResponseCode(),
				STATUS_SUCESS.equals(response.getResponseCode()));
		assertNotNull("order info should not be null", response.getOrderInfo());
		System.out.println("purchase order id: " + response.getOrderInfo().getOrderId().getValue());
		logger.debug("purchase order id: " + response.getOrderInfo().getOrderId().getValue());
		assertNotNull("order id should not be null", response.getOrderInfo().getOrderId());
		
	}
	
	public void testPreparePurchase4Duplicate() throws ThrottlingException {
		String testPTN = createRandomTestPTN();
		PurchaseResponse response = proxy.preparePurchase(
				createPreparePurchaseRequest(testPTN), new TnContext());
		response = proxy.preparePurchase(
				createPreparePurchaseRequest(testPTN), new TnContext());
		assertNotNull("response shoud not be null", response);
		assertTrue("status code should be " + DUPLICATE_REQUEST_ERROR
				+ "but is " + response.getResponseCode(),
				DUPLICATE_REQUEST_ERROR.equals(response.getResponseCode()));
	}
	
	public void testConfirmPurchase4Success() throws ThrottlingException {
		String testPTN = createRandomTestPTN();
		PurchaseResponse prepareResponse = proxy.preparePurchase(
				createPreparePurchaseRequest(testPTN), new TnContext());
		
		PurchaseResponse response = proxy.confirmPurchase(
				createConfirmPurchaseRequest(prepareResponse.getOrderInfo().getOrderId().getValue()),
				new TnContext());
		assertNotNull("response shoud not be null", response);
		assertTrue("status code should be " + STATUS_SUCESS
				+ "but is " + response.getResponseCode(),
				STATUS_SUCESS.equals(response.getResponseCode()));
	}

	public void testConfirmPurchase4Fail() throws ThrottlingException {
		PurchaseResponse response = proxy.confirmPurchase(
				createErrorConfirmPurchaseRequest(), new TnContext());
		assertNotNull("response shoud not be null", response);
		assertTrue("status code should be "
				+ NO_ORDER_FOUND + " but is "
				+ response.getResponseCode(),
				NO_ORDER_FOUND.equalsIgnoreCase(response.getResponseCode()));
	}
	
	public void testDeclinePurchase4Success() throws ThrottlingException {
		String testPTN = createRandomTestPTN();
		PurchaseResponse prepareResponse = proxy.preparePurchase(
				createPreparePurchaseRequest(testPTN), new TnContext());
		PurchaseResponse response = proxy.declinePurchase(
				createDeclinePurchaseRequest(prepareResponse.getOrderInfo().getOrderId().getValue()), new TnContext());
		assertNotNull("response shoud not be null", response);
		assertTrue("status code should be " + STATUS_SUCESS
				+ ",but is " + response.getResponseCode(),
				STATUS_SUCESS.equals(response.getResponseCode()));
	}

	public void testDeclinePurchase4Fail() throws ThrottlingException {
		PurchaseResponse response = proxy.declinePurchase(
				createErrorDeclinePurchaseRequest(), new TnContext());
		assertNotNull("response shoud not be null", response);
		assertTrue("status code should be "
				+ NO_ORDER_FOUND + " but is "
				+ response.getResponseCode(),
				NO_ORDER_FOUND.equals(response.getResponseCode()));
	}

	

	private ConfirmPurchaseRequest createConfirmPurchaseRequest(long orderId) {
		ConfirmPurchaseRequest request = new ConfirmPurchaseRequest();
		request.setOrderId(NonEmptyConverter.toNonEmptyLong(orderId));
		
		//set extra properties here.
		Property param = new Property();
		param.setKey("ORDER_NUMBER");
		param.setValue("iphone receipt");
		request.addExtraProperty(param);
		
		ClientProfile cp = createClientProfile();
		request.setClientProfile(cp);
		
		return request;
	}

	private ConfirmPurchaseRequest createErrorConfirmPurchaseRequest() {
		long orderId = -1;
		ConfirmPurchaseRequest request = new ConfirmPurchaseRequest();
		request.setOrderId(NonEmptyConverter.toNonEmptyLong(orderId));

		ClientProfile cp = createClientProfile();
		request.setClientProfile(cp);
		
		return request;
	}
	
	private DeclinePurchaseRequest createDeclinePurchaseRequest(long orderId) {
		DeclinePurchaseRequest request = new DeclinePurchaseRequest();
		request.setOrderId(NonEmptyConverter.toNonEmptyLong(orderId));

		ClientProfile cp = createClientProfile();
		request.setClientProfile(cp);
		return request;
	}

	private DeclinePurchaseRequest createErrorDeclinePurchaseRequest() {
		long orderId = -1;
		DeclinePurchaseRequest request = new DeclinePurchaseRequest();
		request.setOrderId(NonEmptyConverter.toNonEmptyLong(orderId));

		ClientProfile cp = createClientProfile();
		request.setClientProfile(cp);
		return request;
	}
	
	private String createRandomTestPTN(){
		Random rand = new Random();
		String testPTN = "" + Math.abs(rand.nextLong());
		if(testPTN.length()< 10){
			testPTN = testPTN.concat(StringUtils.repeat("0", 10-testPTN.length()));
		}else if(testPTN.length() > 10){
			testPTN = testPTN.substring(0, 10);
		}
		return testPTN;
	}
	
	private PreparePurchaseRequest createPreparePurchaseRequest(String ptn) {
		PreparePurchaseRequest request = new PreparePurchaseRequest();

		UserCredential uc = new UserCredential();
		uc.setCredentialType(CredentialType.PTN);
		uc.setCredentialId(ptn);

		request.setUserCredential(uc);

		PurchaseItem item = new PurchaseItem();
		item.setOfferCode("tnsc_itunes_30D");
		item.setQuantity(NonEmptyConverter.toNonEmptyInteger(1));

		PurchaseItem[] items = new PurchaseItem[1];
		items[0] = item;

		request.setPurchaseItems(items);

		ClientProfile profile = createClientProfile();

		profile.setAppCode(ServiceProvisioningHelper.NAVIGATION_APP_CODE);
		request.setClientProfile(profile);
		return request;

	}

	private ClientProfile createClientProfile(){
		ClientProfile cp = new ClientProfile();
		cp.setCarrierCode("5004");
		cp.setAppCode("");
		cp.setClientProductType("SCOUT_FREE");
		cp.setPhoneModel("IPHONE4");
		cp.setPlatform("IPHONE");
		cp.setProgramCode("SCOUTPROG");
		cp.setRequestSource("TELENAV_CSERVER");
		cp.setClientVersion("7.1");
		return cp;
	}
	class TestablePaymentProxy extends PaymentProxy {

	}
}
