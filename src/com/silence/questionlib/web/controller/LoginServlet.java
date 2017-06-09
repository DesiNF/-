package com.silence.questionlib.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.silence.questionlib.domain.Paper;
import com.silence.questionlib.domain.Result;
import com.silence.questionlib.domain.Student;
import com.silence.questionlib.domain.Teacher;
import com.silence.questionlib.exception.StudentNotExist;
import com.silence.questionlib.exception.TeacherNotExist;
import com.silence.questionlib.fombean.StudentLoginForm;
import com.silence.questionlib.fombean.TeacherLoginForm;
import com.silence.questionlib.i18n.I18nUtils;
import com.silence.questionlib.service.StudentService;
import com.silence.questionlib.service.TeacherService;
import com.silence.questionlib.serviceimpl.QuestionLibServiceImpl;
import com.silence.questionlib.serviceimpl.StudentServiceImpl;
import com.silence.questionlib.serviceimpl.TeacherServiceImpl;
import com.silence.questionlib.utils.WebUtils;

/**
 * ��¼
 * 
 * @author ����ǿ
 * 
 */
public class LoginServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		HttpSession session = request.getSession(false);

		// ����ѵ�¼��ֱ�ӷ�����Ϣ
		Object user = session.getAttribute("user");
		String admin = request.getParameter("admin");
		if (user != null) {
			if (user instanceof Student) {
				// �����ѧ�������ѵ�¼������Ҫ���µ�¼��ֱ�ӷ���ѧ����Ϣ
				if (admin == null || admin.trim().equals("")) {
					getStudentInfo(request, response, (Student) user);
					return;
				}
			} else if (user instanceof Teacher) {
				// ����ǽ�ʦ�����ѵ�¼������Ҫ���µ�¼��ֱ�ӷ��ؽ�ʦ��Ϣ
				if (admin != null && !admin.trim().equals("")) {
					getTeacherInfo(request, response, new TeacherServiceImpl(),
							(Teacher) user);
					return;
				}
			}
		}

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

		Result result = (Result) session.getAttribute("result");
		String checkCode = request.getParameter("checkCode");
		if (checkCode == null || checkCode.trim().equals("")) {
			// ��֤��Ϊ��
			request.setAttribute("checkCodeError", I18nUtils.getI18nProperty(
					request.getLocale(), "checkcode_null"));
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(
					request, response);
			return;
		} else if (result == null) {
			// ��֤�����
			request.setAttribute("checkCodeError", I18nUtils.getI18nProperty(
					request.getLocale(), "checkcode_timeot"));
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(
					request, response);
			return;
		} else if (!checkCode.equalsIgnoreCase(result.checkCode)) {
			// ��֤�����
			request.setAttribute("checkCodeError", I18nUtils.getI18nProperty(
					request.getLocale(), "checkcode_error"));
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(
					request, response);
			return;
		}
		// ��֤����ȷ
		File file = new File(result.realPath);
		if (file.exists())
			file.delete();
		session.removeAttribute("result");

		if (admin == null || admin.trim().equals("")) {
			studentLogin(request, response);
			return;
		} else {
			teacherLogin(request, response);
			return;
		}
	}

	private void teacherLogin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		TeacherLoginForm form = WebUtils.request2Bean(request,
				TeacherLoginForm.class);
		form.addLocale(request.getLocale());
		if (!form.check()) {
			// ��У��ʧ��
			request.setAttribute("errors", form.getErrors());
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(
					request, response);
			return;
		}
		TeacherService teacherServiceImpl = new TeacherServiceImpl();
		try {
			Teacher teacher = teacherServiceImpl.login(form.getTeaid(),
					form.getPassword());
			if (teacher != null) {
				getTeacherInfo(request, response, teacherServiceImpl, teacher);
			} else {
				request.setAttribute("message", I18nUtils.getI18nProperty(
						request.getLocale(), "login_pswd_inncrect"));
				request.getRequestDispatcher("/message.jsp").forward(request,
						response);
			}
			return;
		} catch (TeacherNotExist e) {
			request.setAttribute("message", I18nUtils.getI18nProperty(
					request.getLocale(), "teacher_not_exist"));
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

	private void getTeacherInfo(HttpServletRequest request,
			HttpServletResponse response, TeacherService teacherServiceImpl,
			Teacher teacher) throws ServletException, IOException {
		request.setAttribute("user", teacher);
		request.getSession().setAttribute("user", teacher);
		request.getSession().setAttribute("groups",
				teacherServiceImpl.findGroups(teacher.getTeaid()));
		request.getRequestDispatcher("/WEB-INF/jsp/teacher.jsp").forward(
				request, response);
	}

	private void studentLogin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		StudentLoginForm form = WebUtils.request2Bean(request,
				StudentLoginForm.class);
		form.addLocale(request.getLocale());
		if (!form.check()) {
			// ��У��ʧ��
			request.setAttribute("errors", form.getErrors());
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(
					request, response);
			return;
		}
		StudentService studentServiceImpl = new StudentServiceImpl();
		try {
			Student student = studentServiceImpl.login(form.getStuid(),
					form.getPassword());
			if (student != null) {
				getStudentInfo(request, response, student);
			} else {
				request.setAttribute("message", I18nUtils.getI18nProperty(
						request.getLocale(), "login_pswd_inncrect"));
				request.getRequestDispatcher("/message.jsp").forward(request,
						response);
			}
			return;
		} catch (StudentNotExist e) {
			request.setAttribute("message", I18nUtils.getI18nProperty(
					request.getLocale(), "student_not_exist"));
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

	private void getStudentInfo(HttpServletRequest request,
			HttpServletResponse response, Student student)
			throws ServletException, IOException {
		request.setAttribute("user", student);
		request.setAttribute("papers", new QuestionLibServiceImpl()
				.findStudentPapers(student.getStuid()));
		request.getSession().setAttribute("user", student);
		request.getRequestDispatcher("/WEB-INF/jsp/student.jsp").forward(
				request, response);
	}
}
