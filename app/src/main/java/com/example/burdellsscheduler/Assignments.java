package com.example.burdellsscheduler;

import java.time.LocalDateTime;

public class Assignments extends Events {
    private Classes associatedClass;
    public Assignments(String label, LocalDateTime time, Classes associatedClass) {
        super(label, time);
        this.associatedClass = associatedClass;
    }

    @Override
    public LocalDateTime getTime() {
        return super.getTime();
    }

    @Override
    public String getLabel() {
        return super.getLabel();
    }

    public Classes getAssociatedClass() {
        return associatedClass;
    }

    public void setAssociatedClass(Classes associatedClass) {
        this.associatedClass = associatedClass;
    }

    @Override
    public void setLabel(String label) {
        super.setLabel(label);
    }

    @Override
    public void setTime(LocalDateTime time) {
        super.setTime(time);
    }

    public int compareClass(Assignments other) {
        if (other.getAssociatedClass().getClassName().equals(associatedClass.getClassName())) {
            return 0;
        }
        if (associatedClass.getClassName() == null) {
            return -1;
        }
        return associatedClass.getClassName().compareTo(other.getAssociatedClass().getClassName());
    }
    public boolean compareDate(Assignments other) {
        return !this.getTime().isBefore(other.getTime());
    }
}
