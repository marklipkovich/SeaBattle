
import model.ShipFactory;
import presenter.SeaBattlePresenter;
import view.SeaField;

public class SeaBattle {

    public static void main(String[] args) {

        SeaField gui = new SeaField();
        SeaBattlePresenter presenter = new SeaBattlePresenter(gui, new ShipFactory());
        gui.setPresenter(presenter);
        gui.initializeGui();
    }
}