package com.telenav.cserver.service.chunkhandler;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

public class AbstractChunkProcssorTestingImp extends AbstractChunkProcssor {

	@Override
	public void doProcess(ExecutorRequest request, ExecutorResponse response, ExecutorContext context) throws Exception {
		// unit test usage, do nothing
	}

	@Override
	public void process(ExecutorRequest request, ExecutorResponse response, ExecutorContext context) throws Exception {
		// unit test usage, do nothing
	}

}
