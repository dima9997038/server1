package server;

import java.sql.*;


public class Db {
    private final static String URL="jdbc:mysql://localhost:3306/chat?serverTimezone=UTC&useSSL=false";
    private final static String USERNAME="root";
    private final static String PASSWORD="root";
    private static  Connection connection;
    private static Statement statement;

   public static boolean checkLogin (String msg){
       boolean result=false;
       ResultSet resultSet;
       String[] data = msg.split(": ", 2);
       System.out.println("Name = "+data[0]);
       System.out.println("Pass = "+data[1]);
       String query = "select * from clients";
       try {
           Class.forName("com.mysql.jdbc.Driver");
           connection= DriverManager.getConnection(URL,USERNAME,PASSWORD);
           if (!connection.isClosed()){
               System.out.println("Connected");
           }
           statement= connection.createStatement();
           resultSet=statement.executeQuery(query);
           System.out.println(msg);
           while (resultSet.next()) {
               if (resultSet.getString(2).equalsIgnoreCase(data[0])&&resultSet.getString(3).equals(data[1])){
                   result =true;
               }
               System.out.println(resultSet.getInt(1) + resultSet.getString(2)+ resultSet.getString(3));
           }

       }
       catch (SQLException e){
           System.out.println("Error");
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       }
       return result;
   }


    public static void main(String args[]) {


        ResultSet resultSet;
        String query = "select count(*) from clients";

        try {
           Class.forName("com.mysql.jdbc.Driver");
            connection= DriverManager.getConnection(URL,USERNAME,PASSWORD);
            if (!connection.isClosed()){
                System.out.println("Connected");
            }
            statement= connection.createStatement();
            resultSet=statement.executeQuery(query);
            while (resultSet.next()) {
                int count = resultSet.getInt(1);
                System.out.println("Total number of clients in the table : " + count);
            }

        }
        catch (SQLException e){
            System.out.println("Error");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


}
