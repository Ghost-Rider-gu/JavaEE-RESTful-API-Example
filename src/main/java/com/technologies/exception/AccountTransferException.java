package com.technologies.exception;

/**
 * Exception for account transfer application
 */
public class AccountTransferException extends Exception {

    private static final Long serialVersionUID = 342567824657L;

    public AccountTransferException(String msg) {
        super(msg);
    }

    public AccountTransferException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
