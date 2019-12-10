package com.cm_project.physio2go.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Plan implements Serializable {

    private int id;
    private String id_patient;
    private String id_doctor;
    private String date_start;
    private String date_end;
    private int total_reps;
    private int reps_done;
    private String description;
    private ArrayList<Exercise> exercises;

    public Plan() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_patient() {
        return id_patient;
    }

    public void setId_patient(String id_patient) {
        this.id_patient = id_patient;
    }

    public String getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(String id_doctor) {
        this.id_doctor = id_doctor;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public int getTotal_reps() {
        return total_reps;
    }

    public void setTotal_reps(int total_reps) {
        this.total_reps = total_reps;
    }

    public int getReps_done() {
        return reps_done;
    }

    public void setReps_done(int reps_done) {
        this.reps_done = reps_done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }
}


