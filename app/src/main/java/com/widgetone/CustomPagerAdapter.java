package com.widgetone;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomPagerAdapter extends PagerAdapter  {


    Context mContext;
    LayoutInflater mLayoutInflater;
    int[] mImage;
    int [] mText;
    ArrayList<Item> pagerArrayList;
    List<String> suggestionsList;


    private final   DataEntrylistener dataEntrylistener;




    public CustomPagerAdapter(Context context, ArrayList <Item> pagerArrayList, DataEntrylistener suggestionSelectionlistener) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pagerArrayList = pagerArrayList;
        this.dataEntrylistener = suggestionSelectionlistener;

    }


    @Override
    public int getCount() {

        return pagerArrayList.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int pagerPosition) {

       View itemView = mLayoutInflater.inflate(R.layout.view_pager_item, container, false);
        Log.d("resetting", "instantiateItem: ");

        final TextView questionTxt;
        questionTxt= (TextView) itemView.findViewById(R.id.viewpager_text_view);

        itemView.setTag("v" + pagerPosition);

        final String questionText = pagerArrayList.get(pagerPosition).getQuestionText();
        questionTxt.setText(questionText);


        final String suggestionsArray[] = pagerArrayList.get(pagerPosition).getSuggestionsText().split("~");


        Log.d("instantiateItem: ", pagerArrayList.get(pagerPosition).getSuggestionsText());
        if (suggestionsArray.length>1) {

            Log.d("suggestionsArray", Integer.toString(suggestionsArray.length));

            final ListView pagerListView = itemView.findViewById(R.id.pager_list_view);
            //pagerListView.setVisibility(View.VISIBLE);


            //Log.d("suggestsionsString", pagerArrayList.get(position).);
            suggestionsList = new ArrayList<String>();
            for (int x = 0; x < suggestionsArray.length; x++) {
                suggestionsList.add(new String(suggestionsArray[x]));







            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_single_choice, suggestionsList);


            pagerListView.setAdapter(adapter);

            pagerListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            int checkedItem = pagerArrayList.get(pagerPosition).getSelection();

            if (checkedItem != -1) {
                pagerListView.setItemChecked(checkedItem, true);
            }



            pagerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    pagerArrayList.get(pagerPosition).setSelection(position);
                    dataEntrylistener.selection(adapter.getItem(position));
                    dataEntrylistener.selectionId(position);


                }
            });
        }
        }

        (container).addView(itemView);


        return itemView;




    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }




    public interface DataEntrylistener {


        void selectionId (int selectionId);
        void selection (String suggestion);



    }





}

