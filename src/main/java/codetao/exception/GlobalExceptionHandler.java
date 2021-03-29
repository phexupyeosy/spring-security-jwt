package codetao.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String,Object>> baseExceptionHandler(HttpServletRequest request, Exception e){
        Map<String, Object> response = new HashMap<>();
        HttpStatus status;
        if(e instanceof NotFoundException){
            status = HttpStatus.NOT_FOUND;
        }else{
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        response.put("status", status.value());
        response.put("error", status.name());
        response.put("message", e.getMessage());
        response.put("url", request.getRequestURI());
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity(response, status);
    }
}
