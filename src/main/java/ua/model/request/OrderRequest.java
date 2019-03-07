package ua.model.request;

import ua.model.entity.Meal;
import ua.model.entity.OrderStatus;
import ua.model.entity.Place;
import ua.validation.flag.OrderFlag;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class OrderRequest {

    private String id;

    private String userId;

    private Place place;

    @NotNull(message = "This field cannot be blank", groups = {OrderFlag.class})
    private List<Meal> meals = new ArrayList<>();

    private OrderStatus status;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
