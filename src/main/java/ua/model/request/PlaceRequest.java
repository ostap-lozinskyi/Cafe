package ua.model.request;

import ua.validation.annotation.UniquePlace;
import ua.validation.flag.PlaceFlag;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class PlaceRequest {

    private String id;

    @UniquePlace(message = "Such a place already exists", groups = PlaceFlag.class)
    @NotBlank(message = "This field cannot be blank", groups = {PlaceFlag.class})
    private String name;

    @NotBlank(message = "This field cannot be blank", groups = {PlaceFlag.class})
    @Pattern(regexp = "^[1-9][0-9]*| *$",
            message = "The Count Of People should be a number and can not begin with a zero symbol", groups = {
            PlaceFlag.class})
    private String countOfPeople;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountOfPeople() {
        return countOfPeople;
    }

    public void setCountOfPeople(String countOfPeople) {
        this.countOfPeople = countOfPeople;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
