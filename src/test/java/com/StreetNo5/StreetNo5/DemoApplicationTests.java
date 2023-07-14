package com.StreetNo5.StreetNo5;

import com.StreetNo5.StreetNo5.repository.RefreshTokenRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private RefreshTokenRedisRepository refreshTokenRedisRepository;



	@Test
	void contextLoads() {
	}
}
