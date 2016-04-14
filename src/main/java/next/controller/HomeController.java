package next.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import next.dao.QuestionDao;

@Controller
public class HomeController {
	private QuestionDao questionDao = QuestionDao.getInstance();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) throws Exception {
		model.addAttribute("questions",questionDao.findAll());
		return "index";
	}
}
