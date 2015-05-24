package com.example.volleyballnotes;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DataAnalyze_1_PracticeItemActivity extends Activity {
    private Button btn_backtomain;
    String[] item={"發球失誤","攻擊失誤","接球失誤","攔網失誤","無法判定"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_data_analyze_1);
        btn_backtomain = (Button)findViewById(R.id.btn_backtomain);
        btn_backtomain.setOnClickListener(btnHomeListner);
        double[] values = new double[] { 4, 5, 3, 4, 6 };//陣列質改變圓餅圖值
        int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN };//對應顏色
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setZoomButtonsVisible(true);
        renderer.setZoomEnabled(true);
        renderer.setChartTitleTextSize(20);
        View view = ChartFactory.getPieChartView(this, buildCategoryDataset("Project budget", values), renderer);
        view.setBackgroundColor(Color.BLACK);
        setContentView(view);
    }

    protected DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setFitLegend(false);
        renderer.setMargins(new int[] { 20, 30, 15, 0 });
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    protected CategorySeries buildCategoryDataset(String title, double[] values) {
        CategorySeries series = new CategorySeries(title);
        int k = 0;
        for (double value : values) {
            series.add(item[k++]+ " (" + value + ")", value);
        }
        return series;
    }
    private Button.OnClickListener btnHomeListner=new Button.OnClickListener(){
        public void onClick(View v){
            finish();
        }
    };
}
