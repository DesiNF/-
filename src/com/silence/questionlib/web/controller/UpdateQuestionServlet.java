package com.silence.questionlib.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.silence.questionlib.domain.Question;
import com.silence.questionlib.domain.Teacher;
import com.silence.questionlib.exception.QuestionNotExist;
import com.silence.questionlib.fombean.QuestionForm;
import com.silence.questionlib.i18n.I18nUtils;
import com.silence.questionlib.service.QuestionLibService;
import com.silence.questionlib.serviceimpl.QuestionLibServiceImpl;
import com.silence.questionlib.utils.WebUtils;

/**
 * ��������
 * 
 * @author ����ǿ
 * 
 */
public class UpdateQuestionServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		HttpSession session = request.getSession(false);
		// �����¼״̬
		if (session == null) {
			request.setAttribute("message",
					I18nUtils.getI18nProperty(request.getLocale(), "not_login"));
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
			return;
		}
		Teacher teacher = (Teacher) session.getAttribute("user");
		if (teacher == null) {
			request.setAttribute("message",
					I18nUtils.getI18nProperty(request.getLocale(), "not_login"));
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
			return;
		}
		// ������
		String formId = request.getParameter("formId");
		String formId2 = (String) session.getAttribute("formId");
		if (formId2 == null || !formId2.equals(formId)) {
			// ���Ŵ����ṩ�����ظ��ύ��
			request.setAttribute("message", I18nUtils.getI18nProperty(
					request.getLocale(), "formid_error"));
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
			return;
		}

		updateQuestion(request, response);
		return;
	}

	private void updateQuestion(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		QuestionForm form = WebUtils.request2Bean(request, QuestionForm.class);
		form.addLocale(request.getLocale());
		if (!form.check()) {
			// ��У��ʧ��
			request.setAttribute("errors", form.getErrors());
			request.getRequestDispatcher("/WEB-INF/jsp/updatequestion.jsp")
					.forward(request, response);
			return;
		}

		Question question = new Question();
		WebUtils.copyBean(form, question);
		QuestionLibService questionLibService = new QuestionLibServiceImpl();
		try {
			questionLibService.updateQuestion(question.getQuesid(), question);
			// request.setAttribute("message", I18nUtils.getI18nProperty(
			// request.getLocale(), "add_question_success"));
			// request.getRequestDispatcher("/message.jsp").forward(request,
			// response);
			response.sendRedirect(request.getContextPath()
					+ "/ListQuestionServlet");
			return;
		} catch (QuestionNotExist e) {
			request.setAttribute("message", I18nUtils.getI18nProperty(
					request.getLocale(), "question_not_exist"));
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", I18nUtils.getI18nProperty(
					request.getLocale(), "server_error"));
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
			return;
		}
	}
}
