package com.icr.atlas.sv;

import java.util.Hashtable;

import com.ibm.sureview.ace.action.ACETenderAction;
import com.ibm.sureview.guiComponent.SureviewActionData;

public class AtlasTenderAction extends ACETenderAction {

	public AtlasTenderAction(String aID) {
		super(aID);
		// TODO Auto-generated constructor stub
		//promptMessageKey=null;
		
	}

	public AtlasTenderAction(SureviewActionData actionData) {
		super(actionData);
		// TODO Auto-generated constructor stub
		//promptMessageKey=null;
	}

	public AtlasTenderAction(String aID, Hashtable attributeList) {
		super(aID, attributeList);
		// TODO Auto-generated constructor stub
		//promptMessageKey=null;
	}
	
	
	
	//gives access to Param1 of the action
	public void setVariety(String newVariety) {
		System.out.println("OLD VARIETY:"+variety);
		variety=newVariety;
		System.out.println("NEW VARIETY:"+variety);
	}

}
