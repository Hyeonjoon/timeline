package me.timeline.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import me.timeline.dto.FollowRequestDTO;
import me.timeline.dto.FollowResponseDTO;
import me.timeline.dto.JwtInputDTO;
import me.timeline.dto.PostCommentRequestDTO;
import me.timeline.dto.PostCommentResponseDTO;
import me.timeline.dto.PostWritingRequestDTO;
import me.timeline.dto.PostWritingResponseDTO;
import me.timeline.dto.SignInRequestDTO;
import me.timeline.dto.SignInResponseDTO;
import me.timeline.dto.SignUpRequestDTO;
import me.timeline.dto.SignUpResponseDTO;
import me.timeline.entity.AuthProvider;
import me.timeline.entity.Comment;
import me.timeline.entity.Following;
import me.timeline.entity.SignatureInformation;
import me.timeline.entity.User;
import me.timeline.entity.Writing;
import me.timeline.repository.AuthProviderRepository;
import me.timeline.repository.CommentRepository;
import me.timeline.repository.FollowingRepository;
import me.timeline.repository.SignatureInformationRepository;
import me.timeline.repository.UserRepository;
import me.timeline.repository.WritingRepository;

@Service("TimelineService")
public class TimelineServiceImpl implements TimelineService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SignatureInformationRepository signatureInformationRepository;
	
	@Autowired
	AuthProviderRepository authProviderRepository;
	
	@Autowired
	WritingRepository writingRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	FollowingRepository followingRepository;
	
	@Autowired
	JwtService jwtService;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	/* SignUp
	 * - Input: SignUpRequestDTO
	 * - Output: SignUpResponseDTO
	 * Check if given email address is already exists.
	 * If it is, return false, and if not, create entities with given information and save them in the database.
	 */
	public SignUpResponseDTO SignUp(SignUpRequestDTO signUpRequestDTO) {
		/* Create a SignUpResponseDTO object. */
		SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO();
		
		/* Check if given email address is already exists in database. */
		boolean isEmailDuplicated = userRepository.existsByEmail(signUpRequestDTO.getEmail());
		if (isEmailDuplicated) {
			signUpResponseDTO.setSuccess(false);
			return signUpResponseDTO;
		}
		else {
			/* Create a new User entity. */
			User user = new User();
			user.setEmail(signUpRequestDTO.getEmail());
			user.setNickname(signUpRequestDTO.getNickname());
			user.initSignatureInformationList();
			user.initWritingList();
			user.initCommentList();
			user.initFollowingList();
			user.initFollowerList();
			
			/* Get a AuthProvider entity with given type. 
			 * We can ensure that an authProvider entity exists, because an user cannot set the provider type. */
			AuthProvider authProvider = authProviderRepository.findByType(signUpRequestDTO.getProvider()).get();
			
			/* Create a new SignatureInformation entity. */
			SignatureInformation signatureInformation = new SignatureInformation();
			signatureInformation.setProviderUserId(0);
			signatureInformation.setPasskey(passwordEncoder.encode(signUpRequestDTO.getPassword()));
			
			/* Create some referential relationships among entities. */
			signatureInformation.setUser(user);
			signatureInformation.setAuthProvider(authProvider);
			user.addSignatureInformation(signatureInformation);
			authProvider.addSignatureInformation(signatureInformation);
			
			/* Save the data in database. */
			userRepository.save(user);
			authProviderRepository.save(authProvider);
			signatureInformationRepository.save(signatureInformation);
			
			signUpResponseDTO.setSuccess(true);
			return signUpResponseDTO;
		}
	}
	
	/* SignIn
	 * - Input: SignInRequestDTO
	 * - Output: SignInResponseDTO
	 * Check if given email and password with given provider type is registered in the database.
	 * If it is, return a Jwt token, and if not, reject the signing in.
	 */
	public SignInResponseDTO SignIn(SignInRequestDTO signInRequestDTO) {
		/* Create a SignInResponseDTO object. */
		SignInResponseDTO signInResponseDTO = new SignInResponseDTO();
		
		/* Get an User entity, and an AuthProvider entity with corresponding email, and provider type.
		 * If there're no such entities, return the response.
		 * We can ensure that an authProvider entity exists, because an user cannot set the provider type. */
		Optional<User> userNullable = userRepository.findByEmail(signInRequestDTO.getEmail());
		if (!userNullable.isPresent()) {
			signInResponseDTO.setSuccess(false);
			signInResponseDTO.setJwtToken("");
			return signInResponseDTO;
		}
		User user = userNullable.get();
		AuthProvider authProvider = authProviderRepository.findByType(signInRequestDTO.getProvider()).get();
		
		/* Get a SignatureInformation entity which references the User entity and AuthProvider entity above.
		 * If there's no such entity, return the response. */
		Optional<SignatureInformation> signatureInformationNullable = signatureInformationRepository.findByUser_IdAndAuthProvider_Id(user.getId(), authProvider.getId());
		if (!signatureInformationNullable.isPresent()) {
			signInResponseDTO.setSuccess(false);
			signInResponseDTO.setJwtToken("");
			return signInResponseDTO;
		}
		SignatureInformation signatureInformation = signatureInformationNullable.get();
		
		/* Check if given password matches the stored password in database. */
		if (passwordEncoder.matches(signInRequestDTO.getPassword(), signatureInformation.getPasskey())) {
			/* Create a Jwt token for the user. */
			JwtInputDTO jwtInputDTO = new JwtInputDTO();
			jwtInputDTO.setId(user.getId());
			signInResponseDTO.setJwtToken(jwtService.JwtCreate(jwtInputDTO));
			signInResponseDTO.setSuccess(true);
		}
		else {
			signInResponseDTO.setSuccess(false);
			signInResponseDTO.setJwtToken("");
		}
		
		return signInResponseDTO;
	}
	
	/* PostWriting
	 * - Input: PostWritingRequestDTO, jwtToken
	 * - Output: postWritingResponseDTO
	 * Check if given writing content has length exceeding 150 characters.
	 * If it is, reject the request, and if not, retrieve user id from Jwt token and save the content in database.
	 */
	public PostWritingResponseDTO PostWriting(PostWritingRequestDTO postWritingRequestDTO, String jwtToken) {
		/* Create a new postWritingResponseDTO object. */
		PostWritingResponseDTO postWritingResponseDTO = new PostWritingResponseDTO();
		
		/* Check if given content has length exceeding 150 characters. */
		if (postWritingRequestDTO.getContent().length() > 150) {
			postWritingResponseDTO.setSuccess(false);
			postWritingResponseDTO.setContent("");
			postWritingResponseDTO.setPostTime(null);
			return postWritingResponseDTO;
		}
		
		/* Retrieve user id from Jwt token, and get the User entity with that user id out of  the database. */
		User user = userRepository.findById(jwtService.JwtGetUserId(jwtToken)).get();
		
		/* Create a new Writing entity object. */
		Writing writing = new Writing();
		Date now = new Date();
		writing.setContent(postWritingRequestDTO.getContent());
		writing.setCreatedAt(now);
		writing.initCommentList();
		
		/* Create a referential relationship between a Writing entity and User entity. */
		writing.setUser(user);
		user.addWriting(writing);
		
		/* Save the data in database. */
		writingRepository.save(writing);
		userRepository.save(user);
		
		/* Construct the postWritingResponseDTO object and return it. */
		postWritingResponseDTO.setSuccess(true);
		postWritingResponseDTO.setContent(postWritingRequestDTO.getContent());
		postWritingResponseDTO.setPostTime(now);
		
		return postWritingResponseDTO;
	}
	
	/* PostComment
	 * - Input: postCommentRequestDTO, jwtToken
	 * - Output: postCommentResponseDTO
	 * Retrieve user id from Jwt token and save the content in database.
	 */
	public PostCommentResponseDTO PostComment(PostCommentRequestDTO postCommentRequestDTO, String jwtToken) {
		/* Create a new postCommentResponseDTO. */
		PostCommentResponseDTO postCommentResponseDTO = new PostCommentResponseDTO();
		
		/* Retrieve user id from Jwt token, and get the User entity with that user id out of the database. 
		 * And also get the Writing entity out of the database. */
		User user = userRepository.findById(jwtService.JwtGetUserId(jwtToken)).get();
		Writing writing = writingRepository.findById(postCommentRequestDTO.getWritingId()).get();
		
		/* Create a new Comment object. */
		Comment comment = new Comment();
		Date now = new Date();
		comment.setContent(postCommentRequestDTO.getContent());
		comment.setCreatedAt(now);
		
		/* Create a referential relationships among entities. */
		comment.setUser(user);
		comment.setWriting(writing);
		user.addComment(comment);
		writing.addComment(comment);
		
		/* Save the data in database. */
		commentRepository.save(comment);
		userRepository.save(user);
		writingRepository.save(writing);
		
		/* Construct the PostCommentResponseDTO object and return it. */
		postCommentResponseDTO.setSuccess(true);
		postCommentResponseDTO.setWritingId(postCommentRequestDTO.getWritingId());
		postCommentResponseDTO.setContent(postCommentRequestDTO.getContent());
		postCommentResponseDTO.setPostTime(now);
		
		return postCommentResponseDTO;
	}
	
	public FollowResponseDTO Follow(FollowRequestDTO followRequestDTO, String jwtToken) {
		/* Create a new FollowResponseDTO object. */
		FollowResponseDTO followResponseDTO = new FollowResponseDTO();
		
		/* Retrieve user id from Jwt token and get the User entity for that user id.
		 * And also get the target User's entity. */
		User follower = userRepository.findById(jwtService.JwtGetUserId(jwtToken)).get();
		User followee = userRepository.findById(followRequestDTO.getTargetId()).get();
		
		/* Create a new Following entity. */
		Following following = new Following();
		
		/* Create referential relationships among entities. */
		following.setFollower(follower);
		following.setFollowee(followee);
		follower.addFollowing(followee);
		followee.addFollower(follower);
		
		/* Save the data in database. */
		followingRepository.save(following);
		
		return followResponseDTO;
	}
}
