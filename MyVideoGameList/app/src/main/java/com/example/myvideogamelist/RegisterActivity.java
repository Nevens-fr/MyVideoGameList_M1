package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.ApiGestion.User;
import com.example.myvideogamelist.Security.PasswordProtect;

import org.json.JSONArray;

import java.io.OutputStreamWriter;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonBack, buttonSignin;

    private String reasonFail = "";

    private final int PASSWORD_LENGTH = 5;

    private String password, mail, userName;

    private final Database database = Database.getDatabase();
    JSONArray users;

    private final int duration = Toast.LENGTH_LONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterActivity currentActivity = this;

        buttonBack = currentActivity.findViewById(R.id.button1);
        buttonSignin = currentActivity.findViewById(R.id.button2);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        buttonSignin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkFields()){
                    addNewUser();
                    Intent intent = new Intent(currentActivity, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    currentActivity.startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), reasonFail, duration).show();
                }
            }
        });
    }

    /**
     * Add a new user to the database
     */
    private void addNewUser(){
        try {
            User user = new User(String.valueOf(users.length()), PasswordProtect.hashPassword(password), userName, mail);
            database.getUsers().getJSONArray("users").put(user.getJSONObject());
            database.requestPost(1, null, Database.getDatabase().getUsers());
            database.setSelectedUserID(database.getUsers().getJSONArray("users").length() - 1);
            database.createListsGames();
            writeUserIndex(database.getUsers().getJSONArray("users").length() - 1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Write user index in config file
     * @param index
     */
    private void writeUserIndex(int index){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("config.txt", getApplicationContext().MODE_PRIVATE));
            outputStreamWriter.write(index);
            outputStreamWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check text fields to verify if user data (is mail format correct, password length) are ok
     * @return true if everything is good
     */
    private boolean checkFields(){
        password = ((TextView)findViewById(R.id.user_search_text_id2)).getText().toString();
        String passwordCheck = ((TextView)findViewById(R.id.user_search_text_id3)).getText().toString();
        userName = ((TextView)findViewById(R.id.user_search_text_id4)).getText().toString();
        mail = ((TextView)findViewById(R.id.user_search_text_id)).getText().toString().toLowerCase();

        if(passwordCheck.length() == 0 || password.length() == 0 || mail.length() == 0 || userName.length() == 0){
            reasonFail = "Field(s) missing";
            return false;
        }

        if(passwordCheck.compareTo(password) != 0){
            reasonFail = "Passwords differs";
            return false;
        }

        if(passwordCheck.length() < PASSWORD_LENGTH){
            reasonFail = "Password length not sufficient (5 characters minimum).";
            return false;
        }

        if(!validationEmail(mail)){
            reasonFail = "Mail is not valid";
            return false;
        }

        if(checkMailAlreadyExist(mail)){
            reasonFail = "User already exist for this mail address";
            return false;
        }

        return true;
    }

    /**
     * Check if the user already exist in the database
     * @param mail mail written by the user in the activity
     * @return true if already exists
     */
    private boolean checkMailAlreadyExist(String mail){
        try {
            users = database.getUsers().getJSONArray("users");
            for (int i = 0; i < users.length(); i++) {
                if (users.getJSONObject(i).getString("mail").compareTo(mail) == 0) {
                    return true;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if the mail is valid
     * @param email mail to check
     * @return true if the mail is valid
     */
    private boolean validationEmail(String email){
        try {
            String at = email.substring(email.indexOf("@"));
            at.substring(at.indexOf("."));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}