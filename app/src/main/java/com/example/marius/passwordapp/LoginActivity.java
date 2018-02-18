package com.example.marius.passwordapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import cryptopack.PasswordCrypto;

public class LoginActivity extends AppCompatActivity {

    EditText passEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref=getSharedPreferences("PassAppSharedPref", Context.MODE_PRIVATE);
        boolean nextRun=sharedPref.getBoolean("notFirstRun",false);
        if(!nextRun){
            SharedPreferences.Editor editor=sharedPref.edit();
            editor.putBoolean("notFirstRun",true);
            editor.apply();

            Intent toFirstRunActivity=new Intent(this,FirstRunActivity.class);
            startActivity(toFirstRunActivity);
        }

        setContentView(R.layout.activity_login);
        passEditText=(EditText)findViewById(R.id.passEditText);
        passEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_GO){
                    logIn(textView);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Action Listener attached to the button from the GUI, specifies the functionality: check the password against
     * the stored value and proceed if correct
     * @param view
     */
    public void logIn(View view){
        String input=passEditText.getText().toString();
        SharedPreferences sharedPreferences=getSharedPreferences("PassAppSharedPref", Context.MODE_PRIVATE);

        byte[] salt= Base64.decode(sharedPreferences.getString("salt",""),Base64.DEFAULT);
        byte[] encryptedPassword=Base64.decode(sharedPreferences.getString("encryptedPass",""),Base64.DEFAULT);

        try {
            if(PasswordCrypto.authenticate(input,encryptedPassword,salt)){
                Intent toContentList=new Intent(this,ContentList.class);
                startActivity(toContentList);
            } else{
                Toast.makeText(this,"Invalid password",Toast.LENGTH_LONG).show();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
}
