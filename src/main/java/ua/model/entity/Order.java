package ua.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "_order")
public class Order extends AbstractEntity {

    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    @ManyToMany
    private List<Meal> meals = new ArrayList<>();

    private String status;

    public Order() {
    }

    public Order(List<Meal> meals, Place place) {
        this.meals = meals;
        this.place = place;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
