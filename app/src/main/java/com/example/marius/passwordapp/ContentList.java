package com.example.marius.passwordapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import model.AccountControl;

public class ContentList extends AppCompatActivity {

    String[] returnData;
    Context context=this;
    AccountControl accountControl;
    boolean restart=false;
    boolean longClicked;
    CheckLineAdapter adapter;
    ListView listView;
    private boolean password;
    private boolean logOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accountControl =new AccountControl(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.mipmap.ic_add);
        //fab.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
        fab.setRippleColor(Color.YELLOW);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(null,null);

            }
        });

        createList(accountControl);

        SharedPreferences sharedPref=context.getSharedPreferences("PassAppSharedPref", Context.MODE_PRIVATE);
        password=sharedPref.getBoolean("Hide password",true);
        logOut=sharedPref.getBoolean("Auto log-out",true);
    }

    /**
     * Create the ListView including setting the listeners
     * @param accountControl the AccountControl object to be used to get data for the list
     */
    private void createList(final AccountControl accountControl){
        adapter=new CheckLineAdapter(this, R.layout.row_with_select, accountControl.getAccountDomains());
        listView=(ListView)findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        showDialog(accountControl.getAccount(i),i);

                    }
                }
        );
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        longClicked=true;
                        adapter.showCheckBox();
                        invalidateOptionsMenu();
                        return true;
                    }
                }
        );
    }

    /**
     * Create and show a Dialog
     * @param data the data for the EditTexts (if any) corresponding to the Account object currently edited, null for new Accounts
     * @param i the index of the Account object currently edited (if any), null for new Accounts
     */
    private void showDialog(@Nullable String[] data, @Nullable final Integer i){
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.account_view_dialog_layout);
        dialog.setTitle("Title");

        final EditText domainEditText=dialog.findViewById(R.id.domainEditText);
        final EditText usernameEditText=dialog.findViewById(R.id.usernameEditText);
        final EditText passEditText=dialog.findViewById(R.id.passEditText);

        if(data!=null){
            domainEditText.setText(data[0]);
            usernameEditText.setText(data[1]);
            passEditText.setText(data[2]);
        }

        Button cancelButton=dialog.findViewById(R.id.cancel_button);
        Button okButton=dialog.findViewById(R.id.ok_button);

        cancelButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                }
        );

        okButton.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        returnData=new String[3];
                        returnData[0]=domainEditText.getText().toString();
                        returnData[1]=usernameEditText.getText().toString();
                        returnData[2]=passEditText.getText().toString();

                        if(i==null){
                            accountControl.addAccount(returnData[0],returnData[1],returnData[2]);
                            createList(accountControl);
                        } else{
                            accountControl.modifyAccount(i,returnData[0],returnData[1],returnData[2]);
                            createList(accountControl);
                        }

                        dialog.cancel();
                    }
                }
        );

        CheckBox showPass=dialog.findViewById(R.id.checkBox);

        showPass.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            passEditText.setTransformationMethod(null);
                        } else {
                            passEditText.setTransformationMethod(new PasswordTransformationMethod());
                        }
                    }
                }
        );

        if(!password){
            showPass.setChecked(true);
            passEditText.setTransformationMethod(null);
        }

        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        accountControl.updateFile();
        restart=true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref=context.getSharedPreferences("PassAppSharedPref", Context.MODE_PRIVATE);
        password=sharedPref.getBoolean("Hide password",true);
        logOut=sharedPref.getBoolean("Auto log-out",true);

        if(restart&&logOut){
            Intent backToLogin=new Intent(this,LoginActivity.class);
            startActivity(backToLogin);
            restart=false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_act_menu, menu);
        MenuItem menuItem=menu.findItem(R.id.action_delete);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem=menu.findItem(R.id.action_delete);
        if(longClicked){
            menuItem.setVisible(true);
        }
        else {
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(longClicked){
            longClicked=false;
            adapter.hideCheckBox();
            invalidateOptionsMenu();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                deleteSelectedItems();
                break;
            case R.id.action_settings:
                Intent i=new Intent(this,SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.action_change_pass:
                Intent i1=new Intent(this,ChangePasswordActivity.class);
                startActivity(i1);
                break;
        }
        return true;
    }

    /**
     * Delete the selected items from the list and update
     */
    public void deleteSelectedItems(){
        ArrayList<Integer> selectedIndices=new ArrayList<>();
        for(int i=0; i<adapter.getCount(); i++){
            View view=getViewByPosition(i,listView);

            CheckBox cb=view.findViewById(R.id.hidden_box);
            TextView tv=view.findViewById(R.id.line_text);
            if(cb.isChecked()){
                selectedIndices.add(i);
            }
        }

        for(int i=selectedIndices.size()-1;i>=0;i--){
            accountControl.deleteAccount(selectedIndices.get(i));
        }

        //at the end
        longClicked=false;
        adapter.hideCheckBox();
        invalidateOptionsMenu();
        createList(accountControl);
    }

    /**
     * Return the View in the ListView at position pos
     * @param pos the position
     * @param listView the ListView
     * @return the desired View
     */
    public View getViewByPosition(int pos, ListView listView){
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
