package com.kinwae.popularmovies.net;

import java.io.IOException;

/**
 * Created by Kembene on 6/24/2015.
 */
public interface NetworkResponseCallback {
    void onResponseReceived(NetworkResponse response);
    void onFailure(Exception exception);
}
