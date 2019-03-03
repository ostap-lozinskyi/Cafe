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
import ua.exception.CafeException;
import ua.model.filter.MealFilter;
import ua.model.request.MealRequest;
import ua.model.view.ComponentView;
import ua.model.view.MealIndexView;
import ua.model.view.MealView;
import ua.repository.ComponentRepository;
import ua.repository.CuisineRepository;
import ua.repository.MealRepository;
import ua.repository.MealViewRepository;
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

    @Value("${cloudinary.url}")
    Cloudinary cloudinary = new Cloudinary();

    @Autowired
    public MealServiceImpl(MealRepository repository, MealViewRepository mealViewrepository,
                           CuisineRepository cuisineRepository, ComponentRepository componentRepository) {
        this.mealRepository = repository;
        this.mealViewRepository = mealViewrepository;
        this.cuisineRepository = cuisineRepository;
        this.componentRepository = componentRepository;
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
    public void saveMeal(MealRequest mealRequest) {
        LOG.info("In 'saveMeal' method");
        Meal newMeal = Meal.of(mealRequest);
        mealRepository.save(newMeal);
        LOG.info("Exit from 'saveMeal' method");
    }

    @Override
    public MealRequest findOneRequest(String id) {
        LOG.info("In 'findOneCommentRequest' method. Id = {}", id);
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Meal with id [%s} not found", id)));
        MealRequest mealRequest = MealRequest.of(meal);
        LOG.info("Exit from 'findOneCommentRequest' method. Request = {}", mealRequest);
        return mealRequest;
    }

    @Override
    public void deleteMeal(String id) {
        LOG.info("In 'deleteMeal method'. Id = {}", id);
        mealRepository.deleteById(id);
        LOG.info("Exit from 'deleteMeal' method");
    }

    @Override
    public void updateMealRate(String id, Integer newRate) {
        LOG.info("In 'updateMealRate method'. Id = {}, NewRate = {}", id, newRate);
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Meal with id [%s} not found", id)));
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
    public void updateComments(String id, Comment comment) {
        LOG.info("In 'updateComments method'. Id = {}, Comment = {}", id, comment);
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Meal with id [%s} not found", id)));
        List<Comment> comments = meal.getComments();
        comments.add(comment);
        meal.setComments(comments);
        mealRepository.save(meal);
        LOG.info("Exit from 'updateComments' method");
    }

    @Override
    public MealView findMealViewById(String id) {
        return mealRepository.findMealViewById(id);
    }

    @Override
    public Meal findMealById(String id) {
        return mealRepository.findById(id)
                .orElseThrow(() -> new CafeException(String.format("Meal with id [%s} not found", id)));
    }

    public MealRequest uploadPhotoToCloudinary(MealRequest mealRequest, MultipartFile toUpload) throws IOException {
        LOG.info("In 'uploadPhotoToCloudinary' method. FilePath = {}", toUpload);
        @SuppressWarnings("rawtypes")
        Map uploadResult = cloudinary.uploader().upload(toUpload.getBytes(), ObjectUtils.asMap("use_filename",
                "true", "unique_filename", "false"));
        String cloudinaryUrl = (String) uploadResult.get("url");
        int version = cloudinaryUrl.equals(mealRequest.getPhotoUrl()) ? mealRequest.getVersion() + 1 : 0;
        mealRequest.setVersion(version);
        mealRequest.setPhotoUrl(cloudinaryUrl);
        LOG.info("Exit from uploadPhotoToCloudinary method. MealRequest = {}", mealRequest);
        return mealRequest;
    }

    @Override
    public List<Comment> findCommentList(String id) {
        return mealRepository.findCommentList(id);
    }

}
