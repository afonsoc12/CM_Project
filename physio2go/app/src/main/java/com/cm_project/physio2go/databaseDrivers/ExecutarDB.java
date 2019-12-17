package com.cm_project.physio2go.databaseDrivers;

import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.sql.Connection;
import java.sql.ResultSet;

public class ExecutarDB extends AsyncTask<String, Void, ResultSet> {

    private Connection connection;
    private String query;

    //todo o que e esta class? apagat??
    public ExecutarDB(Connection connection, String query) {
        this.connection = connection;
        this.query = query;
    }

    @Override
    protected ResultSet doInBackground(String... strings) {
        ResultSet resultSet = null;

        try {
            resultSet = connection.prepareStatement(query).executeQuery();
        } catch (Exception e) {

        } finally {
            try {
                connection.close();
            } catch (Exception ex) {

            }
        }
        return resultSet;
    }
}
