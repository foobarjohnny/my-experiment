package com.telenav.cserver.matcher;

import org.easymock.IArgumentMatcher;

import com.telenav.j2me.datatypes.TxNode;

public class TxNodeMatcher implements IArgumentMatcher {

	private TxNode txNode;

	public TxNodeMatcher(TxNode txNode) {
		this.txNode = txNode;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof TxNode)
			return true;
		
		return false;
	}

	@Override
	public void appendTo(StringBuffer arg0) {

	}

}
