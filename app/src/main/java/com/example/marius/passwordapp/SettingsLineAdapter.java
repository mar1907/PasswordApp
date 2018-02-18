package com.example.marius.passwordapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;


class SettingsLineAdapter extends ArrayAdapter{

    private boolean password;
    private boolean logOut;

    public SettingsLineAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Object[] objects) {
        super(context, resource, objects);

        SharedPreferences sharedPref=context.getSharedPreferences("PassAppSharedPref", Context.MODE_PRIVATE);
        password=sharedPref.getBoolean("Hide password",true);
        logOut=sharedPref.getBoolean("Auto log-out",true);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View customView= inflater.inflate(R.layout.row_with_toggle,parent,false);

        final String text=(String)getItem(position);
        Switch sw=customView.findViewById(R.id.switch1);
        sw.setText(text);
        if(text.equals("Hide password")){
            sw.setChecked(password);
        } else if(text.equals("Auto log-out")){
            sw.setChecked(logOut);
        }

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(customListener!=null){
                    customListener.onSwitch(b,text);
                }
            }
        });

        return customView;
    }

    CustomSwitchListener customListener;

    public interface CustomSwitchListener{
        /**
         * Define the action to be done on switch
         * @param b the boolean value of the switch
         * @param value the label of the switch
         */
        public void onSwitch(boolean b, String value);
    }

    public void setCustomListener(CustomSwitchListener listener){
        customListener=listener;
    }
}
