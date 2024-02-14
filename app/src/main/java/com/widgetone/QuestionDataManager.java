package com.widgetone;

import android.content.Context;
import android.util.Log;

public class QuestionDataManager {
    Context context;

    public QuestionDataManager(Context context) {
        this.context = context;

    }

    public String getQuestion(String questionStringId) {

        String mQuestionStringId = questionStringId;

        String questionString = "";


        Integer questionId = context.getResources().getIdentifier(mQuestionStringId, "string", context.getPackageName());
        questionString = context.getString(questionId);

       /* switch (questionStringId) {


            case "log_one_t":
                questionString = context.getString(R.string.logged_experience_1);
                break;
            case "log_two_t":
                questionString = context.getString(R.string.logged_experience_2);
                break;

            case "log_three_t":
                questionString = context.getString(R.string.logged_experience_3);
                break;

            case "log_four_t":
                questionString = context.getString(R.string.logged_experience_4);
                break;

            case "log_five_t":
                questionString = context.getString(R.string.logged_experience_5);
                break;

            case "log_six_t":
                questionString = context.getString(R.string.logged_experience_6);
                break;

            case "log_seven_t":
                questionString = context.getString(R.string.logged_experience_7);
                break;

            case "log_eight_t":
                questionString = context.getString(R.string.logged_experience_8);
                break;

            case "w_choice_t":
                questionString = context.getString(R.string.widget_choice);
                break;

            case "title_choice_t":
                questionString = context.getString(R.string.title_choice);
                break;

            case "comp_widget_t":
                questionString = context.getString(R.string.comparing_widgets);
                break;

            case "inactivity_t":
                questionString = context.getString(R.string.inactivity);
                break;

            case "general_t_a":
                questionString = context.getString(R.string.general_experience_a);
                break;

            case "general_t_b":
                questionString = context.getString(R.string.general_experience_b);
                break;


        }
        */

        return questionString;
    }

    public String getSuggestions (String questionTitleId) {

        String suggestionsString = "";

        switch (questionTitleId) {

            case "log_one":

                for (int i=1;i<5;i++) {
                    Integer suggestionId = context.getResources().getIdentifier("logged_experience_1_" + i, "string", context.getPackageName());
                    suggestionsString = suggestionsString + context.getString(suggestionId) +",";
                }
                break;
            case "log_two":
                for (int i=1;i<5;i++) {
                    Integer suggestionId = context.getResources().getIdentifier("logged_experience_2_" + i, "string", context.getPackageName());
                    suggestionsString = suggestionsString + context.getString(suggestionId) +",";
                }                break;

            case "log_three":
                for (int i=1;i<6;i++) {
                    Integer suggestionId = context.getResources().getIdentifier("logged_experience_3_" + i, "string", context.getPackageName());
                    suggestionsString = suggestionsString + context.getString(suggestionId) +",";
                }                break;

            case "log_four":
                for (int i=1;i<6;i++) {
                    Integer suggestionId = context.getResources().getIdentifier("logged_experience_4_" + i, "string", context.getPackageName());
                    suggestionsString = suggestionsString + context.getString(suggestionId) +",";
                }

            case "log_five":
                for (int i=1;i<7;i++) {
                    Integer suggestionId = context.getResources().getIdentifier("logged_experience_5_" + i, "string", context.getPackageName());
                    suggestionsString = suggestionsString + context.getString(suggestionId) +",";
                }

            case "log_six":
                for (int i=1;i<4;i++) {
                    Integer suggestionId = context.getResources().getIdentifier("logged_experience_6_" + i, "string", context.getPackageName());
                    suggestionsString = suggestionsString + context.getString(suggestionId) +",";
                }
            case "log_seven":
                for (int i=1;i<7;i++) {
                    Integer suggestionId = context.getResources().getIdentifier("logged_experience_7_" + i, "string", context.getPackageName());
                    suggestionsString = suggestionsString + context.getString(suggestionId) +",";
                }

            case "widget_choice":
                for (int i=1;i<5;i++) {
                    Integer suggestionId = context.getResources().getIdentifier("widget_choice_" + i, "string", context.getPackageName());
                    suggestionsString = suggestionsString + context.getString(suggestionId) +",";
                }

            case "title_choice":
                for (int i=1;i<6;i++) {
                    Integer suggestionId = context.getResources().getIdentifier("title_choice_" + i, "string", context.getPackageName());
                    suggestionsString = suggestionsString + context.getString(suggestionId) +",";
                }

            case "comparing_widgets":
                for (int i=1;i<6;i++) {
                    Integer suggestionId = context.getResources().getIdentifier("comparing_widgets_" + i, "string", context.getPackageName());
                    suggestionsString = suggestionsString + context.getString(suggestionId) +",";
                }

            case "inactivity":
                for (int i=1;i<8;i++) {
                    Integer suggestionId = context.getResources().getIdentifier("comparing_widgets_" + i, "string", context.getPackageName());
                    suggestionsString = suggestionsString + context.getString(suggestionId) +",";
                }

        }

        return suggestionsString;
    }

}
