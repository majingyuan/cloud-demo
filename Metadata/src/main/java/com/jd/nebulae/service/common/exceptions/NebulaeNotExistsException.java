package com.jd.nebulae.service.common.exceptions;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
public class NebulaeNotExistsException extends RuntimeException {

    public NebulaeNotExistsException(String message) {
        super(String.format("%s not exists", message));
    }
}
