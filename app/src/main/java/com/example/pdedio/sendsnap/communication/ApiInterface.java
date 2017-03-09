package com.example.pdedio.sendsnap.communication;

import com.example.pdedio.sendsnap.models.EmptyModel;
import com.example.pdedio.sendsnap.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by pawel on 09.03.2017.
 */

import static com.example.pdedio.sendsnap.communication.ApiInterface.Headers.*;
import static com.example.pdedio.sendsnap.communication.ApiInterface.QueryParams.*;

public interface ApiInterface {

    class Headers {
        public static final String AUTHORIZATION = "Authorization";
    }

    class QueryParams {
        public static final String PAGE = "page";
        public static final String PAGE_SIZE = "page_size";
        public static final String SEARCH_FIELD = "search_field";
        public static final String SEARCH_VALUE = "search_value";
        public static final String SORT_BY = "sort_by";
        public static final String SORT_ORDER = "sort_order";
    }

    //Session
    @POST("/session/create")
    Call<User> postSessionCreate(@Body User user);

    @DELETE("/session/destroy")
    Call<EmptyModel> deleteSessionDestroy(@Header(AUTHORIZATION) String authorizationHeader);


    //User
    @POST("/users")
    Call<User> postUsers(@Body User user);

    @GET("/users/{id}")
    Call<User> getUsersId(@Header(AUTHORIZATION) String authorizationHeader, @Path("id") String id);

    @GET("/users")
    Call<List<User>> getUsers(@Header(AUTHORIZATION) String authorizationHeader, @Query(SEARCH_FIELD) String searchField,
                              @Query(SEARCH_VALUE) String searchValue);

    @PUT("/users/{id}")
    Call<User> putUsersId(@Header(AUTHORIZATION) String authorizationHeader, @Path("id") String id, @Body User user);
}
