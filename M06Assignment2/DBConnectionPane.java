import java.sql.Connection;
import java.sql.DriverManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class DBConnectionPane extends BorderPane {
    private TextField tfDriver = new TextField();
    private TextField tfURL = new TextField();
    private TextField tfUsername = new TextField();
    private PasswordField pfPassword = new PasswordField();
    private Button btConnect = new Button("Connect to DB");
    private Connection connection;

    public DBConnectionPane() {
        // set the default values
        tfDriver.setText("org.sqlite.JDBC");

        // layout setup
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        gridPane.add(new Label("JDBC Driver:"), 0, 0);
        gridPane.add(tfDriver, 1, 0);

        gridPane.add(new Label("Database URL:"), 0, 1);
        gridPane.add(tfURL, 1, 1);

        gridPane.add(new Label("Username:"), 0, 2);
        gridPane.add(tfUsername, 1, 2);

        gridPane.add(new Label("Password:"), 0, 3);
        gridPane.add(pfPassword, 1, 3);

        gridPane.add(btConnect, 1, 4);

        setCenter(gridPane);

        // connect button action
        btConnect.setOnAction(e -> connectToDB());
    }

    private void connectToDB() {
        try {
            String driver = tfDriver.getText();
            if (driver != null && !driver.isEmpty()) {
                Class.forName(driver);
            }

            if (driver.contains("sqlite")) {
                connection = DriverManager.getConnection(tfURL.getText());
            } else {
                connection = DriverManager.getConnection(tfURL.getText(),
                                                         tfUsername.getText(),
                                                         pfPassword.getText());
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Connected to DB successfully.");
            alert.showAndWait();

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to DB: " + ex.getMessage());
            alert.showAndWait();
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}


