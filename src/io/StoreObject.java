package io;

import presenter.Ship;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;

public class StoreObject implements Serializable {
    private  List<Ship> ships;
    private  JButton[][] buttonGrid;

    public StoreObject(List<Ship> ships, JButton[][] buttonGrid) {
        this.ships = ships;
        this.buttonGrid = buttonGrid;
    }

    public StoreObject(JButton[][] buttonGrid) {
        this.buttonGrid = buttonGrid;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public JButton[][] getButtonGrid() {
        return buttonGrid;
    }

}

