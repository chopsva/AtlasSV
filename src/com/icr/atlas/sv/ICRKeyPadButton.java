package com.icr.atlas.sv;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import com.ibm.sureview.guiComponent.KeyPadButton;
import com.ibm.sureview.guiComponent.TouchButton;

public class ICRKeyPadButton extends KeyPadButton implements ComponentListener, ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private Color stripeColor= Color.green;
	private String fullText="WHAT?";
	private int stripeWidth=20;
	private int iconPadding=10;
	private float baseFontSize=14;
	

	public ICRKeyPadButton() {
		super();
	}
	
	public ICRKeyPadButton(String id, Hashtable attribs) {
		super(id,attribs);
		addComponentListener(this);
		addActionListener(this);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		//set preferred size here
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int stripeWidth = 20;

        // Background
        g2.setColor(isSelected() ? getBackground().darker() : Color.DARK_GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Stripe
        g2.setColor(stripeColor);
        g2.fillRect(0, 0, stripeWidth, getHeight());

        // Text
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(getFont().deriveFont(Font.PLAIN, baseFontSize));
        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = fm.getHeight();
        int xText = stripeWidth + 10;
        int yText = 10 + fm.getAscent();
//        for (String line : lines) {
//            g2.drawString(line, xText, yText);
//            yText += lineHeight;
//        }
        g2.drawString(fullText, xText, yText);

        // Border
        g2.setColor(Color.DARK_GRAY.darker());
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g2.dispose();
    }

}
