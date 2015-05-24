package com.example.volleyballnotes;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RecordContestActivity extends Activity {
    private Button btn_back, btn_yes;
    private EditText data_opponent, data_contest_date;
    private RadioGroup rdogrp_service, rdogrp_contest_type;
    private ListView player_list;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    // PHP file url
    String url_get_player = "http://120.105.129.103/VB_sqlAPI/execute_cmd_one_line.php";
    // define string array
    String[] parse_json_player_column = { "Name", "BackNumber" };
    String[] contest_infromation;
    String[] player_name;
    String[] player_number;
    String[] player_select_list;
    // define variable
    private static String record_type = "basic";
    private static String serve_side = "our_service";
    private static final int BASIC_RECORD = 1;
    private static final int SPECIALTY_RECORD = 2;
    private static String SIGN_IN_ACCOUNT = null;
    private static int count = 0;
    private static int IS_CHECKED = 0;
    private static int data_number = 0;
    /****/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_record_contest);
        GetUserAccount();
        GetView();
        SetListener();
        DownloadtheTableData();
        SetPlayerList();
    }

    // get widget view
    public void GetView() {
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_yes = (Button) findViewById(R.id.btn_yes);
        data_opponent = (EditText) findViewById(R.id.data_opponent);
        data_contest_date = (EditText) findViewById(R.id.data_contest_date);
        rdogrp_service = (RadioGroup) findViewById(R.id.rdogrp_service);
        rdogrp_contest_type = (RadioGroup) findViewById(R.id.rdogrp_contest_type);
        player_list = (ListView) findViewById(R.id.player_list);
    }

    // set widget listener
    public void SetListener() {
        btn_back.setOnClickListener(switchPageLinstener);
        btn_yes.setOnClickListener(switchPageLinstener);
        rdogrp_service.setOnCheckedChangeListener(serviceListener);
        rdogrp_contest_type.setOnCheckedChangeListener(contesttypeListener);
    }

    // catch user input
    public void GetUserInputData() {
        contest_infromation[0] = data_opponent.getText().toString();
        contest_infromation[1] = data_contest_date.getText().toString();
        Toolbox.LogCenter("btn_yes","opponent,contest_date",contest_infromation[0]+","+contest_infromation[1]);
    }

    // Get sign in account
    public void GetUserAccount() {
        intent = this.getIntent();
        bundle = intent.getExtras();
        SIGN_IN_ACCOUNT = bundle.getString("SIGN_IN_ACCOUNT");
    }

    // get the activity response
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BASIC_RECORD:
                    Toolbox.LogCenter("from_to","From RecordContest","To Main");
                    RecordContestActivity.this.finish();
                    break;
                case SPECIALTY_RECORD:
                    Toolbox.LogCenter("from_to","From RecordContest","To Main");
                    RecordContestActivity.this.finish();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // download the player's name and back number
    public void DownloadtheTableData() {
        // fetch all data at table
        String cmd = "SELECT* FROM table_team_member";
        String res_json = DBConnector.SQLCommandOneline(url_get_player,
                SIGN_IN_ACCOUNT, cmd);
        Toolbox.LogCenter("btn_yes","all player",res_json);
        // parse the jsonArray to get each jsonObject
        try {
            JSONArray jsonArray = new JSONArray(res_json);
            JSONObject jsonData = null;
            player_name = new String[jsonArray.length()];
            player_number = new String[jsonArray.length()];
            // parse the jsonArray to get each jsonObject
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonData = jsonArray.getJSONObject(i);
                player_name[i] = jsonData.getString("Name");
                player_number[i] = jsonData.getString("BackNumber");
                Toolbox.LogCenter("btn_yes","parse json num:player",player_name[i]+":"+player_number[i]);
            }
        } catch (Exception e) {
            Log.e("DownloadtheTableData", e.toString());
        }
    }

    // set player chose list
    public void SetPlayerList() {
        String[] build_list_str = new String[player_name.length];
        // consist the list data by number:name
        for (int x = 0; x < player_name.length; x++) {
            build_list_str[x] = player_number[x] + ":" + player_name[x];
            Toolbox.LogCenter("btn_yes","Num:Player list",build_list_str[x]);
        }
        // set player list data
        ArrayAdapter<String> adapter_player_list = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_multiple_choice,
                build_list_str);
        // set the list view to multiple choice
        player_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        player_list.setAdapter(adapter_player_list);
        // get sum of the item
        count = adapter_player_list.getCount();
        Toolbox.LogCenter("btn_yes","count",count+"");
    }

    // get starting lineup list
    public void GetTheSelectData() {
        // initial the IS_CHECKED
        IS_CHECKED = 0;
        // get the checked player back number
        player_select_list = new String[count];
        for (int index = 0; index < count; index++) {
            if (player_list.isItemChecked(index)) {
                player_select_list[index] = player_number[index];
                IS_CHECKED++;
            } else {
                player_select_list[index] = "X";
            }
        }
        Toolbox.LogCenter("btn_yes","is Check player num",""+IS_CHECKED);
    }

    // check the user's input
    public boolean CheckUserInput() {
        GetTheSelectData();
        Toolbox.LogCenter("btn_yes","IS_CHECKED",IS_CHECKED+"");
        if (IS_CHECKED != 6) {
            Toast.makeText(RecordContestActivity.this, "比賽球員需為六人",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (record_type == "basic") {
            data_number = IS_CHECKED + 2;
        }
        if (record_type == "specialty") {
            data_number = IS_CHECKED + 3;
        }
        Toolbox.LogCenter("btn_yes","info column",data_number+"");
        contest_infromation = new String[data_number];
        GetUserInputData();
        for (int x = 0; x < 2; x++) {
            if (contest_infromation[x].isEmpty()) {
                Toast.makeText(RecordContestActivity.this, "請完整填寫資料",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    // set the contest basic information
    public void SetContestInformation() {
        int start = 0;
        // if type is basic,the value will be covered
        contest_infromation[2] = serve_side;
        Toolbox.LogCenter("btn_yes","info contest serve side",serve_side);
        start = data_number - IS_CHECKED;
        Toolbox.LogCenter("btn_yes","start index",start+"");
        for (int x = 0, player_index = start; x < count; x++) {
            if (player_select_list[x] != "X") {
                contest_infromation[player_index] = player_select_list[x];
                player_index++;
                Toolbox.LogCenter("btn_yes","info player"+player_index,player_select_list[x]);
            }
        }
        // debug
        String[] info_column = { "Opponent", "Contest Date",
                "Serve Sile(Player0)" };
        for (int x = 0; x < data_number; x++) {
            if (x < 3)
                Toolbox.LogCenter("btn_yes",info_column[x],contest_infromation[x]);
            else
                Toolbox.LogCenter("btn_yes","Player" + (x - 2),contest_infromation[x]);
        }
    }

    // pass information and start record
    public void StartRecord() {
        // pass sign in account
        bundle.putString("SIGN_IN_ACCOUNT", SIGN_IN_ACCOUNT);
        // pass contest information
        bundle.putStringArray("contest_infromation", contest_infromation);
        for ( int x=0;x<contest_infromation.length;x++ ){
            Toolbox.LogCenter("btn_yes","info contest list",contest_infromation[x]);
        }
        // pass data to activity
        intent.putExtras(bundle);
        // start activity and get result
        if (record_type == "basic") {
            intent.setClass(RecordContestActivity.this,
                    BasicRecordActivity.class);
            Toolbox.LogCenter("from_to","From RecordContest ","To BasicRecord");
            startActivityForResult(intent, BASIC_RECORD);
        } else {
            intent.setClass(RecordContestActivity.this,
                    SpecialtyRecordActivity.class);
            Toolbox.LogCenter("from_to","From RecordContest","To SpecialtyRecord");
            startActivityForResult(intent, SPECIALTY_RECORD);
        }
    }

    // Listener
    // get the data of service side
    private RadioGroup.OnCheckedChangeListener serviceListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rdo_our:
                    serve_side = "our_service";
                    break;
                case R.id.rdo_they:
                    serve_side = "their_service";
                    break;
            }
        }

    };
    // get the data of record type
    private RadioGroup.OnCheckedChangeListener contesttypeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rdo_basic:
                    record_type = "basic";
                    break;
                case R.id.rdo_specialty:
                    record_type = "specialty";
                    break;
            }
        }

    };

    private Button.OnClickListener switchPageLinstener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_yes:
                    if (CheckUserInput()) {
                        SetContestInformation();
                        Toolbox.LogCenter("btn_yes","Set and check","success");
                        StartRecord();
                    }
                    break;
                case R.id.btn_back:
                    Toolbox.LogCenter("from_to","From RecordContest","To MainActivity");
                    RecordContestActivity.this.finish();
                    break;
            }
        }
    };
}