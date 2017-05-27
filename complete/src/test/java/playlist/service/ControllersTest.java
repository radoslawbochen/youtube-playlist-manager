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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
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
import playlist.entity.youtubePlaylist.YoutubePlaylistInfo;
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
	private UsermadePlaylistService usermadePlaylistServiceMock;
	@Autowired
	private YoutubePlaylistService youtubePlaylistServiceMock;
	
	@Test
	public void testPlaylistView() throws Exception {
		MockHttpSession session = new MockHttpSession();
		String channelId = "TestChannelIdYoutubeAPI";
		String playlistName = "Test PlaylistName";
				
		YoutubePlaylistInfo youtubePlaylistInfo1 = new YoutubePlaylistInfo(
				"playlistName",
				11,
				"playlistId"
				);
		
		YoutubePlaylistInfo youtubePlaylistInfo2 = new YoutubePlaylistInfo(
				"playlistName",
				12,
				"playlistId"
				);
		
		UsermadePlaylist playlist1 = new UsermadePlaylist(
				10L, 
				channelId, 
				playlistName, 
				"link1", 
				2,
				"videoTitle1"
				);
		
		UsermadePlaylist playlist2 = new UsermadePlaylist(
				10L, 
				channelId, 
				playlistName, 
				"link2", 
				2,
				"videoTitle2"
				);
		
		when(youtubePlaylistServiceMock.findYoutubePlaylistsInfo(session, playlistName)).thenReturn(Arrays.asList(youtubePlaylistInfo1, youtubePlaylistInfo2));
		when(usermadePlaylistServiceMock.findByChannelIdAndPlaylistName(channelId, playlistName)).thenReturn(Arrays.asList(playlist1, playlist2));	
	
		mockMvc.perform(
				get("/viewPlaylist")
					.sessionAttr("channelId", "TestChannelIdYoutubeAPI")
				)
				.andExpect(status().isOk())
				.andExpect(model().attribute("youtubePlaylistInfoList", hasItem(
						allOf(
								hasProperty("name", is("playlistName")),
								hasProperty("videosAmount", is(11)),
								hasProperty("playlistId", is("playlistId"))
						)
						
						)))
				.andExpect(model().attribute("youtubePlaylistInfoList", hasItem(
						allOf(
								hasProperty("name", is("playlistName")),
								hasProperty("videosAmount", is(12)),
								hasProperty("playlistId", is("playlistId"))
						)						
						
						)))
				.andExpect(model().attribute("usermadePlaylistInfoList", hasItem(
						allOf(
								hasProperty("id", is(10L)),
								hasProperty("channelId", is("channelId")),
								hasProperty("playlistName", is("playlistName")),
								hasProperty("link", is("link1")),
								hasProperty("timesRepeated", is(2)),
								hasProperty("videoTitle", is("videoTitle1"))
						)	
						)))
				.andExpect(model().attribute("usermadePlaylistInfoList", hasItem(
						allOf(
								hasProperty("id", is(10L)),
								hasProperty("channelId", is("channelId")),
								hasProperty("playlistName", is("playlistName")),
								hasProperty("link", is("link2")),
								hasProperty("timesRepeated", is(2)),
								hasProperty("videoTitle", is("videoTitle2"))
						)	
						)))
				;		
		
		verify(youtubePlaylistServiceMock, times(1)).findYoutubePlaylistsInfo(session, playlistName);
		verify(usermadePlaylistServiceMock, times(1)).findByChannelIdAndPlaylistName(channelId, playlistName);
		verifyNoMoreInteractions(youtubePlaylistServiceMock);
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
