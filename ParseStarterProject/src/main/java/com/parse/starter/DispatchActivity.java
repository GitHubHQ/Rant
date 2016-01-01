package com.parse.starter;

/**
 * Created by harrison on 12/31/2015.
 */
import com.parse.ParseUser;
import com.parse.ui.*;
import com.parse.ui.ParseLoginBuilder;
import com.parse.ui.ParseLoginDispatchActivity;

public class DispatchActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
                return MainActivity.class;
    }
}