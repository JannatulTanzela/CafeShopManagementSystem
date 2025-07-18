package cafeshop;

import static cafeshop.data.username1;
import java.awt.Insets;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainFormController implements Initializable {

    @FXML
    private Button dashboard_btn, inventory_btn, menu_btn, customers_btn, logout_btn;
    @FXML
    private AnchorPane main_form, inventory_form;
    @FXML
    private TableView<productData> inventory_tableView;
    @FXML
    private TableColumn<productData, String> inventory_col_productID, inventory_col_productName, inventory_col_type, inventory_col_stock, inventory_col_price, inventory_col_status, inventory_col_date;
    @FXML
    private ImageView inventory_imageView;
    @FXML
    private Button inventory_importBtn, inventory_addBtn, inventory_updateBtn, inventory_clearBtn, inventory_deleteBtn;
    @FXML
    private Label username1;
    @FXML
    private TextField inventory_productID, inventory_stock, inventory_productName, inventory_price;
    @FXML
    private ComboBox<String> inventory_status, inventory_type;

    private Alert alert;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;
    @FXML
    private ScrollPane menu_scrollPane;
    @FXML
    private GridPane menu_gridPane;
    @FXML
    private TableView<?> menu_tableView;
    @FXML
    private TableColumn<?, ?> menu_col_productName;
    @FXML
    private TableColumn<?, ?> menu_col_quantity;
    @FXML
    private TableColumn<?, ?> menu_col_price;
    @FXML
    private Label menu_total;
    @FXML
    private TextField menu_amount;
    @FXML
    private Label menu_change;
    @FXML
    private Button menu_payBtn;
    @FXML
    private Button menu_removeBtn;
    @FXML
    private Button menu_receiptBtn;

    private ObservableList<productData> cardListData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane dashboard_form;
    @FXML
    private AnchorPane menu_form;

    @FXML
    public void inventoryAddBtn() {
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_type.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || data.path == null || data.path.trim().isEmpty()) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

            //return;
        } else {
            String checkProdID = "SELECT prod_id FROM product WHERE prod_id = '"
                    + inventory_productID.getText() + "'";

            connect = database.connectDB();
        }

        try {
            //int stock = Integer.parseInt(inventory_stock.getText());
            //double price = Double.parseDouble(inventory_price.getText());
            Statement statement = connect.createStatement();
            String checkProdID = null;
            result = statement.executeQuery(checkProdID);
            //prepare = connect.prepareStatement(checkProdID);
            //prepare.setString(1, inventory_productID.getText());
            //result = prepare.executeQuery();

            if (result.next()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText(inventory_productID.getText() + " is already taken");
                alert.showAndWait();
            } else {
                String insertData = "INSERT INTO product "
                        + "(prod_id, prod_name, type, stock, price, status, image, date) "
                        + "VALUES(?,?,?,?,?,?,?,?)";

                prepare = connect.prepareStatement(insertData);
                prepare.setString(1, inventory_productID.getText());
                prepare.setString(2, inventory_productName.getText());
                prepare.setString(3, inventory_type.getSelectionModel().getSelectedItem());
                prepare.setString(4, inventory_stock.getText());
                prepare.setString(5, inventory_price.getText());
                prepare.setString(6, inventory_status.getSelectionModel().getSelectedItem());

                String path = data.path.replace("\\", "\\\\");
                prepare.setString(7, path);

                Date sqlDate = new Date(new java.util.Date().getTime());
                prepare.setDate(8, sqlDate);

                prepare.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();

                inventoryShowData();
                inventoryClearBtn();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void inventoryUpdateBtn() {

        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_type.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || data.path == null || data.id == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {

            String path = data.path;
            path = path.replace("\\", "\\\\");

            String updateData = "UPDATE product SET "
                    + "prod_id = '" + inventory_productID.getText() + "', prod_name = '"
                    + inventory_productName.getText() + "', type = '"
                    + inventory_type.getSelectionModel().getSelectedItem() + "', stock = '"
                    + inventory_stock.getText() + "', price = '"
                    + inventory_price.getText() + "', status = '"
                    + inventory_status.getSelectionModel().getSelectedItem() + "', image = '"
                    + path + "', date = '"
                    + data.date + "' WHERE id = " + data.id;

            connect = database.connectDB();

            try {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE PRoduct ID: " + inventory_productID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    // TO UPDATE YOUR TABLE VIEW
                    inventoryShowData();
                    // TO CLEAR YOUR FIELDS
                    inventoryClearBtn();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void inventoryDeleteBtn() {
        if (data.id == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE Product ID: " + inventory_productID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM product WHERE id = " + data.id;
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("successfully Deleted!");
                    alert.showAndWait();

                    // TO UPDATE YOUR TABLE VIEW
                    inventoryShowData();
                    // TO CLEAR YOUR FIELDS
                    inventoryClearBtn();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void inventoryClearBtn() {
        inventory_productID.clear();
        inventory_productName.clear();
        inventory_type.getSelectionModel().clearSelection();
        inventory_stock.clear();
        inventory_price.clear();
        inventory_status.getSelectionModel().clearSelection();
        data.path = "";
        data.id = 0;
        inventory_imageView.setImage(null);
    }

    @FXML
    public void inventoryImportBtn() {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*.png", "*.jpg"));
        File file = openFile.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 139, 148, false, true);
            inventory_imageView.setImage(image);
        }
    }

    public ObservableList<productData> inventoryDataList() {
        ObservableList<productData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        connect = database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                listData.add(new productData(
                        result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<productData> inventoryListData;

    public void inventoryShowData() {
        inventoryListData = inventoryDataList();
        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        inventory_tableView.setItems(inventoryListData);
    }

    public void inventorySelectData() {

        productData prodData = inventory_tableView.getSelectionModel().getSelectedItem();
        int num = inventory_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        inventory_productID.setText(prodData.getProductId());
        inventory_productName.setText(prodData.getProductName());
        inventory_stock.setText(String.valueOf(prodData.getStock()));
        inventory_price.setText(String.valueOf(prodData.getPrice()));

        data.path = prodData.getImage();

        String path = "File:" + prodData.getImage();
        data.date = String.valueOf(prodData.getDate());
        data.id = prodData.getId();

        image = new Image(path, 139, 148, false, true);
        inventory_imageView.setImage(image);
    }

    private final String[] typeList = {"Meals", "Drinks"};

    public void inventoryTypeList() {
        inventory_type.setItems(FXCollections.observableArrayList(typeList));
    }

    private final String[] statusList = {"Available", "Unavailable"};

    public void inventoryStatusList() {
        inventory_status.setItems(FXCollections.observableArrayList(statusList));
    }

    public ObservableList<productData> menuGetData() {

        String sql = "SELECT * FROM product";

        ObservableList<productData> listData = FXCollections.observableArrayList();
        connect = database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prod;

            while (result.next()) {
                prod = new productData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    public void menuDisplayCard() {

        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menu_gridPane.getChildren().clear();
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();

        for (int q = 0; q < cardListData.size(); q++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("cardProduct.fxml"));
                AnchorPane pane = load.load();
                cardProductController cardC = load.getController();
                cardC.setData(cardListData.get(q));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                menu_gridPane.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(10));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
     private int cID;
    
    public void customerID() {
        
        String sql = "SELECT MAX(customer_id) FROM customer";
        connect = database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            if (result.next()) {
                cID = result.getInt("MAX(customer_id)");
            }
            
            String checkCID = "SELECT MAX(customer_id) FROM receipt";
            prepare = connect.prepareStatement(checkCID);
            result = prepare.executeQuery();
            int checkID = 0;
            if (result.next()) {
                checkID = result.getInt("MAX(customer_id)");
            }
            
            if (cID == 0) {
                cID += 1;
            } else if (cID == checkID) {
                cID += 1;
            }
            
            data.cID = cID;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

    public void switchForm(ActionEvent event) {

        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);

        } else if (event.getSource() == inventory_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(true);
            menu_form.setVisible(false);

            inventoryTypeList();
            inventoryStatusList();
            inventoryShowData();

        } else if (event.getSource() == menu_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(true);

            menuDisplayCard();

        }
    }

    @FXML
    public void logout() {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to log out?");

            Optional<ButtonType> option = alert.showAndWait();
            if (option.isPresent() && option.get() == ButtonType.OK) {
                logout_btn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Cafe Shop Management System");
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayUsername() {
        String user = data.username1;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username1.setText(user);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayUsername();
        inventoryTypeList();
        inventoryStatusList();
        inventoryShowData();
    }

}
