package com.cm_project.physio2go.databaseDrivers;

import android.widget.Toast;

import com.cm_project.physio2go.ExecutarDB;
import com.cm_project.physio2go.classes.Exercise;
import com.cm_project.physio2go.classes.Plan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ServerDatabaseDriver implements Runnable {

    private final String PATIENTS = "patients";
    private final String DOCTORS = "doctors";
    private final String PLANS = "plans";
    private final String EXERCISES = "exercises";
    private final String PLAN_EXERCISES = "plan_exercises";
    private Connection conn;
    private String host = "acosta-server.ddns.net";
    private String db = "physio_db";
    private int port = 5432;
    private String user = "dev_db";
    private String pass = "123dev321";
    private String url = "jdbc:postgresql://%s:%d/%s";

    public ServerDatabaseDriver() {
        this.url = String.format(this.url, this.host, this.port, this.db);
        //this.conectar();
        //this.disconectar();
    }

    @Override
    public void run() {
        try {
            Class.forName("org.postgresql.Driver");
            this.conn = DriverManager.getConnection(this.url, this.user, this.pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void conectar() {
        Thread thread = new Thread(this);
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconectar() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.conn = null;
            }
        }
    }

    public ResultSet select(String query) {
        this.conectar();
        ResultSet resultSet = null;
        try {
            resultSet = new ExecutarDB(this.conn, query).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.disconectar();
        return resultSet;
    }

    public ResultSet executar(String query) {
        this.conectar();
        ResultSet resultSet = null;
        try {
            resultSet = new ExecutarDB(this.conn, query).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.disconectar();
        return resultSet;
    }

    public String getPasswordForUsername(String username) {
        this.conectar();
        String query = String.format("SELECT password FROM %s WHERE username = '%s';", PATIENTS, username);
        ResultSet resultSet = this.select(query);

        String passwordDB = null;
        try {
            while (resultSet.next()) {
                passwordDB = resultSet.getString("password");
                this.disconectar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return passwordDB;
    }

    /**
     * Retrieves all plans for the specified user
     *
     * @param username
     * @return
     */
    public ArrayList<Plan> getPlansOfUser(String username) {

        this.conectar();

        ArrayList<Plan> plans = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();
        Plan thisPlan;

        String query = String.format("select * from %s where id_patient = '%s'", PLANS, username);
        ResultSet resultSet = this.select(query);

        int id_plan;
        try {
            while (resultSet.next()) {
                thisPlan = new Plan();
                id_plan = resultSet.getInt("id");
                exercises = getExercisesOfPlan(id_plan);

                thisPlan.setId(id_plan);
                thisPlan.setId_patient(resultSet.getString("id_patient"));
                thisPlan.setId_doctor(resultSet.getString("id_doctor"));
                thisPlan.setDate_start(resultSet.getString("date_start"));
                thisPlan.setDate_end(resultSet.getString("date_end"));
                thisPlan.setTotal_reps(resultSet.getInt("total_reps"));
                thisPlan.setReps_done(resultSet.getInt("reps_done"));
                thisPlan.setDescription(resultSet.getString("description"));
                thisPlan.setExercises(exercises);

                plans.add(thisPlan);
            }

            this.disconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.disconectar();

        return plans;
    }

    private ArrayList<Exercise> getExercisesOfPlan(int plan_id) {
        this.conectar();

        ArrayList<Exercise> exercises = new ArrayList<>();
        Exercise thisExercise;

        String query = String.format("select pe.id_plan, pe.id_exercise, pe.repetitions, e.* from %s pe " +
                "join %s as e on pe.id_exercise = e.id " +
                "where pe.id_plan = %s;", PLAN_EXERCISES, EXERCISES, plan_id);

        ResultSet resultSet = this.select(query);

        try {
            while (resultSet.next()) {
                thisExercise = new Exercise();

                thisExercise.setId(resultSet.getInt("id_exercise"));
                thisExercise.setBody_side(resultSet.getString("body_side"));
                thisExercise.setName(resultSet.getString("name"));
                thisExercise.setDescription(resultSet.getString("description"));
                thisExercise.setRepetitions(resultSet.getInt("repetitions"));

                /*thisExercise.setId_patient(resultSet.getString("id_patient"));
                thisExercise.setId_doctor(resultSet.getString("id_doctor"));
                thisExercise.setDate_start(resultSet.getString("date_start"));
                thisExercise.setDate_end(resultSet.getString("date_end"));
                thisExercise.setTotal_reps(resultSet.getInt("total_reps"));
                thisExercise.setReps_done(resultSet.getInt("reps_done"));
                thisExercise.setDescription(resultSet.getString("description"));
*/
                exercises.add(thisExercise);
            }

            this.disconectar();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return exercises;
    }
}
