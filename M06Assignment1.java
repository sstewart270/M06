import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

public class M06Assignment1 extends Application {

    private TextField tfId = new TextField();
    private TextField tfLastName = new TextField();
    private TextField tfFirstName = new TextField();
    private TextField tfMi = new TextField();
    private TextField tfAddress = new TextField();
    private TextField tfCity = new TextField();
    private TextField tfState = new TextField();
    private TextField tfTelephone = new TextField();
    private TextField tfEmail = new TextField();

    @Override
    public void start(Stage primaryStage) {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);

        // Labels and Fields
        pane.add(new Label("ID:"), 0, 0);          pane.add(tfId, 1, 0);
        pane.add(new Label("Last Name:"), 0, 1);   pane.add(tfLastName, 1, 1);
        pane.add(new Label("First Name:"), 0, 2);  pane.add(tfFirstName, 1, 2);
        pane.add(new Label("MI:"), 0, 3);          pane.add(tfMi, 1, 3);
        pane.add(new Label("Address:"), 0, 4);     pane.add(tfAddress, 1, 4);
        pane.add(new Label("City:"), 0, 5);        pane.add(tfCity, 1, 5);
        pane.add(new Label("State:"), 0, 6);       pane.add(tfState, 1, 6);
        pane.add(new Label("Telephone:"), 0, 7);   pane.add(tfTelephone, 1, 7);
        pane.add(new Label("Email:"), 0, 8);       pane.add(tfEmail, 1, 8);

        // buttons
        Button btnView = new Button("View");
        Button btnInsert = new Button("Insert");
        Button btnUpdate = new Button("Update");
        Button btnClear = new Button("Clear");

        pane.add(btnView, 0, 9);
        pane.add(btnInsert, 1, 9);
        pane.add(btnUpdate, 2, 9);
        pane.add(btnClear, 3, 9);

        // button Events
        btnView.setOnAction(e -> viewRecord());
        btnInsert.setOnAction(e -> insertRecord());
        btnUpdate.setOnAction(e -> updateRecord());
        btnClear.setOnAction(e -> clearFields());

        primaryStage.setTitle("Staff Database");
        primaryStage.setScene(new Scene(pane, 600, 400));
        primaryStage.show();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:staff.db");
    }

    private void viewRecord() {
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM Staff WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tfId.getText());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                tfLastName.setText(rs.getString("lastName"));
                tfFirstName.setText(rs.getString("firstName"));
                tfMi.setText(rs.getString("mi"));
                tfAddress.setText(rs.getString("address"));
                tfCity.setText(rs.getString("city"));
                tfState.setText(rs.getString("state"));
                tfTelephone.setText(rs.getString("telephone"));
                tfEmail.setText(rs.getString("email"));
            } else {
                showAlert("Record not found.");
            }
        } catch (SQLException ex) {
            showAlert("Error: " + ex.getMessage());
        }
    }

    private void insertRecord() {
        try (Connection conn = connect()) {
            String sql = "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tfId.getText());
            pstmt.setString(2, tfLastName.getText());
            pstmt.setString(3, tfFirstName.getText());
            pstmt.setString(4, tfMi.getText());
            pstmt.setString(5, tfAddress.getText());
            pstmt.setString(6, tfCity.getText());
            pstmt.setString(7, tfState.getText());
            pstmt.setString(8, tfTelephone.getText());
            pstmt.setString(9, tfEmail.getText());

            pstmt.executeUpdate();
            showAlert("Record inserted.");
        } catch (SQLException ex) {
            showAlert("Insert failed: " + ex.getMessage());
        }
    }

    private void updateRecord() {
        try (Connection conn = connect()) {
            String sql = "UPDATE Staff SET lastName=?, firstName=?, mi=?, address=?, city=?, state=?, telephone=?, email=? WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tfLastName.getText());
            pstmt.setString(2, tfFirstName.getText());
            pstmt.setString(3, tfMi.getText());
            pstmt.setString(4, tfAddress.getText());
            pstmt.setString(5, tfCity.getText());
            pstmt.setString(6, tfState.getText());
            pstmt.setString(7, tfTelephone.getText());
            pstmt.setString(8, tfEmail.getText());
            pstmt.setString(9, tfId.getText());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Record updated.");
            } else {
                showAlert("ID not found.");
            }
        } catch (SQLException ex) {
            showAlert("Update failed: " + ex.getMessage());
        }
    }

    private void clearFields() {
        tfId.clear();
        tfLastName.clear();
        tfFirstName.clear();
        tfMi.clear();
        tfAddress.clear();
        tfCity.clear();
        tfState.clear();
        tfTelephone.clear();
        tfEmail.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
