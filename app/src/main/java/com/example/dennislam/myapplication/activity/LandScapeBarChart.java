package com.example.dennislam.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.dennislam.myapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

public class LandScapeBarChart extends AppCompatActivity {

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_scape_bar_chart);

        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            title = (String) b.get("title");
            TextView sdtv = (TextView)findViewById(R.id.salarydistributionTVL);
            sdtv.setText(title +" 's Salary Distribution Chart");
        }


        BarChart barChart = (BarChart)findViewById(R.id.barChart2) ;

        BarDataSet set1 = new BarDataSet(SalaryCheckResultActivity.entries, "Labour");
        BarDataSet set2 =new BarDataSet(SalaryCheckResultActivity.entries2,"Employer");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// x-axis  value
        ///// //x-axis value for bar Chart

        final  String[] salaryIndex = new String[] {"10k-20k", "20k-30k", "30k-40k","40k-50k","50k-60K","60k-70k","70k-80k" ,"80k-90k","90k-100K",">100k","","","","","","","",""};

        //////Formatter

        IAxisValueFormatter moneyFormatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return salaryIndex[(int)value];
            }

            @Override
            public int getDecimalDigits() {  return 0; }
        };


        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(moneyFormatter);
        xAxis.setGranularity(1f);
        //background , xy axis.
        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);
        barChart.getAxisLeft().setValueFormatter(new PercentFormatter());
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

//        barChart.setHighlightPerDragEnabled(false);
//        barChart.setHighlightPerTapEnabled(false);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Onclick
        barChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
//                Intent intent = new Intent(Test.this,MainActivity.class);
//                startActivity(intent);
                LandScapeBarChart.super.finish();
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Setting of chart

        //////////////legend
        Legend legendBarChart = barChart.getLegend();
        legendBarChart.setOrientation(Legend.LegendOrientation.VERTICAL);
        legendBarChart.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        ////////////////Description
        barChart.getDescription().setEnabled(false);

        //////////////Color and space
        set1.setColors(Color.parseColor("#81DAF5"));
        set2.setColor(Color.parseColor("#5858FA"));
        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset


        BarData data = new BarData(set1,set2);
        data.setBarWidth(barWidth);
        barChart.setData(data);

        barChart.setVisibleXRange(0f,10f);
        barChart.setFitBars(true);

        barChart.groupBars(-0.5f, groupSpace, barSpace);

        barChart.invalidate();


    }
}
