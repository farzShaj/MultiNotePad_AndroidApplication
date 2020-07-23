package com.example.MultiNotePad;

import java.io.Serializable;

public class Note implements Serializable {
    private String title,text;
    private String last_save_date;
    Note(){}
    Note(String title, String text, String last_save_date){
        this.title=title;
        this.text=text;
        this.last_save_date=last_save_date;
    }
    public String getTitle(){
        return title;
    }
    public String getText()
    {
        return text;
    }
    public String getDate(){
        return last_save_date;
    }
    public void setDate(String last_save_date)
    {
        this.last_save_date=last_save_date;
    }
}
