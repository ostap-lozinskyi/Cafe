package ua.model.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PlaceFilter {

    private static final Pattern INT_PATTERN = Pattern.compile("^[0-9]{1,10}$");

    private String name = "";

    private List<String> countOfPeople = new ArrayList<>();

    private String isFree = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (INT_PATTERN.matcher(name).matches()) {
            this.name = name;
        }
    }

    public List<String> getCountOfPeople() {
        return countOfPeople;
    }

    public void setCountOfPeople(List<String> countOfPeople) {
        this.countOfPeople = countOfPeople;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

}
