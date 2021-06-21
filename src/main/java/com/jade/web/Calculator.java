package com.jade.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/calculator")
public class Calculator extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
Cookie[] cookies = request.getCookies();
		
		String exp = "0"; // 초기화
		
		
		//쿠키값이 있으면 exp값 변경
		if (cookies != null) {  
			for (Cookie c : cookies) {
				if (c.getName().equals("exp")) {
					exp = c.getValue();
					break;
				}
			}
		}

		
		response.setCharacterEncoding("UTF-8"); // utf-8로 인코딩하여 전달
		response.setContentType("text/html; charset=UTF-8");// 전달값이 utf-8임을 브라우저에 알림

		PrintWriter out = response.getWriter(); // 출력스트림 생성

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\">");
		out.println("<title>Insert title here</title>");
		out.println("<style>");
		out.println("input{");
		out.println("width:50px;");
		out.println("height:50px;");
		out.println("} ");
		out.println(".output{");
		out.println("height:50px;");
		out.println("background: #e9e9e9;");
		out.println("font-size : 24px;");
		out.println("font-weight : bold;");
		out.println("text-align : right;");
		out.println("padding : 0px 5px;");
		out.println("}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<form method=\"post\">");
		out.println("	<table>");
		out.println("		<tr>");
		out.printf("			<td class=\"output\" colspan=\"8\">%s<td/>", exp);
		out.println("		<tr/>");
		out.println("		<tr>");
		out.println("			<td><input type=\"submit\" name=\"operator\" value=\"CE\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"operator\" value=\"C\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"operator\" value=\"BS\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"operator\" value=\"/\"/><td/>");
		out.println("		<tr/>");
		out.println("		<tr>");
		out.println("			<td><input type=\"submit\" name=\"value\" value=\"7\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"value\" value=\"8\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"value\" value=\"9\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"operator\" value=\"*\"/><td/>");
		out.println("		<tr/>");
		out.println("		<tr>");
		out.println("			<td><input type=\"submit\" name=\"value\" value=\"4\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"value\" value=\"5\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"value\" value=\"6\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"operator\" value=\"-\"/><td/>");
		out.println("		<tr/>");
		out.println("		<tr>");
		out.println("			<td><input type=\"submit\" name=\"value\" value=\"1\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"value\" value=\"2\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"value\" value=\"3\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"operator\" value=\"+\"/><td/>");
		out.println("		<tr/>");
		out.println("			<tr>");
		out.println("			<td><td/>");
		out.println("			<td><input type=\"submit\" name=\"value\" value=\"0\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"dot\" value=\".\"/><td/>");
		out.println("			<td><input type=\"submit\" name=\"operator\" value=\"=\"/><td/>");
		out.println("		<tr/>");
		out.println("		</table>");
		out.println("	</form>");
		out.println("</body>");
		out.println("</html>");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();

		String value = request.getParameter("value");
		String operator = request.getParameter("operator");
		String dot = request.getParameter("dot");

		String exp = "";
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals("exp")) {
					exp = c.getValue();
					break;
				}
			}
		}

		if (operator != null && operator.equals("=")) {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");
			try {
				exp = String.valueOf(engine.eval(exp));
				System.out.println(exp);
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		} else if (operator != null && operator.equals("C")) {
			exp = "";
		} else {
			exp += value == null ? "" : value;
			exp += operator == null ? "" : operator;
			exp += dot == null ? "" : dot;

		}
		Cookie expCookie = new Cookie("exp", exp);

		if (operator != null && operator.equals("C"))
			expCookie.setMaxAge(0);

		expCookie.setPath("/JSPPrj3/calculator");
		response.addCookie(expCookie);
		response.sendRedirect("/JSPPrj3/calculator");
	}
}
