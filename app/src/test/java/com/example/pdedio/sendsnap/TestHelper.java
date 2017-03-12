package com.example.pdedio.sendsnap;

import com.example.pdedio.sendsnap.models.User;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by pawel on 12.03.2017.
 */

public class TestHelper {

    public static Response<User> prepareErrorResponse(int code) {
        Response<User> response = Response.error(code, ResponseBody.create(MediaType.parse("json"), "{}"));

        return response;
    }
}
