package com.kinwae.popularmovies.net;

import android.net.Uri;

import com.kinwae.popularmovies.net.reader.ResponseDecoder;

/**
 * Created by Kembene on 6/24/2015.
 */
public class RequestResponse<T> {
    private Uri uri;
    private ResponseDecoder<T> decoder;

    public RequestResponse(Uri uri, ResponseDecoder<T> decoder) {
        this.uri = uri;
        this.decoder = decoder;
    }

    public Uri getUri() {
        return uri;
    }

    public ResponseDecoder<T> getDecoder() {
        return decoder;
    }
}
