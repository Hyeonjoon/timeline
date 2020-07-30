package me.timeline.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import me.timeline.dto.CommentInformationDTO;
import me.timeline.dto.FollowRequestDTO;
import me.timeline.dto.FollowResponseDTO;
import me.timeline.dto.UserInformationDTO;
import me.timeline.dto.WritingInformationDTO;
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
import me.timeline.exception.DatabaseRelatedException;
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
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	/* SignUp
	 * - Input: SignUpRequestDTO
	 * - Output: SignUpResponseDTO
	 * Check if given email address is already exists.
	 * If it is, return false, and if not, create entities with given information and save them in the database.
	 */
	@Transactional
	public SignUpResponseDTO SignUp(SignUpRequestDTO signUpRequestDTO) {
		/* Create a SignUpResponseDTO object. */
		SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO();
		
		/* Check if given email address already exists in database. */
		boolean isEmailDuplicated = userRepository.existsByEmail(signUpRequestDTO.getEmail());
		if (isEmailDuplicated) {
			signUpResponseDTO.setSuccess(false);
			signUpResponseDTO.setMessage("Given email is duplicated. If you want to link your accounts, sign in and try linking account.");
			return signUpResponseDTO;
		}
		
		/* Check if given nickname already exists in database. */
		boolean isNicknameDuplicated = userRepository.existsByNickname(signUpRequestDTO.getNickname());
		if (isNicknameDuplicated) {
			signUpResponseDTO.setSuccess(false);
			signUpResponseDTO.setMessage("Given nickname is duplicated.");
			return signUpResponseDTO;
		}
		
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
		signatureInformation.setProviderUserId(signUpRequestDTO.getProviderUserId());
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
	
	/* SignIn
	 * - Input: SignInRequestDTO
	 * - Output: SignInResponseDTO
	 * Check if given email and password with given provider type is registered in the database.
	 * If it is, return a Jwt token, and if not, reject the signing in.
	 */
	@Transactional(readOnly = true)
	public SignInResponseDTO SignIn(SignInRequestDTO signInRequestDTO) {
		/* Create a SignInResponseDTO object. */
		SignInResponseDTO signInResponseDTO = new SignInResponseDTO();
		
		/* Get an User entity, and an AuthProvider entity with corresponding email, and provider type.
		 * If there're no such entities, return the response.
		 * We can ensure that an authProvider entity exists, because an user cannot set the provider type. */
		Optional<User> userNullable = userRepository.findByEmail(signInRequestDTO.getEmail());
		if (!userNullable.isPresent()) {
			signInResponseDTO.setSuccess(false);
			signInResponseDTO.setJwtToken(null);
			return signInResponseDTO;
		}
		User user = userNullable.get();
		AuthProvider authProvider = authProviderRepository.findByType(signInRequestDTO.getProvider()).get();
		
		/* Get a SignatureInformation entity which references the User entity and AuthProvider entity above.
		 * If there's no such entity, return the response. */
		Optional<SignatureInformation> signatureInformationNullable = signatureInformationRepository.findByUser_IdAndAuthProvider_Id(user.getId(), authProvider.getId());
		if (!signatureInformationNullable.isPresent()) {
			signInResponseDTO.setSuccess(false);
			signInResponseDTO.setJwtToken(null);
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
			signInResponseDTO.setJwtToken(null);
		}
		
		return signInResponseDTO;
	}
	
	/* LinkAccount
	 * - Input: SignUpRequestDTO, String jwtToken
	 * - Output: SignUpResponseDTO
	 * Create a SignatureInformation entity with a new Authorization Provider,
	 * and link it with User entity with user id described in jwtToken.
	 */
	@Transactional(rollbackFor = DatabaseRelatedException.class)
	public SignUpResponseDTO LinkAccount(SignUpRequestDTO signUpRequestDTO, String jwtToken) {
		/* Create a new SignUpResponseDTO object. */
		SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO();
		
		/* Get an User entity, and an AuthProvider entity with corresponding email, and provider type.
		 * If there're no such entities, return the response.
		 * We can ensure that an authProvider entity exists, because an user cannot set the provider type. */
		Optional <User> userNullable = userRepository.findById(jwtService.JwtGetUserId(jwtToken));
		if (!userNullable.isPresent()) {
			throw new DatabaseRelatedException("The user id retrieved from Jwt token is not valid.");
		}
		User user = userNullable.get();
		AuthProvider authProvider = authProviderRepository.findByType(signUpRequestDTO.getProvider()).get();
		
		/* Check if this user already has a signature information with given authorization provider. */
		boolean isAccountExists = signatureInformationRepository.existsByUser_IdAndAuthProvider_Id(user.getId(), authProvider.getId());
		if (isAccountExists) {
			throw new DatabaseRelatedException("The user account with given auth provider already exists.");
		}
		
		/* Check if the email described in SignUpRequestDTO equals to email of existing user information.
		 * They should equal to each other. */
		if (!signUpRequestDTO.getEmail().equals(user.getEmail())) {
			signUpResponseDTO.setSuccess(false);
			signUpResponseDTO.setMessage("New account should have same email with existing account's.");
			return signUpResponseDTO;
		}
		
		/* Create a new SignatureInformation entity. */
		SignatureInformation signatureInformation = new SignatureInformation();
		signatureInformation.setProviderUserId(signUpRequestDTO.getProviderUserId());
		signatureInformation.setPasskey(passwordEncoder.encode(signUpRequestDTO.getPassword()));
		
		/* Create some referential relationships among entities. */
		signatureInformation.setUser(user);
		signatureInformation.setAuthProvider(authProvider);
		user.addSignatureInformation(signatureInformation);
		authProvider.addSignatureInformation(signatureInformation);
		
		/* Save the data in database. */
		signatureInformationRepository.save(signatureInformation);
		
		signUpResponseDTO.setSuccess(true);
		return signUpResponseDTO;
	}
	
	/* PostWriting
	 * - Input: PostWritingRequestDTO, jwtToken
	 * - Output: postWritingResponseDTO
	 * Check if given writing content has length exceeding 150 characters.
	 * If it is, reject the request, and if not, retrieve user id from Jwt token and save the content in database.
	 */
	@Transactional(rollbackFor = DatabaseRelatedException.class)
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
		Optional <User> userNullable = userRepository.findById(jwtService.JwtGetUserId(jwtToken));
		if (!userNullable.isPresent()) {
			throw new DatabaseRelatedException("The user id retrieved from Jwt token is not valid.");
		}
		User user = userNullable.get();
		
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
		
		/* Construct the postWritingResponseDTO object and return it. */
		postWritingResponseDTO.setSuccess(true);
		postWritingResponseDTO.setContent(postWritingRequestDTO.getContent());
		postWritingResponseDTO.setPostTime(now);
		
		return postWritingResponseDTO;
	}
	
	/* PostComment
	 * - Input: postCommentRequestDTO, jwtToken
	 * - Output: postCommentResponseDTO
	 * Save the given comment for given writing in database.
	 */
	@Transactional(rollbackFor = DatabaseRelatedException.class)
	public PostCommentResponseDTO PostComment(PostCommentRequestDTO postCommentRequestDTO, String jwtToken) {
		/* Create a new postCommentResponseDTO. */
		PostCommentResponseDTO postCommentResponseDTO = new PostCommentResponseDTO();
		
		/* Retrieve user id from Jwt token, and get the User entity with that user id out of the database. 
		 * And also get the Writing entity out of the database. */
		Optional <User> userNullable = userRepository.findById(jwtService.JwtGetUserId(jwtToken));
		Optional <Writing> writingNullable = writingRepository.findById(postCommentRequestDTO.getWritingId());
		if (!userNullable.isPresent()) {
			throw new DatabaseRelatedException("The user id retrieved from Jwt token is invalid. Please sign in again.");
		}
		if (!writingNullable.isPresent()) {
			throw new DatabaseRelatedException("The post id is invalid. It would be deleted.");
		}
		User user = userNullable.get();
		Writing writing = writingNullable.get();
		
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
		
		/* Construct the PostCommentResponseDTO object and return it. */
		postCommentResponseDTO.setSuccess(true);
		postCommentResponseDTO.setWritingId(postCommentRequestDTO.getWritingId());
		postCommentResponseDTO.setContent(postCommentRequestDTO.getContent());
		postCommentResponseDTO.setPostTime(now);
		
		return postCommentResponseDTO;
	}
	
	/* Follow
	 * - Input: FollowRequestDTO, jwtToken
	 * - Output: FollowResponseDTO
	 * Save the information about following relationship,
	 * more specifically, the information that the user described in jwtToken follows the user described in request DTO in database.
	 */
	@Transactional(rollbackFor = DatabaseRelatedException.class)
	public FollowResponseDTO Follow(FollowRequestDTO followRequestDTO, String jwtToken) {
		/* Create a new FollowResponseDTO object. */
		FollowResponseDTO followResponseDTO = new FollowResponseDTO();
		
		/* Retrieve user id from Jwt token and get the User entity for that user id.
		 * And also get the target User's entity. */
		Optional <User> followerNullable = userRepository.findById(jwtService.JwtGetUserId(jwtToken));
		Optional <User> followeeNullable = userRepository.findById(followRequestDTO.getTargetId());
		if (!followerNullable.isPresent()) {
			throw new DatabaseRelatedException("The user id retrieved from Jwt token is invalid. Please sign in again.");
		}
		if (!followeeNullable.isPresent()) {
			throw new DatabaseRelatedException("The id of user to follow is invalid.");
		}
		User follower = followerNullable.get();
		User followee = followeeNullable.get();
		
		/* A user cannot follow or unfollow him/herself. */
		if (follower.getId( ) == followee.getId()) {
			throw new DatabaseRelatedException("A user cannot follow or unfollow him/herself.");
		}
		
		/* A user cannot follow a user who already following. */
		boolean isAlreadyFollowing = followingRepository.existsByFollower_IdAndFollowee_Id(follower.getId(), followee.getId());
		if (isAlreadyFollowing) {
			throw new DatabaseRelatedException("A user cannot follow a user who already following.");
		}
		
		/* Create a new Following entity. */
		Following following = new Following();
		
		/* Create referential relationships among entities. */
		following.setFollower(follower);
		following.setFollowee(followee);
		follower.addFollowing(following);
		followee.addFollower(following);
		
		/* Save the data in database. */
		followingRepository.save(following);
		
		/* Construct the FollowResponseDTO object and return it. */
		followResponseDTO.setSuccess(true);
		
		return followResponseDTO;
	}
	
	/* Unfollow
	 * - Input: FollowRequestDTO, jwtToken
	 * - Output: FollowResponseDTO
	 * Delete the following information from the database.
	 */
	@Transactional(rollbackFor = DatabaseRelatedException.class)
	public FollowResponseDTO Unfollow(FollowRequestDTO followRequestDTO, String jwtToken) {
		/* Create a new FollowResponseDTO object. */
		FollowResponseDTO followResponseDTO = new FollowResponseDTO();
		
		/* Get the User entities. */
		Optional <User> followerNullable = userRepository.findById(jwtService.JwtGetUserId(jwtToken));
		Optional <User> followeeNullable = userRepository.findById(followRequestDTO.getTargetId());
		if (!followerNullable.isPresent()) {
			throw new DatabaseRelatedException("The user id retrieved from Jwt token is invalid. Please sign in again.");
		}
		if (!followeeNullable.isPresent()) {
			throw new DatabaseRelatedException("The id of user to unfollow is invalid.");
		}
		
		User follower = followerNullable.get();
		User followee = followeeNullable.get();
		
		/* A user cannot follow or unfollow him/herself. */
		if (follower.getId( )== followee.getId()) {
			throw new DatabaseRelatedException("A user cannot follow or unfollow him/herself.");
		}
		
		/* Get the Following entity. */
		Optional <Following> followingNullable = followingRepository.findByFollower_IdAndFollowee_Id(follower.getId(), followee.getId());
		if (!followingNullable.isPresent()) {
			throw new DatabaseRelatedException("It is impossible to unfollow a user who has not been followed.");
		}
		Following following = followingNullable.get();
		
		/* Delete the following information. */
		follower.removeFollowing(following);
		followee.removeFollower(following);
		followingRepository.delete(following);
		
		/* Construct the FollowResponseDTO and return it. */
		followResponseDTO.setSuccess(true);
		
		return followResponseDTO;
	}
	
	/* GetFollower
	 * - Input: jwtToken
	 * - Output: List<FollowingInformationDTO>
	 * Get the list of informations of users who are following the user described in jwtToken.
	 */
	@Transactional(rollbackFor = DatabaseRelatedException.class)
	public List<UserInformationDTO> GetFollower(String jwtToken) {
		/* Retrieve user id from Jwt token and get the User entity for that user id. */
		Optional<User> userNullable = userRepository.findById(jwtService.JwtGetUserId(jwtToken));
		if (!userNullable.isPresent()) {
			throw new DatabaseRelatedException("The user id retrieved from Jwt token is invalid. Please sign in again.");
		}
		User user = userNullable.get();
		
		/* Get a list of Following entities in which this user is a followee. */
		List<Following> followerList = user.getFollowerList();
		
		/* Convert the list of Following entities into a list of User entities who are following this user. */
		List<User> userList = followerList.stream()
				.map(followingEntity -> followingEntity.getFollower())
				.collect(Collectors.toList());
		
		/* Convert the list of User entities into a list of FollowingInformationDTO objects and return it. */
		List<UserInformationDTO> userInformationDTOList = userList.stream()
				.map(userEntity -> new UserInformationDTO(userEntity.getId(), userEntity.getEmail(), userEntity.getNickname()))
				.collect(Collectors.toList());
		
		return userInformationDTOList;
	}
	
	/* GetFollowing
	 * - Input: jwtToken
	 * - Output: List<FollowingInformationDTO>
	 * Get the list of informations of users who are followed by the user described in jwtToken.
	 */
	@Transactional(rollbackFor = DatabaseRelatedException.class)
	public List<UserInformationDTO> GetFollowing(String jwtToken) {
		/* Retrieve user id from Jwt token and get the User entity for that user id. */
		Optional<User> userNullable = userRepository.findById(jwtService.JwtGetUserId(jwtToken));
		if (!userNullable.isPresent()) {
			throw new DatabaseRelatedException("The user id retrieved from Jwt token is invalid. Please sign in again.");
		}
		User user = userNullable.get();
		
		/* Get a list of Following entities in which this user is a follower. */
		List<Following> followingList = user.getFollowingList();
		
		/* Convert the list of Following entities into a list of User entities who are followed by this user. */
		List<User> userList = followingList.stream()
				.map(followingEntity -> followingEntity.getFollowee())
				.collect(Collectors.toList());
		
		/* Convert the list of User entities into a list of FollowingInformationDTO objects and return it. */
		List<UserInformationDTO> userInformationDTOList = userList.stream()
				.map(userEntity -> new UserInformationDTO(userEntity.getId(), userEntity.getEmail(), userEntity.getNickname()))
				.collect(Collectors.toList());
		
		return userInformationDTOList;
	}
	
	/* GetTimeline
	 * - Input: jwtToken
	 * - Output: List<WritingInformationDTO>
	 * Return a list of WritingInformationDTOs posted by users who are followed by the user described in jwtToken.
	 * The WritingInformationDTOs are sorted in descending order with the time they are created,
	 * which means that they are listed from newest to oldest.
	 * The lists of comments included in the WritingInformationDTOs(if exist) are sorted in ascending order.
	 */
	@Transactional(readOnly = true)
	public List<WritingInformationDTO> GetTimeline(String jwtToken){
		/* Retrieve user id from Jwt token and get the User entity for that user id. */
		Optional<User> userNullable = userRepository.findById(jwtService.JwtGetUserId(jwtToken));
		if (!userNullable.isPresent()) {
			throw new DatabaseRelatedException("The user id retrieved from Jwt token is invalid. Please sign in again.");
		}
		User user = userNullable.get();
		
		/* Get a list of Following entities in which this user is a follower. */
		List<Following> followingList = user.getFollowingList();
		
		/* Convert the list of Following entities into a list of User entities who are followed by this user. */
		List<User> userList = followingList.stream()
				.map(followingEntity -> followingEntity.getFollowee())
				.collect(Collectors.toList());
		
		/* Convert the list of User entities into a list of lists which have Writing entities. */
		List<List<Writing>> listOfWritingLists = userList.stream()
				.map(userEntity -> userEntity.getWritingList())
				.collect(Collectors.toList());
		
		/* Merge the lists of Writing entities. */
		List<Writing> writingList = new ArrayList<>();
		for (List<Writing> oneWritingList : listOfWritingLists) {
			writingList.addAll(oneWritingList);
		}
		
		/* Convert the list of Writing entities into a list of WritingInformationDTO objects. */
		List<WritingInformationDTO> writingInformationDTOList =
				writingList.stream()
				.map(writingEntity -> new WritingInformationDTO(
						writingEntity.getId(),
						new UserInformationDTO(writingEntity.getUser().getId(), writingEntity.getUser().getEmail(), writingEntity.getUser().getNickname()),
						writingEntity.getContent(),
						writingEntity.getCreatedAt(),
						writingEntity.getCommentList().stream()
							.map(commentEntity -> new CommentInformationDTO(
									commentEntity.getId(),
									new UserInformationDTO(commentEntity.getUser().getId(), commentEntity.getUser().getEmail(), commentEntity.getUser().getNickname()),
									commentEntity.getContent(),
									commentEntity.getCreatedAt()))
							.collect(Collectors.toList())))
				.collect(Collectors.toList());
		
		/* Sort the list of WritingInformationDTO objects with 'createdAt' attribute as a key in descending order and return it. */
		writingInformationDTOList.sort((writing1, writing2) -> writing2.getCreatedAt().compareTo(writing1.getCreatedAt()));
		
		return writingInformationDTOList;
	}
}
