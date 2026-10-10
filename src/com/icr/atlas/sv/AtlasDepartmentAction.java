package com.icr.atlas.sv;

import java.util.Hashtable;

import com.ibm.sureview.guiComponent.SureviewActionData;
import com.ibm.sureview.sa.action.SADepartmentAction;

public class AtlasDepartmentAction extends SADepartmentAction {

	public AtlasDepartmentAction(String aID) {
		super(aID);
		// TODO Auto-generated constructor stub
	}
	public AtlasDepartmentAction(SureviewActionData actionData) {
		super(actionData);
	}
	public AtlasDepartmentAction(String aID,Hashtable attributeList) {
		super(aID,attributeList);
	}
	
	@Override
	public void initializeAction() {
		super.initializeAction();
		System.out.println("AtlasDepartmentAction init");
		departmentRequiresPrice=false; //prevent popup entry
	}
	
	
}
