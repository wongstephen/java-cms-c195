package cms.stephenwongc195.controller;

import cms.stephenwongc195.dao.Query;
import cms.stephenwongc195.model.Contact;
import cms.stephenwongc195.model.Customer;
import cms.stephenwongc195.utils.AlertUtils;
import cms.stephenwongc195.utils.Context;
import cms.stephenwongc195.utils.Navigate;
import cms.stephenwongc195.utils.TimeUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static cms.stephenwongc195.dao.ContactDao.getAllContacts;
import static cms.stephenwongc195.dao.CustomerDao.getAllCustomers;
import static cms.stephenwongc195.dao.Query.insertAppointment;

public class AddAppointmentController implements Initializable {
    @FXML
    private DatePicker startDateDp;
    @FXML
    private DatePicker endDateDp;
    @FXML
    private TextField userIdTF;
    @FXML
    private TextField descriptionTF;
    @FXML
    private TextField titleTF;
    @FXML
    private TextField locationTF;
    public ComboBox appointmentTypeCombo;
    public ComboBox startHourCombo;
    public ComboBox startMinCombo;
    public ComboBox startSecondCombo;
    public ComboBox endHourCombo;
    public ComboBox endMinuteCombo;
    public ComboBox endSecondCombo;
    public ComboBox<Customer> customerIdCombo;
    public ComboBox<Contact> contactCombo;
    String[] appointmentTypes = {"Planning", "Debrief", "Consultation", "Follow-up",  "Support", "Training", "Meeting", "Presentation", "Interview", "Feedback", "Other"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (String type : appointmentTypes) {
            appointmentTypeCombo.getItems().add(type);
        }
        userIdTF.setText(String.valueOf(Context.getUserId()));
        populateHourCombo();
        populateMinuteSecondCombo();
        populateCustomerCombo();
        populateContactCombo();
        userIdTF.setText(String.valueOf(Context.getUserId()));

    }
    /**
     * Populates the hour combo boxes with values 8-16 EST and will be translated to local time.
     * */
    private void populateHourCombo() {
        for (int i = 8; i < 16; i++) {
            startHourCombo.getItems().add(TimeUtil.convertEstToLocal(i).getHour());
            endHourCombo.getItems().add(TimeUtil.convertEstToLocal(i).getHour());
        }
    }

    /**
     * Populates the minute and second combo boxes with values 0-59.
     * */
    private void populateMinuteSecondCombo() {
        for (int i = 0; i < 60; i++) {
            startMinCombo.getItems().add(i);
            endMinuteCombo.getItems().add(i);
            startSecondCombo.getItems().add(i);
            endSecondCombo.getItems().add(i);
        }
    }

    /**
     * Handles the cancel button. Returns to the home screen.
     * @param actionEvent Cancel button click on home screen
     */
    public void handleCancelBtn(ActionEvent actionEvent) throws IOException {
    Navigate.changeScene(actionEvent, "home");
    }

    /**
     * Populates the customer combo box with all customers in the database.
     */
    private void populateCustomerCombo() {
        customerIdCombo.setItems(getAllCustomers().sorted());
    }

    /**
     * Populates the contact combo box with all contacts in the database.
     */
    private void populateContactCombo() {
        contactCombo.setItems(getAllContacts().sorted());
    }

    public void handleEndHourCombo(ActionEvent actionEvent) {
        int shclen = endHourCombo.getItems().size()-1;
        int endComboValue = (int)endHourCombo.getValue();
        if (endComboValue == (int)endHourCombo.getItems().get(shclen)) {
            endMinuteCombo.setDisable(true);
            endSecondCombo.setDisable(true);
            endMinuteCombo.setValue(0);
            endSecondCombo.setValue(0);
        } else {
            endMinuteCombo.setDisable(false);
            endSecondCombo.setDisable(false);
        }
    }

    public void handleSaveBtn(ActionEvent actionEvent) {

        List<String> exceptionArray = new ArrayList<String>();
        boolean hasException;

        if (customerIdCombo.getValue() == null) {
            hasException = true;
            exceptionArray.add("User ID cannot be empty.");
        } else {
            hasException = false;
        }

        if (contactCombo.getValue() == null) {
            hasException = true;
            exceptionArray.add("Contact cannot be empty.");
        } else {
            hasException = false;
        }

        if (titleTF.getText().isEmpty()) {
            hasException = true;
            exceptionArray.add("Title cannot be empty.");
        } else {
            hasException = false;
        }

        if (descriptionTF.getText().isEmpty()) {
            hasException = true;
            exceptionArray.add("Description cannot be empty.");
        } else {
            hasException = false;
        }

        if (appointmentTypeCombo.getValue() == null) {
            hasException = true;
            exceptionArray.add("Type cannot be empty.");
        } else {
            hasException = false;
        }

        if(startDateDp.getValue() == null) {
            hasException = true;
            exceptionArray.add("Start date cannot be empty.");
        } else {
            hasException = false;
        }

        if (startHourCombo.getValue() == null) {
            hasException = true;
            exceptionArray.add("Start hour cannot be empty.");
        } else {
            hasException = false;
        }

        if (startMinCombo.getValue() == null) {
            hasException = true;
            exceptionArray.add("Start minute cannot be empty.");
        } else {
            hasException = false;
        }

        if(endDateDp.getValue() == null) {
            hasException = true;
            exceptionArray.add("End date cannot be empty.");
        } else {
            hasException = false;
        }

        if (endSecondCombo.getValue() == null) {
            hasException = true;
            exceptionArray.add("Start second cannot be empty.");
        } else {
            hasException = false;
        }

        if (endHourCombo.getValue() == null) {
            hasException = true;
            exceptionArray.add("End hour cannot be empty.");
        } else {
            hasException = false;
        }

        if (endMinuteCombo.getValue() == null) {
            hasException = true;
            exceptionArray.add("End minute cannot be empty.");
        } else {
            hasException = false;
        }

        if (endSecondCombo.getValue() == null) {
            hasException = true;
            exceptionArray.add("End second cannot be empty.");
        } else {
            hasException = false;
        }

        if (hasException) {
            String exceptionString = String.join("\n", exceptionArray);
            AlertUtils.alertError("Please fill out all required fields before submitting the form.", exceptionString);
        } else {
            ZonedDateTime startDate = ZonedDateTime.of(startDateDp.getValue().getYear(), startDateDp.getValue().getMonthValue(), startDateDp.getValue().getDayOfMonth(), Integer.parseInt(startHourCombo.getValue().toString()), Integer.parseInt(startMinCombo.getValue().toString()),Integer.parseInt(startSecondCombo.getValue().toString()), 0, ZoneId.systemDefault());
            ZonedDateTime endDate = ZonedDateTime.of(endDateDp.getValue().getYear(), endDateDp.getValue().getMonthValue(), endDateDp.getValue().getDayOfMonth(), Integer.parseInt(endHourCombo.getValue().toString()), Integer.parseInt(endMinuteCombo.getValue().toString()),Integer.parseInt(endSecondCombo.getValue().toString()), 0, ZoneId.systemDefault());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            ZonedDateTime utcStartDateTime = startDate.withZoneSameInstant(ZoneId.of("UTC"));
            String startDateFormatted = formatter.format(utcStartDateTime);
            System.out.println(startDateFormatted);

            ZonedDateTime utcEndDateTime = endDate.withZoneSameInstant(ZoneId.of("UTC"));
            String endDateFormatted = formatter.format(utcEndDateTime);
            System.out.println(endDateFormatted);

            int recordsAdded = insertAppointment(titleTF.getText(), descriptionTF.getText(), locationTF.getText(), appointmentTypeCombo.getValue().toString(), startDateFormatted, endDateFormatted, customerIdCombo.getValue().getCustomerId(), Context.getUserId(), contactCombo.getValue().getContactId());
            System.out.println("Records added: " + recordsAdded);
            AlertUtils.alertInformation("Appointment added successfully", "Appointment has been added successfully.");
            Navigate.changeScene(actionEvent, "home");

        }
    }
}
