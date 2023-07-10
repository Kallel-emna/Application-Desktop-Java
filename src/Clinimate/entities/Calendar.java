package Clinimate.entities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Clinimate.entities.User;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author youfo
 */
public class Calendar {

    public int id;
    public String title;
    public LocalDate start;
    public LocalDate end;
    public String description;
    public Boolean all_day;

    String backgroundColor;
    String borderColor;
    String textColor;
    public User user;

    public Calendar(int id, String title, LocalDate start, LocalDate end, String description, String backgroundColor, String borderColor, String textColor) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.description = description;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
    }

    public Calendar(String title, LocalDate start, LocalDate end, String description, Boolean all_day, String backgroundColor, String borderColor, String textColor, User user) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.description = description;
        this.all_day = all_day;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
        this.user = user;
    }

    public Calendar(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Calendar() {
    }

    public Calendar(User user, String title, LocalDate start, LocalDate end, String description, Boolean all_day) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.description = description;
        this.all_day = all_day;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getAll_day() {
        return all_day;
    }

    public void setAll_day(Boolean all_day) {
        this.all_day = all_day;
    }

    @Override
    public String toString() {
        return "Calendar{" + "id=" + id + ", title=" + title + ", start=" + start + ", end=" + end + ", description=" + description + ", backgroundColor=" + backgroundColor + ", borderColor=" + borderColor + ", textColor=" + textColor + '}';
    }

}
