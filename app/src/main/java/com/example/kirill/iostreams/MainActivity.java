package com.example.kirill.iostreams;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private EditText editText;
    private Button buttonSaveSD;
    private Button buttonSaveMemory;
    private Button buttonLoadSD;
    private Button buttonLoadMemory;
    private AppCompatCheckedTextView checkedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initComponents();
    }

    private void initComponents() {
        this.initToolbar();
        this.initFloatingActionButton();
        this.initButtons();
        this.initCheckedTextView();
    }

    private void initToolbar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initButtons() {
        this.editText = (EditText) this.findViewById(R.id.edit_text);
        this.buttonSaveSD  = (Button) this.findViewById(R.id.save_sd_card);
        this.buttonSaveSD.setOnClickListener(this);
        this.buttonSaveMemory = (Button) this.findViewById(R.id.save_phone_memory);
        this.buttonSaveMemory.setOnClickListener(this);
        this.buttonLoadSD = (Button) this.findViewById(R.id.load_sd_card);
        this.buttonLoadSD.setOnClickListener(this);
        this.buttonLoadMemory = (Button) this.findViewById(R.id.load_phone_memory);
        this.buttonLoadMemory.setOnClickListener(this);
    }

    private void initCheckedTextView() {
        this.checkedTextView = (AppCompatCheckedTextView) this.findViewById(R.id.checked_text_view);
        this.checkedTextView.setOnClickListener(new View.OnClickListener() {
            private ActionMode actionMode;

            @Override
            public void onClick(View v) {
                if (!checkedTextView.isChecked()) {
                    checkedTextView.setChecked(true);
                    actionMode = startActionMode(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            return false;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            checkedTextView.setChecked(false);
                        }
                    });
                } else {
                    checkedTextView.setChecked(false);
                    actionMode.finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            this.editText.setText("");
            this.toastMaker("Clear");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_sd_card:
                this.buttonSaveSDCard();
                break;
            case R.id.save_phone_memory:
                this.buttonSavePhoneMemory();
                break;
            case R.id.load_sd_card:
                this.buttonLoadSDCard();
                break;
            case R.id.load_phone_memory:
                this.buttonLoadPhoneMemory();
                break;
        }
    }

    private void buttonSaveSDCard() {
        if(!Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED))
            return;
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath, "IOStreams");
        if(!sdPath.exists())
            sdPath.mkdirs();
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(new File(sdPath, "file.txt")));
            bufferedWriter.write(this.editText.getText().toString());
            bufferedWriter.close();
            this.toastMaker("Saving successful");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buttonSavePhoneMemory() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(this.openFileOutput("file.txt", MODE_PRIVATE)));
            bufferedWriter.write(this.editText.getText().toString());
            bufferedWriter.close();
            this.toastMaker("Saving successful");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buttonLoadSDCard() {
        if(!Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED))
            return;
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath, "IOStreams");
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(new File(sdPath, "file.txt")));
            this.editText.setText(bufferedReader.readLine());
            bufferedReader.close();
            this.toastMaker("Loading successful");
        } catch (IOException e) {
            e.printStackTrace();
            this.toastMaker("File not found");
        }
    }

    private void buttonLoadPhoneMemory() {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(this.openFileInput("file.txt")));
            this.editText.setText(bufferedReader.readLine());
            bufferedReader.close();
            this.toastMaker("Loading successful");
        } catch (IOException e) {
            e.printStackTrace();
            this.toastMaker("File not found");
        }
    }

    private void toastMaker(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
