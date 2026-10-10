package com.icr.atlas.sv;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.ibm.sureview.guiComponent.LineDisplay;
import com.ibm.sureview.guiComponent.PromptData;

public class AtlasLineDisplay extends LineDisplay {
	
	Font atlasFont;
	int atlasFontSize;

	public AtlasLineDisplay(PromptData promptData) {
		super(promptData);
		// TODO Auto-generated constructor stub
	}

	public AtlasLineDisplay(int numLines, int numChars, int fontSize, Color backgroundColor, Color foregroundColor,
			PromptData promptData) {
		super(numLines, numChars, fontSize, backgroundColor, foregroundColor, promptData);
		// TODO Auto-generated constructor stub
		atlasFontSize=fontSize;
	}

	public AtlasLineDisplay(int numLines, int numChars, Font font, Color backgroundColor, Color foregroundColor,
			PromptData promptData) {
		super(numLines, numChars, font, backgroundColor, foregroundColor, promptData);
		// TODO Auto-generated constructor stub
		atlasFont=font;
	}
	
	@Override
	public void setText(String msg) {
		System.out.println("LINEDISPLAY setTEXT:"+msg);
		super.setText(alignAsIfMonospaced(msg));
	}
	
	public String alignAsIfMonospaced(String text) {
		Graphics g=new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB).getGraphics();
		g.setFont(atlasFont);
		FontMetrics fm=g.getFontMetrics();
		int wWidth=fm.charWidth('W');
		int spaceWidth=fm.charWidth(' ');
		StringBuilder result=new StringBuilder();
		String[] lines=text.split("\n");
		
		for(String line : lines) {
			StringBuilder alignedLine=new StringBuilder();
			int vPos=0;
			int cursorX=0;
			
			String[] words=line.split("  ");
			for(String word : words) {
				if (word.isEmpty()) {
					//vPos++;
					vPos+=2; //split was on 2
					continue;
				}
				int expectedStartX=vPos * wWidth;
				while (cursorX+spaceWidth <=expectedStartX) {
					alignedLine.append(' ');
					cursorX +=spaceWidth;
				}
				
				alignedLine.append(word);
				int wordActualWidth=fm.stringWidth(word);
				cursorX +=wordActualWidth;
				
				vPos+=word.length()+1;
			}
			result.append(alignedLine.append("\n"));
		}
		System.out.println("aligned:"+result.toString());
		return result.toString();
	}

}
