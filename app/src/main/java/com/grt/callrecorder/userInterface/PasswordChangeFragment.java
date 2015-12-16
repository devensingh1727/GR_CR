package com.grt.callrecorder.userInterface;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.grt.callrecorder.R;
import com.grt.callrecorder.storage.MyPreferences;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DEVEN SINGH on 9/8/2015.
 */
public class PasswordChangeFragment extends Fragment {

    @Bind(R.id.old_pass_ti)
    TextInputLayout oldPassTil;
    @Bind(R.id.new_pass_ti)
    TextInputLayout newPassTil;
    @Bind(R.id.confirm_pass_ti)
    TextInputLayout confirmTil;
    @Bind(R.id.old_pass)
    EditText oldPass;
    @Bind(R.id.new_pass)
    EditText newPass;
    @Bind(R.id.confirm_pass)
    EditText confirmPass;
    @Bind(R.id.save_changes)
    Button saveChanegs;
    private MyPreferences myPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        ButterKnife.bind(this, view);
        myPreferences = new MyPreferences(getActivity());
        editTextMethods();
        return view;
    }

    private void editTextMethods() {
        if(TextUtils.isEmpty(myPreferences.getPassword())){
            oldPassTil.setVisibility(View.GONE);
        }
        confirmPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        confirmPass.clearFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                    }
                }
                return false;
            }
        });
        oldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable edt) {
                if (oldPass.getText().length() > 0 && oldPass.getText().length() < 4) {
                    oldPassTil.setError("password should be 4 digits");
                }
                if (oldPass.getText().length() == 4) {
                    oldPassTil.setError(null);
                }
            }
        });
        newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable edt) {
                if (newPass.getText().length() > 0 && newPass.getText().length() < 4) {
                    newPassTil.setError("password should be 4 digits");
                }
                if (newPass.getText().length() == 4) {
                    newPassTil.setError(null);
                }
            }
        });
        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable edt) {
                if (confirmPass.getText().length() > 0 && confirmPass.getText().length() < 4) {
                    confirmTil.setError("password should be 4 digits");
                }
                if (confirmPass.getText().length() == 4) {
                    confirmTil.setError(null);
                }
            }
        });
    }

    @OnClick(R.id.save_changes)
    void changePassword() {
        if (!TextUtils.isEmpty(myPreferences.getPassword()) && !oldPass.getText().toString().equalsIgnoreCase(myPreferences.getPassword())) {
            oldPassTil.setError("Enter correct old password.");
        } else if (newPass.getText().toString().length() < 4) {
            newPassTil.setError("password should be 4 digits.");
        } else if (!confirmPass.getText().toString().equalsIgnoreCase(newPass.getText().toString())) {
            confirmTil.setError("password mismatched.");
        } else {
            myPreferences.setPassword(confirmPass.getText().toString());
            Toast.makeText(getActivity(), "Password successfully changed.", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

}
