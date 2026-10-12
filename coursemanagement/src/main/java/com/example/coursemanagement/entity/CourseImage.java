package com.example.coursemanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "CourseImages")
public class CourseImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Đường dẫn ảnh không được để trống")
    @Size(max = 1000, message = "Đường dẫn ảnh quá dài")
    @Column(nullable = false, length = 1000)
    private String imageUrl;
    @Column(nullable = false)
    private Boolean isThumbnail = false;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CourseId", nullable = false)
    private Course course;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsThumbnail() {
        return isThumbnail;
    }

    public void setIsThumbnail(Boolean isThumbnail) {
        this.isThumbnail = isThumbnail;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
