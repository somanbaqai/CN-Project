/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnproject;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author K16-3621 & K16-3639
 */
public class FXMLDocumentController implements Initializable {

    private Label label;
    @FXML
    private JFXTextField PortInput;
    @FXML
    private JFXTextField Usernameinput;
    @FXML
    private Text PortErrorField;

    private void handleButtonAction(ActionEvent event) throws IOException {
        System.out.println("You clicked me!");
        label.setText("Hello World!");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleNextButtonAction(ActionEvent event) throws IOException {
        String port = PortInput.getText();
        if (port.length() < 4) {
            PortErrorField.setText("chek port length");
        } else if(Usernameinput.getText().length() > 0) {
            GroupChat.portStr = PortInput.getText();
            GroupChat.name = Usernameinput.getText();
            ((Stage) Usernameinput.getScene().getWindow()).close();
            Parent parent = FXMLLoader.load(getClass().getResource("ChatFXML.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Home - Chat Messenger - " + PortInput.getText().trim());
            stage.setScene(new Scene(parent));
            stage.show();
        } else {
            PortErrorField.setText("write username");
        }

        System.out.println("port main: " + PortInput.getText());
    }

}
