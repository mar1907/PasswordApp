package com.example.marius.passwordapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements SettingsLineAdapter.CustomSwitchListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String[] settings={"Hide password","Auto log-out"};
        SettingsLineAdapter adapter=new SettingsLineAdapter(this,R.layout.row_with_toggle,settings);
        adapter.setCustomListener(this);

        ListView listView=(ListView)findViewById(R.id.settings_list_view_1);
        listView.setAdapter(adapter);
    }

    @Override
    public void onSwitch(boolean b, String value) {
        SharedPreferences sharedPref=getSharedPreferences("PassAppSharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();

        editor.putBoolean(value,b);
        editor.apply();
    }
}
