package cafeshop;

import static cafeshop.data.username1;
import java.io.File;
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

        // Validate initialization
        if (menu_total == null || menu_amount == null) {
            System.err.println("Initialization errors: "
                    + "menu_total=" + menu_total + ", menu_amount=" + menu_amount);
        }
    }

    public void dashboardDisplayNC() {

        String sql = "SELECT COUNT(id) FROM receipt";
        connect = database.connectDB();

        try {
            int nc = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                nc = result.getInt("COUNT(id)");
            }
            dashboard_NC.setText(String.valueOf(nc));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dashboardDisplayTI() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "SELECT SUM(total) FROM receipt WHERE date = '"
                + sqlDate + "'";

        connect = database.connectDB();

        try {
            double ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getDouble("SUM(total)");
            }

            dashboard_TI.setText("$" + ti);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardTotalI() {
        String sql = "SELECT SUM(total) FROM receipt";

        connect = database.connectDB();

        try {
            float ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getFloat("SUM(total)");
            }
            dashboard_TotalI.setText("$" + ti);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardNSP() {

        String sql = "SELECT COUNT(quantity) FROM customer";

        connect = database.connectDB();

        try {
            int q = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                q = result.getInt("COUNT(quantity)");
            }
            dashboard_NSP.setText(String.valueOf(q));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardIncomeChart() {
        dashboard_IncomeChart.getData().clear();

        String sql = "SELECT date, SUM(total) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
        connect = database.connectDB();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
            }

            dashboard_IncomeChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardCustomerChart() {
        dashboard_CustomerChart.getData().clear();

        String sql = "SELECT date, COUNT(id) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
        connect = database.connectDB();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }

            dashboard_CustomerChart.getData().add(chart);

        } catch (Exception e) {
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
        System.out.println("menu_amount text: '" + amountText + "'"); // Debug

        if (amountText.isEmpty() || totalP == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText(totalP == 0 ? "Please choose your order first!" : "Please enter a valid amount!");
            alert.showAndWait();
            amount = 0;
            menu_change.setText("$0.00");
        } else {
            try {
                amount = Double.parseDouble(amountText);
                System.out.println("Parsed amount: " + amount); // Debug
                if (amount < totalP) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Amount must be at least $" + String.format("%.2f", totalP));
                    alert.showAndWait();
                    menu_change.setText("$0.00");
                } else {
                    change = amount - totalP;
                    menu_change.setText("$" + String.format("%.2f", change));
                }
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse menu_amount: '" + amountText + "'"); // Debug
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid number!");
                alert.showAndWait();
                amount = 0;
                menu_change.setText("$0.00");
            }
        }
    }

    @FXML
    public void menuPayBtn() {
        alert = new Alert(AlertType.ERROR);

        // Update amount from menu_amount
        menuAmount();
        System.out.println("totalP: " + totalP + ", amount: " + amount); // Debug

        if (totalP <= 0) {
            alert.setAlertType(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose your order first!");
            alert.showAndWait();
            return;
        }

        if (amount <= 0) {
            alert.setAlertType(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid payment amount!");
            alert.showAndWait();
            return;
        }
        if (amount < totalP) {
            alert.setAlertType(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Payment amount must be at least $" + String.format("%.2f", totalP));
            alert.showAndWait();
            return;
        }

        String insertPay = "INSERT INTO receipt (customer_id, total, date, em_username) VALUES (?, ?, ?, ?)";

        try (Connection connect = database.connectDB()) {
            if (connect == null) {
                alert.setAlertType(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Failed to connect to the database!");
                alert.showAndWait();
                return;
            }

            if (cID <= 0) {
                customerID();
                if (cID <= 0) {
                    alert.setAlertType(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid customer ID!");
                    alert.showAndWait();
                    return;
                }
            }

            alert.setAlertType(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                try (PreparedStatement prepare = connect.prepareStatement(insertPay)) {
                    prepare.setInt(1, cID);
                    prepare.setDouble(2, totalP);
                    prepare.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                    prepare.setString(4, data.username);
                    prepare.executeUpdate();

                    alert.setAlertType(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    //alert.setContentText("Successful.");
                    alert.setContentText("Payment successful. Change: $" + String.format("%.2f", amount - totalP));
                    alert.showAndWait();

                    menuShowOrderData();
                    menuRestart();
                }
            } else {
                alert.setAlertType(AlertType.WARNING);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Payment cancelled.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            alert.setAlertType(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Database error: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void menuGetTotal() {
        customerID();
        String total = "SELECT SUM(price) AS total_price FROM customer WHERE customer_id = " + cID;

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
                try (ResultSet result = prepare.executeQuery()) {
                    if (result.next()) {
                        totalP = result.getDouble("total_price");
                        System.out.println("totalP in menuGetTotal: " + totalP); // Debug
                    } else {
                        totalP = 0;
                        System.out.println("No orders found for customer_id: " + cID); // Debug
                    }
                }
                if (menu_total != null) {
                    menu_total.setText("$" + String.format("%.2f", totalP));
                } else {
                    System.err.println("Error: menu_total is null in menuGetTotal.");
                }
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

            try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                try (ResultSet result = prepare.executeQuery()) {
                    if (result.next()) {
                        cID = result.getInt("max_id");
                    } else {
                        cID = 0;
                    }
                }
            }

            String checkCID = "SELECT MAX(customer_id) AS max_id FROM receipt";
            try (PreparedStatement prepare = connect.prepareStatement(checkCID)) {
                try (ResultSet result = prepare.executeQuery()) {
                    int checkID = 0;
                    if (result.next()) {
                        checkID = result.getInt("max_id");
                    }
                    if (cID == 0 || cID == checkID) {
                        cID += 1;
                    }
                    data.cID = cID;
                    System.out.println("Generated cID: " + cID); // Debug
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error in customerID: " + e.getMessage());
            e.printStackTrace();
            cID = 1;
            data.cID = cID;
        }
    }

    // Existing methods (unchanged for brevity; add fixes as needed)
    @FXML
    public void inventoryAddBtn() {
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_type.getSelectionModel().isEmpty()
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status.getSelectionModel().isEmpty()
                || data.path == null || data.path.trim().isEmpty()) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            return;
        }

        try (Connection connect = database.connectDB()) {
            if (connect == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Database connection failed");
                alert.showAndWait();
                return;
            }

            String checkProdID = "SELECT prod_id FROM product WHERE prod_id = ?";
            try (PreparedStatement checkStmt = connect.prepareStatement(checkProdID)) {
                checkStmt.setString(1, inventory_productID.getText());
                try (ResultSet result = checkStmt.executeQuery()) {
                    if (result.next()) {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText(inventory_productID.getText() + " is already taken");
                        alert.showAndWait();
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

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();

                inventoryShowData();
                inventoryClearBtn();
            }
        } catch (SQLException | NumberFormatException e) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error adding product: " + e.getMessage());
            alert.showAndWait();
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
                || data.id == null|| data.id == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields and select a product");
            alert.showAndWait();
            return;
        }

        try (Connection connect = database.connectDB()) {
            if (connect == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Database connection failed");
                alert.showAndWait();
                return;
            }

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to UPDATE Product ID: " + inventory_productID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
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

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();
                }
            } else {
                alert = new Alert(AlertType.WARNING);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Update cancelled.");
                alert.showAndWait();
            }
        } catch (SQLException | NumberFormatException e) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error updating product: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    public void inventoryDeleteBtn() {
        if (data.id == null || data.id == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to delete");
            alert.showAndWait();
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
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Database connection failed");
                    alert.showAndWait();
                    return;
                }

                String deleteData = "DELETE FROM product WHERE id = ?";
                try (PreparedStatement prepare = connect.prepareStatement(deleteData)) {
                    prepare.setInt(1, data.id);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();
                }
            } catch (SQLException e) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Error deleting product: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        } else {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Deletion cancelled.");
            alert.showAndWait();
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
        String sql = "SELECT * FROM product";

        try (Connection connect = database.connectDB()) {
            if (connect == null) {
                System.err.println("Error: Database connection failed in inventoryDataList.");
                return listData;
            }
            try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                try (ResultSet result = prepare.executeQuery()) {
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

    public ObservableList<productData> menuGetData() {
        String sql = "SELECT * FROM product";
        ObservableList<productData> listData = FXCollections.observableArrayList();

        try (Connection connect = database.connectDB()) {
            if (connect == null) {
                System.err.println("Error: Database connection failed in menuGetData.");
                return listData;
            }
            try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                try (ResultSet result = prepare.executeQuery()) {
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
            System.err.println("SQL error in menuGetData: " + e.getMessage());
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

        for (productData prod : cardListData) {
            try {
                FXMLLoader load = new FXMLLoader(getClass().getResource("cardProduct.fxml"));
                AnchorPane pane = load.load();
                CardProductController cardC = load.getController();
                cardC.setData(prod);

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                menu_gridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new javafx.geometry.Insets(10));
            } catch (Exception e) {
                System.err.println("Error loading cardProduct.fxml: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public ObservableList<productData> menuGetOrder() {
        customerID();
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        ObservableList<productData> listData = FXCollections.observableArrayList();

        try (Connection connect = database.connectDB()) {
            if (connect == null) {
                System.err.println("Error: Database connection failed in menuGetOrder.");
                return listData;
            }
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
    }

    @FXML
    public void menuSelectOrder() {
        productData prod = menu_tableView.getSelectionModel().getSelectedItem();
        if (prod == null) {
            return;
        }
        getid = prod.getId();
    }

    @FXML
    public void menuRemoveBtn() {
        if (getid == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the order you want to remove");
            alert.showAndWait();
            return;
        }

        String deleteData = "DELETE FROM customer WHERE id = ?";
        try (Connection connect = database.connectDB()) {
            if (connect == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Database connection failed");
                alert.showAndWait();
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
                }
            }
        } catch (SQLException e) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error deleting order: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public ObservableList<customersData> customersDataList() {

        ObservableList<customersData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM receipt";
        connect = database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            customersData cData;

            while (result.next()) {
                cData = new customersData(result.getInt("id"),
                        result.getInt("customer_id"),
                        result.getDouble("total"),
                        result.getDate("date"),
                        result.getString("em_username"));

                listData.add(cData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<customersData> customersListData;

    public void customersShowData() {
        customersListData = customersDataList();

        customers_col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customers_col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        customers_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customers_col_cashier.setCellValueFactory(new PropertyValueFactory<>("emUsername"));

        customers_tableView.setItems(customersListData);
    }

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
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error during logout: " + e.getMessage());
            alert.showAndWait();
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
}
