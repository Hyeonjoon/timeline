package me.timeline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import me.timeline.dto.SignatureInfoDTO;
import me.timeline.entity.AuthProvider;
import me.timeline.entity.SignatureInformation;
import me.timeline.entity.User;
import me.timeline.repository.AuthProviderRepository;
import me.timeline.repository.SignatureInformationRepository;
import me.timeline.repository.UserRepository;

@Service("TimelineService")
public class TimelineServiceImpl implements TimelineService {
	
	@Autowired
	UserRepository timelineUserRepository;
	
	@Autowired
	SignatureInformationRepository timelineSignatureInformationRepository;
	
	@Autowired
	AuthProviderRepository timelineAuthProviderRepository;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	/* signUp
	 * - Input: SignatureInfoDTO
	 * - Output: boolean
	 * Check if given email address is already exists.
	 * If it is, return false, and if not, create entities with given information and save them in the database.
	 */
	public boolean signUp(SignatureInfoDTO signatureInfoDTO) {
		boolean isEmailDuplicated = timelineUserRepository.existsByEmail(signatureInfoDTO.getEmail());
		if (isEmailDuplicated) {
			return false;
		}
		else {
			/* Create a new User entity. */
			User user = new User();
			user.setEmail(signatureInfoDTO.getEmail());
			user.setNickname(signatureInfoDTO.getNickname());
			user.initSignatureInformation();
			
			/* Get a AuthProvider entity with given type. */
			AuthProvider authProvider = timelineAuthProviderRepository.findByType(signatureInfoDTO.getProvider()).get();
			
			/* Create a new SignatureInformation entity. */
			SignatureInformation signatureInformation = new SignatureInformation();
			signatureInformation.setProviderUserId(0);
			signatureInformation.setPasskey(passwordEncoder.encode(signatureInfoDTO.getPassword()));
			
			/* Create some referential relationships among entities. */
			signatureInformation.setUser(user);
			signatureInformation.setAuthProvider(authProvider);
			user.addSignatureInformation(signatureInformation);
			authProvider.addSignatureInformation(signatureInformation);
			
			/* Save the data in database. */
			timelineUserRepository.save(user);
			timelineAuthProviderRepository.save(authProvider);
			timelineSignatureInformationRepository.save(signatureInformation);
			
			return true;
		}
	}
}
