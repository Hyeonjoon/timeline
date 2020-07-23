package me.timeline.exception;

public class DatabaseRelatedException extends RuntimeException{
	
	private static final long serialVersionUID = -5941877479635638910L;
	
	private int status = 500;
	private String exception = "DatabaseRelatedException";
	
	public DatabaseRelatedException() {
		super("Fail to handle request because an exception related to database occurs. The request body may be manipulated.");
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getException() {
		return exception;
	}
}
