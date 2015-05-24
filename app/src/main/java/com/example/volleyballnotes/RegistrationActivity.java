package com.example.volleyballnotes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
    private Button btn_register, btn_cancel;
    private EditText dataR_team_name, dataR_account, dataR_password,
            dataR_password_check, dataR_school,dataR_team_register_checker;
    private RadioGroup rdogrp_is_student;
    private TextView view_school;
    //	private RadioButton rdo_student,rdo_society;
    //PHP file url
    String url="http://120.105.129.103/VB_sqlAPI/add_useraccount.php";
    String creatDBurl="http://120.105.129.103/VB_sqlAPI/execute_cmd_one_line.php";
    //MySQL table's data column
    String[] PHP_post_value={"TeamName","Account","Password","School","TeamRegisterCode"};
    //string array which store admin user
    private String[] AdminUserData= new String[5];
    // define variable
    private int TeamName = 0, Account = 1, Password = 2, School = 3,TeamRegisterCode = 4;
    private int IS_SELECT=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_registration);
        GetView();
        SetListener();
    }

    public void GetView() {
        //text view
        view_school = (TextView)findViewById(R.id.view_school);
        //button
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        //edittext
        dataR_team_name = (EditText) findViewById(R.id.dataR_team_name);
        dataR_account = (EditText) findViewById(R.id.dataR_account);
        dataR_password = (EditText) findViewById(R.id.dataR_password);
        dataR_password_check = (EditText) findViewById(R.id.dataR_password_check);
        dataR_school = (EditText) findViewById(R.id.dataR_school);
        dataR_team_register_checker = (EditText) findViewById(R.id.dataR_team_register_checker);
        //radio group
        rdogrp_is_student = (RadioGroup) findViewById(R.id.rdogrp_is_student);
        //radio button
//		rdo_student = (RadioButton) findViewById(R.id.rdo_student);
//		rdo_society = (RadioButton) findViewById(R.id.rdo_society);
    }

    public void SetListener() {
        btn_register.setOnClickListener(switchPageLinstener);
        btn_cancel.setOnClickListener(switchPageLinstener);
        rdogrp_is_student.setOnCheckedChangeListener(isStudentListener);
    }

    //check the user is student or not
    private RadioGroup.OnCheckedChangeListener isStudentListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rdo_student:
                    view_school.setVisibility(View.VISIBLE);
                    dataR_school.setVisibility(View.VISIBLE);
                    IS_SELECT=1;
                    break;
                case R.id.rdo_society:
                    view_school.setVisibility(View.GONE);
                    dataR_school.setVisibility(View.GONE);
                    IS_SELECT=0;
                    break;
            }

        }

    };


    private Button.OnClickListener switchPageLinstener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                //send the register request
                case R.id.btn_register:
                    if ( CheckDataCorrect() ){
                        DBConnector.SQLCommand(url, PHP_post_value, AdminUserData);
                        BasicCreateDB();
                        Toast.makeText(RegistrationActivity.this,"註冊成功",Toast.LENGTH_SHORT).show();
                        Toolbox.LogCenter("btn_register", "Leave","RegistrationActivity");
                        finish();
                    }
                    break;
                //back to the sign in page
                case R.id.btn_cancel:
                    Toolbox.LogCenter("btn_register", "Leave","RegistrationActivity");
                    finish();
                    break;
            }
        }
    };
    //Create Basic User Database and Data table
    public boolean BasicCreateDB() {
        //create database which name is user account
        DBConnector.SQLCommandOneline(creatDBurl,null, "CREATE DATABASE  `"+AdminUserData[Account]+"` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci");
        String cmd = null;
        //create table_team_verify_member
        cmd ="CREATE TABLE  `table_team_verify_member` (`PlayerAccount` VARCHAR( 20 ) NOT NULL ,`PlayerPassword` VARCHAR( 20 ) NOT NULL ,`Level` VARCHAR( 10 ) NOT NULL ,`Gender` VARCHAR( 10 ) NOT NULL ,`Name` VARCHAR( 10 ) NOT NULL ,`BackNumber` VARCHAR( 5 ) NOT NULL) ENGINE = MYISAM";
        DBConnector.SQLCommandOneline(creatDBurl, AdminUserData[Account], cmd);
        //create table_team_verify_member
        cmd ="CREATE TABLE `table_team_verify_member` (`PlayerAccount` VARCHAR( 20 ) NOT NULL ,`PlayerPassword` VARCHAR( 20 ) NOT NULL ,`Level` VARCHAR( 10 ) NOT NULL ,`Gender` VARCHAR( 10 ) NOT NULL ,`Name` VARCHAR( 10 ) NOT NULL ,`BackNumber` VARCHAR( 5 ) NOT NULL) ENGINE = MYISAM";
        DBConnector.SQLCommandOneline(creatDBurl, AdminUserData[Account], cmd);
        //create table_team_property
        cmd ="CREATE TABLE `table_team_property` ( `ReasonItem` VARCHAR( 20 ) NOT NULL , `Money` INT( 10 ) NOT NULL ) ENGINE = MYISAM ";
        DBConnector.SQLCommandOneline(creatDBurl, AdminUserData[Account], cmd);
        //create table_win_record
        cmd ="CREATE TABLE `table_win_record` ( `ContestDate` VARCHAR( 10 ) NOT NULL , `ContestName` VARCHAR( 10 ) NOT NULL , `Awards` VARCHAR( 20 ) NOT NULL ) ENGINE = MYISAM ";
        DBConnector.SQLCommandOneline(creatDBurl, AdminUserData[Account], cmd);
        //create table_practice_list
        cmd ="CREATE TABLE `table_practice_list` ( `PracticeCategory` VARCHAR( 20 ) NOT NULL , `PracticeName` VARCHAR( 20 ) NOT NULL , `PracticeDescribe` VARCHAR( 300 ) NOT NULL ) ENGINE = MYISAM ";
        DBConnector.SQLCommandOneline(creatDBurl, AdminUserData[Account], cmd);
        //create table_fitness_list
        cmd ="CREATE TABLE `table_fitness_list` ( `FitnessCategory` VARCHAR( 20 ) NOT NULL , `FitnessName` VARCHAR( 20 ) NOT NULL , `FitnessDescrible` VARCHAR( 300 ) NOT NULL ) ENGINE = MYISAM ";
        DBConnector.SQLCommandOneline(creatDBurl, AdminUserData[Account], cmd);
        //create table_practice_logs
        cmd ="CREATE TABLE `table_practice_logs` ( `PracticeDate` VARCHAR( 10 ) NOT NULL , `PracticeItemName` VARCHAR( 20 ) NOT NULL , `PracticeAttendNumber` TEXT NOT NULL ) ENGINE = MYISAM ";
        DBConnector.SQLCommandOneline(creatDBurl, AdminUserData[Account], cmd);
        //create table_basic_record
        cmd ="CREATE TABLE `table_basic_record` ( `BasicRecordDate` VARCHAR( 10 ) NOT NULL , `BasicRecordOpponent` VARCHAR( 20 ) NOT NULL , `BasicRecordOutcome` VARCHAR( 20 ) NOT NULL , `BasicRecordMissService` VARCHAR( 5 ) NOT NULL , `BasicRecordMissReceive` VARCHAR( 5 ) NOT NULL , `BasicRecordMissAttack` VARCHAR( 5 ) NOT NULL , `BasicRecordMissBlock` VARCHAR( 5 ) NOT NULL , `BasicRecordUnknow` VARCHAR( 5 ) NOT NULL ,`BasicRecordPlayer` VARCHAR( 50 ) NOT NULL ) ENGINE = MYISAM ";
        DBConnector.SQLCommandOneline(creatDBurl, AdminUserData[Account], cmd);
        //create table_specialty_record
        cmd ="CREATE TABLE `table_specialty_record` ( `SpecialtyRecordDate` VARCHAR( 10 ) NOT NULL , `SpecialtyRecordOpponent` VARCHAR( 20 ) NOT NULL , `SpecialtyRecordOutcome` VARCHAR( 20 ) NOT NULL ) ENGINE = MYISAM ";
        DBConnector.SQLCommandOneline(creatDBurl, AdminUserData[Account], cmd);
        //create table_specialty_record_member
        cmd ="CREATE TABLE `table_specialty_record_member` ( `BackNumber` VARCHAR( 5 ) NOT NULL , `Name` VARCHAR( 20 ) NOT NULL , `Service` VARCHAR( 16 ) NOT NULL , `Receive` VARCHAR( 16 ) NOT NULL , `Set` VARCHAR( 16 ) NOT NULL , `Attack` VARCHAR( 16 ) NOT NULL , `Block` VARCHAR( 16 ) NOT NULL ) ENGINE = MYISAM ";
        DBConnector.SQLCommandOneline(creatDBurl, AdminUserData[Account], cmd);
        //create table_team_member
        cmd ="CREATE TABLE `table_team_member` ( `PlayerAccount` VARCHAR( 20 ) NOT NULL , `PlayerPassword` VARCHAR( 20 ) NOT NULL , `Level` VARCHAR( 10 ) NOT NULL , `Gender` VARCHAR( 10 ) NOT NULL , `Name` VARCHAR( 20 ) NOT NULL , `BackNumber` VARCHAR( 10 ) NOT NULL , `Attend` INT( 5 ) NOT NULL ) ENGINE = MYISAM ";
        String test=DBConnector.SQLCommandOneline(creatDBurl, AdminUserData[Account], cmd);
        Toolbox.LogCenter("btn_register", "BasicCreateDB",test);
        return true;
    }
    public boolean CheckDataCorrect() {
        // Get Data
        AdminUserData[TeamName]=dataR_team_name.getText().toString();
        AdminUserData[Account]=dataR_account.getText().toString();
        AdminUserData[Password]=dataR_password.getText().toString();
        AdminUserData[TeamRegisterCode]=dataR_team_register_checker.getText().toString();
        if ( IS_SELECT==1 ) AdminUserData[School]=dataR_school.getText().toString();
        else AdminUserData[School]="society";
        //Check the data is not empty
        for ( int x=0;x<AdminUserData.length;x++ ){
            if (AdminUserData[x].isEmpty()) {
                Toast.makeText(RegistrationActivity.this,"請填寫欄位"+PHP_post_value[x],Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        //Check Password
        String pwd,pwd_check;
        pwd=dataR_password.getText().toString();
        pwd_check=dataR_password_check.getText().toString();
        if ( pwd.equals(pwd_check) ) return true;
        else {
            Toast.makeText(RegistrationActivity.this, "請確認密碼是否相同",Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}