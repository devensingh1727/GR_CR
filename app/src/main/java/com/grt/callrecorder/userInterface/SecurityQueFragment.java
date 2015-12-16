package com.grt.callrecorder.userInterface;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.grt.callrecorder.R;
import com.grt.callrecorder.storage.MyPreferences;
import com.grt.callrecorder.utilities.ConstantsValue;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DEVEN SINGH on 9/15/2015.
 */
public class SecurityQueFragment extends Fragment {

    @Bind(R.id.security_question)
    TextView security_question;
    @Bind(R.id.security_answer)
    EditText security_answer;
    @Bind(R.id.save_que)
    Button saveSecurityQues;
    private MyPreferences myPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_security_question, container, false);
        ButterKnife.bind(this, view);
        myPreferences = new MyPreferences(getActivity());
        return view;
    }

    @OnClick(R.id.save_que)
    void saveQuestionAnswer() {
        if (TextUtils.isEmpty(security_answer.getText())) {
            Toast.makeText(getActivity(), "Answer should not be empty", Toast.LENGTH_LONG).show();
        } else if (security_question.getText().toString().equalsIgnoreCase("Select Question")) {
            Toast.makeText(getActivity(), "Select a question", Toast.LENGTH_LONG).show();
        } else {
            myPreferences.setSecurityAnswer(security_answer.getText().toString());
            myPreferences.setSecurityQuestion(security_question.getText().toString());
            Toast.makeText(getActivity(), "Security question and answer saved.", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @OnClick(R.id.security_question)
    void chooseSecurityQuestion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.IshiDialogTheme);
        builder.setTitle("Select Security Question")
                .setItems(ConstantsValue.SECURITY_QUESTION, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        security_question.setText("" + ConstantsValue.SECURITY_QUESTION[which]);
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
