package com.jhu.cvrg.portal.videodisplay.utility;
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
import java.io.IOException;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.portletfaces.liferay.faces.context.LiferayFacesContext;
import org.portletfaces.liferay.faces.helper.LongHelper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;

public class ResourceUtility {

	private final static LiferayFacesContext liferayFacesContext = LiferayFacesContext.getInstance();
	private final static PortletPreferences prefs = liferayFacesContext.getPortletPreferences();
	
	public static DLFileEntry getVideo(long videoId){
		DLFileEntry video = null;

		try {
			video = com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil
			.getFileEntry(Long.valueOf(videoId));
		} catch (NoSuchFileEntryException e){
				printErrorMessage("ResourceUtility:Requested video file does not exist.");
		} catch (PortalException e) {
			printErrorMessage("Resource Utility");
			e.printStackTrace();
		} catch (SystemException e) {
			printErrorMessage("Resource Utility");
			e.printStackTrace();
		}
		
		return video;
	}
	
	public static String getControlType(){
		return prefs.getValue("control", "1");
	}
	
	public static String getStoredVideo(){
		return prefs.getValue("default", "0");
	}
	
	public static String getStoredFolder(){
		return prefs.getValue("folder", "0");
	}
	
	public static String getStoredHostname(){
		return prefs.getValue("hostname", "localhost:8080");
	}
	
	public static boolean getFullScreen(){
		return prefs.getValue("fullscreen", "false").equals("true");
	}
	
	public static void savePreferences(String folder, String video, String hostname, String controlType, boolean fullscreen){
		try {
			prefs.setValue("control", controlType);
			prefs.setValue("folder", folder);
			prefs.setValue("default", video);
			prefs.setValue("hostname", hostname);
			prefs.setValue("fullscreen", String.valueOf(fullscreen));
			prefs.store();
		} catch (ReadOnlyException e) {
			printErrorMessage("Resource Utility");
			e.printStackTrace();
		} catch (ValidatorException e) {
			printErrorMessage("Resource Utility");
			e.printStackTrace();
		} catch (IOException e) {
			printErrorMessage("Resource Utility");
			e.printStackTrace();
		}

	}
	
	public static void printErrorMessage(String source){
		System.err.println("*************************** Error in " + source + " ******************************");
	}
	
	public static long getIdParameter(String param){
		return LongHelper.toLong(liferayFacesContext.getExternalContext().getRequestParameterMap().get(param), 0L);
	}
	
	public static User getUser(long userId){
		User user = null;
		try {
			user = UserLocalServiceUtil.getUser(userId);

		} catch (PortalException e) {
			printErrorMessage("Resource Utility");
			e.printStackTrace();
		} catch (SystemException e) {
			printErrorMessage("Resource Utility");
			e.printStackTrace();
		}
		return user;
	}
	
	public static long getCurrentGroupId(){
		
		PortletRequest request = (PortletRequest) liferayFacesContext.getExternalContext().getRequest();	
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
		
		return themeDisplay.getLayout().getGroupId();	
	}
	
	public static User getCurrentUser(){
		
		User currentUser = null;
		try {
			currentUser = UserLocalServiceUtil.getUser(Long.parseLong(liferayFacesContext.getPortletRequest().getRemoteUser()));
		} catch (NumberFormatException e) {
			printErrorMessage("Resource Utility");
			e.printStackTrace();
		} catch (PortalException e) {
			printErrorMessage("Resource Utility");
			e.printStackTrace();
		} catch (SystemException e) {
			printErrorMessage("Resource Utility");
			e.printStackTrace();
		}
		return currentUser;
	}


	public static long getGroupId(String communityName){
		long groupId = 0L;
		List<Group> groupList;
		try {
			groupList = GroupLocalServiceUtil.getGroups(0, GroupLocalServiceUtil.getGroupsCount());;
			for(Group group : groupList){
				if(group.getName().equals(communityName)){
					groupId = group.getGroupId();
				}
			}
		} catch (SystemException e) {
			printErrorMessage("Resource Utility");
			e.printStackTrace();
		}

		return groupId;
	}


}
