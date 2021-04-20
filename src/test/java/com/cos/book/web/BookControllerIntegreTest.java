package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


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
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN ID RESTART WITH 1").executeUpdate();
	}
	
	//BDDMockito 패턴
		//given, when, then
		@Test
		public void save_test() throws Exception {
			//given (테스트를 하기 위한준비)
			Book book = new Book(null,"스프링따라하기","코스");
			String content = new ObjectMapper().writeValueAsString(book);
			//json형태로 바꿔준다.
			
			//when (테스트 실행)
			ResultActions resultAction = mockMvc.perform(post("/book")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(content)
					.accept(MediaType.APPLICATION_JSON_UTF8));
			
			//then (검증)
			resultAction
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("스프링따라하기"))
				.andExpect(jsonPath("$.author").value("코스"))
				.andDo(MockMvcResultHandlers.print());
		}
		
		@Test
		public void findAll_test() throws Exception {
			//given
			List<Book> books = new ArrayList<Book>();
			books.add(new Book(1L,"스프링부트 따라하기","코스"));
			books.add(new Book(2L,"리액트 따라하기","코스"));
			books.add(new Book(3L,"J유닛 따라하기","코스"));
			bookRepository.saveAll(books);
			
			//when
			ResultActions resultAction = mockMvc.perform(get("/book")
					.accept(MediaType.APPLICATION_JSON_UTF8));
			
			//then
			resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$",Matchers.hasSize(3)))
				.andExpect(jsonPath("$.[2].title").value("J유닛 따라하기"))
				.andDo(MockMvcResultHandlers.print());
		}
		
		@Test
		public void findById_test() throws Exception {
			//given
			Long id = 2L;
			List<Book> books = new ArrayList<Book>();
			books.add(new Book(1L,"스프링부트 따라하기","코스"));
			books.add(new Book(2L,"리액트 따라하기","코스"));
			books.add(new Book(3L,"J유닛 따라하기","코스"));
			bookRepository.saveAll(books);
			
			//when
			ResultActions resultAction = mockMvc.perform(get("/book/{id}",id)
					.accept(MediaType.APPLICATION_JSON_UTF8));
			
			//then
			resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("리액트 따라하기"))
				.andDo(MockMvcResultHandlers.print());
		}
		
		@Test
		public void update_test() throws Exception {
			//given
			Long id = 1L;
			List<Book> books = new ArrayList<Book>();
			books.add(new Book(1L,"스프링부트 따라하기","코스"));
			books.add(new Book(2L,"리액트 따라하기","코스"));
			books.add(new Book(3L,"J유닛 따라하기","코스"));
			bookRepository.saveAll(books);
			
			Book book = new Book(null,"c++따라하기","코스");
			String content = new ObjectMapper().writeValueAsString(book);
			//json형태로 바꿔준다.
			
			//when
			ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(content)
					.accept(MediaType.APPLICATION_JSON_UTF8));
			
			//then
			resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("c++따라하기"))
			.andDo(MockMvcResultHandlers.print());
		}
		
		@Test
		public void delete_test() throws Exception {
			//given
			Long id = 1L;
			List<Book> books = new ArrayList<Book>();
			books.add(new Book(1L,"스프링부트 따라하기","코스"));
			books.add(new Book(2L,"리액트 따라하기","코스"));
			books.add(new Book(3L,"J유닛 따라하기","코스"));
			bookRepository.saveAll(books);
			
			//when
			ResultActions resultAction = mockMvc.perform(delete("/book/{id}",id)
					.accept(MediaType.TEXT_PLAIN));
			
			//then
			resultAction
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
			
			MvcResult requestResult = resultAction.andReturn();
			String result = requestResult.getResponse().getContentAsString();
			
			assertEquals("ok",result);
		}
}
