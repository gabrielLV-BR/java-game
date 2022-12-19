package com.application.javagame.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.application.javagame.GameState;

public class DBConnection {
    
    private final String INSERT_SCORE = 
        "INSERT INTO scores (score, name) VALUES(?, ?);";

    private final String GET_SCORES = 
        "SELECT score, name FROM scores";

    private final String CREATE_DATABASE = 
        "CREATE TABLE IF NOT EXISTS scores (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "score INTEGER," +
            "name CHAR(5)" +
        ")";

    public DBConnection() {
        createDatabase();
    }

    private Connection openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc::sqlite:data.db");
        } catch(ClassNotFoundException e) {
            System.out.println("Could not find org.sqlite.JDBC");
            e.printStackTrace();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void createDatabase() {
        try(
            Connection c = openConnection()
        ) {
            Statement statement = c.createStatement();
            statement.execute(CREATE_DATABASE);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertScore(Score score) {
        try(
            Connection c = openConnection()
        ) {
            PreparedStatement statement = c.prepareStatement(INSERT_SCORE);
            statement.setInt(1, score.score);
            statement.setString(2, score.name);
            statement.execute();
            statement.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void printScores() {
        try(
            Connection c = openConnection()
        ) {
            Statement statement = c.createStatement();
            ResultSet result = statement.executeQuery(GET_SCORES);

            while(result.next()) {
                int score = result.getInt("score");
                String name = result.getString("name");

                System.out.println(name + " scored " + score + " points");
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveState(GameState state) {        

    }

    public ArrayList<Score> getScores() {
        ArrayList<Score> scores = new ArrayList<>();

        

        return scores;
    }

}