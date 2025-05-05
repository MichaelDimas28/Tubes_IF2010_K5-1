import java.util.List;

public class NPCHouse extends Location {
    private NPC Owner;

    public NPCHouse(List<String> availableAction, NPC Owner) {
        super(availableAction);
        this.Owner = Owner;
    }

    public NPC getOwner() {
        return Owner;
    }
}