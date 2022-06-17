package agh.ics.oop;

import javafx.application.Platform;

import java.util.ArrayList;

public class SimulationEngine implements Runnable {
    private final ArrayList<IObserver> observers=new ArrayList<>();
    private IWorldMap map;
    private int moveEnergy;
    private boolean runningLoop=true;

    public SimulationEngine(){
    }

    public void setParameters(int beginningNumbersOfAnimals,int startEnergy,int moveEnergy, IWorldMap a){
        this.moveEnergy=moveEnergy;
        map=a;
        for (int i=0;i<beginningNumbersOfAnimals;i++){
            map.placeNewAnimal();
        }
    }


    public void addObserver(IObserver observer){
        observers.add(observer);
    }

    public void mapChanged(){
        for (IObserver observer : observers) {
            observer.positionChanged();
        }
    }



    @Override
    public void run() {
        while (runningLoop){
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                System.out.println(e);
            }
            map.moveAnimals(moveEnergy);
            map.makeGrass();
            map.makeGrass();
            map.getChart().incrementNumberOfDays();
            map.getChart().makeChart();
            map.getFile().update();
            if (map.isListEmpty()){
                runningLoop=false;
            }
            Platform.runLater(()->{
                mapChanged();
            });
        }
    }
}
