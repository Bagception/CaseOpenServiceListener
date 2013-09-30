package de.uniulm.bagception.service;

public interface CaseOpenServiceConstants {
	public static final String BROADCAST_COMMAND_INTENT = "de.uniulm.bagception.broadcast.CMD";
	
	public static final String BROADCAST_COMMAND_SHUTDOWN = "SHUTDOWN";
	public static final String BROADCAST_COMMAND_START = "START";
	
	public static final String BROADCAST_CASE_STATE = "de.uniulm.bagception.broadcast.casestate";
	
	public static final int CASE_OPEN_STATE_CHANGED_TO_OPEN=1;
	public static final int CASE_OPEN_STATE_CHANGED_TO_CLOSED=2;
	
	public static final String SERVICE_NAME = "de.uniulm.bagception.service.CaseOpenService";
}
