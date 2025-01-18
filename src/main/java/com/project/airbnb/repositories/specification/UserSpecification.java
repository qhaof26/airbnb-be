package com.project.airbnb.repositories.specification;

import com.project.airbnb.models.User;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> isActive(Boolean status) {
        return (root, query, criteriaBuilder) ->{
            if(status == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<User> hasRole(String roleName){
        return (root, query, criteriaBuilder) -> {
            if(roleName == null || roleName.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            root.fetch("roles", JoinType.LEFT);
            return criteriaBuilder.equal(root.get("roles").get("roleName"), roleName);

            // Join<User, Role> roles = root.join("roles"); n+1 query problems
            // return criteriaBuilder.equal(roles.get("roleName"), roleName);
        };
    }

    public static Specification<User> searchByKeyword(String keyword){
        return (root, query, criteriaBuilder) -> {
            if(keyword == null || keyword.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likePattern)
            );
        };
    }

    public static Specification<User> filterUsers(String roleName, Boolean status, String keyword){
        return Specification.where(hasRole(roleName))
                .and(isActive(status))
                .and(searchByKeyword(keyword));
    }
}
