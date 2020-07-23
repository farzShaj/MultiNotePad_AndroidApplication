package com.example.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static android.provider.LiveFolders.INTENT;
import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener  {
    private static final int CODE = 1;
    private static final String TAG = "MainActivity";
    public List<Note> notes=new ArrayList<Note>();
    private RecyclerView recycler;
    private FileListAdapter fileListAdapter;
    private Note old_note,nextnote,note;
    private String note_title,note_text,last_save_date;
    private int num=0,pos;
    Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler=findViewById(R.id.recycler);
        fileListAdapter=new FileListAdapter(notes,this);
        recycler.setAdapter(fileListAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        //load JSON and set to recycler
        loadFile();
        //Collections.sort(notes);
        fileListAdapter.notifyDataSetChanged();
        num=notes.size();
        setTitle("Multi Notes("+num+")");
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                Intent intent=new Intent(this,AboutPage.class);
                startActivity(intent);
                break;
            case R.id.add:
                intent1=new Intent(this,Notespage.class);
                startActivityForResult(intent1,CODE);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==-1)
        {
                Note arraynote = (Note) data.getSerializableExtra("NEW_NOTE");
                String changed=data.getStringExtra("CHANGE_FLAG");
                if(changed.equals("YES"))
                {
                    notes.remove(pos);
                }
                notes.add(0,arraynote);
                fileListAdapter.notifyDataSetChanged();
                num=notes.size();
                setTitle("Multi Notes("+num+")");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void onPause(){
        try {
            Note next_note;
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent(" ");
            Iterator it=notes.iterator();
            writer.beginArray();
            while(it.hasNext())
            {
                next_note=(Note)it.next();
                writer.beginObject();
                writer.name("title").value(next_note.getTitle());
                writer.name("text").value(next_note.getText());
                writer.name("date").value(next_note.getDate());
                writer.endObject();
            }
            writer.endArray();
            writer.close();

        }catch(Exception e){}
        super.onPause();
    }
    private void loadFile() {
        note = new Note();
        try {
            InputStream is = getApplicationContext().
                    openFileInput(getString(R.string.file_name));
            String name;
            JsonReader reader=new JsonReader(new InputStreamReader(is, "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    name = reader.nextName();
                    if (name.equals("title")) {
                        note_title = reader.nextString();
                    } else if (name.equals("text")) {
                        note_text = reader.nextString();
                    } else if (name.equals("date") ) {
                        last_save_date= reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
                nextnote=new Note(note_title,note_text,last_save_date);
                notes.add(nextnote);
            }
            reader.endArray();
            reader.close();

           } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "loadFile: gettingLoadingerror");
            e.printStackTrace();
            e.getMessage();
            Log.d(TAG, "loadFile: Loadingerror");
        }
    }
    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks

        pos = recycler.getChildLayoutPosition(v);
        old_note = notes.get(pos);
        Intent intent1=new Intent(this,Notespage.class);
        intent1.putExtra(Intent.EXTRA_TEXT,old_note);
        startActivityForResult(intent1,CODE);
        intent1.putExtra("NOTES_DETAILS",old_note);
    }

    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        int pos = recycler.getChildLayoutPosition(v);
        Note del_note = notes.get(pos);
        dialogBox(del_note);
        return false;
    }
    public void dialogBox(final Note newnote){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Delete note '"+newnote.getTitle()+"'?");
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {
                notes.remove(newnote);
                fileListAdapter.notifyDataSetChanged();
                num=notes.size();
                setTitle("Multi Notes("+num+")");
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {

            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

    }
}
