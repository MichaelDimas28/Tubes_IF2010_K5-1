public class Food extends Items implements Edible{
    int energy;
    Boolean recipeAquiredStatus;
    String[] ingredients;

    public Food(String itemName, int buyPrice, int sellPrice,int energy,String[] ingredients){
        super(itemName, buyPrice, sellPrice);
        recipeAquiredStatus = false;
        this.ingredients = ingredients;
        this.energy = energy;
    }

    public void setRecipeAquiredStatus(Boolean recipeAquiredStatus) {
        this.recipeAquiredStatus = recipeAquiredStatus;
    }
    
    public Boolean getRecipeAquiredStatus() {
        return recipeAquiredStatus;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public int getEnergyRestore(){
        return this.energy;
    }

}
