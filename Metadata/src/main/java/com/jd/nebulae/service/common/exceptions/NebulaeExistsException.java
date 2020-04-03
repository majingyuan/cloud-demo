package com.jd.nebulae.service.common.exceptions;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
public class NebulaeExistsException extends RuntimeException {

    public NebulaeExistsException(String message) {
        super(String.format("%s already exists", message));
    }
}
