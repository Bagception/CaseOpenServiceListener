package de.uniulm.bagception;


import de.philipphock.android.lib.services.ServiceUtil;
import de.uniulm.bagception.caseopenservicelistener.R;
import de.uniulm.bagception.service.CaseOpenBroadcastActor;
import de.uniulm.bagception.service.CaseOpenServiceBroadcastReactor;
import de.uniulm.bagception.service.CaseOpenServiceConstants;
import de.uniulm.bagception.service.CaseOpenServiceRemote;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CaseOpenServiceListener extends Activity implements CaseOpenServiceBroadcastReactor{

	private CaseOpenBroadcastActor caseOpenBroadcastActor;
	private CaseOpenServiceRemote remoteService;
	private boolean serviceBound = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_case_open_service_control);
		
		caseOpenBroadcastActor = new CaseOpenBroadcastActor(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.case_open_service_control, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ServiceUtil.isServiceRunning(this, CaseOpenServiceConstants.SERVICE_NAME)){
			onServiceStarted();
		}else{
			onServiceShutdown();
		}
		caseOpenBroadcastActor.register(this);

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		caseOpenBroadcastActor.unregister(this);
	}
	
	
	private void onServiceShutdown(){ 
		TextView serviceStatusText = (TextView) findViewById(R.id.serviceStatusText);
		serviceStatusText.setText("offline");
		serviceStatusText.setTextColor(Color.RED);
		
		Button startStopService = (Button) findViewById(R.id.startStopService);
		startStopService.setText("Start Service");
		startStopService.setEnabled(true);	
		
		TextView caseStatusText = (TextView) findViewById(R.id.casestatus);
		caseStatusText.setText("unknown");
		caseStatusText.setTextColor(Color.RED);
		
		if (serviceBound){
			serviceBound = false;
			unbindService(serviceConnection);
		}
		

	}
	private void onServiceStarted(){
		TextView serviceStatusText = (TextView) findViewById(R.id.serviceStatusText);
		serviceStatusText.setText("online");
		serviceStatusText.setTextColor(Color.GREEN);
		
		Button startStopService = (Button) findViewById(R.id.startStopService);
		startStopService.setText("Stop Service");
		startStopService.setEnabled(true);
		
		
		if (!serviceBound){
			Log.d("Service","BIND to "+CaseOpenServiceRemote.class.getName());
			
			if (!bindService(new Intent(CaseOpenServiceRemote.class.getName()),
	                serviceConnection, Context.BIND_AUTO_CREATE)){
				Log.d("Service","error binding to service");
			}else{
				serviceBound = true;
			}
		}
	}
		
	
	private void startService(){
		 Intent serviceIntent = new Intent(this, CaseOpenServiceRemote.class);
	        this.startService(serviceIntent);		
	}
	private void stopService(){
		 Intent serviceIntent = new Intent(this, CaseOpenServiceRemote.class);
	        this.stopService(serviceIntent);
	}
	public void onStartStopServiceClicked(View v){
		Button startStopService = (Button) findViewById(R.id.startStopService);
		startStopService.setEnabled(false);
		if (ServiceUtil.isServiceRunning(this, CaseOpenServiceConstants.SERVICE_NAME)){
			Log.d("Service", "stop Service click command");
			stopService();
		}else{
			Log.d("Service", "start Service click command");
			startService();
		}
	}
	
	/* 
	 * CaseOpenServiceBroadcastReactor 
	 */


	@Override
	public void serviceShutdown() {
		onServiceShutdown();
	}

	@Override
	public void serviceStarted() {
		onServiceStarted();
	}

	@Override
	public void caseOpened() {
		TextView serviceStatusText = (TextView) findViewById(R.id.casestatus);
		serviceStatusText.setText("opened");
		serviceStatusText.setTextColor(Color.BLUE);

	}

	@Override
	public void caseClosed() {
		TextView serviceStatusText = (TextView) findViewById(R.id.casestatus);
		serviceStatusText.setText("closed");
		serviceStatusText.setTextColor(Color.GREEN);
		
	}

	
	//communication with service
	private ServiceConnection serviceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d("Service","service disconnected");

			remoteService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			remoteService=CaseOpenServiceRemote.Stub.asInterface(service);
			Log.d("Service","service connected");
			try {
				if (remoteService.isCaseOpened()){
					caseOpened();
				}else{
					caseClosed();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	};
	
}
