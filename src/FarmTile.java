public class FarmTile extends Tile {
    // private TileState state;
    private SoilState soilState;
    private PlantState plantState;
    private Seeds seed;
    private int growthDays;
    private int requiredDaysToGrow;

    public FarmTile() {
        // this.state = TileState.LAND;
        soilState = SoilState.LAND;
        plantState = PlantState.NONE;
        this.seed = null;
        this.growthDays = 0;
        this.requiredDaysToGrow = 0;
    }

    // public TileState getState() { return state; }
    public SoilState getSoilState() { return soilState; }
    public PlantState getPlantState() { return plantState; }
    public void setSoilState(SoilState state) { soilState = state; }
    public void setPlantState(PlantState state) { plantState = state; }

    public Seeds getSeed() { return seed; }

    public int getGrowthDays() { return growthDays; }
    public int getRequiredDays() { return requiredDaysToGrow; }

    public void plantSeed(Seeds seed) {
        this.seed = seed;
        this.requiredDaysToGrow = seed.getDaysToHarvest();
        this.growthDays = 0;
        plantState = PlantState.PLANTED;
    }

    public void grow() {
        if (plantState == PlantState.PLANTED || soilState == SoilState.WATERED) {
            growthDays++;
            if (growthDays >= requiredDaysToGrow) {
                plantState = PlantState.HARVEST;
            } else {
                soilState = SoilState.TILLED;
            }
        }
    }

    public void water() {
        if (soilState.equals(SoilState.TILLED)) {
            soilState = SoilState.WATERED;
        }
    }

    public void reset() {
        // this.state = TileState.LAND;
        soilState = SoilState.LAND;
        plantState = PlantState.NONE;
        this.seed = null;
        this.growthDays = 0;
        this.requiredDaysToGrow = 0;
    }
}
