package com.example.coursemanagement.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
@Entity @Table(name="CourseCategories")
public class CourseCategory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @NotBlank(message="Tên loại khóa học không được để trống") @Column(nullable=false,length=100) private String name;
 @Column(length=500) private String description;
 @OneToMany(mappedBy="courseCategory") private List<Course> courses=new ArrayList<>();
 public Integer getId(){return id;} public void setId(Integer id){this.id=id;}
 public String getName(){return name;} public void setName(String name){this.name=name;}
 public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
 public List<Course> getCourses(){return courses;} public void setCourses(List<Course> courses){this.courses=courses;}
}
