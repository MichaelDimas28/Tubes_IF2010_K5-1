import java.util.List;

public class Location {
    private List<String> availableAction;

    public Location(List<String> availableAction) {
        this.availableAction = availableAction;
    }

    public List<String> getAvailableAction() {
        return availableAction;
    }
}