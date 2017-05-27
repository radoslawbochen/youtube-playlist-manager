package playlist.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import playlist.entity.User;
import playlist.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public String user(Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		model.addAttribute("user", user);

		return "user";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLogin(){
		
		return "login";
	}
			
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String registration(Model model){
		User user = new User();
		model.addAttribute("user", user);

		return "registration";
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.user",
							"There is already a user registered with the given email");
		}
		if (bindingResult.hasErrors()) {
			
			return "registration";
		} else {
			userService.saveUser(user);
			model.addAttribute("successMessage", "User has been registered successfully");
			model.addAttribute("user", new User());

			return "registration";			
		}
	}
}
