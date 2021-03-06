package com.cos.book.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

//단위 테스트( DB 관련된 Bean이 Ioc에 등록되면 됨)
@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY) //가짜 db로 테스트, replace.none은 실제 db로 테스트
@DataJpaTest //jpa관련 애들만 뜬다. repository들을 다 ioc에 등록해줌.
public class BookRepositoryUnitTest {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Test
	public void save_test() {
		//given
		Book book = new Book(null, "책제목1", "책저자1");
		
		//when
		Book bookEntity = bookRepository.save(book);
		
		//then
		assertEquals("책제목1", bookEntity.getTitle());
	}
}
