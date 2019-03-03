package ua.model.filter;

import java.util.ArrayList;
import java.util.List;

public class OrderFilter {

	private List<String> mealName = new ArrayList<>();
	
	private List<String> placeName = new ArrayList<>();
	
	private List<String> status = new ArrayList<>();

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

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

}
