package it.course.myblogc3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.course.myblogc3.entity.LoginAttempt;
import it.course.myblogc3.entity.LoginAttemptId;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, LoginAttemptId>{

}
