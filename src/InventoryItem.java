
public class InventoryItem{
    private Items item;
    private int quantity;

    public InventoryItem(Items item, int quantity){
        this.item = item;
        this.quantity = quantity;
    }

    public Items getItem(){
        return this.item;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
}
