package agh.ics.oop;

import javafx.scene.chart.XYChart;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MakeCSVFile {
    private int numberOfAnimals=0;
    private int numberOfPlants=0;
    private float avgEnergyLevel=0;
    private float avgLifeLength=0;
    private int sumLifeLength=0;
    private int numberOfDeathAnimals=0;
    private float avgNumberOfChildren;

    private final List<Integer> numberOfAnimalsList;
    private final List<Integer> numberOfPlantsList;
    private final List<Float> avgEnergyLevelList;
    private final List<Float> avgLifeLengthList;
    private final List<Float> avgNumberOfChildrenList;

    private int sum1=0;
    private int sum2=0;
    private int sum3=0;
    private int sum4=0;
    private int sum5=0;

    public MakeCSVFile(){
        numberOfAnimalsList=new LinkedList<>();
        numberOfPlantsList=new LinkedList<>();
        avgEnergyLevelList=new LinkedList<>();
        avgLifeLengthList=new LinkedList<>();
        avgNumberOfChildrenList=new LinkedList<>();
    }

    public void writeData(String docName) throws FileNotFoundException {
        PrintWriter writer=new PrintWriter(docName+".csv");
        writer.println("Ilosc zwierzat,ilosc roslin,srednia energia,srednia dlugosc zycia,srednia liczba dzieci");
        int k=numberOfAnimalsList.size();
        for (int i=0;i<numberOfAnimalsList.size();i++){
            sum1+=numberOfAnimalsList.get(i);
            sum2+=numberOfPlantsList.get(i);
            sum3+=avgEnergyLevelList.get(i);
            sum4+=avgLifeLengthList.get(i);
            sum5+=avgNumberOfChildrenList.get(i);
            writer.println(numberOfAnimalsList.get(i)+","+numberOfPlantsList.get(i)+","+avgEnergyLevelList.get(i)+","+avgLifeLengthList.get(i)+","+avgNumberOfChildrenList.get(i));
        }
        writer.println();
        writer.println(sum1/k+","+sum2/k+","+sum3/k+","+sum4/k+","+sum5/k);
        writer.close();
    }

    public void incrementNumberOfAnimals(){
        numberOfAnimals++;
    }

    public void decrementNumberOfAnimals(){
        numberOfAnimals--;
    }

    public void incrementNumberOfPlant(){
        numberOfPlants++;
    }

    public void decrementNumberOfPlant(){
        numberOfPlants--;
    }

    public void addAvgEnergyLevel(Map<Pair<Vector2d, Animal>,Animal> animals){
        avgEnergyLevel=0;
        for(Animal animal:animals.values()){
            avgEnergyLevel+=animal.getEnergy();
        }
        avgEnergyLevel/=numberOfAnimals;
    }

    public void addSumLifeLength(int lifeLength){
        sumLifeLength+=lifeLength;
    }

    public void incrementNumberOfDeathAnimals(){
        numberOfDeathAnimals++;
        avgLifeLength=(float) (sumLifeLength/numberOfDeathAnimals);
    }


    public void addAvgNumberOfChildren(Map<Pair<Vector2d, Animal>,Animal> animals){
        avgNumberOfChildren=0;
        for(Animal animal:animals.values()){
            avgNumberOfChildren+=animal.getNumberOfChildren();
        }
        avgNumberOfChildren/=numberOfAnimals;
    }


    public void update(){
        numberOfAnimalsList.add(numberOfAnimals);
        numberOfPlantsList.add(numberOfPlants);
        avgEnergyLevelList.add(avgEnergyLevel);
        avgLifeLengthList.add(avgLifeLength);
        avgNumberOfChildrenList.add(avgNumberOfChildren);
    }
}
