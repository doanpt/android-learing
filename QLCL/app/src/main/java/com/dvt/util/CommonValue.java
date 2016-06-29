package com.dvt.util;

import java.util.regex.Pattern;

/**
 * Created by DoanPT1 on 6/29/2016.
 */
public class CommonValue {
    public static final String RESULT_CODE_STUDENT = "CODE_STUDENT";
    public static final String KEY_CODE = "KEY_CODE";
    public static final Pattern sPattern = Pattern.compile("^[0-9]{10}$");
}
