package com.example.volleyballnotes;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DataAnalyze_2_AttendRecordActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_data_analyze_2);

        String[] titles = new String[] { "2008", "2007","2006","2005","2004"};//標題
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[] { 14230, 12300, 14240, 15244, 15900, 19200, 22030, 21200, 19500, 15500, 12600, 14000 });//長條圖的值
        values.add(new double[] { 5230, 7300, 9240, 10540, 7900, 9200, 12030, 11200, 9500, 10500, 11600, 13500 });
        values.add(new double[] { 14230, 12300, 14240, 15244, 15900, 19200, 22030, 21200, 19500, 15500, 12600, 14000 });
        values.add(new double[] { 14230, 12300, 14240, 15244, 15900, 19200, 22030, 21200, 19500, 15500, 12600, 14000 });
        values.add(new double[] { 14230, 12300, 14240, 15244, 15900, 19200, 22030, 21200, 19500, 15500, 12600, 14000 });
        int[] colors = new int[] { Color.BLUE, Color.CYAN ,Color.GRAY,Color.YELLOW,Color.GREEN};//顏色
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        setChartSettings(renderer, "Monthly sales in the last 2 years", "Month", "Units sold", 0.5, 12.5, 0, 24000, Color.GRAY, Color.LTGRAY);
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
        renderer.getSeriesRendererAt(2).setDisplayChartValues(true);//每當多一個title、陣列需ser一個
        renderer.getSeriesRendererAt(3).setDisplayChartValues(true);
        renderer.getSeriesRendererAt(4).setDisplayChartValues(true);
        renderer.setXLabels(12);//X軸幾個長條 對應陣列個數
        renderer.setYLabels(10);//
        renderer.setXLabelsAlign(Align.LEFT);
        renderer.setYLabelsAlign(Align.LEFT);
        renderer.setPanEnabled(true, false);
        renderer.setZoomEnabled(false);
        renderer.setZoomRate(1.1f);
        renderer.setBarSpacing(0.5f);
        View view = ChartFactory.getBarChartView(this, buildBarDataset(titles, values), renderer, Type.DEFAULT); //Type.STACKED
        view.setBackgroundColor(Color.BLACK);
        setContentView(view);
    }

    protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            double[] v = values.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(v[k]);
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }
}
