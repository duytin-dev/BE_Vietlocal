package com.vietlocal.app.exception;

public class ResourceNotFoundException extends BusinessException {

	public ResourceNotFoundException(String message) {
		super(ErrorCode.RESOURCE_NOT_FOUND, message);
	}
}
