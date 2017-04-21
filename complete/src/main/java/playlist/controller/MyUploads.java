package playlist.controller;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.PlaylistItems;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.common.collect.Lists;

import playlist.entity.YoutubePlaylist;
import playlist.entity.YoutubePlaylistInfo;
import playlist.security.Auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Print a list of videos uploaded to the authenticated user's YouTube channel.
 *
 * @author Jeremy Walker
 */

public class MyUploads {
	
	private UserContents userContents;
    private static YouTube youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, PlaylistController.credential).setApplicationName(
            "youtube-cmdline-user-playlists").build();
    public List<String> userPlaylistIdList = new ArrayList<String>();
    public List<Integer> userPlaylistSizeList = new ArrayList<Integer>();
    public List<String> userPlaylistNameList = new ArrayList<String>();
    /**
     * Authorize the user, call the youtube.channels.list method to retrieve
     * the playlist ID for the list of videos uploaded to the user's channel,
     * and then call the youtube.playlistItems.list method to retrieve the
     * list of videos in that playlist.
     *
     * @param args command line args (not used).
     * @return 
     * @return 
     * @throws IOException 
     */
    
    public static String getChannelId(){
    	String channelId = null;
			try {
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
		return channelId;
    }
           
    public MyUploads(UserContents userContents){
    	this.userContents = userContents;
    	// This OAuth 2.0 access scope allows for read-only access to the
        // authenticated user's account, but not other types of account access.
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.readonly");

        try {
            // Authorize the request.
        	if (PlaylistController.credential != null){
        		if(PlaylistController.credential.getClientAuthentication() == null){
        			PlaylistController.login();
        	}
        		//PlaylistController.credential = Auth.authorize(scopes, "UserPlaylists");
        		
        	}else{
        		PlaylistController.login();
        	}
            

            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, PlaylistController.credential).setApplicationName(
                    "youtube-cmdline-user-playlists").build();

            
            
            YouTube.Playlists.List  searchList = youtube.playlists().list("id,snippet,contentDetails");     
            searchList.setFields("etag,eventId,items(contentDetails,etag,id,kind,player,snippet,status),kind,nextPageToken,pageInfo,prevPageToken,tokenPagination");
            searchList.setMine(true);
            PlaylistListResponse playListResponse = searchList.execute();
            List<Playlist> playlists = playListResponse.getItems();
            
            if (playlists != null) {
            	Iterator<Playlist> iteratorPlaylistResults = playlists.iterator();
                                if (!iteratorPlaylistResults.hasNext()) {
                                    System.out.println(" There aren't any results for your query.");
                                }
                                while (iteratorPlaylistResults.hasNext()) {
                                    Playlist playlist = iteratorPlaylistResults.next();                          
                                    int playlistSize = (int) (long) playlist.getContentDetails().getItemCount();
                                   
                                    List<YoutubePlaylistInfo> youtubePlaylistInfoList = new ArrayList<YoutubePlaylistInfo>();
                                    youtubePlaylistInfoList.add(new YoutubePlaylistInfo(
                                    		playlist.getSnippet().getTitle(),
                                    		playlistSize,
                                    		playlist.getId()));
                                    
                                    //userPlaylistSizeList.add(playlistSize);
                                    //userPlaylistIdList.add(playlist.getId());
                                    //userPlaylistNameList.add(playlist.getSnippet().getTitle());
                                }
                                
            }
            
            // Call the API's channels.list method to retrieve the
            // resource that represents the authenticated user's channel.
            // In the API response, only include channel information needed for
            // this use case. The channel's contentDetails part contains
            // playlist IDs relevant to the channel, including the ID for the
            // list that contains videos uploaded to the channel.
            
            YouTube.Channels.List channelIdRequest = youtube.channels().list("id");
            channelIdRequest.setMine(true);
            channelIdRequest.getId();
            ChannelListResponse channelIdResult = channelIdRequest.execute();
            this.userContents.setChannelId(channelIdResult.getItems().get(0).getId());
                        
            YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
            channelRequest.setMine(true);
            channelRequest.setFields("items/contentDetails,nextPageToken,pageInfo");
            ChannelListResponse channelResult = channelRequest.execute();
            
            List<Channel> channelsList = channelResult.getItems();

            if (channelsList != null) {
                // The user's default channel is the first item in the list.
                // Extract the playlist ID for the channel's videos from the
                // API response.
                String uploadPlaylistId =
                        channelsList.get(0).getContentDetails().getRelatedPlaylists().getUploads();
                this.userContents.setId(uploadPlaylistId);
                
                // Define a list to store items in the list of uploaded videos.
                List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();

                // Retrieve the playlist of the channel's uploaded videos.
                
                YouTube.PlaylistItems.List playlistItemRequest =
                        youtube.playlistItems().list("id,contentDetails,snippet");
                
                playlistItemRequest.setPlaylistId(uploadPlaylistId);
                

                // Only retrieve data used in this application, thereby making
                // the application more efficient. See:
                // https://developers.google.com/youtube/v3/getting-started#partial
                playlistItemRequest.setFields(
                        "items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");

                String nextToken = "";

                // Call the API one or more times to retrieve all items in the
                // list. As long as the API response returns a nextPageToken,
                // there are still more items to retrieve.
                do {
                    playlistItemRequest.setPageToken(nextToken);
                    PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();

                    playlistItemList.addAll(playlistItemResult.getItems());

                    nextToken = playlistItemResult.getNextPageToken();
                } while (nextToken != null);

                // Prepare information about the playlist.
                this.userContents.setPlaylistSize(playlistItemList.size());
                //prettyPrint(size, playlistItemList.iterator(), id, channelID);
            }

        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /*
     * Print information about all of the items in the playlist.
     *
     * @param size size of list
     *
     * @param iterator of Playlist Items from uploaded Playlist
     */
    private static void prettyPrint(int size, Iterator<PlaylistItem> playlistEntries, String id, String channelID) {
        System.out.println("=============================================================");
        System.out.println("\t\tTotal Videos Uploaded: " + size);
        System.out.println("=============================================================\n");
        System.out.println("Playlist ID : " + id);
        System.out.println("Channel ID : " + channelID);


        while (playlistEntries.hasNext()) {
            PlaylistItem playlistItem = playlistEntries.next();
            System.out.println(" video name  = " + playlistItem.getSnippet().getTitle());
            System.out.println(" video id    = " + playlistItem.getContentDetails().getVideoId());
            System.out.println("\n-------------------------------------------------------------\n");
        }
    }
    
    public static List<YoutubePlaylistInfo> fetchPlaylistsInfoList(){
        List<YoutubePlaylistInfo> youtubePlaylistInfoList = new ArrayList<YoutubePlaylistInfo>();
        YouTube.Playlists.List searchList;
		try {
			searchList = youtube.playlists().list("id,snippet,contentDetails");
			searchList.setFields("etag,eventId,items(contentDetails,etag,id,kind,player,snippet,status),kind,nextPageToken,pageInfo,prevPageToken,tokenPagination");
	        searchList.setMine(true);
	        PlaylistListResponse playListResponse = searchList.execute();
	        List<Playlist> playlists = playListResponse.getItems();
	        
	        if (playlists != null) {
	        	Iterator<Playlist> iteratorPlaylistResults = playlists.iterator();
	                            if (!iteratorPlaylistResults.hasNext()) {
	                                System.out.println(" There aren't any results for your query.");
	                            }
	                            while (iteratorPlaylistResults.hasNext()) {
	                                Playlist playlist = iteratorPlaylistResults.next();                          
	                                int playlistSize = (int) (long) playlist.getContentDetails().getItemCount();
	                               
	                                youtubePlaylistInfoList.add(new YoutubePlaylistInfo(
	                                		playlist.getSnippet().getTitle(),
	                                		playlistSize,
	                                		playlist.getId()));                                
	                            }
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}     
        
    	return youtubePlaylistInfoList;    	
    }

	public static List<YoutubePlaylist> fetchPlaylistList(String channelId) {
        List<YoutubePlaylist> youtubePlaylistList = new ArrayList<YoutubePlaylist>();
        YouTube.Playlists.List searchList;
		try {
			searchList = youtube.playlists().list("id,snippet,contentDetails");
			searchList.setFields("etag,eventId,items(contentDetails,etag,id,kind,player,snippet,status),kind,nextPageToken,pageInfo,prevPageToken,tokenPagination");
	        searchList.setMine(true);
	        PlaylistListResponse playlistListResponse = searchList.execute();
	        List<Playlist> playlists = playlistListResponse.getItems();
	        
	        if (playlists != null) {
	        	Iterator<Playlist> iteratorPlaylistResults = playlists.iterator();
	                            if (!iteratorPlaylistResults.hasNext()) {
	                                System.out.println(" There aren't any results for your query.");
	                            }
	                            while (iteratorPlaylistResults.hasNext()) {
	                                Playlist playlist = iteratorPlaylistResults.next();                          
	                                int playlistSize = (int) (playlist.getContentDetails().getItemCount() - 1);
	                                String playlistId = playlist.getId();
	                                
	                                youtubePlaylistList.add(new YoutubePlaylist(
	                                		playlist.getSnippet().getTitle(),
                                    		playlistSize,
                                    		playlistId,
	                                		getPlaylistItems(playlistId)
	                                		));                                
	                            }
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}     
        
    	return youtubePlaylistList;
	}	
	
	public static List<String> getPlaylistItems(String playlistId) throws IOException
    {
        List<PlaylistItem> items = new ArrayList<>();

        YouTube.PlaylistItems.List request = youtube.playlistItems().list("contentDetails,snippet");
        request.setPlaylistId(playlistId);
        PlaylistItemListResponse retrieved = request.execute();

        while (retrieved.getNextPageToken() != null){
            items.addAll(retrieved.getItems());
            request = youtube.playlistItems().list("contentDetails,snippet");
            request.setPlaylistId(playlistId);
            request.setPageToken(retrieved.getNextPageToken());
            retrieved = request.execute();
        }

        if (retrieved.getNextPageToken() == null){
            items.addAll(retrieved.getItems());
        }
        
        List<String> itemsIdList = new ArrayList<String>();
        Iterator<PlaylistItem> itr = items.iterator();
        while(itr.hasNext()){
        	itemsIdList.add(new String(itr.next().getContentDetails().getVideoId()));
        }
        
        return itemsIdList;
    }
	
}