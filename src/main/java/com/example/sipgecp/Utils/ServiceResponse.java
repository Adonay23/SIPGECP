package com.example.sipgecp.Utils;

public class ServiceResponse {

	public static final String OK_STATUS_CODE = "00";
	public static final String OK_STATUS_MESSAGE = "generico.operacion_exito";
	public static final String ERROR_STATUS_CODE = "99";
	public static final String ERROR_STATUS_MESSAGE = "generico.operacion_error";

	public String code;
	public String message;
	public Object result;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public static String getOkStatusCode() {
		return OK_STATUS_CODE;
	}

	public static String getOkStatusMessage() {
		return OK_STATUS_MESSAGE;
	}

	public static String getErrorStatusCode() {
		return ERROR_STATUS_CODE;
	}

	public static String getErrorStatusMessage() {
		return ERROR_STATUS_MESSAGE;
	}

	public ServiceResponse() {
		super();
	}

	public ServiceResponse(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public ServiceResponse(String code, String message, Object result) {
		super();
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public ServiceResponse(Object result) {
		super();
		this.result = result;
	}
	
	
}
