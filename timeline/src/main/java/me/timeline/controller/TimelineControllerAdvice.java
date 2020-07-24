package me.timeline.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import me.timeline.dto.ExceptionDTO;
import me.timeline.exception.DatabaseRelatedException;
import me.timeline.exception.UnauthorizedException;

@ControllerAdvice
/* This is a global exception handler. */
public class TimelineControllerAdvice {
	
	/* Handle the UnauthorizedException. */
	@ResponseBody
	@ExceptionHandler(UnauthorizedException.class)
	public ExceptionDTO UnauthorizedExceptionHandler(UnauthorizedException e) {
		ExceptionDTO exceptionDTO = new ExceptionDTO();
		exceptionDTO.setStatus(e.getStatus());
		exceptionDTO.setException(e.getException());
		exceptionDTO.setMessage(e.getMessage());
		return exceptionDTO;
	}
	
	/* Handle the DatabaseRelatedException. */
	@ResponseBody
	@ExceptionHandler(DatabaseRelatedException.class)
	public ExceptionDTO DatabaseRelatedExceptionHandler(DatabaseRelatedException e) {
		ExceptionDTO exceptionDTO = new ExceptionDTO();
		exceptionDTO.setStatus(e.getStatus());
		exceptionDTO.setException(e.getException());
		exceptionDTO.setMessage(e.getMessage());
		return exceptionDTO;
	}
}
