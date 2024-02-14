package com.widgetone;

public class Item {

    private String timeText;
    private String colourText;
    private int backgroundColour;
    private int visibility;
    private int date;
    private String questionText;
    private String likertText;
    private boolean likert;
    private int layoutID;
    private String suggestionsText;
    private String titleText;
    private boolean checked;
    private int selection;
    private String qResponse;
    private String addOn;
    private String recordingId;

    public Item(String timeText, String colourText) {
        this.timeText = timeText;
        this.colourText = colourText;
    }

    public Item(String suggestionsText, boolean checked) {
        this.suggestionsText = suggestionsText;
        this.checked = checked;
    }

    public Item(String questionText) {
        this.questionText = questionText;
    }

    public Item(String questionText, String likertText, boolean likert, int layoutId) {
        this.layoutID = layoutId;
        this.questionText = questionText;
        this.likertText = likertText;
        this.likert = likert;
    }

    public Item(String questionText, String suggestionsText, String qResponse, int selection) {
        this.questionText = questionText;
        this.selection= selection;
        this.suggestionsText = suggestionsText;
        this.qResponse = qResponse;
        this.recordingId = recordingId;

    }

    public Item(int backgroundColour, int visibility) {
        this.backgroundColour = backgroundColour;
        this.visibility = visibility;
    }


    public void setBackgroundColour(int backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {

    this.visibility = visibility;
}

    public int getBackgroundColour () {
        return backgroundColour;
    }





    public String getQResponse () {
        return qResponse;
    }

    public void setQResponse (String qResponse) {
        this.qResponse = qResponse;
    }

    public Boolean getChecked () {
        return checked;
    }

    public void setChecked (Boolean checked) {
        this.checked = checked;
    }


    public String getQuestionText() { return questionText;
    }


    public String getSuggestionsText(){
        return suggestionsText;
    }


    public int getSelection() {
        return selection;
    }

    public void setSelection (int selection) {
        this.selection = selection;
    }

}
