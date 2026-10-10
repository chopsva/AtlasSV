package com.ibm.sureview.guiComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;

public class TouchOptionPaneUI extends BasicOptionPaneUI implements KeyPadListener, SureviewUI {
   static final String COPYRIGHT = "\n\nLicensed Materials - Property of Toshiba Global Commerce Solutions, Inc.\nCopyright © 2004, 2016 Toshiba Global Commerce Solutions, Inc. All Rights Reserved.\nUS Government Users Restricted Rights - Use, duplication or\ndisclosure restricted by GSA ADP Schedule Contract with Toshiba Global Commerce Solutions, Inc.\n\n";

   static String copyright() {
      return "\n\nLicensed Materials - Property of Toshiba Global Commerce Solutions, Inc.\nCopyright © 2004, 2016 Toshiba Global Commerce Solutions, Inc. All Rights Reserved.\nUS Government Users Restricted Rights - Use, duplication or\ndisclosure restricted by GSA ADP Schedule Contract with Toshiba Global Commerce Solutions, Inc.\n\n";
   }

   public static ComponentUI createUI(JComponent x) {
      return new TouchOptionPaneUI();
   }

   protected void installListeners() {
      super.installListeners();
      KeyPad.setModalKeyPadListener(this);
   }

   public void enterPressed(KeyPadEvent kpe) {
      if (this.optionPane != null && this.getButtons().length == 1) {
         this.optionPane.setValue(new Integer(0));
      }

   }

   public void cancelPressed() {
      if (this.optionPane != null) {
         this.optionPane.setValue(new Integer(2));
      }

   }

   public void functionKeyPressed(KeyPadEvent kpe) {
   }

   protected Container createMessageArea() {
      JPanel top = new JPanel();
      top.setBackground(UIManager.getColor("OptionPane.background"));
      //top.setBackground(Color.CYAN);
      top.setBorder(UIManager.getBorder("OptionPane.messageAreaBorder"));
      top.setLayout(new BorderLayout());
      Container body = new JPanel();
      body.setBackground(UIManager.getColor("OptionPane.background"));
      //body.setBackground(Color.GRAY);
      Container realBody = new JPanel();
      //realBody.setBackground(UIManager.getColor("OptionPane.background"));
      realBody.setBackground(Color.ORANGE);
      Container sep = new JPanel() {
         public Dimension getPreferredSize() {
            return new Dimension(15, 1);
         }
      };
      sep.setBackground(UIManager.getColor("OptionPane.background"));
      realBody.setLayout(new BorderLayout());
      realBody.add(sep, "West");
//      realBody.add(sep,"East");
      realBody.add(body, "Center");
      body.setLayout(new GridBagLayout());
      GridBagConstraints cons = new GridBagConstraints();
      cons.gridx = cons.gridy = 0;
      cons.gridwidth = 0;
      cons.gridheight = 1;
      cons.anchor = 17;
      cons.insets = new Insets(0, 0, 3, 0);
      this.addMessageComponents(body, cons, this.getMessage(), this.getMaxCharactersPerLineCount(), false);
      top.add(realBody, "Center");
      this.addIcon(top);
      return top;
   }

   protected Container createButtonArea() {
      Container cont = super.createButtonArea();
      cont.setBackground(UIManager.getColor("OptionPane.background"));
      return cont;
   }

   public void performModeSwitch(JComponent comp) {
   }
}
