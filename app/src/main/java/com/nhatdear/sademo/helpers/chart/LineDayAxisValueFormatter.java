package com.nhatdear.sademo.helpers.chart;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.nhatdear.sademo.activities.SA_MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LineDayAxisValueFormatter implements IAxisValueFormatter
{
    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };
    private int currentSearchYear = 2017;
    private SA_MainActivity.MODE mode;
    public LineDayAxisValueFormatter(int currentSearchYear, SA_MainActivity.MODE mode) {
        this.currentSearchYear = currentSearchYear;
        this.mode = mode;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        value = value + 1; //increase value by 1
        switch (mode) {
            case DAILY:
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, currentSearchYear);
                cal.set(Calendar.DAY_OF_YEAR, (int)value);
                SimpleDateFormat sdf;
                sdf = new SimpleDateFormat("MMM dd");
                return sdf.format(cal.getTime());
            case MONTHLY:
                return mMonths[(int)value - 1];
            case QUARTERLY:
                return String.format("Q%d",(int)value);
            default:
                return String.valueOf(value);
        }
    }
}
