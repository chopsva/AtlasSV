package com.icr.atlas.sv;

public class AtlasTenderButtonConfig {

	public String buttonText;
	public String functionCode;
	public String variety;
	public String colorCode;
	
	public AtlasTenderButtonConfig() {
		// TODO Auto-generated constructor stub
		this.buttonText="NO TEXT";
		this.functionCode="";
		this.variety="";
		this.colorCode="";
	}
	
	public AtlasTenderButtonConfig(String text,String fc,String v,String cc) {
		this.buttonText=text;
		this.functionCode=fc;
		this.variety=v;
		this.colorCode=cc;
	}
	public AtlasTenderButtonConfig(String text,String fc,String v) {
		this(text,fc,v,"");
	}

	@Override
	public String toString() {
		return "AtlasTenderButtonConfig buttonText:"+this.buttonText+" FC:"+this.functionCode+" Variety:"+this.variety;
	}
		
}

//options
//   set button stripe color?
