package com.example.pdedio.sendsnap.helpers;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by pawel on 25.02.2017.
 */

public class ValidationHelperTest {


    private ValidationHelper prepareValidationHelper() {
        ValidationHelper helper = new ValidationHelper();

        return helper;
    }


    //isNotEmpty()
    @Test
    public void shouldReturnTrueForNotEmptyValue() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.isNotEmpty("value");

        assertTrue(value);
    }

    @Test
    public void shouldReturnFalseForEmptyString() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.isNotEmpty("");

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForNull() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.isNotEmpty(null);

        assertFalse(value);
    }

    //haveCorrectLength()
    @Test
    public void shouldReturnTrueForValueBetweenMaxAndMin() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.haveCorrectLength("12345678", 1, 9);

        assertTrue(value);
    }

    @Test
    public void shouldReturnTrueForTheSameLengthAsMin() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.haveCorrectLength("1", 1, 9);

        assertTrue(value);
    }

    @Test
    public void shouldReturnTrueForTheSameLengthAsMax() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.haveCorrectLength("123", 1, 3);

        assertTrue(value);
    }

    @Test
    public void shouldReturnFalseForShorterValue() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.haveCorrectLength("2", 3, 9);

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForLongerValue() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.haveCorrectLength("12345", 1, 4);

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForNullString() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.haveCorrectLength(null, 1, 4);

        assertFalse(value);
    }


    //areValuesTheSame()
    @Test
    public void shouldReturnTrueForTheSameValues() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.areValuesTheSame("value", "value");

        assertTrue(value);
    }

    @Test
    public void shouldReturnFalseForDifferentValues() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.areValuesTheSame("value1", "value2");

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseWhenOneValueHasSpace() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.areValuesTheSame("value1 ", "value1");

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseWhenFirstValueIsNull() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.areValuesTheSame(null, "value");

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseWhenSecondValueIsNull() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.areValuesTheSame("value", null);

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseWhenBothValuesAreNull() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.areValuesTheSame(null, null);

        assertFalse(value);
    }


    //isValidEmail()
    @Test
    public void shouldReturnTrueForCorrectEmail() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.isValidEmail("test@test.com");

        assertTrue(value);
    }

    @Test
    public void shouldReturnFalseForEmailWithoutName() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.isValidEmail("@test.com");

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForEmailWithTwoAt() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.isValidEmail("test@@test.com");

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForEmailWithoutAddress() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.isValidEmail("test@");

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForEmailWithoutAt() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.isValidEmail("testtest.com");

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForEmailWithoutDotInAddress() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.isValidEmail("test@testcom");

        assertFalse(value);
    }

    @Test
    public void shouldReturnFalseForEmailWithDotInNameAndWithoutDotInAddress() {
        ValidationHelper helper = this.prepareValidationHelper();

        boolean value = helper.isValidEmail("test.com@testcom");

        assertFalse(value);
    }
}
