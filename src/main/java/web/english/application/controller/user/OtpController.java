package web.english.application.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Users;
import web.english.application.service.MyEmailService;
import web.english.application.service.OtpService;
import web.english.application.utils.EmailTemplate;
import web.english.application.utils.JwtHelper;
import web.english.application.utils.RoleType;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shrisowdhaman
 * Dec 15, 2017
 */
@Controller
@RequestMapping("/")
@Slf4j
public class OtpController {
	@Autowired
	public OtpService otpService;
	
	@Autowired
	public MyEmailService myEmailService;

	@Autowired
	public UserDAO userDAO;

	private JwtHelper jwtHelper=new JwtHelper();



	@GetMapping("/register/generateOtp")
	public String generateOtp(@ModelAttribute("msg") String msg, Model model, @ModelAttribute("users") Users users){
		String username = users.getUsername();

		int serverOtp = otpService.getOtp(username);
		log.info(serverOtp+"");
		log.info(users.getUsername());
		if (serverOtp == 0){
			int otp = otpService.generateOTP(username);

			log.info("OTP : "+otp);

			String message = "Your OTP is " + String.valueOf(otp);
			myEmailService.sendOtpMessage(users.getEmail(), "OTP -SpringBoot", message);
		}else {
			model.addAttribute("msg",msg);
		}
		model.addAttribute("users",users);
		return "otppage";
	}
	
	@PostMapping("/register/validateOtp")
	public String passvalidateOtp(@ModelAttribute("users") Users users,@RequestParam("otpnum") int otpnum, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest){
		
		log.info(" Otp Number : "+otpnum);
		log.info(users.toString());
		//Validate the Otp 
		if(otpnum >= 0){
			int serverOtp = otpService.getOtp(users.getUsername());
			
			if(serverOtp > 0){
				if(otpnum == serverOtp){
					otpService.clearOTP(users.getUsername());
					String users1 = userDAO.save(users);
					redirectAttributes.addFlashAttribute("success","Đăng ký thành công. Mời bạn đăng nhập.");
					return "redirect:/login";
				}else{
					log.info("false");
					redirectAttributes.addFlashAttribute("msg","Nhập OTP không chính xác.Mời nhập lại");
					redirectAttributes.addFlashAttribute("users",users);
					return "redirect:/register/generateOtp";
				}
			}else {
				redirectAttributes.addFlashAttribute("msg","Nhập OTP không chính xác.Mời nhập lại");
				redirectAttributes.addFlashAttribute("users",users);
				return "redirect:/register/generateOtp";
			}
		}else {
			redirectAttributes.addFlashAttribute("msg","Nhập OTP không chính xác.Mời nhập lại");
			redirectAttributes.addFlashAttribute("users",users);
			return "redirect:/register/generateOtp";
		}
	}

	@GetMapping("/user/password/generateOtp")
	public String passgenerateOtp(@ModelAttribute("msg") String msg, Model model, @ModelAttribute("users") Users users){
		String username = users.getUsername();

		int serverOtp = otpService.getOtp(username);
		log.info(serverOtp+"");
		log.info(users.getUsername());
		if (serverOtp == 0){
			int otp = otpService.generateOTP(username);

			log.info("OTP : "+otp);

			String message = "Your OTP is " + String.valueOf(otp);
			myEmailService.sendOtpMessage(users.getEmail(), "OTP -SpringBoot", message);
		}else {
			model.addAttribute("msg",msg);
		}
		model.addAttribute("users",users);
		return "otppassword";
	}

	@PostMapping("/user/password/validateOtp")
	public String validateOtp(@ModelAttribute("users") Users users,@RequestParam("otpnum") int otpnum, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest){

		log.info(" Otp Number : "+otpnum);
		log.info(users.toString());
		//Validate the Otp
		if(otpnum >= 0){
			int serverOtp = otpService.getOtp(users.getUsername());

			if(serverOtp > 0){
				if(otpnum == serverOtp){
					otpService.clearOTP(users.getUsername());
					Users users1 = userDAO.updatePassword(users);
					log.info(users1.getPassword());
					redirectAttributes.addFlashAttribute("success","Cập nhật thành công");
					return "redirect:/user/info/"+users1.getId();
				}else{
					log.info("false");
					redirectAttributes.addFlashAttribute("msg","Nhập OTP không chính xác.Mời nhập lại");
					redirectAttributes.addFlashAttribute("users",users);
					return "redirect:/user/password/generateOtp";
				}
			}else {
				redirectAttributes.addFlashAttribute("msg","Nhập OTP không chính xác.Mời nhập lại");
				redirectAttributes.addFlashAttribute("users",users);
				return "redirect:/user/password/generateOtp";
			}
		}else {
			redirectAttributes.addFlashAttribute("msg","Nhập OTP không chính xác.Mời nhập lại");
			redirectAttributes.addFlashAttribute("users",users);
			return "redirect:/user/password/generateOtp";
		}
	}
}
