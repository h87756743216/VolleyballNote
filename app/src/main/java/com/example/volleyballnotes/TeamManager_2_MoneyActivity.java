package com.example.volleyballnotes;

import com.example.volleyballnotes.R;
import com.example.volleyballnotes.R.id;
import com.example.volleyballnotes.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeamManager_2_MoneyActivity extends Activity {
	private Button btn_backtomain;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_team_manager_2);
		btn_backtomain = (Button)findViewById(R.id.btn_backtomain);
		btn_backtomain.setOnClickListener(btnHomeListner);
	}
	private Button.OnClickListener btnHomeListner=new Button.OnClickListener(){
    	public void onClick(View v){
    		finish();
    	}
    }; 
}
