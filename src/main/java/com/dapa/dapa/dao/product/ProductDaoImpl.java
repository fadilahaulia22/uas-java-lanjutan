package com.dapa.dapa.dao.product;

import com.dapa.dapa.dto.PageResponse;
import com.dapa.dapa.entity.Category;
import com.dapa.dapa.entity.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao {

    @Autowired
    EntityManager entityManager;

    @Override
    public PageResponse<Products> findAll(String productName, String productMerk, String productType,
                                          Category category, int page, int size, String sortBy, String sortOrder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Products> criteriaQuery = criteriaBuilder.createQuery(Products.class);
        Root<Products> productRoot = criteriaQuery.from(Products.class);

        Predicate[] predicates = createPredicate(criteriaBuilder, productRoot, productName, productMerk, productType, category);
        criteriaQuery.where(predicates);

        // Sorting
        if (sortBy != null && !sortBy.isBlank() && sortOrder != null && !sortOrder.isBlank()) {
            if (sortOrder.equalsIgnoreCase("asc")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(productRoot.get(sortBy)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(productRoot.get(sortBy)));
            }
        }

        List<Products> result = entityManager.createQuery(criteriaQuery)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Products> root = countQuery.from(Products.class);
        countQuery.select(criteriaBuilder.count(root))
                .where(createPredicate(criteriaBuilder, root, productName, productMerk, productType, category));

        Long totalItem = entityManager.createQuery(countQuery).getSingleResult();

        return PageResponse.success(result, page, size, totalItem);
    }

    private Predicate[] createPredicate(CriteriaBuilder criteriaBuilder, Root<Products> root,
                                        String productName, String productMerk, String productType, Category category) {
        List<Predicate> predicates = new ArrayList<>();

        if (productName != null && !productName.isBlank() && !productName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("productName"), "%" + productName + "%"));
        }

        if (productMerk != null && !productMerk.isBlank() && !productMerk.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("productMerk"), "%" + productMerk + "%"));
        }

        if (productType != null && !productType.isBlank() && !productType.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("productType"), "%" + productType + "%"));
        }

        if (category != null) {
            predicates.add(criteriaBuilder.equal(root.get("category"), category));
        }

        return predicates.toArray(new Predicate[0]);
    }
}
