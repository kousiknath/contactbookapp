package com.assignment.contactbook;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactbookApplicationTests {

	@Test
	public void contextLoads() {
		assertThat(1).isEqualTo(1);
	}

}
