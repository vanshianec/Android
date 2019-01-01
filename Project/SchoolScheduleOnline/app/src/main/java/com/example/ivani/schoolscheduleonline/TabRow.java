package com.example.ivani.schoolscheduleonline;

public class TabRow {
    private String order;
    private String clockText;
    private String teacherText;
    private String roomText;
    private String color;

    public TabRow(){

    }

    public TabRow(String order, String clockText, String teacherText, String roomText, String color) {
        this.order = order;
        this.clockText = clockText;
        this.teacherText = teacherText;
        this.roomText = roomText;
        this.color = color;

    }
    public String getColor() {
        return color;
    }

    public String getOrder() {
        return order;
    }

    public String getClockText() {
        return clockText;
    }

    public String getTeacherText() {
        return teacherText;
    }

    public String getRoomText() {
        return roomText;
    }
}
