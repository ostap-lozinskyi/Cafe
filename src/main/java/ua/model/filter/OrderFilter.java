package ua.model.filter;

import ua.model.entity.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderFilter {

	private List<String> mealName = new ArrayList<>();
	
	private List<String> placeName = new ArrayList<>();
	
	private List<OrderStatus> status = new ArrayList<>();

	public List<String> getMealName() {
		return mealName;
	}

	public void setMealName(List<String> mealName) {
		this.mealName = mealName;
	}

	public List<String> getPlaceName() {
		return placeName;
	}

	public void setPlaceName(List<String> placeName) {
		this.placeName = placeName;
	}

	public List<OrderStatus> getStatus() {
		return status;
	}

	public void setStatus(List<OrderStatus> status) {
		this.status = status;
	}

}
