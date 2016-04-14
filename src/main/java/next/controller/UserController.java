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

import next.dao.UserDao;
import next.model.User;

@Controller
@RequestMapping("/users")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private UserDao userDao = UserDao.getInstance();
	
	@RequestMapping(value="", method = RequestMethod.GET)
	public ModelAndView index(HttpSession session) throws Exception {
		if (!UserSessionUtils.isLogined(session)) {
			return new ModelAndView("redirect:/users/loginForm");
		}
		
		ModelAndView mav = new ModelAndView("/user/list");
		mav.addObject("users",userDao.findAll());
		return mav;
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String form() throws Exception {
		logger.debug("inside");
		return "/user/form";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String create(User user) throws Exception {
		//User user = new User (userId, password, name, email);
		userDao.insert(user);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm() throws Exception {
		return "/user/login";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(User user, HttpSession session) throws Exception {
		
		User loginUser = userDao.findByUserId(user.getUserId());
		
		if (loginUser == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        
        if (loginUser.matchPassword(user.getPassword())) {
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
	public ModelAndView edit(@PathVariable String id, HttpSession session) throws Exception {
		
		User user = userDao.findByUserId(id);
		if (!UserSessionUtils.isSameUser(session, user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		
		ModelAndView mav = new ModelAndView("/user/updateForm");
		mav.addObject("user",user);
		return mav;
	}
	
	@RequestMapping(value="/{id}", method= RequestMethod.PUT)
	public String update(@PathVariable String id, User user, HttpSession session) throws Exception {
		//User updateUser = userDao.findByUserId(user.getUserId());
		
		if (!UserSessionUtils.isSameUser(session, user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		
		User updateUser = new User(user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
		userDao.update(updateUser);
		
		return "redirect:/";
	}
	
	/*
	// mappings.put("/users/form", new ForwardController("/user/form.jsp"));
	//    mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
	//    mappings.put("/users", new ListUserController());
	//	mappings.put("/users/login", new LoginController());
	//	mappings.put("/users/profile", new ProfileController());
	//    mappings.put("/users/logout", new LogoutController());
	//    mappings.put("/users/create", new CreateUserController());
	//    mappings.put("/users/updateForm", new UpdateFormUserController());
	//    mappings.put("/users/update", new UpdateUserController());
	 */
	
}
