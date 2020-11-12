package it.course.myblogc3.service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import it.course.myblogc3.payload.response.PostResponseForSearch;

@Service
public class PostService {
	
		public List<PostResponseForSearch> searchListForPattern(Pattern pattern , List<PostResponseForSearch> posts) {
			
			
			posts = posts.stream()
					.filter(p -> 
					pattern.matcher(p.getContent()).find() || 
					pattern.matcher(p.getTitle()).find() )
					.collect(Collectors.toList());
			
			return posts;
			
			
		}
	

}
