package com.cm_project.physio2go.databaseDrivers;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cm_project.physio2go.classes.Plan;

import java.util.ArrayList;

public class LocalDatabase {//extends SQLiteOpenHelper {

 /*   public LocalDatabase(SQLiteDatabase db){

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }*/

    public void updatePlans(ArrayList<Plan> plans) {
        // TODO Destruir a bd local e dar update aos plans
        /*Estes campos
            Id
            Id_patient
            Id_doctor
            Date_start
            Date_end
            Total_reps
            reps_done
            Description

            Mais escrever estes exercises numa tabela exercises
            ArrayList<Exercise>
         */

    }
}
