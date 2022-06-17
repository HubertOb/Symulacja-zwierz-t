package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.Vector2d;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;
import java.util.Map;


public class Chart {
    private final LineChart<Number,Number> lineChart;

    private int numberOfAnimals=0;
    private int numberOfPlant=0;
    private float avgEnergyLevel=0;
    private int avgLifeLength=0;
    private int sumLifeLength=0;
    private int numberOfDeathAnimals=0;
    private int numberOfDays=0;
    private float avgNumberOfChildren;

    XYChart.Series<Number,Number>numberOfAnimalsSeries;
    XYChart.Series<Number,Number>numberOfPlantSeries;
    XYChart.Series<Number,Number>avgEnergyLevelSeries;
    XYChart.Series<Number,Number>avgLifeLengthSeries;
    XYChart.Series<Number,Number>avgNumberOfChildrenSeries;



    public Chart(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        lineChart=new LineChart<Number,Number>(xAxis, yAxis);
        numberOfAnimalsSeries=new XYChart.Series<Number,Number>();
        numberOfAnimalsSeries.setName("Ilosc zwierzat");
        numberOfPlantSeries=new XYChart.Series<Number,Number>();
        numberOfPlantSeries.setName("Ilosc roslin");
        avgEnergyLevelSeries=new XYChart.Series<Number,Number>();
        avgEnergyLevelSeries.setName("Sredni poziom energii");
        avgLifeLengthSeries=new XYChart.Series<Number,Number>();
        avgLifeLengthSeries.setName("Srednia dlugosc zycia");
        avgNumberOfChildrenSeries=new XYChart.Series<Number,Number>();
        avgNumberOfChildrenSeries.setName("Srednia liczba dzieci");
        lineChart.getData().addAll(numberOfAnimalsSeries,numberOfPlantSeries,avgEnergyLevelSeries,avgLifeLengthSeries,avgNumberOfChildrenSeries);
    }

    public LineChart<Number,Number> getLineChart(){
        return lineChart;
    }

    public synchronized void incrementNumberOfAnimals(){
        numberOfAnimals++;
    }

    public synchronized void decrementNumberOfAnimals(){
        numberOfAnimals--;
    }

    public synchronized void incrementNumberOfPlant(){
        numberOfPlant++;
    }

    public synchronized void decrementNumberOfPlant(){
        numberOfPlant--;
    }

    public synchronized void addAvgEnergyLevel(Map<Pair<Vector2d, Animal>,Animal> animals){
        avgEnergyLevel=0;
        for(Animal animal:animals.values()){
            avgEnergyLevel+=animal.getEnergy();
        }
        avgEnergyLevel/=numberOfAnimals;
    }

    public synchronized void addSumLifeLength(int lifeLength){
        sumLifeLength+=lifeLength;
    }

    public synchronized void incrementNumberOfDeathAnimals(){
        numberOfDeathAnimals++;
        avgLifeLength=sumLifeLength/numberOfDeathAnimals;
    }

    public synchronized void incrementNumberOfDays(){
        numberOfDays++;
    }

    public synchronized void addAvgNumberOfChildren(Map<Pair<Vector2d, Animal>,Animal> animals){
        avgNumberOfChildren=0;
        for(Animal animal:animals.values()){
            avgNumberOfChildren+=animal.getNumberOfChildren();
        }
        avgNumberOfChildren/=numberOfAnimals;
    }

    public synchronized void makeChart(){
        numberOfAnimalsSeries.getData().add(new XYChart.Data<Number,Number>(numberOfDays,numberOfAnimals));
        numberOfPlantSeries.getData().add(new XYChart.Data<Number,Number>(numberOfDays,numberOfPlant));
        avgEnergyLevelSeries.getData().add(new XYChart.Data<Number,Number>(numberOfDays,avgEnergyLevel));
        avgLifeLengthSeries.getData().add(new XYChart.Data<Number,Number>(numberOfDays,avgLifeLength));
        avgNumberOfChildrenSeries.getData().add(new XYChart.Data<Number,Number>(numberOfDays,avgNumberOfChildren));
    }
}
