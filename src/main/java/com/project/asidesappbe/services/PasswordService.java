package com.project.asidesappbe.services;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
	
	StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();

	public String encryptUserPassword(String password) {	
				
		return encryptor.encryptPassword(password);
	}
	
	public boolean isCorrectPassword(String userEnteredPassword, String retrievedPassword) {
		
		return encryptor.checkPassword(userEnteredPassword, retrievedPassword);
	}

}
