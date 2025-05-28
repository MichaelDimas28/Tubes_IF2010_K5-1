public class Crops extends Items implements Edible{
    int harvestAmount;
    int energy = 3;

    public Crops(String itemName, int buyPrice, int sellPrice,int harvestAmount, String imageName){
        super(itemName, buyPrice, sellPrice, imageName);
        this.harvestAmount = harvestAmount;
    }

    public int getHarvestAmount() {
        return harvestAmount;
    }

    public int getEnergyRestore(){
        return this.energy;
    }
}
