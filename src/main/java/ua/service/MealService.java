package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ua.entity.Comment;
import ua.entity.Meal;
import ua.model.filter.MealFilter;
import ua.model.request.MealRequest;
import ua.model.view.ComponentView;
import ua.model.view.MealIndexView;
import ua.model.view.MealView;

import java.io.IOException;
import java.util.List;

public interface MealService {

    List<String> findAllCuisinesNames();

    List<ComponentView> findAllComponentsView();

    Page<MealIndexView> findAllMealIndexView(MealFilter filter, Pageable pageable);

    List<MealIndexView> find5MealIndexViewsByRate();

    Page<MealView> findAllMealView(MealFilter filter, Pageable pageable);

    void saveMeal(MealRequest request);

    MealRequest findOneRequest(String id);

    void deleteMeal(String id);

    void updateMealRate(String id, Integer newRate);

    void updateComments(String id, Comment comment);

    MealView findMealViewById(String id);

    Meal findMealById(String id);

    MealRequest uploadPhotoToCloudinary(MealRequest request, MultipartFile toUpload) throws IOException;

    List<Comment> findCommentList(String id);

}