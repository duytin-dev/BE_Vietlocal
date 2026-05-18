package com.vietlocal.app.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vietlocal.app.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	private boolean success;
	private String message;
	private ErrorCode errorCode;
	private T data;

	public static <T> ApiResponse<T> ok(T data) {
		return ApiResponse.<T>builder().success(true).data(data).build();
	}

	public static <T> ApiResponse<T> ok(String message, T data) {
		return ApiResponse.<T>builder().success(true).message(message).data(data).build();
	}

	public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message) {
		return ApiResponse.<T>builder().success(false).errorCode(errorCode).message(message).build();
	}
}
