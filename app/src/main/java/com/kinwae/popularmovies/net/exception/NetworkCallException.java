package com.kinwae.popularmovies.net.exception;

/**
 * Created by Kembene on 6/24/2015.
 */
public class NetworkCallException extends RuntimeException {
    public NetworkCallException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NetworkCallException(Throwable throwable) {
        super(throwable);
    }
}
