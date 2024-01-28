package com.example.burdellsscheduler;

import java.time.LocalDateTime;

public abstract class Events {
    private String label;
    private LocalDateTime time;
    public Events(String label, LocalDateTime time){
        this.label = label;
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getLabel() {
        return label;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
