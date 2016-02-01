package com.yahoo.reportr.data.service;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit.Converter;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

/**
 * This custom type converter is used to convert String into text/plain for making requests. It is
 * because for Multipart requests, the String body is converted to JSON through the default converter
 * and appends extra quotes which results in errors on the backend. This converter avoids that
 * scenario. The solution has been extracted from a very lively discussion here
 * https://github.com/square/retrofit/issues/1210
 *
 */
public class GsonStringConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
        if (String.class.equals(type)) {
            return new Converter<String, RequestBody>() {
                @Override
                public RequestBody convert(String value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value);
                }
            };
        }
        return null;
    }
}
