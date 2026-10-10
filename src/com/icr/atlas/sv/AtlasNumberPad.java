package com.icr.atlas.sv;

import java.awt.Color;

import com.ibm.sureview.guiComponent.NumberPad;
import com.ibm.sureview.guiComponent.TouchButton;

public class AtlasNumberPad extends NumberPad {

	public AtlasNumberPad(int rows, int columns) {
		super(rows, columns);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public TouchButton createTouchButton(String text) {
		TouchButton b=super.createTouchButton(text);
		((ICRTouchButton)b).setStripeColor(new Color(64,64,64));
		((ICRTouchButton)b).setBaseFontSize(24);
		
		return b;
	}
	
	
//	@Override
//	protected void initializeNumberPad() {
//		super.initializeNumberPad();
//		
//		for(int i = 0; i < 10; i++)
//        {
//            ((ICRTouchButton)numberButtons[i]).setBaseFontSize(24);
//            ((ICRTouchButton)numberButtons[i]).setStripeColor(new Color(64,64,64));
//        }
//		((ICRTouchButton)dZeroButton).setBaseFontSize(24);
//		((ICRTouchButton)dZeroButton).setStripeColor(new Color(64,64,64));
//		((ICRTouchButton)periodButton).setBaseFontSize(24);
//		((ICRTouchButton)periodButton).setStripeColor(new Color(64,64,64));
//	}

}
