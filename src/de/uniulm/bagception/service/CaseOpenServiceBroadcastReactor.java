package de.uniulm.bagception.service;

import de.philipphock.android.lib.Reactor;

public interface CaseOpenServiceBroadcastReactor extends Reactor{

	public void serviceShutdown();
	public void serviceStarted();
	
	public void caseOpened();
	public void caseClosed();
}
