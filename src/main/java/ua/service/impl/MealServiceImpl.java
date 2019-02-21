package ua.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.entity.Comment;
import ua.entity.Meal;
import ua.model.filter.MealFilter;
import ua.model.request.MealRequest;
import ua.model.view.ComponentView;
import ua.model.view.MealIndexView;
import ua.model.view.MealView;
import ua.repository.*;
import ua.service.MealService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class MealServiceImpl implements MealService {

    private static final Logger LOG = LoggerFactory.getLogger(MealServiceImpl.class);

    private final MealRepository mealRepository;

    private final MealViewRepository mealViewRepository;

    private final CuisineRepository cuisineRepository;

    private final ComponentRepository componentRepository;

    private final UserRepository userRepository;

    @Value("${cloudinary.url}")
    Cloudinary cloudinary = new Cloudinary();

    @Autowired
    public MealServiceImpl(MealRepository repository, MealViewRepository mealViewrepository,
                           CuisineRepository cuisineRepository, ComponentRepository componentRepository,
                           UserRepository userRepository) {
        this.mealRepository = repository;
        this.mealViewRepository = mealViewrepository;
        this.cuisineRepository = cuisineRepository;
        this.componentRepository = componentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<String> findAllCuisinesNames() {
        return cuisineRepository.findAllCuisinesNames();
    }

    @Override
    public List<ComponentView> findAllComponentsView() {
        return componentRepository.findAllComponentsView();
    }

    @Override
    public Page<MealIndexView> findAllMealIndexView(MealFilter filter, Pageable pageable) {
        return mealViewRepository.findAllMealIndexView(filter, pageable);
    }

    @Override
    public List<MealIndexView> find5MealIndexViewsByRate() {
        return mealRepository.find5MealIndexViewsByRate();
    }

    @Override
    public Page<MealView> findAllMealView(MealFilter filter, Pageable pageable) {
        return mealViewRepository.findAllMealView(filter, pageable);
    }

    @Override
    public void saveMeal(MealRequest request) {
        LOG.info("In 'saveMeal' method");
        Meal meal = new Meal();
        meal.setCuisine(request.getCuisine());
        meal.setFullDescription(request.getFullDescription());
        meal.setShortDescription(request.getShortDescription());
        meal.setId(request.getId());
        meal.setName(request.getName());
        meal.setPrice(new BigDecimal(request.getPrice()));
        meal.setWeight(Integer.valueOf(request.getWeight()));
        meal.setComponents(request.getComponents());
        meal.setPhotoUrl(request.getPhotoUrl());
        meal.setVersion(request.getVersion());
        mealRepository.save(meal);
        LOG.info("Exit from 'saveMeal' method");
    }

    @Override
    public MealRequest findOneRequest(Integer id) {
        LOG.info("In 'findOneRequest' method. Id = {}", id);
        Meal meal = mealRepository.findOneRequest(id);
        MealRequest request = new MealRequest();
        request.setCuisine(meal.getCuisine());
        request.setFullDescription(meal.getFullDescription());
        request.setShortDescription(meal.getShortDescription());
        request.setId(meal.getId());
        request.setName(meal.getName());
        request.setPrice(meal.getPrice().toString());
        request.setWeight(String.valueOf(meal.getWeight()));
        request.setComponents(meal.getComponents());
        request.setPhotoUrl(meal.getPhotoUrl());
        request.setVersion(meal.getVersion());
        LOG.info("Exit from 'findOneRequest' method. Request = {}", request);
        return request;
    }

    @Override
    public void deleteMeal(Integer id) {
        LOG.info("In 'deleteMeal method'. Id = {}", id);
        mealRepository.deleteById(id);
        LOG.info("Exit from 'deleteMeal' method");
    }

    @Override
    public void updateMealRate(Integer id, Integer newRate) {
        LOG.info("In 'updateMealRate method'. Id = {}, NewRate = {}", id, newRate);
        Meal meal = mealRepository.findMealById(id);
        meal.setVotesCount(meal.getVotesCount() + 1);
        meal.setVotesAmount(meal.getVotesAmount() + newRate);
        mealRepository.save(meal);
        BigDecimal votesAmount = new BigDecimal(meal.getVotesAmount());
        BigDecimal votesCount = new BigDecimal(meal.getVotesCount());
        BigDecimal rateToSave = votesAmount.divide(votesCount, 2, BigDecimal.ROUND_HALF_UP);
        meal.setRate(rateToSave);
        mealRepository.save(meal);
        LOG.info("Exit from 'updateMealRate' method. RateToSave = {}", rateToSave);
    }

    @Override
    public void updateComments(Integer id, Comment comment) {
        LOG.info("In 'updateComments method'. Id = {}, Comment = {}", id, comment);
        Meal meal = mealRepository.findMealById(id);
        List<Comment> comments = meal.getComments();
        comments.add(comment);
        meal.setComments(comments);
        mealRepository.save(meal);
        LOG.info("Exit from 'updateComments' method");
    }

    @Override
    public MealView findMealViewById(Integer id) {
        return mealRepository.findMealViewById(id);
    }

    @Override
    public List<Integer> findMealIdByUserId(Integer id) {
        return userRepository.findMealIdByUserId(id);
    }

    public MealRequest uploadPhotoToCloudinary(MealRequest mealRequest, MultipartFile toUpload) throws IOException {
        LOG.info("In 'uploadPhotoToCloudinary' method. FilePath = {}", toUpload);
        @SuppressWarnings("rawtypes")
        Map uploadResult = cloudinary.uploader().upload(toUpload.getBytes(),
                ObjectUtils.asMap("use_filename", "true", "unique_filename", "false"));
        String cloudinaryUrl = (String) uploadResult.get("url");
        String oldPhotoUrl = mealRequest.getPhotoUrl();
        if (cloudinaryUrl.equals(oldPhotoUrl)) {
            mealRequest.setVersion(mealRequest.getVersion() + 1);
        } else {
            mealRequest.setVersion(0);
        }
        mealRequest.setPhotoUrl(cloudinaryUrl);
        LOG.info("Exit from uploadPhotoToCloudinary method. MealRequest = {}", mealRequest);
        return mealRequest;
    }

    @Override
    public List<Comment> findCommentList(Integer id) {
        return mealRepository.findCommentList(id);
    }

}
