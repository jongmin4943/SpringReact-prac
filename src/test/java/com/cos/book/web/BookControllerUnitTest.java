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

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

//단위 테스트 ( Controller 관련 로직만 테스트)
//Controller,Filter,ControllerAdvice...


@Slf4j
@WebMvcTest //controller관련된 빈만 뜬다.
//@RunWith(SpringRunner.class) JUnit4에서 필요하다 
//5에서는 WebMvcTest안에 @ExtendWith(SpringExtension.class) 가 있어서 필요없다
public class BookControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean // Ioc환경에 bean 등록됨
	private BookService bookService;
	
	//BDDMockito 패턴
	//given, when, then
	@Test
	public void save_test() throws Exception {
		//given (테스트를 하기 위한준비)
		Book book = new Book(null,"스프링따라하기","코스");
		String content = new ObjectMapper().writeValueAsString(book);
		//json형태로 바꿔준다.
		when(bookService.save(book)).thenReturn(new Book(1L,"스프링따라하기","코스"));
		
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
		when(bookService.getAll()).thenReturn(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",Matchers.hasSize(2)))
			.andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findById_test() throws Exception {
		//given
		Long id = 1L;
		when(bookService.getOne(id)).thenReturn(new Book(1L,"자바 공부하기","쌀"));
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}",id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("자바 공부하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_test() throws Exception {
		//given
		Long id = 1L;
		Book book = new Book(null,"c++따라하기","코스");
		String content = new ObjectMapper().writeValueAsString(book);
		//json형태로 바꿔준다.
		when(bookService.update(id,book)).thenReturn(new Book(1L,"c++따라하기","코스"));
		
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
		//json형태로 바꿔준다.
		when(bookService.delete(id)).thenReturn("ok");
		
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
