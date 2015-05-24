package com.example.volleyballnotes;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button btn_team_manager, btn_record_contest, btn_practice,
			btn_data_analyze, btn_suggestion, btn_exit;
	private TextView view_team_name,view_user_name;
	Intent intent = new Intent();
	Bundle bundle = new Bundle();
	//define variable
	private static final int SIGN_IN_RES = 1;
	private static final int TEAM_MANAGER_RES = 2;
	private static final int CONTEST_RES = 3;
	private static final int PRACTICE_RES = 4;
	private static final int DATA_ANALYZE_RES = 5;
	private static final int SUGGESTION_RES = 6;
	
	private String SIGN_IN_ACCOUNT=null;
	private String SIGN_IN_TEAM_NAME=null;
	private String SIGN_IN_USER_NAME=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//check the user's identity
		if ( SignInChecker() )
			setContentView(R.layout.activity_main);
		else
			finish();
		GetView();
		SetListener();
	}
	public void GetView() {
		//button
		btn_team_manager = (Button) findViewById(R.id.btn_team_manager);
		btn_record_contest = (Button) findViewById(R.id.btn_record_contest);
		btn_practice = (Button) findViewById(R.id.btn_practice);
		btn_data_analyze = (Button) findViewById(R.id.btn_data_analyze);
		btn_suggestion = (Button) findViewById(R.id.btn_suggestion);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		//text view
		view_team_name = (TextView) findViewById(R.id.view_team_name);
	}
	
	public void SetListener() {
		btn_team_manager.setOnClickListener(switchListener);
		btn_record_contest.setOnClickListener(switchListener);
		btn_practice.setOnClickListener(switchListener);
		btn_data_analyze.setOnClickListener(switchListener);
		btn_suggestion.setOnClickListener(switchListener);
		btn_exit.setOnClickListener(switchListener);
	}
	
	public boolean SignInChecker() {
		intent.setClass(MainActivity.this, SignInActivity.class);
		startActivityForResult(intent, SIGN_IN_RES);
		return true;
	}
	
	//get the activity response
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case SIGN_IN_RES:
					bundle = data.getExtras();
					String check = bundle.getString("sign_in_res_msg");
					SIGN_IN_TEAM_NAME = bundle.getString("team_name");
					view_team_name.setText(SIGN_IN_TEAM_NAME);
					Toolbox.LogCenter("main","User's team name",SIGN_IN_TEAM_NAME);
					if ( check.equals("backLeave") ){
						Toolbox.LogCenter("from_to","From Main","To OutApp");
						finish();
					} 
					else {
						SIGN_IN_ACCOUNT=check;
						Toolbox.LogCenter("main","User Account",SIGN_IN_ACCOUNT);
					}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private Button.OnClickListener switchListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			//send account to all activity
			bundle.putString("SIGN_IN_ACCOUNT",SIGN_IN_ACCOUNT);
			//code to judge activity response
			int response_msg=0;
			switch (v.getId()) {
			case R.id.btn_team_manager:
				response_msg=TEAM_MANAGER_RES;
				intent.setClass(MainActivity.this, TeamManagerActivity.class);
				break;
			case R.id.btn_record_contest:
				response_msg=CONTEST_RES;
				intent.setClass(MainActivity.this, RecordContestActivity.class);
				break;
			case R.id.btn_practice:
				response_msg=PRACTICE_RES;
				intent.setClass(MainActivity.this, PracticeActivity.class);
				break;
			case R.id.btn_data_analyze:
				response_msg=DATA_ANALYZE_RES;
				intent.setClass(MainActivity.this, DataAnalyzeActivity.class);
				break;
			case R.id.btn_suggestion:
				response_msg=SUGGESTION_RES;
				intent.setClass(MainActivity.this, SuggestionActivity.class);
				break;
			case R.id.btn_exit:
				onBackPressed();
				break;
			}
			if (v.getId() != R.id.btn_exit){
				intent.putExtras(bundle);
				Toolbox.LogCenter("main","bundle","send success");
				startActivityForResult(intent,response_msg);
			}
				
		}
	};
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("Really Exit?")
				.setMessage("Are you sure you want to exit?")
				.setNegativeButton(android.R.string.no, null)
				.setPositiveButton(android.R.string.yes, new OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						MainActivity.super.onBackPressed();
					}
				}).create().show();
	}
}