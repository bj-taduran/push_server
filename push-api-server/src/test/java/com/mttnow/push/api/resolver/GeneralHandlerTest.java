package com.mttnow.push.api.resolver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import com.mttnow.push.api.exceptions.PushApplicationException;
import com.mttnow.push.api.exceptions.PushChannelException;
import com.mttnow.push.api.models.ErrorMessage;

public class GeneralHandlerTest {

  private GeneralHandler handler;
  
  @Before
  public void setUp(){
    handler = new GeneralHandler();
  }
  
  @Test
  public void shouldHandleException(){
    ModelAndView modelAndView = handler.handleException(new Exception("exception"));
    test("exception", modelAndView);
  }
  
  @Test
  public void shouldHandlePushApplicationException(){
    ModelAndView modelAndView = handler.handlePushApplicationException(new PushApplicationException("push application exception"));
    test("push application exception", modelAndView);
  }
  
  @Test
  public void shouldHandlePushChannelException(){
    ModelAndView modelAndView = handler.handlePushChannelException(new PushChannelException("push application exception"));
    test("push application exception", modelAndView);
  }
  
  @Test
  public void shouldHandlePushException(){
    IllegalStateException illegalStateException =new IllegalStateException("illegal state exception");
    ModelAndView modelAndView = handler.handlePushException(illegalStateException);
    test("illegal state exception", modelAndView);
    
    illegalStateException = mock(IllegalStateException.class);
    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setError("push application exception");
    when(illegalStateException.getCause()).thenReturn(new PushApplicationException("push application exception", errorMessage));
    modelAndView = handler.handlePushException(illegalStateException);
    test("push application exception", modelAndView);
    
    illegalStateException = mock(IllegalStateException.class);
    when(illegalStateException.getCause()).thenReturn(new PushChannelException("push application exception"));
    modelAndView = handler.handlePushException(illegalStateException);
    test("push application exception", modelAndView);
  }
  
  private void test(String message, ModelAndView modelAndView){
    assertNotNull(modelAndView);
    assertEquals(message,modelAndView.getModelMap().get("error"));
    assertNotNull(modelAndView.getView());
  }
  
}
