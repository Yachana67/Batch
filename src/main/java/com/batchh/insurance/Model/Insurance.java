package com.batchh.insurance.Model;

public class Insurance {

	
	private String Category;
	private String NAME;
	private String Email;
	@Override
	public String toString() {
		return "Insurance [Category=" + Category + ", NAME=" + NAME + ", Email=" + Email + "]";
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
}
