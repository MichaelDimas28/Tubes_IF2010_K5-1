import java.util.ArrayList;
import java.util.List;

public class Food extends Items implements Edible {

    private int energy;
    private boolean recipeAcquired;
    private List<Items> ingredients;

    // Constructor untuk food biasa (tanpa resep, bisa dibeli)
    public Food(String itemName, int buyPrice, int sellPrice, int energy, String imageName) {
        super(itemName, buyPrice, sellPrice, imageName);
        this.energy = energy;
        this.recipeAcquired = false;
        this.ingredients = new ArrayList<>();
    }

    // Constructor untuk food dengan resep dan bahan-bahan
    public Food(String itemName, int buyPrice, int sellPrice, int energy, List<Items> ingredients, String imageName, boolean recipeAcquired) {
        super(itemName, buyPrice, sellPrice, imageName);
        this.energy = energy;
        this.recipeAcquired = false;
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
        this.recipeAcquired = recipeAcquired;
    }

    // Mengatur apakah resep sudah diperoleh
    public void setRecipeAcquired(boolean acquired) {
        this.recipeAcquired = acquired;
    }

    public boolean isRecipeAcquired() {
        return recipeAcquired;
    }

    // Mendapatkan jumlah energi yang dipulihkan
    @Override
    public int getEnergyRestore() {
        return energy;
    }

    // Mendapatkan daftar bahan-bahan makanan
    public List<Items> getIngredients() {
        return ingredients;
    }

    // Menambahkan bahan (optional jika butuh dinamis)
    public void addIngredient(Items item) {
        ingredients.add(item);
    }

}
