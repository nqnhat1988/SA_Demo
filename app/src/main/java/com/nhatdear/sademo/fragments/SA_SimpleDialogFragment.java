package com.nhatdear.sademo.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhatdear.sademo.R;
import com.nhatdear.sademo.components.MyCustomButtonView;
import com.nhatdear.sademo.components.MyCustomEditText;
import com.nhatdear.sademo.components.MyCustomTextView;
import com.nhatdear.sademo.helpers.SA_Validator;

public class SA_SimpleDialogFragment extends DialogFragment {
    MyCustomEditText editText;
    MyCustomButtonView btn_ok;
    MyCustomButtonView btn_cancel;
    MyCustomTextView tv_title;
    MyCustomTextView tv_message;
    TextInputLayout til_editName;

    OnOKClickListener onOKClickListener;
    OnCancelClickListener onCancelClickListener;

    private String title = "";
    private String message = "";
    private String editTextHint = "";
    public SA_SimpleDialogFragment() {

    }

    public static SA_SimpleDialogFragment newInstance() {
        SA_SimpleDialogFragment fragment = new SA_SimpleDialogFragment();
        return fragment;
    }

    public static SA_SimpleDialogFragment newInstance(String title, String message, String editTextHint) {
        SA_SimpleDialogFragment fragment = new SA_SimpleDialogFragment();
        fragment.title = title;
        fragment.message = message;
        fragment.editTextHint = editTextHint;
        return fragment;
    }

    public void addOnBtnOkClickListener(OnOKClickListener listener) {
        this.onOKClickListener = listener;
    }

    public void addOnBtnCancelClickListener(OnCancelClickListener listener) {
        this.onCancelClickListener = listener;
    }

    void onOKClick() {
        String userName = editText.getText().toString().trim();
        if (userName.isEmpty()) {
            editText.setError("Required!");
            return;
        } else if (!SA_Validator.validateNormalString(userName)) {
            editText.setError("Require alphanumeric input");
            return;
        }
        if (onOKClickListener != null) {
            onOKClickListener.onBtnOkClick(userName);
        }
    }

    void onCancelClick() {
        if (onCancelClickListener != null) {
            onCancelClickListener.onBtnCancelClick();
        } else {
            this.dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onCancelClickListener = null;
        onOKClickListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_dialog, container, false);

        editText = (MyCustomEditText)view.findViewById(R.id.editText);

        tv_title = (MyCustomTextView)view.findViewById(R.id.tv_title);
        if (!title.isEmpty()) {
            tv_title.setText(title);
        }

        tv_message = (MyCustomTextView)view.findViewById(R.id.tv_message);
        if (!message.isEmpty()) {
            tv_message.setText(message);
        }

        til_editName = (TextInputLayout)view.findViewById(R.id.til_editName);
        if (!editTextHint.isEmpty()) {
            til_editName.setHint(editTextHint);
        }

        btn_ok = (MyCustomButtonView)view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(v -> onOKClick());

        btn_cancel = (MyCustomButtonView)view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(v -> onCancelClick());
        return view;
    }

    public interface OnOKClickListener {
        void onBtnOkClick(String s);
    }

    public interface OnCancelClickListener {
        void onBtnCancelClick();
    }
}
