package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ua.dto.MealDTO;
import ua.model.entity.Role;
import ua.model.entity.User;
import ua.model.filter.UserFilter;
import ua.model.request.RegistrationRequest;

import java.io.IOException;
import java.util.List;

public interface UserService {

    Page<User> findAllUsers(Pageable pageable, UserFilter filter);

    void saveUser(RegistrationRequest request);

    void deleteById(String id);

    void setDefaultPhoto(String userId);

    void updateRole(String userId, Role role);

    User findUserByEmail(String email);

    void uploadPhotoToCloudinary(MultipartFile toUpload) throws IOException;

    boolean findMealInUserOrders(Page<MealDTO> mealDTOs);

    List<String> findUserMealsIds();

    List<Integer> findUserMealDTOs();

    User findCurrentUser();

}
