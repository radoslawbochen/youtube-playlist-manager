package playlist.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;

import playlist.repositories.YoutubeUserRepository;
import playlist.security.Auth;

@Service
public class YoutubeAccountServiceImpl implements YoutubeAccountService {

	@Autowired UserService userService;
	
	@Override
	public String getChannelId() throws IOException {
		Credential credential = Auth.getFlow().loadCredential(Auth.getUserId(userService));

		String userId = Auth.getUserId(userService);
		String channelId = YoutubeUserRepository.getChannelId(credential, userId);
		return channelId;
	}	

}
