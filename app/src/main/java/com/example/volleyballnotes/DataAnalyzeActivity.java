package com.example.volleyballnotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class DataAnalyzeActivity extends Activity {
	private Button btn_backtomain,btn_data_analyze_1,btn_data_analyze_2,btn_data_analyze_3,btn_data_analyze_4,btn_data_analyze_5;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_analyze);
		setBtn();
		btn_backtomain = (Button)findViewById(R.id.btn_backtomain);
		btn_backtomain.setOnClickListener(btnHomeListner);
	}
	public void setBtn() {
		btn_data_analyze_1 = (Button) findViewById(R.id.btn_data_analyze_1);
		btn_data_analyze_1.setOnClickListener(switchListener);
		btn_data_analyze_2 = (Button) findViewById(R.id.btn_data_analyze_2);
		btn_data_analyze_2.setOnClickListener(switchListener);
		btn_data_analyze_3 = (Button) findViewById(R.id.btn_data_analyze_3);
		btn_data_analyze_3.setOnClickListener(switchListener);
		btn_data_analyze_4 = (Button) findViewById(R.id.btn_data_analyze_4);
		btn_data_analyze_4.setOnClickListener(switchListener);
		btn_data_analyze_5 = (Button) findViewById(R.id.btn_data_analyze_5);
		btn_data_analyze_5.setOnClickListener(switchListener);
		
	}
	private Button.OnClickListener btnHomeListner=new Button.OnClickListener(){
    	public void onClick(View v){
    		finish();
    	}
    }; 
    private Button.OnClickListener switchListener=new Button.OnClickListener(){
    	public void onClick(View v){
    			switch (v.getId()) {
    			case R.id.btn_data_analyze_1:
    				Intent itn_tm1 = new Intent();
    				itn_tm1.setClass(DataAnalyzeActivity.this, DataAnalyze_1_PracticeItemActivity.class);
    				startActivity(itn_tm1);
    				break;
    			case R.id.btn_data_analyze_2:
    				Intent itn_tm2 = new Intent();
    				itn_tm2.setClass(DataAnalyzeActivity.this, DataAnalyze_2_AttendRecordActivity.class);
    				startActivity(itn_tm2);
    				break;
    			case R.id.btn_data_analyze_3:
    				Intent itn_tm3 = new Intent();
    				itn_tm3.setClass(DataAnalyzeActivity.this, DataAnalyze_3_BaseRecordActivity.class);
    				startActivity(itn_tm3);
    				break;
    			case R.id.btn_data_analyze_4:
    				Intent itn_tm4 = new Intent();
    				itn_tm4.setClass(DataAnalyzeActivity.this, DataAnalyze_4_ProfessionalRecordActivity.class);
    				startActivity(itn_tm4);
    				break;
    			case R.id.btn_data_analyze_5:
    				Intent itn_tm5 = new Intent();
    				itn_tm5.setClass(DataAnalyzeActivity.this, DataAnalyze_5_OutcomeTableActivity.class);
    				startActivity(itn_tm5);
    				break;
    			
    			}
    	}
    }; 
}
