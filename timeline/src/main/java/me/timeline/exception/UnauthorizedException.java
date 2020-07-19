package me.timeline.exception;

public class UnauthorizedException extends RuntimeException{
	
	private static final long serialVersionUID = -112063417703749542L;
	
	private int status = 401;
	private String exception = "UnauthorizedException";
	
	public UnauthorizedException() {
		super("You do not have permission. Please sign in.");
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getException() {
		return exception;
	}
}
