package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.Security.PasswordProtect;

import org.json.JSONArray;

import java.io.OutputStreamWriter;

/**
 * Class managing the component that allow a user to log in to the app 
 */
public class LoginActivity extends AppCompatActivity {

    private Button buttonBack, buttonLogin;
    private String reasonFail = "";
    private final int duration = Toast.LENGTH_SHORT;
    private final Database database = Database.getDatabase();
    private JSONArray users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginActivity currentActivity = this;

        buttonBack = currentActivity.findViewById(R.id.button1);
        buttonLogin = currentActivity.findViewById(R.id.button2);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkConnection()){
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
     * Check if the datas are valids and if user exists
     * @return true if everythin isgood and user is know
     */
    private boolean checkConnection(){

        String password = ((TextView)findViewById(R.id.user_search_text_id4)).getText().toString();
        String mail = ((TextView)findViewById(R.id.user_search_text_id)).getText().toString().toLowerCase();

        if(password.length() == 0 ||mail.length() == 0){
            reasonFail = "Missing(s) field";
            return false;
        }

        if(!validationEmail(mail)){
            reasonFail = "Mail is not valid";
            return false;
        }

        int index = checkUserExists(mail, password);

        if(index == -1){
            reasonFail = "User unknown";
            return false;
        }
        else if(index == -2){
            return false;
        }
        Database.getDatabase().setSelectedUserID(index);
        Database.getDatabase().createListsGames();
        writeUserIndex(index);

        return true;
    }

    /**
     * Write user index in config file
     * @param index user's index in json
     */
    private void writeUserIndex(int index){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("config.txt", getApplicationContext().MODE_PRIVATE));
            outputStreamWriter.write(String.valueOf(index));
            outputStreamWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the user exists in database
     * @param mail user mail
     * @param password user password
     * @return -2 if wrong password, -1 if user unknown, else user index in db
     */
    private int checkUserExists(String mail, String password){
        try{
            users = database.getUsers().getJSONArray("users");
            for(int i = 0; i < users.length(); i++){
                if(users.getJSONObject(i).getString("mail").compareTo(mail) == 0){
                    if(PasswordProtect.comparePassword(password, users.getJSONObject(i).getString("pwd"))){
                        return i;
                    }
                    else{
                        reasonFail = "Incorrect password";
                        return -2;
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return -1;
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