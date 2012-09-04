package com.telenav.browser.movie.executor;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.backend.addresssharing.AddressSharingResponse;
import com.telenav.cserver.backend.addresssharing.AddressSharingServiceProxy;
import com.telenav.cserver.backend.addresssharing.LocationSharingRequest;
import com.telenav.cserver.backend.datatypes.addresssharing.ContactInfo;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;

public class ShareMovieExecutor extends AbstractExecutor{
    
	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException {

        // Get the request and response
    	ShareMovieRequest movieRequest = (ShareMovieRequest)req;
    	ShareMovieResponse movieResponse = (ShareMovieResponse) resp;
    	TnContext tnContext = context.getTnContext();
    	
        try{

	        String[] nos = movieRequest.getRecipients();
	        int sendToLength = nos.length;
	        List<ContactInfo> contactList = new ArrayList<ContactInfo>();
	        for(int i=0;i<sendToLength;i++)
	        {
	            ContactInfo contact = new ContactInfo();
	            contact.setName("");
	            contact.setPtn(nos[i]);
	            
	            contactList.add(contact);
	        }
	        
        
	        LocationSharingRequest request = new LocationSharingRequest();
	    	request.setUserId(movieRequest.getUserId());
	    	request.setPtn(movieRequest.getPtn());
	    	request.setContextString(tnContext.toContextString());
	    	request.setAddress(movieRequest.getAddress());
	    	request.setContactList(contactList);
	    	request.setMovieName(movieRequest.getMovieName());
	    	request.setBrandName(movieRequest.getTheaterName());
            Stop address = request.getAddress();
//          To fix http://jira.telenav.com:8080/browse/ATTBBPUB-1015 per PM. And we think this is a common bug for all TN6x devices, so fix it for them
            address.label = movieRequest.getTheaterName();
            request.setAddress(address);	    	
	        
	        AddressSharingResponse response = AddressSharingServiceProxy.getInstance().shareMovie(request, tnContext);
	        String statusCode = response.getStatusCode();
	        
        	if ("OK".equalsIgnoreCase(statusCode)){
                movieResponse.setStatus(ExecutorResponse.STATUS_OK);
                return;
        	}
            
        }catch(Exception ex){
            movieResponse.setErrorMessage("SMS.notAvailable");
            movieResponse.setStatus(ExecutorResponse.STATUS_FAIL);
            return;
        }
        movieResponse.setStatus(ExecutorResponse.STATUS_FAIL);
   }
}
