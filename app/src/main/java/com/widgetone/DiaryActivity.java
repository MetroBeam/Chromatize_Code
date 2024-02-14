package com.widgetone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DiaryActivity extends AppCompatActivity {


    FileManager fileManager;
    EditText editText;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    Button stopbtn;
   // Button playbtn;
    Button stopplay;

    private ImageView recordView;
    private ImageView confirmButton;
    private ImageView previousButton;
    private  FrameLayout confirmFrame;
    private  FrameLayout previousFrame;
    private RelativeLayout textBox;
    private ImageView emailClientLink;

    LinearLayout audioPanel;
    Context context;


    String confirmStatus = "surveying";
    String lastConfirmStatus = "surveying";


    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    boolean text = false;

    int numOptions = 0;

    private ArrayList<Item> questionArrayList = new ArrayList<Item>();


    private String currentQuestionAsked = "";
    private String currentAnswerReceived ="";
    private int currentQuestionId = 0;
    private int currentAnswerId = 0;

    int timer = 0;
    int nTaps = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_new);



        editText = findViewById(R.id.reflection_text_view);


        previousButton = findViewById(R.id.previous_button);
        previousFrame = findViewById(R.id.previous_button_frame);
        confirmButton = findViewById(R.id.confirm_button);
        confirmFrame = findViewById(R.id.confirm_button_frame);
        textBox = findViewById(R.id.text_box);
        emailClientLink = findViewById(R.id.email_client);


        confirmButton.setBackgroundResource(R.drawable.right);





        final ImageView exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exitIntent = new Intent(DiaryActivity.this, MainActivity.class);
                exitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // TOP
                startActivity(exitIntent);
                finish();
            }
        });


        emailClientLink.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   nTaps++;

                                                   if (nTaps==5) {
                                                       Intent i = new Intent(Intent.ACTION_SEND);
                                                       i.setType("message/rfc822");
                                                       i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"mattsapptest@gmail.com"});
                                                       String recordedColoursString = fileManager.readInternalStringFile("recorded_colours_history");
                                                       String topicsHistoryString = fileManager.readInternalStringFile("topics_history");
                                                       String surveyResponsesString = fileManager.readInternalStringFile("survey_data");
                                                       String colourPresets = fileManager.readInternalStringFile("preset_colours_history");
                                                       String overviewLooks = fileManager.readInternalStringFile("colour_overview");


                                                       String[]recorded_colours_history = recordedColoursString.split("~");
                                                       String subject = recorded_colours_history[recorded_colours_history.length-3];
                                                       i.putExtra(Intent.EXTRA_SUBJECT, subject);
                                                       i.putExtra(Intent.EXTRA_TEXT   ,  "COLOURS" + "~" + recordedColoursString + "~" + "TOPICS" + topicsHistoryString + "~" + "SURVEY" + "~" + surveyResponsesString + "PRESETS" + "~" + colourPresets + "OVERVIEWS" + "~" + overviewLooks);
                                                       try {
                                                           startActivity(Intent.createChooser(i, "Send mail..."));
                                                       } catch (android.content.ActivityNotFoundException ex) {
                                                           Toast.makeText(DiaryActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                                       }

                                                       nTaps = 0;
                                                   }
                                               }
                                           }
        );




    Intent intent = getIntent();



    if (intent.hasExtra("colour_logged")) {



        for (int x = 1; x < 9; x++) {

            String idString = "logged_experience_" + x;
            questionArrayList.add(new Item(getQuestion(idString), getSuggestions(idString), "", -1));

        }
    }


    else if (intent.hasExtra("inactivity")) {


        String idString = "inactivity";
        questionArrayList.add(new Item(getQuestion(idString), getSuggestions(idString), "", -1));
        lastConfirmStatus = "lastQuestion";
        confirmButton.setBackgroundResource(R.drawable.checked_green);

    }

       else if (intent.hasExtra("colours_changed")) {


                    String idString = "colours_choice";
                    questionArrayList.add(new Item(getQuestion(idString), getSuggestions(idString), "", -1));

                    idString = "general_experience_b";
                    questionArrayList.add(new Item(getQuestion(idString), "", "",  -1));

        }




       else if (intent.hasExtra("topic_changed")) {

            String idString = "title_choice";
            questionArrayList.add(new Item(getQuestion(idString), getSuggestions(idString), "",  -1));

            idString = "general_experience_b";
            questionArrayList.add(new Item(getQuestion(idString),"", "",  -1));

        }


      else  if (intent.hasExtra("widget_choice")) {

            String idString = "widget_choice";
            questionArrayList.add(new Item(getQuestion(idString), getSuggestions(idString), "",  -1));

            idString = "general_experience_b";
            questionArrayList.add(new Item(getQuestion(idString), "", "",  -1));

        }


    else  if (intent.hasExtra("comparing_widgets")) {

        String idString = "comparing_widgets";
        questionArrayList.add(new Item(getQuestion(idString), getSuggestions(idString), "",  -1));

        idString = "general_experience_b";
        questionArrayList.add(new Item(getQuestion(idString), "", "",  -1));

    }

        else  {
            String idString = "general_experience_a";

            questionArrayList.add(new Item(getQuestion(idString), "", "",  -1));
            textBox.setVisibility(View.VISIBLE);
            editText.requestFocus();
            editText.setHint("Describe...");
            lastConfirmStatus = "typing";
            confirmButton.setBackgroundResource(R.drawable.checked_green);

        }



        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        final CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(this, questionArrayList, new CustomPagerAdapter.DataEntrylistener() {

            @Override
            public void selectionId(int selectionId) {
                currentAnswerId = selectionId;

                questionArrayList.get(viewPager.getCurrentItem()).setSelection(selectionId);
            }

            @Override
            public void selection(String suggestion) {

                currentAnswerReceived = suggestion;



                    if ((suggestion.equals("Other"))) {

                        textBox.setVisibility(View.VISIBLE);
                        viewPager.clearFocus();
                        editText.requestFocus();
                        editText.setHint("Describe...");

                        String enteredText = questionArrayList.get(viewPager.getCurrentItem()).getQResponse();
                        editText.setText(enteredText);
                        editText.setSelection(editText.getText().length());

                        }




                    else {
                        textBox.setVisibility(View.INVISIBLE);
                    }


                    confirmFrame.setVisibility(View.VISIBLE);
                currentAnswerReceived = suggestion;

            }


        });

        currentQuestionAsked = questionArrayList.get(0).getQuestionText();


        viewPager.setAdapter(customPagerAdapter);


        if (questionArrayList.size()==1) {
            confirmFrame.setVisibility(View.VISIBLE);
        }





        fileManager = new FileManager(this);


        findViewById(R.id.reflection_text_view);





        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                currentQuestionAsked = questionArrayList.get(viewPager.getCurrentItem()).getQuestionText();
                // always set to surveying if it goes backwards
                confirmStatus = "surveying";
                confirmButton.setBackgroundResource(R.drawable.right);
                //always start by hiding keyboard. Is this necessary? or could it be more specific?

                // if typing hide keyboard and reset


                textBox.setVisibility(View.INVISIBLE);
                if (lastConfirmStatus.equals("typing")) {
                    hideKeyboard(DiaryActivity.this);
                    lastConfirmStatus.equals("surveying");
                }


                if (viewPager.getCurrentItem() > 0) {

                    if (currentQuestionAsked.equals(getResources().getString(R.string.logged_experience_8))) {
                        int selection = questionArrayList.get(viewPager.getCurrentItem() - 2).getSelection();
                        if (selection == 0) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                        } else {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() - 2);
                        }
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }


                    if (viewPager.getCurrentItem() == 0) {
                        previousFrame.setVisibility(View.INVISIBLE);
                    }
                }


                    /*String enteredText = questionArrayList.get(viewPager.getCurrentItem()).getQResponse();
                    if (enteredText.length()>0) {
                        editText.setText(enteredText);
                        textBox.setVisibility(View.VISIBLE);
                        editText.setSelection(editText.getText().length());
                        editText.requestFocus();

                    }

                    else {
                        editText.setText("");
                        textBox.setVisibility(View.INVISIBLE);
                    }
                    */


                    if (questionArrayList.get(viewPager.getCurrentItem()).getSelection()!=-1) {
                        String[] selectionsArray = questionArrayList.get(viewPager.getCurrentItem()).getSuggestionsText().split("~");
                        String selectedSuggestion = selectionsArray[questionArrayList.get(viewPager.getCurrentItem()).getSelection()];
                        if (selectedSuggestion.equals("Other")) {

                            String enteredText = questionArrayList.get(viewPager.getCurrentItem()).getQResponse();

                            textBox.setVisibility(View.VISIBLE);
                            editText.setText(enteredText);
                            editText.requestFocus();
                            editText.setSelection(editText.getText().length());
                        } else {
                            editText.setText("");
                            textBox.setVisibility(View.INVISIBLE);

                        }
                    }


                lastConfirmStatus = confirmStatus;

            }

        });





        confirmButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {




                // get the current question before viewpager has moved
                currentQuestionAsked = questionArrayList.get(viewPager.getCurrentItem()).getQuestionText();

                // if we are not on the last page then do the following

                textBox.setVisibility(View.INVISIBLE);



                if (lastConfirmStatus.equals("typing")) {
                    hideKeyboard(DiaryActivity.this);

                    Log.d("indazone", "onClick: ");
                    if (viewPager.getCurrentItem() == questionArrayList.size() - 1) {
                        lastConfirmStatus ="lastQuestion"; }
                    else {
                        lastConfirmStatus= "surveying";
                    }

                }

                    if (viewPager.getCurrentItem() < questionArrayList.size() - 1) {


                        // if its a general experience question, show the

                   /* if ((currentQuestionAsked.equals(getResources().getString(R.string.general_experience_b))))   {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+ 1);
                    }
                    */

                        // if we are on the logged experience question 6 and the aswe is yes then skip a question. if not move to next question

                        if (currentQuestionAsked.equals(getResources().getString(R.string.logged_experience_6))) {

                            if (questionArrayList.get(viewPager.getCurrentItem()).getSelection() == 0) {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            } else {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 2);
                                textBox.setVisibility(View.VISIBLE);
                                editText.requestFocus();
                                editText.setHint("Describe...");
                            }
                        }

                        //If it is not that question move on

                        else {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

                        }

                        // make previous button visible. Do this after the change to the next page.
                        previousFrame.setVisibility(View.VISIBLE);

                    }


                    // resetting after text entered and if its last question directly


                // if in the exceptional case that the system skips a question then show the textbox and set to lastquestion

                currentQuestionAsked = questionArrayList.get(viewPager.getCurrentItem()).getQuestionText();


                if (questionArrayList.get(viewPager.getCurrentItem()).getSelection()!=-1) {
                    String[] selectionsArray = questionArrayList.get(viewPager.getCurrentItem()).getSuggestionsText().split("~");
                    String selectedSuggestion = selectionsArray[questionArrayList.get(viewPager.getCurrentItem()).getSelection()];
                    if (selectedSuggestion.equals("Other")) {

                        String enteredText = questionArrayList.get(viewPager.getCurrentItem()).getQResponse();

                        textBox.setVisibility(View.VISIBLE);
                        editText.setText(enteredText);
                        editText.requestFocus();
                        editText.setSelection(editText.getText().length());
                    }
                    else {
                        textBox.setVisibility(View.INVISIBLE);
                        editText.setText("");
                    }
                }

                else{
                    if (viewPager.getCurrentItem()!=questionArrayList.size()-1) {
                        textBox.setVisibility(View.INVISIBLE);
                        editText.setText("");
                    Log.d("indahooose", "inyamoose");
                    }

                }


                if ((currentQuestionAsked.equals(getResources().getString(R.string.general_experience_b))||(currentQuestionAsked.equals(getResources().getString(R.string.logged_experience_8)))))  {

                    confirmButton.setBackgroundResource(R.drawable.checked_green);
                    confirmStatus = "lastQuestion";
                    textBox.setVisibility(View.VISIBLE);
                    editText.setFocusable(true);
                    editText.requestFocus();
                    editText.setHint("Describe...");
                }


                // if the next page leads to last question then fire through to the last conform status box below.

                if (viewPager.getCurrentItem()==questionArrayList.size()-1) {
                    confirmButton.setBackgroundResource(R.drawable.checked_green);
                    confirmStatus = "lastQuestion"; // changed ths

                }





                if (lastConfirmStatus.equals("lastQuestion")) {




                    String newSurveyData = "";
                    String currentTime =  Calendar.getInstance().getTime().toString();
                    String currentSurveyData = "";
                    currentSurveyData = fileManager.readInternalStringFile("survey_data");


                    for (int x = 0; x< questionArrayList.size();x++) {

                        int selection = questionArrayList.get(x).getSelection();

                        String selectedSuggestion = "";
                        Log.d("lala", questionArrayList.get(x).getSuggestionsText());
                        String[] selectionsArray = questionArrayList.get(x).getSuggestionsText().split("~");

                        if (selection!=-1) {

                                selectedSuggestion = selectionsArray[selection];
                                Log.d("seleectiom", selectedSuggestion);

                        }
                        else {
                            selectedSuggestion = "none";
                        }

                        newSurveyData = newSurveyData + questionArrayList.get(x).getQuestionText() + "~" + selectedSuggestion + "~" + questionArrayList.get(x).getQResponse() +"~";
                    }






                    String surveyDataToSave = currentTime + "~" + newSurveyData + currentSurveyData;
                    fileManager.createInternalStringFile(surveyDataToSave,"survey_data");

                    Toast toast = Toast.makeText(getApplicationContext(), "Thanks! Your answers have been submitted", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent exitIntent = new Intent(DiaryActivity.this, MainActivity.class);
                    exitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // TOP
                    startActivity(exitIntent);
                    finish();


                }

                Log.d("question", questionArrayList.get(viewPager.getCurrentItem()).getQuestionText());
                Log.d("selection", Integer.toString(questionArrayList.get(viewPager.getCurrentItem()).getSelection()));


                lastConfirmStatus = confirmStatus;


            }





        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String enteredText = editText.getText().toString();
                if (enteredText.length()>0) {
                    if (enteredText.charAt(enteredText.length() - 1) == '~') {
                        enteredText = enteredText.replace(enteredText.substring(enteredText.length() - 1), "");
                        editText.setText(enteredText);
                        editText.setSelection(editText.getText().length());
                        Toast.makeText(DiaryActivity.this, "Sorry, you cannot use the ~ symbol", Toast.LENGTH_SHORT).show();

                    }
                }

                questionArrayList.get(viewPager.getCurrentItem()).setQResponse(editText.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        editText.setOnTouchListener(new View.OnTouchListener()

        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                String qResponse = questionArrayList.get(viewPager.getCurrentItem()).getQResponse();
                lastConfirmStatus="typing";
                confirmStatus = "typing";
                confirmFrame.setVisibility(View.VISIBLE);

                return false;
            }
        });



        final CountDownTimer countDownTimer = new CountDownTimer(1000000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                timer++;
                editText.setText("        "+ Integer.toString(timer) + " s");
            }

            @Override
            public void onFinish() {
                editText.setText("");
                timer = 0;

            }
        };










    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                    } else {
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(DiaryActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    
    public String getQuestion(String questionStringId) {

        String mQuestionStringId = questionStringId;
        String questionString = "";


        Integer questionId = this.getResources().getIdentifier(mQuestionStringId, "string", this.getPackageName());
        questionString = this.getString(questionId);

        return questionString;
    }

    public String getSuggestions (String questionTitleId) {

        String suggestionsString = "";
        String mQuestionTitleId = questionTitleId;


        switch (mQuestionTitleId) {

            case "logged_experience_1":

                for (int i=1;i<5;i++) {
                    Integer suggestionId = this.getResources().getIdentifier("logged_experience_1_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";
                }
                numOptions = 4;
                break;
            case "logged_experience_2":
                for (int i=1;i<4;i++) {
                    Integer suggestionId = this.getResources().getIdentifier("logged_experience_2_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";


                }    numOptions = 3;             break;

            case "logged_experience_3":
                for (int i=1;i<6;i++) {
                    Integer suggestionId = this.getResources().getIdentifier("logged_experience_3_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";


                }          numOptions = 5;       break;

            case "logged_experience_4":
                for (int i=1;i<4;i++) {
                    Integer suggestionId = this.getResources().getIdentifier("logged_experience_4_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";

                } numOptions = 3;
                break;

            case "logged_experience_5":
                for (int i=1;i<5;i++) {
                    Integer suggestionId = this.getResources().getIdentifier("logged_experience_5_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";
                } numOptions = 4;
                break;

            case "logged_experience_6":
                for (int i=1;i<4;i++) {
                    Integer suggestionId = this.getResources().getIdentifier("logged_experience_6_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";
                }  numOptions = 3;
                break;
            case "logged_experience_7":
                for (int i=1;i<7;i++) {
                    Integer suggestionId = this.getResources().getIdentifier("logged_experience_7_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";
                }  numOptions = 6;
                break;


            case "widget_choice":
                for (int i=1;i<5;i++) {
                    Integer suggestionId = this.getResources().getIdentifier("widget_choice_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";
                } numOptions = 4;
                break;

            case "colours_choice":
                for (int i=1;i<5;i++) {
                    Integer suggestionId = this.getResources().getIdentifier("colours_choice_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";
                }
                numOptions = 4;
                break;

            case "title_choice":
                for (int i=1;i<5;i++) {
                    Integer suggestionId = this.getResources().getIdentifier("title_choice_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";
                }
                numOptions = 4;
                break;

            case "comparing_widgets":
                for (int i=1;i<4;i++) {
                    Integer suggestionId = this.getResources().getIdentifier("comparing_widgets_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";
                } numOptions = 3;
                break;

            case "inactivity":
                for (int i=1;i<6;i++) {
                    Log.d("inactivity", "getSuggestions: ");
                    Integer suggestionId = this.getResources().getIdentifier("inactivity_" + i, "string", this.getPackageName());
                    suggestionsString = suggestionsString + this.getString(suggestionId) +"~";
                }  numOptions = 5;
                break;


        }
        //Log.d("suggestion strring", suggestionsString);

        return suggestionsString;
    }
 


}










