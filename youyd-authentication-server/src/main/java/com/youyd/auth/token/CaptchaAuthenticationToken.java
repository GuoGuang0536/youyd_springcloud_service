package com.youyd.auth.token;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


/**
 * 图片登录验证信息封装类
 * @author : LGG
 * @create : 2019-06-18 14:34
 **/
@Data
public class CaptchaAuthenticationToken extends UsernamePasswordAuthenticationToken {


	//未认证Authentication构造方法
	public CaptchaAuthenticationToken(Object principal, Object credentials) {
		super(principal,credentials);
		setAuthenticated(false);
	}

	//已认证Authentication构造方法
	public CaptchaAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		super(principal,"",authorities);
	}


}
