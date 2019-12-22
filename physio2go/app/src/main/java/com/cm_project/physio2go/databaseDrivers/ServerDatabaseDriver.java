package com.cm_project.physio2go.databaseDrivers;

import com.cm_project.physio2go.AsyncTasks.ServerQueryAsyncTask;
import com.cm_project.physio2go.LoginActivity;
import com.cm_project.physio2go.classes.Doctor;
import com.cm_project.physio2go.classes.Exercise;
import com.cm_project.physio2go.classes.Patient;
import com.cm_project.physio2go.classes.Plan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    /**
     * Performs SELECT SQL queries inside asynchronous tasks.
     *
     * @param query
     * @return
     */
    private ResultSet selectQuery(String query) {
        this.conectar();
        ResultSet resultSet = null;
        try {
            resultSet = this.conn.prepareStatement(query).executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            this.disconectar();
        }
        this.disconectar();
        return resultSet;
    }

    /**
     * Performs SELECT SQL queries asynchronously, using an AsyncTask.
     *
     * @param query
     * @return
     */
    public ResultSet selectAsyncQuery(String query) {
        ResultSet resultSet = null;
        try {
            String[] credentials = {this.url, this.user, this.pass};
            resultSet = new ServerQueryAsyncTask(credentials, query)
                    .execute(ServerQueryAsyncTask.SELECT_QUERY)
                    .get();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * Performs UPDATE or INSERT SQL queries inside asynchronous tasks.
     * @param query
     */
    private void updateQuery(String query) {
        this.conectar();
        try {
            this.conn.createStatement().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.disconectar();
    }

    /**
     * Performs UPDATE or INSERT SQL queries asynchronously, using an AsyncTask.
     *
     * @param query
     * @return
     */
    private boolean updateAsyncQuery(String query) {

        boolean status;
        try {
            String[] credentials = {this.url, this.user, this.pass};
            new ServerQueryAsyncTask(credentials, query)
                    .execute(ServerQueryAsyncTask.UPDATE_QUERY);
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }

        return status;
    }

    /**
     * Returns the Doctor object, provided the doctor ID.
     *
     * @param id_doctor
     * @return
     */
    private Doctor getThisPatientDoctor(String id_doctor, String type) {

        Doctor doctor = new Doctor();

        String query = String.format("select * from %s where username = '%s'", DOCTORS, id_doctor);

        ResultSet resultSet;
        // To avoid AsyncTask inside AsyncTask
        if (type.equals("async")) {
            resultSet = this.selectAsyncQuery(query);
        } else {
            resultSet = this.selectQuery(query);
        }

        try {
            while (resultSet.next()) {
                doctor.setUsername(resultSet.getString("username"));
                doctor.setName(resultSet.getString("name"));
                doctor.setSurname(resultSet.getString("surname"));
                doctor.setSpeciality(resultSet.getString("speciality"));
                doctor.setHospital(resultSet.getString("hospital"));
                doctor.setBio(resultSet.getString("bio"));
            }
        } catch (Exception e) {
            doctor = null;
            e.printStackTrace();
        }

        return doctor;
    }

    /**
     * Returns the password for the provided username. Returns NULL if the username is not found.
     *
     * @param username
     * @return
     */
    private String getPasswordForUsername(String username) {
        String query = String.format("SELECT password FROM %s WHERE username = '%s';", PATIENTS, username);
        ResultSet resultSet = this.selectQuery(query);

        String passwordDB = null;
        try {
            while (resultSet.next()) {
                passwordDB = resultSet.getString("password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return passwordDB;
    }

    /**
     * Returns the Exercises that belong to the specified plan ID.
     *
     * @param plan_id
     * @return
     */
    private ArrayList<Exercise> getExercisesOfPlan(int plan_id, String type) {
        ArrayList<Exercise> exercises = new ArrayList<>();
        Exercise thisExercise;

        String query = String.format("select pe.id_plan, pe.id_exercise, pe.repetitions, e.* from %s pe " +
                "join %s as e on pe.id_exercise = e.id " +
                "where pe.id_plan = %s;", PLAN_EXERCISES, EXERCISES, plan_id);

        ResultSet resultSet;
        // To avoid AsyncTask inside AsyncTask
        if (type.equals("async")) {
            resultSet = this.selectAsyncQuery(query);
        } else {
            resultSet = this.selectQuery(query);
        }

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return exercises;
    }

    /**
     * Inserts a patient into the database, when the register finishes.
     *
     * @param newPatient
     */
    public void insertNewPatient(Patient newPatient) {
        String query = String.format("INSERT INTO %s (username,id_doctor,password,name,surname,dob,address,height,weight,condition) VALUES " +
                        "('%s','%s','%s','%s','%s','%s','%s',%s,%s,'%s');", PATIENTS, newPatient.getUsername(), newPatient.getDoctor().getUsername(), newPatient.getPassword(),
                newPatient.getName(), newPatient.getSurname(), newPatient.getDob(), newPatient.getAddress(), newPatient.getHeight(), newPatient.getWeight(), newPatient.getCondition());

        updateQuery(query);
    }

    /**
     * Checks the login combination and returns a result code.
     *
     * @param username
     * @param password
     * @return
     */
    public int checkLoginCombination(String username, String password) {
        int loginCode;

        //todo MUDAR ISTO
        boolean isNetAvailable = true;
        //boolean isNetAvailable = MainActivity.isNetworkAvilable(this);

        if (isNetAvailable) {
            try {
                String passwordDB = this.getPasswordForUsername(username);
                System.out.println(passwordDB);

                if (password.equals(passwordDB)) {
                    loginCode = LoginActivity.LOGIN_OK;
                } else if (passwordDB == null) {
                    loginCode = LoginActivity.LOGIN_USER_NOT_FOUND;
                } else if (!password.equals(passwordDB)) {
                    loginCode = LoginActivity.LOGIN_WRONG_PASSWORD;
                } else {
                    loginCode = LoginActivity.LOGIN_CONNECTION_FAILED;
                }

            } catch (Exception e) {
                loginCode = LoginActivity.LOGIN_CONNECTION_FAILED;
                e.printStackTrace();
            }
        } else {
            loginCode = LoginActivity.LOGIN_NO_INTERNET_CONNECTION;
        }

        return loginCode;
    }

    /**
     * Retrieves all plans for the specified user.
     * @param username
     * @param type
     * @return
     */
    public ArrayList<Plan> getPlansOfUser(String username, String type) {
        ArrayList<Plan> plans = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();
        Plan thisPlan;

        String query = String.format("select * from %s where id_patient = '%s'", PLANS, username);

        ResultSet resultSet;
        // To avoid AsyncTask inside AsyncTask
        if (type.equals("async")) {
            resultSet = this.selectAsyncQuery(query);
        } else {
            resultSet = this.selectQuery(query);
        }

        int id_plan;
        try {
            while (resultSet.next()) {
                thisPlan = new Plan();
                id_plan = resultSet.getInt("id");
                exercises = getExercisesOfPlan(id_plan, type);

                thisPlan.setId(id_plan);
                thisPlan.setId_patient(resultSet.getString("id_patient"));
                thisPlan.setId_doctor(resultSet.getString("id_doctor"));
                thisPlan.setDate_start(resultSet.getString("date_start"));
                thisPlan.setDate_end(resultSet.getString("date_end"));
                thisPlan.setTotal_reps(resultSet.getInt("total_reps"));
                thisPlan.setReps_done(resultSet.getInt("reps_done"));
                thisPlan.setDescription(resultSet.getString("description"));
                thisPlan.setPlan_name(resultSet.getString("plan_name"));
                thisPlan.setExercises(exercises);

                plans.add(thisPlan);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return plans;
    }

    /**
     * Returns all doctors available in the database.
     *
     * @return
     */
    public ArrayList<Doctor> listOfDoctors() {
        ArrayList<Doctor> doctors = new ArrayList<>();
        Doctor thisDoctor;

        String query = String.format("select * from %s", DOCTORS);

        ResultSet resultSet = this.selectAsyncQuery(query);

        try {
            while (resultSet.next()) {
                thisDoctor = new Doctor();

                thisDoctor.setUsername(resultSet.getString("username"));
                thisDoctor.setSurname(resultSet.getString("surname"));
                thisDoctor.setName(resultSet.getString("name"));
                thisDoctor.setSpeciality(resultSet.getString("speciality"));
                thisDoctor.setHospital(resultSet.getString("hospital"));
                thisDoctor.setBio(resultSet.getString("bio"));

                doctors.add(thisDoctor);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return doctors;
    }

    /**
     * Checks if the username provided is already registered.
     *
     * @param username
     * @return
     */
    public boolean usernameExists(String username) {

        boolean exist = false;

        String query = String.format("selectQuery username from %s where username = '%s'", PATIENTS, username);

        ResultSet resultSet = this.selectAsyncQuery(query);

        try {

            while (resultSet.next()) {
                String username_exist = resultSet.getString("username");

                if (username_exist.equals(username)) {
                    exist = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return exist;
    }

    /**
     * Retrieves all information about the patient, provided the username.
     *
     * @param username
     * @return
     */
    public Patient getPatientDetails(String username, String type) {

        Patient patient = new Patient();
        Doctor doctor;
        String query = String.format("select * from %s where username = '%s'", PATIENTS, username);
        ResultSet resultSet;
        // To avoid AsyncTask inside AsyncTask
        if (type.equals("async")) {
            resultSet = this.selectAsyncQuery(query);
        } else {
            resultSet = this.selectQuery(query);
        }

        try {
            while (resultSet.next()) {

                String id_doctor = resultSet.getString("id_doctor");
                doctor = getThisPatientDoctor(id_doctor, type);

                patient.setUsername(resultSet.getString("username"));
                patient.setDoctor(doctor);
                patient.setName(resultSet.getString("name"));
                patient.setSurname(resultSet.getString("surname"));
                patient.setDob(resultSet.getString("dob"));
                patient.setAddress(resultSet.getString("address"));
                patient.setHeight(resultSet.getFloat("height"));
                patient.setWeight(resultSet.getFloat("weight"));
                patient.setCondition(resultSet.getString("condition"));
            }

        } catch (Exception e) {
            patient = null;
            e.printStackTrace();
        }

        return patient;
    }

    /**
     * Increments the Plan, when the database is online.
     *
     * @param plan
     */
    public void updatePlan(Plan plan) {
        int planID = plan.getId();
        int newRepsDone = plan.getReps_done() + 1;


        String query = String.format("update %s " +
                "set reps_done = %s " +
                "where id = %s;", PLANS, newRepsDone, planID);
        updateQuery(query);
    }

    /**
     * Migrates the increments to the server, that were previously saved offline.
     * @param planIDs
     * @return
     */
    public boolean incrementOfflinePlans(ArrayList<Integer> planIDs) {
        boolean status = false;
        for (int thisID : planIDs) {
            String query = String.format("update %s " +
                    "set reps_done = reps_done + 1 " +
                    "where id = %s;", PLANS, thisID);
            status = updateAsyncQuery(query);
        }
        return status;
    }
}
