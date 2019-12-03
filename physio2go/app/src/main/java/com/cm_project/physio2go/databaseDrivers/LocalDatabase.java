package com.cm_project.physio2go.databaseDrivers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cm_project.physio2go.classes.Plan;

import java.sql.ResultSet;
import java.util.ArrayList;

import static java.sql.Types.INTEGER;
import static java.sql.Types.NULL;

public class LocalDatabase extends SQLiteOpenHelper {

    //Database Name
    private static final String DATABASE_NAME = "physio2go.db";

    //Database Version
    private static final int SCHEMA = 1;

    //Tables Names
    static final String TABLE_PLANS = "plans";
    static final String TABLE_PLANS_EXERCISES = "plans_exercises";
    static final String TABLE_EXERCISES = "exercises";


    //Database Fields for Plans Table
    static final String ID = "id";
    static final String ID_PATIENT = "id_patient";
    static final String ID_DOCTOR = "id_doctor";
    static final String DATE_START = "date_start";
    static final String DATE_END = "date_end";
    static final String TOTAL_REPS = "total_reps";
    static final String REPS_DONE = "reps_done";
    static final String DESCRIPTION_PLAN = "description";

    //Database Fields for Plans_Exercises Table
    static final String ID_PLAN = "id_plan";
    static final String ID_EXERCISE = "id_exercise";
    static final String REPETITIONS = "repetitions";

    //Database Fields for Exercises Table
    static final String ID_EX = "id_ex";
    static final String NAME = "name";
    static final String BODY_SIDE = "body_side";
    static final String DESCRIPTION_EXERCISE= "description_exercise";


    SQLiteDatabase database;

    private String name;
    ContentValues cv = new ContentValues();
    Cursor cursor=null;

    public LocalDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, SCHEMA);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_PLANS+" ( "+ID+" INTEGER PRIMARY KEY, "+ID_PATIENT+" VARCHAR, "+ID_DOCTOR+" VARCHAR, "+DATE_START+" DATE, "+DATE_END+" DATE, "+TOTAL_REPS+" INTEGER, "+REPS_DONE+" INTEGER, "+DESCRIPTION_PLAN+" VARCHAR)");
        db.execSQL("CREATE TABLE "+TABLE_PLANS_EXERCISES+" ( "+ID_PLAN+" INTEGER PRIMARY KEY, "+ID_EXERCISE+" INTEGER, "+REPETITIONS+" INTEGER)");
        db.execSQL("CREATE TABLE "+TABLE_EXERCISES+" ( "+ID_EX+" INTEGER PRIMARY KEY, "+NAME+" VARCHAR, "+BODY_SIDE+" VARCHAR, "+DESCRIPTION_EXERCISE+" VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("How did we get here?");
    }


    public void updatePlans(ArrayList<Plan> plansFromServer){

        //delete field from table plans
        database.execSQL("delete from "+ TABLE_PLANS);

        // APAGAR
        cv.put(ID, 1);
        cv.put(ID_PATIENT, "2");
        cv.put(ID_DOCTOR, "3");
        cv.put(DATE_START, "4");
        cv.put(DATE_END, "5");
        cv.put(TOTAL_REPS, 10);
        cv.put(REPS_DONE, 22);
        cv.put(DESCRIPTION_PLAN, "hi");
        // ATE AQUI

        database = this.getWritableDatabase();
        database.insert(TABLE_PLANS, null, cv);

        // get data from all plans associated to user
        for (Plan plan : plansFromServer) {

            int db_id = plan.getId();
            String db_id_patient = plan.getId_patient();
            String db_id_doctor = plan.getId_doctor();
            String db_date_start = plan.getDate_start();
            String db_date_end = plan.getDate_end();
            int db_total_reps = plan.getTotal_reps();
            int db_reps_done = plan.getReps_done();
            String db_description = plan.getDescription();

            // insert in content values for update local database table plans
            cv.put(ID, db_id);
            cv.put(ID_PATIENT, db_id_patient);
            cv.put(ID_DOCTOR, db_id_doctor);
            cv.put(DATE_START, db_date_start);
            cv.put(DATE_END, db_date_end);
            cv.put(TOTAL_REPS, db_total_reps);
            cv.put(REPS_DONE, db_reps_done);
            cv.put(DESCRIPTION_PLAN, db_description);

            database = this.getWritableDatabase();
            database.insert(TABLE_PLANS, null, cv);

        }

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

    public ArrayList<Plan> getPlansOfUser(){
        ArrayList<Plan> plans = new ArrayList<>();
        Plan thisPlan;
        Cursor  cursor = database.rawQuery("select * from "+ TABLE_PLANS,null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                //get the fields from every row in table plans
                int id_plan = cursor.getInt(cursor.getColumnIndex(ID));
                String id_patient = cursor.getString(cursor.getColumnIndex(ID_PATIENT));
                String id_doctor = cursor.getString(cursor.getColumnIndex(ID_DOCTOR));
                String date_start = cursor.getString(cursor.getColumnIndex(DATE_START));
                String date_end = cursor.getString(cursor.getColumnIndex(DATE_END));
                int total_reps = cursor.getInt(cursor.getColumnIndex(TOTAL_REPS));
                int reps_done = cursor.getInt(cursor.getColumnIndex(REPS_DONE));
                String description_plan = cursor.getString(cursor.getColumnIndex(DESCRIPTION_PLAN));

                //create a obkect plan with all the fields set
                thisPlan = new Plan();
                thisPlan.setId(id_plan);
                thisPlan.setId_patient(id_patient);
                thisPlan.setId_doctor(id_doctor);
                thisPlan.setDate_start(date_start);
                thisPlan.setDate_end(date_end);
                thisPlan.setTotal_reps(total_reps);
                thisPlan.setReps_done(reps_done);
                thisPlan.setDescription(description_plan);

                plans.add(thisPlan);

                cursor.moveToNext();
            }
        }

        return plans;
    }

}
