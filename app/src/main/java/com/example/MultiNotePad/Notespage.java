package com.example.MultiNotePad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notespage extends AppCompatActivity {
    private static boolean isSave = false;
    private String note_title,note,last_save_date;
    private EditText notes, title;
    private Note newnote;
    private String old_title="",old_text="",old_date="";
    private static final String TAG = "MainActivity";
    private MainActivity main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notespage);
        notes = findViewById(R.id.notes);
        notes.setMovementMethod(new ScrollingMovementMethod());
        title=findViewById(R.id.title);
        setTitle("Multi Notes");
        isSave = false;
        //Get note and set texts for it
        Intent receive=getIntent();
        if(receive.hasExtra(Intent.EXTRA_TEXT))
        {
            Note old=(Note)receive.getSerializableExtra(Intent.EXTRA_TEXT);
            old_title=old.getTitle();
            old_text=old.getText();
            old_date=old.getDate();
            title.setText(old_title);
            notes.setText(old_text);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection based on button
        switch (item.getItemId()) {
            case R.id.save:
                isSave = true;
                String flag="";
                note_title = title.getText().toString();
                note = notes.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd, HH:mm aa");
                last_save_date = sdf.format(new Date());
                newnote = new Note(note_title, note, last_save_date);
                if (old_title.equals(note_title) && old_text.equals(note)) {
                    finish();
                }
                if (!note_title.equals("")) {
                    if(!old_title.equals(""))
                        flag="YES";
                    saveCurrentNote(newnote,flag);
                } else {
                    Toast.makeText(this, "Untitled note not saved!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
                return super.onOptionsItemSelected(item);

    }

    //save new note
    public void saveCurrentNote(Note newnote,String flag){
        try {
            Intent data = new Intent();
            data.putExtra("NEW_NOTE", newnote);
            data.putExtra("CHANGE_FLAG",flag);
            setResult(RESULT_OK, data);
            finish();
        }
        catch(Exception e)
        {
        }
    }

    //back button pressed
    public void onBackPressed() {
        if(!isSave) {
            String flag="";
            note_title = title.getText().toString();
            note = notes.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd, HH:mm aa");
            last_save_date = sdf.format(new Date());
            newnote = new Note(note_title, note, last_save_date);
            if(!old_title.equals(note_title) || !old_text.equals(note) && !note_title.equals("")) {
                if(!old_title.equals(""))
                    flag="YES";
                dialogBox(newnote, flag);
            }
            else
                super.onBackPressed();
            isSave=false;
        }
        else
            super.onBackPressed();
    }

    //new note dialog box
    public void dialogBox(final Note newnote,final String flag){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Your note is not saved!\nSave note '"+newnote.getText()+"'?");
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {
                saveCurrentNote(newnote,flag);
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {
                finish();

            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
