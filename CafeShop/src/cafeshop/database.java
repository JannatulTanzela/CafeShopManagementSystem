/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeshop;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Jannatul Tanzela
 */
public class database {

    public static Connection connectDB() {

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/cafe", "root", "");
            return connect;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
