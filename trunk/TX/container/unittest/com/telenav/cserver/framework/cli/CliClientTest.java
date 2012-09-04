package com.telenav.cserver.framework.cli;

import junit.framework.TestCase;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorRequest;

public class CliClientTest extends TestCase
{   
    public void testCliClient()
    {
        UserProfile userProfile = new UserProfile();
        userProfile.setCarrier("ATT");
        
        ExecutorRequest[] requests = new ExecutorRequest[2];
        requests[0] = new ExecutorRequest();
        requests[0].setUserProfile(userProfile);
        requests[0].setExecutorType("Get_SPT");
        
        requests[1] = new ExecutorRequest();
        requests[1].setUserProfile(userProfile);
        requests[1].setExecutorType("Dynamical_Route");
        
        CliThreadLocalUtil.setCliThreadLocal(requests);
        
        CliThreadLocalUtil.setSingleExecutorType("Get_SPT");
        
        CliTransaction cli = CliTransactionFactory.getInstance(CliConstants.TYPE_URL);
        assertEquals(false, cli instanceof CliFakeTransaction);
        
        CliThreadLocalUtil.setSingleExecutorType(null);
        cli = CliTransactionFactory.getInstance(CliConstants.TYPE_URL);
        assertEquals(false, cli instanceof CliFakeTransaction);

    }
    
    
    public void testCliThreadLocalUtil()
    {
        class TestCliThread extends Thread
        {
            String executorType = null;
            long loopTime = 0;
            TestCliThread(String executorType, long loopTime)
            {
                this.executorType = executorType;
                this.loopTime = loopTime;
            }
            public void run()
            {
                CliThreadLocalUtil.setSingleExecutorType(executorType);
                
                //just for cosuming time
                //for(int i=0; i<loopTime; i++)
                {
                    try
                    {
                        System.out.println("start:"+loopTime);
                        Thread.sleep(loopTime);
                        System.out.println("end");
                        //Math.atan2(new java.util.Random().nextDouble() , new java.util.Random().nextDouble());
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                
                System.out.println(CliThreadLocalUtil.getExecutorType());
                
                assertEquals(executorType, CliThreadLocalUtil.getExecutorType());
                
            }
        }
        
        TestCliThread testClient1 = new TestCliThread("Map1",1);
        TestCliThread testClient2 = new TestCliThread("Map2",40);
        TestCliThread testClient3 = new TestCliThread("Map3",20);
        testClient1.start();
        testClient2.start();
        testClient3.start();
        
    }
}
