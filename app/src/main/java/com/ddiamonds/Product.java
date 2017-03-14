package com.ddiamonds;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String description;
	private String cat_id;
	private String brand_id;
	private List<String> Images;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	public String getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(String brand_id) {
		this.brand_id = brand_id;
	}

	public List<String> getImages() {
		return Images;
	}

	public void setImages(List<String> images) {
		Images = images;
	}

	public Product(String id, String title, String description, String cat_id,
			String brand_id, List<String> images) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.cat_id = cat_id;
		this.brand_id = brand_id;
		Images = images;
	}
	
	
	

}
