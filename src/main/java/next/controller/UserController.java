package next.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import next.annotation.LoginUser;
import next.dao.UserDao;
import next.model.User;

@Controller
@RequestMapping("/users")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private UserDao userDao = UserDao.getInstance();
	
	@RequestMapping(value="", method = RequestMethod.GET)
	public String index(@LoginUser User loginUser, Model model) throws Exception {
		logger.debug("list");
		logger.debug("loginUser: {}", loginUser);
		if (loginUser == null) {
			model.addAttribute("user", new User());
			return "redirect:/users/login";
		}
		
		model.addAttribute("users",userDao.findAll());
		return "/user/list";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String form(Model model) throws Exception {
		logger.debug("inside");
		model.addAttribute("user", new User());
		return "/user/form";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String create(User user) throws Exception {
		userDao.insert(user);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm(Model model) throws Exception {
		model.addAttribute("user", new User());
		return "/user/login";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(User user, HttpSession session) throws Exception {
		
		User loginUser = userDao.findByUserId(user.getUserId());
		
		if (loginUser == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        
        if (loginUser.matchPassword(user.getPassword())) { // 사용자가 있고 패스워드까지 맞으면 세션 등
            session.setAttribute("user", loginUser);
        	return "redirect:/";
        } else {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public String profile(@PathVariable String id, Model model) throws Exception {
		model.addAttribute("user", userDao.findByUserId(id));
		return "/user/profile";
	}
	 
	@RequestMapping(value="/logout", method= RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		session.removeAttribute("user");
		//return "redirect:/qna/list";
		return "redirect:/";
	}
	
	@RequestMapping(value="/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable String id, @LoginUser User loginUser, Model model) throws Exception {
		
		User user = userDao.findByUserId(id);
		if (!user.isSameUser(loginUser)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		
		model.addAttribute("user", user);
		return "/user/updateForm";
	}
	
	@RequestMapping(value="/{id}", method= RequestMethod.PUT)
	public String update(@PathVariable String id, User user, @LoginUser User loginUser) throws Exception {
		if (!user.isSameUser(loginUser)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		
		User updateUser = new User(user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
		userDao.update(updateUser);
		
		return "redirect:/";
	}
	
}
