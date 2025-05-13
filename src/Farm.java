public class Farm {
    private String farmName;
    private Season season;
    private enum Weather {
        Sunny, Rainy;
    }
    private Weather weather;
    private int day = 1;
    private Time time;
    private Player farmer;
    private FarmMap farm;
    private ShippingBin shippingBin;

    Farm (String name, Weather weather, int day, Season season, Player farmer, FarmMap farmMap, Time time, ShippingBin shippingBin) {
        this.farmName = name;
        this.weather = weather;
        this.day = day;
        this.season = season;
        this.farmer = farmer;
        this.farm = farmMap;
        this.time = time;
        this.shippingBin = shippingBin;
    }

    public void setFarmName(String newName) {
        this.farmName = newName;
    }

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
}
