package com.nhatdear.sademo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhatdear.sademo.R;
import com.nhatdear.sademo.components.MyCustomButtonView;
import com.nhatdear.sademo.components.MyCustomEditText;
import com.nhatdear.sademo.helpers.SA_Validator;

import static com.nhatdear.sademo.helpers.SA_Helper.EMAIL_IS_NOT_CORRECT_FORMAT;
import static com.nhatdear.sademo.helpers.SA_Helper.USERNAME_MUST_NOT_CONTAIN_SPECIAL_CHAR;

public class SA_SignUpFragment extends Fragment {
    MyCustomEditText et_username;
    MyCustomEditText et_email;
    MyCustomEditText et_password;
    MyCustomButtonView btn_signUpEmail;
    private OnFragmentInteractionListener mListener;

    public SA_SignUpFragment() {
    }

    public static SA_SignUpFragment newInstance() {
        SA_SignUpFragment fragment = new SA_SignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        et_username = (MyCustomEditText)v.findViewById(R.id.et_username);

        et_email = (MyCustomEditText)v.findViewById(R.id.et_email);

        et_password = (MyCustomEditText)v.findViewById(R.id.et_password);

        btn_signUpEmail = (MyCustomButtonView)v.findViewById(R.id.btn_signUpEmail);
        btn_signUpEmail.setOnClickListener(view -> onButtonPressed());

        return v;
    }

    public void onButtonPressed() {
        if (mListener != null && validateForm()) {
            mListener.onSignUpWithEmail(et_username.getText().toString(), et_email.getText().toString(), et_password.getText().toString());
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String userName = et_username.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            et_username.setError("Required.");
            valid = false;
        } else if (!SA_Validator.validateNormalString(userName)) {
            et_username.setError(USERNAME_MUST_NOT_CONTAIN_SPECIAL_CHAR);
            valid = false;
        } else {
            et_username.setError(null);
        }

        String email = et_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            et_email.setError("Required.");
            valid = false;
        } else if (!SA_Validator.validateEmail(email)) {
            et_email.setError(EMAIL_IS_NOT_CORRECT_FORMAT);
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
        void onSignUpWithEmail(String userName, String email, String password);
    }
}
