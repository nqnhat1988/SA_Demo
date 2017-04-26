package com.nhatdear.sademo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhatdear.sademo.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.nhatdear.sademo.activities.SA_MainActivity;
import com.nhatdear.sademo.helpers.DateUtils;
import com.nhatdear.sademo.helpers.chart.DayAxisValueFormatter;
import com.nhatdear.sademo.models.SA_Nav;
import com.nhatdear.sademo.models.SA_Portfolio;

import java.util.ArrayList;
import java.util.Random;


public class SA_BarChartFragment extends Fragment implements OnChartValueSelectedListener {

    float groupSpace = 0.0f;
    float barSpace = 0.0f;
    float barWidth = 0.0f;
    protected BarChart mChart;
    private Typeface mTfLight;
    private ArrayList<SA_Portfolio> arrayList;
    private SA_MainActivity.MODE mode;
    private int currentSearchYear = 2017;

    public SA_BarChartFragment() {
        // Required empty public constructor
    }

    public static SA_BarChartFragment newInstance(ArrayList<SA_Portfolio> arrayList, SA_MainActivity.MODE mode) {
        SA_BarChartFragment fragment = new SA_BarChartFragment();
        fragment.arrayList = arrayList;
        fragment.mode = mode;
        return fragment;
    }

    public static SA_BarChartFragment newInstance(ArrayList<SA_Portfolio> arrayList, SA_MainActivity.MODE mode, int year) {
        SA_BarChartFragment fragment = new SA_BarChartFragment();
        fragment.arrayList = arrayList;
        fragment.mode = mode;
        fragment.currentSearchYear = year;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sa__bar_chart, container, false);

        mTfLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Light.ttf");

        mChart = (BarChart) view.findViewById(R.id.chart);
        mChart.setOnChartValueSelectedListener(this);

        setupBarChart();

        mChart.setScaleEnabled(false);

        setDataToChart(arrayList, mode);

        mChart.animateXY(1000, 1000);
        return view;
    }

    private void setupBarChart() {
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new LargeValueFormatter();

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setXOffset(0f);
        l.setYOffset(-40f);

        mChart.setExtraBottomOffset(110f);
    }

    private void setDataToChart(ArrayList<SA_Portfolio> arrayList, SA_MainActivity.MODE mode) {
        if (arrayList == null || arrayList.size() == 0) return;

        int groupCount = 0;
        int start = 1;

        setupSpace(arrayList.size());

        switch (mode) {
            case DAILY:
                groupCount = 365;
                break;
            case QUARTERLY:
                groupCount = 4;
                break;
            case MONTHLY:
                groupCount = 12;
                break;
        }

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();

        for (int i = 0; i < arrayList.size(); i++) {
            BarDataSet barDataSet = createDataSet(arrayList.get(i), getRandomColor(), mode);
            dataSets.add(i,barDataSet);
        }

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(mTfLight);

        mChart.setData(data);

        // specify the width each bar should have
        mChart.getBarData().setBarWidth(barWidth);
        // restrict the x-axis range
        mChart.getXAxis().setAxisMinimum(start);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mChart.getXAxis().setAxisMaximum(start + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart, currentSearchYear, mode);
        mChart.getXAxis().setValueFormatter(xAxisFormatter);
        mChart.groupBars(start, groupSpace, barSpace);
        mChart.invalidate();
        mChart.clearAllViewportJobs();
        mChart.fitScreen();
        mChart.setVisibleXRangeMaximum(2);
        mChart.moveViewToX(0); //
    }

    private BarDataSet createDataSet(SA_Portfolio sa_portfolio, int color, SA_MainActivity.MODE mode) {

        if (sa_portfolio.getNavs().size() == 0) return null;
        try {
            ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
            BarDataSet dataSet;

            switch (mode) {
                case DAILY:
                    int numberDaysOfYear = DateUtils.getDaysInYear(sa_portfolio.getNavs().get(0).getDate());
                    for (int i = 0; i < numberDaysOfYear; i++) {
                        yVals.add(new BarEntry(i, 0));
                    }
                    for (int i = 0; i < sa_portfolio.getNavs().size(); i++) {
                        SA_Nav sa_nav = sa_portfolio.getNavs().get(i);
                        int index = DateUtils.getDayInYear(sa_nav.getDate()) - 1;
                        yVals.set(index, new BarEntry(i, Float.parseFloat(String.valueOf(sa_nav.getAmount()))));
                    }
                    break;
                case QUARTERLY:
                    for (int i = 0; i < 4; i++) {
                        yVals.add(new BarEntry(i, 0));
                    }

                    for (int i = 0; i < sa_portfolio.getNavs().size(); i++) {
                        SA_Nav sa_nav = sa_portfolio.getNavs().get(i);
                        int index = DateUtils.getQuarterFromDate(sa_nav.getDate()) - 1;
                        BarEntry barEntry = yVals.get(index);
                        float sum = barEntry.getY() + Float.parseFloat(String.valueOf(sa_nav.getAmount()));
                        barEntry.setY(sum);
                    }
                    break;
                case MONTHLY:
                    for (int i = 0; i < 12; i++) {
                        yVals.add(new BarEntry(i, 0));
                    }

                    for (int i = 0; i < sa_portfolio.getNavs().size(); i++) {
                        SA_Nav sa_nav = sa_portfolio.getNavs().get(i);
                        int index = DateUtils.getMonthFromDate(sa_nav.getDate()) - 1;
                        BarEntry barEntry = yVals.get(index);
                        float sum = barEntry.getY() + Float.parseFloat(String.valueOf(sa_nav.getAmount()));
                        barEntry.setY(sum);
                    }
                    break;
            }

            dataSet = new BarDataSet(yVals, String.format("Portfolio %s", sa_portfolio.getPortfolioId()) );
            dataSet.setDrawIcons(false);
            dataSet.setColors(color);
            return dataSet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getRandomColor() {
        Random rd = new Random();
        return Color.rgb(rd.nextInt(255),rd.nextInt(255),rd.nextInt(255));
    }

    private void setupSpace(int numberOfBars) {
        if (numberOfBars < 5) {
            barWidth = 0.2f;
            barSpace = 0.03f;
        } else {
            barWidth = 0.15f;
            barSpace = 0.015f;
        }

        groupSpace = 1f - (barSpace + barWidth) * numberOfBars;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
