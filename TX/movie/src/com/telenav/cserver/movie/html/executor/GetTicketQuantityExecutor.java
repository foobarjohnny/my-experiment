/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.movie.html.datatypes.TicketItem;


/**
 * GetTicketQuantityExecutor.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Dec 21, 2010
 *
 */
public class GetTicketQuantityExecutor extends AbstractExecutor
{
    public static boolean isDebug = false;
    
    @Override
    public void doExecute(ExecutorRequest req, ExecutorResponse res, ExecutorContext context) throws ExecutorException
    {
        if(isDebug){
            GetTicketQuantityRequest request = (GetTicketQuantityRequest)req;
            GetTicketQuantityResponse response = (GetTicketQuantityResponse)res;
            response = getDummyTickets(request,response);

            response.setStatus(ExecutorResponse.STATUS_OK);
            
        }else{
            GetTicketQuantityRequest request = (GetTicketQuantityRequest)req;
            GetTicketQuantityResponse response = (GetTicketQuantityResponse)res;
            MovieServiceProxy.getTicketPrices(request, response); 
            response.setStatus(ExecutorResponse.STATUS_OK);
        }

    }
    
    public GetTicketQuantityResponse getDummyTickets(GetTicketQuantityRequest request ,GetTicketQuantityResponse response){
        List<TicketItem> ticketList = new ArrayList<TicketItem>();
        
        TicketItem ticketOne = new TicketItem();
        
        ticketOne.setTicketId("111");
        ticketOne.setType("Adult");
        ticketOne.setPrice(12.5);
        ticketOne.setCurrency("");
        ticketOne.setQuantity(0);
        ticketList.add(ticketOne);
        
        ticketOne = new TicketItem();
        ticketOne.setTicketId("222");
        ticketOne.setType("Child");
        ticketOne.setPrice(8.5);
        ticketOne.setCurrency("");
        ticketOne.setQuantity(0);
        ticketList.add(ticketOne);
        
        ticketOne = new TicketItem();
        ticketOne.setTicketId("333");
        ticketOne.setType("General");
        ticketOne.setPrice(9);
        ticketOne.setCurrency("");
        ticketOne.setQuantity(0);
        ticketList.add(ticketOne);
        

        response.setTicketList(ticketList); 
        
        return response;
    }
}
