package com.example.coursemanagement.controller;
import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.entity.CourseImage;
import com.example.coursemanagement.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
@Controller
public class CourseController {
 private final CourseService service;
 public CourseController(CourseService service){this.service=service;}
 @GetMapping("/") public String home(){return "redirect:/courses";}
 @GetMapping("/courses") public String index(@RequestParam(required=false) String keyword,@RequestParam(required=false) Integer categoryId,@RequestParam(required=false) String status,@RequestParam(required=false) BigDecimal minFee,@RequestParam(required=false) BigDecimal maxFee,@RequestParam(defaultValue="") String order,Model model){
  model.addAttribute("courses", service.search(keyword,categoryId,status,minFee,maxFee,order)); model.addAttribute("categories",service.categories());
  model.addAttribute("keyword",keyword==null?"":keyword); model.addAttribute("categoryId",categoryId); model.addAttribute("status",status==null?"":status); model.addAttribute("minFee",minFee); model.addAttribute("maxFee",maxFee); model.addAttribute("order",order);
  if(minFee!=null&&maxFee!=null&&minFee.compareTo(maxFee)>0) model.addAttribute("filterError","Học phí tối thiểu không được lớn hơn học phí tối đa.");
  return "courses/index";
 }
 @GetMapping("/courses/create") public String create(Model model){model.addAttribute("course",new Course());model.addAttribute("categories",service.categories());return "courses/form";}
 @PostMapping("/courses/create") public String createPost(@Valid @ModelAttribute("course") Course course,BindingResult result,@RequestParam(required=false) Integer categoryId,Model model,RedirectAttributes ra){
  validateCategory(categoryId,result); if(result.hasErrors()){model.addAttribute("categories",service.categories());return "courses/form";}
  try{service.save(course,categoryId);ra.addFlashAttribute("success","Thêm khóa học thành công.");return "redirect:/courses";}catch(IllegalArgumentException ex){model.addAttribute("categories",service.categories());model.addAttribute("error",ex.getMessage());return "courses/form";}
 }
 @GetMapping("/courses/{id}/edit") public String edit(@PathVariable Integer id,Model model,RedirectAttributes ra){Course c=service.find(id).orElse(null);if(c==null){ra.addFlashAttribute("error","Khóa học không tồn tại.");return "redirect:/courses";}model.addAttribute("course",c);model.addAttribute("categories",service.categories());model.addAttribute("categoryId",c.getCourseCategory().getId());return "courses/form";}
 @PostMapping("/courses/{id}/edit") public String editPost(@PathVariable Integer id,@Valid @ModelAttribute("course") Course course,BindingResult result,@RequestParam(required=false) Integer categoryId,Model model,RedirectAttributes ra){
  course.setId(id);validateCategory(categoryId,result);if(result.hasErrors()){model.addAttribute("categories",service.categories());return "courses/form";}
  try{service.save(course,categoryId);ra.addFlashAttribute("success","Cập nhật khóa học thành công.");return "redirect:/courses";}catch(IllegalArgumentException ex){model.addAttribute("categories",service.categories());model.addAttribute("error",ex.getMessage());return "courses/form";}
 }
 private void validateCategory(Integer id,BindingResult result){if(id==null)result.rejectValue("courseCategory","category.required","Vui lòng chọn loại khóa học.");else if(service.categories().stream().noneMatch(c->c.getId().equals(id)))result.rejectValue("courseCategory","category.invalid","Loại khóa học không tồn tại.");}
 @GetMapping("/courses/{id}") public String detail(@PathVariable Integer id,Model model){Course c=service.find(id).orElseThrow(()->new IllegalArgumentException("Khóa học không tồn tại."));model.addAttribute("course",c);model.addAttribute("newImage",new CourseImage());return "courses/detail";}
 @PostMapping("/courses/{id}/images") public String addImage(@PathVariable Integer id,@RequestParam String imageUrl,@RequestParam(defaultValue="false") boolean thumbnail,RedirectAttributes ra){try{service.addImage(id,imageUrl,thumbnail);ra.addFlashAttribute("success","Đã thêm ảnh.");}catch(IllegalArgumentException ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/courses/"+id;}
 @PostMapping("/courses/{id}/images/{imageId}/thumbnail") public String thumbnail(@PathVariable Integer id,@PathVariable Integer imageId,RedirectAttributes ra){try{service.setThumbnail(id,imageId);ra.addFlashAttribute("success","Đã đổi ảnh đại diện.");}catch(IllegalArgumentException ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/courses/"+id;}
 @PostMapping("/courses/{id}/images/{imageId}/delete") public String deleteImage(@PathVariable Integer id,@PathVariable Integer imageId,RedirectAttributes ra){try{service.deleteImage(id,imageId);ra.addFlashAttribute("success","Đã xóa ảnh.");}catch(IllegalArgumentException ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/courses/"+id;}
 @PostMapping("/courses/{id}/delete") public String delete(@PathVariable Integer id,RedirectAttributes ra){try{service.delete(id);ra.addFlashAttribute("success","Đã xóa khóa học.");}catch(IllegalArgumentException ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/courses";}
}
