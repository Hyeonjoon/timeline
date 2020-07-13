package me.timeline.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.timeline.dto.SignatureInfoDTO;

@Repository
public class SignatureInfoDAOImpl  implements SignatureInfoDAO{
	
	public SignatureInfoDTO signUp(String email, String password) {
		SignatureInfoDTO signatureinfo = new SignatureInfoDTO();
		signatureinfo.setUserName("TESTNAME");
		signatureinfo.setUserId(100);
		return signatureinfo;
	}

}
