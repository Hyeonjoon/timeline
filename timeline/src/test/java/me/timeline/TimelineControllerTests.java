package me.timeline;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.timeline.controller.TimelineController;
import me.timeline.dto.FollowRequestDTO;
import me.timeline.dto.PostCommentRequestDTO;
import me.timeline.dto.PostWritingRequestDTO;
import me.timeline.dto.SignInRequestDTO;
import me.timeline.dto.SignInResponseDTO;
import me.timeline.dto.SignUpRequestDTO;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimelineControllerTests {
	@Autowired
	TimelineController timelineController;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	/*
	 * Test SignUp.
	 */
	@Test
	@Order(1)
	public void SignUpTest() throws Exception {
		
		/* 1.
		 * Expected result:
		 * 		success: true
		 * Test if SignUp works well with a input which should be succeed to sign up.
		 */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "Test", "TestPassword", "None", 0);
		SignUpRequestDTO signUpRequestDTO2 = new SignUpRequestDTO("Test2@test.com", "Test2", "TestPassword2", "Facebook", 1000);
		SignUpRequestDTO signUpRequestDTO3 = new SignUpRequestDTO("Test3@test.com", "Test3", "TestPassword3", "Kakaotalk", 2000);
		SignUpRequestDTO signUpRequestDTO4 = new SignUpRequestDTO("Test4@test.com", "Test4", "TestPassword4", "None", 0);
		
		String signUpRequestJson = objectMapper.writeValueAsString(signUpRequestDTO);
		String signUpRequestJson2 = objectMapper.writeValueAsString(signUpRequestDTO2);
		String signUpRequestJson3 = objectMapper.writeValueAsString(signUpRequestDTO3);
		String signUpRequestJson4 = objectMapper.writeValueAsString(signUpRequestDTO4);

		
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson2)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson3)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson4)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		/* 2.
		 * Expected result:
		 * 		success: false
		 * Test if SignUp works well with inputs which should be rejected.
		 */
		SignUpRequestDTO signUpRequestDTO5 = new SignUpRequestDTO("Test@test.com", "Test5", "TestPassword5", "None", 0);
		SignUpRequestDTO signUpRequestDTO6 = new SignUpRequestDTO("Test6@test.com", "Test", "TestPassword6", "None", 0);
		
		String signUpRequestJson5 = objectMapper.writeValueAsString(signUpRequestDTO5);
		String signUpRequestJson6 = objectMapper.writeValueAsString(signUpRequestDTO6);
		
		/* signUpRequestJson4 should be rejected because it has duplicate email with existing data. */
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson5)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Given email is duplicated. If you want to link your accounts, sign in and try linking account."));
		
		/* signUpRequestJson5 should be rejected because it has duplicate nickname with existing data. */
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson6)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Given nickname is duplicated."));
	}
	
	/*
	 * Test SignIn.
	 */
	@Test
	@Order(2)
	public void SignInTest() throws Exception {
		/* 1.
		 * Expected result:
		 * 		success : true, jwtToken: non-Null
		 * Test if SignIn works well with a input which should be succeed to sign in.
		 */
		SignInRequestDTO signInRequestDTO = new SignInRequestDTO("Test@test.com", "TestPassword", "None");
		
		String signInRequestJson = objectMapper.writeValueAsString(signInRequestDTO);
		
		mockMvc.perform(post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signInRequestJson)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
				.andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken").exists());
		
		/* 2.
		 * Expected result:
		 * 		success: false, jwtToken: Null
		 * Test if SignIn works well with inputs which should be rejected.
		 */
		SignInRequestDTO signInRequestDTO2 = new SignInRequestDTO("Test@test.com", "TestPassword2", "None");
		SignInRequestDTO signInRequestDTO3 = new SignInRequestDTO("Test@test.com", "TestPassword", "Facebook");
		
		String signInRequestJson2 = objectMapper.writeValueAsString(signInRequestDTO2);
		String signInRequestJson3 = objectMapper.writeValueAsString(signInRequestDTO3);
		
		/* signInRequestJson2 should be rejected because it has wrong password. */
		mockMvc.perform(post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signInRequestJson2)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
				.andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken").doesNotExist());
		
		/* signInRequestJson3 should be rejected because there's no match with given Authorization Provider. */
		mockMvc.perform(post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signInRequestJson3)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
				.andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken").doesNotExist());
	}
	
	/*
	 * Test LinkAccount.
	 */
	@Test
	@Order(3)
	public void LinkAccountTest() throws Exception {
		/* 1.
		 * Expected result:
		 * 		success : true
		 * Test if LinkAccount works well with a input which should be succeed to link account.
		 * To test LinkAccount, it is required to sign in first.
		 */
		/* Sign in and get the jwtToken as a response. */
		SignInRequestDTO signInRequestDTO = new SignInRequestDTO("Test@test.com", "TestPassword", "None");
		
		String signInRequestJson = objectMapper.writeValueAsString(signInRequestDTO);
		
		MvcResult mvcResult = mockMvc.perform(post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signInRequestJson)
				.characterEncoding("utf-8"))
				.andReturn();
		
		String signInResponseJson = mvcResult.getResponse().getContentAsString();
		SignInResponseDTO signInResponseDTO = objectMapper.readValue(signInResponseJson, SignInResponseDTO.class);
		
		/* Test LinkAccount with jwtToken as a request header. */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "Test", "TestPassword", "Facebook", 100);
		
		String signUpRequestJson = objectMapper.writeValueAsString(signUpRequestDTO);
		
		mockMvc.perform(post("/linkaccount")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		/* 2.
		 * Expected result:
		 * 		Exception
		 * Test if LinkAccount works well with a input which should be rejected.
		 */
		SignUpRequestDTO signUpRequestDTO2 = new SignUpRequestDTO("Test@test.com", "Test", "TestPassword", "Facebook", 100);
		SignUpRequestDTO signUpRequestDTO3 = new SignUpRequestDTO("Test@test.com", "Test", "TestPassword", "Kakaotalk", 100);
		
		String signUpRequestJson2 = objectMapper.writeValueAsString(signUpRequestDTO2);
		String signUpRequestJson3 = objectMapper.writeValueAsString(signUpRequestDTO3);
		
		/* signUpRequestJson2 should be rejected because there's an already linked account with given Authorization provider. */
		mockMvc.perform(post("/linkaccount")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson2)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500));
		
		/* signUpRequestJson3 should be rejected because LinkAccount needs a valid jwtToken. */
		mockMvc.perform(post("/linkaccount")
				.header("Authorization", "")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson3)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401));
	}
	
	/*
	 * Test PostWriting.
	 */
	@Test
	@Order(4)
	public void PostWritingTest() throws Exception {
		/* 1.
		 * Expected result:
		 * 		success : true
		 * Test if PostWriting works well with a input which should be succeed to post.
		 * To test PostWriting, it is required to sign in first.
		 */
		/* Sign in and get the jwtToken as a response. */
		SignInRequestDTO signInRequestDTO = new SignInRequestDTO("Test@test.com", "TestPassword", "None");
		
		String signInRequestJson = objectMapper.writeValueAsString(signInRequestDTO);
		
		MvcResult mvcResult = mockMvc.perform(post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signInRequestJson)
				.characterEncoding("utf-8"))
				.andReturn();
		
		String signInResponseJson = mvcResult.getResponse().getContentAsString();
		SignInResponseDTO signInResponseDTO = objectMapper.readValue(signInResponseJson, SignInResponseDTO.class);
		
		/* Test PostWriting with jwtToken as a request header. */
		PostWritingRequestDTO postWritingRequestDTO = new PostWritingRequestDTO("This is a test writing #1.");
		
		String postWritingRequestJson = objectMapper.writeValueAsString(postWritingRequestDTO);
		
		mockMvc.perform(post("/postwriting")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(postWritingRequestJson)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		/* 2.
		 * Expected result:
		 * 		success : false Or Exception
		 * Test if PostWriting works well with a input which should be rejected.
		 * postWritingRequestDTO3 has a content which has exactly 151 characters.
		 */
		PostWritingRequestDTO postWritingRequestDTO2 = new PostWritingRequestDTO("This is a test writing #1.");
		PostWritingRequestDTO postWritingRequestDTO3 = new PostWritingRequestDTO
				("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		
		String postWritingRequestJson2 = objectMapper.writeValueAsString(postWritingRequestDTO2);
		String postWritingRequestJson3 = objectMapper.writeValueAsString(postWritingRequestDTO3);
		
		/* postWritingRequestJson2 should be rejected because PostWriting needs a valid jwtToken. */
		mockMvc.perform(post("/postwriting")
				.header("Authorization", "")
				.contentType(MediaType.APPLICATION_JSON)
				.content(postWritingRequestJson2)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401));
		
		/* postWritingRequestJson3 should be rejected because it has a content exceeding 150 characters. */
		mockMvc.perform(post("/postwriting")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(postWritingRequestJson3)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
	}
	
	/*
	 * Test PostComment.
	 */
	@Test
	@Order(5)
	public void PostCommentTest() throws Exception {
		/* 1.
		 * Expected result:
		 * 		success : true
		 * Test if PostComment works well with a input which should be succeed to post.
		 * To test PostComment, it is required to sign in first.
		 */
		/* Sign in and get the jwtToken as a response. */
		SignInRequestDTO signInRequestDTO = new SignInRequestDTO("Test@test.com", "TestPassword", "None");
		
		String signInRequestJson = objectMapper.writeValueAsString(signInRequestDTO);
		
		MvcResult mvcResult = mockMvc.perform(post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signInRequestJson)
				.characterEncoding("utf-8"))
				.andReturn();
		
		String signInResponseJson = mvcResult.getResponse().getContentAsString();
		SignInResponseDTO signInResponseDTO = objectMapper.readValue(signInResponseJson, SignInResponseDTO.class);
		
		/* Test PostComment with jwtToken as a request header. */
		PostCommentRequestDTO postCommentRequestDTO = new PostCommentRequestDTO(1, "This is a test comment#1 on writing #1.");
		
		String postCommentRequestJson = objectMapper.writeValueAsString(postCommentRequestDTO);
		
		mockMvc.perform(post("/postcomment")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(postCommentRequestJson)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		/* 2.
		 * Expected result:
		 * 		Exception
		 * Test if PostComment works well with a input which should be rejected.
		 */
		PostCommentRequestDTO postCommentRequestDTO2 = new PostCommentRequestDTO(1, "This is a test comment#2 on writing #1.");
		PostCommentRequestDTO postCommentRequestDTO3 = new PostCommentRequestDTO(2, "This is a test comment#1 on writing #2 which is not existing.");
		
		String postCommentRequestJson2 = objectMapper.writeValueAsString(postCommentRequestDTO2);
		String postCommentRequestJson3 = objectMapper.writeValueAsString(postCommentRequestDTO3);
		
		/* postCommentRequestJson2 should be rejected because PostComment needs a valid jwtToken. */
		mockMvc.perform(post("/postcomment")
				.header("Authorization", "")
				.contentType(MediaType.APPLICATION_JSON)
				.content(postCommentRequestJson2)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401));
		
		/* postCommentRequestJson3 should be rejected because the id of the target writing is invalid which means that it is a not existing writing. */
		mockMvc.perform(post("/postcomment")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(postCommentRequestJson3)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500));
	}
	
	/*
	 * Test Follow.
	 */
	@Test
	@Order(6)
	public void FollowTest() throws Exception {
		/* 1.
		 * Expected result:
		 * 		success : true
		 * Test if Follow works well with a input which should be succeed.
		 * To test Follow, it is required to sign in first.
		 */
		/* Sign in and get the jwtToken as a response. */
		SignInRequestDTO signInRequestDTO = new SignInRequestDTO("Test@test.com", "TestPassword", "None");
		
		String signInRequestJson = objectMapper.writeValueAsString(signInRequestDTO);
		
		MvcResult mvcResult = mockMvc.perform(post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signInRequestJson)
				.characterEncoding("utf-8"))
				.andReturn();
		
		String signInResponseJson = mvcResult.getResponse().getContentAsString();
		SignInResponseDTO signInResponseDTO = objectMapper.readValue(signInResponseJson, SignInResponseDTO.class);
		
		/* Test Follow with jwtToken as a request header. */
		FollowRequestDTO followRequestDTO = new FollowRequestDTO(2);
		FollowRequestDTO followRequestDTO2 = new FollowRequestDTO(3);
		
		String followRequestJson = objectMapper.writeValueAsString(followRequestDTO);
		String followRequestJson2 = objectMapper.writeValueAsString(followRequestDTO2);
		
		mockMvc.perform(post("/follow")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		mockMvc.perform(post("/follow")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson2)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		/* 2.
		 * Expected result:
		 * 		Exception
		 * Test if Follow works well with a input which should be rejected.
		 */
		FollowRequestDTO followRequestDTO3 = new FollowRequestDTO(4);
		FollowRequestDTO followRequestDTO4 = new FollowRequestDTO(3);
		FollowRequestDTO followRequestDTO5 = new FollowRequestDTO(5);
		FollowRequestDTO followRequestDTO6 = new FollowRequestDTO(1);
		
		String followRequestJson3 = objectMapper.writeValueAsString(followRequestDTO3);
		String followRequestJson4 = objectMapper.writeValueAsString(followRequestDTO4);
		String followRequestJson5 = objectMapper.writeValueAsString(followRequestDTO5);
		String followRequestJson6 = objectMapper.writeValueAsString(followRequestDTO6);
		
		/* followRequestJson3 should be rejected because Follow requires a valid jwtToken. */
		mockMvc.perform(post("/follow")
				.header("Authorization", "")
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson3)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401));
		
		/* followRequestJson4 should be rejected because it requests to follow a user who already following. */
		mockMvc.perform(post("/follow")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson4)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500));
		
		/* followRequestJson5 should be rejected because it requests to follow a non-existing user. */
		mockMvc.perform(post("/follow")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson5)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500));
		
		/* followRequestJson6 should be rejected because it requests to follow him/herself. */
		mockMvc.perform(post("/follow")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson6)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500));
	}
	
	/*
	 * Test Unfollow.
	 */
	@Test
	@Order(7)
	public void UnfollowTest() throws Exception {
		/* 1.
		 * Expected result:
		 * 		success : true
		 * Test if Unfollow works well with a input which should be succeed.
		 * To test Unfollow, it is required to sign in first.
		 */
		/* Sign in and get the jwtToken as a response. */
		SignInRequestDTO signInRequestDTO = new SignInRequestDTO("Test@test.com", "TestPassword", "None");
		
		String signInRequestJson = objectMapper.writeValueAsString(signInRequestDTO);
		
		MvcResult mvcResult = mockMvc.perform(post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signInRequestJson)
				.characterEncoding("utf-8"))
				.andReturn();
		
		String signInResponseJson = mvcResult.getResponse().getContentAsString();
		SignInResponseDTO signInResponseDTO = objectMapper.readValue(signInResponseJson, SignInResponseDTO.class);
		
		/* Test Unfollow with jwtToken as a request header. */
		FollowRequestDTO followRequestDTO = new FollowRequestDTO(2);
		
		String followRequestJson = objectMapper.writeValueAsString(followRequestDTO);
		
		mockMvc.perform(post("/unfollow")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		/* 2.
		 * Expected result:
		 * 		Exception
		 * Test if Unfollow works well with a input which should be rejected.
		 */
		FollowRequestDTO followRequestDTO2 = new FollowRequestDTO(3);
		FollowRequestDTO followRequestDTO3 = new FollowRequestDTO(2);
		FollowRequestDTO followRequestDTO4 = new FollowRequestDTO(5);
		FollowRequestDTO followRequestDTO5 = new FollowRequestDTO(1);
		
		String followRequestJson2 = objectMapper.writeValueAsString(followRequestDTO2);
		String followRequestJson3 = objectMapper.writeValueAsString(followRequestDTO3);
		String followRequestJson4 = objectMapper.writeValueAsString(followRequestDTO4);
		String followRequestJson5 = objectMapper.writeValueAsString(followRequestDTO5);
		
		/* followRequestJson2 should be rejected because Unfollow requires a valid jwtToken. */
		mockMvc.perform(post("/unfollow")
				.header("Authorization", "")
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson2)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401));
		
		/* followRequestJson3 should be rejected because it requests to unfollow a user who are not followed by yet. */
		mockMvc.perform(post("/unfollow")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson3)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500));
		
		/* followRequestJson4 should be rejected because it requests to unfollow a non-existing user. */
		mockMvc.perform(post("/unfollow")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson4)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500));
		
		/* followRequestJson5 should be rejected because it requests to unfollow him/herself. */
		mockMvc.perform(post("/unfollow")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson5)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500));
	}
	
	/*
	 * Test GetFollower.
	 */
	@Test
	@Order(8)
	public void GetFollowerTest() throws Exception {
		/* 1.
		 * Expected result:
		 * 		List<UserInformationDTO>
		 * Test if GetFollower works well with a input which should not throw exception.
		 * To test GetFollower, it is required to sign in first.
		 */
		/* Sign in and get the jwtToken as a response. */
		SignInRequestDTO signInRequestDTO = new SignInRequestDTO("Test@test.com", "TestPassword", "None");
		
		String signInRequestJson = objectMapper.writeValueAsString(signInRequestDTO);
		
		MvcResult mvcResult = mockMvc.perform(post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signInRequestJson)
				.characterEncoding("utf-8"))
				.andReturn();
		
		String signInResponseJson = mvcResult.getResponse().getContentAsString();
		SignInResponseDTO signInResponseDTO = objectMapper.readValue(signInResponseJson, SignInResponseDTO.class);
		
		/* Test GetFollower with jwtToken as a request header. */
		mockMvc.perform(get("/getfollower")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").doesNotHaveJsonPath());
		
		/* 2.
		 * Expected result:
		 * 		Exception
		 * Test if GetFollower works well with a input which should produce an exception.
		 */
		/* The GET request with invalid jwtToken would result to an exception. */
		mockMvc.perform(get("/getfollower")
				.header("Authorization", "")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401));
	}
	
	/*
	 * Test GetFollowing.
	 */
	@Test
	@Order(9)
	public void GetFollowingTest() throws Exception {
		/* 1.
		 * Expected result:
		 * 		List<UserInformationDTO>
		 * Test if GetFollowing works well with a input which should not throw exception.
		 * To test GetFollowing, it is required to sign in first.
		 */
		/* Sign in and get the jwtToken as a response. */
		SignInRequestDTO signInRequestDTO = new SignInRequestDTO("Test@test.com", "TestPassword", "None");
		
		String signInRequestJson = objectMapper.writeValueAsString(signInRequestDTO);
		
		MvcResult mvcResult = mockMvc.perform(post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signInRequestJson)
				.characterEncoding("utf-8"))
				.andReturn();
		
		String signInResponseJson = mvcResult.getResponse().getContentAsString();
		SignInResponseDTO signInResponseDTO = objectMapper.readValue(signInResponseJson, SignInResponseDTO.class);
		
		/* Follow one more user for a test. */
		FollowRequestDTO followRequestDTO = new FollowRequestDTO(2);
		
		String followRequestJson = objectMapper.writeValueAsString(followRequestDTO);
		
		mockMvc.perform(post("/follow")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(followRequestJson)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		/* Test GetFollower with jwtToken as a request header. */
		mockMvc.perform(get("/getfollowing")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").doesNotHaveJsonPath());
		
		/* 2.
		 * Expected result:
		 * 		Exception
		 * Test if GetFollowing works well with a input which should produce an exception.
		 */
		/* The GET request with invalid jwtToken would result to an exception. */
		mockMvc.perform(get("/getfollowing")
				.header("Authorization", "")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401));
	}
	
	/*
	 * Test GetTimeline.
	 */
	@Test
	@Order(10)
	public void GetTimelineTest() throws Exception {
		/* 1.
		 * Expected result:
		 * 		List<WritingInformationDTO>
		 * Test if GetTimeline works well with a input which should not throw exception.
		 * To test GetTimeline, it is required to sign in first.
		 */
		/* Sign in and get the jwtToken as a response. */
		SignInRequestDTO signInRequestDTO = new SignInRequestDTO("Test@test.com", "TestPassword", "None");
		
		String signInRequestJson = objectMapper.writeValueAsString(signInRequestDTO);
		
		MvcResult mvcResult = mockMvc.perform(post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signInRequestJson)
				.characterEncoding("utf-8"))
				.andReturn();
		
		String signInResponseJson = mvcResult.getResponse().getContentAsString();
		SignInResponseDTO signInResponseDTO = objectMapper.readValue(signInResponseJson, SignInResponseDTO.class);
		
		/* Test GetTimeline with jwtToken as a request header. */
		mockMvc.perform(get("/gettimeline")
				.header("Authorization", signInResponseDTO.getJwtToken())
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").doesNotHaveJsonPath());
		
		/* 2.
		 * Expected result:
		 * 		Exception
		 * Test if GetTimeline works well with a input which should produce an exception.
		 */
		/* The GET request with invalid jwtToken would result to an exception. */
		mockMvc.perform(get("/gettimeline")
				.header("Authorization", "")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401));
	}
}
