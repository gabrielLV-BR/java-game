package com.application.javagame.Data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

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

        // não precisamos normalizar
        if(maxValue <= 100) maxValue = 1;

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

        try {
            downloadGraph(barChart.toURLString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return barChart.toURLString();
    }

    private void downloadGraph(String imageURL) throws FileNotFoundException, IOException {

        URL url = new URL(imageURL);
        try (
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream("graph.png");
        ) {
            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
        }
    }
}
