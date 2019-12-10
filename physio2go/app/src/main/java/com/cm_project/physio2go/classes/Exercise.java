package com.cm_project.physio2go.classes;

import java.io.Serializable;

public class Exercise implements Serializable {

    private int id;
    private String name;
    private String body_side;
    private String description;
    private int repetitions; // Este repetitions vem da tabela plan_exercise e nao da exercise

    public Exercise() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody_side() {
        return body_side;
    }

    public void setBody_side(String body_side) {
        this.body_side = body_side;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }
}
