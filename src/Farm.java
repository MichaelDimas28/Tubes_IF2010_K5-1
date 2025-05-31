import java.util.Random;

public class Farm {
    // private String farmName;
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

    Farm (Weather weather, int day, Season season, Time time, ShippingBin shippingBin) {
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
    }

    // public void setFarmName(String newName) {
    //     this.farmName = newName;
    // }

    public void nextDay() {
        day++;
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
