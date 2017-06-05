package com.example.pdedio.sendsnap.helpers;

import com.example.pdedio.sendsnap.models.User;
import com.example.pdedio.sendsnap.models.User_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by pawel on 09.03.2017.
 */
@EBean(scope = EBean.Scope.Singleton)
public class SessionManager {

    @Bean
    protected SharedPreferenceManager sharedPreferenceManager;

    private String userId;

    public void logInUser(User user) {
        this.sharedPreferenceManager.setUserId(user.id);
        this.sharedPreferenceManager.setUserToken(user.authToken);
    }

    public User getLoggedUser() {
        User user = SQLite
                .select(User_Table.ALL_COLUMN_PROPERTIES)
                .from(User.class)
                .where(User_Table.id.is(this.getUserId()))
                .querySingle();

        if(user != null) {
            user.authToken = this.sharedPreferenceManager.getUserToken();
        }

        return user;
    }

    private String getUserId() {
        if(this.userId == null || this.userId.isEmpty()) {
            this.userId = this.sharedPreferenceManager.getUserId();
        }

        return this.userId;
    }

    public void logOutUser() {
        User user = this.getLoggedUser();

        if(user != null) {
            user.delete();
            this.sharedPreferenceManager.setUserToken("");
            this.sharedPreferenceManager.setUserId("");
        }
    }
}
