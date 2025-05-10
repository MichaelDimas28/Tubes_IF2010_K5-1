public class Crops extends Items implements Edible{
    int harvestAmount;
    int energy = 3;

    public Crops(String itemName, int buyPrice, int sellPrice,int harvestAmount, int energy){
        super(itemName, buyPrice, sellPrice);
        this.harvestAmount = harvestAmount;
        this.energy = energy;
    }

    public int getHarvestAmount() {
        return harvestAmount;
    }

    public void getEnergyRestore(){
        return;
    }
}
