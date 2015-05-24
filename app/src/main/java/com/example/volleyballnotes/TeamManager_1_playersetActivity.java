package com.example.volleyballnotes;

import java.util.TooManyListenersException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TeamManager_1_playersetActivity extends Activity {
	private Button btn_back, btn_update;
	Intent intent = new Intent();
	Bundle bundle = new Bundle();
	// MySQL table's data column
	String[] PHP_post_value = {"PlayerAccount",
			"PlayerPassword", "Level", "Gender", "Name", "BackNumber" };
	// php file url
	String url_get_member_verify = "http://120.105.129.103/VB_sqlAPI/execute_cmd_one_line.php";
	// define variable
	private int TeamName = 0, Account = 1, Password = 2, School = 3,
			TeamRegisterCode = 4;
	private String SIGN_IN_ACCOUNT=null;
	//string array which store admin user
	private String[] check_member_list= new String[5];
	// which data
	 String[] which_data_acc= new String[100];
	 String[] which_data_pwd= new String[100];
	 String[] which_data_lv= new String[100];
	 String[] which_data_gen= new String[100];
	 String[] which_data_name= new String[100];
	 String[] which_data_num= new String[100];
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_team_manager_1);
		GetView();
		SetListener();
		//Get sign in account
		intent = this.getIntent();
		bundle = intent.getExtras();
		SIGN_IN_ACCOUNT=bundle.getString("SIGN_IN_ACCOUNT");
		Log.i("TeamManagerVerify", SIGN_IN_ACCOUNT);
	}

	public void GetView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_update = (Button) findViewById(R.id.btn_update);
	}

	public void SetListener() {
		btn_back.setOnClickListener(btnHomeListner);
		btn_update.setOnClickListener(btnHomeListner);
	}

	private Button.OnClickListener btnHomeListner = new Button.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_update:
				setContentView(R.layout.page_team_manager_1);
				ShowDataAtTable(GetVerifyData());
				break;
			case R.id.btn_back:
				finish();
				break;
			}
		}
	};

	public String GetVerifyData() {
		//need to get admin user account from sign in activity
		String cmd="SELECT *FROM `table_team_verify_member`";
		String res=DBConnector.SQLCommandOneline(url_get_member_verify, SIGN_IN_ACCOUNT,cmd);
		Log.i("Test Get data", res);
		return res;
	}

	public void ShowDataAtTable(String result) {
		TableLayout user_list = (TableLayout) findViewById(R.id.table_verify_member);
		user_list.setStretchAllColumns(true);
		TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TextView player_bask_number, player_name,player_grade;
		Button btn_ok = null,btn_no = null;
		try {
			int btn_id=0;
			JSONArray jsonArray = new JSONArray(result);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonData = jsonArray.getJSONObject(i);
				TableRow tr = new TableRow(TeamManager_1_playersetActivity.this);
				tr.setLayoutParams(row_layout);
				tr.setGravity(Gravity.CENTER_HORIZONTAL);
				//create new text view to show player's data
				player_grade = new TextView(TeamManager_1_playersetActivity.this);
				player_grade.setText(jsonData.getString("Level"));
				player_grade.setLayoutParams(view_layout);
				player_bask_number = new TextView(TeamManager_1_playersetActivity.this);
				player_bask_number.setText(jsonData.getString("BackNumber"));
				player_bask_number.setLayoutParams(view_layout);
				player_name = new TextView(TeamManager_1_playersetActivity.this);
				player_name.setText(jsonData.getString("Name"));
				player_name.setLayoutParams(view_layout);
				//create new button for each data
				btn_ok= new Button(TeamManager_1_playersetActivity.this);
				btn_ok.setId(btn_id);
				btn_id++;
				btn_ok.setText("O");
				btn_ok.setOnClickListener(ManyBtnListener);
				btn_no= new Button(TeamManager_1_playersetActivity.this);
				btn_no.setId(btn_id);
				btn_id++;
				btn_no.setText("X");
				btn_no.setOnClickListener(ManyBtnListener); 
				//catch all table's data
				which_data_acc[i]=jsonData.getString("PlayerAccount");;
				which_data_pwd[i]=jsonData.getString("PlayerPassword");;
				which_data_lv[i]=jsonData.getString("Level");;
				which_data_gen[i]=jsonData.getString("Gender");;
				which_data_name[i]=jsonData.getString("Name");;
				which_data_num[i]=jsonData.getString("BackNumber");;
				//add the new view to table
				tr.addView(player_grade);
				tr.addView(player_bask_number);
				tr.addView(player_name);
				tr.addView(btn_ok, 30, 60);
				tr.addView(btn_no, 30, 60);
				user_list.addView(tr); 
			} 
		} catch (Exception e) {
			Log.e("log_tagAc", e.toString());
		} 
	}
	
	private Button.OnClickListener ManyBtnListener = new Button.OnClickListener() {
		public void onClick(View v) {
			int judge=(v.getId()+2)%2;
			if ( judge==0 ) CopyDataToTeamMember(v.getId());
			else DeleteData(v.getId());
		}
	};
	
	public void CopyDataToTeamMember(int user) {
		int parse_user=(user+2)/2-1;
		String[] PHP_post = {"AccountDatabase","PlayerAccount","PlayerPassword", "Level", "Gender", "Name", "BackNumber" };
		String[] user_data={SIGN_IN_ACCOUNT,which_data_acc[parse_user],which_data_pwd[parse_user],which_data_lv[parse_user],which_data_gen[parse_user],which_data_name[parse_user],which_data_num[parse_user]};
		String url="http://120.105.129.103/VB_sqlAPI/add_player_member.php";
		DBConnector.SQLCommand(url, PHP_post, user_data);
		DeleteData(parse_user+1);
	}
	
	
	public void DeleteData(int user) {
		int parse_user=(user+1)/2-1;
		String url_delete="http://120.105.129.103/VB_sqlAPI/delete_verify.php";
		String[] PHP_delete_post={"AccountDatabase","DATA1"};
		String[] delete_data={SIGN_IN_ACCOUNT,which_data_acc[parse_user]};
		Log.i("res", DBConnector.SQLCommand(url_delete, PHP_delete_post, delete_data));
		setContentView(R.layout.page_team_manager_1);
		ShowDataAtTable(GetVerifyData());
	}
}
