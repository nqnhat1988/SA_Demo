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
import com.nhatdear.sademo.events.SA_ErrorEvent;
import com.nhatdear.sademo.helpers.SA_Helper;
import com.nhatdear.sademo.helpers.SA_Validator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.nhatdear.sademo.helpers.SA_Helper.EMAIL_IS_NOT_CORRECT_FORMAT;


public class SA_ResetPasswordFragment extends Fragment {
    private static final String EMAIL = "email";

    MyCustomEditText et_email;
    MyCustomButtonView btn_reset;

    private String email;
    private OnFragmentInteractionListener mListener;

    public SA_ResetPasswordFragment() {
        // Required empty public constructor
    }

    public static SA_ResetPasswordFragment newInstance(String email) {
        SA_ResetPasswordFragment fragment = new SA_ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putString(EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reset_password, container, false);

        et_email = (MyCustomEditText)v.findViewById(R.id.et_email);
        et_email.setText(email);

        btn_reset = (MyCustomButtonView)v.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(view -> onButtonPressed());

        return v;
    }

    public void onButtonPressed() {
        if (mListener != null && validateForm()) {
            mListener.doResetPasswordAction(et_email.getText().toString());
        }
    }

    private boolean validateForm() {
        boolean valid = true;

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(SA_ErrorEvent event) {
        SA_Helper.showSnackbar(getView(), event.mMessage);
    }

    public interface OnFragmentInteractionListener {
        void doResetPasswordAction(String s);
    }
}
