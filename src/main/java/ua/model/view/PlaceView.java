package ua.model.view;

public class PlaceView {

    private String id;

    private String name;

    private int countOfPeople;

    private boolean isFree;

    public PlaceView(String id, String name, int countOfPeople, boolean isFree) {
        this.id = id;
        this.name = name;
        this.countOfPeople = countOfPeople;
        this.isFree = isFree;
    }

    public String getPrint() {
        return "Table_" + name + "_" + "CountOfPeople_" + countOfPeople;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountOfPeople() {
        return countOfPeople;
    }

    public void setCountOfPeople(int countOfPeople) {
        this.countOfPeople = countOfPeople;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean isFree) {
        this.isFree = isFree;
    }

}
