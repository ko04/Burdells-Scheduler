package com.example.burdellsscheduler;

import java.time.LocalDateTime;

public class Todos extends Events implements Comparable<Todos>{
    private Classes associatedClass;

    public Todos(String label, LocalDateTime time, Classes associatedClass) {
        super(label, time);
        this.associatedClass = associatedClass;
    }

    public Classes getAssociatedClass() {
        return this.associatedClass;
    }

    public void setAssociatedClass(Classes associatedClass) {
        this.associatedClass = associatedClass;
    }

    @Override
    public int compareTo(Todos o) {
        return this.getTime().compareTo(o.getTime());
    }
}
