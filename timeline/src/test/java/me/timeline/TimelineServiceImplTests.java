package me.timeline;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.timeline.dto.SignUpRequestDTO;
import me.timeline.dto.SignUpResponseDTO;
import me.timeline.entity.AuthProvider;
import me.timeline.entity.SignatureInformation;
import me.timeline.entity.User;
import me.timeline.repository.AuthProviderRepository;
import me.timeline.repository.CommentRepository;
import me.timeline.repository.FollowingRepository;
import me.timeline.repository.SignatureInformationRepository;
import me.timeline.repository.UserRepository;
import me.timeline.repository.WritingRepository;
import me.timeline.service.TimelineServiceImpl;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimelineServiceImplTests {
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	SignatureInformationRepository signatureInformationRepository;
	
	@Mock
	AuthProviderRepository authProviderRepository;
	
	@Mock
	WritingRepository writingRepository;
	
	@Mock
	CommentRepository commentRepository;
	
	@Mock
	FollowingRepository followingRepository;
	
	@InjectMocks
	TimelineServiceImpl timelineServiceImpl;
	
	/*
	 * Test SignUp.
	 */
	@Test
	@Order(1)
	public void SignUpTest() throws Exception {
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "TEST", "TestPassword", "None", 0);
		
		when(userRepository.save(any(User.class))).thenReturn(new User());
		//when(signatureInformationRepository.save(any(SignatureInformation.class))).thenReturn(new SignatureInformation());
		//when(authProviderRepository.save(any(AuthProvider.class))).thenReturn(new AuthProvider());
		SignUpResponseDTO user = timelineServiceImpl.SignUp(signUpRequestDTO);
		//assertThat(user.getEmail()).isSameAs(signUpRequestDTO.getEmail());
		
	}
	
	/*
	 * Test SignIn.
	 */
	@Test
	@Order(2)
	public void SignInTest() throws Exception {
		
	}
	
	/*
	 * Test LinkAccount.
	 */
	@Test
	@Order(3)
	public void LinkAccountTest() throws Exception {
		
	}
	
	/*
	 * Test PostWriting.
	 */
	@Test
	@Order(4)
	public void PostWritingTest() throws Exception {
		
	}
	
	/*
	 * Test PostComment.
	 */
	@Test
	@Order(5)
	public void PostCommentTest() throws Exception {
		
	}
	
	/*
	 * Test Follow.
	 */
	@Test
	@Order(6)
	public void FollowTest() throws Exception {
		
	}
	
	/*
	 * Test Unfollow.
	 */
	@Test
	@Order(7)
	public void UnfollowTest() throws Exception {
		
	}
	
	/*
	 * Test GetFollower.
	 */
	@Test
	@Order(8)
	public void GetFollowerTest() throws Exception {
		
	}
	
	/*
	 * Test GetFollowing.
	 */
	@Test
	@Order(9)
	public void GetFollowingTest() throws Exception {
		
	}
	
	/*
	 * Test GetTimeline.
	 */
	@Test
	@Order(10)
	public void GetTimelineTest() throws Exception {
		
	}
}
