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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

import com.ibm.sureview.guiComponent.TouchButton;

public class ICRTouchButton extends TouchButton implements ComponentListener, ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private Color stripeColor= Color.green;
	private String fullText="WHAT?";
	private int stripeWidth=20;
	private int iconPadding=4;
	private float baseFontSize=14;
	

	public ICRTouchButton() {
		super();
	}
	
	public ICRTouchButton(String id, Hashtable attribs) {
		super(id,attribs);
		addComponentListener(this);
		addActionListener(this);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		readAttribs(attribs);
		//set preferred size here
		//System.out.println("ICRTouchButton:"+id);
		
	}
	
	public void readAttribs(Hashtable attribs) {
		//Enumeration keys=attribs.keys();
		if (attribs.get("atlasFontSize") != null) {
			baseFontSize=Float.parseFloat((String)attribs.get("atlasFontSize"));
			System.out.println("atlasFontSize found"+baseFontSize);
		}
		if (attribs.get("atlasColor")!=null) {
			stripeColor=Color.decode((String)attribs.get("atlasColor"));
		}
	
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
	public void setText(String txt) {
		fullText=txt;
		super.setText(txt+":)");
		
	}
	
	public void setStripeColor(Color color) {
		this.stripeColor=color;
		repaint();
	}
	
	public void setBaseFontSize(float x) {
		this.baseFontSize=x;
		repaint();
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int stripeWidth = 20;
        int maxStripeWidth=10;

        // Background
        g2.setColor(isSelected() ? getBackground().darker() : Color.DARK_GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        stripeWidth=Math.min((int)(getWidth() * 0.10),maxStripeWidth); //09-06-25 set max  
        Color workColor=isEnabled() ? stripeColor : Color.GRAY;
        
        
        
        // Stripe
        g2.setColor(stripeColor);
        g2.fillRect(0, 0, stripeWidth, getHeight());

        // Text
//        g2.setColor(Color.LIGHT_GRAY);
//        g2.setFont(getFont().deriveFont(Font.PLAIN, 16));
//        FontMetrics fm = g2.getFontMetrics();
//        int lineHeight = fm.getHeight();
//        int xText = stripeWidth + 10;
//        int yText = 10 + fm.getAscent();
//        for (String line : lines) {
//            g2.drawString(line, xText, yText);
//            yText += lineHeight;
//        }
       // g2.drawString(fullText, xText, yText);
        //text wrapping
        int textStartX=stripeWidth+iconPadding;
        g2.setColor(new Color(224, 224, 224));
        g2.setFont(getFont().deriveFont(Font.PLAIN,baseFontSize));
        FontMetrics fm=g2.getFontMetrics();
        int maxTextWidth=getWidth()-textStartX-10;
        List<String> wrappedLines=wrapText(fullText,fm,maxTextWidth);
        int yText=iconPadding;
        int lineHeight=fm.getHeight();
        for(String line : wrappedLines) {
        	g2.drawString(line, textStartX, yText+fm.getAscent());
        	yText+=lineHeight;
        }
       

        // Border
        g2.setColor(Color.DARK_GRAY.darker());
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g2.dispose();
    }
	private List<String> wrapText(String text,FontMetrics fm,int maxWidth) {
		List<String> lines=new ArrayList<>();
		String[] words=text.split(" ");
		StringBuilder line=new StringBuilder();
		
		for (String word : words) {
			String testLine=line.length()==0 ? word : line+" "+word;
			if (fm.stringWidth(testLine)<=maxWidth) {
				line=new StringBuilder(testLine);
			} else {
				lines.add(line.toString());
				line=new StringBuilder(word);
			}
		}
		if (line.length()>0) {
			lines.add(line.toString());
		}
		
		return lines;
	}
	
	@Override
	public void setIcon(Icon icon) {
		super.setIcon(icon);
        int avgR = 0;
        int avgG = 0;
        int avgB = 0;
		
	    if (icon != null) {
            BufferedImage img = new BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g = img.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();

            long sumR = 0, sumG = 0, sumB = 0, count = 0;
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int argb = img.getRGB(x, y);
                    int alpha = (argb >> 24) & 0xff;
                    if (alpha < 10) continue;

                    int r = (argb >> 16) & 0xff;
                    int gCol = (argb >> 8) & 0xff;
                    int b = argb & 0xff;

                    sumR += r;
                    sumG += gCol;
                    sumB += b;
                    count++;
                }
            }

            if (count > 0) {
                avgR = (int) (sumR / count);
                avgG = (int) (sumG / count);
                avgB = (int) (sumB / count);
                //setStripeColor(new Color(avgR, avgG, avgB));
            }
            
        }
	    //force to 7 colors
//	    avgR=(avgR>128) ? 255 : 0;
//	    avgG=(avgG>128) ? 255 : 0;
//	    avgB=(avgB>128) ? 255 : 0;
	    //force 26 colors
//	    avgR=(avgR<86) ? 0 : (avgR<172) ? 128 : 255;
//	    avgG=(avgG<86) ? 0 : (avgG<172) ? 128 : 255;
//	    avgB=(avgB<86) ? 0 : (avgB<172) ? 128 : 255;
	    avgR=avgR & 0xf0;
	    avgG=avgG & 0xf0;
	    avgB=avgB & 0xf0;
	    
	    
	    if (avgR<32 && avgG<32 && avgB<32) {
	    	avgR=255;
	    	avgG=255;
	    	avgB=255;
	    }
	    
	    
	    setStripeColor(new Color(avgR,avgG,avgB));
	}

}
