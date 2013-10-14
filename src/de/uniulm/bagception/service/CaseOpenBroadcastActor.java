package de.uniulm.bagception.service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import de.philipphock.android.lib.BroadcastActor;

public class CaseOpenBroadcastActor extends BroadcastActor<CaseOpenServiceBroadcastReactor> {

	public CaseOpenBroadcastActor(CaseOpenServiceBroadcastReactor reactor) {
		super(reactor);
	}

	@Override
	public void register(Context a) {
		IntentFilter filter = new IntentFilter(CaseOpenServiceConstants.BROADCAST_COMMAND_INTENT);
		filter.addAction(CaseOpenServiceConstants.BROADCAST_CASE_STATE);
	    a.registerReceiver(this, filter);		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (CaseOpenServiceConstants.BROADCAST_COMMAND_INTENT.equals(intent.getAction())){
			if (CaseOpenServiceConstants.BROADCAST_COMMAND_SHUTDOWN.equals(intent.getStringExtra(CaseOpenServiceConstants.BROADCAST_COMMAND_INTENT))){
				reactor.serviceShutdown();
			}else if(CaseOpenServiceConstants.BROADCAST_COMMAND_START.equals(intent.getStringExtra(CaseOpenServiceConstants.BROADCAST_COMMAND_INTENT))){
				reactor.serviceStarted();
			}	
		}else if (CaseOpenServiceConstants.BROADCAST_CASE_STATE.equals(intent.getAction())){
			if (CaseOpenServiceConstants.CASE_OPEN_STATE_CHANGED_TO_CLOSED == intent.getIntExtra(CaseOpenServiceConstants.BROADCAST_CASE_STATE,-1)){
				reactor.caseClosed();
			}else if (CaseOpenServiceConstants.CASE_OPEN_STATE_CHANGED_TO_OPEN == intent.getIntExtra(CaseOpenServiceConstants.BROADCAST_CASE_STATE,-1)){
				reactor.caseOpened();
			}
		}
		
	}

}
