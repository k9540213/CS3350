import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;

public class EmployeeTestRun extends Application {
    private static DatabaseManager db = new DatabaseManager();
    private Stage primaryStage;
    private int currentEmpId;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Company Z Employee Portal");
        showLoginScreen();
    }

    private void showLoginScreen() {
        VBox wrapper = new VBox(10);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(40));
        wrapper.setStyle("-fx-background-color: linear-gradient(to bottom, #f3e6ff, #ffffff);");

        Label titleLabel = new Label("Login");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#4b0082"));

        Label idLabel = new Label("Login using your Employee or Admin ID");
        idLabel.setFont(Font.font("Segoe UI", 14));
        idLabel.setTextFill(Color.web("#666666"));

        TextField idField = new TextField();
        idField.setPromptText("Enter Employee ID");
        idField.setMaxWidth(250);
        idField.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter Password");
        passField.setMaxWidth(250);
        passField.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Button loginBtn = new Button("Login");
        styleButton(loginBtn, "#6a0dad");

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);

        wrapper.getChildren().addAll(titleLabel, idLabel, idField, passField, loginBtn, errorLabel);

        loginBtn.setOnAction(e -> {
            errorLabel.setText("");
            String idText = idField.getText().trim();
            String passText = passField.getText().trim();

            if (idText.isEmpty()) {
                errorLabel.setText("Employee ID is required.");
                return;
            }

            try {
                int empid = Integer.parseInt(idText);
                currentEmpId = empid;
                System.out.println("Logging in with empid: " + empid); // Debug print
                if (empid == 999) {
                    if (passText.isEmpty()) {
                        errorLabel.setText("Password is required for admin.");
                        return;
                    }
                    if ("123".equals(passText)) {
                        showAdminScreen();
                    } else {
                        errorLabel.setText("Invalid admin password!");
                    }
                } else {
                    // Employees don't need a password
                    showEmployeeScreen(empid);
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Enter a valid numeric ID.");
            }
        });

        Scene scene = new Scene(wrapper, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void styleButton(Button btn, String color) {
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold; -fx-font-size: 12px;");
        btn.setMinWidth(100);
    }

    private void styleSidebarButton(Button btn) {
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #845ec2; -fx-text-fill: white;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAdminScreen() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f8ff;");

        // Sidebar
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(30));
        sidebar.setStyle("-fx-background-color: #6a0dad;");
        sidebar.setPrefWidth(160);

        Label sidebarTitle = new Label("Menu");
        sidebarTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button homeBtn = new Button("Home");
        Button panelBtn = new Button("Admin Panel");
        Button logoutBtn = new Button("Logout");
        styleSidebarButton(homeBtn);
        styleSidebarButton(panelBtn);
        styleSidebarButton(logoutBtn);
        sidebar.getChildren().addAll(sidebarTitle, homeBtn, panelBtn, logoutBtn);

        // Content
        VBox homeView = new VBox(10, new Label("Welcome to Admin Dashboard"));
        homeView.setPadding(new Insets(20));
        homeView.setAlignment(Pos.CENTER);

        VBox adminPanel = createAdminPanel();

        StackPane contentPane = new StackPane();
        contentPane.setPadding(new Insets(20));
        contentPane.getChildren().add(homeView);

        homeBtn.setOnAction(e -> contentPane.getChildren().setAll(homeView));
        panelBtn.setOnAction(e -> contentPane.getChildren().setAll(adminPanel));
        logoutBtn.setOnAction(e -> showLoginScreen());

        root.setLeft(sidebar);
        root.setCenter(contentPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    private VBox createAdminPanel() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        // Search Section
        Label searchLabel = new Label("Search Employees:");
        searchLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        TextField fnameField = new TextField();
        fnameField.setPromptText("First Name");
        TextField lnameField = new TextField();
        lnameField.setPromptText("Last Name");
        TextField ssnField = new TextField();
        ssnField.setPromptText("SSN");
        TextField empIdField = new TextField();
        empIdField.setPromptText("Employee ID");
        Button searchButton = new Button("Search");
        styleButton(searchButton, "#2196f3");
        ListView<Employee> searchResults = new ListView<>();
        searchResults.setPrefHeight(150);
        searchResults.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Employee emp, boolean empty) {
                super.updateItem(emp, empty);
                setText(empty || emp == null ? "" : emp.getFname() + " " + emp.getLname() + " (ID: " + emp.getEmpid() + ")");
            }
        });

        // Edit Section
        Label editLabel = new Label("Edit Selected Employee:");
        editLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        TextField editFname = new TextField();
        editFname.setPromptText("First Name");
        TextField editLname = new TextField();
        editLname.setPromptText("Last Name");
        TextField editEmail = new TextField();
        editEmail.setPromptText("Email");
        Button updateButton = new Button("Update Employee");
        styleButton(updateButton, "#4caf50");
        Button deleteSelectedButton = new Button("Delete Selected");
        styleButton(deleteSelectedButton, "#f44336");
        Label updateStatus = new Label();
        updateStatus.setTextFill(Color.RED);

        // Salary Update Section
        Label salaryLabel = new Label("Update Salaries in Range:");
        salaryLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        TextField minSalaryField = new TextField();
        minSalaryField.setPromptText("Min Salary");
        TextField maxSalaryField = new TextField();
        maxSalaryField.setPromptText("Max Salary");
        TextField percentageField = new TextField();
        percentageField.setPromptText("Percentage");
        Button salaryUpdateButton = new Button("Update Salaries");
        styleButton(salaryUpdateButton, "#ff9800");

        // Reports Section
        Label reportLabel = new Label("Reports:");
        reportLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        Button payHistoryButton = new Button("Pay History");
        Button payByJobButton = new Button("Pay by Job Title");
        Button payByDivisionButton = new Button("Pay by Division");
        styleButton(payHistoryButton, "#2196f3");
        styleButton(payByJobButton, "#2196f3");
        styleButton(payByDivisionButton, "#2196f3");
        TextArea reportArea = new TextArea();
        reportArea.setPrefHeight(200);
        reportArea.setEditable(false);

        // Add Employee Section
        Label addEmployeeLabel = new Label("Add New Employee:");
        addEmployeeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        TextField addEmpIdField = new TextField();
        addEmpIdField.setPromptText("Employee ID");
        TextField addFnameField = new TextField();
        addFnameField.setPromptText("First Name");
        TextField addLnameField = new TextField();
        addLnameField.setPromptText("Last Name");
        TextField addEmailField = new TextField();
        addEmailField.setPromptText("Email");
        TextField addHireDateField = new TextField();
        addHireDateField.setPromptText("Hire Date (YYYY-MM-DD)");
        TextField addSalaryField = new TextField();
        addSalaryField.setPromptText("Salary");
        TextField addSsnField = new TextField();
        addSsnField.setPromptText("SSN");
        Button addButton = new Button("Add Employee");
        styleButton(addButton, "#4caf50");
        Label addStatusLabel = new Label();
        addStatusLabel.setTextFill(Color.RED);

        // Event Handlers
        searchResults.setOnMouseClicked(e -> {
            Employee selected = searchResults.getSelectionModel().getSelectedItem();
            if (selected != null) {
                editFname.setText(selected.getFname());
                editLname.setText(selected.getLname());
                editEmail.setText(selected.getEmail());
            }
        });

        searchButton.setOnAction(e -> {
            try {
                Integer empid = empIdField.getText().isEmpty() ? null : Integer.parseInt(empIdField.getText());
                ArrayList<Employee> results = db.searchEmployees(fnameField.getText(), lnameField.getText(), ssnField.getText(), empid);
                searchResults.getItems().clear();
                searchResults.getItems().addAll(results);
                updateStatus.setText(results.isEmpty() ? "No results found." : "Found " + results.size() + " employees.");
            } catch (SQLException | NumberFormatException ex) {
                updateStatus.setText("Search error: " + ex.getMessage());
            }
        });

        updateButton.setOnAction(e -> {
            try {
                Employee selected = searchResults.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    db.updateEmployee(selected.getEmpid(), editFname.getText(), editLname.getText(), editEmail.getText());
                    updateStatus.setText("Employee updated!");
                    searchButton.fire();
                } else {
                    updateStatus.setText("Select an employee to update.");
                }
            } catch (SQLException ex) {
                showErrorAlert("Update error: " + ex.getMessage());
            }
        });

        deleteSelectedButton.setOnAction(e -> {
            Employee selected = searchResults.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    db.deleteEmployee(selected.getEmpid());
                    updateStatus.setText("Employee deleted successfully!");
                    searchButton.fire();
                } catch (SQLException ex) {
                    showErrorAlert("Error deleting employee: " + ex.getMessage());
                }
            } else {
                updateStatus.setText("Select an employee to delete.");
            }
        });

        salaryUpdateButton.setOnAction(e -> {
            try {
                String minText = minSalaryField.getText().trim();
                String maxText = maxSalaryField.getText().trim();
                String percentText = percentageField.getText().trim();
                if (minText.isEmpty() || maxText.isEmpty() || percentText.isEmpty()) {
                    updateStatus.setText("All fields are required.");
                    return;
                }
                double min = Double.parseDouble(minText);
                double max = Double.parseDouble(maxText);
                double percent = Double.parseDouble(percentText);
                if (min < 0 || max < min || percent < 0) {
                    updateStatus.setText("Invalid salary range or percentage.");
                    return;
                }
                db.updateSalariesInRange(min, max, percent);
                updateStatus.setText("Salaries updated successfully.");
            } catch (NumberFormatException ex) {
                updateStatus.setText("Enter valid numeric values.");
            } catch (SQLException ex) {
                showErrorAlert("Database error: " + ex.getMessage());
            }
        });

        payHistoryButton.setOnAction(e -> {
            try {
                reportArea.setText(db.getPayHistoryReport(999));
            } catch (SQLException ex) {
                reportArea.setText("Error: " + ex.getMessage());
            }
        });

        payByJobButton.setOnAction(e -> {
            try {
                reportArea.setText(db.getPayByJobReport("2025-01"));
            } catch (SQLException ex) {
                reportArea.setText("Error: " + ex.getMessage());
            }
        });

        payByDivisionButton.setOnAction(e -> {
            try {
                reportArea.setText(db.getPayByDivisionReport("2025-01"));
            } catch (SQLException ex) {
                reportArea.setText("Error: " + ex.getMessage());
            }
        });

        addButton.setOnAction(e -> {
            try {
                String empidText = addEmpIdField.getText().trim();
                String fname = addFnameField.getText().trim();
                String lname = addLnameField.getText().trim();
                String email = addEmailField.getText().trim();
                String hireDate = addHireDateField.getText().trim();
                String salaryText = addSalaryField.getText().trim();
                String ssn = addSsnField.getText().trim();

                if (empidText.isEmpty() || fname.isEmpty() || lname.isEmpty() || email.isEmpty() ||
                    hireDate.isEmpty() || salaryText.isEmpty() || ssn.isEmpty()) {
                    addStatusLabel.setText("All fields are required.");
                    return;
                }

                int empid = Integer.parseInt(empidText);
                double salary = Double.parseDouble(salaryText);

                if (!hireDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    addStatusLabel.setText("Hire date must be in YYYY-MM-DD format.");
                    return;
                }

                db.addEmployee(empid, fname, lname, email, hireDate, salary, ssn);
                addStatusLabel.setText("Employee added successfully!");
                addEmpIdField.clear();
                addFnameField.clear();
                addLnameField.clear();
                addEmailField.clear();
                addHireDateField.clear();
                addSalaryField.clear();
                addSsnField.clear();
                searchButton.fire();
            } catch (NumberFormatException ex) {
                addStatusLabel.setText("Invalid numeric input for ID or salary.");
            } catch (SQLException ex) {
                showErrorAlert("Error adding employee: " + ex.getMessage());
            }
        });

        layout.getChildren().addAll(
            searchLabel, fnameField, lnameField, ssnField, empIdField, searchButton, searchResults,
            editLabel, editFname, editLname, editEmail, updateButton, deleteSelectedButton, updateStatus,
            salaryLabel, minSalaryField, maxSalaryField, percentageField, salaryUpdateButton,
            reportLabel, payHistoryButton, payByJobButton, payByDivisionButton, reportArea,
            addEmployeeLabel, addEmpIdField, addFnameField, addLnameField, addEmailField,
            addHireDateField, addSalaryField, addSsnField, addButton, addStatusLabel
        );

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f8f8ff;");
        return new VBox(scrollPane);
    }

    private void showEmployeeScreen(int empid) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f8f8ff;");

        Label titleLabel = new Label("Your Info");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));

        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);

        try {
            ResultSet rs = db.getEmployeeById(empid);
            if (rs.next()) {
                Employee emp = new Employee(
                    rs.getInt("empid"), rs.getString("Fname"), rs.getString("Lname"),
                    rs.getString("email"), rs.getString("HireDate"), rs.getDouble("Salary"),
                    rs.getString("SSN")
                );
                infoBox.getChildren().addAll(
                    new Label("Employee ID: " + emp.getEmpid()),
                    new Label("Name: " + emp.getFname() + " " + emp.getLname()),
                    new Label("Email: " + emp.getEmail()),
                    new Label("Hire Date: " + emp.getHireDate()),
                    new Label("Salary: $" + String.format("%.2f", emp.getSalary())),
                    new Label("SSN: " + emp.getSsn())
                );
            } else {
                errorLabel.setText("Employee not found.");
            }
            rs.close();
        } catch (SQLException ex) {
            errorLabel.setText("Error: " + ex.getMessage());
        }

        Button payHistoryButton = new Button("View Pay History");
        styleButton(payHistoryButton, "#2196f3");
        payHistoryButton.setOnAction(e -> {
            try {
                String report = db.getPayHistoryReport(empid);
                infoBox.getChildren().clear();
                infoBox.getChildren().add(new TextArea(report));
            } catch (SQLException ex) {
                showErrorAlert("Error: " + ex.getMessage());
            }
        });

        Button logoutButton = new Button("Logout");
        styleButton(logoutButton, "#f44336");
        logoutButton.setOnAction(e -> showLoginScreen());

        infoBox.getChildren().add(errorLabel);
        layout.getChildren().addAll(titleLabel, infoBox, payHistoryButton, logoutButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }
}