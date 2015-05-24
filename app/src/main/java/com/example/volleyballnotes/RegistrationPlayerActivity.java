package com.example.volleyballnotes;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistrationPlayerActivity extends Activity {
    private Button btnP_register, btnP_cancel;
    private EditText dataR_player_name, dataR_player_account,
            dataR_player_password, dataR_player_password_check,
            dataR_team_register_checker, dataR_player_back_number;
    private Spinner spn_level;
    private RadioGroup rdogrp_gender;
    //spinner's adapter and value list
    private ArrayAdapter<String> LevelList;
    private String[] level = { "社會人士", "大一", "大二", "大三", "大四", "研究所", "博士生" };
    // PHP file url
    String url_getAccDB = "http://120.105.129.103/VB_sqlAPI/get_AccDB_by_TRC.php";
    String url_add_player = "http://120.105.129.103/VB_sqlAPI/add_player.php";
    String url_add_to_table_player="http://120.105.129.103/VB_sqlAPI/add_to_table_player.php";
    // MySQL table's data column
    String[] PHP_post_value = { "AccountDatabase", "PlayerAccount",
            "PlayerPassword", "Level", "Gender", "Name", "BackNumber" };
    // string array which store player
    private String[] PlayerData = new String[7];
    private String[] adminData = new String[5];
    // define variable
    private int TeamName = 0, Account = 1, Password = 2, School = 3,
            TeamRegisterCode = 4;
    private int AccountDatabase = 0, PlayerAccount = 1, PlayerPassword = 2,
            Level = 3, Gender = 4, Name = 5, BackNumber = 6;
    private String TeamRegisterCodeCheck = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_registration_player);
        GetView();
        SetListener();
        //initial the value of gender and level
        PlayerData[Gender] = "Male";
        PlayerData[Level] = "society";
        //set spinner's adapter
        LevelList = new ArrayAdapter<String>(RegistrationPlayerActivity.this,
                android.R.layout.simple_spinner_item, level);
        spn_level.setAdapter(LevelList);
    }

    public void GetView() {
        btnP_register = (Button) findViewById(R.id.btnP_register);
        btnP_cancel = (Button) findViewById(R.id.btnP_cancel);
        dataR_player_name = (EditText) findViewById(R.id.dataR_player_name);
        dataR_player_account = (EditText) findViewById(R.id.dataR_player_account);
        dataR_player_password = (EditText) findViewById(R.id.dataR_player_password);
        dataR_player_password_check = (EditText) findViewById(R.id.dataR_player_password_check);
        dataR_team_register_checker = (EditText) findViewById(R.id.dataR_team_register_checker);
        dataR_player_back_number = (EditText) findViewById(R.id.dataR_player_back_number);
        spn_level = (Spinner) findViewById(R.id.spn_level);
        rdogrp_gender = (RadioGroup) findViewById(R.id.rdogrp_gender);
    }

    public void SetListener() {
        btnP_register.setOnClickListener(switchPageLinstener);
        btnP_cancel.setOnClickListener(switchPageLinstener);
        spn_level.setOnItemSelectedListener(levelListener);
        rdogrp_gender.setOnCheckedChangeListener(genderListener);
    }
    //get the player's grade
    AdapterView.OnItemSelectedListener levelListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view,
                                   int position, long id) {
            String temp = null;
            switch (adapterView.getSelectedItemPosition()) {
                case 0:
                    temp = "society";
                    break;
                case 1:
                    temp = "U1";
                    break;
                case 2:
                    temp = "U2";
                    break;
                case 3:
                    temp = "U3";
                    break;
                case 4:
                    temp = "U4";
                    break;
                case 5:
                    temp = "institute";
                    break;
                case 6:
                    temp = "doctor";
                    break;
            }
            PlayerData[Level] = temp;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };
    //get the player's gender
    private RadioGroup.OnCheckedChangeListener genderListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rdobtn_male:
                    PlayerData[Gender] = "Male";
                    break;
                case R.id.rdobtn_female:
                    PlayerData[Gender] = "Female";
                    break;
            }

        }

    };

    private Button.OnClickListener switchPageLinstener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnP_register:
                    if (CheckDataCorrect()) {
                        DBConnector.SQLCommand(url_add_player, PHP_post_value,PlayerData);
                        AddToTablePlayer();
                        finish();
                    }
                    break;
                case R.id.btnP_cancel:
                    finish();
                    break;
            }
        }
    };
    //add the player's account and team database name to table player
    public void AddToTablePlayer() {
        String[] add_to_table_player_phpcmd={"AccountDatabase","Account","AtDatabase"};
        String[] add_to_table_player_data={"volleyballnotes",PlayerData[PlayerAccount],PlayerData[AccountDatabase]};
        DBConnector.SQLCommand(url_add_to_table_player, add_to_table_player_phpcmd,add_to_table_player_data);
    }
    //search the player's team database name by team register code
    public String getAccountDatabase() {
        //get the player's input
        TeamRegisterCodeCheck = dataR_team_register_checker.getText()
                .toString();
        //search the team register code from table user account
        String[] getAccDB_php_cmd = { "AccountDatabase", "TeamRegisterCode" };
        String[] getAccDB_data = { "volleyballnotes", TeamRegisterCodeCheck };
        String json_response = DBConnector.SQLCommand(url_getAccDB,
                getAccDB_php_cmd, getAccDB_data);
        //check the response is not empty
        int DATA_NOT_EXSIT = json_response.indexOf("CommandResultIsEmpty");
        if (DATA_NOT_EXSIT != -1)
            return "TRC not exist";
        //check the team register code is exist
        String[] checker = JSONConvertStringArray(json_response);
        if (checker[TeamRegisterCode].equals(TeamRegisterCodeCheck))
            return checker[Account];
        else
            return "TRC not exist";
    }

    public boolean CheckDataCorrect() {
        // get player's input
        if (getAccountDatabase() != "TRC not exist") {
            PlayerData[AccountDatabase] = getAccountDatabase();
            Toast.makeText(RegistrationPlayerActivity.this,
                    PlayerData[AccountDatabase], Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegistrationPlayerActivity.this, "請確認註冊碼",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        PlayerData[PlayerAccount] = dataR_player_account.getText().toString();
        PlayerData[PlayerPassword] = dataR_player_password.getText().toString();
        PlayerData[Name] = dataR_player_name.getText().toString();
        PlayerData[BackNumber] = dataR_player_back_number.getText().toString();
        // Check the data is not empty
        for (int x = 0; x < PlayerData.length; x++) {
            if (PlayerData[x].isEmpty()) {
                Toast.makeText(RegistrationPlayerActivity.this,
                        "請填寫欄位" + PHP_post_value[x], Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        // Check Password
        String pwd, pwd_check;
        pwd = dataR_player_password.getText().toString();
        pwd_check = dataR_player_password_check.getText().toString();
        if (pwd.equals(pwd_check))
            return true;
        else {
            Toast.makeText(RegistrationPlayerActivity.this, "請確認密碼是否相同",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    //convert the json to string (old function)
    public String[] JSONConvertStringArray(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonData = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonData = jsonArray.getJSONObject(i);
            }
            adminData[TeamName] = jsonData.getString("TeamName");
            adminData[Account] = jsonData.getString("Account");
            adminData[Password] = jsonData.getString("Password");
            adminData[School] = jsonData.getString("School");
            adminData[TeamRegisterCode] = jsonData.getString("TeamRegisterCode");
            for (int x = 0; x < 5; x++)
                Log.i("UserAccountValue", adminData[x]);
        } catch (Exception e) {
            Log.e("JSONConvertStringArray", e.toString());
        }
        return adminData;
    }
}