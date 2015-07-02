package com.kinwae.popularmovies.net.reader;

import com.kinwae.popularmovies.net.NetworkResponse;

/**
 * Created by Kembene on 6/24/2015.
 */
public class BaseResponseDecoder<T> implements ResponseDecoder<T> {

    @Override
    public T decodeResponse(NetworkResponse response) {
        return null;
    }
}
