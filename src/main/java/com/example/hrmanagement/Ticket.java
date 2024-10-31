package com.example.hrmanagement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ticket {
    private final StringProperty ticketID;
    private final StringProperty date;
    private final StringProperty request;

    public Ticket(String ticketID, String date, String request) {
        this.ticketID = new SimpleStringProperty(ticketID);
        this.date = new SimpleStringProperty(date);
        this.request = new SimpleStringProperty(request);
    }

    public String getTicketID() {
        return ticketID.get();
    }

    public StringProperty ticketIDProperty() {
        return ticketID;
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty ticketDateProperty() {
        return date;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public String getRequest() {
        return request.get();
    }

    public StringProperty ticketRequestProperty() {
        return request;
    }

    public StringProperty requestProperty() {
        return request;
    }
}