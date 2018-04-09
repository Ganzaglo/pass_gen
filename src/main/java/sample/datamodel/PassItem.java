package sample.datamodel;

import javafx.beans.property.SimpleStringProperty;

public class PassItem {
    private SimpleStringProperty source = new SimpleStringProperty("");
    private SimpleStringProperty account= new SimpleStringProperty("");
    private SimpleStringProperty password= new SimpleStringProperty("");

    public PassItem() {
    }

    public PassItem(String source, String account, String password) {
        this.source.set(source);
        this.account.set(account);
        this.password.set(password);
    }

    public String getSource() {
        return source.get();
    }

    public SimpleStringProperty sourceProperty() {
        return source;
    }

    public void setSource(String source) {
        this.source.set(source);
    }

    public String getAccount() {
        return account.get();
    }

    public SimpleStringProperty accountProperty() {
        return account;
    }

    public void setAccount(String account) {
        this.account.set(account);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    @Override
    public String toString() {
        return "PassItem{" +
                "source=" + source +
                ", account=" + account +
                ", password=" + password +
                '}';
    }
}
