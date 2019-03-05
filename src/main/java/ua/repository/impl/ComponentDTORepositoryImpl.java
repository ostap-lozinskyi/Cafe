package ua.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import ua.dto.ComponentDTO;
import ua.model.entity.Component;
import ua.model.entity.Component_;
import ua.model.entity.Ingredient;
import ua.model.entity.Ms;
import ua.model.filter.ComponentFilter;
import ua.repository.ComponentDTORepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

@Repository
public class ComponentDTORepositoryImpl implements ComponentDTORepository {

    private static final String AMOUNT = "amount";
    private static final String NAME = "name";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ComponentDTO> findAllDTOs(ComponentFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ComponentDTO> cq = cb.createQuery(ComponentDTO.class);
        Root<Component> root = cq.from(Component.class);
        Join<Component, Ms> joinMs = root.join(Component_.ms);
        Join<Component, Ingredient> joinIngredient = root.join(Component_.ingredient);
        cq.multiselect(root.get(Component_.id), root.get(AMOUNT), joinIngredient.get(NAME), joinMs.get(NAME));
        Predicate predicate = new PredicateBuilder(cb, root, filter).toPredicate();
        if (predicate != null) cq.where(predicate);
        cq.orderBy(toOrders(pageable.getSort(), root, cb));
        List<ComponentDTO> content = entityManager.createQuery(cq)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Component> countRoot = countQuery.from(Component.class);
        countQuery.select(cb.count(countRoot));
        Predicate countPredicate = new PredicateBuilder(cb, countRoot, filter).toPredicate();
        if (countPredicate != null) countQuery.where(countPredicate);
        return PageableExecutionUtils.getPage(content, pageable, () -> entityManager.createQuery(countQuery).getSingleResult());
    }

    private static class PredicateBuilder {

        final CriteriaBuilder cb;

        final Root<Component> root;

        final ComponentFilter filter;

        final List<Predicate> predicates = new ArrayList<>();

        PredicateBuilder(CriteriaBuilder cb, Root<Component> root, ComponentFilter filter) {
            this.cb = cb;
            this.root = root;
            this.filter = filter;
        }

        void findByMinAmount() {
            if (!filter.getMinAmount().isEmpty()) {
                predicates.add(cb.ge(root.get(AMOUNT), new Integer(filter.getMinAmount())));
            }
        }

        void findByMaxAmount() {
            if (!filter.getMaxAmount().isEmpty()) {
                predicates.add(cb.le(root.get(AMOUNT), new Integer(filter.getMaxAmount())));
            }
        }

        void findByMsId() {
            if (!filter.getMsName().isEmpty()) {
                Join<Component, Ms> join = root.join(Component_.ms);
                predicates.add(join.get(NAME).in(filter.getMsName()));
            }
        }

        void findByIngredientId() {
            if (!filter.getIngredientName().isEmpty()) {
                Join<Component, Ingredient> join = root.join(Component_.ingredient);
                predicates.add(join.get(NAME).in(filter.getIngredientName()));
            }
        }

        Predicate toPredicate() {
            findByMinAmount();
            findByMaxAmount();
            findByMsId();
            findByIngredientId();
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        }
    }
}
