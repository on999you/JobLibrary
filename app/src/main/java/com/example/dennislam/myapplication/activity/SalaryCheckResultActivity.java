package com.example.dennislam.myapplication.activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dennislam.myapplication.R;
import com.example.dennislam.myapplication.adapter.SalaryCheckListViewAdapter;
import com.example.dennislam.myapplication.dao.GetSalaryCheckGraphDao;
import com.example.dennislam.myapplication.dao.GetSalaryCheckResultDao;
import com.example.dennislam.myapplication.xml.GetSalaryCheckGraphXML;
import com.example.dennislam.myapplication.xml.GetSalaryCheckResultXML;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalaryCheckResultActivity extends BaseActivity {

    private ListView listView;

    public static List<BarEntry> entries = new ArrayList<>();
    public static List<BarEntry> entries2 = new ArrayList<>();
    public static List<PieEntry> entriesMax = new ArrayList<>();
    public static List<PieEntry> entriesMed = new ArrayList<>();
    public static List<PieEntry> entriesMin = new ArrayList<>();
    boolean firstuse =true;
    public BarChart barChart;
    public PieChart pieChartMax,pieChartMin,pieChartMed;
    public static int totalE= 0;
    public static int totalJ= 0;
    public double percent=0;
    public int sourceType= 0;
    private double itemcount;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###,##0.00");
    private int resultMax, resultMed, resultMin;



    //Receive inserting data
    Boolean withSimilarWord;
    String finalJobTitle, finalWorkExpFromID, finalWorkExpToID, finalSalarySourceID, finalDataSource;
    ArrayList<String> finalSelectedJobCatArray = new ArrayList<String>();
    ArrayList<String> finalSelectedJobIndustryArray = new ArrayList<String>();

    int itemsTotal;

    ArrayList<Integer> medSalaryArray = new ArrayList<Integer>();

    ArrayList<String> labelArray = new ArrayList<String>();
    ArrayList<String> countArray = new ArrayList<String>();
    ArrayList<String> sourceTypeArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_salary_check_result, null, false);
        mDrawer.addView(contentView, 0);

        totalE = 0;
        totalJ = 0;
        entries.clear();
        entries2.clear();
        entriesMax.clear();
        entriesMed.clear();
        entriesMin.clear();

//        listView = (ListView)findViewById(R.id.list_view);
        Button relevantDataButton = (Button)findViewById(R.id.relevantDataButton);

        relevantDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RelevantDataActivity.class);
                intent.putExtra("jobTitle", finalJobTitle);
                intent.putExtra("withSimilarWord", withSimilarWord);
                intent.putExtra("jobCat", finalSelectedJobCatArray);
                intent.putExtra("jobIndustry", finalSelectedJobIndustryArray);
                intent.putExtra("workExpFrom", finalWorkExpFromID);
                intent.putExtra("workExpTo", finalWorkExpToID);
                intent.putExtra("salarySource", finalSalarySourceID);
                intent.putExtra("dataSource", finalDataSource);
                startActivity(intent);
            }
        });

        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            finalJobTitle =(String) b.get("jobTitle");
            withSimilarWord =(Boolean) b.get("withSimilarWord");
            finalSelectedJobCatArray =(ArrayList<String>) b.get("jobCat");
            finalSelectedJobIndustryArray =(ArrayList<String>) b.get("jobIndustry");
            finalWorkExpFromID =(String) b.get("workExpFrom");
            finalWorkExpToID =(String) b.get("workExpTo");
            finalSalarySourceID =(String) b.get("salarySource");
            finalDataSource = (String) b.get("dataSource");

            Log.v("criterias_bg",
                    finalJobTitle + "\n" +
                            withSimilarWord + "\n" +
                            finalSelectedJobCatArray + "\n" +
                            finalSelectedJobIndustryArray + "\n" +
                            finalWorkExpFromID + "\n" +
                            finalWorkExpToID + "\n" +
                            finalSalarySourceID);
        }

        //Run the code if there are network connected
        if(globalVariable.getNetwork() == true){
            new GetSalaryCheckResultAsyncTaskRunner().execute();
            new GetGraphInfoAsyncTaskRunner().execute();
        }



        barChart = (BarChart) findViewById(R.id.barChart);
        pieChartMax = (PieChart) findViewById(R.id.pieMax);
        pieChartMed = (PieChart) findViewById(R.id.pieMed);
        pieChartMin = (PieChart) findViewById(R.id.pieMin);

        final  String[] salaryIndex = new String[] {"10k-20k", "20k-30k", "30k-40k","40k-50k","50k-60K","60k-70k","70k-80k" ,"80k-90k","90k-100K",">100k"};

        IAxisValueFormatter moneyFormatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return salaryIndex[(int)value];
            }

            @Override
            public int getDecimalDigits() {  return 1; }
        };

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(moneyFormatter);
        xAxis.setGranularity(1f);

        barChart.getDescription().setEnabled(false);
        pieChartMax.getDescription().setEnabled(false);
        pieChartMed.getDescription().setEnabled(false);
        pieChartMin.getDescription().setEnabled(false);

        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);
        barChart.getAxisLeft().setValueFormatter(new PercentFormatter());
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);

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
                Intent myintent = new Intent(getBaseContext(),LandScapeBarChart.class);
                intent.putExtra("title", finalJobTitle);
                startActivity(myintent);
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

    }

    class GetSalaryCheckResultAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<GetSalaryCheckResultXML.SalaryResultItem> salaryResultItemList = new ArrayList<GetSalaryCheckResultXML.SalaryResultItem>();
        GetSalaryCheckResultDao salaryResultItemDao = new GetSalaryCheckResultDao();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //loadingInternetDialog = new ProgressDialog(SalaryCheckResultActivity.this);
            //loadingInternetDialog.setMessage("Loading...");
            //loadingInternetDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if(salaryResultItemList == null) {
                Toast.makeText(getBaseContext(), "nothing first", Toast.LENGTH_LONG).show();
            } else {
                salaryResultItemList = salaryResultItemDao.getSalaryResultItemDao(finalJobTitle, withSimilarWord, finalSelectedJobCatArray, finalSelectedJobIndustryArray, finalWorkExpFromID, finalWorkExpToID, finalSalarySourceID, finalDataSource);
            }

            itemsTotal = salaryResultItemDao.getItemsTotal();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            //loadingInternetDialog.dismiss();

            if(salaryResultItemList == null || salaryResultItemList.isEmpty()) {
                Toast.makeText(getBaseContext(), "nothing", Toast.LENGTH_LONG).show();
            } else {

                for(int i = 0; i < salaryResultItemList.size(); i++) {
                    medSalaryArray.add(i, Integer.parseInt(salaryResultItemList.get(i).getMedSalary()));
                }

                Collections.sort(medSalaryArray);

                int mid = medSalaryArray.size()/2;
                int median = (int)medSalaryArray.get(mid);
                if (medSalaryArray.size()%2 == 0) {
                    median = (median + (int)medSalaryArray.get(mid-1))/2;
                }

                resultMax = Integer.parseInt(salaryResultItemList.get(0).getMaxSalary());
                resultMin = Integer.parseInt(salaryResultItemList.get(0).getMinSalary());
                resultMed = median;

                Log.v(resultMax+" "+resultMed+" "+resultMin+" ","~~");

                entriesMax.add(new PieEntry(resultMax, 0));
                entriesMed.add(new PieEntry(resultMed, 0));
                entriesMin.add(new PieEntry(resultMin, 0));

                entriesMax.add(new PieEntry(110000-resultMax,1));
                entriesMed.add(new PieEntry(110000-resultMed,1));
                entriesMin.add(new PieEntry(110000-resultMin,1));



//                String resultMedSalary = res.getString(R.string.barC_mid) + median;
//                String resultMaxSalary = res.getString(R.string.barC_high) + salaryResultItemList.get(0).getMaxSalary();
//                String resultMinSalary = res.getString(R.string.barC_low) + salaryResultItemList.get(0).getMinSalary();



//                //Set ListView Item
//                //Header
//                View header = (View)getLayoutInflater().inflate(R.layout.salary_check_result_listview_header,null);
//                TextView headerValue = (TextView) header.findViewById(R.id.txtHeader);
//                headerValue.setText(finalJobTitle);
//                listView.addHeaderView(header);
//
//                //Row
//                String[] list = {resultMinSalary,resultMedSalary,resultMaxSalary};
//                listView.setAdapter(new SalaryCheckListViewAdapter(getBaseContext(), list));

                System.out.println(itemsTotal);
                Log.v("Testing steps", "Salary Check : Got Salary Check Result");
            }


        }
    }

    class GetGraphInfoAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        List<GetSalaryCheckGraphXML.GraphInfoItem> graphInfoItemList = new ArrayList<GetSalaryCheckGraphXML.GraphInfoItem>();
        GetSalaryCheckGraphDao graphInfoItemDao = new GetSalaryCheckGraphDao();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loadingInternetDialog = new ProgressDialog(SalaryCheckResultActivity.this);
            loadingInternetDialog.setMessage("Loading...");
            loadingInternetDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            graphInfoItemList = graphInfoItemDao.getGraphInfoItemDao(finalJobTitle, withSimilarWord, finalSelectedJobCatArray, finalSelectedJobIndustryArray, finalWorkExpFromID, finalWorkExpToID, finalSalarySourceID, finalDataSource);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            loadingInternetDialog.dismiss();

            if (graphInfoItemList!=null) {
                for (int i = 0; i < graphInfoItemList.size(); i++) {
                    labelArray.add(i, graphInfoItemList.get(i).getLabel());
                    countArray.add(i, graphInfoItemList.get(i).getCount());
                    sourceTypeArray.add(i, graphInfoItemList.get(i).getSourceType());

                    System.out.println(labelArray.get(i));
                    System.out.println(countArray.get(i));
                    System.out.println(sourceTypeArray.get(i));

            }

                for (int i = 0; i < graphInfoItemList.size(); i++) {
                    sourceType = Integer.parseInt(graphInfoItemList.get(i).getSourceType());
                    if (sourceType==2) {
                        totalE = totalE + Integer.parseInt(graphInfoItemList.get(i).getCount());
                    }else{
                        totalJ = totalJ + Integer.parseInt(graphInfoItemList.get(i).getCount());
                    }
                }


                for (int i = 0; i < graphInfoItemList.size(); i++) {
                    sourceType = Integer.parseInt(graphInfoItemList.get(i).getSourceType());
                    switch (graphInfoItemList.get(i).getLabel()) {
                        case "10000 to 19999":
                            if (sourceType == 1) {
                                addEntries1(graphInfoItemList.get(i).getCount(), i, 0);
                                Log.v("labour", "labour");
                            }
                            else if (sourceType == 2) {
                                addEntries2(graphInfoItemList.get(i).getCount(), i, 0);
                            }
                            break;
                        case "20000 to 29999":
                            if (sourceType == 1) {
                                addEntries1(graphInfoItemList.get(i).getCount(), i, 1);
                                Log.v("labour", "labour");
                            } else if (sourceType == 2) {
                                addEntries2(graphInfoItemList.get(i).getCount(), i, 1);
                            }
                            break;
                        case "30000 to 39999":
                            if (sourceType == 1) {
                                addEntries1(graphInfoItemList.get(i).getCount(), i, 2);
                                Log.v("labour", "labour");
                            } else if (sourceType == 2) {
                                addEntries2(graphInfoItemList.get(i).getCount(), i, 2);
                            }
                            break;
                        case "40000 to 49999":
                            if (sourceType == 1) {
                                addEntries1(graphInfoItemList.get(i).getCount(), i, 3);
                                Log.v("labour", "labour");
                            } else if (sourceType == 2) {
                                addEntries2(graphInfoItemList.get(i).getCount(), i, 3);
                            }
                            break;
                        case "50000 to 59999":
                            if (sourceType == 1) {
                                addEntries1(graphInfoItemList.get(i).getCount(), i, 4);
                                Log.v("labour", "labour");
                            } else if (sourceType == 2) {
                                addEntries2(graphInfoItemList.get(i).getCount(), i, 4);
                            }
                            break;
                        case "60000 to 69999":
                            if (sourceType == 1) {
                                addEntries1(graphInfoItemList.get(i).getCount(), i, 5);
                                Log.v("labour", "labour");
                            } else if (sourceType == 2) {
                                addEntries2(graphInfoItemList.get(i).getCount(), i, 5);
                            }
                            break;
                        case "70000 to 79999":
                            if (sourceType == 1) {
                                addEntries1(graphInfoItemList.get(i).getCount(), i, 6);
                                Log.v("labour", "labour");
                            } else if (sourceType == 2) {
                                addEntries2(graphInfoItemList.get(i).getCount(), i, 6);
                            }
                            break;
                        case "80000 to 89999":
                            if (sourceType == 1) {
                                addEntries1(graphInfoItemList.get(i).getCount(), i, 7);
                                Log.v("labour", "labour");
                            } else if (sourceType == 2) {
                                addEntries2(graphInfoItemList.get(i).getCount(), i, 7);
                            }
                            break;
                        case "90000 to 99999":
                            if (sourceType == 1) {
                                addEntries1(graphInfoItemList.get(i).getCount(), i, 8);
                                Log.v("labour", "labour");
                            } else if (sourceType == 2) {
                                addEntries2(graphInfoItemList.get(i).getCount(), i, 8);
                            }
                            break;
                        case ">100000":
                            if (sourceType == 1) {
                                addEntries1(graphInfoItemList.get(i).getCount(), i, 9);
                                Log.v("labour", "labour");
                            } else if (sourceType == 2) {
                                addEntries2(graphInfoItemList.get(i).getCount(), i, 9);
                            }
                            break;
                    }
                }
            }
            else {
                AlertDialog.Builder myAD = new AlertDialog.Builder(SalaryCheckResultActivity.this);
                myAD.setTitle(R.string.sC_reminder7);
                myAD.setMessage(R.string.sC_reminder8);
                DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SalaryCheckResultActivity.super.finish();
                    }
                };
                myAD.setNeutralButton(R.string.baseAct_reminder3,OkClick);
                myAD.show();
            }
            PieDataSet setPieMax = new PieDataSet(entriesMax,"");
            PieDataSet setPieMed = new PieDataSet(entriesMed,"");
            PieDataSet setPieMin = new PieDataSet(entriesMin,"");

            BarDataSet set1 = new BarDataSet(entries,"Labour");
            BarDataSet set2 = new BarDataSet(entries2,"Employer");

            setPieMax.setColors(new int[]{Color.parseColor("#B88C8C"),Color.parseColor("#778787")});
            setPieMed.setColors(new int[]{Color.parseColor("#B88C8C"),Color.parseColor("#778787")});
            setPieMin.setColors(new int[]{Color.parseColor("#B88C8C"),Color.parseColor("#778787")});

            set1.setColor(Color.parseColor("#5B6940"));
            set2.setColor(Color.parseColor("#996940"));
            float groupSpace = 0.06f;
            float barSpace = 0.02f; // x2 dataset
            float barWidth = 0.45f; // x2 dataset

            PieData pieDataMax = new PieData(setPieMax);
            PieData pieDataMed = new PieData(setPieMed);
            PieData pieDataMin = new PieData(setPieMin);
            pieDataMax.setDrawValues(false);
            pieDataMed.setDrawValues(false);
            pieDataMin.setDrawValues(false);

            BarData data = new BarData(set1,set2);
            data.setBarWidth(barWidth);
            barChart.setData(data);
            pieChartMax.setData(pieDataMax);
            pieChartMed.setData(pieDataMed);
            pieChartMin.setData(pieDataMin);

            pieChartMax.setCenterText(res.getString(R.string.barC_high)+"\n $"+resultMax);
            pieChartMed.setCenterText(res.getString(R.string.barC_mid)+"\n $"+resultMed);
            pieChartMin.setCenterText(res.getString(R.string.barC_low)+"\n $"+resultMin);

            pieChartMax.setRotationEnabled(false);
            pieChartMed.setRotationEnabled(false);
            pieChartMin.setRotationEnabled(false);


            pieChartMax.getLegend().setEnabled(false);
            pieChartMed.getLegend().setEnabled(false);
            pieChartMin.getLegend().setEnabled(false);

            pieChartMax.invalidate();
            pieChartMed.invalidate();
            pieChartMin.invalidate();

            barChart.setFitBars(true);
            barChart.setDoubleTapToZoomEnabled(false);
            barChart.groupBars(-0.5f, groupSpace, barSpace);
            barChart.invalidate();

        }
    }

    public void addEntries1 (String count, int i,float position){
        itemcount = Integer.parseInt(count);
        percent= (itemcount*100/totalJ);
        decimalFormat.format(percent);
        entries.add(new BarEntry(position,(float)percent));
    }

    public void addEntries2 (String count,int i,float firstletter){
        itemcount = Integer.parseInt(count);
        percent= (itemcount*100/totalE);
        decimalFormat.format(percent);
        entries2.add(new BarEntry(firstletter,(float)percent));
    }


}
