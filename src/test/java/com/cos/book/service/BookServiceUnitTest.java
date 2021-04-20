package com.cos.book.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cos.book.domain.BookRepository;

/**
 * 단위 테스트(service와 관련된 애들만 메모리에 띄움)
 * service와 관련된놈이 repository밖에 없다.
 *	BoardRepository => 가짜 객체로만들수 있음
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {
	//Bookservice객체가 만들어질때 BookServiceUnitTest파일에 @Mock 으로 등록된 모든 애들을 주입받는다
	@InjectMocks
	private BookService bookService;
	
	@Mock//Ioc가 아닌 따른 공간에 생성..
	private BookRepository bookRepository;
	
}
