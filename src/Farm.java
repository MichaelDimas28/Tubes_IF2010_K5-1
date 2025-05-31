import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Farm {
    // private String farmName;
    private GamePanel gp;
    private int day = 1;
    private Season season = Season.Spring;
    private Weather weather = Weather.Sunny;
    private Time time = new Time(6, 0);
    // private Player farmer;
    // private FarmMap farm;
    private ShippingBin shippingBin;
    private Random randomGenerator = new Random();
    private int rainyDaysThisSeason = 0;
    private Weather weatherForTomorrow; 
    private FarmTile[][] tiles;

    Farm (GamePanel gp, Weather weather, int day, Season season, Time time, ShippingBin shippingBin) {
        this.gp = gp;
        this.weather = weather;
        this.day = day;
        this.season = season;
        // this.farmer = farmer;
        // this.farm = farmMap;
        this.time = time;
        this.shippingBin = shippingBin;
        if (this.weather == Weather.Rainy) { // Hitung hari hujan awal jika dimulai dengan hujan
            this.rainyDaysThisSeason = 1;
        }
        determineWeatherForTomorrow(); 
        tiles = new FarmTile[32][32];
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j<32 ; j++) {
                tiles[i][j] = new FarmTile();
            }
        }
    }

    public FarmTile getTileAt(int col, int row) {
        return tiles[col][row];
    }

    // public void setFarmName(String newName) {
    //     this.farmName = newName;
    // }

    public void nextDay() {
        for (int row = 0; row < 32; row++) {
            for (int col = 0; col < 32; col++) {
                FarmTile tile = getTileAt(col, row);
                if (tile.getSoilState() == SoilState.WATERED) {
                    tile.water(); // lakukan grow + update state
                }
            }
        }
    }

//     public void draw(Graphics2D g2) {
//     for (int row = 0; row < tiles.length; row++) {
//         for (int col = 0; col < tiles[0].length; col++) {
//             Tile tile = tiles[row][col];
//             if (tile != null) {
//                 g2.drawImage(tile.getImage(), col * 48, row * 48, null);
//             }
//         }
//     }
// }
    public void drawFarm(Graphics2D g2) {
        if (gp.currentMap != 0) return;

        for (int row = 0; row < 32; row++) {
            for (int col = 0; col < 32; col++) {
                FarmTile tile = getTileAt(col, row);

                int worldX = col * gp.tileSize;
                int worldY = row * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                // Cek apakah tile ada dalam layar
                if (screenX + gp.tileSize > 0 && screenX < gp.screenWidth &&
                    screenY + gp.tileSize > 0 && screenY < gp.screenHeight) {

                    switch (tile.getSoilState()) {
                        case LAND -> {} // default tile
                        case TILLED -> g2.drawImage(gp.tileM.tilledTile, screenX, screenY, gp.tileSize, gp.tileSize, null);
                        case WATERED -> g2.drawImage(gp.tileM.wateredTile, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }

                    switch (tile.getPlantState()) {
                        case PLANTED, HARVEST -> {
                            // g2.drawImage(gp.tileM.tilledTile, screenX, screenY, gp.tileSize, gp.tileSize, null);
                            int growth = tile.getGrowthDays();
                            int required = tile.getRequiredDays();

                            BufferedImage cropImg = gp.tileM.crop1;
                            if (growth == 0) cropImg = gp.tileM.crop1;
                            else if (growth < required) cropImg = gp.tileM.crop2;
                            else cropImg = gp.tileM.crop3;

                            g2.drawImage(cropImg, screenX, screenY, gp.tileSize, gp.tileSize, null);
                        }
                        case NONE -> {}
                    } 
                }
            }
        }
    }

    public void updateWeather (Weather weather) {
        this.weather = weather;
    }

    public void updateSeason (Season season) {
        this.season = season;
    }

    public Weather getWeather() {
        return weather;
    }

    public int getDay() {
        return day;
    }

    public Season getSeason() {
        return season;
    }

    public String getSeasonString() {
        switch(getSeason()) {
            case Spring:
            return "Spring";
            case Summer:
            return "Summer";
            case Fall:
            return "Fall";
            case Winter:
            return "Winter";
            default:
            return null;
        }
    }

    public String getWeatherString() {
        switch(getWeather()) {
            case Sunny:
            return "Sunny";
            case Rainy:
            return "Rainy";
            default:
            return null;
        }
    }

    public ShippingBin getShippingBin() {
        return shippingBin;
    }
    // public String getFarmName() {
    //     return farmName;
    // }
    public Time getTime() {
        return time;
    }

    public boolean checkForNewDay() {
        if (this.time.getHour() == 0 && this.time.getMinute() == 0) { // Baru saja melewati tengah malam
            return true; // Hari telah berganti
        }
        return false; // Belum ada pergantian hari
    }
    public void processNewDay() {
        this.day++; // nextDay() Anda sudah melakukan ini

        // 1. Update cuaca hari ini dari ramalan kemarin
        this.weather = this.weatherForTomorrow;
        if (this.weather == Weather.Rainy) {
            this.rainyDaysThisSeason++;
            // TODO: Efek hujan pada FarmMap (membuat tile basah)
            // if (this.farmMap != null) this.farmMap.setAllTilesWet(true);
            System.out.println("== HARI INI HUJAN! ==");
        } else {
            // if (this.farmMap != null) this.farmMap.setAllTilesWet(false);
        }

        // 2. Tentukan cuaca untuk besok
        determineWeatherForTomorrow();

        // 3. Update Musim jika 10 hari telah berlalu
        int dayInCurrentSeason = calculateDayInSeason();
        if (dayInCurrentSeason == 1 && this.day > 1) { // Baru masuk hari pertama musim baru (dan bukan hari pertama game)
            this.rainyDaysThisSeason = 0; // Reset counter hujan untuk musim baru
            int nextSeasonOrdinal = (this.season.ordinal() + 1) % Season.values().length;
            updateSeason(Season.values()[nextSeasonOrdinal]); // updateSeason() sudah ada
            System.out.println("======================================");
            System.out.println("MUSIM BERGANTI MENJADI: " + this.season);
            System.out.println("======================================");
            // TODO: Efek pergantian musim pada FarmMap (tanaman mati, dll.)
            // if (this.farmMap != null) this.farmMap.handleSeasonChange(this.season);
        }

        System.out.println("FARM: SELAMAT PAGI! Hari ke-" + this.day +
                           " Musim: " + this.season +
                           " (Hari ke-" + calculateDayInSeason() + ")" +
                           " Cuaca: " + this.weather);

        // TODO: Panggil proses harian lain di sini (pertumbuhan tanaman di FarmMap, Shipping Bin, dll)
    }

    private void determineWeatherForTomorrow() {
        // Logika penentuan cuaca besok, pastikan minimal 2 hari hujan per musim
        int currentDayInSeason = calculateDayInSeason();
        int daysRemainingInSeason = 10 - currentDayInSeason;
        int neededRainyDays = 2 - this.rainyDaysThisSeason;

        if (daysRemainingInSeason <= neededRainyDays && neededRainyDays > 0) {
            // Jika sisa hari <= sisa target hujan (dan masih ada target hujan), paksa hujan
            this.weatherForTomorrow = Weather.Rainy;
        } else if (neededRainyDays > 0 && randomGenerator.nextInt(daysRemainingInSeason +1 ) < neededRainyDays) {
            // Probabilitas untuk memenuhi sisa hujan jika masih ada slot hari
             this.weatherForTomorrow = Weather.Rainy;
        }
        else {
            this.weatherForTomorrow = randomGenerator.nextBoolean() ? Weather.Sunny : Weather.Rainy;
        }
        // System.out.println("[DEBUG] Prakiraan cuaca besok: " + this.weatherForTomorrow + " (Rainy days sejauh ini: " + this.rainyDaysThisSeason + ", Hari di musim: " + currentDayInSeason + ")");
    }

    public Weather getWeatherForTomorrow() { // Untuk aksi 'watch TV'
        return this.weatherForTomorrow;
    }

    public int calculateDayInSeason() {
        // Hari dalam permainan dimulai dari 1. Musim berganti setiap 10 hari.
        // Hari 1-10 -> Musim 1 (hari ke 1-10)
        // Hari 11-20 -> Musim 2 (hari ke 1-10)
        if (this.day == 0) return 1; // Kasus khusus jika day bisa 0
        int dayAdjusted = this.day -1; // Jadikan berbasis 0 untuk modulo
        return (dayAdjusted % 10) + 1;
    }

    public void checkPassiveActions(Player player) {
        if (player.isCooking && player.getRecipeBeingCooked() != null) {
            // Waktu saat ini di farm
            int currentFarmDay = this.getDay();
            int currentFarmTimeInt = this.time.getTimeAsInt();

            // Waktu selesai masak dari player
            int pCookingCompletionDay = player.getCookingCompletionDay();
            int pCookingCompletionTimeInt = player.getCookingCompletionTimeAsInt();

            if (currentFarmDay > pCookingCompletionDay ||
                (currentFarmDay == pCookingCompletionDay && currentFarmTimeInt >= pCookingCompletionTimeInt)) {
                player.finishCooking();
            }
        }
    }
}
