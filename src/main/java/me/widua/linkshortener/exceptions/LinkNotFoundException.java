package me.widua.linkshortener.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LinkNotFoundException extends RuntimeException {
    public LinkNotFoundException() {
    }

    public LinkNotFoundException(String message) {
        super(message);
    }
}
