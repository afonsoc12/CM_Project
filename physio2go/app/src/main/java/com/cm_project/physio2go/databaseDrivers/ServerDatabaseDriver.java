package com.cm_project.physio2go.databaseDrivers;

import android.widget.Toast;

import com.cm_project.physio2go.ExecutarDB;
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
        Plan thisPlan;

        String query = String.format("select * from %s where id_patient = '%s'", PLANS, username);
        ResultSet resultSet = this.select(query);

        try {
            while (resultSet.next()) {
                thisPlan = new Plan();
                thisPlan.setId(resultSet.getInt("id"));
                thisPlan.setId_patient(resultSet.getString("id_patient"));
                thisPlan.setId_doctor(resultSet.getString("id_doctor"));
                thisPlan.setDate_start(resultSet.getString("date_start"));
                thisPlan.setDate_end(resultSet.getString("date_end"));
                thisPlan.setTotal_reps(resultSet.getInt("total_reps"));
                thisPlan.setReps_done(resultSet.getInt("reps_done"));
                thisPlan.setDescription(resultSet.getString("description"));

                plans.add(thisPlan);

                this.disconectar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.disconectar();

        return plans;
    }
}
