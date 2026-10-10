package com.icr.atlas.sv;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Hashtable;

import com.ibm.sureview.application.BooleanTable;
import com.ibm.sureview.factory.FactoryTable;
import com.ibm.sureview.guiComponent.ElementEvent;
import com.ibm.sureview.guiComponent.GUIDisplay;
import com.ibm.sureview.guiComponent.LineDisplay;
import com.ibm.sureview.guiComponent.OperatorPromptData;
import com.ibm.sureview.resource.Bundle;

public class AtlasGUIDisplay extends GUIDisplay {

	Hashtable b;
	
	public AtlasGUIDisplay() {
		// TODO Auto-generated constructor stub
		super();
		System.out.println("AtlasGUIDisplay blank");
		
	}

	public AtlasGUIDisplay(String arg0, Hashtable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		System.out.println("AtlasGUIDisplay 2");
		//initializeAtlasDisplay();
	}

	public AtlasGUIDisplay(int numLines, int numChars, int fontSize, Color background, Color foreground) {
		super(numLines, numChars, fontSize, background, foreground);
		// copied from base b/c the call to initializeDisplay is PRIVATE!!!!!!@!
		System.out.println("AtlasGUIDisplay 5");
		//remove(twoByTwenty);
        //initializeAtlasDisplay();
        
	}

	@Override
	public void endElement(ElementEvent e) {
		//copy from base
		if (e.getElementType().equals(getElementType())) {
			setColorScheme(colorScheme);
			if(font==null) {
				font=FactoryTable.getGUIComponentFactory().makeConfigurableFontImpl("default-line-display");
			}
			initializeDisplay();
			saveInternalState();
		}
	}
	
	public void initializeDisplay() {
		//copy from base
        if(isSecondaryDisplay())
        {
            promptData = FactoryTable.getGUIComponentFactory().makeCustomerPromptData();
            java.util.ResourceBundle labelText = FactoryTable.getI18NFactory().makeLabelBundle();
            promptData.addSubstitution((Bundle)labelText);
        } else
        {
            promptData = OperatorPromptData.getInstance();
        }
        if(font != null)
        {
            twoByTwenty = new AtlasLineDisplay(numLines, numChars, font.getFont(), background, foreground, promptData);
        } else
        {
            twoByTwenty = new AtlasLineDisplay(numLines, numChars, fontSize, background, foreground, promptData);
        }
        twoByTwenty.setOpaque(isOpaque());
        setLayout(new GridLayout(1, 1));
        add(twoByTwenty);
		
	}
	
		
	public void initializeAtlasDisplay()
    {
		System.out.println("AtlasGUIDisplay initDisplay");
	  if (twoByTwenty==null) {
        if(font != null)
        {
            twoByTwenty =(LineDisplay) new AtlasLineDisplay(numLines, numChars, font.getFont(), background, foreground, promptData);
        } else
        {
            twoByTwenty =(LineDisplay) new AtlasLineDisplay(numLines, numChars, fontSize, background, foreground, promptData);
        }
        twoByTwenty.setOpaque(isOpaque());
        setLayout(new GridLayout(1, 1)); //super handled this
        add(twoByTwenty);
	  }
    }
}
