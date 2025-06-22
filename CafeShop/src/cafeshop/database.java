/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeshop;

/**
 *
 * @author Jannatul Tanzela
 */
public class database {
    public static Connection connectDB(){
        
        try{
            
            Class.forName("com.mysql.jbdc.Drive");
            
        }catch(Exception e) {e.printStackTrace();}
    }
    
}
