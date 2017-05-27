package playlist.services;

import org.springframework.beans.factory.annotation.Autowired;

import playlist.entity.User;
import playlist.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User findUserByEmail(String email) {		
		return userRepo.findByEmail(email);
	}

	@Override
	public void saveUser(User user) {		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
		userRepo.save(user);
	}

}
