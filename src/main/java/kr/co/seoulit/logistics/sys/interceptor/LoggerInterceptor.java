package kr.co.seoulit.logistics.sys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class LoggerInterceptor extends HandlerInterceptorAdapter{

	private final Log log = LogFactory.getLog(getClass());
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.debug("======================================          START         ======================================");
		log.debug(" Request URI \t:  " + request.getRequestURI());
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		log.debug("======================================           END          ======================================\n");
	}
}
