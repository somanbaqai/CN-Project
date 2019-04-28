/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnproject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Taseen Syed
 */
public class ChatFXMLController implements Initializable {

    @FXML
    private JFXTextField messagaeField;
    @FXML
    private JFXButton sendButton;
    @FXML
    private ListView<String> chatMessageList;
    public static ObservableList<String> chatMessages = FXCollections.observableArrayList();
    public static String txt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        chatMessageList.setItems(chatMessages);
        Thread gc = new Thread(new GroupChat(chatMessages));
        gc.start();

    }

    @FXML
    private void handleAttachButtonAction(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File f = fc.showOpenDialog(null);
        if (f != null) {
//            label.setText("file path: " + f.getAbsolutePath());
            messagaeField.setText("file path: " + f.getAbsolutePath());
        }
    }

    @FXML
    private void handelEnterPressedAction(ActionEvent event) {
        txt = messagaeField.getText().trim();
        GroupChat.setTXT(messagaeField.getText().trim());
        if (!txt.isEmpty()) {
            chatMessages.add(GroupChat.name + " : " + messagaeField.getText());
        }
        messagaeField.setText("");
    }

    @FXML
    private void handleSendButton(ActionEvent event) {
        txt = messagaeField.getText().trim();
        GroupChat.setTXT(messagaeField.getText().trim());
        if (!txt.isEmpty()) {
            chatMessages.add(GroupChat.name + " : " + messagaeField.getText());
        }
        messagaeField.setText("");
    }

    @FXML
    private void handleListClick(javafx.scene.input.MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            String path = chatMessageList.getSelectionModel().getSelectedItem();
            if (path.toLowerCase().contains("file path:")) {
//                chatMessages.add("item double cliked: " + path);
                Desktop.getDesktop().open(new File("E:\\Books"));
            }

        }
    }

}
