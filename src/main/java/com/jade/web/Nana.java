package com.jade.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hi")
public class Nana extends HttpServlet{

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setCharacterEncoding("UTF-8");//인코딩 방식
		response.setContentType("text/html; UTF-8");// 브라우저가 읽는 방식
		PrintWriter out = response.getWriter();
		
		for(int i=0; i<100; i++) {
			out.println(i+" 안녕~ <br/>");
		}
	}
}
