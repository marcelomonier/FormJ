package form;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
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

    static final Logger logger = Logger.getLogger("logFile");
    
    FileHandler fh;

    public FormController() {
        try {
            // Setting the logger and setting format.
            fh = new FileHandler("src/logs/logFile.log", true);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException | IOException se) {
            System.out.println("EXCEPTION: " + se);
        }

        // Strings with CSS styles as value to apply to the form.
        this.cssCorrectField = "-fx-border-color: #1aaf5d; -fx-border-radius: 3;";
        this.cssFieldError = "-fx-border-color: #ef5350; -fx-border-radius: 3;";

        // Initializing regular expressions to check the fields.
        // Regular expression to name and last name allowing spanish characters.
        this.checkName = Pattern.compile("^([A-Za-z ñáéíóúÑÁÉÍÓÚ']{2,25})$");
        // Regular expression to e-mail address.
        this.checkMail = Pattern.compile("^[_A-Za-z0-9]+([_A-Za-z0-9]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,6})$");
        // Regular expression to spanish number phone.
        this.checkPhone = Pattern.compile("^[9|6|7][0-9]{8}");
        // Regular expression to date with format dd/mm/yyyy.
        this.checkDate = Pattern.compile("^\\d{1,2}/\\d{1,2}/\\d{4}$");
        // Regular expression to spanish zip.
        this.checkZip = Pattern.compile("^(0[1-9]|[1-4][0-9]|5[0-2])[0-9]{3}$");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        alert.setTitle("Something wrong");

        // Name and last name fields only accepts letters as text input.
        nameField.addEventFilter(KeyEvent.KEY_TYPED, letterValidation(25));
        lastNameField.addEventFilter(KeyEvent.KEY_TYPED, letterValidation(25));
        phoneField.addEventFilter(KeyEvent.KEY_TYPED, numberValidation(11));
        zipField.addEventFilter(KeyEvent.KEY_TYPED, numberValidation(8));
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
            } else if (!checkRegEx(checkName, nameField.getText())) {
                setCorrectFieldStyle(mailField);
                setCorrectFieldStyle(mailRepeatField);
                nameIncorrect();
            } else if (!checkRegEx(checkName, lastNameField.getText())) {
                setCorrectFieldStyle(nameField);
                lastNameIncorrect();
            } else if (!checkRegEx(checkPhone, phoneField.getText())) {
                setCorrectFieldStyle(lastNameField);
                phoneIncorrect();
            } else if (!checkRegEx(checkDate, birthdayField.getText())) {
                setCorrectFieldStyle(phoneField);
                birthdayIncorrect();
            } else if (!checkRegEx(checkZip, zipField.getText())) {
                setCorrectFieldStyle(birthdayField);
                zipIncorrect();
            } else {
                setCorrectFieldStyle(zipField);
                submitForm(mailField.getText(), nameField.getText(), lastNameField.getText(), phoneField.getText(), birthdayField.getText(), zipField.getText());
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
        emptyFieldsStyle();
        writeInLogFile(Level.WARNING, "empty fields.");
    }
    
    //Method to set style to empty fields.
    private void emptyFieldsStyle() {
        if (mailField.getText().trim().isEmpty() || mailRepeatField.getText().trim().isEmpty()) {
            setIncorrectMailStyle();
        }
        if (nameField.getText().trim().isEmpty()) {
            setIncorrectNameStyle();
        }
        if (lastNameField.getText().trim().isEmpty()) {
            setIncorrectLastNameStyle();
        }
        if (phoneField.getText().trim().isEmpty()) {
            setIncorrectPhoneStyle();
        }
        if (birthdayField.getText().trim().isEmpty()) {
            setIncorrectBirthdayStyle();
        }
        if (zipField.getText().trim().isEmpty()) {
            setIncorrectZipStyle();
        }
    }

    // Method to show an alert when e-mail is not correct.
    private void incorrectMailAlert() {
        alert.setHeaderText("Endereço de email incorreto");
        alert.setContentText("Entre com um endereço de email válido");
        alert.show();
        setIncorrectMailStyle();
        writeInLogFile(Level.WARNING, "Email incorreto.");
    }

    // Method to show an alert when e-mails does not match.
    private void mailDoesNotMatch() {
        alert.setHeaderText("E-mail address doesn't match!");
        alert.setContentText("Re-enter e-mail address.");
        alert.show();
        setIncorrectMailStyle();
        writeInLogFile(Level.WARNING, "e-mail doesn't match.");
    }

    // Method to show an alert when name or last name is not correct.
    private void nameIncorrect() {
        alert.setHeaderText("Incorrect name.");
        alert.setContentText("You haven't entered a valid name or last name.");
        alert.show();
        setIncorrectNameStyle();
        writeInLogFile(Level.WARNING, "name incorrect.");
    }

    // Method to show an alert when last name is not correct.
    private void lastNameIncorrect() {
        alert.setHeaderText("Incorrect last name");
        alert.setContentText("You haven't entered a valid last name.");
        alert.show();
        setIncorrectLastNameStyle();
        writeInLogFile(Level.WARNING, "last name incorrect.");
    }

    // Method to show an alert when phone number is not correct.
    private void phoneIncorrect() {
        alert.setHeaderText("Número de telefone incorreto");
        alert.setContentText("Digite um telefone válido.");
        alert.show();
        setIncorrectPhoneStyle();
        writeInLogFile(Level.WARNING, "Telefone icorreto.");
    }

    // Method to show an alert when birthday is not correct.
    private void birthdayIncorrect() {
        alert.setHeaderText("Birthday date is not correct");
        alert.setContentText("Re-enter birthday date.");
        alert.show();
        setIncorrectBirthdayStyle();
        writeInLogFile(Level.WARNING, "birthday incorrect.");
    }

    // Method to show an alert when zip is not correct.
    private void zipIncorrect() {
        alert.setHeaderText("CEP inválido");
        alert.setContentText("Digite um CEP válido.");
        alert.show();
        setIncorrectZipStyle();
        writeInLogFile(Level.WARNING, "CEP incorreto");
    }

    // Method to show an alert when the form has been successfully submitted.
    private void submitForm(String mail, String name, String lastName, String phone, String birthday, String zip) {
        alert.setTitle("Congratulations!");
        alert.setHeaderText("Submitted form!");
        alert.setContentText("The form has been successfully submitted.");
        alert.show();
        writeInLogFile(Level.INFO, "The form has been successfully submitted.\n"
                + "MAIL: " + mail + "\n"
                + "NAME: " + name + "\n"
                + "LAST NAME: " + lastName + "\n"
                + "PHONE: " + phone + "\n"
                + "BIRTHDAY: " + birthday + "\n"
                + "ZIP: " + zip + "\n");
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

    /* Method to write in log file.
    *
    * @params level     level of the message.
    * @params message   message to write in log file.
    */
    private void writeInLogFile(Level level, String message) {
        logger.log(level, message);
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

            if (txt_TextField.getText().length() >= maxLength || !e.getCharacter().matches("[0-9]")) {
                e.consume();
            }
        };
    }
}
