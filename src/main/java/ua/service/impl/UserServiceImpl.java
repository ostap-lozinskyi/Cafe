package ua.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.model.entity.Role;
import ua.model.entity.User;
import ua.exception.UserNotFoundException;
import ua.model.filter.UserFilter;
import ua.model.request.RegistrationRequest;
import ua.dto.MealDTO;
import ua.repository.UserRepository;
import ua.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final HttpServletRequest httpServletRequest;

    @Value("${cloudinary.url}")
    Cloudinary cloudinary = new Cloudinary();

    @Value("${user.default.photoUrl}")
    private String defaultPhotoUrl;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder,
                           HttpServletRequest httpServletRequest) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.httpServletRequest = httpServletRequest;
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
        LOG.info("In 'saveUser' method");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        String photoFromRequest = request.getPhotoUrl();
        String newPhotoUrl = Objects.nonNull(photoFromRequest) ? photoFromRequest : defaultPhotoUrl;
        user.setPhotoUrl(newPhotoUrl);
        userRepository.save(user);
        LOG.info("Exit from 'saveUser' method. PhotoUrl = {}", newPhotoUrl);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void setDefaultPhoto(String userId) {
        LOG.info("In 'setDefaultPhoto' method. UserId = {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPhotoUrl(defaultPhotoUrl);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException(String.format("User with id [%s] nod found", userId));
        }
        LOG.info("Exit from 'setDefaultPhoto' method");
    }

    @Override
    public void updateRole(String userId, Role role) {
        LOG.info("In 'updateRole' method. UserId = {}, Role = {}", userId, role);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRole(role);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException(String.format("User with id [%s] nod found", userId));
        }
        LOG.info("Exit from 'updateRole' method");
    }

    @Override
    public User findUserByEmail(String email) {
        LOG.info("In 'findUserByEmail' method. Email = {}", email);
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void uploadPhotoToCloudinary(MultipartFile toUpload) throws IOException {
        LOG.info("In 'uploadPhotoToCloudinary' method");
        User user = findCurrentUser();

        @SuppressWarnings("rawtypes")
        Map uploadResult = cloudinary.uploader().upload(toUpload.getBytes(), ObjectUtils.asMap("use_filename",
                "true", "unique_filename", "false", "transformation", "w_150,h_150,c_fill,g_face,r_max"));
        String cloudinaryUrl = (String) uploadResult.get("url");
        int version = cloudinaryUrl.equals(user.getPhotoUrl()) ? user.getVersion() + 1 : 0;
        user.setVersion(version);
        user.setPhotoUrl(cloudinaryUrl);
        userRepository.save(user);
        LOG.info("Exit from 'updateRole' method");
    }

    /**
     * Searching Meal in User orders
     */
    @Override
    public boolean findMealInUserOrders(Page<MealDTO> mealDTOs) {
        LOG.info("In 'findMealInUserOrders' method");
        List<String> userMealsIds = findUserMealsIds();
        List<String> mealDTOsIds = new ArrayList<>();
        for (MealDTO mealDTO : mealDTOs) {
            mealDTOsIds.add(mealDTO.getId());
        }
        for (String userMealId : userMealsIds) {
            if (mealDTOsIds.contains(userMealId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> findUserMealsIds() {
        LOG.info("In 'findUserMealsIds' method");
        User user = findCurrentUser();
        return userRepository.findUserMealsIds(user.getId());
    }

    @Override
    public List<Integer> findUserMealDTOs() {
        LOG.info("In 'findUserMealDTOs' method");
        User user = findCurrentUser();
        return userRepository.findUserMealDTOs(user.getId());
    }

    @Override
    public User findCurrentUser() {
        LOG.info("In 'findCurrentUser' method");
        Principal principal = httpServletRequest.getUserPrincipal();
        if (Objects.nonNull(principal)) {
            return userRepository.findUserByEmail(principal.getName());
        }
        return null;
    }
}