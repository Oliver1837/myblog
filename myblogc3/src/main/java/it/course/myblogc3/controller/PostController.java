package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Language;
import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.Tag;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.payload.request.LanguageRequest;
import it.course.myblogc3.payload.request.PostCostRequest;
import it.course.myblogc3.payload.request.PostRequest;
import it.course.myblogc3.payload.request.TagNamesRequests;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.payload.response.CommentResponse;
import it.course.myblogc3.payload.response.CommentResponseForPostDerail;
import it.course.myblogc3.payload.response.PostDetailResponse;
import it.course.myblogc3.payload.response.PostDetailTagResponse;
import it.course.myblogc3.repository.CommentRepository;
import it.course.myblogc3.repository.LanguageRepository;
import it.course.myblogc3.repository.PostRepository;
import it.course.myblogc3.repository.TagRepository;
import it.course.myblogc3.repository.UserRepository;
import it.course.myblogc3.service.PostService;
import it.course.myblogc3.service.UserService;
import it.course.myblogc3.payload.response.PostResponse;
import it.course.myblogc3.payload.response.PostResponseForSearch;
import it.course.myblogc3.payload.response.PostResponseWithPreferredTags;

@RestController
@Validated
public class PostController {
	@Autowired
	PostRepository postRepository;
	@Autowired
	TagRepository tagRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	LanguageRepository languageRepository;
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;
	@Autowired
	PostService postService;
	@PostMapping("private/create-post")
	@PreAuthorize("hasRole('EDITOR')")
	public ResponseEntity<ApiResponseCustom> createPost(@RequestBody PostRequest postRequest,
			HttpServletRequest request) {
		User loggedUser = userService.getAuthenticatedUser();
		if (postRepository.existsByTitle(postRequest.getTitle())) {
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
					"Post title already exists", request.getRequestURI()), HttpStatus.OK);
		}

		Post p = new Post(postRequest.getTitle(), postRequest.getContent(), loggedUser);

		Set<Tag> tags = tagRepository.findByTagNameInAndEnableTrue(postRequest.getTagNames());
		if (!tags.isEmpty()) {
			p.setTags(tags);
		}

		Optional<Language> language = languageRepository.findByLangCodeAndVisibleTrue(postRequest.getLanguage());
		if (!language.isPresent()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "Languega not found ", request.getRequestURI()),
					HttpStatus.OK);

		}
		p.setLanguage(language.get());

		postRepository.save(p);

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "New post add", request.getRequestURI()),
				HttpStatus.OK);
	}

	
	
	
	@PutMapping("private/update-post-status/{postId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> updateStatusPost(@PathVariable long postId, HttpServletRequest request) {

		if (!postRepository.existsById(postId)) {

			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "Post not found", request.getRequestURI()),
					HttpStatus.OK);

		}

		postRepository.updateStatusPostJpql(postId);

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "Post is update", request.getRequestURI()),
				HttpStatus.OK);
	}
	@PutMapping("private/update-post-cost/{postId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> updateCostPost(@PathVariable long postId, HttpServletRequest request) {
		Optional<Post> post = 	postRepository.findByIdAndVisible(postId);
		if (!post.isPresent()) {

			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 404, "Not found", "Post not found", request.getRequestURI()),
					HttpStatus.OK);

		}

		postRepository.save(post.get());

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "Post is update", request.getRequestURI()),
				HttpStatus.OK);
	}


	@GetMapping("public/get-visible-posts")
	public ResponseEntity<ApiResponseCustom> getVisisblePosts(HttpServletRequest request) {

		List<PostResponse> ps = postRepository.getPostsVisible();

		if (ps.isEmpty()) {

			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 404, "OK", "Posts not found", request.getRequestURI()),
					HttpStatus.NOT_FOUND);

		}

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", ps, request.getRequestURI()), HttpStatus.OK);
	}

	@GetMapping("public/get-visible-post/{id}")
	public ResponseEntity<ApiResponseCustom> getVisisblePost(@PathVariable long id, HttpServletRequest request) {

		PostDetailResponse ps = postRepository.getPostVisible(id);

		if (ps == null) {

			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 404, "OK", "Post not found", request.getRequestURI()),
					HttpStatus.NOT_FOUND);

		}

		Set<Tag> tags = tagRepository.getTagByIdPost(id);

		ps.setTagNames(tags.stream().map(Tag::getTagName).collect(Collectors.toSet()));

		List<CommentResponseForPostDerail> comment = commentRepository.getCommentsVisibleByPostId(id);
		ps.setComment(comment);

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", ps, request.getRequestURI()), HttpStatus.OK);
	}

	@DeleteMapping("private/delete-post/{idPost}")
	public ResponseEntity<ApiResponseCustom> deletePost(@PathVariable long idPost, HttpServletRequest request) {

		postRepository.deletePost(idPost);

		return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
				"Post id:" + idPost + "has been delete", request.getRequestURI()), HttpStatus.OK);

	}

	@GetMapping("public/get-visible-posts-by-tags")
	public ResponseEntity<ApiResponseCustom> getComment(@RequestBody TagNamesRequests tagNamesRequests,
			HttpServletRequest request) {

		Set<Tag> tags = tagRepository.findByTagNameInAndEnableTrue(tagNamesRequests.getTagNames());

		if (tags.isEmpty()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "Tag not found", request.getRequestURI()),
					HttpStatus.OK);
		}

		Set<String> listaTag = tags.stream().map(x -> x.getTagName()).collect(Collectors.toSet());

		List<PostResponse> post = postRepository.getPostsByTags(listaTag);

		if (post.isEmpty()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "Post not found", request.getRequestURI()),
					HttpStatus.OK);
		}

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", post, request.getRequestURI()), HttpStatus.OK);
	}

	@GetMapping("public/get-visible-posts-by-lang/{langCode}")
	public ResponseEntity<ApiResponseCustom> getVisiblePostsByLang(
			@PathVariable @NotBlank @Size(min = 2, max = 2) String langCode, HttpServletRequest request) {

		List<PostResponse> posts = postRepository.getPostsByLangCode(langCode);

		if (posts.isEmpty()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "Posts not found ", request.getRequestURI()),
					HttpStatus.OK);
		}

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", posts, request.getRequestURI()), HttpStatus.OK);
	}

	// get voting
	@GetMapping("public/get-visible-posts-and-voting/{postId}")
	public ResponseEntity<ApiResponseCustom> getVisiblePostsAndAverageVoting(@PathVariable Long postId,
			HttpServletRequest request) {

		PostDetailResponse post = postRepository.getPostsVisibleById(postId);
		if (post == null) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "Post not found", request.getRequestURI()),
					HttpStatus.OK);

		}

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", post, request.getRequestURI()), HttpStatus.OK);
	}

	@GetMapping("public/get-visible-posts-by-keyword")
	public ResponseEntity<ApiResponseCustom> getVisiblePostsByKeyword(@RequestParam boolean caseSensitive,
			@RequestParam boolean exacSensitive, @RequestParam String keyword, HttpServletRequest request) {

		String wordFindExact = "\\b".concat(keyword).concat("\\b");
		List<Post> posts = new ArrayList<Post>();
		if (caseSensitive && exacSensitive) {
			posts = postRepository.getPostsVisibleBySearchCaseSensitiveTrue(wordFindExact);
		}
		if (caseSensitive && !exacSensitive) {

			posts = postRepository.getPostsVisibleBySearchCaseSensitiveTrue(keyword);
		}
		if (!caseSensitive && exacSensitive) {
			posts = postRepository.getPostsVisibleBySearchCaseSensitiveFalse(wordFindExact);
		}
		if (!caseSensitive && !exacSensitive) {
			posts = postRepository.getPostsVisibleBySearchCaseSensitiveFalse(keyword);
		}

		if (posts.isEmpty()) {

			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 404, "Not Found", "Post not found", request.getRequestURI()),
					HttpStatus.OK);

		}
		List<PostResponseForSearch> ps = posts.stream().map(p -> new PostResponseForSearch(p.getId(), p.getTitle(),
				p.getContent(), postRepository.getAuthorUsernameByPostId(p.getId()), p.getCreatedAt())

		).collect(Collectors.toList());

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", ps, request.getRequestURI()), HttpStatus.OK);

	}

	@GetMapping("public/get-visible-posts-by-keyword-java")
	public ResponseEntity<ApiResponseCustom> getVisiblePostsByKeywordJava(@RequestParam boolean caseSensitive,
			@RequestParam boolean exacSensitive, @RequestParam String keyword, HttpServletRequest request) {

		String wordFindExact = "\\b".concat(keyword).concat("\\b");
		List<PostResponseForSearch> posts = postRepository.getPostsForSearch();
		
		Pattern  pattern = null;
		
		if (caseSensitive) {
			if (exacSensitive)  {pattern = Pattern.compile(wordFindExact);posts = postService.searchListForPattern(pattern, posts);}
			else  {pattern = Pattern.compile(keyword);posts = postService.searchListForPattern(pattern, posts);}
		}

	
		if (!caseSensitive ) {
			if (exacSensitive) {  pattern = Pattern.compile(wordFindExact,Pattern.CASE_INSENSITIVE); posts = postService.searchListForPattern(pattern, posts);}
			else {  pattern = Pattern.compile(keyword,Pattern.CASE_INSENSITIVE);posts = postService.searchListForPattern(pattern, posts);}
		}
		if (posts.isEmpty()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 404, "Not found", " Not found posts", request.getRequestURI()),
					HttpStatus.OK);

		}

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", posts, request.getRequestURI()), HttpStatus.OK);

	}
	@GetMapping("private/get-visibile-posts-by-preferred-tags")
	@PreAuthorize("hasRole('READER') or hasRole('EDITOR')")
	public ResponseEntity<ApiResponseCustom> getVisiblePostsByPreferredTags(HttpServletRequest request){
		
		User loggeduser=userService.getAuthenticatedUser();
		
		Set<Post> ps = postRepository.getPostsVisibleByPreferredTags(loggeduser.getId());
		
		if(ps.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom( Instant.now(), 200,"OK","no post found ",
					request.getRequestURI()),HttpStatus.OK);
		
		List<PostResponseWithPreferredTags> prwp = ps.stream().map(PostResponseWithPreferredTags::createFromEntity)
		.collect(Collectors.toList());
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", prwp, request.getRequestURI()),
				HttpStatus.OK);
	}

}
