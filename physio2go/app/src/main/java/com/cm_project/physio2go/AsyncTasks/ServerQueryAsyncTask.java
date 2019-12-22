package com.cm_project.physio2go.AsyncTasks;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class ServerQueryAsyncTask extends AsyncTask<Integer, Void, ResultSet> {

    public final static int SELECT_QUERY = 1;
    public final static int UPDATE_QUERY = 2;
    private Connection connection;
    private String query;
    private String url;
    private String user;
    private String pass;


    public ServerQueryAsyncTask(String[] credentials, String query) {
        this.url = credentials[0];
        this.user = credentials[1];
        this.pass = credentials[2];
        this.query = query;
    }

    @Override
    protected ResultSet doInBackground(Integer... ints) {
        ResultSet resultSet = null;
        int type = ints[0];

        switch (type) {
            case SELECT_QUERY:
                try {
                    connect();
                    resultSet = connection.prepareStatement(query).executeQuery();
                    this.connection.close();
                } catch (Exception e) {

                } finally {
                    try {
                        connection.close();
                    } catch (Exception ex) {

                    }
                }
                break;
            case UPDATE_QUERY:
                try {
                    connect();
                    this.connection.createStatement().executeUpdate(query);
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (Exception ex) {

                    }
                }
                break;
            default:
                break;
        }
        return resultSet;
    }

    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(this.url, this.user, this.pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
