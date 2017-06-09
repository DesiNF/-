package junit.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.silence.questionlib.dao.QuestionDao;
import com.silence.questionlib.dao.StudentDao;
import com.silence.questionlib.dao.TeacherDao;
import com.silence.questionlib.daoimpl.QuestionDaoImpl;
import com.silence.questionlib.daoimpl.StudentDaoImpl;
import com.silence.questionlib.daoimpl.TeacherDaoImpl;
import com.silence.questionlib.domain.Group;
import com.silence.questionlib.domain.Paper;
import com.silence.questionlib.domain.Question;
import com.silence.questionlib.domain.Student;
import com.silence.questionlib.domain.Teacher;
import com.silence.questionlib.exception.PaperNotExist;
import com.silence.questionlib.exception.QuestionNotExist;
import com.silence.questionlib.exception.QuestionUsedException;
import com.silence.questionlib.exception.StudentNotExist;
import com.silence.questionlib.exception.TeacherNotExist;
import com.silence.questionlib.service.QuestionLibService;
import com.silence.questionlib.service.StudentService;
import com.silence.questionlib.service.TeacherService;
import com.silence.questionlib.serviceimpl.QuestionLibServiceImpl;
import com.silence.questionlib.serviceimpl.StudentServiceImpl;
import com.silence.questionlib.serviceimpl.TeacherServiceImpl;
import com.silence.questionlib.utils.WebUtils;

public class ServiceTest {

	@Test
	public void testLoginStudent() throws StudentNotExist {
		StudentService service = new StudentServiceImpl();
		Student student = service.login("201301050415", "123");
		System.out.println(student);
	}

	@Test
	public void testLoginTeacher() throws TeacherNotExist {
		TeacherService service = new TeacherServiceImpl();
		Teacher teacher = service.login("12345678", "123");
		// Teacher teacher = service.login("12345678", "123456");
		System.out.println(teacher);
	}

	@Test
	public void testRegisterTeacher() {
		Teacher teacher = new Teacher();
		teacher.setCollege("�����ѧԺ");
		teacher.setEmail("aaa@qq.com");
		teacher.setPassword("123");
		teacher.setPhone("110");
		teacher.setRegisterTime(new Date());
		teacher.setTeaid("170608");
		teacher.setTeaname("���÷");

		TeacherService service = new TeacherServiceImpl();
		service.register(teacher);
	}

	@Test
	public void testCreateGroup() {
		TeacherService service = new TeacherServiceImpl();
		Group group = new Group();
		group.setCollege("�����ѧԺ");
		group.setGroupname("�����14-3��");
		group.setTeaid("170608");
		group.setGroupid(WebUtils.getUUID());

		service.createClass(group);
	}

	@Test
	public void testGroupStudent() {
		TeacherService service = new TeacherServiceImpl();
		List<Student> students = service
				.findGroupStudents("297e1e39517197f001517197f4ce0000");
		for (Student student : students)
			System.out.println(student);
	}

	@Test
	public void testFindGroups() {
		TeacherService service = new TeacherServiceImpl();
		List<Group> groups = service.findGroups("170608");
		System.out.println(groups);
	}

	@Test
	public void testRegisterStudet() {
		Student student = new Student();
		student.setCollege("�����ѧԺ");
		student.setEmail("a3@qq.com");
		student.setGroupid("755bd3a3-5be8-4068-8861-b377cb31a79d");
		student.setPassword("123456");
		student.setPhone("178110");
		student.setRegisterTime(new Date());
		student.setStuid("201401050318");
		student.setStuname("������");

		StudentService service = new StudentServiceImpl();
		service.register(student);
	}

	@Test
	public void testStudentLogin() throws StudentNotExist {
		StudentService service = new StudentServiceImpl();
		Student student = service.login("201401050318", "123456");
		// Student student = service.login("201301050411", "123456");
		System.out.println(student);
	}

	@Test
	public void testQuestion() {
		QuestionDao dao = new QuestionDaoImpl();
		Question question = dao
				.findQuestion("023e6816-8cea-4322-840b-5fadefaa5ce5");
		System.out.println(question);
	}

	@Test
	public void testQuestionType() {
		QuestionDao dao = new QuestionDaoImpl();
		List<Question> questions = dao.findQuestions("xuanze");
		for (Question question : questions)
			System.out.println(question);
	}

	@Test
	public void testAddQuestion1() {
		QuestionLibService service = new QuestionLibServiceImpl();
		Question question = new Question();
		question.setInsertTime(new Date());
		question.setOther1("other1");
		question.setOther2("other2");
		question.setOther3("other3");
		question.setOther4("other4");
		question.setOwnerid("12345678");
		question.setQuesanswer("��");
		question.setQuescontent("����");
		question.setQuesid(WebUtils.getUUID());
		question.setQuesname("����");
		question.setQuestype("xuanze");
		service.addQuestion(question);
	}

	@Test
	public void testUpdateQuestion() throws QuestionNotExist {
		QuestionLibService service = new QuestionLibServiceImpl();
		Question question = new Question();
		question.setInsertTime(new Date());
		question.setOther1("other1");
		question.setOther2("other2");
		question.setOther3("other3");
		question.setOther4("other4");
		question.setOwnerid("12345678");
		question.setQuesanswer("��---->>�޸�");
		question.setQuescontent("����");
		question.setQuesid("f74ddabc-9be4-483d-80b2-751caa849b36");
		question.setQuesname("����");
		question.setQuestype("xuanze");

		service.updateQuestion("f74ddabc-9be4-483d-80b2-751caa849b36", question);
	}

	@Test
	public void testDeleteQuestion() throws QuestionNotExist,
			QuestionUsedException {
		QuestionLibService service = new QuestionLibServiceImpl();
		service.deleteQuestion("f74ddabc-9be4-483d-80b2-751caa849b36");
	}

	@Test
	public void testFindPaper() throws PaperNotExist {
		QuestionLibService service = new QuestionLibServiceImpl();
		Paper paper = service
				.queryPaper("54211d16-99f2-4cd4-86a2-111c6f248dc2");
		System.out.println(paper);
	}

	@Test
	public void testFindStudentPapers() {
		QuestionLibService service = new QuestionLibServiceImpl();
		List<Paper> papers = service.findStudentPapers("201301050415");
		for (Paper paper : papers)
			System.out.println(paper);
	}

	@Test
	public void testFindPaperQuestion() throws QuestionNotExist {
		QuestionLibService service = new QuestionLibServiceImpl();
		List<Question> questions = service
				.queryPaperQuestions("255ed114-631f-46e9-92e3-9f49886870cf");
		for (Question question : questions)
			System.out.println(question);
	}

	/**
	 * �����������
	 */
	@Test
	public void testAddQuestion() {
		QuestionLibService service = new QuestionLibServiceImpl();

		Question question = new Question();
		question.setInsertTime(new Date());
		question.setOwnerid("232421421");
		question.setOwnername("��ʦ1");

		for (int i = 0; i < 20; i++) {
			question.setQuesid(WebUtils.getUUID());

			question.setQuescontent("���ʽ�����Ŀ����" + (i + 1));
			question.setQuesname("���ʽ��ͱ���" + (i + 1));
			question.setQuesanswer("���ʽ��ʹ�" + (i + 1));
			question.setQuestype(Question.Type.jeishi);

			// question.setQuescontent("�����Ŀ����"+(i+1));
			// question.setQuesname("������"+(i+1));
			// question.setQuesanswer("����"+(i+1));
			// question.setQuestype(Question.Type.jianda);

			// question.setQuescontent("�����Ŀ����"+(i+1));
			// question.setQuesname("��ձ���"+(i+1));
			// question.setQuesanswer("��մ�"+(i+1));
			// question.setQuestype(Question.Type.tiankong);

			// question.setQuescontent("Ӧ����Ŀ����"+(i+1));
			// question.setQuesname("Ӧ�ñ���"+(i+1));
			// question.setQuesanswer("Ӧ�ô�"+(i+1));
			// question.setQuestype(Question.Type.yingyong);

			// question.setQuescontent("ѡ����Ŀ����"+(i+1));
			// question.setQuesname("ѡ�����"+(i+1));
			// question.setQuesanswer("ѡ���"+(i+1));
			// question.setOther1("A.ѡ��A");
			// question.setOther2("B.ѡ��B");
			// question.setOther3("C.ѡ��C");
			// question.setOther4("D.ѡ��D");
			// question.setQuestype(Question.Type.xuanze);

			service.addQuestion(question);
		}
	}

	/**
	 * ��������Ծ�
	 */
	@Test
	public void testAddPaper() {
		QuestionLibService service = new QuestionLibServiceImpl();

		Paper paper = new Paper();
		paper.setDestGroup("297e1e39517197f001517197f4ce0000");
		paper.setOwnerid("12345678");
		paper.setOwnername("���÷");
		paper.setPaperid(WebUtils.getUUID());
		paper.setPapername("������̿���");
		paper.setPaperTime(new Date());
		List<String> quesids = new ArrayList<String>();// ��Ŀid
		List<Float> qvalues = new ArrayList<Float>();// ��Ŀ�ܷ�
		
		List<Question> questions = service.getAll(Question.Type.xuanze);
		for(Question q:questions){
			quesids.add(q.getQuesid());
			qvalues.add(new Random().nextFloat());
		}
		
		questions = service.getAll(Question.Type.jeishi);
		for(Question q:questions){
			quesids.add(q.getQuesid());
			qvalues.add(new Random().nextFloat());
		}
		
		questions = service.getAll(Question.Type.jianda);
		for(Question q:questions){
			quesids.add(q.getQuesid());
			qvalues.add(new Random().nextFloat());
		}
		
		questions = service.getAll(Question.Type.tiankong);
		for(Question q:questions){
			quesids.add(q.getQuesid());
			qvalues.add(new Random().nextFloat());
		}
		
		questions = service.getAll(Question.Type.yingyong);
		for(Question q:questions){
			quesids.add(q.getQuesid());
			qvalues.add(new Random().nextFloat());
		}
		
		service.createPaper(paper, quesids, qvalues);
	}
}
