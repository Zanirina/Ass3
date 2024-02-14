package org.example;

import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentNavigableMap;


public class Main {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";
    public static void main(String[] args) throws Exception {


        String URL = "jdbc:postgresql://localhost:5432/visitors";
        String USER = "postgres";
        String PASS = "zarina0511";

        Connection connection = DriverManager.getConnection(URL, USER, PASS);


        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println(ANSI_CYAN+ "--- Hotel manage system"+ ANSI_RESET);
            System.out.println("1. Show all visitors");
            System.out.println("2. Show personal information");
            System.out.println("3. Add new visitor");
            System.out.println("4. The visitor leaves the hotel");
            System.out.println("5. Update personal info");
            System.out.println("6. Start new day");
            System.out.println();
            System.out.print("Enter your choice: ");
            int choice = scan.nextInt();

            if (choice == 1) {
                showallvis(connection);
            } else if (choice == 2) {
                showperinf(connection);
            } else if (choice == 3) {
                addguests(connection);
            } else if (choice == 4) {
                int visle = deleteguest(connection);
            } else if (choice == 5) {
                System.out.println(ANSI_BLUE+"– Change number of nights left"+ANSI_RESET);
                System.out.print("Enter ID: ");
                int id = scan.nextInt();
                System.out.print("How many days to add: ");
                int addday = scan.nextInt();
                upddayleft(connection, id, addday);
            } else if (choice == 6) {
                newday(connection);
            }


        }
    }

    public static void showallvis(Connection connection) throws Exception {
        Statement stat = connection.createStatement();
        ResultSet res = stat.executeQuery("select * from guests");
        System.out.println(ANSI_GREEN+"– All visitors: "+ANSI_RESET);
        while (res.next()) {
            System.out.print(res.getInt("id") + " ");
            System.out.print(res.getString("name") + " ");
            System.out.println(res.getString("surname") + " ");
            System.out.println("Number of person: " + res.getInt("numberofguests") + " ");
            System.out.println("Total number of nights: " + res.getString("paidNumberOfNights") + " ");
            System.out.println("Number of nights left: " + res.getString("nightsleft") + " ");
            System.out.println("Type of room: " + res.getString("typeOfroom") + " ");
            System.out.println();
        }
        System.out.println();
    }

    public static void showperinf(Connection connection) throws Exception {

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter ID: ");
        int chid = scan.nextInt();
        System.out.println(ANSI_GREEN+"– Personal Information:  "+ANSI_RESET);
        Statement stat = connection.createStatement();
        String lisql = "select * from guests where id = ?";
        PreparedStatement pstat = connection.prepareStatement(lisql);
        pstat.setInt(1, chid);

        ResultSet res = pstat.executeQuery();
        while (res.next()) {
            System.out.print(res.getInt("id") + " ");
            System.out.print(res.getString("name") + " ");
            System.out.println(res.getString("surname") + " ");
            System.out.println("Number of person: " + res.getInt("numberofguests") + " ");
            System.out.println("Total number of nights: " + res.getString("paidNumberOfNights") + " ");
            System.out.println("Number of nights left: " + res.getString("nightsleft") + " ");
            System.out.println("Type of room: " + res.getString("typeOfroom") + " \n");

        }
        System.out.println();

    }

    public static void addguests(Connection connection) throws Exception {
        System.out.println(ANSI_YELLOW+"– Add visitor: "+ANSI_RESET);
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a name: ");
        String nam = scan.next();
        System.out.print("Enter a surname: ");
        String snam = scan.next();
        System.out.print("Enter a number of guests: ");
        int num = scan.nextInt();
        System.out.print("Enter a total day: ");
        int tot = scan.nextInt();
        System.out.print("Enter a type of room: ");
        String typ = scan.next();
        int nl = 0;
        String sql = "insert into guests(name, surname, numberofguests, paidNumberOfNights, nightsleft, typeOfroom) values(?, ?, ?, ?, ?, ?)";
        PreparedStatement pstat = connection.prepareStatement(sql);
        pstat.setString(1, nam);
        pstat.setString(2, snam);
        pstat.setInt(3, num);
        pstat.setInt(4, tot);
        pstat.setInt(5, tot);
        pstat.setString(6, typ);

        pstat.executeUpdate();
    }

    public static int deleteguest(Connection connection) throws Exception {
        int counter = 0;
        System.out.println(ANSI_RED+"– Delete visitor: "+ANSI_RESET);

        try {
            Scanner scan = new Scanner(System.in);
            Statement stat = connection.createStatement();
            String sql = "delete from guests where nightsleft = 0";
            PreparedStatement pstat = connection.prepareStatement(sql);
            ResultSet res;
            res = pstat.executeQuery();
            while (res.next()) {
                pstat.executeUpdate();
                counter += 1;
            }
        } catch (PSQLException e) {
            System.out.println("Success! ");
        }
        return counter;

    }

    public static void upddayleft(Connection connection, int id, int adday) throws Exception {

        PreparedStatement pstat2 = connection.prepareStatement("update guests set nightsleft = nightsleft + ? where id=?");
        pstat2.setInt(2, id);
        pstat2.setInt(1, adday);
        pstat2.executeUpdate();

        String sesql = "update guests set paidNumberOfNights = paidNumberOfNights + ? where id=?";
        PreparedStatement pstat = connection.prepareStatement(sesql);
        pstat.setInt(1, adday);
        pstat.setInt(2, id);

        pstat.executeUpdate();

    }

    public static void newday(Connection connection) throws Exception {
        Statement stst = connection.createStatement();
        stst.executeUpdate("update guests set nightsleft = nightsleft-1");
        System.out.println("New day has been started\n");

    }
}