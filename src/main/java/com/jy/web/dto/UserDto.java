package com.jy.web.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.jy.domain.Role;
import com.jy.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDto {
	
	/* 회원 서비스 요청 RequestDTO 클래스 */
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Builder
	@Setter
	public static class RequestUserDto{
		private Long id;
		
		@NotBlank(message = "아이디는 필수 입력값입니다.")
		@Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
		private String username;
		
		@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
		private String password;
		
		@NotBlank(message = "닉네임은 필수 입력값입니다.")
		@Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$" , message = "닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
		private String nickname;
		
		@NotBlank(message = "이메일은 필수 입력값입니다.")
		@Email(message = "이메일 형식이 올바르지 않습니다.")
		private String email;
		
		private Role role;
	
		/* 암호화된 password */
		public void encryptPassword(String BCryptpassword) {
			this.password = BCryptpassword;
		}
		
		/* DTO -> Entity */
		public User toEntity() {
			User user = User.builder()
						.id(id)
						.username(username)
						.password(password)
						.nickname(nickname)
						.email(email)
						.role(role.USER)
						.build();
			return user;
		}
	}
	
	/** 인증된 사용자 정보를 세션에 저장하기 위한 클래스
	 *  세션에 저장하기 위헤 USER 엔티티 클래스를 직접 사용하면 직렬화가 필요함
	 *  엔티티 클래스에 직렬화를 넣어주면 추후 다른 엔티티와 연관관계를 맺을 시
	 *  직렬화 대상에 다른 엔티티까지 포함될 수 있으므로 성능 이슈 우려
	 *  세션 저장용 응답 ResponseDto 클래스
	 */
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ResponseUserDto implements Serializable{
		private Long id;
		private String username;
		private String nickname;
		private String email;
		private Role role;
		private String modifiedDate;
		
		/* Entity -> DTO */
		public ResponseUserDto(User user) {
			this.id = user.getId();
			this.username = user.getUsername();
			this.nickname = user.getNickname();
			this.email = user.getEmail();
			this.role = user.getRole();
			this.modifiedDate = user.getModifiedDate();
		}
	}

	

}
