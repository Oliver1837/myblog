package it.course.myblogc3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.PostCost;
import it.course.myblogc3.entity.PostCostId;

@Repository
public interface PostCostRepository extends JpaRepository<PostCost, PostCostId> {

}
