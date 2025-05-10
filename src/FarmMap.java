import java.io.File;
import java.io.IOException;
import java.util.Random;

public class FarmMap extends Map {
    public FarmMap(String filePath) throws IOException {
        super("Farm", 32, 32, filePath);
    }

    public static void main(String args[]) {
        
        
        Random random = new Random();
        int mapNumber = random.nextInt(3) + 1;
        
        String filePath = "./data/farmMap/farm_map"+mapNumber+".txt";
        FarmMap farmMap = null;

        System.out.println("Trying to load file: " + filePath);
        File file = new File(filePath);
        System.out.println("File exists? " + file.exists());

        try {
            farmMap = new FarmMap(filePath);
            System.out.println("Berhasil memuat FarmMap ke-"+mapNumber);
            farmMap.showMap();
        } catch (IOException e) {
            System.out.println("Gagal membuat peta");
        }
    }
}
