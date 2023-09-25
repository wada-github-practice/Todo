package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@Controller
public class TopController {

	@Autowired
	UserService userService;

	@GetMapping("/login")
	public String index(Model model) {

		return "login";
	}

	@PostMapping("/check")
	public String loginCheck(@RequestParam(name = "name") String name, @RequestParam(name = "pass") String pass,
			RedirectAttributes redirectAttributes) {
		// todo ユーザーが存在しない場合の例外処理
		User user = userService.getByUsername(name);
		if (user == null) {
			redirectAttributes.addAttribute("loginfail", "ログインに失敗しました。登録したユーザー名とパスワードを入力してください。");
			return "redirect:/login";
		}

		if (user.getUsername().matches(name) && user.getPass().matches(pass)) {
			return "redirect:/todo/list/" + name;
		} else {
			redirectAttributes.addAttribute("loginfail", "ログインに失敗しました。登録したユーザー名とパスワードを入力してください。");
			return "redirect:/login";
		}
	}

	@GetMapping("/user")
	public String user(Model model) {
		List<User> user = userService.getByUserList();
		model.addAttribute("userlist", user);
		return "UserList";
	}
}
