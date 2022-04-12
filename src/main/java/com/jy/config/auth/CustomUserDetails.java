package com.jy.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jy.domain.user.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
	private User user;

	/* 유저의 권한 목록, 권한 반환*/
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole().getValue();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	/* 계정 만료 여부
	 * true :  만료 안됨
	 * false : 만료
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/* 계정 잠김 여부
	 * true : 잠기지 않음
	 * false : 잠김
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	/* 비밀번호 만료 여부
	 * true : 만료 안 됨
	 * false : 만료
	 */

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/* 사용자 활성화 여부
	 * true : 활성화 됨
	 * false : 활성화 안 됨
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
}
