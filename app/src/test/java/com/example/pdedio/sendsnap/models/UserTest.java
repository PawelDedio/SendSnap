package com.example.pdedio.sendsnap.models;

import android.content.Context;

import static junit.framework.Assert.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by pawel on 27.02.2017.
 */

public class UserTest {

    @Mock
    protected Context mockedContext;

    public User createUser() {
        MockitoAnnotations.initMocks(this);

        User user = new User();
        user.name = "correctName";
        user.displayName = "displayName";
        user.email = "email@test.com";
        user.password = "correctPassword";
        user.passwordConfirmation = "correctPassword";
        user.termsAccepted = true;
        user.role = "user";
        user.authToken = "d1f735bb6513499580b45d27c5889942";
        user.tokenExpireTime = new Date(Calendar.getInstance().getTime().getTime() + TimeUnit.DAYS.toMillis(1));
        user.createdAt = Calendar.getInstance().getTime();
        user.updatedAt = Calendar.getInstance().getTime();

        return user;
    }


    //isValid
    @Test
    public void shouldReturnTrueForCorrectData() {
        User user = this.createUser();

        boolean value = user.isValid(this.mockedContext);

        assertTrue(value);
    }

    @Test
    public void shouldReturnFalseForEmptyName() {
        User user = this.createUser();
        user.name = null;

        boolean value = user.isValid(this.mockedContext);

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForTooShortName() {
        User user = this.createUser();
        user.name = "a";

        boolean value = user.isValid(this.mockedContext);

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForTooLongName() {
        User user = this.createUser();
        user.name = "totallytolongusername";

        boolean value = user.isValid(this.mockedContext);

        assertFalse(value);
    }

    @Test
    public void shouldReturnTrueForEmptyDisplayName() {
        User user = this.createUser();
        user.displayName = null;

        boolean value = user.isValid(this.mockedContext);

        assertTrue(value);
    }

    @Test
    public void shouldReturnFalseForTooShortDisplayName() {
        User user = this.createUser();
        user.displayName = "a";

        boolean value = user.isValid(this.mockedContext);

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForTooLongDisplayName() {
        User user = this.createUser();
        user.displayName = "totallytolonguserdisplayname";

        boolean value = user.isValid(this.mockedContext);

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForEmptyEmail() {
        User user = this.createUser();
        user.email = null;

        boolean value = user.isValid(this.mockedContext);

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForWrongEmail() {
        User user = this.createUser();
        user.email = "wrongemail";

        boolean value = user.isValid(this.mockedContext);

        assertFalse(value);
    }

    @Test
    public void shouldReturnTrueForEmptyPasswordWhenObjectWasSaved() {
        User user = this.createUser();
        user.password = null;
        user.createdAt = Calendar.getInstance().getTime();

        boolean value = user.isValid(this.mockedContext);

        assertTrue(value);
    }

    @Test
    public void shouldReturnFalseForEmptyPasswordWhenObjectWasNotSaved() {
        User user = this.createUser();
        user.password = null;
        user.createdAt = null;

        boolean value = user.isValid(this.mockedContext);

        assertFalse(value);
    }

    @Test
    public void shouldReturnTrueForEmptyPasswordConfirmationWhenObjectWasSaved() {
        User user = this.createUser();
        user.passwordConfirmation = null;
        user.createdAt = Calendar.getInstance().getTime();

        boolean value = user.isValid(this.mockedContext);

        assertTrue(value);
    }

    @Test
    public void shouldReturnFalseForEmptyPasswordConfirmationWhenObjectWasNotSaved() {
        User user = this.createUser();
        user.passwordConfirmation = null;
        user.createdAt = null;

        boolean value = user.isValid(this.mockedContext);

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseWhenPasswordAndPasswordConfirmationAreDifferent() {
        User user = this.createUser();
        user.password = "password";
        user.passwordConfirmation = "differentPassword";
        user.createdAt = null;

        boolean value = user.isValid(this.mockedContext);

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForNotAcceptedTermsAndConditions() {
        User user = this.createUser();
        user.termsAccepted = false;

        boolean value = user.isValid(this.mockedContext);

        assertFalse(value);
    }
}
