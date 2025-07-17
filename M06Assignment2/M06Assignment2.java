import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class M06Assignment2 extends Application {

    private DBConnectionPane dbConnectionPane = new DBConnectionPane();
    private TextArea textArea = new TextArea();
    private Button btnBatch = new Button("Batch Update");
    private Button btnNonBatch = new Button("Non-Batch Update");

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(btnBatch, btnNonBatch, textArea);

        root.setTop(dbConnectionPane);
        root.setCenter(vbox);

        btnBatch.setOnAction(e -> performBatchUpdate());
        btnNonBatch.setOnAction(e -> performNonBatchUpdate());

        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.setTitle("M06Assignment2: Batch vs Non-Batch Updates");
        primaryStage.show();
    }

    private void performBatchUpdate() {
        Connection conn = dbConnectionPane.getConnection();
        if (conn == null) {
            showAlert("No database connection.");
            return;
        }

        String sql = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < 1000; i++) {
                pstmt.setDouble(1, Math.random());
                pstmt.setDouble(2, Math.random());
                pstmt.setDouble(3, Math.random());
                pstmt.addBatch();
            }

            pstmt.executeBatch();

            long elapsedTime = System.currentTimeMillis() - startTime;
            textArea.appendText("Batch update completed in " + elapsedTime + " ms\n");

        } catch (SQLException ex) {
            showAlert("Batch update failed: " + ex.getMessage());
        }
    }

    private void performNonBatchUpdate() {
        Connection conn = dbConnectionPane.getConnection();
        if (conn == null) {
            showAlert("No database connection.");
            return;
        }

        String sql = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < 1000; i++) {
                pstmt.setDouble(1, Math.random());
                pstmt.setDouble(2, Math.random());
                pstmt.setDouble(3, Math.random());
                pstmt.executeUpdate();
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            textArea.appendText("Non-Batch update completed in " + elapsedTime + " ms\n");

        } catch (SQLException ex) {
            showAlert("Non-Batch update failed: " + ex.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
