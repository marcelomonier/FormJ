package form;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Jesus Daniel Cuesta fuentes
 */
public class FormController implements Initializable {

    private Label label;
    @FXML
    private TextField mailField;
    @FXML
    private TextField mailRepeatField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField birthdayField;
    @FXML
    private TextField zipField;

    Alert alert = new Alert(AlertType.INFORMATION);

    Pattern checkName, checkMail, checkPhone, checkDate, checkZip;

    Matcher m;

    String cssCorrectField, cssFieldError;

    public FormController() {
        // Strings with CSS styles as value to apply to the form.
        this.cssCorrectField = "-fx-border-color: #1aaf5d; -fx-border-radius: 3;";
        this.cssFieldError = "-fx-border-color: #ef5350; -fx-border-radius: 3;";

        // Initializing regular expressions to check the fields.
        this.checkName = Pattern.compile("^([A-Za-z ñáéíóúÑÁÉÍÓÚ']{2,25})$");
        this.checkMail = Pattern.compile("^[_A-Za-z0-9]+([_A-Za-z0-9]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,6})$");
        // Regular expression to spanish number phone.
        this.checkPhone = Pattern.compile("^[9|6|7][0-9]{8}");
        this.checkDate = Pattern.compile("^\\d{1,2}/\\d{1,2}/\\d{4}$");
        this.checkZip = Pattern.compile("^(0[1-9]|[1-4][0-9]|5[0-2])[0-9]{3}$");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        alert.setTitle("Something wrong");

        // Name and last name fields only accepts letters as text input.
        nameField.addEventFilter(KeyEvent.KEY_TYPED, letterValidation(25));
        lastNameField.addEventFilter(KeyEvent.KEY_TYPED, letterValidation(25));
        phoneField.addEventFilter(KeyEvent.KEY_TYPED, numberValidation(9));
    }

    // Send form method
    @FXML
    private void sendForm(ActionEvent event) {

        if (mailField.getText().trim().isEmpty()
                || mailRepeatField.getText().trim().isEmpty()
                || nameField.getText().trim().isEmpty()
                || lastNameField.getText().trim().isEmpty()
                || phoneField.getText().trim().isEmpty()
                || birthdayField.getText().trim().isEmpty()
                || zipField.getText().trim().isEmpty()) {
            emptyField();
        } else if (checkRegEx(checkMail, mailField.getText())) {
            if (!mailField.getText().equals(mailRepeatField.getText())) {
                mailDoesNotMatch();
            } else {
                setCorrectFieldStyle(mailField);
                setCorrectFieldStyle(mailRepeatField);
            }

            if (!checkRegEx(checkName, nameField.getText())) {
                nameIncorrect();
            } else {
                setCorrectFieldStyle(nameField);
            }

            if (!checkRegEx(checkName, lastNameField.getText())) {
                lastNameIncorrect();
            } else {
                setCorrectFieldStyle(lastNameField);
            }
            if (!checkRegEx(checkPhone, phoneField.getText())) {
                phoneIncorrect();
            } else {
                setCorrectFieldStyle(phoneField);
            }

            if (!checkRegEx(checkDate, birthdayField.getText())) {
                birthdayIncorrect();
            } else {
                setCorrectFieldStyle(birthdayField);
            }

            if (!checkRegEx(checkZip, zipField.getText())) {
                zipIncorrect();
            } else {
                setCorrectFieldStyle(zipField);
                submitForm();
            }
        } else {
            incorrectMailAlert();
        }

    }

    /* Method to check RegEx.
    *
    * @param p      Pattern with which to compare.
    * @param str    String to check.
    * @return       true or false depending on the validation.
     */
    private boolean checkRegEx(Pattern p, String str) {
        m = p.matcher(str);
        return m.matches();
    }

    // Method to show an alert when fields are empty.
    private void emptyField() {
        alert.setHeaderText("There is empty fields!");
        alert.setContentText("You can't leave any empty field!");
        alert.show();
    }

    // Method to show an alert when e-mail is not correct.
    private void incorrectMailAlert() {
        alert.setHeaderText("Incorrect e-mail address.");
        alert.setContentText("You haven't entered a valid e-mail address.");
        alert.show();
        setIncorrectMailStyle();
    }

    // Method to show an alert when e-mails does not match.
    private void mailDoesNotMatch() {
        alert.setHeaderText("E-mail address doesn't match!");
        alert.setContentText("Re-enter e-mail address.");
        alert.show();
        setIncorrectMailStyle();
    }

    // Method to show an alert when name or last name is not correct.
    private void nameIncorrect() {
        alert.setHeaderText("Incorrect name.");
        alert.setContentText("You haven't entered a valid name or last name.");
        alert.show();
        setIncorrectNameStyle();
    }

    // Method to show an alert when last name is not correct.
    private void lastNameIncorrect() {
        alert.setHeaderText("Incorrect last name");
        alert.setContentText("You haven't entered a valid last name.");
        alert.show();
        setIncorrectLastNameStyle();
    }

    // Method to show an alert when phone number is not correct.
    private void phoneIncorrect() {
        alert.setHeaderText("Incorrect phone number");
        alert.setContentText("You haven't entered a valid phone number.");
        alert.show();
        setIncorrectPhoneStyle();
    }

    // Method to show an alert when birthday is not correct.
    private void birthdayIncorrect() {
        alert.setHeaderText("Birthday date is not correct");
        alert.setContentText("Re-enter birthday date.");
        alert.show();
        setIncorrectBirthdayStyle();
    }

    // Method to show an alert when zip is not correct.
    private void zipIncorrect() {
        alert.setHeaderText("Incorrect ZIP");
        alert.setContentText("You haven't entered a valid zip.");
        alert.show();
        setIncorrectZipStyle();
    }
    
    // Method to show an alert when the form has been successfully submitted.
    private void submitForm() {
        alert.setTitle("Congratulations!");
        alert.setHeaderText("Submitted form!");
        alert.setContentText("The form has been successfully submitted.");
        alert.show();
    }

    // Method to set a CSS style when e-mail is not correct.
    private void setIncorrectMailStyle() {
        mailField.setStyle(cssFieldError);
        mailRepeatField.setStyle(cssFieldError);
    }

    // Method to apply css style to field when name is not correct.
    private void setIncorrectNameStyle() {
        nameField.setStyle(cssFieldError);
    }

    // Method to apply css style to field when last name is not correct.
    private void setIncorrectLastNameStyle() {
        lastNameField.setStyle(cssFieldError);
    }

    // Method to set a CSS style when number phone is not correct.
    private void setIncorrectPhoneStyle() {
        phoneField.setStyle(cssFieldError);
    }

    // Method to apply css style to field when birthday date is not correct.
    private void setIncorrectBirthdayStyle() {
        birthdayField.setStyle(cssFieldError);
    }

    // Method to apply css style to field when zip is not correct.
    private void setIncorrectZipStyle() {
        zipField.setStyle(cssFieldError);
    }

    // Method to apply css style to a correct field.
    private void setCorrectFieldStyle(TextField tf) {
        tf.setStyle(cssCorrectField);
    }

    // Method to allow only letters when someone is typing a text in the field.
    public EventHandler<KeyEvent> letterValidation(final Integer maxLength) {
        return (KeyEvent e) -> {
            TextField txt_TextField = (TextField) e.getSource();

            if (txt_TextField.getText().length() >= maxLength || !e.getCharacter().matches("[A-Za-z ñáéíóúÑÁÉÍÓÚ']")) {
                e.consume();
            }
        };

    }

    // Method to allow only numbers when someone is typing a text in the field.
    public EventHandler<KeyEvent> numberValidation(final Integer maxLength) {
        return (KeyEvent e) -> {
            TextField txt_TextField = (TextField) e.getSource();

            if (txt_TextField.getText().length() >= maxLength || !e.getCharacter().matches("[0-9 -]")) {
                e.consume();
            }
        };
    }
}