package me.timeline;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import me.timeline.dto.CommentInformationDTO;
import me.timeline.dto.ExceptionDTO;
import me.timeline.dto.FollowRequestDTO;
import me.timeline.dto.FollowResponseDTO;
import me.timeline.dto.PostCommentRequestDTO;
import me.timeline.dto.PostCommentResponseDTO;
import me.timeline.dto.PostWritingRequestDTO;
import me.timeline.dto.PostWritingResponseDTO;
import me.timeline.dto.SignInRequestDTO;
import me.timeline.dto.SignInResponseDTO;
import me.timeline.dto.SignUpRequestDTO;
import me.timeline.dto.SignUpResponseDTO;
import me.timeline.dto.UserInformationDTO;
import me.timeline.dto.WritingInformationDTO;
import me.timeline.entity.AuthProvider;
import me.timeline.entity.SignatureInformation;
import me.timeline.entity.User;
import me.timeline.exception.DatabaseRelatedException;
import me.timeline.repository.AuthProviderRepository;
import me.timeline.repository.CommentRepository;
import me.timeline.repository.FollowingRepository;
import me.timeline.repository.SignatureInformationRepository;
import me.timeline.repository.UserRepository;
import me.timeline.repository.WritingRepository;
import me.timeline.service.TimelineService;

/* Test TimelineService.
 * JwtToken validation test is omitted because it is tested in TimelineControllerTests.
 * Run a SpringBootTest and refresh the database between each tests.
 * */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimelineServiceTests {
	
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
	TimelineService timelineService;
	
	/*
	 * Test SignUp.
	 */
	@Test
	@Order(1)
	public void SignUpTest() throws Exception {
		/* Create test data and expected results. */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "None", 0);
		SignUpRequestDTO signUpRequestDTO2 = new SignUpRequestDTO("Test@test.com", "TEST2", "TestPassword2", "None", 0);
		SignUpRequestDTO signUpRequestDTO3 = new SignUpRequestDTO("Test3@test.com", "TEST", "TestPassword3", "None", 0);
		SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO(true, "");
		SignUpResponseDTO signUpResponseDTO2 = new SignUpResponseDTO(false, "Given email is duplicated. If you want to link your accounts, sign in and try linking account.");
		SignUpResponseDTO signUpResponseDTO3 = new SignUpResponseDTO(false, "Given nickname is duplicated.");
		
		
		/* Check if responses are same as expected. */
		SignUpResponseDTO signUpTestResponseDTO = timelineService.SignUp(signUpRequestDTO);
		SignUpResponseDTO signUpTestResponseDTO2 = timelineService.SignUp(signUpRequestDTO2);
		SignUpResponseDTO signUpTestResponseDTO3 = timelineService.SignUp(signUpRequestDTO3);
		
		assertThat(signUpTestResponseDTO.getSuccess(), is(signUpResponseDTO.getSuccess()));
		assertThat(signUpTestResponseDTO.getMessage(), is(signUpResponseDTO.getMessage()));
		assertThat(signUpTestResponseDTO2.getSuccess(), is(signUpResponseDTO2.getSuccess()));
		assertThat(signUpTestResponseDTO2.getMessage(), is(signUpResponseDTO2.getMessage()));
		assertThat(signUpTestResponseDTO3.getSuccess(), is(signUpResponseDTO3.getSuccess()));
		assertThat(signUpTestResponseDTO3.getMessage(), is(signUpResponseDTO3.getMessage()));
		
		/* Check if data are stored in database or rejected well. */
		boolean isStored = userRepository.existsByEmailAndNickname(signUpRequestDTO.getEmail(), signUpRequestDTO.getNickname());
		boolean isStored2 = userRepository.existsByEmailAndNickname(signUpRequestDTO2.getEmail(), signUpRequestDTO2.getNickname());
		boolean isStored3 = userRepository.existsByEmailAndNickname(signUpRequestDTO3.getEmail(), signUpRequestDTO3.getNickname());
		
		assertThat(isStored, is(true));
		assertThat(isStored2, is(false));
		assertThat(isStored3, is(false));
	}
	
	/*
	 * Test SignIn.
	 */
	@Test
	@Order(2)
	public void SignInTest() throws Exception {
		/* First create a sign up data for test. */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "None", 0);
		timelineService.SignUp(signUpRequestDTO);
		
		/* Create test data and expected results. */
		SignInRequestDTO signInRequestDTO = new SignInRequestDTO("Test@test.com", "TestPassword", "None");
		SignInRequestDTO signInRequestDTO2 = new SignInRequestDTO("Test2@test.com", "TestPassword", "None");
		SignInRequestDTO signInRequestDTO3 = new SignInRequestDTO("Test@test.com", "TestPassword2", "None");
		SignInRequestDTO signInRequestDTO4 = new SignInRequestDTO("Test@test.com", "TestPassword", "Facebook");
		SignInResponseDTO signInResponseDTO = new SignInResponseDTO(true, null);
		SignInResponseDTO signInResponseDTO2 = new SignInResponseDTO(false, null);
		SignInResponseDTO signInResponseDTO3 = new SignInResponseDTO(false, null);
		SignInResponseDTO signInResponseDTO4 = new SignInResponseDTO(false, null);
		
		/* Check if responses are same as expected. */
		SignInResponseDTO signInTestResponseDTO = timelineService.SignIn(signInRequestDTO);
		SignInResponseDTO signInTestResponseDTO2 = timelineService.SignIn(signInRequestDTO2);
		SignInResponseDTO signInTestResponseDTO3 = timelineService.SignIn(signInRequestDTO3);
		SignInResponseDTO signInTestResponseDTO4 = timelineService.SignIn(signInRequestDTO4);
		
		assertThat(signInTestResponseDTO.getSuccess(), is(signInResponseDTO.getSuccess()));
		assertThat(signInTestResponseDTO.getJwtToken(), is(notNullValue()));
		assertThat(signInTestResponseDTO2.getSuccess(), is(signInResponseDTO2.getSuccess()));
		assertThat(signInTestResponseDTO2.getJwtToken(), is(nullValue()));
		assertThat(signInTestResponseDTO3.getSuccess(), is(signInResponseDTO3.getSuccess()));
		assertThat(signInTestResponseDTO3.getJwtToken(), is(nullValue()));
		assertThat(signInTestResponseDTO4.getSuccess(), is(signInResponseDTO4.getSuccess()));
		assertThat(signInTestResponseDTO4.getJwtToken(), is(nullValue()));
	}
	
	/*
	 * Test LinkAccount.
	 */
	@Test
	@Order(3)
	public void LinkAccountTest() throws Exception {
		/* First create a sign up data for test. */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "None", 0);
		timelineService.SignUp(signUpRequestDTO);
		
		/* Get a jwtToken through SignIn for test. */
		String jwtToken = timelineService.SignIn(new SignInRequestDTO("Test@test.com", "TestPassword", "None")).getJwtToken();
		
		/* Create test data and expected results. */
		SignUpRequestDTO signUpRequestDTO2 = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "Facebook", 0);
		SignUpRequestDTO signUpRequestDTO3 = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "Facebook", 0);
		SignUpRequestDTO signUpRequestDTO4 = new SignUpRequestDTO("Test2@test.com", "TEST", "TestPassword", "Kakaotalk", 0);
		SignUpResponseDTO signUpResponseDTO2 = new SignUpResponseDTO(true, "");
		ExceptionDTO signUpResponseDTO3 = new ExceptionDTO(500, "DatabaseRelatedException", "The user account with given auth provider already exists.");
		SignUpResponseDTO signUpResponseDTO4 = new SignUpResponseDTO(false, "New account should have same email with existing account's.");

		/* Check if responses are same as expected. */
		SignUpResponseDTO signUpTestResponse2 = timelineService.LinkAccount(signUpRequestDTO2, jwtToken);
		Throwable exception = assertThrows(DatabaseRelatedException.class, () -> 
	    		timelineService.LinkAccount(signUpRequestDTO3, jwtToken)
		);
		SignUpResponseDTO signUpTestResponse4 = timelineService.LinkAccount(signUpRequestDTO4, jwtToken);
		
		assertThat(signUpTestResponse2.getSuccess(), is(signUpResponseDTO2.getSuccess()));
		assertThat(exception.getMessage(), is(signUpResponseDTO3.getMessage()));
		assertThat(signUpTestResponse4.getSuccess(), is(signUpResponseDTO4.getSuccess()));
		assertThat(signUpTestResponse4.getMessage(), is(signUpResponseDTO4.getMessage()));
		
		/* Check if data are stored in database or rejected well. */
		User user = userRepository.findByEmail("Test@test.com").get();
		AuthProvider authProvider = authProviderRepository.findByType("None").get();
		AuthProvider authProvider2 = authProviderRepository.findByType("Facebook").get();
		AuthProvider authProvider3 = authProviderRepository.findByType("Kakaotalk").get();
		boolean isStored = signatureInformationRepository.existsByUser_IdAndAuthProvider_Id(user.getId(), authProvider.getId());
		boolean isStored2 = signatureInformationRepository.existsByUser_IdAndAuthProvider_Id(user.getId(), authProvider2.getId());
		boolean isStored3 = signatureInformationRepository.existsByUser_IdAndAuthProvider_Id(user.getId(), authProvider3.getId());
		
		assertThat(isStored, is(true));
		assertThat(isStored2, is(true));
		assertThat(isStored3, is(false));
	}
	
	/*
	 * Test PostWriting.
	 */
	@Test
	@Order(4)
	public void PostWritingTest() throws Exception {
		/* First create a sign up data for test. */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "None", 0);
		timelineService.SignUp(signUpRequestDTO);
		
		/* Get a jwtToken through SignIn for test. */
		String jwtToken = timelineService.SignIn(new SignInRequestDTO("Test@test.com", "TestPassword", "None")).getJwtToken();
		
		/* Create test data and expected results. */
		PostWritingRequestDTO postWritingRequestDTO = new PostWritingRequestDTO("This is a test writing#1.");
		PostWritingRequestDTO postWritingRequestDTO2 = new PostWritingRequestDTO
				("한글로 150자..............................................................................................................................................");
		PostWritingRequestDTO postWritingRequestDTO3 = new PostWritingRequestDTO
				("한글로 151자...............................................................................................................................................");
		PostWritingResponseDTO postWritingResponseDTO = new PostWritingResponseDTO(true, "This is a test writing#1.", null);
		PostWritingResponseDTO postWritingResponseDTO2 = new PostWritingResponseDTO(true, "한글로 150자..............................................................................................................................................", null);
		PostWritingResponseDTO postWritingResponseDTO3 = new PostWritingResponseDTO(false, "", null);
		
		/* Check if responses are same as expected. */
		PostWritingResponseDTO postWritingTestResponseDTO = timelineService.PostWriting(postWritingRequestDTO, jwtToken);
		PostWritingResponseDTO postWritingTestResponseDTO2 = timelineService.PostWriting(postWritingRequestDTO2, jwtToken);
		PostWritingResponseDTO postWritingTestResponseDTO3 = timelineService.PostWriting(postWritingRequestDTO3, jwtToken);
		
		assertThat(postWritingTestResponseDTO.getSuccess(), is(postWritingResponseDTO.getSuccess()));
		assertThat(postWritingTestResponseDTO.getContent(), is(postWritingResponseDTO.getContent()));
		assertThat(postWritingTestResponseDTO2.getSuccess(), is(postWritingResponseDTO2.getSuccess()));
		assertThat(postWritingTestResponseDTO2.getContent(), is(postWritingResponseDTO2.getContent()));
		assertThat(postWritingTestResponseDTO3.getSuccess(), is(postWritingResponseDTO3.getSuccess()));
		
		/* Check if data are stored in database or rejected well. */
		User user = userRepository.findByEmail("Test@test.com").get();
		boolean isStored = writingRepository.existsByUser_IdAndContent(user.getId(), postWritingRequestDTO.getContent());
		boolean isStored2 = writingRepository.existsByUser_IdAndContent(user.getId(), postWritingRequestDTO2.getContent());
		boolean isStored3 = writingRepository.existsByUser_IdAndContent(user.getId(), postWritingRequestDTO3.getContent());
		
		assertThat(isStored, is(true));
		assertThat(isStored2, is(true));
		assertThat(isStored3, is(false));
	}
	
	/*
	 * Test PostComment.
	 */
	@Test
	@Order(5)
	public void PostCommentTest() throws Exception {
		/* First create a sign up data for test. */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "None", 0);
		timelineService.SignUp(signUpRequestDTO);
		
		/* Get a jwtToken through SignIn for test. */
		String jwtToken = timelineService.SignIn(new SignInRequestDTO("Test@test.com", "TestPassword", "None")).getJwtToken();
		
		/* Create a writing data for test. */
		User user = userRepository.findByEmail("Test@test.com").get();
		PostWritingRequestDTO postWritingRequestDTO = new PostWritingRequestDTO("This is a test writing#1.");
		timelineService.PostWriting(postWritingRequestDTO, jwtToken);
		int writingId = writingRepository.findByUser_IdAndContent(user.getId(), postWritingRequestDTO.getContent()).get().getId();
		
		/* Create test data and expected results. */
		PostCommentRequestDTO postCommentRequestDTO = new PostCommentRequestDTO(writingId, "This is a test comment#1 on writing#1.");
		PostCommentRequestDTO postCommentRequestDTO2 = new PostCommentRequestDTO(100, "This is a test comment#1 on invalid writing#1.");
		PostCommentResponseDTO postCommentResponseDTO = new PostCommentResponseDTO(true, 1, "This is a test comment#1 on writing#1.", null);
		ExceptionDTO postCommentResponseDTO2 = new ExceptionDTO(500, "DatabaseRelatedException", "The post id is invalid. It would be deleted.");
		
		/* Check if responses are same as expected. */
		PostCommentResponseDTO postCommentTestResponseDTO = timelineService.PostComment(postCommentRequestDTO, jwtToken);
		Throwable exception = assertThrows(DatabaseRelatedException.class, () -> 
			timelineService.PostComment(postCommentRequestDTO2, jwtToken)
		);
		
		assertThat(postCommentTestResponseDTO.getSuccess(), is(postCommentResponseDTO.getSuccess()));
		assertThat(postCommentTestResponseDTO.getWritingId(), is(postCommentResponseDTO.getWritingId()));
		assertThat(postCommentTestResponseDTO.getContent(), is(postCommentResponseDTO.getContent()));
		assertThat(exception.getMessage(), is(postCommentResponseDTO2.getMessage()));
		
		/* Check if data are stored in database or rejected well. */
		boolean isStored = commentRepository.existsByWriting_IdAndContent(postCommentRequestDTO.getWritingId(), postCommentRequestDTO.getContent());
		boolean isStored2 = commentRepository.existsByWriting_IdAndContent(postCommentRequestDTO2.getWritingId(), postCommentRequestDTO2.getContent());
		
		assertThat(isStored, is(true));
		assertThat(isStored2, is(false));
	}
	
	/*
	 * Test Follow.
	 */
	@Test
	@Order(6)
	public void FollowTest() throws Exception {
		/* First create some sign up data for test. */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "None", 0);
		SignUpRequestDTO signUpRequestDTO2 = new SignUpRequestDTO("Test2@test.com", "TEST2", "TestPassword2", "None", 0);
		
		timelineService.SignUp(signUpRequestDTO);
		timelineService.SignUp(signUpRequestDTO2);
		
		/* Get a jwtToken through SignIn for test. */
		String jwtToken = timelineService.SignIn(new SignInRequestDTO("Test@test.com", "TestPassword", "None")).getJwtToken();
		
		/* Get the Users' data. */
		User user = userRepository.findByEmail(signUpRequestDTO.getEmail()).get();
		User user2 = userRepository.findByEmail(signUpRequestDTO2.getEmail()).get();
		
		/* Create test data and expected results. */
		FollowRequestDTO followRequestDTO = new FollowRequestDTO(user2.getId());
		FollowRequestDTO followRequestDTO2 = new FollowRequestDTO(user.getId());
		FollowRequestDTO followRequestDTO3 = new FollowRequestDTO(user2.getId());
		FollowRequestDTO followRequestDTO4 = new FollowRequestDTO(100);
		FollowResponseDTO followResponseDTO = new FollowResponseDTO(true);
		ExceptionDTO followResponseDTO2 = new ExceptionDTO(500, "DatabaseRelatedException", "A user cannot follow or unfollow him/herself.");
		ExceptionDTO followResponseDTO3 = new ExceptionDTO(500, "DatabaseRelatedException", "A user cannot follow a user who already following.");
		ExceptionDTO followResponseDTO4 = new ExceptionDTO(500, "DatabaseRelatedException", "The id of user to follow is invalid.");
		
		/* Check if responses are same as expected. */
		FollowResponseDTO followTestResponseDTO = timelineService.Follow(followRequestDTO, jwtToken);
		Throwable followTestResponseDTO2 = assertThrows(DatabaseRelatedException.class, () ->
			timelineService.Follow(followRequestDTO2, jwtToken)
		);
		Throwable followTestResponseDTO3 = assertThrows(DatabaseRelatedException.class, () ->
			timelineService.Follow(followRequestDTO3, jwtToken)
		);
		Throwable followTestResponseDTO4 = assertThrows(DatabaseRelatedException.class, () ->
			timelineService.Follow(followRequestDTO4, jwtToken)
		);
		
		assertThat(followTestResponseDTO.getSuccess(), is(followResponseDTO.getSuccess()));
		assertThat(followTestResponseDTO2.getMessage(), is(followResponseDTO2.getMessage()));
		assertThat(followTestResponseDTO3.getMessage(), is(followResponseDTO3.getMessage()));
		assertThat(followTestResponseDTO4.getMessage(), is(followResponseDTO4.getMessage()));
		
		/* Check if data are stored in database or rejected well. */
		boolean isStored = followingRepository.existsByFollower_IdAndFollowee_Id(user.getId(), user2.getId());
		boolean isStored2 = followingRepository.existsByFollower_IdAndFollowee_Id(user.getId(), user.getId());
		boolean isStored3 = followingRepository.existsByFollower_IdAndFollowee_Id(user.getId(), 100);
		
		assertThat(isStored, is(true));
		assertThat(isStored2, is(false));
		assertThat(isStored3, is(false));
	}
	
	/*
	 * Test Unfollow.
	 */
	@Test
	@Order(7)
	public void UnfollowTest() throws Exception {
		/* First create some sign up data for test. */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "None", 0);
		SignUpRequestDTO signUpRequestDTO2 = new SignUpRequestDTO("Test2@test.com", "TEST2", "TestPassword2", "None", 0);
		
		timelineService.SignUp(signUpRequestDTO);
		timelineService.SignUp(signUpRequestDTO2);
		
		/* Get a jwtToken through SignIn for test. */
		String jwtToken = timelineService.SignIn(new SignInRequestDTO("Test@test.com", "TestPassword", "None")).getJwtToken();
		
		/* Get the Users' data. */
		User user = userRepository.findByEmail(signUpRequestDTO.getEmail()).get();
		User user2 = userRepository.findByEmail(signUpRequestDTO2.getEmail()).get();
		
		/* Create a following data for test. */
		FollowRequestDTO followRequestDTO = new FollowRequestDTO(user2.getId());
		timelineService.Follow(followRequestDTO, jwtToken);
		
		/* Create test data and expected results. */
		FollowRequestDTO unfollowRequestDTO = new FollowRequestDTO(user2.getId());
		FollowRequestDTO unfollowRequestDTO2 = new FollowRequestDTO(user.getId());
		FollowRequestDTO unfollowRequestDTO3 = new FollowRequestDTO(user2.getId());
		FollowRequestDTO unfollowRequestDTO4 = new FollowRequestDTO(100);
		FollowResponseDTO unfollowResponseDTO = new FollowResponseDTO(true);
		ExceptionDTO unfollowResponseDTO2 = new ExceptionDTO(500, "DatabaseRelatedException", "A user cannot follow or unfollow him/herself.");
		ExceptionDTO unfollowResponseDTO3 = new ExceptionDTO(500, "DatabaseRelatedException", "It is impossible to unfollow a user who has not been followed.");
		ExceptionDTO unfollowResponseDTO4 = new ExceptionDTO(500, "DatabaseRelatedException", "The id of user to unfollow is invalid.");
		
		/* Check if responses are same as expected. */
		FollowResponseDTO unfollowTestResponseDTO = timelineService.Unfollow(unfollowRequestDTO, jwtToken);
		Throwable unfollowTestResponseDTO2 = assertThrows(DatabaseRelatedException.class, () ->
			timelineService.Unfollow(unfollowRequestDTO2, jwtToken)
		);
		Throwable unfollowTestResponseDTO3 = assertThrows(DatabaseRelatedException.class, () ->
			timelineService.Unfollow(unfollowRequestDTO3, jwtToken)
		);
		Throwable unfollowTestResponseDTO4 = assertThrows(DatabaseRelatedException.class, () ->
			timelineService.Unfollow(unfollowRequestDTO4, jwtToken)
		);
		
		assertThat(unfollowTestResponseDTO.getSuccess(), is(unfollowResponseDTO.getSuccess()));
		assertThat(unfollowTestResponseDTO2.getMessage(), is(unfollowResponseDTO2.getMessage()));
		assertThat(unfollowTestResponseDTO3.getMessage(), is(unfollowResponseDTO3.getMessage()));
		assertThat(unfollowTestResponseDTO4.getMessage(), is(unfollowResponseDTO4.getMessage()));
		
		/* Check if data are stored in database or rejected well. */
		boolean isStored = followingRepository.existsByFollower_IdAndFollowee_Id(user.getId(), user2.getId());
		
		assertThat(isStored, is(false));
	}
	
	/*
	 * Test GetFollower.
	 */
	@Test
	@Order(8)
	public void GetFollowerTest() throws Exception {
		/* First create some sign up data for test. */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "None", 0);
		SignUpRequestDTO signUpRequestDTO2 = new SignUpRequestDTO("Test2@test.com", "TEST2", "TestPassword2", "None", 0);
		SignUpRequestDTO signUpRequestDTO3 = new SignUpRequestDTO("Test3@test.com", "TEST3", "TestPassword3", "None", 0);
		timelineService.SignUp(signUpRequestDTO);
		timelineService.SignUp(signUpRequestDTO2);
		timelineService.SignUp(signUpRequestDTO3);
		
		/* Get a jwtTokens through SignIn for test. */
		String jwtToken = timelineService.SignIn(new SignInRequestDTO("Test@test.com", "TestPassword", "None")).getJwtToken();
		String jwtToken2 = timelineService.SignIn(new SignInRequestDTO("Test2@test.com", "TestPassword2", "None")).getJwtToken();
		String jwtToken3 = timelineService.SignIn(new SignInRequestDTO("Test3@test.com", "TestPassword3", "None")).getJwtToken();
		
		/* Get the Users' data. */
		User user = userRepository.findByEmail(signUpRequestDTO.getEmail()).get();
		User user2 = userRepository.findByEmail(signUpRequestDTO2.getEmail()).get();
		User user3 = userRepository.findByEmail(signUpRequestDTO3.getEmail()).get();
		
		/* Create some following data for test. */
		FollowRequestDTO followRequestDTO = new FollowRequestDTO(user.getId());
		timelineService.Follow(followRequestDTO, jwtToken2);
		timelineService.Follow(followRequestDTO, jwtToken3);
		
		/* Create expected results. */
		List<UserInformationDTO> expectedList = new ArrayList<>();
		expectedList.add(new UserInformationDTO(user2.getId(), user2.getEmail(), user2.getNickname()));
		expectedList.add(new UserInformationDTO(user3.getId(), user3.getEmail(), user3.getNickname()));
		expectedList.sort((userInformationDTO1, userInformationDTO2) -> Integer.compare(userInformationDTO1.getId(), userInformationDTO2.getId()));

		
		/* Check if response is same as expected. */
		List<UserInformationDTO> resultList = timelineService.GetFollower(jwtToken);
		resultList.sort((userInformationDTO1, userInformationDTO2) -> Integer.compare(userInformationDTO1.getId(), userInformationDTO2.getId()));
		
		assertThat(resultList.size(), is(expectedList.size()));
		for (int i = 0; i < resultList.size(); i++) {
			assertThat(resultList.get(i).getId(), is(expectedList.get(i).getId()));
			assertThat(resultList.get(i).getEmail(), is(expectedList.get(i).getEmail()));
			assertThat(resultList.get(i).getNickname(), is(expectedList.get(i).getNickname()));
		}
	}
	
	/*
	 * Test GetFollowing.
	 */
	@Test
	@Order(9)
	public void GetFollowingTest() throws Exception {
		/* First create some sign up data for test. */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "None", 0);
		SignUpRequestDTO signUpRequestDTO2 = new SignUpRequestDTO("Test2@test.com", "TEST2", "TestPassword2", "None", 0);
		SignUpRequestDTO signUpRequestDTO3 = new SignUpRequestDTO("Test3@test.com", "TEST3", "TestPassword3", "None", 0);
		timelineService.SignUp(signUpRequestDTO);
		timelineService.SignUp(signUpRequestDTO2);
		timelineService.SignUp(signUpRequestDTO3);
		
		/* Get a jwtTokens through SignIn for test. */
		String jwtToken = timelineService.SignIn(new SignInRequestDTO("Test@test.com", "TestPassword", "None")).getJwtToken();
		
		/* Get the Users' data. */
		User user2 = userRepository.findByEmail(signUpRequestDTO2.getEmail()).get();
		User user3 = userRepository.findByEmail(signUpRequestDTO3.getEmail()).get();
		
		/* Create some following data for test. */
		FollowRequestDTO followRequestDTO = new FollowRequestDTO(user2.getId());
		FollowRequestDTO followRequestDTO2 = new FollowRequestDTO(user3.getId());
		timelineService.Follow(followRequestDTO, jwtToken);
		timelineService.Follow(followRequestDTO2, jwtToken);
		
		/* Create expected results. */
		List<UserInformationDTO> expectedList = new ArrayList<>();
		expectedList.add(new UserInformationDTO(user2.getId(), user2.getEmail(), user2.getNickname()));
		expectedList.add(new UserInformationDTO(user3.getId(), user3.getEmail(), user3.getNickname()));
		expectedList.sort((userInformationDTO1, userInformationDTO2) -> Integer.compare(userInformationDTO1.getId(), userInformationDTO2.getId()));

		
		/* Check if response is same as expected. */
		List<UserInformationDTO> resultList = timelineService.GetFollowing(jwtToken);
		resultList.sort((userInformationDTO1, userInformationDTO2) -> Integer.compare(userInformationDTO1.getId(), userInformationDTO2.getId()));
		
		assertThat(resultList.size(), is(expectedList.size()));
		for (int i = 0; i < resultList.size(); i++) {
			assertThat(resultList.get(i).getId(), is(expectedList.get(i).getId()));
			assertThat(resultList.get(i).getEmail(), is(expectedList.get(i).getEmail()));
			assertThat(resultList.get(i).getNickname(), is(expectedList.get(i).getNickname()));
		}
	}
	
	/*
	 * Test GetTimeline.
	 */
	@Test
	@Order(10)
	public void GetTimelineTest() throws Exception {
		/* First create some sign up data for test. */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "None", 0);
		SignUpRequestDTO signUpRequestDTO2 = new SignUpRequestDTO("Test2@test.com", "TEST2", "TestPassword2", "None", 0);
		SignUpRequestDTO signUpRequestDTO3 = new SignUpRequestDTO("Test3@test.com", "TEST3", "TestPassword3", "None", 0);
		timelineService.SignUp(signUpRequestDTO);
		timelineService.SignUp(signUpRequestDTO2);
		timelineService.SignUp(signUpRequestDTO3);
		
		/* Get a jwtTokens through SignIn for test. */
		String jwtToken = timelineService.SignIn(new SignInRequestDTO("Test@test.com", "TestPassword", "None")).getJwtToken();
		String jwtToken2 = timelineService.SignIn(new SignInRequestDTO("Test2@test.com", "TestPassword2", "None")).getJwtToken();
		String jwtToken3 = timelineService.SignIn(new SignInRequestDTO("Test3@test.com", "TestPassword3", "None")).getJwtToken();
		
		/* Get the Users' data. */
		User user = userRepository.findByEmail(signUpRequestDTO.getEmail()).get();
		User user2 = userRepository.findByEmail(signUpRequestDTO2.getEmail()).get();
		User user3 = userRepository.findByEmail(signUpRequestDTO3.getEmail()).get();
		
		/* Create some following data for test. */
		FollowRequestDTO followRequestDTO = new FollowRequestDTO(user2.getId());
		FollowRequestDTO followRequestDTO2 = new FollowRequestDTO(user3.getId());
		timelineService.Follow(followRequestDTO, jwtToken);
		timelineService.Follow(followRequestDTO2, jwtToken);
		
		/* Create some writing data for test. */
		PostWritingRequestDTO postWritingRequestDTO = new PostWritingRequestDTO("User#2, Writing #1.");
		PostWritingRequestDTO postWritingRequestDTO2 = new PostWritingRequestDTO("User#2, Writing #2.");
		PostWritingRequestDTO postWritingRequestDTO3 = new PostWritingRequestDTO("User#3, Writing #1.");
		PostWritingResponseDTO postWritingResponseDTO = timelineService.PostWriting(postWritingRequestDTO, jwtToken2);
		PostWritingResponseDTO postWritingResponseDTO2 = timelineService.PostWriting(postWritingRequestDTO2, jwtToken2);
		PostWritingResponseDTO postWritingResponseDTO3 = timelineService.PostWriting(postWritingRequestDTO3, jwtToken3);
		
		/* Create some comment data for test. */
		PostCommentRequestDTO postCommentRequestDTO = new PostCommentRequestDTO(1, "For User#2, Writing#1, Comment #1 by User#1.");
		PostCommentRequestDTO postCommentRequestDTO2 = new PostCommentRequestDTO(1, "For User#2, Writing#1, Comment #2 by User#2.");
		PostCommentRequestDTO postCommentRequestDTO3 = new PostCommentRequestDTO(3, "For User#3, Writing#1, Comment #1. by User#2.");
		PostCommentResponseDTO postCommentResponseDTO = timelineService.PostComment(postCommentRequestDTO, jwtToken);
		PostCommentResponseDTO postCommentResponseDTO2 = timelineService.PostComment(postCommentRequestDTO2, jwtToken2);
		PostCommentResponseDTO postCommentResponseDTO3 = timelineService.PostComment(postCommentRequestDTO3, jwtToken2);
		
		/* Create expected results. */
		List<CommentInformationDTO> commentList = new ArrayList<>();
		List<CommentInformationDTO> commentList2 = new ArrayList<>();
		List<CommentInformationDTO> commentList3 = new ArrayList<>();
		commentList.add(new CommentInformationDTO(
				1,
				new UserInformationDTO(user.getId(), user.getEmail(), user.getNickname()),
				postCommentResponseDTO.getContent(),
				postCommentResponseDTO.getPostTime()));
		commentList.add(new CommentInformationDTO(
				2,
				new UserInformationDTO(user2.getId(), user2.getEmail(), user2.getNickname()),
				postCommentResponseDTO2.getContent(),
				postCommentResponseDTO2.getPostTime()));
		commentList3.add(new CommentInformationDTO(
				3,
				new UserInformationDTO(user2.getId(), user2.getEmail(), user2.getNickname()),
				postCommentResponseDTO3.getContent(),
				postCommentResponseDTO3.getPostTime()));
		
		commentList.sort((comment1, comment2) -> comment1.getCreatedAt().compareTo(comment2.getCreatedAt()));
		commentList2.sort((comment1, comment2) -> comment1.getCreatedAt().compareTo(comment2.getCreatedAt()));
		commentList3.sort((comment1, comment2) -> comment1.getCreatedAt().compareTo(comment2.getCreatedAt()));
		
		List<WritingInformationDTO> expectedList = new ArrayList<>();
		expectedList.add(new WritingInformationDTO(
				1,
				new UserInformationDTO(user2.getId(), user2.getEmail(), user2.getNickname()),
				postWritingResponseDTO.getContent(),
				postWritingResponseDTO.getPostTime(),
				commentList
				));
		expectedList.add(new WritingInformationDTO(
				2,
				new UserInformationDTO(user2.getId(), user2.getEmail(), user2.getNickname()),
				postWritingResponseDTO2.getContent(),
				postWritingResponseDTO2.getPostTime(),
				commentList2
				));
		expectedList.add(new WritingInformationDTO(
				3,
				new UserInformationDTO(user3.getId(), user3.getEmail(), user3.getNickname()),
				postWritingResponseDTO3.getContent(),
				postWritingResponseDTO3.getPostTime(),
				commentList3
				));
		
		expectedList.sort((writing1, writing2) -> writing2.getCreatedAt().compareTo(writing1.getCreatedAt()));
		
		/* Check if response is same as expected. */
		List<WritingInformationDTO> resultList = timelineService.GetTimeline(jwtToken);
		
		assertThat(resultList.size(), is(expectedList.size()));
		for (int i = 0; i < resultList.size(); i++) {
			assertThat(resultList.get(i).getId(), is(expectedList.get(i).getId()));
			assertThat(resultList.get(i).getContent(), is(expectedList.get(i).getContent()));
			
			UserInformationDTO resultWritingUserInformationDTO = resultList.get(i).getUserInformationDTO();
			UserInformationDTO expectedWritingUserInformationDTO = expectedList.get(i).getUserInformationDTO();
			assertThat(resultWritingUserInformationDTO.getId(), is(expectedWritingUserInformationDTO.getId()));
			assertThat(resultWritingUserInformationDTO.getEmail(), is(expectedWritingUserInformationDTO.getEmail()));
			assertThat(resultWritingUserInformationDTO.getNickname(), is(expectedWritingUserInformationDTO.getNickname()));
			
			List<CommentInformationDTO> resultCommentList = resultList.get(i).getCommentInformationDTOList();
			List<CommentInformationDTO> expectedCommentList = expectedList.get(i).getCommentInformationDTOList();
			assertThat(resultCommentList.size(), is(expectedCommentList.size()));
			for (int j = 0; j < resultCommentList.size(); j++) {
				assertThat(resultCommentList.get(j).getId(), is(expectedCommentList.get(j).getId()));
				assertThat(resultCommentList.get(j).getContent(), is(expectedCommentList.get(j).getContent()));
				
				UserInformationDTO resultCommentUserInformationDTO = resultCommentList.get(j).getUserInformationDTO();
				UserInformationDTO expectedCommentUserInformationDTO = expectedCommentList.get(j).getUserInformationDTO();
				assertThat(resultCommentUserInformationDTO.getId(), is(expectedCommentUserInformationDTO.getId()));
				assertThat(resultCommentUserInformationDTO.getEmail(), is(expectedCommentUserInformationDTO.getEmail()));
				assertThat(resultCommentUserInformationDTO.getNickname(), is(expectedCommentUserInformationDTO.getNickname()));
			}
		}
	}
}
