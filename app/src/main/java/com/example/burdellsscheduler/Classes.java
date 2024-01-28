package com.example.burdellsscheduler;

import androidx.annotation.Nullable;

/**
 * This class is used to simulate a class a student might add to their schedule.
 * @author Tianyi Yu
 */
public class Classes {
    private String instructor;
    private String location;
    private boolean[] day = {false, false, false, false, false};
    private String time;
    private String className;
    public Classes(String instructor, String location, String day, String time, String className) {
        this.instructor = instructor;
        this.location = location;
        for (int i = 0; i < 5; i++) {
            if (day.substring(i, i + 1).equals("1")) {
                this.day[i] = true;
            }
        }
        this.time = time;
        this.className = className;
    }
    public Classes(String className) {
        this.className = className;
    }
    public String getClassName() {
        return className;
    }
    public String getInstructor() {
        return instructor;
    }

    public boolean[] getDay() {
        return day;
    }
    public String getTime() {
        return time;
    }
    public String getLocation() {
        return location;
    }
    public void setClassName(String className) {
        if (className != null && className != "") {
            this.className = className;
        }
    }
    public void setInstructor(String instructor) {
        if (instructor != null && instructor != "")
        this.instructor = instructor;
    }
    public void setLocation(String location) {
        if (location != null && location != "") this.location = location;
    }

    public void setDay(boolean[] day) {
        for (int i = 0; i < 5; i++) {
            this.day[i] = day[i];
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return this.className == ((Classes) obj).getClassName();
    }

    public void setTime(String time) {
        if (time != null && location != null) this.time = time;
    }
}
