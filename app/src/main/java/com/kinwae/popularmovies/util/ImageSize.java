package com.kinwae.popularmovies.util;

/**
 * Created by Kembene on 6/24/2015.
 */
public enum ImageSize {
    SMALLEST("w92"),
    SMALLER("w154"),
    SMALL("w185"),
    DEFAULT("w185"),
    LARGE("w342"),
    LARGER("w500"),
    LARGEST("w780"),
    ORIGINAL("original");

    private String size;

    ImageSize(String size){
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "ImageSize{" +
                "size='" + size + '\'' +
                '}';
    }
}
