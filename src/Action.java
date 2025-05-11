public interface Action{
    public void tile();
    public void recoverLand();
    public void plant(Seeds seed);
    public void water();
    public void harvest();
    public void eat(Food food);
    public void sleep();
    public void cook(Food food);
    public void fish();
    public void propose(NPC npc);
    public void marry();
    public void watch();
    public void visit();
    public void chat(NPC npc);
    public void gift();
    public void move(Coordinate coordinate);
    public void openInventory();
    public void showTime();
    public void showLocation();
    public void sell(Items item);
}