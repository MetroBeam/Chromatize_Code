package com.widgetone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class PagerListAdapter extends BaseAdapter {

    Context context;
    //ArrayList<Item> suggestionsArray;
    String [] suggestionsArray;
    LayoutInflater inflater;


    //public PagerListAdapter(Context applicationContext, ArrayList<Item> suggestionsArray) {
        public PagerListAdapter(Context applicationContext, String [] suggestionsArray) {
        this.context = applicationContext;
        this.suggestionsArray = suggestionsArray;
        inflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        //return suggestionsArray.size();
        return suggestionsArray.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.pager_list_item, null); // inflate the layout
        //final TextView questionTextView = (TextView) view.findViewById(R.id.check_text_box); // get the reference of ImageView
        final CheckedTextView questionTextView = (CheckedTextView) view.findViewById(R.id.check_text_view); // get the reference of ImageView
       // final CheckBox checkBox = view.findViewById(R.id.check);
        String currentItemtext = suggestionsArray[i];
       // final Boolean checked = suggestionsArray.get(i).getChecked();










        questionTextView.setText(currentItemtext); // set logo images
        //checkBox.setChecked(checked);



        return view;
    }


}

