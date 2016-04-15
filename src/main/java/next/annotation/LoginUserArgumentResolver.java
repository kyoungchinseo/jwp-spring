package next.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import next.controller.UserSessionUtils;
import next.model.User;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

	private static final Logger logger = LoggerFactory.getLogger(LoginUserArgumentResolver.class);
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoginUser.class);

	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		Object user = null;
		//Object user = webRequest.getAttribute(USER_SESSION_KEY,  WebRequest.SCOPE_SESSION);
		user = webRequest.getAttribute("user", WebRequest.SCOPE_SESSION);
		//
		//user = UserSessionUtils.getUserFromSession(webRequest);
		logger.debug("user: {}",user);
		
		return user;
	}

}
