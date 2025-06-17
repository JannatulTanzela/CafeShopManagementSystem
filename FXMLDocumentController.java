/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package cafeshop;

import java.net.URL;

import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public abstract class FXMLDocumentController implements Initializable {
    
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
    private ComboBox<?> question;
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
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    
    
    @FXML
    public void switchForm(ActionEvent event){
        
        TranslateTransition slider = new TranslateTransition();
        if(event.getSource() == sideCreateBtn){
            slider.setNode(sideForm);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));
            
            slider.setOnFinished((ActionEvent e) ->{
            alreadyHave.setVisible(true);
            sideCreateBtn.setVisible(false);
            
        });
            
            slider.play();         
        }
        
        else if (event.getSource() == alreadyHave){
            slider.setNode(sideForm);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));
            
            slider.setOnFinished((ActionEvent e) ->{
            alreadyHave.setVisible(false);
            sideCreateBtn.setVisible(true);
            
        });
            
         slider.play();        
    }  
     
}
    
}
