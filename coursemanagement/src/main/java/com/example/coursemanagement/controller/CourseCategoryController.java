package com.example.coursemanagement.controller;

import com.example.coursemanagement.entity.CourseCategory;
import com.example.coursemanagement.repository.CourseCategoryRepository;
import com.example.coursemanagement.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CourseCategoryController {
    private final CourseCategoryRepository categories;
    private final CourseRepository courses;

    public CourseCategoryController(CourseCategoryRepository categories, CourseRepository courses) {
        this.categories = categories;
        this.courses = courses;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("categories", categories.findAll());
        model.addAttribute("category", new CourseCategory());
        return "categories/index";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("category") CourseCategory category, BindingResult result, Model model,
            RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categories.findAll());
            return "categories/index";
        }
        categories.save(category);
        ra.addFlashAttribute("success", "Đã thêm loại khóa học.");
        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        CourseCategory c = categories.findById(id).orElse(null);
        if (c == null) {
            ra.addFlashAttribute("error", "Loại khóa học không tồn tại.");
        } else if (courses.existsByCourseCategoryId(id)) {
            ra.addFlashAttribute("error", "Không thể xóa loại khóa học đang có khóa học thuộc loại này.");
        } else {
            categories.delete(c);
            ra.addFlashAttribute("success", "Đã xóa loại khóa học.");
        }
        return "redirect:/categories";
    }
}
