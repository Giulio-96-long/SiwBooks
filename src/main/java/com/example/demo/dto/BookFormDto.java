package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Author;

public class BookFormDto {	
	
	private Long id;
	
    private String title;

    private Integer publicationYear;
    
    private List<MultipartFile> images = new ArrayList<>();
    
    private List<Long> authorIds = new ArrayList<>();    
    
    private List<Author> authors = new ArrayList<>();  
	
	
	private Long existingCoverImageId;

	private Integer newCoverIndex;
   
    public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

	public List<Long> getAuthorIds() {
		return authorIds;
	}

	public void setAuthorIds(List<Long> authorIds) {
		this.authorIds = authorIds;
	}

	public Long getExistingCoverImageId() {
		return existingCoverImageId;
	}

	public void setExistingCoverImageId(Long existingCoverImageId) {
		this.existingCoverImageId = existingCoverImageId;
	}

	public Integer getNewCoverIndex() {
		return newCoverIndex;
	}

	public void setNewCoverIndex(Integer newCoverIndex) {
		this.newCoverIndex = newCoverIndex;
	}   
    
}

