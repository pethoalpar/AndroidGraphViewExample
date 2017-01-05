package com.pethoalpar.androidgraphviewexamplet;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String LINE_GRAPH = "Line graph";
    public static final String BAR_CHART = "Bar chart";
    public static final String POINT_GRAPH = "Point graph";
    private GraphView graphView;
    private Spinner spinner;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graphView = (GraphView) this.findViewById(R.id.graph);
        spinner = (Spinner) this.findViewById(R.id.spinner);
        button = (Button) this.findViewById(R.id.button);

        graphView.addSeries(generateSeries(20, LineGraphSeries.class, Color.DKGRAY));
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);
        graphView.getViewport().setScrollableY(true);

        List<String> list = new ArrayList<>();
        list.add(LINE_GRAPH);
        list.add(BAR_CHART);
        list.add(POINT_GRAPH);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Series series = graphView.getSeries().get(0);
                Random random = new Random();
                if(series instanceof LineGraphSeries){
                    LineGraphSeries s = (LineGraphSeries) series;
                    s.appendData(new DataPoint(s.getHighestValueX()+1, random.nextInt(20)),true,40);
                }
                if(series instanceof BarGraphSeries){
                    BarGraphSeries s = (BarGraphSeries) series;
                    s.appendData(new DataPoint(s.getHighestValueX()+1, random.nextInt(20)),true,40);
                }
                if(series instanceof PointsGraphSeries){
                    PointsGraphSeries s = (PointsGraphSeries) series;
                    s.appendData(new DataPoint(s.getHighestValueX()+1, random.nextInt(20)),true,40);
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getSelectedItem().toString()){
                    case LINE_GRAPH:
                        graphView.removeAllSeries();
                        graphView.addSeries(generateSeries(20,LineGraphSeries.class,Color.DKGRAY));
                        break;
                    case BAR_CHART:
                        graphView.removeAllSeries();
                        graphView.addSeries(generateSeries(20,BarGraphSeries.class,Color.BLUE));
                        break;
                    case POINT_GRAPH:
                        graphView.removeAllSeries();
                        graphView.addSeries(generateSeries(20,PointsGraphSeries.class,Color.RED));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private <T extends BaseSeries> T generateSeries(int n, Class<T> type, int color){
        try{
            BaseSeries series = type.newInstance();
            Random rand = new Random();
            for(int i =0; i<n; ++i){
                series.appendData(new DataPoint(i,rand.nextInt(20)),true,40);
            }

            //Optional set tap listener
            series.setOnDataPointTapListener(new OnTapListener());

            //Optional set series color
            series.setColor(color);

            return (T) series;

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class OnTapListener implements OnDataPointTapListener{

        @Override
        public void onTap(Series series, DataPointInterface dataPoint) {
            Toast.makeText(getApplicationContext(),"x:"+dataPoint.getY()+" y:"+dataPoint.getY(),Toast.LENGTH_SHORT).show();
        }
    }
}
