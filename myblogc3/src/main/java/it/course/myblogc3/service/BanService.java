package it.course.myblogc3.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.course.myblogc3.entity.User;
import it.course.myblogc3.repository.CommentRepository;
import it.course.myblogc3.repository.UserRepository;

@Service
public class BanService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	CommentRepository commentRepository;
	public void createBanUntil(int value,User u) {
		
		
		if(u.getBannedUtil()==null) {
		
		u.setBannedUtil( LocalDateTime.now().plusDays(value));
		}else {
			
			u.setBannedUtil( u.getBannedUtil().plusDays(value));
			
		}
		
		u.setEnabled(false);
		
	
	}
	public boolean verifyBanUntil(User u) {
		
		if(u.getBannedUtil()==null) {
			return false;
		}else {
			return u.getBannedUtil().isBefore(LocalDateTime.now());
		}
		
	}
	

}
