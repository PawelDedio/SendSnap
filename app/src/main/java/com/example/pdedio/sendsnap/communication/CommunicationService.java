package com.example.pdedio.sendsnap.communication;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.helpers.SessionManager;
import com.example.pdedio.sendsnap.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pawel on 09.03.2017.
 */
@EBean(scope = EBean.Scope.Singleton)
public class CommunicationService {

    @Bean
    protected SessionManager sessionManager;

    @StringRes(R.string.api_url)
    protected String apiUrl;

    @StringRes(R.string.auth_header_schema)
    protected String authHeaderSchema;

    private ApiInterface apiInterface;



    @AfterInject
    protected void afterInjectCommunicationService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.apiUrl)
                .client(this.prepareOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.apiInterface = retrofit.create(ApiInterface.class);
    }

    private OkHttpClient prepareOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return client;
    }


    public Call<User> signInUser(User user) {
        return this.apiInterface.postSessionCreate(user);
    }

    public Call<User> registerUser(User user) {
        return this.apiInterface.postUsers(user);
    }

    public Call<User> updateUser(User user) {

        String headerToken = null;
        if(this.sessionManager.getLoggedUser() != null) {
            headerToken = this.getTokenHeader(this.sessionManager.getLoggedUser().authToken);
        }

        return this.apiInterface.putUsersId(headerToken, user.id, user);
    }


    //Private methods
    private String getTokenHeader(String token) {
        return this.authHeaderSchema + token;
    }
}
