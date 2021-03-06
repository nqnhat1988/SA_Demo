package com.nhatdear.sademo.helpers.chart;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.nhatdear.sademo.activities.SA_MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter
{
    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private BarLineChartBase<?> chart;
    private int currentSearchYear = 2017;
    private SA_MainActivity.MODE mode;
    public DayAxisValueFormatter(BarLineChartBase<?> chart, int currentSearchYear, SA_MainActivity.MODE mode) {
        this.chart = chart;
        this.currentSearchYear = currentSearchYear;
        this.mode = mode;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        switch (mode) {
            case DAILY:
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, currentSearchYear);
                cal.set(Calendar.DAY_OF_YEAR, (int)value);
                SimpleDateFormat sdf;
                sdf = new SimpleDateFormat("dd MMMM yyyy");
                return sdf.format(cal.getTime());
            case MONTHLY:
                int iValue = (int)value - 1;
                if (iValue >= 0 && iValue < 12) {
                    return mMonths[iValue];
                }
                //return String.format("%s %d", mMonths[(int)value],currentSearchYear);
            case QUARTERLY:
                return String.format("Quarter %d",(int)value);
            default:
                return String.valueOf(value);
        }
    }
}
