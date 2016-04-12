package next.controller;


import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.fasterxml.jackson.annotation.JsonView;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Result;
import next.model.User;

@RestController
@RequestMapping("/api/qna/")
public class ApiController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	private QuestionDao questionDao = QuestionDao.getInstance();
	private AnswerDao answerDao = AnswerDao.getInstance();
	
	@RequestMapping("/deleteQuestion")
	public String deleteQuestion() throws Exception {
		return null;
	}
	
	@RequestMapping("/list")
	public String listQuestion() throws Exception {
		return null;
	}
	
	@RequestMapping(value = "/addAnswer", method=RequestMethod.POST)
	public ModelAndView addAnswer(@RequestParam String contents, @RequestParam String questionId, HttpSession session) throws Exception {
		logger.debug("addAnswer post method call this method");
		
		if (!UserSessionUtils.isLogined(session)) {
			return new ModelAndView("jsonview").addObject("result",	Result.fail("need login"));
		}
		
		User user = UserSessionUtils.getUserFromSession(session);
		Answer answer = new Answer(user.getUserId(), contents, Long.parseLong(questionId));
		logger.debug("answer: {} ", answer);
		
		Answer savedAnswer = answerDao.insert(answer);
		questionDao.updateCountOfAnswer(savedAnswer.getQuestionId());
		
		return new ModelAndView("jsonview").addObject("answer", savedAnswer).addObject("result",Result.ok());
	}
	
	@RequestMapping("/deleteAnswer")
	public String deleteAnswer() throws Exception {
		return null;
	}
	
	

}
