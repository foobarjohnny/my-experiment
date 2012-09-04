package com.telenav.cserver.poi.executor;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.backend.datatypes.contents.EditablePoi;
import com.telenav.cserver.framework.executor.ExecutorException;

import junit.framework.TestCase;

public class TestEditPOIExecutor_WS extends TestCase {
	private EditPOIReponse resp = new EditPOIReponse();

	public void testDoExecute() throws ExecutorException {
		EditPOIExecutor_WS excutor = new EditPOIExecutor_WS();
		excutor.doExecute(getEditPOIRequest(), resp, TestUtil
				.getExecutorContext());
	}

	private EditPOIRequest getEditPOIRequest() {
		EditPOIRequest request = new EditPOIRequest();
		request.poiToEdit = new EditablePoi();

		request.poiToEdit.setPoiId(3000623166L);

		request.poiToEdit.setPriceRange(0);
		request.poiToEdit.setBrandName("Citibank");
		request.poiToEdit.setPhone("8006273999");
		request.poiToEdit.setRating(3);
		request.poiToEdit.setReview("");
		request.setCategoryId("374");

		request.setUserProfile(TestUtil.getUserProfile());
		System.out.println(request.toString());

		return request;
	}
}
