package com.example.demo.dto;

public class RatingCountResponseDto {

	private final int rating;
	private final long count;

	public RatingCountResponseDto(int rating, long count) {
		this.rating = rating;
		this.count = count;
	}

	public int getRating() {
		return rating;
	}

	public long getCount() {
		return count;
	}

}
