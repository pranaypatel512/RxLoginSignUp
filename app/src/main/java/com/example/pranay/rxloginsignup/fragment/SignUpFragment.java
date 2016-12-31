package com.example.pranay.rxloginsignup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.pranay.rxloginsignup.BaseFragment;
import com.example.pranay.rxloginsignup.R;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func4;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Pranay.
 */

public class SignUpFragment extends BaseFragment {

    //TextInput Layout
    @BindView(R.id.text_input_email)
    TextInputLayout text_input_email;
    @BindView(R.id.text_input_password)
    TextInputLayout text_input_password;
    @BindView(R.id.text_input_password_confirm)
    TextInputLayout text_input_password_confirm;
    @BindView(R.id.text_input_phone)
    TextInputLayout text_input_phone;


    // EditText
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_password_confirm)
    EditText edt_password_confirm;
    @BindView(R.id.edt_phone)
    EditText edt_phone;

    @BindView(R.id.checkbox_terms_condition)
    CheckBox checkbox_terms_condition;

    //Button
    @BindView(R.id.btnSignUp)
    Button btnSignUp;


    //Subscription that represents a group of Subscriptions that are unsubscribed together.
    private CompositeSubscription mCompositeSubscription;

    private Observable<CharSequence> mEmailObservable, mPasswordObservable, mPasswordConfirmObservable, mPhoneObservable;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCompositeSubscription = new CompositeSubscription();
        initEditTextObservable();
        initSubscriber();
    }

    /**
     * init edittext observable
     */
    private void initEditTextObservable() {
        mEmailObservable = RxTextView.textChanges(edt_email);
        mPasswordObservable = RxTextView.textChanges(edt_password);
        mPasswordConfirmObservable = RxTextView.textChanges(edt_password_confirm);
        mPhoneObservable = RxTextView.textChanges(edt_phone);
    }

    private void initSubscriber() {
        //=========== For Email Validation
        Subscription mEmailSubscription = mEmailObservable.doOnNext(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                emailEditTextError(1); // disable email error
            }
        })
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return !TextUtils.isEmpty(charSequence); // check if not null
                    }
                }).observeOn(AndroidSchedulers.mainThread()) // Main UI Thread
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {
                        // on Completed
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Error
                        Log.e("mEmailSubscription", e.getMessage());
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        // Check every user input for valid email address
                        if (!isUserInputValid(charSequence.toString(), "", 1)) {
                            emailEditTextError(2); // show error for invalid email
                        } else {
                            emailEditTextError(1); // hide error on valid email
                        }
                    }
                });
        mCompositeSubscription.add(mEmailSubscription); // Add email subscriber in composite subscription


        //=========== For Password Validation
        Subscription mPasswordSubscription = mPasswordObservable
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        passwordEditTextError(1);
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return !TextUtils.isEmpty(charSequence);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {
                        // On Completed
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("mPasswordSubscription", e.getMessage());
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        if (!isUserInputValid(charSequence.toString(), "", 2)) {
                            passwordEditTextError(2);
                        } else {
                            passwordEditTextError(1);
                        }
                    }
                });

        mCompositeSubscription.add(mPasswordSubscription);


        Subscription mPasswordConfirmSubscription = mPasswordConfirmObservable.doOnNext(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                passwordEditTextError(3);
            }
        }).debounce(500, TimeUnit.MILLISECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        // If both password EditText Value available
                        return !TextUtils.isEmpty(edt_password.getText().toString())
                                && !TextUtils.isEmpty(charSequence.toString());

                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("mPwdConfirmSubscription", e.getMessage());
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        if (!isUserInputValid(charSequence.toString(),edt_password.getText().toString(),
                                 3)) {
                            passwordEditTextError(4);
                        } else {
                            passwordEditTextError(3);
                        }

                    }
                });
        mCompositeSubscription.add(mPasswordConfirmSubscription);


        Subscription mPhoneSubscription = mPhoneObservable.doOnNext(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                phoneEditTextError(1);
            }
        }).debounce(500, TimeUnit.MILLISECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return !TextUtils.isEmpty(charSequence);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("mPhoneSubscription", e.getMessage());
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        if (!isUserInputValid(charSequence.toString(), "", 4)) {
                            phoneEditTextError(2);
                        } else {
                            phoneEditTextError(1);
                        }
                    }
                });

        mCompositeSubscription.add(mPhoneSubscription);


        Subscription allSignUpFieldSubScription = Observable.combineLatest(mEmailObservable, mPasswordObservable, mPasswordConfirmObservable, mPhoneObservable, new Func4<CharSequence, CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence mEmail, CharSequence mPassword, CharSequence mPasswordConfirm, CharSequence mPhone) {

                return
                        isUserInputValid(mEmail.toString(), "", 1) // Email
                                && isUserInputValid(mPassword.toString(), "", 2) // Password
                                && isUserInputValid(mPassword.toString(), mPasswordConfirm.toString(), 3) // Confirm Password
                                && isUserInputValid(mPhone.toString(), "", 4) // Phone
                        ;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("SignUpFieldSubScription", e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            signUpButtonState(1);
                        } else {
                            signUpButtonState(2);
                        }
                    }
                });
        mCompositeSubscription.add(allSignUpFieldSubScription);


    }

    /**
     * Enable and disable Email error as per case
     *
     * @param whichCase: 1 -> for hide , 2 -> for show
     */
    private void emailEditTextError(int whichCase) {
        switch (whichCase) {
            case 1: // for hide error
                if (text_input_email.getChildCount() == 2) {
                    text_input_email.getChildAt(1).setVisibility(View.GONE);
                }
                text_input_email.setError(null);
                break;
            case 2: // for show error
                if (text_input_email.getChildCount() == 2) {
                    text_input_email.getChildAt(1).setVisibility(View.VISIBLE);
                }
                text_input_email.setError(getString(R.string.str_enter_valid_email));
                break;
        }
    }

    /**
     * Enable and disable Email error as per case
     *
     * @param whichCase: 1 -> for hide , 2 -> for show
     */
    private void passwordEditTextError(int whichCase) {
        switch (whichCase) {
            case 1: // for hide error
                if (text_input_password.getChildCount() == 2) {
                    text_input_password.getChildAt(1).setVisibility(View.GONE);
                }
                text_input_password.setError(null);
                break;
            case 2: // for show error
                if (text_input_password.getChildCount() == 2) {
                    text_input_password.getChildAt(1).setVisibility(View.VISIBLE);
                }
                text_input_password.setError(getString(R.string.str_enter_password));
                break;
            case 3: // for hide error
                if (text_input_password_confirm.getChildCount() == 2) {
                    text_input_password_confirm.getChildAt(1).setVisibility(View.GONE);
                }
                text_input_password_confirm.setError(null);
                break;
            case 4: // for show error
                if (text_input_password_confirm.getChildCount() == 2) {
                    text_input_password_confirm.getChildAt(1).setVisibility(View.VISIBLE);
                }
                text_input_password_confirm.setError(getString(R.string.str_password_must_be_same));
                break;
        }
    }

    /**
     * Phone number EditText Error
     */
    private void phoneEditTextError(int whichCase) {
        switch (whichCase) {
            case 1: // for hide error
                if (text_input_phone.getChildCount() == 2) {
                    text_input_phone.getChildAt(1).setVisibility(View.GONE);
                }
                text_input_phone.setError(null);
                break;
            case 2: // for show error
                if (text_input_phone.getChildCount() == 2) {
                    text_input_phone.getChildAt(1).setVisibility(View.VISIBLE);
                }
                text_input_phone.setError(getString(R.string.str_invalid_phone_number));
                break;
        }
    }

    /**
     * Enable and disable login button as per case
     *
     * @param whichCase : 1 -> enable , 2 ->  disable
     */
    private void signUpButtonState(int whichCase) {
        switch (whichCase) {
            case 1: // enable button
                btnSignUp.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                btnSignUp.setEnabled(true);
                btnSignUp.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                break;
            case 2: // disable button
                btnSignUp.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_disable));
                btnSignUp.setEnabled(false);
                btnSignUp.setTextColor(ContextCompat.getColor(getContext(), R.color.color_disable_text));
                break;
        }

    }

    /**
     * Validate user details for email and password
     */

    private boolean isUserInputValid(String userInput, String userInputMatch, int whichCase) {
        switch (whichCase) {
            case 1: // check email input
                return !TextUtils.isEmpty(userInput) && Patterns.EMAIL_ADDRESS.matcher(userInput).matches();
            case 2: // check password input
                return userInput.length() >= 6; // password should be 6 or more than 6.
            case 3: // confirm password
                return TextUtils.equals(userInput, userInputMatch); // for both password same
            case 4: // Check Phone number
                return userInput.length() >= 10;
            case 5:
                return checkbox_terms_condition.isChecked();
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

    @OnClick(R.id.btnSignUp)
    public void setOnClick() {
        if (!isUserInputValid("", "", 5)) {
            showToast(getString(R.string.str_please_accept));
        } else {
            showToast(getString(R.string.str_signup_success));
        }
    }
}
