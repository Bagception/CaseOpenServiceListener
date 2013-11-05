package de.uniulm.bagception.service;

import de.philipphock.android.lib.Reactor;

public interface CaseOpenServiceBroadcastReactor extends Reactor{

	
	public void caseOpened();
	public void caseClosed();
}
