package com.nhatdear.sademo.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by NhatNguyen on 11/25/2016.
 */

public class SA_Validator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,64})";
    private static final String NORMAL_TEXT = "^[a-zA-Z0-9_ ]{4,32}";
    private static final String TAGS_PATTERN = "^[A-Za-z0-9\\+]+[a-zA-Z0-9_ ]{2,12}";
    public static String InvalidNormalString = "INVALID TAG\nNo special symbol\n3-12 characters";
    public static String Required = "REQUIRED";

    public static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static boolean validateNormalString(String input) {
        Pattern p = Pattern.compile(NORMAL_TEXT);
        Matcher m = p.matcher(input.toLowerCase());
        return m.matches();
    }

    public static boolean validateTagString(String input) {
        Pattern p = Pattern.compile(TAGS_PATTERN);
        Matcher m = p.matcher(input.toLowerCase());
        return m.matches();
    }
}
