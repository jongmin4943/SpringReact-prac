package com.cos.book.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;

import lombok.RequiredArgsConstructor;

//기능을 정의할 수 있고, 트랜잭션 관리..
@RequiredArgsConstructor
@Service
public class BookService {
	private final BookRepository bookRepository;
	
	@Transactional
	public Book save(Book book) {
		return bookRepository.save(book);
	}
	@Transactional(readOnly = true)
	public Book getOne(Long id) {
		return bookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id를 확인해주세요"));
	}
	@Transactional(readOnly = true)
	public List<Book> getAll() {
		return bookRepository.findAll();
	}
	@Transactional
	public Book update(Long id, Book book) {
		//더티체킹
		Book bookEntity = bookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id를 확인해주세요"));
		//셀렉하여 영속화시킨다.
		bookEntity.setTitle(book.getTitle());
		bookEntity.setAuthor(book.getAuthor());
		return bookEntity;
	} // 함수종료 -> 트랜잭션 종료 -> 영속화 되어있는 데이터를 db로 갱신(flush) == 더티체킹
	@Transactional
	public String delete(Long id) {
		bookRepository.deleteById(id);
		return "ok";
	}
}
