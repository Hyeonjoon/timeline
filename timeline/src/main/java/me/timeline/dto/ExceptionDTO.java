package me.timeline.dto;

public class ExceptionDTO {
	private int status;
	private String exception;
	private String message;
	
	public ExceptionDTO(int status, String exception, String message) {
		this.status = status;
		this.exception = exception;
		this.message = message;
	}
	
	public ExceptionDTO() {
		
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getException() {
		return exception;
	}
	
	public void setException(String exception) {
		this.exception = exception;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
