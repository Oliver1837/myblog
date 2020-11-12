package it.course.myblogc3.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import it.course.myblogc3.entity.Language;
import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.repository.LanguageRepository;
import it.course.myblogc3.repository.PostRepository;
import it.course.myblogc3.repository.UserRepository;
@Service
public class Utils {
		@Autowired
	   PostRepository postRepository;
		@Autowired
		  LanguageRepository languageRepository;
		@Autowired
		 UserRepository userRepository;
		static String content="Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullamco laboriosam, nisi ut aliquid ex ea commodi consequatur. Duis aute irure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum\r\n"
				+ "";
		
		/*
		 * @EventListener public void appReady(ApplicationReadyEvent event) {
		 * 
		 * List<Language> list = languageRepository.findAll();
		 * 
		 * List<User> users = userRepository.findByEditor(2L);
		 * 
		 * ArrayList<Post> posts= new ArrayList<Post>(); for(int i = 0 ; i<50 ; i++) {
		 * Random rand = new Random(); Post p = new Post("TITLE_"+i, content,
		 * rand.nextBoolean(),
		 * users.get(rand.nextInt(users.size()-1)),list.get(rand.nextInt(list.size()-1))
		 * ); posts.add(p); }
		 * 
		 * postRepository.saveAll(posts);
		 * 
		 * 
		 * 
		 * }
		 */
}
