package com.jhu.cvrg.portal.videodisplay.listener;
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
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.render.ResponseStateManager;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;

import org.portletfaces.liferay.faces.context.LiferayFacesContext;

import com.jhu.cvrg.portal.videodisplay.backing.PreferencesBacking;
import com.jhu.cvrg.portal.videodisplay.backing.VideoDisplay;
import com.jhu.cvrg.portal.videodisplay.model.VideoList;
import com.jhu.cvrg.portal.videodisplay.utility.ResourceUtility;

public class VideoPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;
	private static LiferayFacesContext liferayFacesContext;
	VideoList videoList;

	public void afterPhase(PhaseEvent pe) {

	}

	public void beforePhase(PhaseEvent pe) {

		liferayFacesContext = LiferayFacesContext.getInstance();
		
		if (pe.getPhaseId() == PhaseId.RENDER_RESPONSE) {

			VideoDisplay videoDisplay = (VideoDisplay) liferayFacesContext
			.getApplication().getELResolver().getValue(
					liferayFacesContext.getELContext(), null, "videoDisplay");		
			
			PreferencesBacking preferencesBacking = (PreferencesBacking) liferayFacesContext
			.getApplication().getELResolver().getValue(
					liferayFacesContext.getELContext(), null, "preferencesBacking");
			
			VideoList videoList = (VideoList) liferayFacesContext
			.getApplication().getELResolver().getValue(
					liferayFacesContext.getELContext(), null, "videoList");


			videoList.refresh(preferencesBacking.getSelectedFolder());

			PortletRequest portletRequest = liferayFacesContext.getPortletRequest();

			ResponseStateManager rsm = FacesContext.getCurrentInstance()
					.getRenderKit().getResponseStateManager();
			
			if(videoDisplay != null){
				if (!rsm.isPostback(liferayFacesContext)) {
					videoDisplay.setSelectedVideo(ResourceUtility.getStoredVideo());
				}

				videoDisplay.setMaximized((portletRequest.getWindowState().equals(WindowState.MAXIMIZED)));
			}
		}

	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
