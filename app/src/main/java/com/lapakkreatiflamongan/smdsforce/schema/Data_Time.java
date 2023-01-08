package com.lapakkreatiflamongan.smdsforce.schema;

public class Data_Time {
    String timeminute,timehour,timeday;

    public String getTimeminute() {
        return timeminute;
    }

    public void setTimeminute(String timeminute) {
        this.timeminute = timeminute;
    }

    public String getTimehour() {
        return timehour;
    }

    public void setTimehour(String timehour) {
        this.timehour = timehour;
    }

    public String getTimeday() {
        return timeday;
    }

    public void setTimeday(String timeday) {
        this.timeday = timeday;
    }

    public Data_Time(String timeminute, String timehour, String timeday) {
        this.timeminute = timeminute;
        this.timehour = timehour;
        this.timeday = timeday;
    }
}
