package com.grt.callrecorder.userInterface;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grt.callrecorder.R;
import com.grt.callrecorder.storage.MyPreferences;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Deven on 02-10-2015.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.ll_pass_digits)
    LinearLayout llPassDigits;
    @Bind(R.id.forgot_pass)
    TextView forgotPassword;
    @BindColor(R.color.secondary_text)
    int textColor;
    private int[] passDigits = {R.id.pass_digit1, R.id.pass_digit2, R.id.pass_digit3, R.id.pass_digit4};
    private int[] numButtonIds = {R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four,
            R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine};
    private TextView[] passDigitTvs = new TextView[passDigits.length];
    private String[] passcode = new String[passDigits.length];
    private Button[] buttonNumbers = new Button[numButtonIds.length];
    private int counter = -1;
    private String password = "";
    private MyPreferences myPreferences;
    private Handler handler = new Handler();
    private Animation animation;
    private Vibrator vibrator = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        setFullScreenActivity();
        ButterKnife.bind(this);
        initComponents();
    }

    public void setFullScreenActivity() {
        if (Build.VERSION.SDK_INT < 19) {
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void initComponents() {
        myPreferences = new MyPreferences(LoginActivity.this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        for (int i = 0; i < numButtonIds.length; i++) {
            buttonNumbers[i] = (Button) findViewById(numButtonIds[i]);
            buttonNumbers[i].setOnClickListener(this);
        }
        for (int i = 0; i < passDigits.length; i++) {
            passDigitTvs[i] = (TextView) findViewById(passDigits[i]);
            passcode[i] = "";
        }
        animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.animation_pass);
        animation.setAnimationListener(mAnimationListener);
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < numButtonIds.length; i++) {
            if (v == buttonNumbers[i]) {
                if (counter < 3) {
                    counter++;
                    passDigitTvs[counter].setBackgroundResource(R.drawable.post_passcode);
                    passcode[counter] = (i + "");
                    if (counter == 3) {
                        for (int j = 0; j < passDigitTvs.length; j++) {
                            password += passcode[j];
                        }
                        if (password.equals(myPreferences.getPassword())) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                                                for (int j = 0; j < passDigitTvs.length; j++) {
                                                    passcode[j] = "";
                                                    counter = -1;
                                                    passDigitTvs[j].setBackgroundResource(R.drawable.pre_passcode);
                                                }
                                                password = "";
                                                vibrator.vibrate(100);
                                                llPassDigits.startAnimation(animation);
                                            }
                                        });
                                    } catch (Exception e) {
                                    }
                                }
                            }, 100);
                        }

                    }
                }
            }
        }

    }

    Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    @OnClick(R.id.forgot_pass)
    void forgotPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.IshiDialogTheme);
        final EditText input = new EditText(this);
        input.setText("");
        input.setHint("enter answer!");
        input.setBackgroundResource(R.drawable.editbox_bg);
        input.setTextColor(textColor);
        builder.setTitle("Security question")
                .setMessage(myPreferences.getSecurityQuestion())
                .setView(input)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (input.getText().toString().equalsIgnoreCase(myPreferences.getSecurityAnswer())) {
                            Toast.makeText(LoginActivity.this, "Password is " + myPreferences.getPassword(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Answer is invalid.", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
//        Window window = dialog.getWindow();
//        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//            window.setBackgroundDrawable(new ColorDrawable(
//                    android.graphics.Color.TRANSPARENT));
//        }
        dialog.show();

    }

}
