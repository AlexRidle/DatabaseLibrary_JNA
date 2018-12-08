package com.researchwork;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Database implements JavaDatabaseAPI {

    private static Database INSTANCE = new Database();
    private String userName = "root";
    private String password = "root";
    private String connectionURL = "jdbc:mysql://localhost:3306/researchwork?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private Database() {
//        System.out.println("// Database.class runned");
    }

    public static void main(String[] args) {
    }

    public static JavaDatabaseAPI getAPI() {
        return INSTANCE;
    }

    @Override
    public void setupDatabase() {
        createDatabase();
        createTables();
    }

    private void createDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", userName, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS researchwork");
//            System.out.println("// Successfully created database.");
        } catch (SQLException e) {
            System.out.println("// Error in method createDatabase().");
        }
    }

    private void createTables() {

        try (Connection connection = DriverManager.getConnection(connectionURL, userName, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("create table if not exists Users (id mediumint not null auto_increment, name text not null, surname text not null, lastname text not null, dicom text not null, research char(5) not null, primary key (id))");
            statement.executeUpdate("create table if not exists Research (id int not null, img1 text not null, img2 text not null, img3 text not null, 3dmodel text not null, txtfile text not null/*, primary key (id)*/)");
//            System.out.println("// Successfully created database tables.");
        } catch (SQLException e) {
            System.out.println("// Error in method createTables().");
        }

    }

    @Override
    public void addToUserTable(String uName, String uSurname, String uLastName, String uDicom, String uResearch) {
        try (Connection connection = DriverManager.getConnection(connectionURL, userName, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("insert into Users (name, surname, lastname, dicom, research) values("
                    + "'" + uName + "'" + ", "
                    + "'" + uSurname + "'" + ", "
                    + "'" + uLastName + "'" + ", "
                    + "'" + uDicom + "'" + ", "
                    + "'" + uResearch + "'" + ")"
            );
//            System.out.println("// Successfully added person into database.");
        } catch (SQLException e) {
            System.out.println("// Error in method addToUserTable().");
        }
    }

    @Override
    public void addPicturesOfUserIDIntoResearchTable(String userId, String img1, String img2, String img3, String model3d, String txtfile) {
        try (Connection connection = DriverManager.getConnection(connectionURL, userName, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("insert into Research (id, img1, img2, img3, 3dmodel, txtfile) values("
                    + "'" + userId + "'" + ", "
                    + "'" + img1 + "'" + ", "
                    + "'" + img2 + "'" + ", "
                    + "'" + img3 + "'" + ", "
                    + "'" + model3d + "'" + ", "
                    + "'" + txtfile + "'" + ")"
            );

//            System.out.println("// Successfully added person\'s research into database.");
        } catch (SQLException e) {
            System.out.println("// Error in method addPicturesOfUserIDIntoResearchTable().");
        }
    }

    @Override
    public void RemoveAllInfoOfID(String userId) {
        try (Connection connection = DriverManager.getConnection(connectionURL, userName, password)) {
            PreparedStatement preparedStatementUsers = connection.prepareStatement("delete from users where id = ?");
            PreparedStatement preparedStatementResearch = connection.prepareStatement("delete from research where id = ?");
            preparedStatementUsers.setString(1, userId);
            preparedStatementResearch.setString(1, userId);
            preparedStatementUsers.executeUpdate();
            preparedStatementResearch.executeUpdate();

//            System.out.println("// Successfully removed person\'s info from database.");
        } catch (SQLException e) {
            System.out.println("// Error in method RemoveAllInfoOfID().");
        }
    }

    @Override
    public String GetElementByIdTableColumn(String userId, String table, String column) {
        String element = null;
        try (Connection connection = DriverManager.getConnection(connectionURL, userName, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("select " + column + " from " + table + " where id = ?");
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                System.out.println(resultSet.getString(column));
                element = resultSet.getString(column);
            }
            return element;

//            System.out.println("// Successfully loaded person\'s element from database.");
        } catch (SQLException e) {
            System.out.println("// Error in method GetElementByIdTableColumn().");
        }
        return null;
    }

    @Override
    public String[] getAllUsersFromUsersTable() {
        ArrayList<String> users = new ArrayList<>();
        String[] result;
        try (Connection connection = DriverManager.getConnection(connectionURL, userName, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from Users");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(resultSet.getString("id") + " "
                        + resultSet.getString("name") + " "
                        + resultSet.getString("surname") + " "
                        + resultSet.getString("lastname")
                );
            }
            result = users.toArray(new String[0]);
            return result;

//            System.out.println("// Successfully loaded list of users from database.");
        } catch (SQLException e) {
            System.out.println("// Error in method getAllUsersFromUsersTable().");
        }
        return null;
    }

    @Override
    public String[] getUserInfoFromTablesByID(String userId) {
        String[] info = null;
        try (Connection connection = DriverManager.getConnection(connectionURL, userName, password)) {
            PreparedStatement preparedStatementUsers = connection.prepareStatement("select * from users where id = ?");
            PreparedStatement preparedStatementResearch = connection.prepareStatement("select * from research where id = ?");
            preparedStatementUsers.setString(1, userId);
            preparedStatementResearch.setString(1, userId);
            ResultSet resultSetUsers = preparedStatementUsers.executeQuery();
            ResultSet resultSetResearch = preparedStatementResearch.executeQuery();

            String name = null;
            String surName = null;
            String lastName = null;
            String dicom = null;
            String research = null;
            String img1 = null;
            String img2 = null;
            String img3 = null;
            String model3d = null;
            String txtfile = null;

            while (resultSetUsers.next()) {
                name = resultSetUsers.getString("name");
                surName = resultSetUsers.getString("surname");
                lastName = resultSetUsers.getString("lastname");
                dicom = resultSetUsers.getString("dicom");
                research = resultSetUsers.getString("research");
            }

            while (resultSetResearch.next()) {
                img1 = resultSetResearch.getString("img1");
                img2 = resultSetResearch.getString("img2");
                img3 = resultSetResearch.getString("img3");
                model3d = resultSetResearch.getString("3dmodel");
                txtfile = resultSetResearch.getString("txtfile");
            }

            info = new String[]{name, surName, lastName, dicom, research, img1, img2, img3, model3d, txtfile};
            return info;

//            System.out.println("// Successfully loaded information about user from tables.");
        } catch (SQLException e) {
            System.out.println("// Error in method getUserInfoFromTablesByID().");
        }
        return null;
    }
}