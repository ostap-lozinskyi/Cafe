package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ua.entity.Role;
import ua.entity.User;
import ua.model.filter.UserFilter;
import ua.model.request.RegistrationRequest;
import ua.model.view.MealView;

import java.io.IOException;
import java.util.List;

public interface UserService extends CrudService<User, Integer> {

    Page<User> findAllUsers(Pageable pageable, UserFilter filter);

    void saveUser(RegistrationRequest request);

    void setDefaultPhoto(Integer userId);

    void updateRole(Integer userId, Role role);

    User findUserByEmail(String email);

    void uploadPhotoToCloudinary(MultipartFile toUpload) throws IOException;

    boolean findMealInUserOrders(Page<MealView> mealViews);

    List<Integer> findUserMealsIds();

    List<Integer> findUserMealViews();

    User findCurrentUser();
}
