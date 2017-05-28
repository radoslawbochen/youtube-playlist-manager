package playlist.repositories;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.PlaylistSnippet;

import playlist.controller.PlaylistController;
import playlist.entity.PlaylistItemInfo;
import playlist.entity.youtubePlaylist.YoutubePlaylist;
import playlist.entity.youtubePlaylist.YoutubePlaylistInfo;
import playlist.security.Auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Print a list of videos uploaded to the authenticated user's YouTube channel.
 *
 * @author Jeremy Walker
 */


public class YoutubeUserRepository {
	
    public static String getChannelId(Credential credential, String userId){
    	String channelId = null;
		try {		    				
			YouTube youtube = new YouTube.Builder(
			    	Auth.HTTP_TRANSPORT, 
			   		Auth.JSON_FACTORY, 
			   		credential
			   		)
			   		.setApplicationName(
		            "youtube-cmdline-user-playlists").build();
				
		    YouTube.Channels.List channelIdRequest = youtube.channels().list("id");
			channelIdRequest = youtube.channels().list("id");
			channelIdRequest.setMine(true);
	        channelIdRequest.getId();
	        ChannelListResponse channelIdResult = channelIdRequest.execute();
	        channelId = channelIdResult.getItems().get(0).getId();
	        channelId = channelId.replace("-", "");
			} catch (IOException e) {
				e.printStackTrace();
			}
		if (channelId.isEmpty()){
			PlaylistController.redirectToOauthLogin();
		}
			
		return channelId;
    }
    
    public static List<YoutubePlaylistInfo> fetchPlaylistsInfoList(Credential credential, String channelId, String userId) {
        List<YoutubePlaylistInfo> youtubePlaylistInfoList = new ArrayList<YoutubePlaylistInfo>();
       		
	    List<YoutubePlaylist> playlists = fetchPlaylistList(credential, userId);
	        
	    if (playlists != null) {
	       for(YoutubePlaylist playlist : playlists) {                         
	       		int playlistSize = playlist.getPlaylistItemsInfoList().size();
	                               
	       		youtubePlaylistInfoList.add(new YoutubePlaylistInfo(
	         	playlist.getName(),
	        	playlistSize,
	         	playlist.getId()));                                
	       }
	     }
	        
    	return youtubePlaylistInfoList;    	
    }

	public static List<YoutubePlaylist> fetchPlaylistList(Credential credential, String userId) {
        List<YoutubePlaylist> youtubePlaylistList = new ArrayList<YoutubePlaylist>();
        
        YouTube.Playlists.List searchList;
        YouTube.Channels.List channelsList;
        
		try {
			YouTube youtube = new YouTube.Builder(
		    		Auth.HTTP_TRANSPORT, 
		    		Auth.JSON_FACTORY, 
		    		credential
		    		)
		    		.setApplicationName(
		            "youtube-cmdline-user-playlists").build();
			
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("part", "id,snippet,contentDetails");
			searchList = youtube.playlists().list(parameters.get("part").toString());
			searchList.setFields("etag,eventId,items(contentDetails,etag,id,kind,player,snippet,status),kind,nextPageToken,pageInfo,prevPageToken,tokenPagination");
	        searchList.setMine(true);
	        PlaylistListResponse playlistListResponse = searchList.execute();
	        List<Playlist> playlists = new ArrayList<Playlist>();

	        // User channel related playlists
	        
			channelsList = youtube.channels().list(parameters.get("part").toString());
	        channelsList.setMine(true);
	        ChannelListResponse channelListResponse = channelsList.execute();
	        List<Channel> userChannelList = channelListResponse.getItems();
	        Channel userChannel = userChannelList.get(0);
	        Playlist likesPlaylist = new Playlist();
	        Playlist uploadsPlaylist = new Playlist();
	        likesPlaylist.setId(userChannel.getContentDetails().getRelatedPlaylists().getLikes());
	        uploadsPlaylist.setId(userChannel.getContentDetails().getRelatedPlaylists().getUploads());
	        PlaylistSnippet likesPlaylistSnippet = new PlaylistSnippet();
	        PlaylistSnippet uploadsPlaylistSnippet = new PlaylistSnippet();
	        likesPlaylistSnippet.setTitle("Likes");
	        uploadsPlaylistSnippet.setTitle("Uploads");
	        likesPlaylist.setSnippet(likesPlaylistSnippet);
	        uploadsPlaylist.setSnippet(uploadsPlaylistSnippet);
	        playlists.add(likesPlaylist);
	        playlists.add(uploadsPlaylist);	        
	        
	        // User playlists
	        
			playlists.addAll(playlistListResponse.getItems());
			
	        if (playlists != null) {
	        	for (Playlist playlist : playlists) {
	        		String playlistId = playlist.getId();
	        		youtubePlaylistList.add(new YoutubePlaylist(
	        				playlist.getSnippet().getTitle(),
                            playlistId,
                            getPlaylistItemsInfo(credential, userId, playlistId)
	                        ));
	        		}	                           
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}     
        
    	return youtubePlaylistList;
	}	
		
	
	static List<PlaylistItemInfo> getPlaylistItemsInfo(Credential credential, String userId, String playlistId){
		
		List<PlaylistItemInfo> playlistItemsInfoList = new ArrayList<>();
		try {			
			YouTube youtube = new YouTube.Builder(
		    		Auth.HTTP_TRANSPORT, 
		    		Auth.JSON_FACTORY, 
		    		credential
		    		)
		    		.setApplicationName(
		            "youtube-cmdline-user-playlists").build();
			
		List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();
		playlistItemList.clear();

        YouTube.PlaylistItems.List playlistItemRequest;
		playlistItemRequest = youtube.playlistItems().list("id,contentDetails,snippet");
		playlistItemRequest.setPlaylistId(playlistId);
			 
	    playlistItemRequest.setFields(
	    		"items(contentDetails/videoId,snippet/title),nextPageToken,pageInfo");

	    String nextToken = "";

	    do {
	    	playlistItemRequest.setPageToken(nextToken);
	     	PlaylistItemListResponse playlistItemResult;
			playlistItemResult = playlistItemRequest.execute();
	       	playlistItemList.addAll(playlistItemResult.getItems());
	     	nextToken = playlistItemResult.getNextPageToken();
	        } while (nextToken != null);
	        
	        Iterator<PlaylistItem> itr = playlistItemList.iterator();
	        while(itr.hasNext()){
	           PlaylistItem playlistItem = itr.next();
	           playlistItemsInfoList.add(new PlaylistItemInfo(
	        		   playlistItem.getSnippet().getTitle(),
	        		   playlistItem.getContentDetails().getVideoId()
	        		   ));
	        }
        
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return playlistItemsInfoList;		
	}
		
}