package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import javax.activation.FileDataSource;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.MainActivity; // Importa la MainActivity

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;

public class BookingActivity2 extends AppCompatActivity {
    String[] required_permissions = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES
    };

    public boolean is_storage_image_permitted;

    private static final int REQUEST_CODE_PERMISSIONS = 1001;


    private static final int REQUEST_CODE = 1;

    final String[] selectedOption = new String[1];
    public String fileName = null;
    public String absolutePath, filePath;
    String textBeforeParentheses, textInParentheses;
    private EditText editTextName;
    private EditText editEmailName;
    private EditText editCarrera;
    private AutoCompleteTextView autoCompleteTextViewDiscapacidad;

    private TextView editTipoDiscapacidad;

    private Button buttonSelectDate;
    private Button buttonSelectTime;
    private TextView textViewSelectedDate;
    private TextView textViewSelectedTime;
    private AutoCompleteTextView autoCompleteTextViewLocation;
    private CheckBox checkBox;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private BookingDatabaseHelper databaseHelper;

    private Button buttonSelectDateEnd;
    private Button buttonSelectTimeEnd;
    private TextView textViewSelectedDateEnd;
    private TextView textViewSelectedTimeEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking2);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSIONS);

        requestPermision();


        editTextName = findViewById(R.id.editTextName);
        editEmailName = findViewById(R.id.editEmailName);
        editCarrera = findViewById(R.id.editCarrera);
        autoCompleteTextViewDiscapacidad = findViewById(R.id.autoCompleteTextViewDiscapacidad); // Cambia el ID del AutoCompleteTextView
        buttonSelectDate = findViewById(R.id.buttonSelectDate);
        buttonSelectTime = findViewById(R.id.buttonSelectTime);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        textViewSelectedTime = findViewById(R.id.textViewSelectedTime);
        checkBox = findViewById(R.id.checkbox);
        buttonSelectDateEnd = findViewById(R.id.buttonSelectDateEnd);
        buttonSelectTimeEnd = findViewById(R.id.buttonSelectTimeEnd);
        textViewSelectedDateEnd = findViewById(R.id.textViewSelectedDateEnd);
        textViewSelectedTimeEnd = findViewById(R.id.textViewSelectedTimeEnd);
        editTipoDiscapacidad = findViewById(R.id.editTipoDiscapacidad);


        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        databaseHelper = new BookingDatabaseHelper(this);


        buttonSelectDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerEnd();
            }
        });

        buttonSelectTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerEnd();
            }
        });


        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        buttonSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        String[] opciones = new String[]{"Rectoría (Av. Garza Sada)", "Aulas IV (Av. Luis Elizondo)", "CIAP (Calle García Roel)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        autoCompleteTextViewLocation = findViewById(R.id.autoCompleteTextViewLocation);
        autoCompleteTextViewLocation.setAdapter(adapter);
        autoCompleteTextViewLocation.setInputType(InputType.TYPE_NULL);

        autoCompleteTextViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextViewLocation.showDropDown();
            }
        });

        autoCompleteTextViewLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedOption[0] = parent.getItemAtPosition(position).toString();
                String text = selectedOption[0];
                int start = text.indexOf('(');
                int end = text.lastIndexOf(')');
                textBeforeParentheses = start > 0 ? text.substring(0, start).trim() : "";
                textInParentheses = start >= 0 && end > start ? text.substring(start + 1, end).trim() : "";
            }
        });

        String[] discapacidadOpciones = new String[]{"Temporal", "Permanente"}; // Opciones del AutoCompleteTextView de discapacidad
        ArrayAdapter<String> discapacidadAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, discapacidadOpciones);
        autoCompleteTextViewDiscapacidad.setAdapter(discapacidadAdapter);
        autoCompleteTextViewDiscapacidad.setInputType(InputType.TYPE_NULL);

        autoCompleteTextViewDiscapacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextViewDiscapacidad.showDropDown();
            }
        });

        Button btnSelectFile = findViewById(R.id.buttonSelectFile);
        btnSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Aquí puedes abrir un explorador de archivos o una actividad para que el usuario seleccione un archivo
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Establece el tipo de archivo que deseas permitir que el usuario seleccione, por ejemplo, "image/*" para imágenes
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
    }

    public boolean permissionResultCheck() {

        return is_storage_image_permitted;
    }

    public void requestPermision() {
        if (ContextCompat.checkSelfPermission(BookingActivity2.this, required_permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, required_permissions[0] + "Granted");

            is_storage_image_permitted = true;
        }
        else {
            request_permission_launcger_storage_images.launch(required_permissions[0]);
        }
    }

    private ActivityResultLauncher<String> request_permission_launcger_storage_images =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                if(isGranted){
                    Log.d(TAG, required_permissions[0]+ " Granted");
                }
                else{
                    is_storage_image_permitted = false;
                    Log.d(TAG, required_permissions[0]+ " Not Granted");

                }
                    });



    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String selectedDate = dateFormat.format(calendar.getTime());
                textViewSelectedDate.setText("Fecha: " + selectedDate);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }



    private void showTimePicker() {
        TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                String selectedTime = timeFormat.format(calendar.getTime());
                textViewSelectedTime.setText("Hora: " + selectedTime);
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeListener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    private void showDatePickerEnd() {
        DatePickerDialog.OnDateSetListener dateEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String selectedDateEnd = dateFormat.format(calendar.getTime());
                textViewSelectedDateEnd.setText("Fecha: " + selectedDateEnd);
            }
        };

        DatePickerDialog datePickerDialogEnd = new DatePickerDialog(this, dateEndListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialogEnd.show();
    }

    private void showTimePickerEnd() {
        TimePickerDialog.OnTimeSetListener timeEndListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                String selectedTimeEnd = timeFormat.format(calendar.getTime());
                textViewSelectedTimeEnd.setText("Hora: " + selectedTimeEnd);
            }
        };

        TimePickerDialog timePickerDialogEnd = new TimePickerDialog(this, timeEndListener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialogEnd.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Aquí obtienes la URI del archivo seleccionado por el usuario
            Uri uri = data.getData();

            // Puedes realizar las operaciones necesarias con el archivo seleccionado, como cargarlo en un servidor, procesarlo, etc.
            // Por ejemplo, puedes obtener la ruta del archivo:
            String fileName = getFileNameFromUri(uri);
            filePath = getPathFromUri(uri);
            File file = new File(this.getFilesDir(), fileName);
            absolutePath = file.getAbsolutePath();
            // Muestra el nombre del archivo en el TextView
            TextView txtFileName = findViewById(R.id.txtFileName);
            txtFileName.setText(fileName);
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }

    private String getFileNameFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex != -1) {
                fileName = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return fileName;
    }





    public void buttonSendEmail(View view) {
        String emailstring;
        String name = editTextName.getText().toString().trim();
        String matricula = editEmailName.getText().toString().trim();
        String selectedDate = textViewSelectedDate.getText().toString().trim();
        String selectedTime = textViewSelectedTime.getText().toString().trim();
        boolean sunflowerCard = checkBox.isChecked();
        String access = textBeforeParentheses;
        String location = textInParentheses;
        String carrera = editCarrera.getText().toString().trim();
        String discapacidad = autoCompleteTextViewDiscapacidad.getText().toString().trim(); // Lee el input del AutoCompleteTextView de discapacidad
        String tipodiscapacidad = editTipoDiscapacidad.getText().toString().trim();
        String selectedDateEnd = textViewSelectedDateEnd.getText().toString().trim();
        String selectedTimeEnd = textViewSelectedTimeEnd.getText().toString().trim();

        if (sunflowerCard) {
            emailstring = "Esta persona informó que cuenta con una discapacidad invisible y puede requerir soporte y/o atención adicional al momento de recoger su silla";
        } else {
            emailstring = "Esta persona no informó contar con una Sunflower Card";
        }

        if (name.isEmpty() || matricula.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty() || access.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            Booking booking = new Booking(name, matricula, selectedDate, selectedTime, access, sunflowerCard, location);
            long result = databaseHelper.addBooking(booking);

            if (result != -1) {
                Toast.makeText(this, "Reserva realizada con éxito", Toast.LENGTH_SHORT).show();
                clearFields();

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Los permisos no se han otorgado, solicítalos
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_PERMISSIONS);
                    System.out.println("no");
                } else {
                    // Los permisos ya se han otorgado, realiza la acción deseada
                    System.out.println("yes");
                    System.out.println(absolutePath);
                    sendEmail(name, matricula, carrera, tipodiscapacidad, discapacidad, selectedDate, selectedTime, selectedDateEnd, selectedTimeEnd, access, emailstring);
                }

                System.out.println(absolutePath);
                sendEmail(name, matricula, carrera, tipodiscapacidad, discapacidad, selectedDate, selectedTime, selectedDateEnd, selectedTimeEnd, access, emailstring);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Error al realizar la reserva", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendEmail(String name, String matricula,String carrera,String tipodiscapacidad, String discapacidad,String selectedDate, String selectedTime, String selectedDateEnd, String selectedTimeEnd, String access, String emailstring) {
        try {
            String stringSenderEmail = "reservacion.sillas@gmail.com";
            String stringReceiverEmail = "reservacion.sillas@gmail.com";
            String stringPasswordSenderEmail = "agktpdzbcgxocpol";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));


            mimeMessage.setSubject("Nueva Reservación de Silla de Ruedas");
            String emailBody = "<b>Se registró la siguiente reservación:</b> <br><br>" +
                    "<b>Nombre:</b> " + name + "<br><br>" +
                    "<b>Matricula/Nómina:</b> " + matricula + "<br><br>" +
                    "<b>Carrera/Departamento:</b> " + carrera + "<br><br>" +
                    "<b>Dispacidad:</b> " + discapacidad + "<br><br>" +
                    "<b>Tipo de Discapacidad:</b> " + tipodiscapacidad + "<br><br>" +
                    "<b>Acceso:</b> " + access + "<br><br>" +
                    "<b>Inicio de uso:</b><br><br>" + selectedDate + " " + selectedTime + "<br><br>" +
                    "<b>Termino de uso:</b><br><br>" + selectedDateEnd + " " + selectedTimeEnd + "<br><br>" +
                    emailstring;
            mimeMessage.setContent(emailBody, "text/html");



            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }

    private void clearFields() {
        editTextName.setText("");
        editEmailName.setText("");
        textViewSelectedDate.setText("");
        textViewSelectedTime.setText("");
        autoCompleteTextViewLocation.setText("");
        checkBox.setChecked(false);
    }
}