package com.jade.web.controller.admin.notice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.jade.web.entity.Notice;
import com.jade.web.service.NoticeService;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 50, maxRequestSize = 1024 * 1024 * 50 * 5)
@WebServlet("/admin/board/notice/reg")
public class RegController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/reg.jsp").forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");// 인코딩 방식
		response.setContentType("text/html; UTF-8");// 브라우저가 읽는 방식

		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String isOpen = request.getParameter("open");
		boolean pub = false;

		Collection<Part> parts = request.getParts();
		StringBuilder builder= new StringBuilder();
		for (Part p : parts) {
			if(!p.getName().equals("file")) 
				continue;
			if(p.getSize()==0) {
				continue;
			}
			
			Part filePart = p;
			String fileName = filePart.getSubmittedFileName();
			
			builder.append(fileName);
			builder.append(",");
			
			InputStream fis = filePart.getInputStream();

			String realPath = request.getServletContext().getRealPath("/upload");
			System.out.println(realPath);
			
			File path = new File(realPath);
			if(!path.exists())
				path.mkdirs();
			
			String filePath = realPath + File.separator + fileName;
			FileOutputStream fos = new FileOutputStream(filePath);
			byte[] buf = new byte[1024];
			int size = 0;
			while ((size = fis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}
			fos.close();
			fis.close();
		}

		builder.delete(builder.length()-1, builder.length());
		
		if (isOpen != null) {
			pub = true;
		}

		Notice notice = new Notice();
		notice.setContent(content);
		notice.setTitle(title);
		notice.setPub(pub);
		notice.setWriterId("admin");
		notice.setFiles(builder.toString());

		NoticeService service = new NoticeService();
		int result = service.inserNotice(notice);
		response.sendRedirect("list");

		PrintWriter out = response.getWriter();
		out.print("title : " + title + "<br/>");
		out.print("content : " + content + "<br/>");
		out.print("isOpen : " + isOpen + "<br/>");

	}
}
