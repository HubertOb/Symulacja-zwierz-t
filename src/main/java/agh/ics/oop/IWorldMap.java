package agh.ics.oop;

import agh.ics.oop.gui.Chart;

import java.util.List;


public interface IWorldMap {
    void placeNewAnimal();

    void moveAnimals(int moveEnergy);

    IMapElement ObjectAt(Vector2d position);

    boolean isOccupied(Vector2d position);

    boolean isListEmpty();

    void checkIfAnimalToBorn();

    void bornNewAnimal(List<Animal> list);

    void makeGrass();

    void animalsAtTheSamePosition(Vector2d position);

    void checkIsGrassToEat();

    Chart getChart();

    MakeCSVFile getFile();

    void setMagicEvolution();

    void setNormalEvolution();

    boolean getJunglePos(int x, int y);
}
