package agh.ics.oop;

import java.util.*;


public class Animal implements IMapElement{
    private final List<IPositionChangeObserver> observers = new ArrayList<>();


    private Vector2d animalPosition;
    private int energy;
    MapDirection animalOrientation;
    private int[] genotype=new int[32];
    private final Random random=new Random();
    private int lifeLength;
    private int numberOfChildren;

    public Animal(int width,int height,int startEnergy){
        animalPosition=new Vector2d(random.nextInt(width),random.nextInt(height));
        for (int i=0;i<32;i++){
            genotype[i]=random.nextInt(8);
        }
        Arrays.sort(genotype);
        energy=startEnergy;
        animalOrientation=MapDirection.NORTH;
        lifeLength=0;
        numberOfChildren=0;
    }


    public Animal(Animal parent1,Animal parent2){
        animalPosition=parent1.animalPosition;
        int los=random.nextInt(2);
        if (los==0){
            makeGenotype(parent1,parent2);
        }
        else{
            makeGenotype(parent2,parent1);
        }
        energy=parent1.energy/4+parent2.energy/4;
        parent1.energy=parent1.energy-parent1.energy/4;
        parent2.energy=parent2.energy-parent2.energy/4;
        animalOrientation=MapDirection.NORTH;
        parent1.numberOfChildren++;
        parent2.numberOfChildren++;
        lifeLength=0;
    }

    public void setGenotype(int[] genotype) {
        this.genotype = genotype;
    }

    public int[] getGenotype(){
        return this.genotype;
    }

    public void incrementLifeLength(){
        lifeLength++;
    }

    public int getLifeLength(){
        return lifeLength;
    }

    public int getNumberOfChildren(){
        return numberOfChildren;
    }

    @Override
    public Vector2d getPosition(){
        return this.animalPosition;
    }


    public int getEnergy(){

        return this.energy;
    }

    private void makeGenotype(Animal p1, Animal p2){
        int ratio=(p1.energy*32)/(p1.energy+p2.energy);
        int j=0;
        for (int i=0;i<ratio;i++){
            genotype[i]=p1.genotype[i];
            j=i;
        }
        for(int k=j+1;k<32;k++){
            genotype[k]=p2.genotype[k];
        }
        Arrays.sort(genotype);
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer:observers){
            observer.positionChanged(oldPosition,newPosition,this);
        }
    }

    public void setEnergy(int e){
        this.energy+=e;
    }


    public void map1Move(int moveEnergy,int width,int height){
        Vector2d tmp;
        int randomGene=genotype[random.nextInt(32)];
        if (randomGene==0){
            tmp=animalPosition.add(MapDirection.toUnitVector(animalOrientation));
            this.energy-=moveEnergy;
            if(this.energy<0){this.energy=0;}
            tmp.x=(width + tmp.x)%width;
            tmp.y=(height + tmp.y)%height;
            positionChanged(animalPosition,tmp);
            animalPosition=tmp;
        }
        else if(randomGene==4){
            tmp=animalPosition.subtract(MapDirection.toUnitVector(animalOrientation));
            this.energy-=moveEnergy;
            if (this.energy<0){this.energy=0;}
            tmp.x=(width + tmp.x)%width;
            tmp.y=(height + tmp.y)%height;
            positionChanged(animalPosition,tmp);
            animalPosition=tmp;
        }
        else{
            for (int i=0;i<randomGene;i++){
                animalOrientation=MapDirection.next(animalOrientation);
            }
            this.energy-=moveEnergy;
            if (this.energy<0){this.energy=0;}
        }
    }

    public void map2Move(int moveEnergy,int width,int height){
        Vector2d tmp;
        int randomGene=genotype[random.nextInt(32)];
        if (randomGene==0){
            tmp=animalPosition.add(MapDirection.toUnitVector(animalOrientation));
            if(tmp.x>=0 && tmp.x<width && tmp.y>=0 && tmp.y<height) {
                this.energy -= moveEnergy;
                if (this.energy<0){this.energy=0;}
                positionChanged(animalPosition, tmp);
                animalPosition = tmp;
            }
        }
        else if(randomGene==4){
            tmp=animalPosition.subtract(MapDirection.toUnitVector(animalOrientation));
            if(tmp.x>=0 && tmp.x<width && tmp.y>=0 && tmp.y<height) {
                this.energy -= moveEnergy;
                if (this.energy<0){this.energy=0;}
                positionChanged(animalPosition, tmp);
                animalPosition = tmp;
            }
        }
        else{
            for (int i=0;i<randomGene;i++){
                animalOrientation=MapDirection.next(animalOrientation);
            }
            this.energy-=moveEnergy;
            if (this.energy<0){this.energy=0;}
        }
    }
}
