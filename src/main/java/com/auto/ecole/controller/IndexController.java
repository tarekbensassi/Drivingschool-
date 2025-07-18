package com.auto.ecole.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.auto.ecole.entity.*;
import com.auto.ecole.repo.*;



@Controller
public class IndexController {
	
   private Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	private final EleveRepository eleveRepository;
	private final ElevedetailsRepository elevedetailsRepository;
	private final  SettingRepository settingRepository;
	
	public IndexController( EleveRepository eleveRepository,
			ElevedetailsRepository elevedetailsRepository,
			SettingRepository settingRepository
			
			) {
		      this.eleveRepository = eleveRepository;
		      this.elevedetailsRepository = elevedetailsRepository;
		      this.settingRepository = settingRepository;
		}
		

	
	@GetMapping("/index")
	public String viewIndexPage(Model model) {
		
			return "auto/index";
	}
	@GetMapping("/suivi")
	public String viewSuiviePage(Model model) {

		Setting Setting1 = settingRepository.findTopByOrderByIdDesc();
		model.addAttribute("setting", Setting1);
			return "auto/suivi";
	}
	@GetMapping("/suividetails")
	public String getdetails(Model model,@RequestParam("cin") String cin) {

		
	     
	    	Eleve eleves = eleveRepository.findEleveByCin(cin);
			model.addAttribute("eleves", eleves);

			Iterable<Elevedetails> elevedetails = elevedetailsRepository.getElevedetailsByEleve(eleves);
			model.addAttribute("elevedetails", elevedetails);
			System.out.print("elevedetails"+elevedetails);
			
			Setting Setting1 = settingRepository.findTopByOrderByIdDesc();
			model.addAttribute("setting", Setting1);
			
			return "auto/suividetails";
	}
	
	

	@PostMapping("/saveeleve")
	public String saveApps(Model model,@ModelAttribute("eleve") @Validated  Eleve eleve ,  BindingResult result
			
			) throws IOException {	


		eleveRepository.save(eleve);
	    return "redirect:/suivi";
	}
	

	@PostMapping("/savedetails")
	public String saveApps(Model model,@ModelAttribute("Elevedetails") @Validated  Elevedetails Elevedetails ,  BindingResult result
		) throws IOException {
		 Eleve eleve = eleveRepository.findById(Elevedetails.getId()).get();
		 Elevedetails ed =new Elevedetails();
		 ed.setDate(new Date());
		 ed.setPrix(Elevedetails.getPrix());
		 ed.setStatus(Elevedetails.getStatus());
		 ed.setEleve(eleve);
		 elevedetailsRepository.save(ed);
		
		
		  Eleve e = eleveRepository.findById(Elevedetails.getId()).get();
			if(Elevedetails.getStatus().equals("Debit") ) // paiement
			{
				 e.setReste(e.getReste()-Elevedetails.getPrix());
				 eleveRepository.save(e);
				 
			}else if(Elevedetails.getStatus().equals("Credit"))//credit de montant
			{    e.setReste(e.getReste()+Elevedetails.getPrix());
				 e.setTotal(e.getTotal()+Elevedetails.getPrix());
				 eleveRepository.save(e);
			}
			
	
		
		
	    return "redirect:/suivi";
	}

	


}

