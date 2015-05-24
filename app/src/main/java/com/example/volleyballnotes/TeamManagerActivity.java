package com.example.volleyballnotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.volleyballnotes.R;
import com.example.volleyballnotes.R.id;
import com.example.volleyballnotes.R.layout;

public class TeamManagerActivity extends Activity {
	private Button btn_team_manager_1,btn_team_manager_2,btn_team_manager_3;
	Intent intent = new Intent();
	Bundle bundle = new Bundle();
	//define variable 
	private String SIGN_IN_ACCOUNT=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_team_manager);
		setBtn();
		//Get sign in account
		intent = this.getIntent();
		bundle = intent.getExtras();
		SIGN_IN_ACCOUNT=bundle.getString("SIGN_IN_ACCOUNT");
		
	}
	
	public void setBtn() {
		btn_team_manager_1 = (Button) findViewById(R.id.btn_team_manager_1);
		btn_team_manager_1.setOnClickListener(switchListener);
		btn_team_manager_2 = (Button) findViewById(R.id.btn_team_manager_2);
		btn_team_manager_2.setOnClickListener(switchListener);
		btn_team_manager_3 = (Button) findViewById(R.id.btn_team_manager_3);
		btn_team_manager_3.setOnClickListener(switchListener);
	}
	
	private Button.OnClickListener switchListener=new Button.OnClickListener(){
    	public void onClick(View v){
    		//send account to all activity
			bundle.putString("SIGN_IN_ACCOUNT",SIGN_IN_ACCOUNT);
    		switch (v.getId()) {
    			case R.id.btn_team_manager_1:
    				intent.setClass(TeamManagerActivity.this, TeamManager_1_playersetActivity.class);
    				break;
    			case R.id.btn_team_manager_2:
    				intent.setClass(TeamManagerActivity.this, TeamManager_2_MoneyActivity.class);
    				break;
    			case R.id.btn_team_manager_3:
    				intent.setClass(TeamManagerActivity.this, TeamManager_3_RewardActivity.class);
    				break;
    		}
    		intent.putExtras(bundle);
    		startActivity(intent);
			
    	}
    }; 
}