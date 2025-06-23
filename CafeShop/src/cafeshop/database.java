/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeshop;

import com.mysql.jdbc.Connection;

/**
 *
 * @author Jannatul Tanzela
 */
public class database {

    public static Connection connectDB() {

        try {

            Class.forName("com.mysql.jbdc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/cafe", "root", "");
            return connect;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
