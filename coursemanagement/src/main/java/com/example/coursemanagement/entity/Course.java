package com.example.coursemanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Courses", uniqueConstraints = @UniqueConstraint(name = "UQ_Courses_Name_Category", columnNames = {
        "Name", "CourseCategoryId" }))
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Tên khóa học không được để trống")
    @Size(max = 200, message = "Tên tối đa 200 ký tự")
    @Column(nullable = false, length = 200)
    private String name;
    @NotNull(message = "Vui lòng nhập học phí")
    @DecimalMin(value = "0.0", inclusive = false, message = "Học phí phải lớn hơn 0")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal tuitionFee;
    @NotNull(message = "Vui lòng nhập thời lượng")
    @Min(value = 1, message = "Thời lượng phải lớn hơn 0")
    @Column(nullable = false)
    private Integer duration;
    @NotNull(message = "Vui lòng chọn trạng thái")
    @Column(nullable = false)
    private Boolean isActive = true;
    @Column(length = 1000)
    private String description;
    @NotNull(message = "Vui lòng chọn loại khóa học")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CourseCategoryId", nullable = false)
    private CourseCategory courseCategory;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseImage> images = new ArrayList<>();

    @Transient
    public String getDurationType() {
        return duration == null ? "" : (duration <= 30 ? "Ngắn hạn" : "Dài hạn");
    }

    @Transient
    public CourseImage getThumbnail() {
        return images.stream().filter(i -> Boolean.TRUE.equals(i.getIsThumbnail())).findFirst()
                .orElse(images.isEmpty() ? null : images.get(0));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTuitionFee() {
        return tuitionFee;
    }

    public void setTuitionFee(BigDecimal tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CourseCategory getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(CourseCategory courseCategory) {
        this.courseCategory = courseCategory;
    }

    public List<CourseImage> getImages() {
        return images;
    }

    public void setImages(List<CourseImage> images) {
        this.images = images;
    }
}
