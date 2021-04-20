package com.cos.book.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


/**
 * 통합 테스트 (모든 Bean들을 똑같이 IoC 올리고 테스트하는것)
 * @SpringBootTest(webEnvironment = WebEnvironment.MOCK) 실제 톰켓을 올리는게 아니라 다른톰켓을로 테스트
 * @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) 실제 톰켓에서에서 테스트 
 * @AutoConfigureMockMvc MockMvc를 Ioc에 등록해줌
 * @Transactional 은 각 각의 테스트함수가 종료 될때마다 트랜잭션을 rollback해주는 어노테이션
 */

@Transactional
@AutoConfigureMockMvc // 단위테스트용 @WebMvcTest 에는 포함되어있다.
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) 
// 모든 애들이 메모리에 다 뜬다.
public class BookControllerIntegreTest {
	@Autowired
	private MockMvc mockMvc; //@@AutoConfigureMockMvc가 필요하다.
	
	
}
