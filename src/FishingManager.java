import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FishingManager {
    GamePanel gp;
    Random rand = new Random();
    
    public FishingManager(GamePanel gp) {
        this.gp = gp;
    }

    public void startFishing() {
        String spotName = switch(gp.currentMap) {
            case 0 -> "Pond";
            case 7 -> "Ocean";
            case 8 -> "Forest River";
            case 9 -> "Mountain Lake";
            default -> null;
        };

        if (spotName == null) {
            gp.ui.showMessage("Tidak bisa memancing di sini!");
            return;
        }

        if (gp.player.getEnergy() < 5) {
            gp.ui.showMessage("Energi tidak cukup untuk memancing!");
            return;
        }

        gp.gamePaused = true;
        
        Fish caughtFish = determineFish(spotName);
        
        if (caughtFish == null) {
            gp.ui.showMessage("Tidak ada ikan yang tersedia saat ini!");
            gp.gamePaused = false;
            return;
        }
        // boolean success = playFishing(caughtFish);
        prepareFishing(caughtFish);
        
        // if (success) {
            //     gp.player.getInventory().addItem(new InventoryItem(gp.itemManager.getItem(caughtFish.getItemName()), 1));
            //     gp.ui.showMessage("Kamu menangkap: "+caughtFish.getItemName());
            // } else {
                //     gp.ui.showMessage("Ikan berhasil lolos...");
                // }
                
        gp.gamePaused = false;
    }
            
    private Fish determineFish(String spotName) {
        // Bisa diganti dengan peluang
        int roll = rand.nextInt(100);
        Map<String, Fish> allFish = new HashMap<>();
        Map<String, Fish> rarityFish = new HashMap<>();
        Map<String, Fish> seasonFish = new HashMap<>();
        Map<String, Fish> weatherFish = new HashMap<>();
        Map<String, Fish> timeFish = new HashMap<>();
        Map<String, Fish> availableFish = new HashMap<>();
        allFish = gp.itemManager.getAllFish();
        
        // Filter ikan berdasarkan rarity
        for (Fish fish : allFish.values()) {
            if (roll < 50) {
                if (fish.getRarity().equals(Rarity.Common)) {
                    rarityFish.put(fish.getItemName(), fish);
                }
            } else if (roll <85) {
                if (fish.getRarity().equals(Rarity.Regular)) {
                    rarityFish.put(fish.getItemName(), fish);
                }
            } else {
                if (fish.getRarity().equals(Rarity.Legendary)) {
                    rarityFish.put(fish.getItemName(), fish);
                }
            }
        }


        for (Fish fish: rarityFish.values()) {
            // Filter ikan berdasarkan season
            for (int i = 0; i<fish.getSeasonAppear().size(); i++) {
                if (fish.getSeasonAppear().contains("Any") || fish.getSeasonAppear().contains(gp.farm.getSeasonString())) {
                    seasonFish.put(fish.getItemName(), fish);
                }
            }
        }

        for (Fish seasonfish: seasonFish.values()) {
            // Filter ikan berdasarkan cuaca
            for (int i = 0; i<seasonfish.getWeatherAppear().size(); i++) {
                if (seasonfish.getWeatherAppear().contains("Any") || seasonfish.getWeatherAppear().contains(gp.farm.getWeatherString())) {
                    weatherFish.put(seasonfish.getItemName(), seasonfish);
                }
            }
        }
            
        for (Fish weatherfish: weatherFish.values()) {
            // Filter ikan berdasarkan waktu muncul
            // System.out.println(gp.farm.getTime().getTimeAsInt());
            for (int i = 0; i<weatherfish.getTimeAppear().size(); i++) {
                if (gp.farm.getTime().isWithin(weatherfish.fishStartAppear(i), weatherfish.fishStopAppear(i))) {
                    timeFish.put(weatherfish.getItemName(), weatherfish);
                }
            }
        }

        for (Fish timefish: timeFish.values()) {
            // System.out.println(gp.player.locationName());
            // System.out.println(spotName);
            // Filter ikan berdasarkan lokasi
            for (int i = 0; i<timefish.getLocationAppear().size(); i++) {
                // System.out.println(timefish.getLocationAppear());
                if (timefish.getLocationAppear().contains("Any") || timefish.getLocationAppear().contains(spotName)) {
                    availableFish.put(timefish.getItemName(), timefish);
                }
            }
        }
        // System.out.println(allFish);
        // System.out.println(rarityFish);
        // System.out.println(seasonFish);
        // System.out.println(weatherFish);
        // System.out.println(timeFish);
        // System.out.println(availableFish);
        if (availableFish.isEmpty()) {
            gp.ui.showMessage("Tidak ada ikan yang tersedia saat ini!");
            return null;
        }
        List<Fish> fishList = new ArrayList<>(availableFish.values());
        return fishList.get(rand.nextInt(fishList.size()));
    }

    public void prepareFishing(Fish fish) {
        int maxTries;
        int maxNumber;

        if (fish.getRarity().equals(Rarity.Common)) {
            maxTries = 10;
            maxNumber = 10;
        } else if (fish.getRarity().equals(Rarity.Regular)) {
            maxTries = 10;
            maxNumber = 100;
        } else {
            maxTries = 7;
            maxNumber = 500;
        }

        gp.farm.getTime().skipTime(15, null);
        gp.player.setEnergy(gp.player.getEnergy()-5);

        gp.ui.fishingActive = true;
        gp.ui.fishingInput = "";
        gp.ui.fishingGuessing = true;
        gp.ui.fishingTarget = new Random().nextInt(maxNumber) + 1;
        gp.ui.fishingMaxNumber = maxNumber;
        gp.ui.fishingTriesLeft = maxTries;
        gp.ui.currentFishingFish = fish;
    }


    // private boolean playFishing(Fish fish) {
    //     int maxTries;
    //     int maxNumber;
    //     if (fish.getRarity().equals(Rarity.Common) || fish.getRarity().equals(Rarity.Regular)) {
    //         maxTries = 10;
    //     } else {
    //         maxTries = 7;
    //     }

    //     if (fish.getRarity().equals(Rarity.Common)) {
    //         maxNumber = 10;
    //     } else if (fish.getRarity().equals(Rarity.Regular)) {
    //         maxNumber = 100;
    //     } else {
    //         maxNumber = 500;
    //     }

    //     int target = rand.nextInt(maxNumber) + 1;
    //     Scanner scanner = new Scanner(System.in);

    //     for (int i = 1; i <= maxTries; i++) {
    //         System.out.print("Tebak angka (1-" + maxNumber + "): ");
    //         int guess = scanner.nextInt();
    //         if (guess == target) return true;
    //         System.out.println("Salah! Coba lagi (" + (maxTries - i) + " kali tersisa)");
    //     }
    //     return false;
    // }
}

