package com.widgetone;

public class GridItem {
    private int backgroundColour;
    private String dateText;
    private String widget;



    public GridItem (int backgroundColour, String dateText, String widget) {
        this.backgroundColour = backgroundColour;
        this.dateText = dateText;
        this.widget = widget;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(int visibility) {
        this.dateText = dateText;
    }

    public int getColour() {
        return backgroundColour;
    }

    public void setColor(int backgroundColour) {
        this.backgroundColour = backgroundColour;
    }


    public String getwidget() {
        return widget;
    }

    public void setWidget(int backgroundColour) {
        this.widget = widget;
    }


}

