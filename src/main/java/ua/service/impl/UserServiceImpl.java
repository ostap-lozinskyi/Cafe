package ua.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.entity.Role;
import ua.entity.User;
import ua.model.filter.UserFilter;
import ua.model.request.RegistrationRequest;
import ua.model.view.MealView;
import ua.repository.UserRepository;
import ua.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
public class UserServiceImpl extends CrudServiceImpl<User, Integer> implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Value("${cloudinary.url}")
    Cloudinary cloudinary = new Cloudinary();

    @Value("${user.default.photoUrl}")
    private String defaultPhotoUrl;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable, UserFilter filter) {
        return userRepository.findAll(filter(filter), pageable);
    }

    private Specification<User> filter(UserFilter filter) {
        return (root, query, cb) -> {
            if (filter.getSearch().isEmpty()) return null;
            return cb.like(root.get("email"), filter.getSearch() + "%");
        };
    }

    /**
     * Saving new User
     */
    @Override
    public void saveUser(RegistrationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        String photoUrl = request.getPhotoUrl();
        if (Objects.nonNull(photoUrl)) {
            user.setPhotoUrl(photoUrl);
        } else {
            user.setPhotoUrl(defaultPhotoUrl);
        }
        userRepository.save(user);
    }

    @Override
    public void setDefaultPhoto(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPhotoUrl(defaultPhotoUrl);
            userRepository.save(user);
        }
    }

    @Override
    public void updateRole(Integer userId, Role role) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRole(role);
            userRepository.save(user);
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void uploadPhotoToCloudinary(MultipartFile toUpload, Principal principal) throws IOException {
        String email = principal.getName();
        User user = findUserByEmail(email);

        @SuppressWarnings("rawtypes")
        Map uploadResult = cloudinary.uploader().upload(toUpload.getBytes(),
                ObjectUtils.asMap("use_filename", "true", "unique_filename", "false", "transformation",
                        "w_150,h_150,c_fill,g_face,r_max"));
        String cloudinaryUrl = (String) uploadResult.get("url");
        String oldPhotoUrl = user.getPhotoUrl();
        if (cloudinaryUrl.equals(oldPhotoUrl)) {
            user.setVersion(user.getVersion() + 1);
        } else {
            user.setVersion(0);
        }
        user.setPhotoUrl(cloudinaryUrl);
        userRepository.save(user);
    }

    /**
     * Searching Meal in User orders
     */
    @Override
    public boolean findMealInUserOrders(Page<MealView> mealViews, Principal principal) {
        List<Integer> userMealsIds = findUserMealsIds(principal);
        List<Integer> mealViewIds = new ArrayList<>();
        for (MealView mealView : mealViews) {
            mealViewIds.add(mealView.getId());
        }
        for (Integer userMealId : userMealsIds) {
            if (mealViewIds.contains(userMealId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Integer> findUserMealsIds(Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName());
        return userRepository.findUserMealsIds(user.getId());
    }

    @Override
    public List<Integer> findUserMealViews(Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName());
        return userRepository.findUserMealViews(user.getId());
    }

    @Override
    public User findCurrentUser() {
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
        return userRepository.findUserByEmail(user.getUsername());
    }
}
