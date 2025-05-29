import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorldMap extends Maps {
    private String[] availableAction = {"move", "visit"};
    private List<String> actionNature = new ArrayList<>();
    private List<String> actionHouse = new ArrayList<>();
    private List<String> actionStore = new ArrayList<>();
    private List<String> actionFarm = new ArrayList<>();
    private Location river = new Location(actionNature);
    private Location lake = new Location(actionNature);
    private Location ocean = new Location(actionNature);
    private Location mayor = new Location(actionHouse);
    private Location perry = new Location(actionHouse);
    private Location abigail = new Location(actionHouse);
    private Location dasco = new Location(actionHouse);
    private Location store = new Location(actionStore);
    private Location caroline = new Location(actionStore);
    private Location farm = new Location(actionFarm);
    private Location[] locations = {farm, river, lake, ocean, mayor, perry, abigail, dasco, caroline, store};
    private Location currLocation = farm;

    public WorldMap (String filePath) throws IOException {
        super("World Map", 32, 9, filePath);
    }

    public Location checkCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(Location location) {
        this.currLocation = location;
    }

    public String[] getAvailableAction() {
        return availableAction;
    }

    public void visitStore(Location location) {
        if (currLocation.equals(location) && location.equals(locations[9])) {
            System.out.println("Emily: Selamat datang di toko!");
        } else if (currLocation.equals(location) && location.equals(locations[8])) {
            System.out.println("Abigail: Halo! Apakah kamu ingin membeli kayu?");
        }
    }

    public void visitNPCHouse (Location location) {
        if (currLocation.equals(location) && location.equals(locations[4])) {
            System.out.println("Anda telah tiba di rumah Mayor Tadi!");
        } else if (currLocation.equals(location) && location.equals(locations[5])) {
            System.out.println("Anda telah tiba di rumah Perry!");
        } else if (currLocation.equals(location) && location.equals(locations[6])) {
            System.out.println("Anda telah tiba di rumah Abigail!");
        } else if (currLocation.equals(location) && location.equals(locations[7])) {
            System.out.println("Anda telah tiba di rumah Dasco!");
        }
    }

    public void visitNature (Location location) {
        if (currLocation.equals(location) && location.equals(locations[1])) {
            System.out.println("Anda telah tiba di Forest River!");
        } else if (currLocation.equals(location) && location.equals(locations[2])) {
            System.out.println("Anda telah tiba di Mountain Lake!");
        } else if (currLocation.equals(location) && location.equals(locations[3])) {
            System.out.println("Anda telah tiba di Ocean!");
        }
    }

    public void visitFarm (Location location) {
        if (currLocation.equals(location) && location.equals(locations[0])) {
            System.out.println("Anda telah kembali ke Farm!");
        }
    }
}