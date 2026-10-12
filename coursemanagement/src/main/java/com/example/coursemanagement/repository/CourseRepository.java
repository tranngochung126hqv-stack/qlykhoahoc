package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.Course;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT c FROM Course c JOIN FETCH c.courseCategory LEFT JOIN FETCH c.images WHERE (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND (:categoryId IS NULL OR c.courseCategory.id=:categoryId) AND (:active IS NULL OR c.isActive=:active) AND (:minFee IS NULL OR c.tuitionFee>=:minFee) AND (:maxFee IS NULL OR c.tuitionFee<=:maxFee)")
    List<Course> search(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId,
            @Param("active") Boolean active, @Param("minFee") BigDecimal minFee, @Param("maxFee") BigDecimal maxFee,
            org.springframework.data.domain.Sort sort);

    @Query("SELECT DISTINCT c FROM Course c JOIN FETCH c.courseCategory LEFT JOIN FETCH c.images WHERE c.id=:id")
    Optional<Course> findDetailById(@Param("id") Integer id);

    boolean existsByNameIgnoreCaseAndCourseCategoryId(String name, Integer categoryId);

    boolean existsByNameIgnoreCaseAndCourseCategoryIdAndIdNot(String name, Integer categoryId, Integer id);

    boolean existsByCourseCategoryId(Integer categoryId);
}
