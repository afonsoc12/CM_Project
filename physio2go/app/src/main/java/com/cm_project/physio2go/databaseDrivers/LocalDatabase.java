package com.cm_project.physio2go.databaseDrivers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.cm_project.physio2go.classes.Exercise;
import com.cm_project.physio2go.classes.Patient;
import com.cm_project.physio2go.classes.Plan;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    static final String TABLE_PATIENT = "patient";
    static final String TABLE_DOCTOR = "doctor";


    //Database Fields for Plans Table
    static final String ID = "id";
    static final String ID_PATIENT = "id_patient";
    static final String ID_DOCTOR = "id_doctor";
    static final String DATE_START = "date_start";
    static final String DATE_END = "date_end";
    static final String TOTAL_REPS = "total_reps";
    static final String REPS_DONE = "reps_done";
    static final String DESCRIPTION_PLAN = "description123";
    static final String PLAN_NAME_PLAN = "plan_name";

    //Database Fields for Plans_Exercises Table
    static final String ID_PLAN = "id_plan";
    static final String ID_EXERCISE = "id_exercise";
    static final String REPETITIONS = "repetitions";

    //Database Fields for Exercises Table
    static final String ID_EX = "id_ex";
    static final String NAME = "name";
    static final String BODY_SIDE = "body_side";
    static final String DESCRIPTION_EXERCISE= "description_exercise";

    //Database fields for Patient details
    static final String USERNAME = "username";
    static final String ID_DOCTOR_PATIENT = "id_doctor";
    static final String NAME_PATIENT = "name";
    static final String SURNAME_PATIENT = "surname";
    static final String DOB = "dob";
    static final String ADDRESS = "address";
    static final String HEIGHT = "height";
    static final String WEIGHT = "weight";
    static final String CONDITION = "condition";

    //Database fields for Doctor (of the current patient)
    static final String USERNAME_DOC = "username";
    static final String NAME_DOC = "name";
    static final String SURNAME_DOCTOR = "surname";
    static final String SPECIALITY = "speciality";
    static final String HOSPITAL = "hospital";
    static final String BIO = "bio";

    SQLiteDatabase database;

    private String name;
    Cursor cursor=null;

    public LocalDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, SCHEMA);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //todo : Verificar a FOREIGN KEYS
        db.execSQL("CREATE TABLE " + TABLE_PLANS + " ( " + ID + " INTEGER PRIMARY KEY, " + ID_PATIENT + " VARCHAR, " + ID_DOCTOR + " VARCHAR, " + DATE_START + " DATE, " + DATE_END + " DATE, " + TOTAL_REPS + " INTEGER, " + REPS_DONE + " INTEGER, " + DESCRIPTION_PLAN + " VARCHAR, " + PLAN_NAME_PLAN + " VARCHAR)");
        db.execSQL("CREATE TABLE " + TABLE_PLANS_EXERCISES + " ( " + ID_PLAN + " INTEGER, " + ID_EXERCISE + " INTEGER, " + REPETITIONS + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_EXERCISES + " ( " + ID_EX + " INTEGER PRIMARY KEY, " + NAME + " VARCHAR, " + BODY_SIDE + " VARCHAR, " + DESCRIPTION_EXERCISE + " VARCHAR)");
        db.execSQL("CREATE TABLE " + TABLE_PATIENT + " ( " + USERNAME + " VARCHAR PRIMARY KEY, " + ID_DOCTOR_PATIENT + " VARCHAR, " + NAME_PATIENT + " VARCHAR, " + SURNAME_PATIENT + " VARCHAR, " + DOB + " VARCHAR, " + ADDRESS + " VARCHAR, " + HEIGHT + " FLOAT4, " + WEIGHT + " FLOAT4, " + CONDITION + " VARCHAR)");
        db.execSQL("CREATE TABLE " + TABLE_DOCTOR + " ( " + USERNAME_DOC + " VARCHAR PRIMARY KEY, " + NAME_DOC + " VARCHAR, " + SURNAME_DOCTOR + " VARCHAR, " + SPECIALITY + " VARCHAR, " + HOSPITAL + " VARCHAR, " + BIO + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("How did we get here?");
    }

    public void delete(){

        database.execSQL("delete from " + TABLE_PLANS);
        database.execSQL("delete from " + TABLE_PLANS_EXERCISES);
        database.execSQL("delete from " + TABLE_EXERCISES);
        database.execSQL("delete from " + TABLE_PATIENT);
        database.execSQL("delete from " + TABLE_DOCTOR);
    }


    public void updatePlans(ArrayList<Plan> plansFromServer){

        ArrayList<Exercise> exercises;
        ContentValues cv = new ContentValues();

        //delete field from table plans
        //database.execSQL("delete from "+ TABLE_PLANS);

        database = this.getWritableDatabase();
        //todo : delete
        //database.insert(TABLE_PLANS, null, cv);

        // get data from all plans associated to user
        for (Plan plan : plansFromServer) {

            int db_id_plan = plan.getId();
            updateExercisesOfPlan(plan.getExercises(), db_id_plan);

            String db_id_patient = plan.getId_patient();
            String db_id_doctor = plan.getId_doctor();
            String db_date_start = plan.getDate_start();
            String db_date_end = plan.getDate_end();
            int db_total_reps = plan.getTotal_reps();
            int db_reps_done = plan.getReps_done();
            String db_description = plan.getDescription();
            String db_plan_name = plan.getPlan_name();

            // insert in content values for update local database table plans
            cv.put(ID, db_id_plan);
            cv.put(ID_PATIENT, db_id_patient);
            cv.put(ID_DOCTOR, db_id_doctor);
            cv.put(DATE_START, db_date_start);
            cv.put(DATE_END, db_date_end);
            cv.put(TOTAL_REPS, db_total_reps);
            cv.put(REPS_DONE, db_reps_done);
            cv.put(DESCRIPTION_PLAN, db_description);
            cv.put(PLAN_NAME_PLAN, db_plan_name);

            database = this.getWritableDatabase();
            database.insert(TABLE_PLANS, null, cv);
        }
    }

    private void updateExercisesOfPlan(ArrayList<Exercise> exercises, int id_plan) {

        ContentValues cvExercises = new ContentValues();
        ContentValues cvPlanExercises = new ContentValues();

        //delete field from table plans
        //database.execSQL("delete from " + TABLE_PLANS_EXERCISES);
        //database.execSQL("delete from " + TABLE_EXERCISES);

        database = this.getWritableDatabase();
        //todo: delete
        //database.insert(TABLE_PLANS_EXERCISES, null, cvPlanExercises);
        //database.insert(TABLE_EXERCISES, null, cvExercises);

        // get data from all plans associated to user
        for (Exercise thisExercise : exercises) {

            database = this.getWritableDatabase();

            // Insert in table Exercise
            cvExercises.put(ID_EX, thisExercise.getId());
            cvExercises.put(NAME, thisExercise.getName());
            cvExercises.put(BODY_SIDE, thisExercise.getBody_side());
            cvExercises.put(DESCRIPTION_EXERCISE, thisExercise.getDescription());

            database.insert(TABLE_EXERCISES, null, cvExercises);

            // Insert in table Plan_Exercise
            cvPlanExercises.put(ID_EXERCISE, thisExercise.getId());
            cvPlanExercises.put(ID_PLAN, id_plan);
            cvPlanExercises.put(REPETITIONS, thisExercise.getRepetitions());

            database.insert(TABLE_PLANS_EXERCISES, null, cvPlanExercises);
        }
    }

    public void updatePatientDetails(Patient patient) {
        ContentValues cvPatient = new ContentValues();
        ContentValues cvDoctor = new ContentValues();

        //delete field from table patient
        //database.execSQL("delete from " + TABLE_PATIENT);
        //database.execSQL("delete from " + TABLE_DOCTOR);


        database = this.getWritableDatabase();
        //todo: delete
        //database.insert(TABLE_PLANS_EXERCISES, null, cvPlanExercise);
        //database.insert(TABLE_EXERCISES, null, cvExercise);

        // get data from a patient
        database = this.getWritableDatabase();

        // Insert in table Patient
        cvPatient.put(USERNAME, patient.getUsername());
        cvPatient.put(ID_DOCTOR_PATIENT, patient.getDoctor().getUsername());
        cvPatient.put(NAME_PATIENT, patient.getName());
        cvPatient.put(SURNAME_PATIENT, patient.getSurname());
        cvPatient.put(DOB, patient.getDob());
        cvPatient.put(ADDRESS, patient.getAddress());
        cvPatient.put(HEIGHT, patient.getHeight());
        cvPatient.put(WEIGHT, patient.getWeight());
        cvPatient.put(CONDITION, patient.getCondition());

        database.insert(TABLE_PATIENT, null, cvPatient);

        // Insert in table Doctor
        cvDoctor.put(USERNAME_DOC, patient.getDoctor().getUsername());
        cvDoctor.put(NAME_DOC, patient.getDoctor().getName());
        cvDoctor.put(SURNAME_DOCTOR, patient.getDoctor().getSurname());
        cvDoctor.put(SPECIALITY, patient.getDoctor().getSpeciality());
        cvDoctor.put(HOSPITAL, patient.getDoctor().getHospital());
        cvDoctor.put(BIO, patient.getDoctor().getBio());

        database.insert(TABLE_DOCTOR, null, cvDoctor);
    }

    public ArrayList<Plan> getPlansOfUser(){
        ArrayList<Plan> plans = new ArrayList<>();
        Plan thisPlan;
        Cursor cursor = database.rawQuery("select * from " + TABLE_PLANS, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                ArrayList<Exercise> exercises = new ArrayList<>();
                //get the fields from every row in table plans
                int id_plan = cursor.getInt(cursor.getColumnIndex(ID));
                String id_patient = cursor.getString(cursor.getColumnIndex(ID_PATIENT));
                String id_doctor = cursor.getString(cursor.getColumnIndex(ID_DOCTOR));
                String date_start = cursor.getString(cursor.getColumnIndex(DATE_START));
                String date_end = cursor.getString(cursor.getColumnIndex(DATE_END));
                int total_reps = cursor.getInt(cursor.getColumnIndex(TOTAL_REPS));
                int reps_done = cursor.getInt(cursor.getColumnIndex(REPS_DONE));
                String description_plan = cursor.getString(cursor.getColumnIndex(DESCRIPTION_PLAN));
                String plan_name = cursor.getString(cursor.getColumnIndex(PLAN_NAME_PLAN));
                exercises = getExercisesOfPlan(id_plan);


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
                thisPlan.setPlan_name(plan_name);
                thisPlan.setExercises(exercises);

                plans.add(thisPlan);

                cursor.moveToNext();
            }
        }
        //fixme: nao esta a dar
        // Collections.sort(plans, Comparator.comparingLong(Plan::getId));
        return plans;
    }

    public ArrayList<Exercise> getExercisesOfPlan(int id_plan){
        ArrayList<Exercise> exercice = new ArrayList<>();
        Exercise thisExercise;
        Cursor  cursor = database.rawQuery("select * from "+ TABLE_PLANS_EXERCISES + " where " + ID_PLAN + " = " + id_plan ,null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                //get the field id_exercise and repetitions from Plans_Exercise table
                int id_exercise = cursor.getInt(cursor.getColumnIndex(ID_EXERCISE));
                int repetitions = cursor.getInt(cursor.getColumnIndex(REPETITIONS));
                System.out.println(id_exercise);
                System.out.println(repetitions);

                //search for every field in exercice table
                Cursor  cursorExercise = database.rawQuery("select * from "+ TABLE_EXERCISES + " where " + ID_EX + " = " + id_exercise ,null);
                cursorExercise.moveToFirst();
                //get the fields from every column in exercise table
                String name = cursorExercise.getString(cursorExercise.getColumnIndex(NAME));
                String body_side = cursorExercise.getString(cursorExercise.getColumnIndex(BODY_SIDE));
                String description_exercise = cursorExercise.getString(cursorExercise.getColumnIndex(DESCRIPTION_EXERCISE));

                //create a object plan with all the fields set
                thisExercise = new Exercise();
                thisExercise.setId(id_exercise);
                thisExercise.setName(name);
                thisExercise.setBody_side(body_side);
                thisExercise.setDescription(description_exercise);
                thisExercise.setRepetitions(repetitions);

                // add elements
                exercice.add(thisExercise);

                cursor.moveToNext();
            }
        }
        //fixme : nao funciona
        //Collections.sort(exercice, Comparator.comparingLong(Exercise::getId));
        return exercice;
    }
}
