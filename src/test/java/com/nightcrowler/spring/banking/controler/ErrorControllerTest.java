package com.nightcrowler.spring.banking.controler;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



@DisplayName("Test Generic Error Controller")
class ErrorControllerTest {

    @Test
    void testCheckCustomErrorController() {

        ErrorAttributes errorAttributes = new DefaultErrorAttributes();
        ErrorController errorController = new ErrorController(errorAttributes);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getAttribute("jakarta.servlet.error.status_code")).thenReturn(403);
        when(httpServletRequest.getAttribute("jakarta.servlet.error.exception")).thenReturn(new RuntimeException("No such method"));
        ResponseEntity<ErrorController.ErrorResponse> response = errorController.errorResponseResponseEntity(httpServletRequest);
        assertEquals(403, response.getStatusCode().value(), "Status code should be 403");
        assertNotNull(response.getBody().getMessage(), "Message should not be null");
        assertNotNull(response.getBody().getTimestamp(), "Timestamp should not be null");
        assertNotEquals(0, response.getBody().getStatus(), "Status should not be 0");
        assertEquals("Forbidden", response.getBody().getError(), "Error should be Forbidden");
    }

    @Test
    void testHttpMediaTypeNotSupportedException() {

        ErrorAttributes errorAttributes = new DefaultErrorAttributes();
        ErrorController errorController = new ErrorController(errorAttributes);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getAttribute("jakarta.servlet.error.status_code")).thenReturn(415);
        when(httpServletRequest.getAttribute("jakarta.servlet.error.exception")).thenReturn(new HttpMediaTypeNotSupportedException("media type is not supported"));
        ResponseEntity<ErrorController.ErrorResponse> response = errorController.errorResponseResponseEntity(httpServletRequest);
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode(), "Status code should be 415");
        assertEquals(415, response.getBody().getStatus(), "Status should be 415");
        assertEquals("media type is not supported", response.getBody().getMessage(), "Message should be media type is not supported");
    }


}
