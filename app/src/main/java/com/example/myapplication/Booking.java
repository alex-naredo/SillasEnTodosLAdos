package com.example.myapplication;

public class Booking {

    private String name;
    private String email;
    private String selectedDate;
    private String selectedTime;
    private String access;
    private boolean hasSunflowerCard;

    public String location;

    public Booking(String name, String email, String selectedDate, String selectedTime, String access, boolean hasSunflowerCard, String location) {
        this.name = name;
        this.email = email;
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;
        this.access = access;
        this.hasSunflowerCard = hasSunflowerCard;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public String getAccess() {
        return access;
    }

    public boolean hasSunflowerCard() {
        return hasSunflowerCard;
    }

    public String getLocation() {
        return location;
    }
}

