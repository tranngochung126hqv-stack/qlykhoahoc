package com.example.coursemanagement.service;
import com.example.coursemanagement.entity.*;
import com.example.coursemanagement.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Service
public class CourseService {
 private final CourseRepository courses; private final CourseCategoryRepository categories; private final CourseImageRepository images;
 public CourseService(CourseRepository courses,CourseCategoryRepository categories,CourseImageRepository images){this.courses=courses;this.categories=categories;this.images=images;}
 public List<Course> search(String keyword,Integer categoryId,String status,BigDecimal minFee,BigDecimal maxFee,String order){
  Boolean active=null; if("true".equals(status)) active=true; else if("false".equals(status)) active=false;
  Sort sort=switch(order==null?"":order){case "feeAsc"->Sort.by("tuitionFee").ascending();case "feeDesc"->Sort.by("tuitionFee").descending();case "durationAsc"->Sort.by("duration").ascending();default->Sort.by("id").ascending();};
  return courses.search(keyword==null||keyword.isBlank()?null:keyword.trim(),categoryId,active,minFee,maxFee,sort);
 }
 public List<CourseCategory> categories(){return categories.findAll(Sort.by("name").ascending());}
 public Optional<Course> find(Integer id){return courses.findDetailById(id);}
 public boolean duplicate(String name,Integer categoryId,Integer id){return id==null?courses.existsByNameIgnoreCaseAndCourseCategoryId(name.trim(),categoryId):courses.existsByNameIgnoreCaseAndCourseCategoryIdAndIdNot(name.trim(),categoryId,id);}
 @Transactional public Course save(Course form,Integer categoryId){
  CourseCategory category=categories.findById(categoryId).orElseThrow(()->new IllegalArgumentException("Loại khóa học không tồn tại."));
  if(duplicate(form.getName(),categoryId,form.getId())) throw new IllegalArgumentException("Tên khóa học đã tồn tại trong loại khóa học này.");
  Course course=form.getId()==null?new Course():courses.findById(form.getId()).orElseThrow(()->new IllegalArgumentException("Khóa học không tồn tại."));
  course.setName(form.getName().trim()); course.setTuitionFee(form.getTuitionFee()); course.setDuration(form.getDuration()); course.setIsActive(form.getIsActive()); course.setDescription(form.getDescription()); course.setCourseCategory(category);
  return courses.save(course);
 }
 @Transactional public void delete(Integer id){Course c=courses.findById(id).orElseThrow(()->new IllegalArgumentException("Khóa học không tồn tại.")); courses.delete(c);}
 @Transactional public CourseImage addImage(Integer courseId,String url,boolean thumbnail){
  Course c=courses.findById(courseId).orElseThrow(()->new IllegalArgumentException("Khóa học không tồn tại."));
  if(url==null||url.isBlank()) throw new IllegalArgumentException("Đường dẫn ảnh không được để trống.");
  CourseImage image=new CourseImage(); image.setImageUrl(url.trim()); image.setCourse(c); image.setIsThumbnail(false); image=images.save(image);
  if(thumbnail || !images.existsByCourseIdAndIsThumbnailTrue(courseId)) setThumbnail(courseId,image.getId());
  return image;
 }
 @Transactional public void setThumbnail(Integer courseId,Integer imageId){
  Course c=courses.findById(courseId).orElseThrow(()->new IllegalArgumentException("Khóa học không tồn tại."));
  CourseImage chosen=images.findById(imageId).orElseThrow(()->new IllegalArgumentException("Ảnh khóa học không tồn tại."));
  if(!chosen.getCourse().getId().equals(courseId)) throw new IllegalArgumentException("Ảnh không thuộc khóa học này.");
  for(CourseImage img:images.findByCourseId(courseId)) img.setIsThumbnail(false);
  chosen.setIsThumbnail(true); images.saveAll(images.findByCourseId(courseId)); images.save(chosen);
 }
 @Transactional public void deleteImage(Integer courseId,Integer imageId){
  CourseImage image=images.findById(imageId).orElseThrow(()->new IllegalArgumentException("Ảnh khóa học không tồn tại."));
  if(!image.getCourse().getId().equals(courseId)) throw new IllegalArgumentException("Ảnh không thuộc khóa học này.");
  boolean wasThumbnail=Boolean.TRUE.equals(image.getIsThumbnail()); images.delete(image);
  if(wasThumbnail){List<CourseImage> remaining=images.findByCourseId(courseId); if(!remaining.isEmpty()) setThumbnail(courseId,remaining.get(0).getId());}
 }
}
