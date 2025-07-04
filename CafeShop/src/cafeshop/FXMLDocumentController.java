/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package cafeshop;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


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
    @FXML
    private AnchorPane loginForm1;
    @FXML
    private Button loginBtn1;
    
    
    @FXML
    public void loginBtn() {

        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Username/Password");
            alert.showAndWait();
        } else {

            String selectData = "SELECT username, password FROM employee WHERE username = ? and password = ?";

            connect = database.connectDB();

            try {
                
                
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, username.getText());
                prepare.setString(2, password.getText());

                Result = prepare.executeQuery();

                if (Result.next()) {
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login ");
                    alert.showAndWait();
                    
                   Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml")) ;
                   
                   Stage stage = new Stage();
                   Scene scene = new Scene(root);
                   
                   stage.setTitle("Cafe Shop Management System");
                   stage.setMinWidth(1100);
                   stage.setMinHeight(600);
                   
                   stage.setScene(scene);
                   stage.show();
                   
                   loginBtn.getScene().getWindow().hide();
                   
                    
                    
                    
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect username/password ");
                    alert.showAndWait();

                }

            } catch (Exception e) {
                e.printStackTrace();
            };

        }
    }

    @FXML
    public void regBtn() {
        if (username_2.getText().isEmpty() || password_2.getText().isEmpty()
                || question.getSelectionModel().getSelectedItem() == null
                || answer.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {

            String regData = " INSERT INTO employee (username,password,question,answer)"
                    + "VALUES(?,?,?,?)";
            connect = database.connectDB();

            try {
                
                String checkUsername = "SELECT usernameFrom employee WHERE username ='" + username_2.getText() + "'";
                prepare =connect.prepareStatement(checkUsername);
                Result = prepare.executeQuery();
                
                if(Result.next()){
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(username_2.getText()+ " is already taken");
                    alert.showAndWait();
                }else if (password_2.getText().length ()< 8){
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Password,atleast 8 characters are needed");
                    alert.showAndWait();
                }else{
                    
                prepare = (PreparedStatement) connect.prepareStatement(regData);
                prepare.setString(1, username_2.getText());
                prepare.setString(2, password_2.getText());
                prepare.setString(3, (String) question.getSelectionModel().getSelectedItem());
                prepare.setString(4, answer.getText());

                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully registered Acount");
                alert.showAndWait();

                username_2.setText("");
                password_2.setText("");
                question.getSelectionModel().clearSelection();
                answer.setText("");

                TranslateTransition slider = new TranslateTransition();

                slider.setNode(sideForm);
                slider.setToX(0);
                slider.setDuration(Duration.seconds(.5));

                slider.setOnFinished((ActionEvent e) -> {
                    alreadyHave.setVisible(false);
                    sideCreateBtn.setVisible(true);

                });

                slider.play();     
          
                }
                               
            } catch (Exception e) {
                e.printStackTrace();
            };

        }

    }

    private String[] questionList = {"What is your favorite Color?", "What is your favorite food?", "What is your birthdate?"};

    public void regLquestionList() {

        List<String> listQ = new ArrayList<>();

        for (String data : questionList) {
            listQ.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listQ);

        question.setItems(listData);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         regLquestionList();
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
