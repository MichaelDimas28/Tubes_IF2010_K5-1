public class Fish extends Items implements Edible {
    String seasonAppear;
    int[] timeAppear;
    String weatherAppear;
    String[] locationAppear;
    String rarity;
    int energi = 1;

    public Fish(
        String itemName, 
        int buyPrice, 
        int sellPrice, 
        String seasonAppear, 
        int[] timeAppear, 
        String weatherAppear, 
        String[] locationAppear, 
        String rarity,
        int energi) {
        super(itemName, buyPrice, sellPrice);
        this.seasonAppear = seasonAppear;
        this.timeAppear = timeAppear;
        this.weatherAppear = weatherAppear;
        this.locationAppear = locationAppear;
        this.rarity = rarity;
        this.energi = energi;
    }

    public String getSeasonAppear() {
        return seasonAppear;
    }

    public int[] getTimeAppear() {
        return timeAppear;
    }

    public String getWeatherAppear() {
        return weatherAppear;
    }
    
    public String[] getLocationAppear() {
        return locationAppear;
    }

    public String getRarity() {
        return rarity;
    }

    public int getEnergyRestore(){
        return this.energi;
    }
    
}
