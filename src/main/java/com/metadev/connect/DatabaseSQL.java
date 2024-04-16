package com.metadev.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSQL {

    private Statement sqlStatement;

    public void setConnection(){
        try{
            String stringCnn = "jdbc:sqlserver://connect-sql-db.database.windows.net:1433;" +
                    "database=Connect_DB;" +
                    "user=connectSQLDB@connect-sql-db;" +
                    "password=metaDev2024;" +
                    "encrypt=true;" +
                    "trustServerCertificate=false;" +
                    "hostNameInCertificate=*.database.windows.net;" +
                    "loginTimeout=30;";
            Connection sqlConnection = DriverManager.getConnection(stringCnn);
            sqlStatement = sqlConnection.createStatement();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void executeUpdate(String sql) throws SQLException {
        sqlStatement.executeUpdate(sql);
    }

    public ResultSet executeQuery(String sql) throws SQLException{
        return sqlStatement.executeQuery(sql);
    }


}
