package agh.ics.oop;

import agh.ics.oop.gui.App;
import agh.ics.oop.gui.Chart;
import javafx.util.Pair;


import java.util.*;

public class Map2 implements IWorldMap,IPositionChangeObserver{
    private final int width;
    private final int height;
    private final int plantEnergy;
    private final int startEnergy;
    private final Chart lineChart;
    private int magicEvolution;
    MakeCSVFile file;

    private App observer;

    Jungle jungle;
    private final Map<Vector2d,Grass> grassLinkedHashMap=new LinkedHashMap<>();
    private final List<Vector2d> grassToRemove=new LinkedList<>();
    private final Map<Pair<Vector2d,Animal>,Animal> animals =new LinkedHashMap<>();
    private Map<Pair<Vector2d,Animal>,Animal> animalsToRemove =new LinkedHashMap<>();
    private Map<Pair<Vector2d,Animal>,Vector2d> animalsChanged =new LinkedHashMap<>();
    private final List<Animal> animalsToAdd=new LinkedList<>();
    private final TreeSet<Pair<Vector2d,Animal>> sortedAnimals=new TreeSet<>((a,b)->{
        if (a.getKey().x>b.getKey().x){return 1;}
        if (a.getKey().x<b.getKey().x){return -1;}
        if (a.getKey().y>b.getKey().y){return 1;}
        if (a.getKey().y<b.getKey().y){return -1;}
        if (a.getValue().getEnergy()>b.getValue().getEnergy()){return -1;}
        if (a.getValue().getEnergy()<b.getValue().getEnergy()){return 1;}
        if (a.getValue().equals(b.getValue())){
            return 0;
        }
        else{
            return 1;
        }
    });
    TreeSet<Animal> animalsAtPosition=new TreeSet<>((a,b)->{
        if (a.getEnergy()>b.getEnergy()){return -1;}
        if (a.getEnergy()<b.getEnergy()){ return 1;}
        if (a.equals(b)){
            return 0;
        }
        else{
            return 1;
        }
    });





    public Map2(int w, int h,float ratio,int energy,int s,Chart l,MakeCSVFile f){
        width=w;
        height=h;
        plantEnergy=energy;
        startEnergy=s;
        lineChart=l;
        file=f;
        jungle=new Jungle(ratio,width,height);
    }

    @Override
    public void placeNewAnimal(){
        Animal tmp=new Animal(width,height,startEnergy);
        tmp.addObserver(this);
        lineChart.incrementNumberOfAnimals();
        file.incrementNumberOfAnimals();
        animals.put(new Pair<>(tmp.getPosition(),tmp),tmp);
        sortedAnimals.add(new Pair(tmp.getPosition(),tmp));
    }

    public void addObserver(App obs){
        this.observer=obs;
    }

    @Override
    public void bornNewAnimal(List<Animal> list){
        if (list.size()<2){
            return;
        }
        if (list.get(0).getEnergy()<startEnergy/2 || list.get(1).getEnergy()<startEnergy/2){
            return;
        }
        Animal tmp=new Animal(list.get(0),list.get(1));
        tmp.addObserver(this);
        lineChart.incrementNumberOfAnimals();
        file.incrementNumberOfAnimals();
        Pair<Vector2d,Animal> k=new Pair(tmp.getPosition(),tmp);
        animalsToAdd.add(tmp);
    }

    @Override
    public void makeGrass() {
        Grass grass=new Grass();
        grass.makeSavannaGrass(jungle.getUpperLeft(), jungle.getLowerRight(), width, height);
        grassLinkedHashMap.put(grass.getPosition(),grass);
        lineChart.incrementNumberOfPlant();
        file.incrementNumberOfPlant();
        Grass grass2=new Grass();
        grass2.makeJungleGrass(jungle.getUpperLeft(), jungle.getLowerRight());
        grassLinkedHashMap.put(grass2.getPosition(),grass2);
        lineChart.incrementNumberOfPlant();
        file.incrementNumberOfPlant();
    }

    @Override
    public void animalsAtTheSamePosition(Vector2d position) {
        for (Animal animal:animals.values()){
            if(animal.getPosition().equals(position)){
                animalsAtPosition.add(animal);
            }
        }
    }

    @Override
    public void checkIsGrassToEat(){
        for (Grass grass:grassLinkedHashMap.values()){
            int energy=plantEnergy;
            int k=0;
            animalsAtTheSamePosition(grass.getPosition());
            if(!animalsAtPosition.isEmpty()){
                k=cosik();
                energy=(int) plantEnergy/k;
            }
            for(Animal animal:animalsAtPosition){
                if(k>0){
                    animal.setEnergy(energy);
                    k--;
                }
                else{
                    break;
                }
            }
            grassToRemove.add(grass.getPosition());
            animalsAtPosition.clear();
        }
        removeGrass();
    }

    @Override
    public Chart getChart() {
        return lineChart;
    }

    @Override
    public MakeCSVFile getFile() {
        return file;
    }

    @Override
    public void setMagicEvolution() {
        magicEvolution=3;
    }

    @Override
    public void setNormalEvolution() {
        magicEvolution=0;
    }

    @Override
    public boolean getJunglePos(int x, int y) {
        int xLeft= jungle.getUpperLeft().x;
        int xRight=jungle.getLowerRight().x;
        int yLower= jungle.getLowerRight().y;
        int yUpper= jungle.getUpperLeft().y;
        return x >= xLeft && x < xRight && y >= yLower && y < yUpper;
    }

    private void removeGrass(){
        for(Vector2d pos:grassToRemove){
            grassLinkedHashMap.remove(pos);
            lineChart.decrementNumberOfPlant();
            file.decrementNumberOfPlant();
        }
        grassToRemove.clear();
    }

    private int cosik(){  //how many animals with the same energy value at the same position
        int k=1;
        Iterator<Animal> iter=animalsAtPosition.iterator();
        Animal a=iter.next();
        Animal b;
        while(iter.hasNext()){
            b=iter.next();
            if(a.getEnergy()==b.getEnergy()){k++;}
            else{break;}
        }
        return k;
    }

    private void copyAnimals(){
        List<Animal> animalsToAdd=new LinkedList<>();
        for (Animal animal:animals.values()){
            Animal tmp=new Animal(width,height,startEnergy);
            tmp.animalOrientation=animal.animalOrientation;
            tmp.setGenotype(animal.getGenotype());
            animalsToAdd.add(tmp);
        }
        for (int i=0;i<animalsToAdd.size();i++){
            Animal a=animalsToAdd.get(i);
            Pair<Vector2d,Animal> k=new Pair(a.getPosition(),a);
            animals.put(k,a);
        }
    }

    @Override
    public void moveAnimals(int moveEnergy) {
        if (magicEvolution>0 && animals.size()==5){
            copyAnimals();
            magicEvolution--;
            observer.magic();
            return;
        }
        for (Animal animal:animals.values()){
            if (animal.getEnergy()>moveEnergy){
                animal.map2Move(moveEnergy,width,height);
                animal.incrementLifeLength();
            }
            else{
                animalsToRemove.put(new Pair<>(animal.getPosition(),animal),animal);
            }
        }
        for(Pair key:animalsChanged.keySet()){
            animals.remove(key);
            sortedAnimals.remove(key);
            animals.put(new Pair(animalsChanged.get(key),key.getValue()), (Animal) key.getValue());
            sortedAnimals.add(new Pair(animalsChanged.get(key),key.getValue()));
        }
        animalsChanged=new LinkedHashMap<>();

        for (Pair key:animalsToRemove.keySet()){
            lineChart.addSumLifeLength(animals.get(key).getLifeLength());
            file.addSumLifeLength(animals.get(key).getLifeLength());
            lineChart.incrementNumberOfDeathAnimals();
            file.incrementNumberOfDeathAnimals();
            animals.remove(key);
            sortedAnimals.remove(key);
            lineChart.decrementNumberOfAnimals();
            file.decrementNumberOfAnimals();
        }
        checkIsGrassToEat();
        animalsToRemove =new LinkedHashMap<>();
        checkIfAnimalToBorn();
        lineChart.addAvgEnergyLevel(animals);
        file.addAvgEnergyLevel(animals);
        lineChart.addAvgNumberOfChildren(animals);
        file.addAvgNumberOfChildren(animals);
    }



    @Override
    public IMapElement ObjectAt(Vector2d position) {
        for(Animal animal:animals.values()){
            if(position.equals(animal.getPosition())){
                return animal;
            }
        }
        if(grassLinkedHashMap.containsKey(position)){
            return grassLinkedHashMap.get(position);
        }
        return null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        for (Animal animal:animals.values()){
            if (position.equals(animal.getPosition())){
                return true;
            }
        }
        return grassLinkedHashMap.containsKey(position);
    }

    @Override
    public boolean isListEmpty() {
        return animals.isEmpty();
    }

    @Override
    public void checkIfAnimalToBorn() {
        Iterator<Pair<Vector2d,Animal>> iter=sortedAnimals.iterator();
        List<Animal> animalsAtTheSamePosition=new LinkedList<>();
        Pair<Vector2d,Animal> k;
        Pair<Vector2d,Animal> l;
        if(!iter.hasNext()){
            return;
        }
        k=iter.next();
        while(iter.hasNext()) {
            animalsAtTheSamePosition.add(k.getValue());
            l=iter.next();
            if(k.getKey().equals(l.getKey())){
                k=l;
            }
            else{
                bornNewAnimal(animalsAtTheSamePosition);
                animalsAtTheSamePosition.clear();
                k=l;
            }
        }
        for(Animal animal:animalsToAdd){
            k=new Pair<>(animal.getPosition(),animal);
            animals.put(k,animal);
            sortedAnimals.add(k);
        }
        animalsToAdd.clear();
    }


    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition,Animal animal) {
        animalsChanged.put(new Pair(oldPosition,animal),newPosition);
    }




}
