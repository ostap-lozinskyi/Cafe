package ua.dto;

import java.math.BigDecimal;

public class ComponentDTO {
	
	private String id;
	
	private BigDecimal amount;

	private String ingredient;
	
	private String ms;
	
	public ComponentDTO(String id, BigDecimal amount, String ingredient, String ms) {
		super();
		this.id = id;
		this.amount = amount;
		this.ingredient = ingredient;
		this.ms = ms;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getMs() {
		return ms;
	}

	public void setMs(String ms) {
		this.ms = ms;
	}

	@Override
	public String toString() {
		return "id " + id + " " + ingredient + " " + amount + " " + ms;
	}

}
