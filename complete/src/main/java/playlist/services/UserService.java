package playlist.services;


import playlist.entity.User;

public interface UserService {

	public User findUserByEmail(String email);	
	
	public void saveUser(User user);
	
}
