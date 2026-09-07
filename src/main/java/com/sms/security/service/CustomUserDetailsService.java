package com.sms.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sms.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email)
	        throws UsernameNotFoundException {


	    return userRepository.findByEmail(email)
	            .orElseThrow(
	                    () -> new UsernameNotFoundException(
	                            "User not found with this email : " + email
	                    )
	            );

	}

}