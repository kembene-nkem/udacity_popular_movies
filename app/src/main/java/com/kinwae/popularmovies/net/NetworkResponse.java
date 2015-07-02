package com.kinwae.popularmovies.net;

import android.net.Uri;


import com.kinwae.popularmovies.net.reader.ResponseDecoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kembene on 6/24/2015.
 */
public class NetworkResponse {
    private Uri requestedUri;
    private String responseBody;
    private int statusCode;

    private Exception callException;

    public NetworkResponse(Exception callException) {
        this.callException = callException;
    }

    public NetworkResponse(Uri requestedUri, int statusCode) {
        this.requestedUri = requestedUri;
        this.statusCode = statusCode;
    }

    public NetworkResponse(Uri requestedUri, String responseBody, int statusCode) {
        this.requestedUri = requestedUri;
        this.responseBody = responseBody;
        this.statusCode = statusCode;
    }

    public Exception getCallException() {
        return callException;
    }

    public NetworkResponse setCallException(Exception callException) {
        this.callException = callException;
        return this;
    }

    public Uri getRequestedUri() {
        return requestedUri;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public boolean isSuccessful() {
        return (statusCode >= 200 && statusCode < 300) && callException == null;
    }
}
