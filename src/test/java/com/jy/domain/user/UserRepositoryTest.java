package com.jy.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.jy.domain.Role;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	/* BCryptPasswordEncoder 생성자로 빈 등록 */
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Test
	public void 유저_생성_조회() {
		
		// given
		String username = "hellospring";
		String rawPassword = "fe!efe3few";
		String nickname = "민하";
		String email = "minhaprincess@jy.com";
		
		String encoderedPassword = passwordEncoder.encode(rawPassword);
		
		// when
		User user = User.builder()
						.username(username)
						.password(encoderedPassword)
						.nickname(nickname)
						.email(email)
						.role(Role.USER)
						.build();
		
		userRepository.save(user);
		
		// then
		assertThat(user.getUsername()).isEqualTo(username);
		assertThat(user.getPassword()).isEqualTo(encoderedPassword);
	}
	
}
