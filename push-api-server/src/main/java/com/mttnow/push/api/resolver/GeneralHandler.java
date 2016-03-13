package com.mttnow.push.api.resolver;

import com.mttnow.push.api.exceptions.PushApplicationException;
import com.mttnow.push.api.exceptions.PushChannelException;
import com.mttnow.push.api.models.JsonError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GeneralHandler {
private static Logger log = LoggerFactory.getLogger(GeneralHandler.class);
    @ExceptionHandler(Throwable.class)
    public ModelAndView handleException (Throwable ex) {
        log.info(ex.getMessage(),ex);
        return new JsonError(ex.getMessage()).asModelAndView();
    }

    @ExceptionHandler(PushApplicationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Problem with your application details.")
    public ModelAndView handlePushApplicationException(PushApplicationException ex){
        log.info(ex.getMessage(),ex);
        return new JsonError(ex.getMessage()).asModelAndView();
    }
    
    @ExceptionHandler(PushChannelException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Problem with your channels.")
    public ModelAndView handlePushChannelException(PushChannelException ex){
        log.info(ex.getMessage(),ex);
        return new JsonError(ex.getMessage()).asModelAndView();
    }
    
    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    public ModelAndView handlePushException(IllegalStateException ex) {
        log.warn(ex.getMessage());
        if (ex.getCause() instanceof PushApplicationException) {
            return ((PushApplicationException) ex.getCause()).getErrorMessage().asModelAndView();
        }
        if (ex.getCause() instanceof PushChannelException) {
          return ((PushChannelException) ex.getCause()).getErrorMessage().asModelAndView();
      }
        return new JsonError(ex.getMessage()).asModelAndView();
    }
}
