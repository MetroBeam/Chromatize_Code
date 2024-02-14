package com.widgetone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<GridItem> arrayList;
    LayoutInflater inflater;
    boolean mutate = false;
    public CustomAdapter(Context applicationContext, ArrayList<GridItem> arrayList) {
        this.context = applicationContext;
        this.arrayList = arrayList;
        inflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return arrayList.size();
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
        view = inflater.inflate(R.layout.grid_item, null); // inflate the layout
        TextView block = (TextView) view.findViewById(R.id.grid_block); // get the reference of ImageView
        ImageView hexagon = view.findViewById(R.id.hexagon);


        final GridItem currentItem = arrayList.get(i);





        int colour = currentItem.getColour();
        String text = currentItem.getDateText();
        String widget = currentItem.getwidget();



        if (widget.equals("Multiple")) {
            hexagon.setVisibility(View.INVISIBLE);

            if (text.equals("null")) {
                block.setBackgroundResource(R.drawable.square_two);
                hexagon.setVisibility(View.VISIBLE);
                hexagon.setBackgroundResource(R.drawable.square_cutout);
            }

            else {
                hexagon.setVisibility(View.INVISIBLE);
                block.setBackgroundResource(R.drawable.square_no_outline);
                block.setTextSize(15f);
                block.setText(text);

            }
            }

        else if (widget.equals("Flower")) {
            hexagon.setVisibility(View.INVISIBLE);
            block.setBackgroundResource(R.drawable.circle);
        }

        else if (widget.equals("Capture"))  {
            block.setBackgroundResource(R.drawable.square_two);
            hexagon.setBackgroundResource(R.drawable.hexgon_cutout);
            hexagon.setVisibility(View.VISIBLE);


        }

        else {
            hexagon.setVisibility(View.INVISIBLE);
            block.setBackgroundResource(R.drawable.square_no_outline);
            block.setText(text);
            block.setTextColor(Color.BLACK);
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/coal.otf");
            block.setTypeface(custom_font);
            block.setTextSize(18f);


        }

       // (widget.equals("Flower"))



    GradientDrawable gradientDrawable = (GradientDrawable) block.getBackground().mutate();
    gradientDrawable.setColor(colour);




        //block.setBackgroundColor(colour); // set logo images



        if (i==0) {
        }
        return view;
    }
}

