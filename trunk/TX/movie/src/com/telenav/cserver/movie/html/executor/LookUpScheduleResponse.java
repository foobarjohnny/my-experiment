package com.telenav.cserver.movie.html.executor;

import java.util.List;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.movie.html.datatypes.ScheduleItem;

public class LookUpScheduleResponse extends ExecutorResponse{
	private List<ScheduleItem> scheduleList;

	public List<ScheduleItem> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(List<ScheduleItem> scheduleList) {
		this.scheduleList = scheduleList;
	}



}
