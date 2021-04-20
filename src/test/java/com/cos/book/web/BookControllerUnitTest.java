package com.cos.book.web;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

//단위 테스트 ( Controller 관련 로직만 테스트)
//Controller,Filter,ControllerAdvice...

@WebMvcTest //controller관련된 빈만 뜬다.
//@RunWith(SpringRunner.class) JUnit4에서 필요하다 
//5에서는 WebMvcTest안에 @ExtendWith(SpringExtension.class) 가 있어서 필요없다
public class BookControllerUnitTest {

}
