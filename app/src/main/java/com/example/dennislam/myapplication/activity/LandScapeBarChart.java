package com.example.dennislam.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.dennislam.myapplication.R;
import com.github.mikephil.charting.animation.Easing;
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

import java.util.Locale;

import static com.example.dennislam.myapplication.activity.SalaryCheckActivity.salarySource_opt;

public class LandScapeBarChart extends AppCompatActivity {

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        if("zh".equals(prefs.getString("Language",""))){
            Locale myLocale = new Locale("zh");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Locale.setDefault(myLocale);
        }else{
            Locale myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Locale.setDefault(myLocale);
        }
        setContentView(R.layout.activity_land_scape_bar_chart);

//        Intent intent= getIntent();
//        Bundle b = intent.getExtras();
//        if(b!=null) {
//            title = (String) b.get("title");
//            TextView sdtv = (TextView)findViewById(R.id.salarydistributionTVL);
//            sdtv.setText(title +" 's Salary Distribution Chart");
//        }

        BarChart barChart = (BarChart)findViewById(R.id.barChart2) ;

        BarDataSet set1 = new BarDataSet(SalaryCheckResultActivity.entries, "Job seeker");
        BarDataSet set2 =new BarDataSet(SalaryCheckResultActivity.entries2,"Employer");

        final  String[] salaryIndex = new String[] {"10k-20k", "20k-30k", "30k-40k","40k-50k","50k-60K","60k-70k","70k-80k" ,"80k-90k","90k-100K",">100k","","","","","","","",""};

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
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisRight().setAxisMinimum(0);

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

        Legend legendBarChart = barChart.getLegend();
        legendBarChart.setOrientation(Legend.LegendOrientation.VERTICAL);
        legendBarChart.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);

        barChart.getDescription().setEnabled(false);
        barChart.animateY(1500, Easing.EasingOption.EaseOutBounce);
        set1.setColors(Color.parseColor("#81DAF5"));
        set2.setColor(Color.parseColor("#5858FA"));
        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset


        if(salarySource_opt.equals(getResources().getString(R.string.all))){
            BarData data = new BarData(set1, set2);
            data.setBarWidth(barWidth);
            barChart.setData(data);
            barChart.groupBars(-0.5f, groupSpace, barSpace);
        }else if(salarySource_opt.equals(getResources().getString(R.string.jobSeeker))){
            BarData data = new BarData(set1);
            barChart.setData(data);
        }else if (salarySource_opt.equals(getResources().getString(R.string.employer))){
            BarData data = new BarData(set2);
            barChart.setData(data);
        }
        barChart.setVisibleXRange(0f,10f);
        barChart.setFitBars(true);
        barChart.invalidate();


    }
}
