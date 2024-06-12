package com.nightcrowler.spring.banking.controler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
@RequestMapping(value = "${server.error.path:${error.path:/error}}",method = {GET, POST, PUT, DELETE})
public class ErrorController extends AbstractErrorController {

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping
    public ResponseEntity<ErrorResponse> errorResponseResponseEntity(HttpServletRequest request) {
        return getErrorResponseResponseEntity(request);
    }


    private ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.of(getALLErrorAttribute()));
        var message = errorAttributes != null ? errorAttributes.getOrDefault("message", "").toString() : "";
        var httpStatus = getStatus(request);
        var errorResponse = getErrorResponse(message, httpStatus);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private static ErrorResponse getErrorResponse(String message, HttpStatus httpStatus) {
        return ErrorResponse.builder()
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(message)
                .build();
    }

    @Getter
    @Builder
    @EqualsAndHashCode
    public static final class ErrorResponse {
        private final int status;
        private final String timestamp;
        private final String error;
        private final String message;

    }

    private ErrorAttributeOptions.Include[] getALLErrorAttribute() {
        return ErrorAttributeOptions.Include.values();
    }

}
