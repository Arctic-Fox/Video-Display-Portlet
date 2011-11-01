package com.jhu.cvrg.portal.videodisplay.model;
/*
Copyright 2011 Johns Hopkins University Institute for Computational Medicine

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
/**
* @author Chris Jurado
* 
*/
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.jhu.cvrg.portal.videodisplay.utility.ResourceUtility;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
@ManagedBean(name = "videoList")
@ViewScoped
public class VideoList {

	private ArrayList<SelectItem> videos;
	private String selectedVideo;

	public void refresh(String selectedFolder) {
		List<DLFileEntry> videoSet;
		long groupId = 0L;

		long lngSelectedFolder = Long.valueOf(selectedFolder);

		
		groupId = ResourceUtility.getCurrentGroupId();

		try {
			videoSet =
			com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil.getFileEntries(groupId, lngSelectedFolder);

			videos = new ArrayList<SelectItem>();

			for (DLFileEntry video : videoSet) {
				SelectItem item = new SelectItem(video.getFileEntryId(), video.getTitle());
				videos.add(item);
			}
			
			sortAlphabetically(videos);
			
		} catch (com.liferay.portal.kernel.exception.SystemException e) {
			ResourceUtility.printErrorMessage("VideoList Bean");
			e.printStackTrace();
		}
		
		
	}
	
	public String getNextVideo(String currentVideo){
		int index = 0;
		for(SelectItem item : videos){
			if(item.getValue().toString().equals(currentVideo)){
				index = videos.indexOf(item);
				if(index == (videos.size() - 1)){
					index = 0;
				}else{
					index ++;
				}
			}
		}
		return videos.get(index).getValue().toString();
	}
	
	public String getPreviousVideo(String currentVideo){
		int index = 0;
		for(SelectItem item : videos){
			if(item.getValue().toString().equals(currentVideo)){
				index = videos.indexOf(item);
				if(index == 0){
					index = (videos.size() - 1);
				}else{
					index --;
				}
			}
		}
		return videos.get(index).getValue().toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void sortAlphabetically(ArrayList<SelectItem> videos){
        Collections.sort(videos, new Comparator(){
        	 
            public int compare(Object o1, Object o2) {
                SelectItem p1 = (SelectItem) o1;
                SelectItem p2 = (SelectItem) o2;
               return p1.getLabel().compareToIgnoreCase(p2.getLabel());
            }
        });
	}

	public void refresh() {
		refresh("0");
	}

	public void setSelectedVideo(String selectedVideo) {
		this.selectedVideo = selectedVideo;
	}

	public String getSelectedVideo() {
		return selectedVideo;
	}

	public ArrayList<SelectItem> getVideos() {
		return videos;
	}
}
