package com.auto.ecole.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auto.ecole.entity.*;
import com.auto.ecole.repo.SettingRepository;
import com.auto.ecole.repo.UserRepository;

import net.bytebuddy.utility.RandomString;

@Controller
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SettingRepository settingRepository;
	@GetMapping("/login")
	public String viewLoginPage(Model model) throws IOException {
		List<Setting> Setting = (List<Setting>) settingRepository.findAll();
		if(Setting.isEmpty()) {
			Setting s = new Setting();
			s.setNomecole("Nom auto Ecole");
			s.setEmail("Contact@auto.ecole");
			s.setTel("00216 70 000 000");
			var imgFile = new ClassPathResource("logo.png");
		    byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
			s.setLogo(bytes);
			settingRepository.save(s);
			Setting Setting1 = settingRepository.findTopByOrderByIdDesc();
			model.addAttribute("setting", Setting1);
		}
		return "auto/login";
	}
	


}

