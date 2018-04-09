package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.datamodel.PassItem;

import java.util.Optional;

public class DialogFxml {

    @FXML
    private Button genButton;
    @FXML
    private TextField passTextField;
    @FXML
    private Spinner<Integer> spinner;
    @FXML
    private CheckBox upper;
    @FXML
    private CheckBox lower;
    @FXML
    private CheckBox numbers;
    @FXML
    private CheckBox symbols;
    @FXML
    private TextField sourceText;
    @FXML
    private TextField accountText;
    @FXML
    private DialogPane dialogPane;

    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public void initialize(){
    }


    @FXML
    private void genButtonClicked(){
        if(!(upper.isSelected()) && !(lower.isSelected()) && !(numbers.isSelected()) && !(symbols.isSelected())){
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("At least one check mark must be selected");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }
        passTextField.setText(PasswordGen.getPassword(spinner.getValue(),upper.isSelected(),lower.isSelected(),numbers.isSelected(),symbols.isSelected()));

    }


    public PassItem getPassItem(){
        String source = sourceText.getText();
        String account = accountText.getText();
        String password = passTextField.getText();

        if(password.isEmpty() && password.trim().isEmpty()){
            alert.setHeaderText(null);
            alert.setContentText("Input password or press Gen");
            Optional<ButtonType> result = alert.showAndWait();
            throw new NullPointerException();
        }

        if(source.isEmpty() || source.trim().isEmpty()){source= "Untitled";}
        if(account.isEmpty() || account.trim().isEmpty()){account= "Untitled";}


        return new PassItem(source,account,password);
    }

    public void editItem(PassItem item){
        sourceText.setText(item.getSource());
        accountText.setText(item.getAccount());
        passTextField.setText(item.getPassword());
    }

    public boolean updateItem(PassItem item){
        if (sourceText.getText().isEmpty() || sourceText.getText().trim().isEmpty() ) {sourceText.setText("Untitled");}
        if (accountText.getText().isEmpty() || accountText.getText().trim().isEmpty() ) {accountText.setText("Untitled");}

        if(passTextField.getText().isEmpty() && passTextField.getText().trim().isEmpty()){
            alert.setHeaderText(null);
            alert.setContentText("Input password or press Gen");
            Optional<ButtonType> result = alert.showAndWait();
            return false;
        }

        item.setSource(sourceText.getText());
        item.setAccount(accountText.getText());
        item.setPassword(passTextField.getText());
        return true;

    }
}
