package com.example.sipgecp.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ServiceFactory {
    private ServiceFactory() {
        super();
    }
    public static ResponseEntity<ServiceResponse> createResponse(ServiceResponse response, HttpStatus status) {
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<ServiceResponse> createResponse(String code, String message, HttpStatus status) {

        ServiceResponse response = new ServiceResponse(code, message);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<ServiceResponse> createResponse(String code, String message, Object result,
                                                                 HttpStatus status) {
        ServiceResponse response = new ServiceResponse(code, message, result);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<ServiceResponse> createOkResponse(String code, String message, Object result) {
        ServiceResponse response = new ServiceResponse(code, message, result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<ServiceResponse> createResponse(Object result) {
        try {
            ServiceResponse response = new ServiceResponse(ServiceResponse.OK_STATUS_CODE,
                    ServiceResponse.OK_STATUS_MESSAGE, result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ServiceResponse(e), HttpStatus.BAD_REQUEST);
        }

    }

}
