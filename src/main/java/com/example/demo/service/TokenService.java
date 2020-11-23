package com.example.demo.service;

import com.example.demo.data.vo.UserInfo;

public interface TokenService {

	public String generateToken(UserInfo userInfo);

	public UserInfo decodeToken(String token);

}