package me.timeline.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import me.timeline.dto.SignInRequestDTO;
import me.timeline.dto.SignInResponseDTO;
import me.timeline.dto.SignUpRequestDTO;
import me.timeline.dto.SignUpResponseDTO;
import me.timeline.entity.AuthProvider;
import me.timeline.entity.SignatureInformation;
import me.timeline.entity.User;
import me.timeline.repository.AuthProviderRepository;
import me.timeline.repository.SignatureInformationRepository;
import me.timeline.repository.UserRepository;

@Service("TimelineService")
public class TimelineServiceImpl implements TimelineService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SignatureInformationRepository signatureInformationRepository;
	
	@Autowired
	AuthProviderRepository authProviderRepository;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	/* signUp
	 * - Input: signUpRequestDTO
	 * - Output: boolean
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
			user.initSignatureInformation();
			
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
	
	public SignInResponseDTO SignIn(SignInRequestDTO signInRequestDTO) {
		/* Create a SignInResponseDTO object. */
		SignInResponseDTO signInResponseDTO = new SignInResponseDTO();
		
		/* Get an User entity, and an AuthProvider entity with corresponding email, and provider type.
		 * If there're no such entities, return the response.
		 * We can ensure that an authProvider entity exists, because an user cannot set the provider type. */
		Optional<User> userNullable = userRepository.findByEmail(signInRequestDTO.getEmail());
		if (!userNullable.isPresent()) {
			signInResponseDTO.setSuccess(false);
			return signInResponseDTO;
		}
		User user = userNullable.get();
		AuthProvider authProvider = authProviderRepository.findByType(signInRequestDTO.getProvider()).get();
		
		/* Get a SignatureInformation entity which references the User entity and AuthProvider entity above.
		 * If there's no such entity, return the response. */
		Optional<SignatureInformation> signatureInformationNullable = signatureInformationRepository.findByUser_IdAndAuthProvider_Id(user.getId(), authProvider.getId());
		if (!signatureInformationNullable.isPresent()) {
			signInResponseDTO.setSuccess(false);
			return signInResponseDTO;
		}
		SignatureInformation signatureInformation = signatureInformationNullable.get();
		
		/* Check if given password matches the stored password in database. */
		if (passwordEncoder.matches(signInRequestDTO.getPassword(), signatureInformation.getPasskey())) {
			signInResponseDTO.setSuccess(true);
		}
		else {
			signInResponseDTO.setSuccess(false);
		}
		
		return signInResponseDTO;
	}
}
