package com.cm_project.physio2go.databaseDrivers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cm_project.physio2go.classes.Doctor;
import com.cm_project.physio2go.classes.Exercise;
import com.cm_project.physio2go.classes.Patient;
import com.cm_project.physio2go.classes.Plan;

import java.util.ArrayList;

public class LocalDatabase extends SQLiteOpenHelper {

    //Database Name
    private static final String DATABASE_NAME = "physio2go.db";

    //Database Version
    private static final int SCHEMA = 1;

    //Database Fields for Plans_Exercises Table
    static final String PLAN_EX_ID_PLAN = "plan_ex_id_plan";
    static final String PLAN_EX_ID_EXERCISE = "plan_ex_id_exercise";
    static final String PLAN_EX_REPETITIONS = "plan_ex_repetitions";
    //Tables Names
    private static final String TABLE_PLANS = "plans";
    private static final String TABLE_PLANS_EXERCISES = "plans_exercises";
    private static final String TABLE_EXERCISES = "exercises";
    private static final String TABLE_PATIENT = "patient";
    private static final String TABLE_DOCTOR = "doctor";
    private static final String TABLE_TMP_PLAN = "tmp_plan";
    //Database Fields for Plans Table
    private static final String PLAN_ID = "plan_id";
    private static final String PLAN_ID_PATIENT = "plan_id_patient";
    private static final String PLAN_ID_DOCTOR = "plan_id_doctor";
    private static final String PLAN_DATE_START = "plan_date_start";
    private static final String PLAN_DATE_END = "plan_date_end";
    private static final String PLAN_TOTAL_REPS = "plan_total_reps";
    private static final String PLAN_REPS_DONE = "plan_reps_done";
    private static final String PLAN_DESCRIPTION = "plan_description";
    private static final String PLAN_NAME_PLAN = "plan_name";
    //Database Fields for Exercises Table
    private static final String EX_ID_EX = "ex_id_ex";
    private static final String EX_NAME = "ex_name";
    private static final String EX_BODY_SIDE = "ex_body_side";
    private static final String EX_DESCRIPTION_EXERCISE = "ex_description_exercise";

    //Database fields for Patient details
    private static final String PAT_USERNAME = "pat_username";
    private static final String PAT_ID_DOCTOR = "pat_id_doctor";
    private static final String PAT_NAME = "pat_name";
    private static final String PAT_SURNAME = "pat_surname";
    private static final String PAT_DOB = "pat_dob";
    private static final String PAT_ADDRESS = "pat_address";
    private static final String PAT_HEIGHT = "pat_height";
    private static final String PAT_WEIGHT = "pat_weight";
    private static final String PAT_CONDITION = "pat_condition";

    //Database fields for Doctor (of the current patient)
    private static final String DOC_USERNAME = "doc_username";
    private static final String DOC_NAME = "doc_name";
    private static final String DOC_SURNAME = "doc_surname";
    private static final String DOC_SPECIALITY = "doc_speciality";
    private static final String DOC_HOSPITAL = "doc_hospital";
    private static final String DOC_BIO = "doc_bio";

    //Database fields for Plans done but not synced with server
    private static final String TMP_PLAN_ID = "tmp_plan_id";

    SQLiteDatabase database;

    public LocalDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, SCHEMA);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //todo : Verificar a FOREIGN KEYS
        db.execSQL("CREATE TABLE " + TABLE_PLANS + " ( " + PLAN_ID + " INTEGER PRIMARY KEY, " + PLAN_ID_PATIENT + " VARCHAR, " + PLAN_ID_DOCTOR + " VARCHAR, " + PLAN_DATE_START + " DATE, " + PLAN_DATE_END + " DATE, " + PLAN_TOTAL_REPS + " INTEGER, " + PLAN_REPS_DONE + " INTEGER, " + PLAN_DESCRIPTION + " VARCHAR, " + PLAN_NAME_PLAN + " VARCHAR)");
        db.execSQL("CREATE TABLE " + TABLE_PLANS_EXERCISES + " ( " + PLAN_EX_ID_PLAN + " INTEGER, " + PLAN_EX_ID_EXERCISE + " INTEGER, " + PLAN_EX_REPETITIONS + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_EXERCISES + " ( " + EX_ID_EX + " INTEGER PRIMARY KEY, " + EX_NAME + " VARCHAR, " + EX_BODY_SIDE + " VARCHAR, " + EX_DESCRIPTION_EXERCISE + " VARCHAR)");
        db.execSQL("CREATE TABLE " + TABLE_PATIENT + " ( " + PAT_USERNAME + " VARCHAR PRIMARY KEY, " + PAT_ID_DOCTOR + " VARCHAR, " + PAT_NAME + " VARCHAR, " + PAT_SURNAME + " VARCHAR, " + PAT_DOB + " VARCHAR, " + PAT_ADDRESS + " VARCHAR, " + PAT_HEIGHT + " FLOAT4, " + PAT_WEIGHT + " FLOAT4, " + PAT_CONDITION + " VARCHAR)");
        db.execSQL("CREATE TABLE " + TABLE_DOCTOR + " ( " + DOC_USERNAME + " VARCHAR PRIMARY KEY, " + DOC_NAME + " VARCHAR, " + DOC_SURNAME + " VARCHAR, " + DOC_SPECIALITY + " VARCHAR, " + DOC_HOSPITAL + " VARCHAR, " + DOC_BIO + " VARCHAR)");
        db.execSQL("CREATE TABLE " + TABLE_TMP_PLAN + " ( " + TMP_PLAN_ID + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("How did we get here?");
    }

    public void delete(){

        database = this.getWritableDatabase();
        database.execSQL("delete from " + TABLE_PLANS);
        database.execSQL("delete from " + TABLE_PLANS_EXERCISES);
        database.execSQL("delete from " + TABLE_EXERCISES);
        database.execSQL("delete from " + TABLE_PATIENT);
        database.execSQL("delete from " + TABLE_DOCTOR);
        //database.execSQL("delete from " + TABLE_TMP_PLAN);
    }

    public void deleteTmpPlanIncrements() {
        database = this.getWritableDatabase();
        database.execSQL("delete from " + TABLE_TMP_PLAN);
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
            cv.put(PLAN_ID, db_id_plan);
            cv.put(PLAN_ID_PATIENT, db_id_patient);
            cv.put(PLAN_ID_DOCTOR, db_id_doctor);
            cv.put(PLAN_DATE_START, db_date_start);
            cv.put(PLAN_DATE_END, db_date_end);
            cv.put(PLAN_TOTAL_REPS, db_total_reps);
            cv.put(PLAN_REPS_DONE, db_reps_done);
            cv.put(PLAN_DESCRIPTION, db_description);
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

        boolean alreadyExists;
        // get data from all plans associated to user
        for (Exercise thisExercise : exercises) {
            ArrayList<Integer> exercises_ids = new ArrayList<Integer>();
            alreadyExists = false;
            Cursor cursor = database.rawQuery("select * from " + TABLE_EXERCISES, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(EX_ID_EX));
                    exercises_ids.add(id);
                    cursor.moveToNext();
                }
            }
            // Check if exercise is already on database
            for (int id : exercises_ids){
                if (id == thisExercise.getId()){
                    alreadyExists = true;
                }
            }
            if (!alreadyExists) {
                database = this.getWritableDatabase();

                // Insert in table Exercise
                cvExercises.put(EX_ID_EX, thisExercise.getId());
                cvExercises.put(EX_NAME, thisExercise.getName());
                cvExercises.put(EX_BODY_SIDE, thisExercise.getBody_side());
                cvExercises.put(EX_DESCRIPTION_EXERCISE, thisExercise.getDescription());

                database.insert(TABLE_EXERCISES, null, cvExercises);

                // Insert in table Plan_Exercise
                cvPlanExercises.put(PLAN_EX_ID_EXERCISE, thisExercise.getId());
                cvPlanExercises.put(PLAN_EX_ID_PLAN, id_plan);
                cvPlanExercises.put(PLAN_EX_REPETITIONS, thisExercise.getRepetitions());

                database.insert(TABLE_PLANS_EXERCISES, null, cvPlanExercises);
            }
        }
    }

    public void updatePatientDetails(Patient patient) {
        ContentValues cvPatient = new ContentValues();
        ContentValues cvDoctor = new ContentValues();

        database = this.getWritableDatabase();

        // Insert in table Patient
        cvPatient.put(PAT_USERNAME, patient.getUsername());
        cvPatient.put(PAT_ID_DOCTOR, patient.getDoctor().getUsername());
        cvPatient.put(PAT_NAME, patient.getName());
        cvPatient.put(PAT_SURNAME, patient.getSurname());
        cvPatient.put(PAT_DOB, patient.getDob());
        cvPatient.put(PAT_ADDRESS, patient.getAddress());
        cvPatient.put(PAT_HEIGHT, patient.getHeight());
        cvPatient.put(PAT_WEIGHT, patient.getWeight());
        cvPatient.put(PAT_CONDITION, patient.getCondition());

        database.insert(TABLE_PATIENT, null, cvPatient);

        // Insert in table Doctor
        cvDoctor.put(DOC_USERNAME, patient.getDoctor().getUsername());
        cvDoctor.put(DOC_NAME, patient.getDoctor().getName());
        cvDoctor.put(DOC_SURNAME, patient.getDoctor().getSurname());
        cvDoctor.put(DOC_SPECIALITY, patient.getDoctor().getSpeciality());
        cvDoctor.put(DOC_HOSPITAL, patient.getDoctor().getHospital());
        cvDoctor.put(DOC_BIO, patient.getDoctor().getBio());

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
                int id_plan = cursor.getInt(cursor.getColumnIndex(PLAN_ID));
                String id_patient = cursor.getString(cursor.getColumnIndex(PLAN_ID_PATIENT));
                String id_doctor = cursor.getString(cursor.getColumnIndex(PLAN_ID_DOCTOR));
                String date_start = cursor.getString(cursor.getColumnIndex(PLAN_DATE_START));
                String date_end = cursor.getString(cursor.getColumnIndex(PLAN_DATE_END));
                int total_reps = cursor.getInt(cursor.getColumnIndex(PLAN_TOTAL_REPS));
                int reps_done = cursor.getInt(cursor.getColumnIndex(PLAN_REPS_DONE));
                String description_plan = cursor.getString(cursor.getColumnIndex(PLAN_DESCRIPTION));
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

    private ArrayList<Exercise> getExercisesOfPlan(int id_plan) {
        ArrayList<Exercise> exercice = new ArrayList<>();
        Exercise thisExercise;
        Cursor cursor = database.rawQuery("select * from " + TABLE_PLANS_EXERCISES + " where " + PLAN_EX_ID_PLAN + " = " + id_plan, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                //get the field id_exercise and repetitions from Plans_Exercise table
                int id_exercise = cursor.getInt(cursor.getColumnIndex(PLAN_EX_ID_EXERCISE));
                int repetitions = cursor.getInt(cursor.getColumnIndex(PLAN_EX_REPETITIONS));
                System.out.println(id_exercise);
                System.out.println(repetitions);

                //search for every field in exercice table
                Cursor cursorExercise = database.rawQuery("select * from " + TABLE_EXERCISES + " where " + EX_ID_EX + " = " + id_exercise, null);
                cursorExercise.moveToFirst();
                //get the fields from every column in exercise table
                String name = cursorExercise.getString(cursorExercise.getColumnIndex(EX_NAME));
                String body_side = cursorExercise.getString(cursorExercise.getColumnIndex(EX_BODY_SIDE));
                String description_exercise = cursorExercise.getString(cursorExercise.getColumnIndex(EX_DESCRIPTION_EXERCISE));

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

    public Patient getPatient() {

        Patient patient = new Patient();
        Doctor doctor = new Doctor();

        String query = String.format("select pt.%s, pt.%s, pt.%s, pt.%s, pt.%s, pt.%s, pt.%s, pt.%s, \n" +
                        "d.%s, d.%s, d.%s, d.%s, d.%s, d.%s \n" +
                        "from %s pt \n" +
                        "left join %s as d \n" +
                        "on pt.%s = d.%s;",
                PAT_NAME, PAT_SURNAME, PAT_USERNAME, PAT_ADDRESS, PAT_CONDITION, PAT_DOB, PAT_HEIGHT, PAT_WEIGHT,
                DOC_USERNAME, DOC_NAME, DOC_SURNAME, DOC_BIO, DOC_HOSPITAL, DOC_SPECIALITY,
                TABLE_PATIENT, TABLE_DOCTOR, PAT_ID_DOCTOR, DOC_USERNAME);

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            // Fill patient properties
            patient.setUsername(cursor.getString(cursor.getColumnIndex(PAT_USERNAME)));
            patient.setName(cursor.getString(cursor.getColumnIndex(PAT_NAME)));
            patient.setSurname(cursor.getString(cursor.getColumnIndex(PAT_SURNAME)));
            patient.setAddress(cursor.getString(cursor.getColumnIndex(PAT_ADDRESS)));
            patient.setCondition(cursor.getString(cursor.getColumnIndex(PAT_CONDITION)));
            patient.setDob(cursor.getString(cursor.getColumnIndex(PAT_DOB)));
            patient.setHeight(cursor.getDouble(cursor.getColumnIndex(PAT_HEIGHT)));
            patient.setWeight(cursor.getDouble(cursor.getColumnIndex(PAT_WEIGHT)));

            // Fill doctor properties
            doctor.setUsername(cursor.getString(cursor.getColumnIndex(DOC_USERNAME)));
            doctor.setName(cursor.getString(cursor.getColumnIndex(DOC_NAME)));
            doctor.setSurname(cursor.getString(cursor.getColumnIndex(DOC_SURNAME)));
            doctor.setBio(cursor.getString(cursor.getColumnIndex(DOC_BIO)));
            doctor.setHospital(cursor.getString(cursor.getColumnIndex(DOC_HOSPITAL)));
            doctor.setSpeciality(cursor.getString(cursor.getColumnIndex(DOC_SPECIALITY)));

            // Assign Doctor to Patient
            patient.setDoctor(doctor);
        }

        return patient;
    }

    public void updatePlanOffline(Plan plan) {
        ContentValues planCV = new ContentValues();

        database = this.getWritableDatabase();

        // Insert in table Patient
        planCV.put(TMP_PLAN_ID, plan.getId());

        database.insert(TABLE_TMP_PLAN, null, planCV);
    }

    public ArrayList<Integer> getOfflinePlans() {
        ArrayList<Integer> planIDs = new ArrayList<>();
        Cursor cursor = database.rawQuery(String.format("select %s from %s;", TMP_PLAN_ID, TABLE_TMP_PLAN), null);
        int thisPlanID;

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                //get the field id_exercise and repetitions from Plans_Exercise table
                thisPlanID = cursor.getInt(cursor.getColumnIndex(TMP_PLAN_ID));
                planIDs.add(thisPlanID);

                cursor.moveToNext();
            }
        }
        return planIDs;
    }


}
