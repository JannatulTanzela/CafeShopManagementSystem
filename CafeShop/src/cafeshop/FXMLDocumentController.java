/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package cafeshop;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 *
 * @author Jannatul Tanzela
 */
public class FXMLDocumentController implements Initializable {

    private Label label;
    @FXML
    private AnchorPane loginForm;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginBtn;
    @FXML
    private Hyperlink forgotpass;
    @FXML
    private TextField username_2;
    @FXML
    private PasswordField password_2;
    @FXML
    private ComboBox<String> question;
    @FXML
    private TextField answer;
    @FXML
    private Button signup_Btn;
    @FXML
    private AnchorPane sideForm;
    @FXML
    private Button sideCreateBtn;

    @FXML
    private Button alreadyHave;
    @FXML
    private AnchorPane signupform;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet Result;
    
    private Alert alert;
    
    public void regBtn(){
        if(username_2.getText().isEmpty() || password_2.getText().isEmpty() || 
                 question.getSelectionModel().getSelectedItem() ==null
                || answer.getText().isEmpty()){
            alert = new Alert (AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }
        else{
            
            String regData = " INSERT INTO employee (username,password,question,answer)"
                    + "VALUES(?,?,?,?)";
            connect = database.connectDB();
            
            try{
                
                prepare =  (PreparedStatement) connect.prepareStatement(regData);
                prepare.setString(1,username_2.getText());
                 prepare.setString(2,password_2.getText());
                  prepare.setString(3,(String)question.getSelectionModel().getSelectedItem());
                   prepare.setString(4,answer.getText());
            
            }catch(Exception e) {e.printStackTrace();};
        
        }
        
    }
    

    private String[] questionList  =  {"What is your favorite Color?" , "What is your favorite food?" , "What is your birthdate?"};
    public void regLquestionList(){
       
        List<String> listQ = new ArrayList<>();
        
        for(String data: questionList){
            listQ.add(data);
        }
        
        ObservableList listData = FXCollections.observableArrayList(listQ);
        
        question.setItems(listData);
       
    }        
   


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void switchForm(ActionEvent event) {

        TranslateTransition slider = new TranslateTransition(Duration.seconds(0.5), sideForm);

        if (event.getSource() == sideCreateBtn) {
            slider.setToX(300);

            slider.setOnFinished((ActionEvent e) -> {
                alreadyHave.setVisible(true);
                sideCreateBtn.setVisible(false);
                
                regLquestionList();
            });
        } else if (event.getSource() == alreadyHave) {
            slider.setToX(0);
            slider.setOnFinished((ActionEvent e) -> {
                alreadyHave.setVisible(false);
                sideCreateBtn.setVisible(true);
            });
        }

        // 🔥 This must run for BOTH cases
        slider.play();
    }

}
