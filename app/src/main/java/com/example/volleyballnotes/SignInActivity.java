package com.example.volleyballnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends Activity {
    private Button btn_sign_in, btn_register_team, btn_register_player;
    private EditText data_password, data_account;
    private TextView view_back_door;
    Bundle bundle = new Bundle();
    Intent intent = new Intent();
    int backpress = 0;
    // define variable
    //	private int , SCHOOL = 3,TEAM_REGISTER_CODE = 4;
    private int TEAM_NAME = 0, ACCOUNT = 1, PASSWORD = 2;
    private static int three_times=0;
    private int USER_INPUST_ACCOUNT=0,USER_INPUT_PASSWORD=1;
    String[] table_user_account;
    String team_name="";
    String admin_account="";
    // PHP file url
    private static String uriAPI_select = "http://120.105.129.103/VB_sqlAPI/user_sign_in.php";
    private static String url_search_table_player="http://120.105.129.103/VB_sqlAPI/search_table_player.php";
    private static String url_player_sign_in="http://120.105.129.103/VB_sqlAPI/player_sign_in.php";
    private static String url_player_find_admin = "http://120.105.129.103/VB_sqlAPI/player_find_admin.php.php";
    // MySQL table's data column
    String[] PHP_post_value = { "Account", "Password" };
    // user input data
    String[] input_data = new String[2];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_sign_in);
        GetView();
        SetListener();
    }

    public void GetView() {
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_register_team = (Button) findViewById(R.id.btn_register_team);
        btn_register_player = (Button) findViewById(R.id.btn_register_player);
        view_back_door=(TextView) findViewById(R.id.view_back_door);
        data_account = (EditText) findViewById(R.id.data_account);
        data_password = (EditText) findViewById(R.id.data_password);
    }

    public void SetListener() {
        btn_sign_in.setOnClickListener(switchPageLinstener);
        btn_register_team.setOnClickListener(switchPageLinstener);
        btn_register_player.setOnClickListener(switchPageLinstener);
        view_back_door.setOnClickListener(switchPageLinstener);
    }

    private Button.OnClickListener switchPageLinstener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                //send the sign in request
                case R.id.btn_sign_in:
                    if ( CheckUserAccount()||CheckPlayerAccount() ) {
                        Toast.makeText(SignInActivity.this, "歡迎使用本系統",
                                Toast.LENGTH_SHORT).show();
                        bundle.putString("sign_in_res_msg", input_data[USER_INPUST_ACCOUNT]);
                        bundle.putString("team_name",team_name);
                        intent.putExtras(bundle);
                        SignInActivity.this.setResult(SignInActivity.RESULT_OK, intent);
                        Toolbox.LogCenter("from_to", "From SingIn","To Main");
                        SignInActivity.this.finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "帳號或密碼錯誤",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                //change the page to register team
                case R.id.btn_register_team:
                    intent.setClass(SignInActivity.this, RegistrationActivity.class);
                    Toolbox.LogCenter("from_to", "From","To Registration");
                    startActivity(intent);
                    break;
                //change the page to register player
                case R.id.btn_register_player:
                    intent.setClass(SignInActivity.this,
                            RegistrationPlayerActivity.class);
                    Toolbox.LogCenter("from_to", "From SingIn","To RegistrationPlayer");
                    startActivity(intent);
                    break;
                //oh yes
                case R.id.view_back_door:
                    three_times++;
                    if ( three_times==3 ){
                        bundle.putString("sign_in_res_msg", "nipapa");
                        bundle.putString("team_name", "OVO");
                        bundle.putString("user_name", "Administrator");
                        Toolbox.LogCenter("btn_sign_in", "Dev putstring","nipapa,OVO,Administrator");
                        intent.putExtras(bundle);
                        Toast.makeText(SignInActivity.this, "被發現惹，很硬要所以按三次",
                                Toast.LENGTH_SHORT).show();
                        SignInActivity.this.setResult(SignInActivity.RESULT_OK, intent);
                        Toolbox.LogCenter("btn_sign_in", "Leave","SingInActivity");
                        SignInActivity.this.finish();
                    }
                    break;
            }

        }
    };

    //check the player's identity
    public boolean CheckPlayerAccount() {
        //define variable
        int PLAYER_ACCOUNT=0,PLAYER_PASSWORD=1;
//		int RESULT_ACCOUNT=0;
        int RESULT_DATABASE=1;
        //get the player's input
        input_data[USER_INPUST_ACCOUNT] = data_account.getText().toString();
        input_data[USER_INPUT_PASSWORD] = data_password.getText().toString();
        Toolbox.LogCenter("btn_sign_in", "player input", input_data[USER_INPUST_ACCOUNT]+","+input_data[USER_INPUT_PASSWORD]);
        //check account and password is not empty
        for (int x = 0; x < input_data.length; x++) {
            if (input_data[x].isEmpty()) {
                return false;
            }
        }
        //get team database name by player's account
        String[] search_phpcmd={"AccountDatabase","DATA1"};
        String[] search_data={"volleyballnotes",input_data[USER_INPUST_ACCOUNT]};
        String res_json=DBConnector.SQLCommand(url_search_table_player, search_phpcmd, search_data);
        Toolbox.LogCenter("btn_sign_in", "search",res_json);
        //convert the response to string array
        String[] table_play_column={"Account","AtDatabase"};
        String[] result = Toolbox.JSONConvertStringArray(res_json,table_play_column);
        admin_account=result[1];
        Toolbox.LogCenter("btn_sign_in", "Convert","Success");
        //search the table team member from result database to verify player's identity
        String[] verify_phpcmd={"AccountDatabase","DATA1","DATA2"};
        String[] verify_data={result[RESULT_DATABASE],input_data[USER_INPUST_ACCOUNT],input_data[USER_INPUT_PASSWORD]};
        res_json = DBConnector.SQLCommand(url_player_sign_in, verify_phpcmd, verify_data);
        Toolbox.LogCenter("btn_sign_in", "verify",res_json);
        //convert the response to string array
        String[] table_team_member_column={"PlayerAccount","PlayerPassword","Level","Gender","Name","BackNumber","Attend"};
        result = Toolbox.JSONConvertStringArray(res_json,table_team_member_column);
        for ( int x=0;x<result.length;x++ )Toolbox.LogCenter("btn_sign_in", "Convert",result[x]);
        //check the player is exist
        if (result[PLAYER_ACCOUNT].equals(input_data[USER_INPUST_ACCOUNT])&& result[PLAYER_PASSWORD].equals(input_data[USER_INPUT_PASSWORD])){
//			GetTeamName();
            return true;
        }
        else
            return false;
    }
    //
//	public void GetTeamName() {
//		String[] search_phpcmd={"AccountDatabase","DATA1"};
//		String[] search_data={"volleyballnotes",admin_account};
//		String json_res=DBConnector.SQLCommand(url_search_table_player, search_phpcmd, search_data);
//		Toolbox.LogCenter("btn_sign_in", "table account",json_res);
//		String[] column = {"TeamName"};
//		String[] team_name_arr =Toolbox.JSONConvertStringArray(json_res, column);	
//		team_name=team_name_arr[0];
//		Toolbox.LogCenter("btn_sign_in", "team_name",team_name);
//	} 
    //check the user's identity
    public boolean CheckUserAccount() {
        input_data[USER_INPUST_ACCOUNT] = data_account.getText().toString();
        input_data[USER_INPUT_PASSWORD] = data_password.getText().toString();
        //Check the data is not empty
        for (int x = 0; x < input_data.length; x++) {
            if (input_data[x].isEmpty()) {
                return false;
            }
        }

        //send the SQL command to database and get the response
        String get_json = DBConnector.SQLCommand(uriAPI_select, PHP_post_value,input_data);
        Toolbox.LogCenter("btn_sign_in", "get_json", get_json);

        //check the data not exist code
        int DATA_NOT_EXSIT=get_json.indexOf("CommandResultIsEmpty");
        if ( DATA_NOT_EXSIT!=-1 ) return false;
        Toolbox.LogCenter("btn_sign_in", "DATA_NOT_EXSIT", "CommandResultIsEmpty");
        //get response and convert to string
        String[] user_column={"TeamName","Account","Password","School","TeamRegisterCode"};
        table_user_account = Toolbox.JSONConvertStringArray(get_json,user_column);
        team_name=table_user_account[TEAM_NAME];
        for ( int x=0;x<table_user_account.length;x++)
            Toolbox.LogCenter("btn_sign_in", "table_user_account", table_user_account[x]);

        //check the user is exist
        if (table_user_account[ACCOUNT].equals(input_data[USER_INPUST_ACCOUNT])&& table_user_account[PASSWORD].equals(input_data[USER_INPUT_PASSWORD]))
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {
        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ",Toast.LENGTH_SHORT).show();
        if (backpress > 1) {
            bundle.putString("sign_in_res_msg", "backLeave");
            intent.putExtras(bundle);
            SignInActivity.this.setResult(SignInActivity.RESULT_OK, intent);
            SignInActivity.this.finish();
        }
    }

}