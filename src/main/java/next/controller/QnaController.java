package next.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import next.CannotDeleteException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;
import next.service.QnaService;

@Controller
@RequestMapping("/qna")
public class QnaController {
	
	private static final Logger logger = LoggerFactory.getLogger(QnaController.class);
	
	private QuestionDao questionDao = QuestionDao.getInstance();
	private AnswerDao answerDao = AnswerDao.getInstance();
	private QnaService qnaService = QnaService.getInstance();
	
	@RequestMapping(value="/show", method= RequestMethod.GET)
	public ModelAndView show(@RequestParam String questionId) throws Exception {
		long id = Long.parseLong(questionId);
		
		Question question = questionDao.findById(id);
		List<Answer> answers = answerDao.findAllByQuestionId(id);
		
		ModelAndView mav = new ModelAndView("/qna/show");
		mav.addObject("question", question);
		mav.addObject("answers", answers);
		
		return mav;
	}

	@RequestMapping(value="/form", method=RequestMethod.GET)
	public String questionForm(HttpSession session) throws Exception {
		if (!UserSessionUtils.isLogined(session)) {
			return "redirect:/users/loginForm";
		}
		return "/qna/form";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createQuestion(@RequestParam String title, @RequestParam String contents, HttpSession session) throws Exception {
		if (!UserSessionUtils.isLogined(session)) {
			return "redirect:/users/loginForm";
		}
		
		User user = UserSessionUtils.getUserFromSession(session);
		Question question = new Question(user.getUserId(), title, contents);
		questionDao.insert(question);
		
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/updateForm", method=RequestMethod.GET)
	public ModelAndView updateQuestionForm(@RequestParam String questionId, HttpSession session) throws Exception {
		if (!UserSessionUtils.isLogined(session)) {
			return new ModelAndView("redirect:/users/loginForm");
		}
		
		long id = Long.parseLong(questionId);
		Question question = questionDao.findById(id);
		if (!question.isSameUser(UserSessionUtils.getUserFromSession(session))) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}
		
		return new ModelAndView("/qna/update").addObject("question",question);
		
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String updateQuestion(@RequestParam String questionId, @RequestParam String title, @RequestParam String contents, HttpSession session) throws Exception {
		
		if (!UserSessionUtils.isLogined(session)) {
			return "redirect:/users/loginForm"; 
		}
		
		long id = Long.parseLong(questionId);
		Question question = questionDao.findById(id);
		if (!question.isSameUser(UserSessionUtils.getUserFromSession(session))) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}
		
		Question newQuestion = new Question(question.getWriter(), title, contents);
		question.update(newQuestion);
		questionDao.update(question);
		return "redirect:/";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public ModelAndView deleteQuestion(@RequestParam String questionId, HttpSession session) throws Exception {
		if (!UserSessionUtils.isLogined(session)) {
			return new ModelAndView("redirect:/users/loginForm");
		}
		
		long id = Long.parseLong(questionId);
		try {
			qnaService.deleteQuestion(id, UserSessionUtils.getUserFromSession(session));
			return new ModelAndView("redirect:/");
		} catch(CannotDeleteException e) {
			return new ModelAndView("/qna/show").addObject("question", qnaService.findById(id)).addObject("answers", qnaService.findAllByQuestionId(id))
					.addObject("errorMessage", e.getMessage());
		}
		
	}
	
																																																																																																																																																																																																																																																																																																																																																																																																		
	/*
	// mappings.put("/qna/show", new ShowQuestionController());
	//	mappings.put("/qna/form", new CreateFormQuestionController());
	//	mappings.put("/qna/create", new CreateQuestionController());
	//	mappings.put("/qna/updateForm", new UpdateFormQuestionController());
	//	mappings.put("/qna/update", new UpdateQuestionController());
	//	mappings.put("/qna/delete", new DeleteQuestionController());
	//	mappings.put("/api/qna/deleteQuestion", new ApiDeleteQuestionController());
		mappings.put("/api/qna/list", new ApiListQuestionController());
		mappings.put("/api/qna/addAnswer", new AddAnswerController());
		mappings.put("/api/qna/deleteAnswer", new DeleteAnswerController()); 
	 */
	
}
