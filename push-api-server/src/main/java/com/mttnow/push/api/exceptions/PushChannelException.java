package com.mttnow.push.api.exceptions;

import com.mttnow.push.api.models.ErrorMessage;

public class PushChannelException extends Throwable {

  private static final long serialVersionUID = 5329839431322915197L;
  private ErrorMessage errorMessage;

  public PushChannelException(String msg) {
    super(msg);
    errorMessage = new ErrorMessage();
    errorMessage.setError(msg);
  }
  
  public ErrorMessage getErrorMessage() {
    return errorMessage;
  }
  
}
