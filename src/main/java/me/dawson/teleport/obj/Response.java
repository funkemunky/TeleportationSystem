package me.dawson.teleport.obj;

import java.util.Optional;

public class Response {
    private final String response;
    private final Exception exception;

    public Response(String response) {
        this.response = response;
        this.exception = null;
    }

    public Response(String response, Exception exception) {
        this.response = response;
        this.exception = exception;
    }

    public boolean isOkay() {
        return exception == null;
    }

    public Optional<Exception> getException() {
        return exception == null ? Optional.empty() : Optional.of(exception);
    }


    public String getResponse() {
        return response;
    }
}
