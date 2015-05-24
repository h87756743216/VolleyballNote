package com.example.volleyballnotes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DataAnalyze_5_OutcomeTableActivity extends Activity {
	private Button btn_backtomain;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_data_analyze_5);
		btn_backtomain = (Button)findViewById(R.id.btn_backtomain);
		btn_backtomain.setOnClickListener(btnHomeListner);
	}
	private Button.OnClickListener btnHomeListner=new Button.OnClickListener(){
    	public void onClick(View v){
    		finish();
    	}
    }; 
}
