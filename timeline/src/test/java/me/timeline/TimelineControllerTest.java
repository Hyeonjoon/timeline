package me.timeline;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.timeline.controller.TimelineController;
import me.timeline.dto.SignUpRequestDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class TimelineControllerTest {
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
	public void SignUpTest() throws Exception {
		
		/* 1.
		 * Expected result: true
		 * Test if SignUp works well with a input which should be succeed to sign up.
		 */
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("Test@test.com", "Test", "TestPassword", "None", 0);
		
		String signUpRequestJson = objectMapper.writeValueAsString(signUpRequestDTO);
		
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
		
		/* 2.
		 * Expected result: false
		 * Test if SignUp works with inputs which should be rejected.
		 */
		SignUpRequestDTO signUpRequestDTO2 = new SignUpRequestDTO("Test@test.com", "Test2", "TestPassword2", "None", 0);
		SignUpRequestDTO signUpRequestDTO3 = new SignUpRequestDTO("Test2@test.com", "Test", "TestPassword2", "None", 0);
		
		String signUpRequestJson2 = objectMapper.writeValueAsString(signUpRequestDTO2);
		String signUpRequestJson3 = objectMapper.writeValueAsString(signUpRequestDTO3);
		
		/* signUpRequestJson2 should be rejected because it has duplicate email with existing data. */
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson2)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Given email is duplicated. If you want to link your accounts, sign in and try linking account."));
		
		/* signUpRequestJson3 should be rejected because it has duplicate nickname with existing data. */
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signUpRequestJson3)
				.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Given nickname is duplicated."));
	}
}
