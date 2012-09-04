/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.protobuf;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.j2me.datatypes.GpsData;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoGpsFix;
import com.telenav.j2me.framework.protocol.ProtoGpsList;
import com.telenav.j2me.framework.protocol.ProtoUserProfile;
import com.telenav.j2me.framework.util.ProtocolBufferUtil;

/**
 * TestProtocolBufferRequestParser
 * @author kwwang
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ProtocolBufferUtil.class,ExecutorDataFactory.class,ProtoUserProfile.class,ProtoGpsList.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory"})
public class TestProtocolBufferRequestParser extends TestCase{
//	private ProtocolBufferRequestParser protocolBufferRequestParser = new ProtocolBufferRequestParser();
//	private ProtoUserProfile protoUserProfile = null;
//	private ProtoGpsList pGpsList = null;
//	//define mock object
//	private ExecutorDataFactory executorDataFactory = PowerMock.createMock(ExecutorDataFactory.class);
//	private ProtocolRequestParser protocolRequestParser = PowerMock.createMock(ProtocolRequestParser.class);
//    
//	
//	@Override
//	protected void setUp() throws Exception {
//		protoUserProfile = UnittestUtil.createProtoUserProfile();
//		pGpsList = UnittestUtil.createProtoGpsList();
//	}
//	
//	public void testParse_fail() throws ExecutorException{
//		//define variables
//		byte[] object = new byte[]{65,66,67,68,69};
//		//prepare and replay
//		PowerMock.mockStatic(ProtocolBufferUtil.class);
//		EasyMock.expect(ProtocolBufferUtil.toBufArray(object)).andReturn(null);
//		PowerMock.replayAll();
//		
//		//invoke and verify
//		protocolBufferRequestParser.parse(object);
//		PowerMock.verifyAll();
//	}
//	
//	public void testParse() throws ExecutorException, IOException{
//		//define variables
//		byte[] object = new byte[]{65,66,67,68,69};
//		
//		
//		ProtocolBuffer[] pBuf = new ProtocolBuffer[3];
//		ProtocolBuffer pBuf0 = new ProtocolBuffer();
//		ProtocolBuffer pBuf1 = new ProtocolBuffer();
//		ProtocolBuffer pBuf2 = new ProtocolBuffer();
//		
//		pBuf[0] = pBuf0;
//		pBuf[1] = pBuf1;
//		pBuf[2] = pBuf2;
//		
//		pBuf0.setObjType("profile");
//		pBuf0.setBufferData(object);
//		pBuf1.setObjType("gps");
//		pBuf1.setBufferData(object);
//		pBuf2.setObjType("else");
//		
//		
//		ExecutorRequest[] executorRequests = new ExecutorRequest[3];
//		executorRequests[0] = new ExecutorRequest();
//		executorRequests[1] = new ExecutorRequest();
//		executorRequests[2] = new ExecutorRequest();
//		//prepare and replay
//		PowerMock.mockStatic(ProtocolBufferUtil.class);
//		EasyMock.expect(ProtocolBufferUtil.toBufArray(object)).andReturn(pBuf);
//		
//		//buffer.getObjType() == others
//		PowerMock.mockStatic(ExecutorDataFactory.class);
//		EasyMock.expect(ExecutorDataFactory.getInstance()).andReturn(executorDataFactory);
//		EasyMock.expect(executorDataFactory.createProtocolRequestParser("Proto_else")).andReturn(protocolRequestParser);
//		EasyMock.expect(protocolRequestParser.parse(pBuf2)).andReturn(executorRequests);
//		//buffer.getObjType() == profile
//		PowerMock.mockStatic(ProtoUserProfile.class);
//		EasyMock.expect(ProtoUserProfile.parseFrom(EasyMock.aryEq(object))).andReturn(protoUserProfile);
//		//buffer.getObjType()== gps
//		PowerMock.mockStatic(ProtoGpsList.class);
//		EasyMock.expect(ProtoGpsList.parseFrom(EasyMock.aryEq(object))).andReturn(pGpsList);
//		
//		PowerMock.replayAll();
//		
//		//invoke and verify
//		ExecutorRequest[] result = protocolBufferRequestParser.parse(object);
//		PowerMock.verifyAll();
//		
//		//assert
//		assertEquals(3,result.length);
//    }
	
	
    @Test
    public void handleDecryptionIssueWithPtnSourceFromSimCard() throws ExecutorException
    {
        UserProfile user = new UserProfile();
        MockProtocolBufferRequestParser parser = new MockProtocolBufferRequestParser();
        ProtoUserProfile.Builder userBuilder = ProtoUserProfile.newBuilder();
        userBuilder.setPtnSource(UserProfile.PTN_SOURCE_FROM_SIM_CARD);
        userBuilder.setUserId("5900112");
        userBuilder.setMin("40859212365");
        
        parser.handleDecryptionIssue(user, userBuilder.build());
        
        Assert.assertEquals(user.getPtnSource(), UserProfile.PTN_SOURCE_FROM_SIM_CARD);
        Assert.assertEquals(user.getMin(), "40859212365");
        Assert.assertEquals(user.getUserId(), "5900112");
        

        userBuilder.setPtnSource(UserProfile.PTN_SOURCE_FROM_USER_INPUT);

    }
    
    @Test
    public void handleDecryptionIssueWithPtnSourceFromUserInput() throws ExecutorException
    {
        UserProfile user = new UserProfile();
        MockProtocolBufferRequestParser parser = new MockProtocolBufferRequestParser();
        ProtoUserProfile.Builder userBuilder = ProtoUserProfile.newBuilder();
        userBuilder.setPtnSource(UserProfile.PTN_SOURCE_FROM_USER_INPUT);
        userBuilder.setUserId("5900112");
        userBuilder.setMin("40859212365");
        
        parser.handleDecryptionIssue(user, userBuilder.build());
        
        Assert.assertEquals(user.getPtnSource(), UserProfile.PTN_SOURCE_FROM_USER_INPUT);
        Assert.assertEquals(user.getMin(), "40859212365");
        Assert.assertEquals(user.getUserId(), "5900112");
    }
    
    @Test
    public void handleDecryptionIssueWithPtnSourceFromTelenavCserverWithInvalidPtnAndUserID() throws ExecutorException
    {
        UserProfile user = new UserProfile();
        MockProtocolBufferRequestParser parser = new MockProtocolBufferRequestParser();
        ProtoUserProfile.Builder userBuilder = ProtoUserProfile.newBuilder();
        userBuilder.setPtnSource(UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER);
        
        //with empty ptn and userid
        userBuilder.setUserId("");
        userBuilder.setMin("");
        
        parser.handleDecryptionIssue(user, userBuilder.build());
        
        Assert.assertEquals(user.getPtnSource(), UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER);
        Assert.assertEquals(user.getMin(), null);
        Assert.assertEquals(user.getUserId(), null);
        
        //with null ptn and userid
        userBuilder = ProtoUserProfile.newBuilder();
        parser.handleDecryptionIssue(user, userBuilder.build());
        Assert.assertEquals(user.getPtnSource(), UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER);
        Assert.assertEquals(user.getMin(), null);
        Assert.assertEquals(user.getUserId(), null);
        
        
        
    }
    
    @Test(expected=ExecutorException.class)
    public void handleDecryptionIssueWithPtnSourceFromTelenavCserverWithException() throws ExecutorException
    {
        UserProfile user = new UserProfile();
        MockProtocolBufferRequestParser parser = new MockProtocolBufferRequestParser();
        ProtoUserProfile.Builder userBuilder = ProtoUserProfile.newBuilder();
        userBuilder.setPtnSource(UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER);
        
      //with invalid (unencrypted userid and ptn)
        userBuilder.setUserId("5900112");
        userBuilder.setMin("40859212365");
        parser.handleDecryptionIssue(user, userBuilder.build());
    }
//    
//    
//
    class MockProtocolBufferRequestParser extends ProtocolBufferRequestParser
    {
        public void handleDecryptionIssue(UserProfile userProfile,ProtoUserProfile pUserProfile) throws ExecutorException
        {
            super.handleDecryptionIssue(userProfile, pUserProfile);
        }
    }

	@Test
	public void testConvert2GpsData() {
		ProtoGpsFix.Builder builder = ProtoGpsFix.newBuilder();
		builder.setType(1);
		builder.setTimeTag(System.currentTimeMillis());
		builder.setSpeed(2);
		builder.setLontitude(3);
		builder.setLatitude(4);
		builder.setHeading(5);
		builder.setErrorSize(6);

		GpsData result = ProtocolBufferRequestParser.convert2GpsData(builder.build());
		assertTrue(result.isValid);

		builder.setTimeTag(-1);
		GpsData result2 = ProtocolBufferRequestParser.convert2GpsData(builder.build());
		assertFalse(result2.isValid);
	}

}
