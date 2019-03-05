package ua.dto;

import java.util.List;

import ua.model.entity.Comment;

public class IngredientDTO {
	
	private String id;

	private String name;
	
	private List<Comment> comments;
	
	public IngredientDTO(String id, String name) {
		this.id = id;
		this.name = name;
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

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
