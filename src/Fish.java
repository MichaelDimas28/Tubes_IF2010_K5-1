import java.util.List;

public class Fish extends Items implements Edible {

    private List<String> seasonAppear;         // bisa lebih dari 1
    private List<int[]> timeAppear;            // setiap int[] = {start, end}
    private List<String> locationAppear;       // bisa lebih dari 1 lokasi
    private List<String> weatherAppear;        // bisa ["Any"], ["Sunny"], ["Rainy"], dll
    private Rarity rarity;                 // enum: COMMON, REGULAR, LEGENDARY
    private final int energy = 1;


    public Fish(
        String itemName,
        List<String> seasonAppear,
        List<int[]> timeAppear,
        List<String> weatherAppear,
        List<String> locationAppear,
        Rarity rarity,
        String imageName
    ) {
        // Harga jual akan dihitung otomatis dari rumus, buyPrice bisa -1 jika tidak dijual
        super(itemName, imageName);
        this.seasonAppear = seasonAppear;
        this.timeAppear = timeAppear;
        this.weatherAppear = weatherAppear;
        this.locationAppear = locationAppear;
        this.rarity = rarity;
        this.sellPrice = calculateSellPriceByFormula();
    }

    // Getter
    public List<String> getSeasonAppear() {
        return seasonAppear;
    }

    public List<int[]> getTimeAppear() {
        return timeAppear;
    }

    public List<String> getWeatherAppear() {
        return weatherAppear;
    }

    public List<String> getLocationAppear() {
        return locationAppear;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public int getEnergyRestore() {
        return energy;
    }

    // Utility function
    private int calculateSellPriceByFormula() {
        int seasonCount = seasonAppear.contains("Any") ? 4 : seasonAppear.size();
        int weatherCount = weatherAppear.contains("Any") ? 2 : weatherAppear.size();
        int locationCount = locationAppear.size();

        int totalHours = 0;
        for (int[] range : timeAppear) {
            int start = range[0]; // e.g. 600 for 06:00
            int end = range[1];   // e.g. 1800 for 18:00
            if (end < start) end += 2400; // handle 20:00 - 02:00 as 2000 - 2600
            totalHours += (end - start) / 100;
        }

        int C;
        switch (rarity) {
            case Common: C = 10; break;
            case Regular: C = 5; break;
            case Legendary: C = 25; break;
            default: C = 5; break;
        }

        // Formula: (4 / seasonCount) * (24 / totalHours) * (2 / weatherCount) * (4 / locationCount) * C
        double price = (4.0 / seasonCount)
                     * (24.0 / totalHours)
                     * (2.0 / weatherCount)
                     * (4.0 / locationCount)
                     * C;

        return (int) Math.round(price);
    }
}
