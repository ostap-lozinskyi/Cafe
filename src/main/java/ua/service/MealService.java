package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ua.model.entity.Comment;
import ua.model.entity.Meal;
import ua.model.filter.MealFilter;
import ua.model.request.MealRequest;
import ua.dto.ComponentDTO;
import ua.dto.MealIndexDTO;
import ua.dto.MealDTO;

import java.io.IOException;
import java.util.List;

public interface MealService {

    List<String> findAllCuisinesNames();

    List<ComponentDTO> findAllComponentsDTOs();

    Page<MealIndexDTO> findAllMealIndexDTOs(MealFilter filter, Pageable pageable);

    List<MealIndexDTO> find5MealIndexDTOsByRate();

    Page<MealDTO> findAllMealDTOs(MealFilter filter, Pageable pageable);

    void saveMeal(MealRequest request);

    MealRequest findOneRequest(String id);

    void deleteMeal(String id);

    void updateMealRate(String id, Integer newRate);

    void updateComments(String id, Comment comment);

    MealDTO findMealDTO(String id);

    Meal findMealById(String id);

    MealRequest uploadPhotoToCloudinary(MealRequest request, MultipartFile toUpload) throws IOException;

    List<Comment> findCommentList(String id);

}