package com.nhatdear.sademo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nhatdear.sademo.R;
import com.nhatdear.sademo.activities.SA_LoginActivity;
import com.nhatdear.sademo.components.MyCustomButtonView;
import com.nhatdear.sademo.components.MyCustomEditText;
import com.nhatdear.sademo.components.MyCustomTextView;
import com.nhatdear.sademo.helpers.SA_Validator;

public class SA_LoginFragment extends Fragment {
    private static final String EMAIL = "email";

    MyCustomEditText et_email;
    MyCustomEditText et_password;
    MyCustomButtonView btn_login;
    MyCustomTextView tv_reset_password;
    LinearLayout ln_signUpFB;
    MyCustomButtonView btn_signUp;
    private String userName;
    private OnFragmentInteractionListener mListener;

    public SA_LoginFragment() {
        // Required empty public constructor
    }

    public static SA_LoginFragment newInstance(String userName) {
        SA_LoginFragment fragment = new SA_LoginFragment();
        Bundle args = new Bundle();
        args.putString(EMAIL, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        et_email = (MyCustomEditText)v.findViewById(R.id.et_email);
        et_email.setText(userName);

        et_password = (MyCustomEditText)v.findViewById(R.id.et_password);

        btn_login = (MyCustomButtonView)v.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view -> onLoginBtnClick());

        tv_reset_password = (MyCustomTextView) v.findViewById(R.id.reset_password);
        tv_reset_password.setOnClickListener(view -> onBtnResetPasswordClick());

        ln_signUpFB = (LinearLayout)v.findViewById(R.id.ln_signUpFB);
        ln_signUpFB.setOnClickListener(view -> onSignUpFBBtnClick());

        btn_signUp = (MyCustomButtonView)v.findViewById(R.id.btn_signUp);
        btn_signUp.setOnClickListener(view -> onSignUpBtnClick());
        return v;
    }

    public void onLoginBtnClick() {
        if (mListener != null && validateForm()) {
            mListener.doLoginWithEmail(et_email.getText().toString(), et_password.getText().toString());
        }
    }

    public void onBtnResetPasswordClick() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SA_ResetPasswordFragment.newInstance(userName)).addToBackStack("RESET_PASSWORD").commit();
    }

    public void onSignUpFBBtnClick() {
        SA_LoginActivity activity = (SA_LoginActivity) getActivity();
        activity.startLoginWithFBAction();
    }

    public void onSignUpBtnClick() {
        SA_LoginActivity activity = (SA_LoginActivity) getActivity();
        activity.getSupportFragmentManager().popBackStack("WELCOME", 0);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = et_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            et_email.setError("Required.");
            valid = false;
        } else if (!SA_Validator.validateEmail(email)) {
            et_email.setError("Email format is incorrect.");
            valid = false;
        } else {
            et_email.setError(null);
        }

        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            et_password.setError("Required.");
            valid = false;
        } else if (!SA_Validator.validatePassword(password)) {
            et_password.setError("Password must be complex ( Cap, Numberic, 8 - 32).");
            valid = false;
        } else {
            et_password.setError(null);
        }

        return valid;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void doLoginWithEmail(String userName, String password);
    }
}
