package com.project.RecipeSpark.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.RecipeSpark.DataNotFoundException;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.UserRepository;

import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public User create(String username,String email,String password) {
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
		user.setPassword(passwordEncoder.encode(password));
		this.userRepository.save(user);
		return user;
	}
	
	public User getUser(String username) throws DataNotFoundException{
		Optional<User> user = this.userRepository.findByUsername(username);
		if(user.isPresent()) {
				return user.get();
			}else {
				throw new DataNotFoundException("site user not found");
			}
	}
	
	public boolean validatePassword(String password1, String password2) {
		return password1.equals(password2);
	}

}
	
