package sample;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import sample.datamodel.PassItem;
import sample.datamodel.PassItemList;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;


public class MainWin {

    @FXML
    private BorderPane mainPane;
    @FXML
    private TableView<PassItem> tableView;
    @FXML
    private TextField filterTextField;
    @FXML
    private TableColumn tableCol1;
    @FXML
    private TableColumn tableCol2;
    @FXML
    private TableColumn tableCol3;

    private PassItemList date;

    private FilteredList<PassItem> filteredList;


    public void initialize() {
        date = new PassItemList();
        try {
            date.loadPassItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

        filteredList = new FilteredList<>(date.getPassItems(), item -> filterTextField.getText().trim().isEmpty());

        SortedList<PassItem> sortedList = new SortedList<>(filteredList, Comparator.comparing(o -> o.getSource().toUpperCase()));

        tableView.setItems(sortedList);
        tableView.getSelectionModel().select(0);

        tableView.getColumns().addListener(new ListChangeListener<TableColumn<PassItem, ?>>() {
            boolean suspended;

            @Override
            public void onChanged(Change<? extends TableColumn<PassItem, ?>> c) {
                c.next();
                if (c.wasReplaced() && !suspended) {
                    this.suspended = true;
                    tableView.getColumns().setAll(tableCol1, tableCol2, tableCol3);
                    this.suspended = false;
                }
            }
        });
    }

    @FXML
    public void showCreationDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Generate new Password");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialogFxml.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DialogFxml dialogFxml = fxmlLoader.getController();

            try {
                PassItem newItem = dialogFxml.getPassItem();
                date.add(newItem);
            } catch (Exception e) {
                showCreationDialog();
            }
            trySave();
        }
    }

    @FXML
    public void handleFilterChange() {
        filteredList.setPredicate(item -> {
            String source = item.getSource();
            String account = item.getAccount();
            return filterTextField.getText().trim().isEmpty() ||
                    source.toUpperCase().trim().contains(filterTextField.getText().toUpperCase().trim()) ||
                    account.toUpperCase().trim().contains(filterTextField.getText().toUpperCase().trim());
        });
    }

    @FXML
    public void contextEdit() {
        PassItem selectedItem = tableView.getSelectionModel().getSelectedItem();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Edit");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialogFxml.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        DialogFxml dialogFxml = fxmlLoader.getController();
        dialogFxml.editItem(selectedItem);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!dialogFxml.updateItem(selectedItem)) {
                contextEdit();
            }
            trySave();
        }
    }

    @FXML
    public void contextDelete() {
        PassItem selectedItem = tableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete?");
        alert.setHeaderText(null);
        alert.setContentText("Delete record " + selectedItem.getSource() + " " + selectedItem.getAccount() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            date.remove(selectedItem);
            trySave();
        }
    }

    @FXML
    public void copyToClipboard() {
        PassItem selectedItem = tableView.getSelectionModel().getSelectedItem();
        StringSelection data = new StringSelection
                (selectedItem.getPassword());
        Clipboard cb = Toolkit.getDefaultToolkit()
                .getSystemClipboard();
        cb.setContents(data, data);
    }

    private void trySave() {
        try {
            date.savePassItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
