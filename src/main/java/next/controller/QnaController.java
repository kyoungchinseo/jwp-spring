package next.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import next.CannotDeleteException;
import next.annotation.LoginUser;
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
	public String questionForm(@LoginUser User loginUser, Model model) throws Exception {
		//if (!UserSessionUtils.isLogined(session)) {
		if (loginUser == null) {
			model.addAttribute("user", new User());
			return "redirect:/users/login";
		}
		model.addAttribute("question", new Question());
		return "/qna/form";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	//public String createQuestion(@RequestParam String title, @RequestParam String contents, @LoginUser User loginUser, Model model) throws Exception {
	public String createQuestion(Question question, @LoginUser User loginUser, Model model) throws Exception {
		//if (!UserSessionUtils.isLogined(session)) {
		if (loginUser == null) {
			model.addAttribute("user", new User());
			return "redirect:/users/login";
		}
		
		//User user = UserSessionUtils.getUserFromSession(session);
		Question createdQuestion = new Question(loginUser.getUserId(), question.getTitle(), question.getContents());
		questionDao.insert(createdQuestion);
		
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/updateForm", method=RequestMethod.GET)
	public String updateQuestionForm(@RequestParam String questionId, @LoginUser User loginUser, Model model) throws Exception {
		//if (!UserSessionUtils.isLogined(session)) {
		if (loginUser == null) {
			model.addAttribute("user", new User());
			return "redirect:/users/login";
		}
		
		long id = Long.parseLong(questionId);
		Question question = questionDao.findById(id);
		//if (!question.isSameUser(UserSessionUtils.getUserFromSession(session))) {
		if (!question.isSameUser(loginUser)) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}
		
		model.addAttribute("question", question);
		return "/qna/update";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String updateQuestion(Question question, @LoginUser User loginUser, Model model) throws Exception {
		
		//if (!UserSessionUtils.isLogined(session)) {
		if (loginUser == null) {
			model.addAttribute("user", new User());
			return "redirect:/users/login"; 
		}
		
		long id = question.getQuestionId();
		Question oldQuestion = questionDao.findById(id);
		//if (!question.isSameUser(UserSessionUtils.getUserFromSession(session))) {
		if (!oldQuestion.isSameUser(loginUser)) {
			throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
		}
		
		Question newQuestion = new Question(oldQuestion.getWriter(), question.getTitle(), question.getContents());
		oldQuestion.update(newQuestion);
		questionDao.update(oldQuestion);
		return "redirect:/";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public ModelAndView deleteQuestion(@RequestParam String questionId, @LoginUser User loginUser) throws Exception {
		//if (!UserSessionUtils.isLogined(session)) {
		if (loginUser == null) {
			return new ModelAndView("redirect:/users/loginForm");
		}
		
		long id = Long.parseLong(questionId);
		try {
			qnaService.deleteQuestion(id, loginUser);
			return new ModelAndView("redirect:/");
		} catch(CannotDeleteException e) {
			return new ModelAndView("/qna/show").addObject("question", qnaService.findById(id)).addObject("answers", qnaService.findAllByQuestionId(id))
					.addObject("errorMessage", e.getMessage());
		}
		
	}
																																																																																																																																																																																																																																																																																																																																																																																																		
}
