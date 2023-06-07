package com.myweb.www.contorller;


import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myweb.www.domain.BoardVO;
import com.myweb.www.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/board/*")
@Slf4j
@Controller
public class BoardContorller {
	private static final Logger log = LoggerFactory.getLogger(BoardContorller.class);
	
	@Inject
	private BoardService bsv;
	
	@GetMapping("/register")
	public String registerGet() {
		return "/board/register";
	}
	
	@PostMapping("/register")
	public String registerPost(BoardVO bvo, RedirectAttributes rAtt) {
		log.info(" >>>>> bvo >>>>> : " + bvo.toString());
		int isOk = bsv.register(bvo);
		log.info(" >>>>> board register >>>>> : " + (isOk > 0 ? "성공" : "실패"));
		rAtt.addFlashAttribute("isOk", isOk);
		return "redirect:/board/list";
	}
	
	// insert update delete -> redirect 처리
	// RedirectAttributes 객체사용 : 데이터의 새로고침
	
	@GetMapping("/list")
	public String list(Model m) {
		List<BoardVO> list = bsv.getList();
		m.addAttribute("list", list);
		return "/board/list";
	}
	
	// detail을 가져와야 하는 케이스 : detail, modify
	@GetMapping({"/detail", "/modify"})
	public void detail(Model m, @RequestParam("bno")int bno) {
		log.info(" >>>>> bno >>>>> : " + bno);
		BoardVO bvo = bsv.getDetail(bno);
		m.addAttribute("board", bvo);
	}
	
	@PostMapping("/modify")
	public String update(RedirectAttributes rAtt, BoardVO bvo) {
		log.info(" >>>>> bvo >>>>> : " + bvo.toString());
		// 세션의 로그인 ID가 작성자와 일치하면 삭제 아니면 삭제불가
		// DB상 update 하기
		return "redirect:/board/list";
	}
	
	@GetMapping("/delete")
	public String delete(RedirectAttributes rAtt, @RequestParam("bno")int bno) {
		// DB상 update하기 isDel ='Y' => 삭제한글 처리
		
		return "redirect:/board/list";
	}
	
}
