package com.ts.mobilepicklist;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.widget.TextView;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private TextView mFirstTestText;

    public ApplicationTest() {
        super(Application.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }
}