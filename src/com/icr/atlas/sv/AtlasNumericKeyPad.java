package com.icr.atlas.sv;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Hashtable;

import javax.swing.Box;

import com.ibm.sureview.factory.FactoryTable;
import com.ibm.sureview.guiComponent.NumericKeyPad;

//this creates the NumericKeyPad. To set anything special we have to do it to the TouchButton. 

public class AtlasNumericKeyPad extends NumericKeyPad {

	public AtlasNumericKeyPad(int rows, int columns) {
		super(rows, columns);
		// TODO Auto-generated constructor stub
	}

	public AtlasNumericKeyPad(String id, Hashtable attributes) {
		super(id, attributes);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initializeActionButtons() {
		super.initializeActionButtons();
		if (cancelButton!=null) {
			((ICRTouchButton)cancelButton).setStripeColor(Color.RED);
		}
		if (clearButton!=null) {
			((ICRTouchButton)clearButton).setStripeColor(Color.BLUE);
		}
		if ((backButton!=null)) {
			((ICRTouchButton)backButton).setStripeColor(Color.ORANGE);
		}
		if ((enterButton!=null)) {
			((ICRTouchButton)enterButton).setStripeColor(new Color(0,128,0));
		}
		if ((spaceButton!=null)) {
			((ICRTouchButton)spaceButton).setStripeColor(new Color(64,64,64));
		}
	}
	
	@Override
	public void initializeNumericKeyPad(int rows,int columns) {
		
		
		    System.out.println("set numericKeyPad " + rows + "," + columns);

		    GridBagLayout gbl = new GridBagLayout();
		    // 3 columns for the pad, 1 for the side buttons
		    gbl.columnWeights = new double[]{1.0, 1.0, 1.0, 0.35};
		    // 4 digit rows + 1 SPACE row (all equal so Enter=2x height when spanning 2 rows)
		    gbl.rowWeights    = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
		    setLayout(gbl);

		    GridBagConstraints c;

		    // Number pad: cols 0..2, rows 0..3
		    c = new GridBagConstraints();
		    c.gridx = 0; c.gridy = 0;
		    c.gridwidth = 3; c.gridheight = 4;
		    c.fill = GridBagConstraints.BOTH;
		    numberPad = FactoryTable.getGUIComponentFactory().makeNumberPad(rows - 1, columns - 1);
		    add(numberPad, c);
		    numberPad.addActionListener(this);

		    // SPACE: row 4, cols 0..2
		    c = new GridBagConstraints();
		    c.gridx = 0; c.gridy = 4;
		    c.gridwidth = 3; c.gridheight = 1;
		    c.fill = GridBagConstraints.BOTH;
		    add(spaceButton, c);

		    // Side buttons: column 3
		    c = new GridBagConstraints();
		    c.gridx = 3; c.gridy = 0; c.fill = GridBagConstraints.BOTH;
		    add(cancelButton, c);

		    c = new GridBagConstraints();
		    c.gridx = 3; c.gridy = 1; c.fill = GridBagConstraints.BOTH;
		    add(clearButton, c);

		    c = new GridBagConstraints();
		    c.gridx = 3; c.gridy = 2; c.fill = GridBagConstraints.BOTH;
		    add(backButton, c);

		    // ENTER: double height  spans rows 3 and 4
		    c = new GridBagConstraints();
		    c.gridx = 3; c.gridy = 3;
		    c.gridheight = 2;
		    c.fill = GridBagConstraints.BOTH;
		    add(enterButton, c);
		}

	
	
    public void initializeNumericKeyPadOLD(int rows, int columns)
    {
	
		System.out.println("set numericKeyPad "+rows+","+columns);
        gbc = new GridBagConstraints();
        gbc.gridwidth = 3;
        gbc.gridheight = 4;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = 18;
        gbc.fill = 2; //horiz

        numberPad = FactoryTable.getGUIComponentFactory().makeNumberPad(rows - 1, columns - 1);
        add(numberPad, gbc);
        numberPad.addActionListener(this);

        gbc.gridy = 4;
        //gbc.gridwidth = 3;
        gbc.gridwidth=columns-1;
        gbc.gridheight = 1;
        //gbc.gridheight=rows-4;
        gbc.weighty=1.0;
        gbc.fill = 1; //both
        add(spaceButton, gbc);

        gbc.fill = 1; //horiz
        gbc.gridx = columns-1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx=1.0;
        gbc.weighty=1.25;
        add(cancelButton, gbc);
      
        //gbc.gridy++;
        gbc.gridy=1;
        add(clearButton, gbc);
      
        //gbc.gridy++;
        gbc.gridy=2;
        add(backButton, gbc);

        //gbc.gridy++;
        gbc.gridy=3;
        gbc.gridheight = rows-3;
        //gbc.gridheight=1;
        gbc.weighty=2.0;
        //gbc.fill = 1;
        add(enterButton, gbc);
        
        GridBagConstraints gbcSpacer=new GridBagConstraints();
        gbcSpacer.gridx=4;
        gbcSpacer.gridy=4;
        gbcSpacer.weighty=1.0;
        add(Box.createVerticalStrut(0),gbcSpacer);
    }
	
}
