package com.codeway.auth.provider;

import com.codeway.auth.service.UserDetailsServiceImpl;
import com.codeway.auth.token.CaptchaAuthenticationToken;
import com.codeway.db.redis.service.RedisService;
import com.codeway.enums.StatusEnum;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 图片验证码登录验证
 **/
@Data
@Component
public class CaptchaAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private RedisService redisService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Override
	public Authentication authenticate(Authentication authentication) {

		CaptchaAuthenticationToken authenticationToken = (CaptchaAuthenticationToken) authentication;
		String account = authenticationToken.getPrincipal().toString();
		UserDetails userInfo = userDetailsServiceImpl.loadUserByUsername(account);
		if (userInfo == null) {
			throw new BadCredentialsException(StatusEnum.LOGIN_ERROR.getMsg());
		}
		String password = userInfo.getPassword();
		String credentials = authentication.getCredentials().toString();
		if (!passwordEncoder.matches(credentials, password)) {
			throw new BadCredentialsException(StatusEnum.LOGIN_ERROR.getMsg());
		}
		//查询该code拥有的权限
		Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities();
		// 认证通过，生成已认证的Authentication，加入请求权限
		CaptchaAuthenticationToken authenticationResult = new CaptchaAuthenticationToken(userInfo, userInfo.getAuthorities());
		authenticationResult.setDetails(authenticationToken.getDetails());
		return new CaptchaAuthenticationToken(userInfo, authorities);
	}


	/**
	 * supports方法用于检查入参的类型，AuthenticationProvider只会认证符合条件的类型
	 * 检查入参Authentication是否是UsernamePasswordAuthenticationToken或它的子类
	 *
	 * 系统默认的Authentication入参都是UsernamePasswordAuthenticationToken类型，所以这里supports必须为true。
	 * 需自定义认证过滤器，到时候就可以自定义不同的入参类型了，以适用于不同的AuthenticationProvider。

	 * @param authorization 符合条件的类型
	 * @return boolean
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		//负责处理MyAuthentication类型登录认证，参考上一篇
		return (CaptchaAuthenticationToken.class.isAssignableFrom(authentication));
	}

}