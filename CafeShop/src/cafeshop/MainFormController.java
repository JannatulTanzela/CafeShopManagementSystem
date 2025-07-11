/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cafeshop;

import static cafeshop.data.username1;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Jannatul Tanzela
 */
public class MainFormController implements Initializable {

    @FXML
    private Button dashboard_btn;
    @FXML
    private Button inventory_btn;
    @FXML
    private Button menu_btn;
    @FXML
    private Button customers_btn;
    @FXML
    private Button logout_btn;
    @FXML
    private AnchorPane main_form;
    @FXML
    private AnchorPane inventory_form;
    @FXML
    private TableView<?> inventory_tableView;
    @FXML
    private TableColumn<?, ?> inventory_col_productID;
    @FXML
    private TableColumn<?, ?> inventory_col_productName;
    @FXML
    private TableColumn<?, ?> inventory_col_type;
    @FXML
    private TableColumn<?, ?> inventory_col_stock;
    @FXML
    private TableColumn<?, ?> inventory_col_price;
    @FXML
    private TableColumn<?, ?> inventory_col_status;
    @FXML
    private TableColumn<?, ?> inventory_col_date;
    @FXML
    private ImageView inventory_imageView;
    @FXML
    private Button inventory_importBtn;
    @FXML
    private Button inventory_addBtn;
    @FXML
    private Button inventory_updateBtn;
    @FXML
    private Button inventory_clearBtn;
    @FXML
    private Button inventory_deleteBtn;
    @FXML
    private Label username1;
    
    
    public void displayUsername(){
        
        String user = data.username1;
        user = user.substring(0,1).toUpperCase() + user.substring(1);
        
        username1.setText(user);
        
        
    }

   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayUsername();
    }    
    
}
