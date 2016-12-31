package com.example.pranay.rxloginsignup.fragment;

import com.example.pranay.rxloginsignup.BaseFragment;
import com.example.pranay.rxloginsignup.R;

/**
 * Created by Pranay.
 */

public class SignUpFragment extends BaseFragment {

    public static SignUpFragment INSTANT = null;

    public static SignUpFragment getInstant() {
        if (INSTANT == null) {
            INSTANT = new SignUpFragment();
        }
        return INSTANT;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_signup;
    }
}
