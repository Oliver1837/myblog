package it.course.myblogc3.controller;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import it.course.myblogc3.entity.Authority;
import it.course.myblogc3.entity.AuthorityName;
import it.course.myblogc3.entity.LoginAttempt;
import it.course.myblogc3.entity.LoginAttemptId;
import it.course.myblogc3.entity.Token;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.payload.request.ChangeAuthoritiesRequest;
import it.course.myblogc3.payload.request.SignInRequest;
import it.course.myblogc3.payload.request.SignUpRequest;
import it.course.myblogc3.payload.request.UpdateMeRequest;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.payload.response.UserResponse;
import it.course.myblogc3.repository.AuthorityRepository;
import it.course.myblogc3.repository.LoginAttemptRepository;
import it.course.myblogc3.repository.TokenRepository;
import it.course.myblogc3.repository.UserRepository;
import it.course.myblogc3.security.JwtAuthenticationResponse;
import it.course.myblogc3.security.JwtTokenUtil;
import it.course.myblogc3.service.BanService;
import it.course.myblogc3.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	AuthorityRepository authorityRepository;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	BanService banService;
	
	@Autowired 
	TokenRepository tokenRepository;
	@Autowired 
	LoginAttemptRepository loginAttemptRepository;

	@Value("${jwt.header}")
	private String tokenHeader;
	@PostMapping("/public/signin")
	public ResponseEntity<ApiResponseCustom> signIn(@Valid @RequestBody SignInRequest signInRequest,
			HttpServletRequest request, HttpServletResponse response) 
			throws AuthenticationException, JsonProcessingException{
		
		Optional<User> u = userRepository.findByUsernameOrEmail(signInRequest.getUsernameOrEmail(), signInRequest.getUsernameOrEmail());
		Optional<LoginAttempt> la=loginAttemptRepository.findById(new LoginAttemptId(u.get()));
		User user=u.get();
		String password=signInRequest.getPassword();
	
		if(!u.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK", "Please register yourself before the Sign In", request.getRequestURI()
					), HttpStatus.OK);
		
		//banned user
		if(userService.isBanned(user))
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK", "your are banned ", request.getRequestURI()
					), HttpStatus.OK);
			
		// not valid password
		if(!userService.isValidPassword(user,password)) {
			if(userService.getAttempt(user,la,request)==3) 
				return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
							Instant.now(), 401,	"Unauthorized", "you can retry after 15 minutes ", request.getRequestURI()
						), HttpStatus.UNAUTHORIZED);
			else
				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 401,	"Unauthorized", "Wrong credentials ", request.getRequestURI()
						), HttpStatus.UNAUTHORIZED);
		}else 
			if(la.isPresent())
			  loginAttemptRepository.delete(la.get());
		if(user.getAuthorities().contains(new Authority(3L,AuthorityName.ROLE_READER))){
		if(userService.getLoginTrace(user)  ) {
			user.setCredits(user.getCredits()+1);
			userRepository.save(user);
		}
		}
		
		
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(u.get().getUsername(), signInRequest.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(u.get().getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		
		response.setHeader(tokenHeader, token);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK",
					new JwtAuthenticationResponse(userDetails.getUsername(), userDetails.getAuthorities(), token),	
					request.getRequestURI()
				), HttpStatus.OK);
		
	}
	@PostMapping("/public/signup")
	@Transactional
	public ResponseEntity<ApiResponseCustom> signUp(@RequestBody SignUpRequest signUpRequest, HttpServletRequest request) {

		long countUsers = userRepository.count(); // select count(*) from user;

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "User already registered", request.getRequestURI()),
					HttpStatus.OK);
		}

		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "Username already in use", request.getRequestURI()),
					HttpStatus.OK);
		}

		User user = new User(signUpRequest.getEmail(), signUpRequest.getUsername(), signUpRequest.getPassword());
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		String registrationConfirmCode = null;
		try {
			registrationConfirmCode = userService.toHexString(userService.getSHA(Instant.now().toString()));
			user.setRegistrationConfirmCode(registrationConfirmCode);
		} catch (NoSuchAlgorithmException e) {
			e.getStackTrace();
		}

		userRepository.save(user);

		Optional<Authority> userAuthority = Optional.empty();
		if (countUsers > 0) {
			userAuthority = authorityRepository.findByName(AuthorityName.ROLE_READER);
		} else {
			userAuthority = authorityRepository.findByName(AuthorityName.ROLE_ADMIN);
		}
		user.setAuthorities(Collections.singleton(userAuthority.get()));

		return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
				"Check your email: localhost:8081/public/confirm-registration/" + registrationConfirmCode,
				request.getRequestURI()), HttpStatus.OK);
	}

/*	@PostMapping("public/signin")
	public ResponseEntity<ApiResponseCustom> signIn(@Valid @RequestBody SignInRequest signInRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, JsonProcessingException {

		Optional<User> u = userRepository.findByUsernameOrEmail(signInRequest.getUsernameOrEmail(),
				signInRequest.getUsernameOrEmail());
		// select * from user where username='pippo' or email='pippo';
		// select * from user where username='pippo@gmail.abc' or
		// email='pippo@gmail.abc';
		if (!u.isPresent())
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
					"Please register yourself before the Sign In", request.getRequestURI()), HttpStatus.OK);
		
		
	
		
	//	boolean  valore = banService.verifyBanUntil(u.get());
		Optional<LoginAttempt> la  = loginAttemptRepository.findById(new LoginAttemptId(u.get()));
		if (u.get().getBannedUtil() != null && u.get().getBannedUtil().isAfter(LocalDateTime.now()))
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 401, "Unauthorized",
					"your are banned ", request.getRequestURI()), HttpStatus.UNAUTHORIZED);
		else if (u.get().getBannedUtil() != null) {
			u.get().setEnabled(true);
			u.get().setBannedUtil(null);
			userRepository.save(u.get());
			if (la.isPresent()) {
			loginAttemptRepository.delete(la.get());
			}
		}
	

		if(!userService.isPasswordCorrect(signInRequest.getPassword(), u.get().getPassword())) {
		
			if(la.isPresent() && LocalDateTime.now().isBefore(la.get().getLoginFailAt().minusDays(1L))) {
				

				int counter = la.get().getCounter();
				
					if(counter == 2) {
						la.get().setCounter(counter++);
						//set ban for 15 min
						u.get().setBannedUtil(LocalDateTime.now().plusMinutes(15L));
						return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 401, "Unauthorized",
								"Wrong credentials",
								request.getRequestURI()), HttpStatus.OK); 
					}
					if(counter==1) {
						la.get().setCounter(counter++);
						return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 401, "Unauthorized",
								"Wrong credentials",
								request.getRequestURI()), HttpStatus.OK); 
					}
				
			}else {
				loginAttemptRepository.save(new LoginAttempt(new LoginAttemptId(u.get()),1,request.getRemoteAddr()));
				return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 401, "Unauthorized",
						"Wrong credentials",
						request.getRequestURI()), HttpStatus.OK); 
			}
		}
		// ctrl if bannedUntil < NOW if yes set enable true on the User
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(u.get().getUsername(), signInRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		final UserDetails userDetails = userDetailsService.loadUserByUsername(u.get().getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);

		response.setHeader(tokenHeader, token);

		return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
				new JwtAuthenticationResponse(userDetails.getUsername(), userDetails.getAuthorities(), token),
				request.getRequestURI()), HttpStatus.OK);

	}
*/
	@PutMapping("public/confirm-registration/{registrationConfirmCode}") // conferma registrazione
	public ResponseEntity<ApiResponseCustom> signIn(
			@PathVariable("registrationConfirmCode") String registrationConfirmCode, @Valid @RequestParam String email,
			HttpServletRequest request) {

		Optional<User> u = userRepository.findByRegistrationConfirmCodeAndEmail(registrationConfirmCode, email);// Verifica
																												// che
																												// l'utente
																												// esiste

		if (!u.isPresent()) {

			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK",
							"User not fount or user already confirmed the registration.", request.getRequestURI()),
					HttpStatus.OK);
		}

		User user = u.get();

		user.setRegistrationConfirmCode(null);
		user.setEnabled(true);
		userRepository.save(user);

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "User confirmed registration", request.getRequestURI()),
				HttpStatus.OK);

	}

	@PutMapping("private/update-authorities")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> signIn(
			@Valid @RequestBody ChangeAuthoritiesRequest changeAuthoritiesRequest, HttpServletRequest request) {

		User loggedUser = userService.getAuthenticatedUser();

		Optional<User> u = userRepository.findByUsername(changeAuthoritiesRequest.getUsername());

		if (!u.isPresent()) {

			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK",
							"User" + changeAuthoritiesRequest.getUsername() + " not found", request.getRequestURI()),
					HttpStatus.OK);

		}
		if (loggedUser.getUsername().equals(changeAuthoritiesRequest.getUsername())) {

			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
					"You can't change yout authority.", request.getRequestURI()), HttpStatus.OK);

		}
		Set<AuthorityName> authorityNames = changeAuthoritiesRequest.getAuthorityName();
		if (authorityNames.isEmpty()) {

			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
					"No Authority has been selected", request.getRequestURI()), HttpStatus.OK);

		}

		Set<Authority> authorities = authorityRepository.findByNameIn(authorityNames);

		u.get().setAuthorities(authorities);

		userRepository.save(u.get());

		return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
				"User " + u.get().getUsername() + " update authorities", request.getRequestURI()), HttpStatus.OK);

	}

	@PutMapping("private/update-user-status")
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> updateUserStatus(@RequestParam String username,
			HttpServletRequest request) {

		User loggedUser = userService.getAuthenticatedUser();

		Optional<User> u = userRepository.findByUsernameOrEmail(username, username);

		if (!u.isPresent()) {// Controllo se esiste l'utente

			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "User  not found", request.getRequestURI()),
					HttpStatus.OK);

		}
		if (loggedUser.getUsername().equals(username)) {

			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
					"You can't change your status.", request.getRequestURI()), HttpStatus.OK);

		}
	
		u.get().setEnabled(!u.get().getEnabled());
		userRepository.save(u.get());

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK",
						"You are change status  " + username + " to " + u.get().getEnabled(), request.getRequestURI()),
				HttpStatus.OK);

	}

	
	@PutMapping("private/change-password") // 1° Cambio password
	public ResponseEntity<ApiResponseCustom> changePassord(@Valid @RequestParam String newPassword,
			HttpServletRequest request) {

		User loggedUser = userService.getAuthenticatedUser();// vado a recuperare l'utente con questo metodo

		loggedUser.setPassword(passwordEncoder.encode(newPassword));// encode password

		userRepository.save(loggedUser);// salva la password

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "User ", request.getRequestURI()), HttpStatus.OK);

	}
	// 2° password cambio password

	@PutMapping("public/reset-password-ic")
	public ResponseEntity<ApiResponseCustom> resetPassord(@Valid @RequestParam String email, HttpServletRequest request,
			HttpServletResponse response) {

		Optional<User> u = userRepository.findByEmail(email);

		if (!u.isPresent()) {// Controllo se esiste l'utente

			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "User  not found", request.getRequestURI()),
					HttpStatus.OK);

		}

		String identifierCode = null;

		try {

			identifierCode = userService.toHexString(userService.getSHA(Instant.now().toString()));

			u.get().setIdentifierCode(identifierCode);

			userRepository.save(u.get());

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		}

		String link = "localhost:8081/public/reset-password-ic/" + identifierCode;
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "Check your email : " + link, request.getRequestURI()),
				HttpStatus.OK);

	}

	@PutMapping("public/reset-password-ic/{idCode}")
	public ResponseEntity<ApiResponseCustom> confirmResetPassord(@PathVariable String idCode,
			@Valid @RequestParam String newPassword, @Valid @RequestParam String email, HttpServletRequest request) {

		Optional<User> u = userRepository.findByIdentifierCodeAndEmail(idCode, email);// Verifica che l'utente esiste

		if (!u.isPresent()) {

			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
					"Identifier Code is not present", request.getRequestURI()), HttpStatus.OK);
		}

		User user = u.get();

		user.setPassword(passwordEncoder.encode(newPassword));

		user.setIdentifierCode(null);

		userRepository.save(user);

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "Password is update ", request.getRequestURI()),
				HttpStatus.OK);

	}

	@PutMapping("private/update-me")
	public ResponseEntity<ApiResponseCustom> updateMe(@Valid @RequestBody UpdateMeRequest updateMeRequest, HttpServletRequest request) {

		User loggedUser = userService.getAuthenticatedUser();

		Optional<User> findUByEmail = userRepository.findByEmail(updateMeRequest.getEmail());

		if (!findUByEmail.isPresent()) {
			loggedUser.setEmail(updateMeRequest.getEmail());
		} else if (findUByEmail.isPresent() && findUByEmail.get().equals(loggedUser)) {
			loggedUser.setEmail(updateMeRequest.getEmail());
		} else {
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
					"Please choose another email", request.getRequestURI()), HttpStatus.OK);
		}

		Optional<User> findUByUsername = userRepository.findByUsername(updateMeRequest.getUsername());
		if (!findUByUsername.isPresent()) {
			loggedUser.setUsername(updateMeRequest.getUsername());
		} else if (findUByUsername.isPresent()
				&& findUByUsername.get().getUsername().equals(loggedUser.getUsername())) {
			loggedUser.setUsername(updateMeRequest.getUsername());
		} else {
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
					"Please choose another username", request.getRequestURI()), HttpStatus.OK);
		}
		// 2° Metodo faccio la modifica sul databse senza eliminare

		
		userRepository.save(loggedUser);

		/* dbFileRepository.delete(findUByEmail.get().getDbFile()); */

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "Profile updated", request.getRequestURI()),
				HttpStatus.OK);

	}

	@GetMapping("private/get-users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> getUsers(HttpServletRequest request) {

		List<User> usersList = userRepository.findAll();

		List<UserResponse> users = usersList.stream()
				.map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getEnabled(),
						u.getAuthorities().stream().map(a -> a.getName()).collect(Collectors.toSet())// chiusura map
																										// authorities
				)).collect(Collectors.toList());

		if (usersList.isEmpty()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "Not found users", request.getRequestURI()),
					HttpStatus.OK);

		}

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "List users" + usersList, request.getRequestURI()),
				HttpStatus.OK);

	}

	@GetMapping("private/get-user/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> getUser(@PathVariable Long id, HttpServletRequest request) {

		List<User> usersList = userRepository.findAll();

		Optional<User> user = usersList.stream().filter(u -> id == u.getId()).findFirst();

		if (!user.isPresent()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "User not present ",

							request.getRequestURI()),
					HttpStatus.OK);

		}

		UserResponse ur = new UserResponse(user.get().getId(), user.get().getUsername(), user.get().getEmail(),
				user.get().getEnabled(),
				user.get().getAuthorities().stream().map(a -> a.getName()).collect(Collectors.toSet()));

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "User : " + ur.toString(),

						request.getRequestURI()),
				HttpStatus.OK);

	}
	@PostMapping("private/logout")
	@Transactional
	public ResponseEntity<ApiResponseCustom> logout(HttpServletRequest request) {
		
		String authToken = request.getHeader(this.tokenHeader);
		Date d = jwtTokenUtil.getExpirationDateFromToken(authToken);
		Token t = new Token(authToken,d);
		tokenRepository.save(t);
		
		
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "Logout",

						request.getRequestURI()),
				HttpStatus.OK);
	}
	@DeleteMapping("private/clean-expired-token")
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional 
	public ResponseEntity<ApiResponseCustom> deleteToken(HttpServletRequest request) {
		
		
		tokenRepository.deleteAllByExpiryDateLessThan(new Date());
		
		
		
		return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, "OK",
				"expired tokens have been removed",
				request.getRequestURI()), HttpStatus.OK);
		
	}
	
	
	@GetMapping("public/ip")
	public ResponseEntity<ApiResponseCustom> getIP(HttpServletRequest request){
		
		return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, 
				"OK", request.getRemoteAddr(), request.getRequestURI()), HttpStatus.OK);
	}


}
