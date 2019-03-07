package ua.dto;

import ua.model.entity.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    private String id;

    private String place;

    private OrderStatus status;

    private List<MealDTO> mealDTOS = new ArrayList<>();

    public OrderDTO(String id, String place, OrderStatus status) {
        this.id = id;
        this.place = place;
        this.status = status;
    }

    public List<MealDTO> getMealDTOS() {
        return mealDTOS;
    }

    public void setMealDTOS(List<MealDTO> mealDTOS) {
        this.mealDTOS = mealDTOS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
