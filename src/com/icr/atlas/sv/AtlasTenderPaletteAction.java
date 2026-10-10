package com.icr.atlas.sv;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import com.ibm.sureview.action.DisplayCashAmountPaletteAction;
import com.ibm.sureview.action.SureviewAction;
import com.ibm.sureview.action.TenderAction;
import com.ibm.sureview.guiComponent.TouchButton;

public class AtlasTenderPaletteAction extends DisplayCashAmountPaletteAction{

	protected Vector<AtlasTenderButtonConfig> tenderNames;
		
	public AtlasTenderPaletteAction(String id,Hashtable attrs) {
		super(id,attrs);
		// TODO Auto-generated constructor stub
		tenderNames=new Vector();
		//test here
//		tenderNames.addElement(new String("Tender #1"));
//		tenderNames.addElement(new String("#2 TENDER"));
//		tenderNames.addElement(new String("TENDER 3 TENDER"));
	}

	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
//		if (palette!=null) {
//			loadTenderNames();
//		}
		
	}
	
	public void initializePalette() {
		super.initializePalette();
		setTenderNames();
	}
	
	private void setTenderNames() {
		if (tenderNames.size()<1) {
			loadTenderNames();
		}
		System.out.println("setTenderButtons size:"+tenderNames.size());
		Enumeration buttons=palette.getButtons();
		do {
			if (buttons==null || !buttons.hasMoreElements()) {
				break;
			}
			TouchButton button=(TouchButton)buttons.nextElement();
			if (button!=null) {
				SureviewAction action=(SureviewAction)button.getButtonAction();
				if (action!=null) {
					String actionId=action.getComponentID();
					System.out.println("setTenderButtons-"+actionId);
					if (actionId.startsWith("ATLASTender")) {
						int test=Integer.parseInt(actionId.substring(actionId.length()-2));
						AtlasTenderButtonConfig bc;
						
						System.out.println("looking up tender "+test);
						try  {
							bc=(AtlasTenderButtonConfig) tenderNames.get(test);
						} catch (Exception e) {
							System.out.println(e);
							bc=new AtlasTenderButtonConfig("NOT FOUND","","");
						}	
						System.out.println("config "+bc);
						if (action instanceof AtlasTenderAction) {
							action.setName(bc.buttonText);
							int tempFC=0;
							if (bc.functionCode!="") {
								try {
									tempFC=Integer.parseInt(bc.functionCode);
								} catch (Exception e) {
									//nothing -- bad data
								}
							}
							action.setFunctionKeyConstant(tempFC);
							if (tempFC==0) {
								button.setEnabled(false);
							} else {
								button.setEnabled(true);
							}
							((AtlasTenderAction) action).setVariety(bc.variety);
//							action.setFunctionKeyConstant(91);
//							((AtlasTenderAction) action).setVariety("6");
							if (bc.colorCode!="" && (button instanceof ICRTouchButton)) {
								try {
									((ICRTouchButton)button).setStripeColor(Color.decode(bc.colorCode));
								} catch (Exception e) {
									System.out.println("bad colorcode:"+bc.colorCode);
								}
							}
							//action.setPromptMessage(bc.buttonText); //10-04-25
							action.setInfoMessage(bc.buttonText + " tender is selected.");
							
						} else {
							action.setName("NOT ATLAS TENDER?");
						}
					}
				}
			}
		} while (true);
	}	
	
	private void loadTenderNames() {
		System.out.println("loadTenderNames");
		tenderNames.clear();
		Properties theProps=new Properties();
		try {
			theProps.load(AtlasTenderPaletteAction.class.getClassLoader().getResourceAsStream("atlasSV.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tenderNames.add(new AtlasTenderButtonConfig("id 0?","",""));
		for(int i=1;i<12;i++) {
			String key="tenderButton."+String.format("%02d", i);
			String holdText=theProps.getProperty(key+".text", "");
			String holdFC=theProps.getProperty(key+".fc","");
			String holdVariety=theProps.getProperty(key+".variety","");
			String holdColor=theProps.getProperty(key+".colorCode","");
			tenderNames.add(new AtlasTenderButtonConfig(holdText,holdFC,holdVariety,holdColor));
			System.out.println("load "+key+":"+holdText);
		}
		
		
	}
	
}
