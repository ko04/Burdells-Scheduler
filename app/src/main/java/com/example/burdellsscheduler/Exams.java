package com.example.burdellsscheduler;

import java.time.LocalDateTime;

public class Exams extends Events implements Comparable<Exams>{
    private Classes associatedClass;

    public Exams(String label, LocalDateTime time, Classes associatedClass) {
        super(label, time);
        this.associatedClass = associatedClass;
    }

    @Override
    public int compareTo(Exams o) {
        return this.getTime().compareTo(o.getTime());
    }

    public Classes getAssociatedClass() {
        return this.associatedClass;
    }

    public void setAssociatedClass(Classes associatedClass) {
        this.associatedClass = associatedClass;
    }
}
