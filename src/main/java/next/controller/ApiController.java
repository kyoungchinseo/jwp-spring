package next.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import next.annotation.LoginUser;
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
	public Map<String, Object> addAnswer(@RequestParam String contents, @RequestParam String questionId, @LoginUser User loginUser) throws Exception {
		logger.debug("addAnswer post method call this method");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (loginUser == null) {
			resultMap.put("result", Result.fail("Login is required"));
			return resultMap;
		}
		
		Answer answer = new Answer(loginUser.getUserId(), contents, Long.parseLong(questionId));
		logger.debug("answer: {} ", answer);
		
		Answer savedAnswer = answerDao.insert(answer);
		questionDao.updateCountOfAnswer(savedAnswer.getQuestionId());
		
		resultMap.put("answer", savedAnswer);
		resultMap.put("result", Result.ok());
		return resultMap;
	}
	
	@RequestMapping(value="/deleteAnswer", method=RequestMethod.POST)
	public Map<String,Object> deleteAnswer(@RequestParam String answerId) throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		long id = Long.parseLong(answerId);
		try {
			answerDao.delete(id);
			resultMap.put("result", Result.ok());
		} catch(Exception e) {
			resultMap.put("result", Result.fail(e.getMessage()));
		}
		
		return resultMap;
	}
	
	

}
