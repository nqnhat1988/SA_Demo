package com.nhatdear.sademo.helpers;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Created by nhatdear on 4/26/17.
 */
public class DateUtilsTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void getMonthFromDate() throws Exception {
        assertEquals(1, DateUtils.getMonthFromDate("2012-01-02"));
        assertEquals(2, DateUtils.getMonthFromDate("2012-02-02"));
        assertEquals(3, DateUtils.getMonthFromDate("2012-03-02"));
    }

}