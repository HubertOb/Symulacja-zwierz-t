package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.FileNotFoundException;


public class App extends Application implements IObserver{
    private final VBox options=new VBox();
    public int beginningNumberOfAnimals;
    public int mapSizeWidth;
    public int mapSizeHeight;
    public int startEnergy;
    public int moveEnergy;
    public int plantEnergy;
    public float jungleRatio;
    private SimulationEngine engine1;
    private SimulationEngine engine2;
    private Map1 map1;
    private Map2 map2;

    private final GridPane map1Grid=new GridPane();
    private final GridPane map2Grid=new GridPane();
    MakeCSVFile fileMap1=new MakeCSVFile();
    MakeCSVFile fileMap2=new MakeCSVFile();

    private TextField text1;
    private TextField width;
    private TextField height;
    private TextField energy;
    private TextField text4;
    private TextField text5;
    private TextField text6;
    private TextField text7;

    private Thread threadEngine1;
    private Thread threadEngine2;

    Label temp;

    private Chart lineChart;

    private int flag1=0; //0 - not running,1-running,2-stopped
    private int flag2=0; //0 - not running,1-running,2-stopped

    Label mag=new Label("");

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox hbox=new HBox();

        VBox vboxMap1=new VBox();
        HBox buttons=new HBox();
        Button startStop=new Button("Start");
        Button saveFile=new Button("Zapisz do pliku");
        buttons.getChildren().addAll(startStop,saveFile);

        HBox radioButtons=new HBox();
        RadioButton normalEvolution=new RadioButton("Ewolucja zwykla");
        RadioButton magicEvolution=new RadioButton("Ewolucja magiczna");
        radioButtons.getChildren().addAll(normalEvolution,magicEvolution);
        ToggleGroup group=new ToggleGroup();
        normalEvolution.setToggleGroup(group);
        normalEvolution.fire();
        magicEvolution.setToggleGroup(group);

        vboxMap1.getChildren().addAll(map1Grid,buttons,radioButtons);

        VBox vboxMap2=new VBox();
        HBox buttons2=new HBox();
        Button startStop2=new Button("Start");
        Button saveFile2=new Button("Zapisz do pliku");
        buttons2.getChildren().addAll(startStop2,saveFile2);

        HBox radioButtons2=new HBox();
        RadioButton normalEvolution2=new RadioButton("Ewolucja zwykla");
        RadioButton magicEvolution2=new RadioButton("Ewolucja magiczna");
        radioButtons2.getChildren().addAll(normalEvolution2,magicEvolution2);
        ToggleGroup group2=new ToggleGroup();
        normalEvolution2.setToggleGroup(group2);
        normalEvolution2.fire();
        magicEvolution2.setToggleGroup(group2);

        vboxMap2.getChildren().addAll(map2Grid,buttons2,radioButtons2);


        HBox maps=new HBox();
        maps.getChildren().addAll(vboxMap1,vboxMap2);
        VBox chartAndOptions=new VBox();
        hbox.getChildren().addAll(chartAndOptions,maps);


        makeOptions();
        lineChart=new Chart();
        chartAndOptions.getChildren().addAll(lineChart.getLineChart(),options);
        Scene scene=new Scene(hbox);
        primaryStage.setScene(scene);
        primaryStage.show();

        saveFile.setOnAction((c)->{
            try {
                fileMap1.writeData("Map1");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        saveFile2.setOnAction((d)->{
            try{
                fileMap2.writeData("Map2");
            } catch(FileNotFoundException e){
                e.printStackTrace();
            }
        });

        startStop.setOnAction((a)->{
            if (flag1==0) {
                try {
                    beginningNumberOfAnimals = Integer.parseInt(text1.getText());
                    mapSizeWidth = Integer.parseInt(width.getText());
                    mapSizeHeight = Integer.parseInt(height.getText());
                    startEnergy = Integer.parseInt(energy.getText());
                    moveEnergy = Integer.parseInt(text4.getText());
                    plantEnergy = Integer.parseInt(text5.getText());
                    jungleRatio = (float)Integer.parseInt(text6.getText()) / Integer.parseInt(text7.getText());
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }
                map1 = new Map1(mapSizeWidth, mapSizeHeight, jungleRatio, plantEnergy, startEnergy, lineChart, fileMap1);
                map1.addObserver(this);
                if(normalEvolution.isSelected()){
                    map1.setNormalEvolution();
                }
                if(magicEvolution.isSelected()){
                    map1.setMagicEvolution();
                }
                engine1.setParameters(beginningNumberOfAnimals, startEnergy, moveEnergy, map1);
                engine1.addObserver(this);
                threadEngine1 = new Thread(engine1);
                makeGrid(map1Grid, map1);

                threadEngine1.start();
                startStop.setText("Stop");
                flag1=1;
            }
            else if(flag1==1){
                threadEngine1.suspend();
                flag1=2;
                startStop.setText("Start");
            }
            else if(flag1==2){
                threadEngine1.resume();
                startStop.setText("Stop");
                flag1=1;
            }
        });

        startStop2.setOnAction((a)->{
            if (flag2==0) {
                try {
                    beginningNumberOfAnimals = Integer.parseInt(text1.getText());
                    mapSizeWidth = Integer.parseInt(width.getText());
                    mapSizeHeight = Integer.parseInt(height.getText());
                    startEnergy = Integer.parseInt(energy.getText());
                    moveEnergy = Integer.parseInt(text4.getText());
                    plantEnergy = Integer.parseInt(text5.getText());
                    jungleRatio = (float)Integer.parseInt(text6.getText()) / Integer.parseInt(text7.getText());
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }
                map2 = new Map2(mapSizeWidth, mapSizeHeight, jungleRatio, plantEnergy, startEnergy, lineChart, fileMap2);
                map2.addObserver(this);
                if(normalEvolution2.isSelected()){
                    map2.setNormalEvolution();
                }
                if(magicEvolution2.isSelected()){
                    map2.setMagicEvolution();
                }
                engine2.setParameters(beginningNumberOfAnimals, startEnergy, moveEnergy, map2);
                engine2.addObserver(this);
                threadEngine2 = new Thread(engine2);
                makeGrid(map2Grid, map2);

                threadEngine2.start();
                startStop2.setText("Stop");
                flag2=1;
            }
            else if(flag2==1){
                threadEngine2.suspend();
                flag2=2;
                startStop2.setText("Start");
            }
            else if(flag2==2){
                threadEngine2.resume();
                startStop2.setText("Stop");
                flag2=1;
            }
        });
    }


    private void makeOptions(){
        HBox hbox1=new HBox();
        Label label1=new Label("Ilosc zwierzat na poczatku: ");
        text1=new TextField("7");
        hbox1.getChildren().addAll(label1,text1);

        HBox hbox2=new HBox();
        Label label2=new Label("Rozmiar mapy (szer/wys): ");
        width=new TextField("8");
        height=new TextField("8");
        hbox2.getChildren().addAll(label2,width,height);

        HBox hbox3=new HBox();
        Label label3=new Label("Ilosc energii poczatkowej: ");
        energy=new TextField("150");
        hbox3.getChildren().addAll(label3,energy);

        HBox hbox4=new HBox();
        Label label4=new Label("Ilosc traconej energii kazdego dnia: ");
        text4=new TextField("7");
        hbox4.getChildren().addAll(label4,text4);

        HBox hbox5=new HBox();
        Label label5=new Label("Ilosc energii zyskiwanej po zjedzeniu rosliny: ");
        text5=new TextField("7");
        hbox5.getChildren().addAll(label5,text5);

        HBox hbox6=new HBox();
        Label label6=new Label("Proporcje dzungli do sawanny ");
        text6=new TextField("1");
        Label label7=new Label(" : ");
        text7=new TextField("4");
        hbox6.getChildren().addAll(label6,text6,label7,text7);

        HBox hbox7=new HBox();
        hbox7.getChildren().add(mag);

        options.getChildren().addAll(hbox1,hbox2,hbox3,hbox4,hbox5,hbox6,hbox7);
    }

    public void magic(){
        mag=new Label("Magia");
    }


    private void makeGrid(GridPane grid,IWorldMap mapka) {
        grid.getChildren().clear();
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        int xSize = mapSizeWidth;
        int ySize = mapSizeHeight;
        int xFirst = 0;
        int yFirst = 0;

        //grid.setGridLinesVisible(true);
        temp=new Label("y\\x");
        grid.add(temp,0,0,1,1);
        //GridPane.setHalignment(temp, HPos.CENTER);
        for (int i = 1; i<= xSize; i++){
            //temp=new Label(String.valueOf(i+ xFirst -1));
            //grid.add(temp,i,0,1,1);
            GridPane.setHalignment(temp, HPos.CENTER);
            grid.getColumnConstraints().add(new ColumnConstraints(50));
        }
        for (int j = 1; j<= ySize; j++){
            //temp=new Label(String.valueOf(ySize + yFirst -j));
            //grid.add(temp,0,j,1,1);
            GridPane.setHalignment(temp, HPos.CENTER);
            grid.getRowConstraints().add(new RowConstraints(50));
        }
        for (int i = 0; i< xSize; i++){
//            grid.getColumnConstraints().add(new ColumnConstraints(50));
            for (int j = 0; j< ySize; j++){
//                grid.getRowConstraints().add(new RowConstraints(50));
                if(mapka.getJunglePos(i, ySize -j-1) && mapka.isOccupied(new Vector2d(i, ySize -j-1))){
                    Pane pane=new Pane();
                    pane.setStyle("-fx-background-color: green;");
                    try {
                        GuiElementBox box = new GuiElementBox(mapka.ObjectAt(new Vector2d(i, ySize -j-1)));
                        pane.getChildren().add(box.getVbox());
                        grid.add(pane, i, j, 1, 1);
                        //GridPane.setHalignment(pane, HPos.CENTER);
                    }catch(FileNotFoundException e){
                        System.out.println(e);
                    }
                }
                else if (mapka.isOccupied(new Vector2d(i, ySize -j-1))){
                    try {
                        Pane pane=new Pane();
                        pane.setStyle("-fx-background-color: yellow;");
                        GuiElementBox box = new GuiElementBox(mapka.ObjectAt(new Vector2d(i, ySize -j-1)));
                        pane.getChildren().add(box.getVbox());
                        grid.add(pane, i, j, 1, 1);
                        //GridPane.setHalignment(box.getVbox(), HPos.CENTER);
                    }catch(FileNotFoundException e){
                        System.out.println(e);
                    }
                }
                else if(mapka.getJunglePos(i, ySize -j-1)){
                    Pane pane=new Pane();
                    pane.setStyle("-fx-background-color: green;");
                    grid.add(pane, i, j, 1, 1);
                    //GridPane.setHalignment(pane, HPos.CENTER);
                }
                else{
                    Pane pane=new Pane();
                    pane.setStyle("-fx-background-color: yellow;");
                    grid.add(pane, i, j, 1, 1);
                    //GridPane.setHalignment(pane, HPos.CENTER);
                }
            }
        }
        grid.getColumnConstraints().add(new ColumnConstraints(50));
        grid.getRowConstraints().add(new RowConstraints(50));
        grid.getRowConstraints().add(new RowConstraints(50));
        grid.add(mag,xSize/2,ySize+1);
    }




    @Override
    public void init(){
        engine1=new SimulationEngine();
        engine2=new SimulationEngine();
    }

    @Override
    public void positionChanged() {
        Platform.runLater(()->{
            if (map1!=null) {
                map1Grid.setGridLinesVisible(true);
                makeGrid(map1Grid, map1);
            }
            if (map2!=null) {
                map2Grid.setGridLinesVisible(true);
                makeGrid(map2Grid, map2);
            }
            lineChart.makeChart();
        });
    }
}
