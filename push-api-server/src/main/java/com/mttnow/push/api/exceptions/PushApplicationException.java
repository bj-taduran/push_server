package com.mttnow.push.api.exceptions;

import com.mttnow.push.api.models.ErrorMessage;

public class PushApplicationException extends Throwable {

    private ErrorMessage errorMessage;

    public PushApplicationException(String s) {
        super(s);
        ErrorMessage msg = new ErrorMessage();
        msg.setError(s);
        this.errorMessage=msg;
    }

    public PushApplicationException(String message, ErrorMessage errorMessage) {
        super(message);
        this.errorMessage = errorMessage;
    }

    public PushApplicationException(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

}
