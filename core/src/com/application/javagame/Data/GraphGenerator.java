package com.application.javagame.Data;

import java.util.ArrayList;

import com.application.javagame.GameState;
import com.badlogic.gdx.utils.Array;
import com.googlecode.charts4j.*;

public class GraphGenerator {
    
    public GraphGenerator() {}

    public String GenerateGraph() {

        DBConnection connection = null;
        try {
            connection = new DBConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "";
        }

        ArrayList<Score> scores = new ArrayList<>(); 
        
        try {
            scores = connection.getScores();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Color colors[] = {
            Color.RED,
            Color.ORANGE,
            Color.PURPLE,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
        };

        ArrayList<Plot> plots = new ArrayList<>();
        ArrayList<Integer> labels = new ArrayList<>();

        int i = 0;
        int maxValue = 1;

        for(Score s : scores) {
            if(s.score > maxValue) maxValue = s.score;
        }

        for(Score s : scores) {
            Color c = colors[i++ % colors.length];    
            Data d = new Data(Double.valueOf(s.score) / maxValue);
            labels.add(s.score);
            Plot plot = Plots.newPlot(d, c, s.name);
            plots.add(plot);
        }

        BarChart barChart = GCharts.newBarChart(
            plots
        );
        barChart.setSize(plots.size() * 35 + 90, 250);
        barChart.addYAxisLabels(AxisLabelsFactory.newAxisLabels());
        barChart.setMargins(10, 10, 10, 10);

        return barChart.toURLString();
    }
}
