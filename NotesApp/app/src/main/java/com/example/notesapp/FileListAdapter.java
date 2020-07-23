package com.example.notesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileListAdapter extends RecyclerView.Adapter<ViewEntry> {
    private List<Note> notes;
    private MainActivity main;

    public FileListAdapter(List<Note> empList, MainActivity ma) {
        this.notes = empList;
        main = ma;
    }
    public ViewEntry onCreateViewHolder(final ViewGroup parent, int viewType){
    View entry= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewentries,parent,false);
    entry.setOnClickListener(main);
    entry.setOnLongClickListener(main);
    return new ViewEntry(entry);
    }
    public void onBindViewHolder(ViewEntry ve,int pos)
    {
        Note fileList=notes.get(pos);
        ve.title.setText(fileList.getTitle());
        ve.date.setText(fileList.getDate());
        String sampletext=fileList.getText();
        if(sampletext!=null && sampletext.length()>80)
            sampletext=sampletext.substring(0,80)+"...";
        ve.content.setText(sampletext);
    }
    public int getItemCount(){
        return notes.size();
    }
}
