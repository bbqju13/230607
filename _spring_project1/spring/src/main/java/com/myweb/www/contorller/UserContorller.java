package com.myweb.www.contorller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myweb.www.domain.UserVO;
import com.myweb.www.service.UserService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequestMapping("/member/*")
@Controller
public class UserContorller {
	
	private static final Logger log = LoggerFactory.getLogger(UserContorller.class);
	@Inject
	private UserService usv;
	
	@GetMapping("/signup")
	public String index(Model m) {
		log.info(" >>>>> HOME 접근 완료 >>>>>");
		m.addAttribute("msg_home", 1);
		return "/user/signup";
	}
	
	@PostMapping("/signup") // 데이터를 들고오는
	public String signUpPost(Model m, UserVO uvo) {
		log.info(" >>>>> 회원가입 접근완료 >>>>> ");
		log.info(" >>>>> 회원가입 >>>>> : "+ uvo.toString());
		int isOk = usv.signUp(uvo);
		if(isOk > 0) {
			m.addAttribute(" >>>>> msg_signUp", 1);
		}else {
			m.addAttribute(" >>>>> msg_signUp", 0);
		}
		return "home"; // 결과 페이지
	}
	
	@GetMapping("/login")
	public String loginGet() {
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String loginPost(Model m, String id, String pw, HttpServletRequest req) {
//		log.info(" >>>>> USER >>>>> : " + user.toString());
		log.info(" >>>>> ID >>>>> : " + id + "," + " >>>>> PW >>>>> : " + pw);
		// 파라미터로 받은 id pw를 DB에 넘겨 일치하는 객체를 받음.
		UserVO isUser = usv.isUser(id, pw);
//		log.info(" >>>>> isuser >>>>> : "+ isUser.toString());
		// DB의 객체가 null이 아니라면 세션에 연결해서 저장.
		if(isUser != null) {
			HttpSession ses = req.getSession();
			ses.setAttribute("ses", isUser); // 세션에 객체 담기
			ses.setMaxInactiveInterval(60*10); // 로그인 유지시간
			
			m.addAttribute("user", isUser);
			m.addAttribute("msg_login", 1);
		}else {
			m.addAttribute("msg_login", 0);
		}
		return "home";
	}
	
	@GetMapping("/logout")
	public String logout(Model m, HttpServletRequest req) {
		req.getSession().removeAttribute("ses");
		req.getSession().invalidate(); // 세션끊기
		m.addAttribute("msg_logout", 1);
		return "home";
	}
	
}
