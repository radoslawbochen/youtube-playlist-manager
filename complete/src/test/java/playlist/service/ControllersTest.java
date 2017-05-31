package playlist.service;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow.Builder;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.entity.usermadePlaylist.UsermadePlaylistWrapper;
import playlist.entity.youtubePlaylist.YoutubePlaylistInfo;
import playlist.services.UserService;
import playlist.services.UsermadePlaylistService;
import playlist.services.YoutubePlaylistService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@Transactional
public class ControllersTest {
	

	private MockMvc mockMvc;
	
	@Autowired
    private UserService userServiceMock;
	@Autowired
	private UsermadePlaylistService usermadePlaylistServiceMock;
	@Autowired
	private YoutubePlaylistService youtubePlaylistServiceMock;
	
	@Test
	@WithMockUser(username = "username", roles={"default"})
	public void testPlaylistView() throws Exception {
		String channelId = "TestChannelIdYoutubeAPI";
		String playlistName = "Test PlaylistName";				
		
		UsermadePlaylist playlist1 = new UsermadePlaylist(10L);		
		UsermadePlaylist playlist2 = new UsermadePlaylist(11L);
		UsermadePlaylist playlist3 = new UsermadePlaylist(10L, channelId, playlistName);
		UsermadePlaylistWrapper usermadePlaylistWrapper = new UsermadePlaylistWrapper();
		ArrayList<UsermadePlaylist> usermadePlaylists = new ArrayList<>(); 
		usermadePlaylists.addAll(Arrays.asList(playlist1, playlist2));
		usermadePlaylistWrapper.setUsermadePlaylists(usermadePlaylists);
		
		//when(usermadePlaylistServiceMock.delete(usermadePlaylists, playlistName)).
		when(usermadePlaylistServiceMock.saveUsermadePlaylist(playlist3)).thenReturn(playlist3);
				 
		mockMvc.perform(
				post("/viewPlaylist")		
					.param("add", playlistName)			
				)
				.andExpect(status().isOk())
				.andExpect(redirectedUrl("/viewPlaylist?playlist=" + playlistName));	
		
		verify(usermadePlaylistServiceMock, times(1)).saveUsermadePlaylist(new UsermadePlaylist(10L, channelId, playlistName));
		verifyNoMoreInteractions(usermadePlaylistServiceMock);
	}
	
}
	
	
	
	
	
	
	
	/*
	@Autowired
	UsermadePlaylistRepository usermadePlaylistRepo;
	
    @Mock
    UsermadePlaylistServiceImpl usermadePlaylistService;

    
	@Test
	public void testAdd() throws Exception{
		List<User> mockedUsers = new ArrayList<>();
		mockedUsers.add(new User(
				10L,
				"channelId", 
				"playlistName", 
				"link", 
				0, 
				"videoTitle"
				));
		mockedUsers.add(new User(
				10L,
				"channelId", 
				"playlistName", 
				"link", 
				0, 
				"videoTitle"
				));
		usermadePlaylistService.addAll(mockedUsers);
		Long[] ids = new Long[mockedUsers.size()];
		for(int i = 0; i < mockedUsers.size(); i++){			
			ids[i] = mockedUsers.get(i).getId();
		}		
		
		List<User> users = usermadePlaylistService.findById(ids);
		for(int i = 0; 0 < users.size(); i++){			
			Assert.assertEquals(users.get(i), mockedUsers.get(i));
		}
		
	}
	
	
}
*/
