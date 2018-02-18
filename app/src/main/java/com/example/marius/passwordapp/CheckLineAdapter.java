package com.example.marius.passwordapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

class CheckLineAdapter extends ArrayAdapter{

    boolean isLongPressed;

    public CheckLineAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
        isLongPressed=false;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View customView= inflater.inflate(R.layout.row_with_select,parent,false);

        String domain=(String)getItem(position);
        TextView textView=customView.findViewById(R.id.line_text);
        textView.setText(domain);

        CheckBox checkBox=customView.findViewById(R.id.hidden_box);
        if(isLongPressed){
            checkBox.setVisibility(View.VISIBLE);
        } else{
            checkBox.setVisibility(View.INVISIBLE);
        }

        return customView;
    }

    /**
     * have the getView method be called again with different values for isLongPressed to show the check box
     */
    public void showCheckBox(){
        isLongPressed=true;
        notifyDataSetChanged();
    }

    /**
     * have the getView method be called again with different values for isLongPressed to hide the check box
     */
    public void hideCheckBox(){
        isLongPressed=false;
        notifyDataSetChanged();
    }
}
