package playlist.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import playlist.entity.usermadePlaylist.UsermadePlaylist;
import playlist.services.UsermadePlaylistService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@Transactional
public class UsermadePlaylistServiceTest {

	@Autowired
	private UsermadePlaylistService usermadePlaylistService;
	
	@Test
	public void compareTest(){
		String[] filesNamesToCompare =  {"fileName1", "fileName2", "fileName3", "fileName4"};
				
		List<UsermadePlaylist> usermadePlaylist = new ArrayList<>();
		UsermadePlaylist video1 = new UsermadePlaylist();
		video1.setVideoTitle("fileName1");
		UsermadePlaylist video2 = new UsermadePlaylist();
		video2.setVideoTitle("fileName2");
		UsermadePlaylist video3 = new UsermadePlaylist();
		video3.setVideoTitle("fileName5");		
		UsermadePlaylist video4 = new UsermadePlaylist();
		video4.setVideoTitle("fileName66666");
		usermadePlaylist.addAll(Arrays.asList(video1, video2, video3, video4));
		
		List<UsermadePlaylist> compared = usermadePlaylistService.compare(filesNamesToCompare, usermadePlaylist);
		
		assertEquals(false , compared.contains(video1));
		assertEquals(false , compared.contains(video2));
		assertEquals(false , compared.contains(video3));
		assertEquals(true , compared.contains(video4));	
	}
	
}
