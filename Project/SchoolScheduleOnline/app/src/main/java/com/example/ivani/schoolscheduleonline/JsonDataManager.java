package com.example.ivani.schoolscheduleonline;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JsonDataManager {
    private List<TabRow> tabRowList;
    private String jsonString;
    private String teacherDay;
    private String roomDay;
    private String day;
    private RowDataManager dataManager;

    public JsonDataManager(String jsonString, String teacherDay, String roomDay, String day, RowDataManager dataManager) {
        this.jsonString = jsonString;
        this.teacherDay = teacherDay;
        this.roomDay = roomDay;
        this.day = day;
        this.dataManager = dataManager;
        tabRowList = new ArrayList<>();
    }

    public void parseJson() {
        int shift;
        try {
            JSONArray jsonarray = new JSONArray(jsonString);
            shift = jsonarray.getJSONObject(0).getInt("shift");
            String clockTime;
            String order;
            String name;
            String room;
            String borderColor;
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                name = jsonobject.getString(teacherDay);
                //check if teacher exists in the current tab row
                if (!name.equals("null") && !name.equals("") && !name.equals("0")) {
                    order = jsonobject.getString("order_id");
                    //set clock time based on shift
                    clockTime = dataManager.getCustomClockTimeBasedOnShift(shift, order);
                    name = name.replace("/", "\n");
                    room = jsonobject.getString(roomDay);
                    room = room.replace("/", " / ");
                    borderColor = dataManager.getColorBasedOnRealTime(clockTime, day);
                    tabRowList.add(new TabRow(order, clockTime, name, room, borderColor));
                }
            }

        } catch (JSONException e) {
            Log.d(e.getMessage(),"JSON parsing error");
            e.printStackTrace();
        }

    }

    public List<TabRow> getTabRowList() {
        return this.tabRowList;
    }

}
