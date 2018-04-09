package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class PassItemList {

    private static String filename = "Data.txt";

    private ObservableList<PassItem> passItems;


    public PassItemList() {
        passItems = FXCollections.observableArrayList();
    }


    public ObservableList<PassItem> getPassItems() {
        return passItems;
    }

    public void setPassItems(ObservableList<PassItem> passItems) {
        this.passItems = passItems;
    }

    public void add(PassItem item) {
        passItems.add(item);
    }

    public void remove(PassItem item) {
        passItems.remove(item);
    }

    public void loadPassItems() throws IOException {
        passItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;
        try {
            while ((input = br.readLine()) != null) {
                String[] stringPieces = input.split("\t");
                String source = stringPieces[0];
                String account = stringPieces[1];
                String password = stringPieces[2];
                PassItem newItem = new PassItem(source, account, password);
                passItems.add(newItem);
            }

        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    public void savePassItems() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter br = Files.newBufferedWriter(path);
        try {
            Iterator<PassItem> iter = passItems.iterator();
            while (iter.hasNext()) {
                PassItem item = iter.next();
                br.write(String.format("%s\t%s\t%s",
                        item.getSource(),
                        item.getAccount(),
                        item.getPassword()
                ));
                br.newLine();
            }

        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
}
