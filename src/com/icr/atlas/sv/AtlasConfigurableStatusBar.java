package com.icr.atlas.sv;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.ibm.sureview.factory.FactoryTable;
import com.ibm.sureview.guiComponent.ConfigurableLabelImpl;
import com.ibm.sureview.guiComponent.ConfigurableStatusBar;
import com.ibm.sureview.guiComponent.IconManager;
import com.icr.atlas.ATLASGuiExtend;
import com.icr.atlas.ATLASGuiExtend.CommState;

public class AtlasConfigurableStatusBar extends ConfigurableStatusBar {
	
	protected JLabel atlasTest;
	protected CommState lastCommState;
	
	
	public AtlasConfigurableStatusBar(String id) {
		super(id);
		// TODO Auto-generated constructor stub
		System.out.println("ATLASConfigurableStatusBar init "+id);
	}

	public AtlasConfigurableStatusBar(String id, Hashtable attrs) {
		super(id, attrs);
		System.out.println("ATLASConfigurableStatusBar init "+id+" + attrs");
		// TODO Auto-generated constructor stub
	}

	@Override 
	protected Vector createComponents() {
		System.out.println("ATLAS createComponents called");
		//return super.createComponents();
		Vector c=super.createComponents();
		c.add(createAtlasTest());
		//tell the master
		ATLASGuiExtend.getInstance().setStatusBar(this);
		return c;
	}
	protected JComponent createAtlasTest() {
		if (atlasTest == null) {
//			atlasTest=new ConfigurableLabelImpl("HI!");
//			atlasTest.setBorder(FactoryTable.getGUIComponentFactory().makeSIRoundedBorder(false));
//            atlasTest.setForeground(foregroundColor);
//            atlasTest.setBackground(backgroundColor);
//            atlasTest.setOpaque(false);
			atlasTest=new JLabel();
			Icon testLogo=IconManager.getIcon("AtlasCommInit");
			atlasTest.setIcon(testLogo);
			atlasTest.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					ATLASGuiExtend.getInstance().showCommStatus(SwingUtilities.getWindowAncestor(e.getComponent()));
				}
			});
		}
		return atlasTest;
	}
	
	public void setCommState(CommState theState) {
		if (atlasTest==null) {
			return;
		}
		if (theState==lastCommState) {
			//nothing to change
			return;
		}
		
		String icon="AtlasCommInit";
		switch (theState) {
			case DISABLED:
				icon="AtlasCommDisabled";
				break;
			case ACTIVE:
				icon="AtlasCommActive";
				break;
			case WARN:
				icon="AtlasCommWarn";
				break;
			case ERROR:
				icon="AtlasCommError";
				break;
			case GOOD:
				icon="AtlasCommGood";
				break;
			default:
				icon="AtlasCommInit";
				break;
		}
		System.out.println("SETTING LOGO TO:"+icon);
		Icon testLogo=IconManager.getIcon(icon);
		atlasTest.setIcon(testLogo);
		lastCommState=theState;
	}
}
