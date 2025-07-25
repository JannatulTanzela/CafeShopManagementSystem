package cafeshop;

import static cafeshop.data.username1;
import java.awt.Desktop;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
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
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
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
    private AnchorPane main_form, inventory_form, dashboard_form, menu_form;
    @FXML
    private TableView<productData> inventory_tableView;
    @FXML
    private TableColumn<productData, String> inventory_col_productID, inventory_col_productName, 
            inventory_col_type, inventory_col_stock, inventory_col_price, inventory_col_status, inventory_col_date;
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
    @FXML
    private ScrollPane menu_scrollPane;
    @FXML
    private GridPane menu_gridPane;
    @FXML
    private TableView<productData> menu_tableView;
    @FXML
    private TableColumn<productData, String> menu_col_productName, menu_col_quantity, menu_col_price;
    @FXML
    private Label menu_total, menu_change;
    @FXML
    private TextField menu_amount;
    @FXML
    private Button menu_payBtn, menu_removeBtn, menu_receiptBtn;
    @FXML
    private AnchorPane customers_form;
    @FXML
    private TableView<customersData> customers_tableView;
    @FXML
    private TableColumn<customersData, String> customers_col_customerID;
    @FXML
    private TableColumn<customersData, String> customers_col_total;
    @FXML
    private TableColumn<customersData, String> customers_col_date;
    @FXML
    private TableColumn<customersData, String> customers_col_cashier;
    @FXML
    private Label dashboard_NC;
    @FXML
    private Label dashboard_TI;
    @FXML
    private Label dashboard_NSP;
    @FXML
    private AreaChart<?, ?> dashboard_IncomeChart;
    @FXML
    private BarChart<?, ?> dashboard_CustomerChart;
    @FXML
    private Label dashboard_TotalI;

    private Alert alert;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;
    private ObservableList<productData> cardListData = FXCollections.observableArrayList();
    private ObservableList<productData> inventoryListData;
    private ObservableList<productData> menuOrderListData;
    private int cID;
    private double totalP;
    private double amount;
    private double change;
    private int getid;
    private boolean paymentCompleted = false; // Track payment status

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayUsername();
        dashboardDisplayNC();
        dashboardDisplayTI();
        dashboardTotalI();
        dashboardNSP();
        dashboardIncomeChart();
        dashboardCustomerChart();
        inventoryTypeList();
        inventoryStatusList();
        inventoryShowData();
        menuDisplayCard();
        menuShowOrderData();
        menuDisplayTotal();
        customersShowData();

        // Setup table selection listeners
        setupTableSelectionListeners();

        // Initially disable receipt button
        if (menu_receiptBtn != null) {
            menu_receiptBtn.setDisable(true);
        }

        // Add TextFormatter to restrict menu_amount to valid numbers
        if (menu_amount != null) {
            TextFormatter<Double> formatter = new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("\\d*\\.?\\d{0,2}")) {
                    return change;
                }
                return null;
            });
            menu_amount.setTextFormatter(formatter);
        }
    }

    private void setupTableSelectionListeners() {
        // Inventory table selection
        if (inventory_tableView != null) {
            inventory_tableView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    inventorySelectData();
                }
            });
        }

        // Menu table selection
        if (menu_tableView != null) {
            menu_tableView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    menuSelectOrder();
                }
            });
        }
    }

    // Dashboard Methods
    public void dashboardDisplayNC() {
        String sql = "SELECT COUNT(id) FROM receipt";
        try (Connection connect = database.connectDB()) {
            int nc = 0;
            if (connect != null) {
                try (PreparedStatement prepare = connect.prepareStatement(sql);
                     ResultSet result = prepare.executeQuery()) {
                    if (result.next()) {
                        nc = result.getInt("COUNT(id)");
                    }
                }
            }
            dashboard_NC.setText(String.valueOf(nc));
        } catch (SQLException e) {
            e.printStackTrace();
            dashboard_NC.setText("0");
        }
    }

    public void dashboardDisplayTI() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String sql = "SELECT SUM(total) FROM receipt WHERE date = ?";
        
        try (Connection connect = database.connectDB()) {
            double ti = 0;
            if (connect != null) {
                try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                    prepare.setDate(1, sqlDate);
                    try (ResultSet result = prepare.executeQuery()) {
                        if (result.next()) {
                            ti = result.getDouble("SUM(total)");
                        }
                    }
                }
            }
            dashboard_TI.setText("$" + String.format("%.2f", ti));
        } catch (SQLException e) {
            e.printStackTrace();
            dashboard_TI.setText("$0.00");
        }
    }

    public void dashboardTotalI() {
        String sql = "SELECT SUM(total) FROM receipt";
        try (Connection connect = database.connectDB()) {
            double ti = 0;
            if (connect != null) {
                try (PreparedStatement prepare = connect.prepareStatement(sql);
                     ResultSet result = prepare.executeQuery()) {
                    if (result.next()) {
                        ti = result.getDouble("SUM(total)");
                    }
                }
            }
            dashboard_TotalI.setText("$" + String.format("%.2f", ti));
        } catch (SQLException e) {
            e.printStackTrace();
            dashboard_TotalI.setText("$0.00");
        }
    }

    public void dashboardNSP() {
        String sql = "SELECT SUM(quantity) FROM customer";
        try (Connection connect = database.connectDB()) {
            int q = 0;
            if (connect != null) {
                try (PreparedStatement prepare = connect.prepareStatement(sql);
                     ResultSet result = prepare.executeQuery()) {
                    if (result.next()) {
                        q = result.getInt("SUM(quantity)");
                    }
                }
            }
            dashboard_NSP.setText(String.valueOf(q));
        } catch (SQLException e) {
            e.printStackTrace();
            dashboard_NSP.setText("0");
        }
    }

    public void dashboardIncomeChart() {
        dashboard_IncomeChart.getData().clear();
        String sql = "SELECT date, SUM(total) FROM receipt GROUP BY date ORDER BY date";
        
        try (Connection connect = database.connectDB()) {
            if (connect != null) {
                XYChart.Series chart = new XYChart.Series();
                try (PreparedStatement prepare = connect.prepareStatement(sql);
                     ResultSet result = prepare.executeQuery()) {
                    while (result.next()) {
                        chart.getData().add(new XYChart.Data<>(result.getString(1), result.getDouble(2)));
                    }
                }
                dashboard_IncomeChart.getData().add(chart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dashboardCustomerChart() {
        dashboard_CustomerChart.getData().clear();
        String sql = "SELECT date, COUNT(id) FROM receipt GROUP BY date ORDER BY date";
        
        try (Connection connect = database.connectDB()) {
            if (connect != null) {
                XYChart.Series chart = new XYChart.Series();
                try (PreparedStatement prepare = connect.prepareStatement(sql);
                     ResultSet result = prepare.executeQuery()) {
                    while (result.next()) {
                        chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
                    }
                }
                dashboard_CustomerChart.getData().add(chart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Inventory Methods
    @FXML
    public void inventoryAddBtn() {
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_type.getSelectionModel().isEmpty()
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status.getSelectionModel().isEmpty()
                || data.path == null || data.path.trim().isEmpty()) {
            
            showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
            return;
        }

        try (Connection connect = database.connectDB()) {
            if (connect == null) {
                showAlert(AlertType.ERROR, "Error Message", "Database connection failed");
                return;
            }

            // Check if product ID already exists
            String checkProdID = "SELECT prod_id FROM product WHERE prod_id = ?";
            try (PreparedStatement checkStmt = connect.prepareStatement(checkProdID)) {
                checkStmt.setString(1, inventory_productID.getText());
                try (ResultSet result = checkStmt.executeQuery()) {
                    if (result.next()) {
                        showAlert(AlertType.ERROR, "Error Message", 
                                inventory_productID.getText() + " is already taken");
                        return;
                    }
                }
            }

            String insertData = "INSERT INTO product (prod_id, prod_name, type, stock, price, status, image, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement prepare = connect.prepareStatement(insertData)) {
                prepare.setString(1, inventory_productID.getText());
                prepare.setString(2, inventory_productName.getText());
                prepare.setString(3, inventory_type.getSelectionModel().getSelectedItem());
                prepare.setInt(4, Integer.parseInt(inventory_stock.getText()));
                prepare.setDouble(5, Double.parseDouble(inventory_price.getText()));
                prepare.setString(6, inventory_status.getSelectionModel().getSelectedItem());
                String path = data.path.replace("\\", "\\\\");
                prepare.setString(7, path);
                prepare.setDate(8, java.sql.Date.valueOf(LocalDate.now()));
                
                prepare.executeUpdate();
                showAlert(AlertType.INFORMATION, "Information Message", "Successfully Added!");
                inventoryShowData();
                inventoryClearBtn();
                menuDisplayCard(); // Refresh menu cards
            }
        } catch (SQLException | NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error Message", "Error adding product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void inventoryUpdateBtn() {
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_type.getSelectionModel().isEmpty()
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status.getSelectionModel().isEmpty()
                || data.path == null || data.path.isEmpty()
                || data.id == null || data.id == 0) {
            
            showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields and select a product");
            return;
        }

        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to UPDATE Product ID: " + inventory_productID.getText() + "?");
        Optional<ButtonType> option = alert.showAndWait();
        
        if (option.isPresent() && option.get() == ButtonType.OK) {
            try (Connection connect = database.connectDB()) {
                if (connect == null) {
                    showAlert(AlertType.ERROR, "Error Message", "Database connection failed");
                    return;
                }

                String updateData = "UPDATE product SET prod_id = ?, prod_name = ?, type = ?, stock = ?, price = ?, status = ?, image = ?, date = ? WHERE id = ?";
                try (PreparedStatement prepare = connect.prepareStatement(updateData)) {
                    prepare.setString(1, inventory_productID.getText());
                    prepare.setString(2, inventory_productName.getText());
                    prepare.setString(3, inventory_type.getSelectionModel().getSelectedItem());
                    prepare.setInt(4, Integer.parseInt(inventory_stock.getText()));
                    prepare.setDouble(5, Double.parseDouble(inventory_price.getText()));
                    prepare.setString(6, inventory_status.getSelectionModel().getSelectedItem());
                    String path = data.path.replace("\\", "\\\\");
                    prepare.setString(7, path);
                    prepare.setDate(8, java.sql.Date.valueOf(LocalDate.now()));
                    prepare.setInt(9, data.id);
                    
                    prepare.executeUpdate();
                    showAlert(AlertType.INFORMATION, "Information Message", "Successfully Updated!");
                    inventoryShowData();
                    inventoryClearBtn();
                    menuDisplayCard(); // Refresh menu cards
                }
            } catch (SQLException | NumberFormatException e) {
                showAlert(AlertType.ERROR, "Error Message", "Error updating product: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void inventoryDeleteBtn() {
        if (data.id == null || data.id == 0) {
            showAlert(AlertType.ERROR, "Error Message", "Please select a product to delete");
            return;
        }

        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to DELETE Product ID: " + inventory_productID.getText() + "?");
        Optional<ButtonType> option = alert.showAndWait();
        
        if (option.isPresent() && option.get() == ButtonType.OK) {
            try (Connection connect = database.connectDB()) {
                if (connect == null) {
                    showAlert(AlertType.ERROR, "Error Message", "Database connection failed");
                    return;
                }

                String deleteData = "DELETE FROM product WHERE id = ?";
                try (PreparedStatement prepare = connect.prepareStatement(deleteData)) {
                    prepare.setInt(1, data.id);
                    prepare.executeUpdate();
                    showAlert(AlertType.INFORMATION, "Information Message", "Successfully Deleted!");
                    inventoryShowData();
                    inventoryClearBtn();
                    menuDisplayCard(); // Refresh menu cards
                }
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Error Message", "Error deleting product: " + e.getMessage());
                e.printStackTrace();
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
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.avif", "*.PNG"));
        File file = openFile.showOpenDialog(main_form.getScene().getWindow());
        
        if (file != null) {
            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 139, 148, false, true);
            inventory_imageView.setImage(image);
        }
    }

    public ObservableList<productData> inventoryDataList() {
        ObservableList<productData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product ORDER BY id ASC";
        
        try (Connection connect = database.connectDB()) {
            if (connect != null) {
                try (PreparedStatement prepare = connect.prepareStatement(sql);
                     ResultSet result = prepare.executeQuery()) {
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
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error in inventoryDataList: " + e.getMessage());
            e.printStackTrace();
        }
        return listData;
    }

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
        if (prodData == null) {
            return;
        }

        inventory_productID.setText(prodData.getProductId());
        inventory_productName.setText(prodData.getProductName());
        inventory_stock.setText(String.valueOf(prodData.getStock()));
        inventory_price.setText(String.valueOf(prodData.getPrice()));
        inventory_type.getSelectionModel().select(prodData.getType());
        inventory_status.getSelectionModel().select(prodData.getStatus());
        
        data.path = prodData.getImage();
        data.date = String.valueOf(prodData.getDate());
        data.id = prodData.getId();
        
        String path = "File:" + prodData.getImage();
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

    // Menu Methods - Fixed to show all products
    public ObservableList<productData> menuGetData() {
        String sql = "SELECT * FROM product ORDER BY id ASC"; // Show all products, not just available ones
        ObservableList<productData> listData = FXCollections.observableArrayList();
        
        try (Connection connect = database.connectDB()) {
            if (connect != null) {
                try (PreparedStatement prepare = connect.prepareStatement(sql);
                     ResultSet result = prepare.executeQuery()) {
                    while (result.next()) {
                        productData product = new productData(
                                result.getInt("id"),
                                result.getString("prod_id"),
                                result.getString("prod_name"),
                                result.getString("type"),
                                result.getInt("stock"),
                                result.getDouble("price"),
                                result.getString("status"),
                                result.getString("image"),
                                result.getDate("date")
                        );
                        listData.add(product);
                        System.out.println("Loaded product: " + product.getProductName() + " - Status: " + product.getStatus()); // Debug
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error in menuGetData: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Total products loaded: " + listData.size()); // Debug
        return listData;
    }

    public void menuDisplayCard() {
        cardListData.clear();
        cardListData.addAll(menuGetData());
        
        // Clear existing content
        menu_gridPane.getChildren().clear();
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();
        
        int row = 0;
        int column = 0;
        
        System.out.println("Displaying " + cardListData.size() + " products in menu"); // Debug
        
        for (int i = 0; i < cardListData.size(); i++) {
            productData prod = cardListData.get(i);
            try {
                FXMLLoader load = new FXMLLoader(getClass().getResource("cardProduct.fxml"));
                AnchorPane pane = load.load();
                CardProductController cardC = load.getController();
                cardC.setData(prod);

                // Calculate grid position
                if (column == 3) { // 3 columns per row
                    column = 0;
                    row++;
                }

                menu_gridPane.add(pane, column, row);
                GridPane.setMargin(pane, new javafx.geometry.Insets(10));
                
                System.out.println("Added card for: " + prod.getProductName() + " at position [" + column + "," + row + "]"); // Debug
                
                column++;
                
            } catch (Exception e) {
                System.err.println("Error loading cardProduct.fxml for product: " + prod.getProductName());
                e.printStackTrace();
            }
        }
        
        // Force layout update
        menu_gridPane.autosize();
        if (menu_scrollPane != null) {
            menu_scrollPane.setContent(menu_gridPane);
        }
    }

    public ObservableList<productData> menuGetOrder() {
        customerID();
        String sql = "SELECT * FROM customer WHERE customer_id = ? ORDER BY id ASC";
        ObservableList<productData> listData = FXCollections.observableArrayList();
        
        try (Connection connect = database.connectDB()) {
            if (connect != null) {
                try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                    prepare.setInt(1, cID);
                    try (ResultSet result = prepare.executeQuery()) {
                        while (result.next()) {
                            listData.add(new productData(
                                    result.getInt("id"),
                                    result.getString("prod_id"),
                                    result.getString("prod_name"),
                                    result.getString("type"),
                                    result.getInt("quantity"),
                                    result.getDouble("price"),
                                    result.getString("image"),
                                    result.getDate("date")
                            ));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error in menuGetOrder: " + e.getMessage());
            e.printStackTrace();
        }
        return listData;
    }

    public void menuShowOrderData() {
        menuOrderListData = menuGetOrder();
        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        menu_tableView.setItems(menuOrderListData);
        menuDisplayTotal(); // Update total when showing order data
    }

    @FXML
    public void menuSelectOrder() {
        productData prod = menu_tableView.getSelectionModel().getSelectedItem();
        if (prod != null) {
            getid = prod.getId();
        }
    }

    @FXML
    public void menuRemoveBtn() {
        if (getid == 0) {
            showAlert(AlertType.ERROR, "Error Message", "Please select the order you want to remove");
            return;
        }

        String deleteData = "DELETE FROM customer WHERE id = ?";
        try (Connection connect = database.connectDB()) {
            if (connect == null) {
                showAlert(AlertType.ERROR, "Error Message", "Database connection failed");
                return;
            }

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this order?");
            Optional<ButtonType> option = alert.showAndWait();
            
            if (option.isPresent() && option.get() == ButtonType.OK) {
                try (PreparedStatement prepare = connect.prepareStatement(deleteData)) {
                    prepare.setInt(1, getid);
                    prepare.executeUpdate();
                    menuShowOrderData();
                    menuDisplayTotal();
                    getid = 0; // Reset selection
                    paymentCompleted = false; // Reset payment status
                    menu_receiptBtn.setDisable(true); // Disable receipt button
                }
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error Message", "Error deleting order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void menuAmount() {
        if (menu_amount == null) {
            System.err.println("Error: menu_amount is null in menuAmount.");
            return;
        }

        menuGetTotal();
        String amountText = menu_amount.getText().trim();
        
        if (amountText.isEmpty() || totalP == 0) {
            if (totalP == 0) {
                showAlert(AlertType.ERROR, "Error Message", "Please choose your order first!");
            } else {
                showAlert(AlertType.ERROR, "Error Message", "Please enter a valid amount!");
            }
            amount = 0;
            menu_change.setText("$0.00");
        } else {
            try {
                amount = Double.parseDouble(amountText);
                if (amount < totalP) {
                    showAlert(AlertType.ERROR, "Error Message", 
                            "Amount must be at least $" + String.format("%.2f", totalP));
                    menu_change.setText("$0.00");
                } else {
                    change = amount - totalP;
                    menu_change.setText("$" + String.format("%.2f", change));
                }
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Error Message", "Please enter a valid number!");
                amount = 0;
                menu_change.setText("$0.00");
            }
        }
    }

    @FXML
    public void menuPayBtn() {
        menuAmount(); // Update amount and change
        
        if (totalP <= 0) {
            showAlert(AlertType.ERROR, "Error Message", "Please choose your order first!");
            return;
        }
        
        if (amount <= 0) {
            showAlert(AlertType.ERROR, "Error Message", "Please enter a valid payment amount!");
            return;
        }
        
        if (amount < totalP) {
            showAlert(AlertType.ERROR, "Error Message", 
                    "Payment amount must be at least $" + String.format("%.2f", totalP));
            return;
        }

        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to process this payment?");
        Optional<ButtonType> option = alert.showAndWait();
        
        if (option.isPresent() && option.get() == ButtonType.OK) {
            String insertPay = "INSERT INTO receipt (customer_id, total, date, em_username) VALUES (?, ?, ?, ?)";
            try (Connection connect = database.connectDB()) {
                if (connect == null) {
                    showAlert(AlertType.ERROR, "Error Message", "Failed to connect to the database!");
                    return;
                }

                if (cID <= 0) {
                    customerID();
                    if (cID <= 0) {
                        showAlert(AlertType.ERROR, "Error Message", "Invalid customer ID!");
                        return;
                    }
                }

                try (PreparedStatement prepare = connect.prepareStatement(insertPay)) {
                    prepare.setInt(1, cID);
                    prepare.setDouble(2, totalP);
                    prepare.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                    prepare.setString(4, data.username != null ? data.username : "System");
                    prepare.executeUpdate();

                    showAlert(AlertType.INFORMATION, "Payment Successful", 
                            "Payment completed successfully!\nTotal: $" + String.format("%.2f", totalP) +
                            "\nAmount Paid: $" + String.format("%.2f", amount) +
                            "\nChange: $" + String.format("%.2f", change) +
                            "\n\nClick 'Receipt' button to generate receipt or continue with next order.");
                    
                    // Mark payment as completed and enable receipt button
                    paymentCompleted = true;
                    menu_receiptBtn.setDisable(false);
                    
                    // Update dashboard data
                    dashboardDisplayNC();
                    dashboardDisplayTI();
                    dashboardTotalI();
                    dashboardNSP();
                    customersShowData();
                }
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Error Message", "Database error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void menuReceiptBtn() {
        if (!paymentCompleted) {
            showAlert(AlertType.ERROR, "Error Message", "Please complete payment first!");
            return;
        }

        if (totalP == 0 || menuOrderListData == null || menuOrderListData.isEmpty()) {
            showAlert(AlertType.ERROR, "Error Message", "No order data available for receipt!");
            return;
        }

        try {
            // Create receipts directory if it doesn't exist
            File dir = new File("receipts");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Create the receipt file
            String filename = "receipts/receipt_" + System.currentTimeMillis() + ".txt";
            File file = new File(filename);
            
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("===== Cafeshop Receipt =====");
                writer.println("Date: " + new java.util.Date());
                writer.println("Cashier: " + (data.username != null ? data.username : "System"));
                writer.println("Customer ID: " + cID);
                writer.println("------------------------------------");
                
                // Get order data
                for (productData p : menuOrderListData) {
                    writer.println("Item: " + p.getProductName());
                    writer.println("Qty : " + p.getQuantity());
                    writer.println("Price: $" + String.format("%.2f", p.getPrice()));
                    writer.println("------------------------------------");
                }
                
                writer.println("Total : $" + String.format("%.2f", totalP));
                writer.println("Paid  : $" + String.format("%.2f", amount));
                writer.println("Change: $" + String.format("%.2f", change));
                writer.println("====================================");
                writer.println("   Thank you for shopping with us!   ");
            }

            // Open the file using system default text viewer
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

            showAlert(AlertType.INFORMATION, "Receipt Generated",
                    "Receipt generated successfully!\nSaved as: " + filename);

            // Clear everything after receipt is generated
            clearAfterReceipt();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to generate receipt:\n" + e.getMessage());
        }
    }

    private void clearAfterReceipt() {
        // Clear customer orders from database
        String deleteOrders = "DELETE FROM customer WHERE customer_id = ?";
        try (Connection connect = database.connectDB()) {
            if (connect != null) {
                try (PreparedStatement prepare = connect.prepareStatement(deleteOrders)) {
                    prepare.setInt(1, cID);
                    prepare.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Reset all values and UI
        menuRestart();
        menuShowOrderData();
        paymentCompleted = false;
        menu_receiptBtn.setDisable(true);
        getid = 0;
    }

    public void menuGetTotal() {
        customerID();
        String total = "SELECT SUM(price) AS total_price FROM customer WHERE customer_id = ?";
        
        try (Connection connect = database.connectDB()) {
            if (connect == null) {
                System.err.println("Error: Database connection failed in menuGetTotal.");
                totalP = 0;
                if (menu_total != null) {
                    menu_total.setText("$0.00");
                }
                return;
            }

            try (PreparedStatement prepare = connect.prepareStatement(total)) {
                prepare.setInt(1, cID);
                try (ResultSet result = prepare.executeQuery()) {
                    if (result.next()) {
                        totalP = result.getDouble("total_price");
                    } else {
                        totalP = 0;
                    }
                }
            }

            if (menu_total != null) {
                menu_total.setText("$" + String.format("%.2f", totalP));
            }
        } catch (SQLException e) {
            System.err.println("SQL error in menuGetTotal: " + e.getMessage());
            e.printStackTrace();
            totalP = 0;
            if (menu_total != null) {
                menu_total.setText("$0.00");
            }
        }
    }

    public void menuDisplayTotal() {
        menuGetTotal();
    }

    public void menuRestart() {
        totalP = 0;
        amount = 0;
        change = 0;
        if (menu_total != null) {
            menu_total.setText("$0.00");
        }
        if (menu_amount != null) {
            menu_amount.clear();
        }
        if (menu_change != null) {
            menu_change.setText("$0.00");
        }
    }

    public void customerID() {
        String sql = "SELECT MAX(customer_id) AS max_id FROM customer";
        try (Connection connect = database.connectDB()) {
            if (connect == null) {
                System.err.println("Error: Database connection failed in customerID.");
                cID = 1;
                data.cID = cID;
                return;
            }

            try (PreparedStatement prepare = connect.prepareStatement(sql);
                 ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    cID = result.getInt("max_id");
                } else {
                    cID = 0;
                }
            }

            String checkCID = "SELECT MAX(customer_id) AS max_id FROM receipt";
            try (PreparedStatement prepare = connect.prepareStatement(checkCID);
                 ResultSet result = prepare.executeQuery()) {
                int checkID = 0;
                if (result.next()) {
                    checkID = result.getInt("max_id");
                }
                if (cID == 0 || cID == checkID) {
                    cID += 1;
                }
                data.cID = cID;
            }
        } catch (SQLException e) {
            System.err.println("SQL error in customerID: " + e.getMessage());
            e.printStackTrace();
            cID = 1;
            data.cID = cID;
        }
    }

    // Customer Methods
    public ObservableList<customersData> customersDataList() {
        ObservableList<customersData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM receipt ORDER BY date DESC, id DESC";
        
        try (Connection connect = database.connectDB()) {
            if (connect != null) {
                try (PreparedStatement prepare = connect.prepareStatement(sql);
                     ResultSet result = prepare.executeQuery()) {
                    while (result.next()) {
                        String username = result.getString("em_username");
                        if (username == null || username.trim().isEmpty()) {
                            username = "System";
                        }
                        customersData cData = new customersData(
                                result.getInt("id"),
                                result.getInt("customer_id"),
                                result.getDouble("total"),
                                result.getDate("date"),
                                username
                        );
                        listData.add(cData);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    public void customersShowData() {
        ObservableList<customersData> customersListData = customersDataList();
        customers_col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customers_col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        customers_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customers_col_cashier.setCellValueFactory(new PropertyValueFactory<>("emUsername"));
        customers_tableView.setItems(customersListData);
    }

    // Navigation Methods
    @FXML
    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            customers_form.setVisible(false);
            dashboardDisplayNC();
            dashboardDisplayTI();
            dashboardTotalI();
            dashboardNSP();
            dashboardIncomeChart();
            dashboardCustomerChart();
        } else if (event.getSource() == inventory_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(true);
            menu_form.setVisible(false);
            customers_form.setVisible(false);
            inventoryTypeList();
            inventoryStatusList();
            inventoryShowData();
        } else if (event.getSource() == menu_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(true);
            customers_form.setVisible(false);
            menuDisplayCard();
            menuShowOrderData();
            menuDisplayTotal();
        } else if (event.getSource() == customers_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            customers_form.setVisible(true);
            customersShowData();
        }
    }

    @FXML
    public void logout() {
        try {
            alert = new Alert(AlertType.CONFIRMATION);
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
            showAlert(AlertType.ERROR, "Error Message", "Error during logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void displayUsername() {
        if (data.username1 != null) {
            String user = data.username1;
            user = user.substring(0, 1).toUpperCase() + user.substring(1);
            username1.setText(user);
        } else {
            username1.setText("Unknown");
            System.err.println("Error: data.username1 is null in displayUsername.");
        }
    }

    // Utility method for showing alerts
    private void showAlert(AlertType alertType, String title, String message) {
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}