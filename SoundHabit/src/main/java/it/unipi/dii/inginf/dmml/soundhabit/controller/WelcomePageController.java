package it.unipi.dii.inginf.dmml.soundhabit.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import it.unipi.dii.inginf.dmml.soundhabit.model.Session;
import it.unipi.dii.inginf.dmml.soundhabit.model.User;
import it.unipi.dii.inginf.dmml.soundhabit.persistence.Neo4jDriver;
import it.unipi.dii.inginf.dmml.soundhabit.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the Welcome page
 */
public class WelcomePageController {
    @FXML private JFXTextField usernameLoginTextField;
    @FXML private JFXPasswordField passwordLoginTextField;
    @FXML private JFXTextField firstNameRegistrationTextField;
    @FXML private JFXTextField lastNameRegistrationTextField;
    @FXML private JFXTextField usernameRegistrationTextField;
    @FXML private JFXPasswordField passwordRegistrationTextField;
    @FXML private JFXPasswordField confirmPasswordRegistrationTextField;
    @FXML private Button loginButton;
    @FXML private Button registrationButton;
    private Neo4jDriver neo4jDriver;

    /**
     * Method called when the controller is initialized
     */
    public void initialize()
    {
        loginButton.setOnMouseClicked(mouseEvent -> handleLoginButtonAction(mouseEvent));
        registrationButton.setOnMouseClicked(mouseEvent -> handleRegisterButtonAction(mouseEvent));
        neo4jDriver = Neo4jDriver.getInstance();

        usernameLoginTextField.setText("jessica.evans");
        passwordLoginTextField.setText("jessica.evans");
    }

    /**
     * Method used to handle the Login button click event
     * @param actionEvent   The event that occurs when the user click the Login button
     */
    private void handleLoginButtonAction(MouseEvent actionEvent) {
        if (usernameLoginTextField.getText().equals("") || passwordLoginTextField.getText().equals(""))
        {
            Utils.showErrorAlert("You need to insert all the values!");
        }
        else
        {
            if (login(usernameLoginTextField.getText(), passwordLoginTextField.getText()))
            {
                if (!Session.getInstance().getLoggedUser().getAdmin())
                    Utils.changeScene("/discovery.fxml", actionEvent);
                else
                    Utils.changeScene("/admin.fxml", actionEvent);
            }
            else
            {
                Utils.showErrorAlert("Login failed!");
            }
        }
    }

    /**
     * Method used to handle the Register button click event
     * @param actionEvent   The event that occurs when the user click the Register button
     */
    private void handleRegisterButtonAction(MouseEvent actionEvent) {
        if ((firstNameRegistrationTextField.getText().equals("") ||
                lastNameRegistrationTextField.getText().equals("") ||
                usernameRegistrationTextField.getText().equals("") ||
                passwordRegistrationTextField.getText().equals("") ||
                confirmPasswordRegistrationTextField.getText().equals(""))
            || (!passwordRegistrationTextField.getText().equals(confirmPasswordRegistrationTextField.getText())))
        {
            Utils.showErrorAlert("There are some problems with the input data");
        }
        else
        {
            if (register(firstNameRegistrationTextField.getText(), lastNameRegistrationTextField.getText(),
                    usernameRegistrationTextField.getText(), passwordRegistrationTextField.getText()))
            {
                if (!Session.getInstance().getLoggedUser().getAdmin())
                    Utils.changeScene("/discovery.fxml", actionEvent);
                else
                    Utils.changeScene("/admin.fxml", actionEvent);
            }
            else
            {
                Utils.showErrorAlert("Registration failed! Username not available..");
            }
        }
    }

    /**
     * Function used to perform the operations needed to login a user
     * @param username  username of the user
     * @param password  password of the user
     * @return          true if the login was successful, otherwise false
     */
    private boolean login (final String username, final String password)
    {
        User user = neo4jDriver.login(username, password);
        if(user != null)
        {
            Session newSession = Session.getInstance();
            newSession.setLoggedUser(user);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Function used to perform the operations needed to register a user
     * @param firsName  first name of the user
     * @param lastName  last name of the user
     * @param username  username of the user
     * @param password  password of the user
     * @return          true if the registration was successful, otherwise false
     */
    private boolean register (final String firsName, final String lastName, final String username,
                           final String password)
    {
        try // Try to register the user, if the username is used by another one than the exception will be throw
        {
            neo4jDriver.addUser(firsName, lastName, username, password);
            Session newSession = Session.getInstance();
            User registered = new User(firstNameRegistrationTextField.getText(),
                    lastNameRegistrationTextField.getText(),
                    usernameRegistrationTextField.getText(), passwordRegistrationTextField.getText(),
                    false);
            newSession.setLoggedUser(registered);
            return true;
        }
        catch (org.neo4j.driver.exceptions.ClientException clientException)
        {
            return false;
        }
    }
}
