package com.auto.ecole.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.auto.ecole.entity.*;
import com.auto.ecole.repo.*;

import net.bytebuddy.utility.RandomString;

@Controller
public class SettingController {
	@Autowired
	private SettingRepository settingRepository;
	
     
	@GetMapping("/settings")  
	public String viewSettingPage(Model model) throws IOException {
		
				Setting Setting1 = settingRepository.findTopByOrderByIdDesc();
				model.addAttribute("setting", Setting1);
			
			return "auto/settings";
	}
	
	@PostMapping("/savesetting")
	public String saveApps(Model model,@ModelAttribute("setting") @Validated  Setting setting ,  BindingResult result
			,@RequestParam("file") MultipartFile logo
			) throws IOException {
		String s = Base64.getEncoder().encodeToString(logo.getBytes());
		System.out.println("s:"+s);
		Setting Setting = settingRepository.findTopByOrderByIdDesc();
		setting.setId(Setting.getId());
		if(s.isEmpty()) {
			var imgFile = new ClassPathResource("logo.png");
		    byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		    setting.setLogo(bytes);
		}else {
			setting.setLogo(logo.getBytes());
		}
		settingRepository.save(setting);
	    return "redirect:/settings";
	}
	@GetMapping("/setting/display/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Setting> Setting)
			throws ServletException, IOException {
		
		Setting = settingRepository.findById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(Setting.get().getLogo());
		response.getOutputStream().close();
	}

}

