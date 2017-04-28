package com.nhatdear.sademo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nhatdear.sademo.R;
import com.nhatdear.sademo.activities.SA_LoginActivity;
import com.nhatdear.sademo.components.MyCustomButtonView;
import com.nhatdear.sademo.components.MyCustomTextView;
import com.nhatdear.sademo.helpers.SA_TagSpan;

public class SA_WelcomeFragment extends Fragment {
    MyCustomTextView tv_information;
    MyCustomButtonView btn_doLogin;
    MyCustomButtonView btn_signUpEmail;
    MyCustomButtonView btn_signUpGuest;
    LinearLayout ln_signUpFB;
    private OnFragmentInteractionListener mListener;

    public SA_WelcomeFragment() {
        // Required empty public constructor
    }


    public static SA_WelcomeFragment newInstance() {
        SA_WelcomeFragment fragment = new SA_WelcomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);

        tv_information = (MyCustomTextView)v.findViewById(R.id.tv_information);
        tv_information.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                String key1 = "Terms Of Service";
                String key2 = "Privacy Policy";
                int start = text.indexOf(key1);
                int end = start + key1.length();
                editable.setSpan(new SA_TagSpan(getContext(), true, Uri.parse("https://aboutme-dd4ae.firebaseapp.com")), start, end, 0);

                start = text.indexOf(key2);
                end = start + key2.length();
                editable.setSpan(new SA_TagSpan(getContext(), true, Uri.parse("https://aboutme-dd4ae.firebaseapp.com")), start, end, 0);
            }
        });

        tv_information.setText(getString(R.string.term));
        tv_information.setMovementMethod(new LinkMovementMethod());

        btn_doLogin = (MyCustomButtonView)v.findViewById(R.id.btn_doLogin);
        btn_doLogin.setOnClickListener(view -> onLoginBtnClick());

        btn_signUpEmail = (MyCustomButtonView)v.findViewById(R.id.btn_signUpEmail);
        btn_signUpEmail.setOnClickListener(view -> signUpEmail());

        ln_signUpFB = (LinearLayout)v.findViewById(R.id.ln_signUpFB);
        ln_signUpFB.setOnClickListener(view -> signUpFB());

        btn_signUpGuest = (MyCustomButtonView)v.findViewById(R.id.btn_signUpGuest);
        btn_signUpGuest.setOnClickListener(view -> loginAsGuest());
        return v;
    }

    private void loginAsGuest() {
        ((SA_LoginActivity)getActivity()).doLoginWithEmail("nhatdear1988@gmail.com","Allinone123");
    }

    public void onLoginBtnClick() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SA_LoginFragment.newInstance("")).addToBackStack("EMAIL_LOGIN").commit();
    }

    public void signUpEmail() {
        if (mListener != null) {
            mListener.startLoginWithEmailFragment();
        }
    }

    public void signUpFB() {
        if (mListener != null) {
            mListener.startLoginWithFBAction();
        }


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
        void startLoginWithEmailFragment();

        void startLoginWithFBAction();
    }
}
