package presenter;

import io.FileIO;
import io.StoreObject;
import java.io.IOException;
import model.ShipFactory;
import view.SeaField;
import java.io.File;
import java.util.List;

public class SeaBattlePresenter {

     private FileIO fileIO = new FileIO();
     private SeaField field;
     private ShipFactory factory;
     private byte boardSize;
     private byte numberShips;
     private byte maxShipSize;
     private List<Ship> ships;

     public SeaBattlePresenter(SeaField field, ShipFactory factory) {
        this.field = field;
        this.factory = factory;
        numberShips = factory.getNumShipsArray()[factory.getNumShipsArray().length-1];
        maxShipSize = factory.getMaxShipsArray()[factory.getMaxShipsArray().length-1];
        boardSize = factory.getBoardSize();
        ships = factory.createFleet(numberShips, maxShipSize);
    }

     public void newGame(){
        ships = factory.createFleet(numberShips, maxShipSize);
        field.newGame();
    }

     public void shot(String cell){
        String result;
        int cellInt = Integer.parseInt(cell);
        field.miss(cellInt);


        for (Ship ship: ships) {
            result = ship.shot(cellInt);
            if (result.equals("HIT")) {
                field.hit(cellInt);


                break;
            }
            if (result.equals("KILLED")) {
                shipKilled(ship);
                break;
            }
        }
    }

    private void shipKilled(Ship ship) {
        field.shipKilled(ship.getArea(),ship.getNumber());
        ships.remove(ship);
        if (ships.isEmpty()) {
            field.gameOver();
        }
    }

     public void saveFile(File file, StoreObject storeObj){
       storeObj.setShips(ships);
       try {
         fileIO.save(file, storeObj);
       }
       catch (IOException e) {
         field.saveError();
       }
    }

     public StoreObject loadFile(File file){
       StoreObject loadObj = null;
       try {
         loadObj = fileIO.load(file);
       } catch (IOException | ClassNotFoundException e) {
         field.loadError();
       }
       if (loadObj != null) {
         ships = loadObj.getShips();
       }

       field.loadGame();
        return loadObj;
     }

     public void selectNumberShips(Byte number){numberShips = number;}

     public void selectMaxShipSize(Byte number){maxShipSize = number;}

     public void onInitGui(){
        field.setInitGui(boardSize, factory.getMaxShipsArray(), maxShipSize, factory.getNumShipsArray(), numberShips);
     }
}
