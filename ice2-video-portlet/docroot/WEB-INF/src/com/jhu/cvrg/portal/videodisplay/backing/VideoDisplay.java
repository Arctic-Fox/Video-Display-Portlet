package com.jhu.cvrg.portal.videodisplay.backing;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.portlet.GenericPortlet;

import com.jhu.cvrg.portal.videodisplay.model.VideoList;
import com.jhu.cvrg.portal.videodisplay.utility.ResourceUtility;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;

@ManagedBean(name = "videoDisplay")
@ViewScoped
public class VideoDisplay extends GenericPortlet {

	private String selectedFolder, selectedVideo;
	private String source;
	private String uuid, hostname, filename;
	private long groupId;
	private boolean noVideo, maximized, control;
	private int width, height;

	private static int MAX_WIDTH = 1000;
	private static int MAX_HEIGHT = 600;
	private static int MIN_WIDTH = 300;
	private static int MIN_HEIGHT = 200;

	@ManagedProperty(name = "videoList", value = "#{videoList}")
	private VideoList videoList;

	public VideoDisplay() {

		selectedFolder = ResourceUtility.getStoredFolder();
		selectedVideo = ResourceUtility.getStoredVideo();
		hostname = ResourceUtility.getStoredHostname();

		if (selectedVideo.equals("0"))
			noVideo = true;
		else
			noVideo = false;

		if (this.isMaximized()) {
			width = MAX_WIDTH;
			height = MAX_HEIGHT;
		} else {
			if(ResourceUtility.getFullScreen()){
				width = MAX_WIDTH;
				height = MAX_HEIGHT;
			}
			else{
				width = MIN_WIDTH;
				height = MIN_HEIGHT;
			}
		}

		if (ResourceUtility.getControlType().equals("1")) {
			control = true;
		} else {
			control = false;
		}
	}

	public void back(ActionEvent event) {
		selectedVideo = videoList.getPreviousVideo(selectedVideo);
	}

	public void forward(ActionEvent event) {
		selectedVideo = videoList.getNextVideo(selectedVideo);
	}

	public void changedVideoEvent(ValueChangeEvent event) {

		PhaseId phaseId = event.getPhaseId();

		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			if (!selectedVideo.equals("0"))
				noVideo = false;
			else
				noVideo = true;
			setVideoURL(selectedVideo);
		}
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setSelectedFolder(String selectedFolder) {
		this.selectedFolder = selectedFolder;
	}

	public String getSelectedFolder() {
		if(!selectedFolder.equals("0")){
			return selectedFolder;
		}
		else{
			return "";
		}
	}

	private void setVideoURL(String selectedVideo) {
		DLFileEntry video = null;

		if (!noVideo && !selectedVideo.equals("0")) {
			video = ResourceUtility.getVideo(Long.valueOf(selectedVideo));

			if (video != null) {
				uuid = video.getUuid();
				filename = video.getTitle();
				groupId = video.getGroupId();
				noVideo = false;
			}
		}
	}

	public void setSelectedVideo(String selectedVideo) {
		this.selectedVideo = selectedVideo;

	}

	public String getSelectedVideo() {
		return selectedVideo;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		if (!selectedVideo.equals("0") && !selectedVideo.equals("")) {
			setVideoURL(selectedVideo);
			source = "http://" + hostname + "/documents/" + groupId + "/"
					+ uuid;
			return source;
		} else {
			return "";
		}
	}

	public void setNoVideo(boolean noVideo) {
		this.noVideo = noVideo;
	}

	public boolean isNoVideo() {
		return noVideo;
	}

	public void setMaximized(boolean maximized) {
		if (maximized || ResourceUtility.getFullScreen()) {
			width = MAX_WIDTH;
			height = MAX_HEIGHT;
		} else {
			width = MIN_WIDTH;
			height = MIN_HEIGHT;
		}
		this.maximized = maximized;
	}

	public boolean isMaximized() {

		return maximized;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public VideoList getVideoList() {
		return videoList;
	}

	public void setVideoList(VideoList videoList) {
		this.videoList = videoList;
	}

	public void setControl(boolean control) {
		this.control = control;
	}

	public boolean isControl() {
		return control;
	}

}