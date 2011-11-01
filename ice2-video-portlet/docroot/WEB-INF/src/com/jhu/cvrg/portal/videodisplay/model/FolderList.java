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
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.jhu.cvrg.portal.videodisplay.utility.ResourceUtility;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFolder;
@ManagedBean(name = "folderList")
@ViewScoped
public class FolderList {
	
	private ArrayList<SelectItem> folders;

	public FolderList() {
		
		List<DLFolder> folderSet;
		long companyId = PortalUtil.getDefaultCompanyId();
		
		try {
			folderSet = com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil.getFolders(companyId);
			folders = new ArrayList<SelectItem>();
		
			for(DLFolder folder : folderSet){
				SelectItem item = new SelectItem(folder.getFolderId(), folder.getName());
				folders.add(item);
			}
		} catch (com.liferay.portal.kernel.exception.SystemException e) {
			ResourceUtility.printErrorMessage("FolderList Bean");
			e.printStackTrace();
		}
		
	}

	public ArrayList<SelectItem> getFolders() {
		return folders;
	}
}
