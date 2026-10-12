package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.CourseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseImageRepository extends JpaRepository<CourseImage, Integer> {
    List<CourseImage> findByCourseId(Integer courseId);

    boolean existsByCourseIdAndIsThumbnailTrue(Integer courseId);
}
