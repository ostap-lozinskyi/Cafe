package ua.model.view;

import java.util.ArrayList;
import java.util.List;

public class OrderView {
	
	private String id;

	private Integer place;
	
	private String status;
	
	private List<MealView> mealViews = new ArrayList<>();
	
	public OrderView(String id, Integer place, String status) {
		this.id = id;
		this.place = place;
		this.status = status;
	}
	
	public List<MealView> getMealViews() {
		return mealViews;
	}

	public void setMealViews(List<MealView> mealViews) {
		this.mealViews = mealViews;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPlace() {
		return place;
	}

	public void setPlace(Integer place) {
		this.place = place;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
