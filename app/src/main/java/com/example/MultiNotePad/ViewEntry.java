package com.example.MultiNotePad;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.MultiNotePad.R;


public class ViewEntry extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView date;
    public TextView content;
    public ViewEntry(View view)
    {
        super(view);
        title =view.findViewById(R.id.title);
        date=view.findViewById(R.id.date);
        content=view.findViewById(R.id.content);
    }
}
