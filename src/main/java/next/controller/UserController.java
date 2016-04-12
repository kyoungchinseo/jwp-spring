package next.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import next.dao.UserDao;
import next.model.User;

@Controller
@RequestMapping("/users")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private UserDao userDao = UserDao.getInstance();
	
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String createUserForm() throws Exception {
		logger.debug("inside");
		return "/user/form";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createUser(@RequestParam String userId, @RequestParam String password, @RequestParam String name, @RequestParam String email) throws Exception {
		
		User user = new User (userId, password, name, email);
		userDao.insert(user);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/loginForm", method = RequestMethod.GET)
	public String loginForm() throws Exception {
		return "/user/login";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(@RequestParam String userId, @RequestParam String password, HttpSession session) throws Exception {
		User user = userDao.findByUserId(userId);
		
		if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        
        if (user.matchPassword(password)) {
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }
	}
	
	@RequestMapping(value="/profile", method = RequestMethod.GET)
	public ModelAndView profile(@RequestParam String userId) throws Exception {
		ModelAndView mav = new ModelAndView("/user/profile");
		mav.addObject("user", userDao.findByUserId(userId));
		return mav;
	}
	 
	@RequestMapping(value="", method = RequestMethod.GET)
	public ModelAndView list(HttpSession session) throws Exception {
		if (!UserSessionUtils.isLogined(session)) {
			return new ModelAndView("redirect:/users/loginForm");
		}
		
		ModelAndView mav = new ModelAndView("/user/list");
		mav.addObject("users",userDao.findAll());
		return mav;
	}
	
	
	@RequestMapping(value="/logout", method= RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		session.removeAttribute("user");
		//return "redirect:/qna/list";
		return "redirect:/";
	}
	
	@RequestMapping(value="/updateForm", method = RequestMethod.GET)
	public ModelAndView updateUserForm(@RequestParam String userId, HttpSession session) throws Exception {
		
		User user = userDao.findByUserId(userId);
		if (!UserSessionUtils.isSameUser(session, user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		
		ModelAndView mav = new ModelAndView("/user/updateForm");
		mav.addObject("user",user);
		return mav;
	}
	
	@RequestMapping(value="/update", method= RequestMethod.POST)
	public String updateUser(@RequestParam String userId, @RequestParam String password, @RequestParam String name, @RequestParam String email, HttpSession session) throws Exception {
		User user = userDao.findByUserId(userId);
		
		if (!UserSessionUtils.isSameUser(session, user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		
		User updateUser = new User(userId, password, name, email);
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
