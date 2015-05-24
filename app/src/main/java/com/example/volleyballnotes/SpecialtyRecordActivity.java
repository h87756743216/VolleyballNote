package com.example.volleyballnotes;

import java.security.PublicKey;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class SpecialtyRecordActivity extends Activity {
    private Button btn_continue, btn_get_point, btn_lose_point, btn_change;
    private TextView view_atk_def, view_our, view_opponent, view_our_opint,
            view_opponent_point, view_player1, view_player2, view_player3,
            view_player4, view_player5, view_player6, view_player_l;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    TextView[] player = new TextView[7];
    // PHP file url
    String url_get_player = "http://120.105.129.103/VB_sqlAPI/execute_cmd_one_line.php";
    // define variable
    //initial
    private String[] contest_infromation;
    //player list
    private String[] all_player_name;
    private String[] all_player_num;
    private String[] out_court_list;
    private String[] on_court_list = new String[6];;
    private String[] on_court_player_num = new String[6];
    private String[] on_court_player_name = new String[6];
    //player's performance
    private int[][] service = new int[4][6];
    private int[][] receive = new int[4][6];
    private int[][] set = new int[4][6];
    private int[][] attack = new int[4][6];
    private int[][] block = new int[4][6];
    //contest
    private String outcome = null;
    private String order_stack="";
    private int[] press_times_list={0,0,0,0,0,0};
    private String serve_side;
    private String Side;//{"AttackSide","DefenseSide"}
    //user
    private String SIGN_IN_ACCOUNT = null;
    //status
    private int result_performance=0;
    private String MODE = "contest_mode";
    private int GET=0,GOOD=1,BAD=2,LOSE=3;
    private boolean THREE_PLAYER=false;
    private boolean SETTLE_ACCOUNT=false;
    private boolean FIRST_BALL=true;
    private int result=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_specialty_record);
        setBtn();
        setText();
        // setSpr();
        GetBasicContestInformation();
        SetBasicContestData();
    }

    public void setBtn() {
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(switchListener);
        btn_get_point = (Button) findViewById(R.id.btn_get_point);
        btn_get_point.setOnClickListener(switchListener);
        btn_lose_point = (Button) findViewById(R.id.btn_lose_point);
        btn_lose_point.setOnClickListener(switchListener);
        btn_change = (Button) findViewById(R.id.btn_change);
        btn_change.setOnClickListener(switchListener);

    }

    public void setText() {
        view_atk_def = (TextView) findViewById(R.id.view_atk_def);
        view_atk_def.setOnClickListener(Listener);
        view_our = (TextView) findViewById(R.id.view_our);
        view_our.setOnClickListener(Listener);
        view_opponent = (TextView) findViewById(R.id.view_opponent);// ID是否該改為view_opponent
        view_opponent.setOnClickListener(Listener);
        view_our_opint = (TextView) findViewById(R.id.view_our_opint);
        view_our_opint.setOnClickListener(Listener);
        view_opponent_point = (TextView) findViewById(R.id.view_opponent_point);
        view_opponent_point.setOnClickListener(Listener);
        view_player1 = (TextView) findViewById(R.id.view_player1);
        view_player1.setOnClickListener(playerStatusListener);
        view_player2 = (TextView) findViewById(R.id.view_player2);
        view_player2.setOnClickListener(playerStatusListener);
        view_player3 = (TextView) findViewById(R.id.view_player3);
        view_player3.setOnClickListener(playerStatusListener);
        view_player4 = (TextView) findViewById(R.id.view_player4);
        view_player4.setOnClickListener(playerStatusListener);
        view_player5 = (TextView) findViewById(R.id.view_player5);
        view_player5.setOnClickListener(playerStatusListener);
        view_player6 = (TextView) findViewById(R.id.view_player6);
        view_player6.setOnClickListener(playerStatusListener);
        view_player_l = (TextView) findViewById(R.id.view_player_l);
        view_player_l.setOnClickListener(playerStatusListener);
        player[0]=view_player1;
        player[1]=view_player2;
        player[2]=view_player3;
        player[3]=view_player4;
        player[4]=view_player5;
        player[5]=view_player6;
        player[6]=view_player_l;
    }


    public void GetBasicContestInformation() {
        // Get sign in account
        intent = this.getIntent();
        bundle = intent.getExtras();
        SIGN_IN_ACCOUNT = bundle.getString("SIGN_IN_ACCOUNT");
        contest_infromation = bundle.getStringArray("contest_infromation");
        // Get starter
        for (int x = 0; x < 6; x++) {
            on_court_player_num[x] = contest_infromation[x + 3];
        }

        // Get all player
        DownloadtheTableData();
        // Get player out court
        Toolbox.LogCenter("Specialty","Player number", all_player_name.length + "");
        Toolbox.LogCenter("Specialty","out_court_list[" + (all_player_name.length - 6) + "]", " ");

        out_court_list = new String[all_player_name.length - 6];
        for (int x = 0, on = 0, out = 0; x < all_player_name.length; x++) {
            boolean starter = false;
            String player = all_player_num[x] + ":" + all_player_name[x];
            for (int y = 0; y < 6; y++) {
                if (all_player_num[x].equals(on_court_player_num[y])) {
                    on_court_list[on] = player;
                    on++;
                    starter = true;
                    break;
                }
            }
            if (!starter) {
                out_court_list[out] = player;
                out++;
            }
        }
        Log.i("Success", "Success catch chair");
        // debug
        for (int x = 0; x < 6; x++)
            Toolbox.LogCenter("Specialty","Starter_" + x, on_court_list[x]);
        for (int x = 0; x < all_player_name.length - 6; x++)
            Toolbox.LogCenter("Specialty","Chair_" + x, out_court_list[x]);
    }

    public void DownloadtheTableData() {
        // fetch all data at table
        String cmd = "SELECT* FROM table_team_member";
        String res_json = DBConnector.SQLCommandOneline(url_get_player,
                SIGN_IN_ACCOUNT, cmd);
        // parse the jsonArray to get each jsonObject
        try {
            JSONArray jsonArray = new JSONArray(res_json);
            JSONObject jsonData = null;
            all_player_name = new String[jsonArray.length()];
            all_player_num = new String[jsonArray.length()];
            // parse the jsonArray to get each jsonObject
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonData = jsonArray.getJSONObject(i);
                all_player_name[i] = jsonData.getString("Name");
                all_player_num[i] = jsonData.getString("BackNumber");
            }
        } catch (Exception e) {
            Log.e("DownloadtheTableData", e.toString());
        }
    }

    public void SetBasicContestData() {
        // Set team name
        view_our.setText(SIGN_IN_ACCOUNT);
        // Set opponent team name
        view_opponent.setText(contest_infromation[0]);
        // Set player's back number
        for ( int x=0;x<6;x++ ){
            player[x].setText(contest_infromation[3+x]);
        }
        // view_player_l.setText(contest_basic_data[0]);
        if ( contest_infromation[2] == "our_service"){
            Side="AttackSide";
            view_atk_def.setText("攻擊方");
        }
        else {
            Side="DefenseSide";
            view_atk_def.setText("防守方");
        }
        //initial
        for ( int x=0;x<4;x++ ){
            for ( int y=0;y<6;y++ ){
                service[x][y]=0;
                receive[x][y]=0;
                set[x][y]=0;
                attack[x][y]=0;
                block[x][y]=0;
            }
        }
    }

    //show the list to change player
    public void changePlayerDialog(final int which_player) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SpecialtyRecordActivity.this);
        //set the title icon
        //builderSingle.setIcon(R.drawable.ic_launcher);
        //set title
        builderSingle.setTitle("球員名單");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                SpecialtyRecordActivity.this,android.R.layout.select_dialog_singlechoice);
        //add data to list view
        for ( int x=0;x<out_court_list.length;x++ ){
            arrayAdapter.add(out_court_list[x]);
        }
        //button on click listener
        builderSingle.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] tokens = out_court_list[which].split(":");
                        player[which_player].setText(tokens[0]);
                        String swap = out_court_list[which];
                        out_court_list[which]=on_court_list[which_player];
                        on_court_list[which_player]=swap;
                        MODE=ChangeListener(MODE);
                    }
                });
        builderSingle.show();
    }
    //change status color
    public int ChangeColorByPressTimes(int which_player,int press_times) {
        int color_status = press_times%3;
        if ( THREE_PLAYER || SETTLE_ACCOUNT ){
            Toolbox.LogCenter("Specialty","Starting ", order_stack);
            String[] token  = order_stack.split(":");
            int[] change_status_play=new int[3];
            for ( int x=0;x<3;x++ )  {
                change_status_play[x]=Integer.parseInt(token[x]);
                Toolbox.LogCenter("Specialty","Changed status player", change_status_play[x]+"");
            }
            if ( change_status_play[0]==which_player ||  change_status_play[1]==which_player || change_status_play[2]==which_player){
                Toolbox.LogCenter("Specialty","Changed status for exist player", "change");
            }
            else return color_status;

        }
        switch (color_status) {
            case 0: player[which_player].setTextColor(android.graphics.Color.BLACK); break;
            case 1: player[which_player].setTextColor(android.graphics.Color.BLUE); break;
            case 2: player[which_player].setTextColor(android.graphics.Color.RED); break;
        }
        return color_status;
    }
    //change two mode
    public String ChangeListener(String current_mode) {
        if (current_mode == "contest_mode"){
            for ( int x=0;x<7;x++ ) player[x].setOnClickListener(changePlayerListener);
            for ( int x=0;x<7;x++ ) player[x].setTextColor(android.graphics.Color.LTGRAY);
            return "changer_player_mode";
        }
        else{
            for ( int x=0;x<7;x++ ) player[x].setOnClickListener(playerStatusListener);
            for ( int x=0;x<7;x++ ) player[x].setTextColor(android.graphics.Color.BLACK);
        }
        return "contest_mode";
    }
    //
    public void SettleAccountsABall() {

    }
    //analyze the performance about player
    public void AnalyzePerformance() {
        //get the data

        if ( Side=="AttackSide" ){
            AttackJudge();
        }
        if ( Side=="DefenseSide"){
            DefenseJudge();
        }
        //initial
        for ( int x=0;x<6;x++ ){
            player[x].setTextColor(android.graphics.Color.BLACK);
            press_times_list[x]=0;
        }
        order_stack="";
    }
    //DefenseJudge
    public void DefenseJudge(){

    }
    //AttackJudge
    public void AttackJudge() {
        String[] order = order_stack.split(":");
        int[] which_player= new int[order.length];
        for ( int x=0;x<order.length;x++ ){
            which_player[x]=Integer.parseInt(order[x]);
        }
        Toolbox.LogCenter("Specialty","Player need to anaylze", order.length+"");
        //there will has order.length player update performance
        for ( int x=0;x<6;x++ ){
            //the player x's performance is press_times_list[x]
            if ( press_times_list[x]!=0 ){
                //get the player x's order
                for ( int y=0;y<order.length;y++ ){
                    //the order of player x is y
                    if (x==which_player[y]){
                        if ( SETTLE_ACCOUNT ){
                            //get final player
                            if ( y==order.length-1) result_performance=result;
                            else result_performance=press_times_list[x];
                        }
                        else{
                            result_performance=press_times_list[x];
                        }
                        //order y in player number=n means
                        switch (order.length){
                            //in one player
                            case 1:
                                //the first order means attack
                                // player x attack and result performance is result_performance
                                attack[result_performance][x]++;
                                Toolbox.LogCenter("Specialty","Player"+x,"attack performance is "+result_performance);
                                break;
                            //in two player
                            case 2:
                                //order y means
                                switch (y){
                                    //order first means set
                                    case 0: set[result_performance][x]++;
                                        Toolbox.LogCenter("Specialty","Player"+x,"set performance is "+result_performance);
                                        break;
                                    //order second means attack
                                    case 1: attack[result_performance][x]++;
                                        Toolbox.LogCenter("Specialty","Player"+x,"attack performance is "+result_performance);
                                        break;
                                }
                                break;
                            //in three player
                            case 3:
                                //order y means
                                switch (y){
                                    //order first means receive
                                    case 0: receive[result_performance][x]++;
                                        Toolbox.LogCenter("Specialty","Player"+x,"receive performance is "+result_performance);
                                        break;
                                    //order second means set
                                    case 1: set[result_performance][x]++;
                                        Toolbox.LogCenter("Specialty","Player"+x,"set performance is "+result_performance);
                                        break;
                                    //order third means attack
                                    case 2: attack[result_performance][x]++;
                                        Toolbox.LogCenter("Specialty","Player"+x,"attack performance is "+result_performance);
                                        break;
                                }
                                break;
                        }
                        //has been settle account one ball performance
                        break;
                    }
                }
            }

        }
    }

    public void Serve() {
        if ( FIRST_BALL ){

        }
        player[5].setTextColor(android.graphics.Color.YELLOW);
        if ( SETTLE_ACCOUNT ) {
            if ( result == LOSE ) Side="DefenseSide";
            service[result][5]++;
        }
        else {
            service[press_times_list[5]][5]++;
        }

    }
    public void ConsistOrderStack() {
        for ( int x=0;x<6&&!THREE_PLAYER;x++ ){
            if ( press_times_list[x]!=0 &&press_times_list[x]==1 ){
                if ( order_stack.indexOf(x+":")>=0 ){
//					Toolbox.LogCenter("Specialty","order_stack exist", x+":");
                    break;
                }
                order_stack=order_stack+x+":";
                Toolbox.LogCenter("Specialty","order_stack", order_stack);
                break;
            }
        }
    }
    // Listener
    private Button.OnClickListener switchListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_continue:
                    if ( serve_side == "our_service" ) Serve();
                    SETTLE_ACCOUNT=false;
                    if ( Side=="AttackSide" ){
                        Side="DefenseSide";
                    }
                    if ( Side=="DefenseSide"){
                        Side="AttackSide";
                    }
                    Toolbox.LogCenter("Specialty","Continue", "not settled");
                    AnalyzePerformance();
                    break;
                case R.id.btn_get_point:
                    if ( FIRST_BALL && contest_infromation[2] == "our_service" ) Serve();
                    SETTLE_ACCOUNT=true;
                    result=GET;
                    Toolbox.LogCenter("Specialty","Lose one point","Starting consist order stack");
                    ConsistOrderStack();
                    AnalyzePerformance();
                    break;
                case R.id.btn_lose_point:
                    if ( FIRST_BALL && contest_infromation[2] == "our_service" ) Serve();
                    SETTLE_ACCOUNT=true;
                    result=LOSE;
                    Toolbox.LogCenter("Specialty","Lose one point","Starting consist order stack");
                    ConsistOrderStack();
                    AnalyzePerformance();
                    break;
                case R.id.btn_change:
                    MODE=ChangeListener(MODE);
                    break;
                default:
                    break;
            }
        }
    };

    private TextView.OnClickListener playerStatusListener = new Button.OnClickListener() {
        public void onClick(View v) {
            if ( !order_stack.isEmpty() ) {
                String[] token  = order_stack.split(":");
                //there has three players is active
                if ( token.length==3 ) {
                    THREE_PLAYER=true;
                    Toolbox.LogCenter("Specialty","THREE_PLAYER","There has three players is active.");
                }
                else {
                    Toolbox.LogCenter("Specialty","THREE_PLAYER","There only has "+(token.length+1)+" players is active.");
                    THREE_PLAYER=false;
                }
            }
            //get the player which is active
            switch (v.getId()) {
                case R.id.view_player1 :
                    press_times_list[0]++;
                    press_times_list[0]=ChangeColorByPressTimes(0,press_times_list[0]);
                    break;
                case R.id.view_player2 :
                    press_times_list[1]++;
                    press_times_list[1]=ChangeColorByPressTimes(1,press_times_list[1]);
                    break;
                case R.id.view_player3 :
                    press_times_list[2]++;
                    press_times_list[2]=ChangeColorByPressTimes(2,press_times_list[2]);
                    break;
                case R.id.view_player4 :
                    press_times_list[3]++;
                    press_times_list[3]=ChangeColorByPressTimes(3,press_times_list[3]);
                    break;
                case R.id.view_player5 :
                    press_times_list[4]++;
                    press_times_list[4]=ChangeColorByPressTimes(4,press_times_list[4]);
                    break;
                case R.id.view_player6 :
                    press_times_list[5]++;
                    press_times_list[5]=ChangeColorByPressTimes(5,press_times_list[5]);
                    break;
                case R.id.view_player_l : break;
                default: break;
            }
            ConsistOrderStack();
        }
    };
    private TextView.OnClickListener changePlayerListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_player1 : changePlayerDialog(0); break;
                case R.id.view_player2 : changePlayerDialog(1); break;
                case R.id.view_player3 : changePlayerDialog(2); break;
                case R.id.view_player4 : changePlayerDialog(3); break;
                case R.id.view_player5 : changePlayerDialog(4); break;
                case R.id.view_player6 : changePlayerDialog(5); break;
                case R.id.view_player_l : break;
                default: break;
            }
        }
    };
    private TextView.OnClickListener Listener = new Button.OnClickListener() {
        public void onClick(View v) {
        }
    };
}