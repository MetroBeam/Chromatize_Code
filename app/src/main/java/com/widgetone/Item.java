package com.widgetone;

public class Item {

    private String timeText;
    private String colourText;
    private int backgroundColour;
    private int visibility;

    public Item(String timeText, String colourText) {
        this.timeText = timeText;
        this.colourText = colourText;
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


    public String getTimeText() { return timeText;
    }

    public void setTimeText(String timeText) { this.timeText = timeText;
    }

    public String getColourText() { return colourText;
    }

    public void setColourText(String colourTextText) { this.colourText= colourText;
    }

}
