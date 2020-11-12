package it.course.myblogc3;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import it.course.myblogc3.entity.Comment;
import it.course.myblogc3.entity.Language;
import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.Tag;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.entity.Voting;
import it.course.myblogc3.entity.VotingId;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.LanguageRepository;
import it.course.myblogc3.repository.PostRepository;
import it.course.myblogc3.repository.TagRepository;
import it.course.myblogc3.repository.TokenRepository;
import it.course.myblogc3.repository.UserRepository;
import it.course.myblogc3.repository.VotingRepository;
@EnableScheduling
@SpringBootApplication
public class Myblogc3Application {
		
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(Myblogc3Application.class, args);		
		
		
		 
	}
	
		@Autowired
	   PostRepository postRepository;
		@Autowired
		  LanguageRepository languageRepository;
		@Autowired
		 UserRepository userRepository;
		@Autowired
		TagRepository tagRepository;
		@Autowired
		VotingRepository votingRepository;
		@Autowired
		TokenRepository tokenRepository;
		//SI PASSANO 5 PARAMETRI : SECONDI ,MINUTI ,ORE,GIORNO DEL MESE , MESE , GIORNO DELLA SETTIMANA
		@Scheduled(cron = "0 0 10 * * *")//*INDICA TUTTI I GIORNI 
		@Transactional
		public void deletedOldTokens() {
			tokenRepository.deleteAllByExpiryDateLessThan(new Date());
		}
		
		
	@Bean
	@Profile("dev")
	@Transactional
	public void createPostAutomatically() {
		 String title = "TITLE_";
		if(!postRepository.existsByTitleStartsWith(title)) {
			List<Language> list =  languageRepository.findAll();
					
		 	List<User> usersEditor = userRepository.findByRole(2L);
		 
		 	List<User> usersReader = userRepository.findByRole(3L);
		 
			String content = "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullamco laboriosam, nisi ut aliquid ex ea commodi consequatur. Duis aute irure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
		 
			List<Tag> tagsList =  tagRepository.findByEnableTrue().stream().collect(Collectors.toList());
			List<Voting> vs= new ArrayList<Voting>(); 
			
			
			
			  ArrayList<Post> posts= new ArrayList<Post>(); 
			  Random rand = new Random();
			  for(int i = 0 ; i<50 ; i++) {
			
			  Post p = new Post("TITLE_"+i, content, rand.nextBoolean(), usersEditor.get(rand.nextInt(usersEditor.size())),list.get(rand.nextInt(list.size())));
			  
			  String contentComment = "COMMENT_";
			  if(p.getVisible()) {
				  //tags
				  Set<Tag> tagsPost = new HashSet<Tag>(); 
				  int[] n = {1,2,3} ;
				  for(int k =0 ; k <=  n[rand.nextInt(3)] ;k++ ) {
					 
					  tagsPost.add(tagsList.get(rand.nextInt(tagsList.size())));
						  
				  }
				  p.setTags(tagsPost);
				  
				  
				  ArrayList<Comment> comments= new ArrayList<Comment>(); 
				//comment  
			  for(int j  = 0 ; j<10 ; j++) {
				  Comment comment = new Comment(contentComment+""+j+"_POST_"+i,rand.nextBoolean() ,usersReader.get(rand.nextInt(usersReader.size())),p );
				  
				  comments.add(comment);
			  }
			  p.setComments(comments);
			  
			  
			 //voting
			  for(int j = 0 ;j <usersReader.size(); j++) {
				  
				  
				 VotingId votingId=  new VotingId(p, usersReader.get(j));
				  vs.add(new Voting(votingId,(int) (Math.random()*5 )+1));
				  
				  
			  }
			  
			  
			  }
			  
			  
			  
			  posts.add(p); 
		
			  
			  
			  }
			  
			  
			  postRepository.saveAll(posts);
			  votingRepository.saveAll(vs);
			 
			  
			  
			  
		}
	}
}
