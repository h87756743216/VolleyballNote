package com.example.volleyballnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class BasicRecordActivity extends Activity {
    private Button btn_re, btn_service, btn_receive, btn_attack, btn_block,
            btn_unknown, btn_add_point;
    private TextView view_our_team, view_opponent_team, view_our_point,
            view_opponent_point, view_service, view_receive, view_attack,
            view_block, view_unknown;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    // define variable
    private String SIGN_IN_ACCOUNT = null;
    private String[] contest_infromation;
    private String outcome=null;
    private int service = 0, receive = 0, attack = 0, block = 0, unknown = 0,
            our_point = 0, opponent_point = 0;
    //php file url
    private String url="http://120.105.129.103/VB_sqlAPI/add_basic_record.php";
    //Toolbox.LogCenter("","","");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_basic_record);
        setBtn();
        setText();
        GetAccount();
    }

    public void setBtn() {
        btn_re = (Button) findViewById(R.id.btn_re);
        btn_re.setOnClickListener(switchListener);
        btn_service = (Button) findViewById(R.id.btn_service);
        btn_service.setOnClickListener(switchListener);
        btn_receive = (Button) findViewById(R.id.btn_receive);
        btn_receive.setOnClickListener(switchListener);
        btn_attack = (Button) findViewById(R.id.btn_attack);
        btn_attack.setOnClickListener(switchListener);
        btn_block = (Button) findViewById(R.id.btn_block);
        btn_block.setOnClickListener(switchListener);
        btn_unknown = (Button) findViewById(R.id.btn_unknown);
        btn_unknown.setOnClickListener(switchListener);
        btn_add_point = (Button) findViewById(R.id.btn_add_point);
        btn_add_point.setOnClickListener(switchListener);
    }

    public void setText() {
        view_our_team = (TextView) findViewById(R.id.view_our_team);
        view_our_team.setOnClickListener(Listener);
        view_opponent_team = (TextView) findViewById(R.id.view_opponent_team);
        view_opponent_team.setOnClickListener(Listener);
        view_our_point = (TextView) findViewById(R.id.view_our_point);
        view_our_point.setOnClickListener(Listener);
        view_opponent_point = (TextView) findViewById(R.id.view_opponent_point);
        view_opponent_point.setOnClickListener(Listener);
        view_service = (TextView) findViewById(R.id.view_service);
        view_service.setOnClickListener(Listener);
        view_receive = (TextView) findViewById(R.id.view_receive);
        view_receive.setOnClickListener(Listener);
        view_attack = (TextView) findViewById(R.id.view_attack);
        view_attack.setOnClickListener(Listener);
        view_block = (TextView) findViewById(R.id.view_block);
        view_block.setOnClickListener(Listener);
        view_unknown = (TextView) findViewById(R.id.view_unknown);
        view_unknown.setOnClickListener(Listener);

    }

    public void AddDataToDatabase() {
        String player="";
        for ( int x=2;x<contest_infromation.length;x++){
            player=player+contest_infromation[x];
            if (x!=contest_infromation.length-1) player+=":";
            Toolbox.LogCenter("basic","player"+(x-2),contest_infromation[x]);
        }
        outcome = our_point+":"+opponent_point;
        Toolbox.LogCenter("basic","outcome",outcome);
        String[] php_column={"AccountDatabase","DATA1","DATA2","DATA3","DATA4","DATA5","DATA6","DATA7","DATA8","DATA9"};
        String[] data={SIGN_IN_ACCOUNT,contest_infromation[0],contest_infromation[1],outcome,service+"", receive+"",
                attack+"", block+"", unknown+"",player};
        String php_response = DBConnector.SQLCommand(url, php_column, data);
        Toolbox.LogCenter("basic","Add basic data",php_response);
    }

    // Get sign in account
    public void GetAccount() {
        intent = this.getIntent();
        bundle = intent.getExtras();
        SIGN_IN_ACCOUNT = bundle.getString("SIGN_IN_ACCOUNT");
        contest_infromation = bundle.getStringArray("contest_infromation");
        Toolbox.LogCenter("basic","BR Account",SIGN_IN_ACCOUNT);
    }

    private Button.OnClickListener switchListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_re:
                    AddDataToDatabase();
                    BasicRecordActivity.this.setResult(BasicRecordActivity.RESULT_OK);
                    Toolbox.LogCenter("from_to","From BasicRecord","To RecordContest");
                    BasicRecordActivity.this.finish();
                    break;
                case R.id.btn_service:
                    service++;
                    opponent_point++;
                    view_opponent_point.setText("" + opponent_point);
                    view_service.setText("" + service);
                    break;
                case R.id.btn_receive:
                    receive++;
                    opponent_point++;
                    view_opponent_point.setText("" + opponent_point);
                    view_receive.setText("" + receive);
                    break;
                case R.id.btn_attack:
                    attack++;
                    opponent_point++;
                    view_opponent_point.setText("" + opponent_point);
                    view_attack.setText("" + attack);
                    break;
                case R.id.btn_block:
                    block++;
                    opponent_point++;
                    view_opponent_point.setText("" + opponent_point);
                    view_block.setText("" + block);
                    break;
                case R.id.btn_unknown:
                    unknown++;
                    opponent_point++;
                    view_opponent_point.setText("" + opponent_point);
                    view_unknown.setText("" + unknown);
                    break;
                case R.id.btn_add_point:
                    our_point++;
                    view_our_point.setText("" + our_point);
                    break;
            }
        }
    };
    private TextView.OnClickListener Listener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_service:
                    service--;
                    opponent_point--;
                    view_opponent_point.setText("" + opponent_point);
                    view_service.setText("" + service);
                    break;
                case R.id.view_receive:
                    receive--;
                    opponent_point--;
                    view_opponent_point.setText("" + opponent_point);
                    view_receive.setText("" + receive);
                    break;
                case R.id.view_attack:
                    attack--;
                    opponent_point--;
                    view_opponent_point.setText("" + opponent_point);
                    view_attack.setText("" + attack);
                    break;
                case R.id.view_block:
                    block--;
                    opponent_point--;
                    view_opponent_point.setText("" + opponent_point);
                    view_block.setText("" + block);
                    break;
                case R.id.view_unknown:
                    unknown--;
                    opponent_point--;
                    view_opponent_point.setText("" + opponent_point);
                    view_unknown.setText("" + unknown);
                    break;
                case R.id.view_our_team:
                    our_point--;
                    view_our_point.setText("" + our_point);
                    break;
            }
        }
    };
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "請按比賽結束返回",Toast.LENGTH_SHORT).show();
    }
}