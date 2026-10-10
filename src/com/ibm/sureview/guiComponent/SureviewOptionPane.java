package com.ibm.sureview.guiComponent;

import com.ibm.retail.AEF.client.AEFPropertyListenerProxyImpl;
import com.ibm.retail.AEF.data.POSDataProvider;
import com.ibm.retail.AEF.event.AEFPropertyChangeEvent;
import com.ibm.retail.AEF.event.AEFPropertyChangeListener;
import com.ibm.sureview.action.ReferencedAction;
import com.ibm.sureview.action.SureviewAction;
import com.ibm.sureview.application.SureviewApplication;
import com.ibm.sureview.factory.FactoryTable;
import com.ibm.sureview.resource.SVProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

public class SureviewOptionPane extends JOptionPane {
   static final String COPYRIGHT = "\n\nLicensed Materials - Property of Toshiba Global Commerce Solutions, Inc.\nCopyright © 2004, 2016 Toshiba Global Commerce Solutions, Inc. All Rights Reserved.\nUS Government Users Restricted Rights - Use, duplication or\ndisclosure restricted by GSA ADP Schedule Contract with Toshiba Global Commerce Solutions, Inc.\n\n";
   protected static Icon messageIcon = IconManager.getIcon("informGIF");
   protected static Icon questionIcon = IconManager.getIcon("questionGIF");
   public static boolean isClearStateDialogDisplayed = false;
   public static JDialog lastOptionPaneDialogDisplayed = null;
   public static JOptionPane joptPane = null;
   public static final int ACTION_PERFORMED = -100;
   public static final int AUTO_CLOSED = -200;
   protected static String sysBusyPlsWait = FactoryTable.getI18NFactory().makeLabelBundle().getString("systemBusyPleaseWait");
   protected static String originalMessage;
   protected static boolean lastBusy = false;
   protected static JOptionPane holdpane;
   protected static AEFPropertyListenerProxyImpl aefPropProxy;
   public static float JVM_VERSION = Float.valueOf(System.getProperty("java.version").substring(0, 3));

   static String copyright() {
      return "\n\nLicensed Materials - Property of Toshiba Global Commerce Solutions, Inc.\nCopyright © 2004, 2016 Toshiba Global Commerce Solutions, Inc. All Rights Reserved.\nUS Government Users Restricted Rights - Use, duplication or\ndisclosure restricted by GSA ADP Schedule Contract with Toshiba Global Commerce Solutions, Inc.\n\n";
   }

   public SureviewOptionPane() {
   }

   public SureviewOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
      super(message, messageType, optionType, icon, options, initialValue);
      setBackground(Color.PINK);
      setForeground(Color.WHITE);
      setAtlasStyle(this);
   }

   public SureviewOptionPane(Object message, int messageType, int optionType, Icon icon) {
      super(message, messageType, optionType, icon);
      this.setBackground(Color.YELLOW);
      this.setForeground(Color.WHITE);
      setAtlasStyle(this);
   }

   public SureviewOptionPane(Object message, int messageType, int optionType) {
      super(message, messageType, optionType);
      setBackground(Color.DARK_GRAY);
      setForeground(Color.WHITE);
      setAtlasStyle(this);
   }
   
   public void setAtlasStyle(Container container) {
	   for (Component comp : container.getComponents()) {  
		   System.out.println(comp);
		   if (comp instanceof JButton) {
	           JButton btn = (JButton) comp;
	           btn.setBackground(new Color(60, 63, 65)); // Dark gray
	           btn.setForeground(Color.WHITE);
	           btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	           btn.setFocusPainted(false);
	           btn.setOpaque(true);
		   }
		   if (comp instanceof Container) {
			   setAtlasStyle((Container)comp);
		   }
	   }
           
	   
   }

   public JDialog createDialog(Component parentComponent, String title) {
      Frame frame = JOptionPane.getFrameForComponent(parentComponent);
//      if ((double)JVM_VERSION > 1.7D) {
//         JDialog.setDefaultLookAndFeelDecorated(true);
//      }
      title="KEN IS GREAT";
      System.out.println("KEN");
      final JDialog dialog = new SureviewDialog(frame, title, true);
      Container contentPane = dialog.getContentPane();
      dialog.setResizable(false);
      dialog.setDefaultCloseOperation(0);
      contentPane.setLayout(new BorderLayout());
      contentPane.add(this, "Center");
      dialog.setUndecorated(true);
      dialog.pack();
      boolean centerOnFrame = SVProperties.getBooleanValue("SureviewDialog.centerOnFrame", false);
      if (centerOnFrame) {
         Component curObj = parentComponent;
         boolean frameFound = parentComponent instanceof RootPaneContainer;

         while(curObj != null && !frameFound) {
            curObj = ((Component)curObj).getParent();
            if (curObj != null && curObj instanceof RootPaneContainer) {
               frameFound = true;
            }
         }

         if (frameFound) {
            dialog.setLocationRelativeTo((Component)curObj);
         } else {
            dialog.setLocationRelativeTo(parentComponent);
         }
      } else {
         dialog.setLocationRelativeTo(parentComponent);
      }

      dialog.addWindowListener(new WindowAdapter() {
         boolean gotFocus = false;

         public void windowClosing(WindowEvent we) {
         }

         public void windowActivated(WindowEvent we) {
            if (!this.gotFocus) {
               SureviewOptionPane.this.selectInitialValue();
               this.gotFocus = true;
            }

         }
      });
      this.addPropertyChangeListener(new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent event) {
            if (dialog.isVisible() && event.getSource() == SureviewOptionPane.this && (event.getPropertyName().equals("value") || event.getPropertyName().equals("inputValue"))) {
               dialog.setVisible(false);
               dialog.dispose();
               SureviewOptionPane.isClearStateDialogDisplayed = false;
               SureviewOptionPane.holdpane = null;
            }

         }
      });
      System.out.println("KEN isUndec"+dialog.isUndecorated());
      return dialog;
   }

   public static Object showClearStateDialog(Component parentComponent, String title, Object message) {
      String dialogClear = FactoryTable.getI18NFactory().makeButtonLabelBundle().getString("DialogClear");
      String[] options = new String[]{dialogClear};
      String msg = (String)message;
      JOptionPane pane = new SureviewOptionPane(msg, 1, -1, messageIcon, options, options[0]);
      ErrorMessageHandler emh = new ErrorMessageHandler(pane, true);
      msg = OperatorPromptData.getInstance().getDisplayableMessage();
      if (msg.trim().length() > 0) {
         pane.setMessage(msg);
      }

      JDialog dialog = pane.createDialog(parentComponent, title);
      Dimension dlgSize = dialog.getSize();
      originalMessage = msg;
      holdpane = pane;

      try {
         AEFPropertyChangeListener AEFSOPDataListener = new AEFPropertyChangeListener() {
            public void propertyChanged(AEFPropertyChangeEvent event) throws RemoteException {
               if (event.getPropertyName().equals("QUEUE_LOCKED")) {
                  String busyStr = (String)event.getNewValue();
                  boolean isSystemBusy = Boolean.valueOf(busyStr);
                  if (isSystemBusy != SureviewOptionPane.lastBusy && SureviewOptionPane.holdpane != null) {
                     SureviewOptionPane.lastBusy = isSystemBusy;
                     if (isSystemBusy) {
                        SureviewOptionPane.holdpane.setMessage(new Object[]{SureviewOptionPane.sysBusyPlsWait});
                     } else {
                        SureviewOptionPane.holdpane.setMessage(new Object[]{SureviewOptionPane.originalMessage});
                     }
                  }
               }

            }
         };
         POSDataProvider posDataProvider = SureviewApplication.getInstance().getSession().getPOSDataProvider();
         if (aefPropProxy != null) {
            aefPropProxy.removeAEFPropertyChangeListener("WORKSTATION_STATUS", "QUEUE_LOCKED");
         }

         aefPropProxy = new AEFPropertyListenerProxyImpl(posDataProvider, AEFSOPDataListener);
         aefPropProxy.addAEFPropertyChangeListener("WORKSTATION_STATUS", "QUEUE_LOCKED");
         String busyStr = (String)posDataProvider.getPropertyValue("WORKSTATION_STATUS", "QUEUE_LOCKED", false);
         boolean busy = Boolean.valueOf(busyStr);
         lastBusy = busy;
         if (busy && holdpane != null) {
            holdpane.setMessage(new Object[]{sysBusyPlsWait});
         }
      } catch (RemoteException var16) {
         System.err.println(var16);
      }

      double newWidth = 0.0D;
      if (SVProperties.screenSize == 1) {
         newWidth = 360.0D;
      } else if (SVProperties.screenSize == 2) {
         newWidth = 409.6D;
      } else if (SVProperties.screenSize == 3) {
         newWidth = 546.4D;
      } else if (SVProperties.screenSize == 4) {
         newWidth = 768.0D;
      } else {
         newWidth = 300.79999999999995D;
      }

      if ((double)dlgSize.width > newWidth) {
         newWidth = (double)dlgSize.width;
      }

      dialog.setSize((int)newWidth, (int)dlgSize.getHeight());
      if (!SVProperties.getApplicationResizable()) {
         dialog.setResizable(false);
      }

      dialog.setLocationRelativeTo(parentComponent);
      pane.selectInitialValue();
      isClearStateDialogDisplayed = true;
      if (lastOptionPaneDialogDisplayed != null) {
         lastOptionPaneDialogDisplayed.dispose();
         lastOptionPaneDialogDisplayed = null;
      }

      lastOptionPaneDialogDisplayed = dialog;
      Object selectedValue;
      if (emh.isClearState(SureviewApplication.getInstance().getPOSStateID()) && SVProperties.shouldCloseDialog(msg)) {
         dialog.dispose();
         emh.setValue(new Integer(-200));
         selectedValue = new Integer(-200);
      } else if (emh.isClearState(SureviewApplication.getInstance().getPOSStateID())) {
         OperatorPromptData.getInstance().clear();
         dialog.show();
         selectedValue = pane.getValue();
      } else {
         dialog.dispose();
         selectedValue = null;
      }

      if (SwingUtilities.isEventDispatchThread()) {
         lastOptionPaneDialogDisplayed = null;
         isClearStateDialogDisplayed = false;
      }

      int dialogCloseBehavior = dialog.getDefaultCloseOperation();
      dialog.setDefaultCloseOperation(dialogCloseBehavior);
      emh.removeListener();
      emh = null;
      dialog = null;
      pane = null;

      try {
         if (aefPropProxy != null) {
            aefPropProxy.removeAEFPropertyChangeListener("WORKSTATION_STATUS", "QUEUE_LOCKED");
         }
      } catch (Throwable var15) {
         System.err.println(var15);
      }

      aefPropProxy = null;
      KeyPad.removeModalKeyPadListener();
      return selectedValue;
   }

   public static void showWarningMessageDialog(Component parentComponent, String title, Object message) {
      JOptionPane pane = new SureviewOptionPane(message, 1, -1, messageIcon);
      JDialog dialog = pane.createDialog(parentComponent, title);
      if (!SVProperties.getApplicationResizable()) {
         dialog.setResizable(false);
      }

      pane.selectInitialValue();
      if (lastOptionPaneDialogDisplayed != null) {
         lastOptionPaneDialogDisplayed.dispose();
         lastOptionPaneDialogDisplayed = null;
      }

      lastOptionPaneDialogDisplayed = dialog;
      dialog.show();
      if (SwingUtilities.isEventDispatchThread()) {
         lastOptionPaneDialogDisplayed = null;
      }

      Object selectedValue = pane.getValue();
      KeyPad.removeModalKeyPadListener();
   }

   public static void showErrorMessageDialog(Component parentComponent, String title, Object message) {
      JOptionPane pane = new SureviewOptionPane(message, 0, -1);
      JDialog dialog = pane.createDialog(parentComponent, title);
      pane.selectInitialValue();
      if (lastOptionPaneDialogDisplayed != null) {
         lastOptionPaneDialogDisplayed.dispose();
         lastOptionPaneDialogDisplayed = null;
      }

      lastOptionPaneDialogDisplayed = dialog;
      dialog.show();
      if (SwingUtilities.isEventDispatchThread()) {
         lastOptionPaneDialogDisplayed = null;
      }

      Object selectedValue = pane.getValue();
      KeyPad.removeModalKeyPadListener();
   }

   public static int showYesNoOptionDialog(Component parentComponent, Object message) {
      ResourceBundle buttonText = FactoryTable.getI18NFactory().makeButtonLabelBundle();
      ResourceBundle labelText = FactoryTable.getI18NFactory().makeLabelBundle();
      int result = 0; //false
      String[] options = new String[]{buttonText.getString("Yes"), buttonText.getString("No")};
      JOptionPane pane = new SureviewOptionPane(message, 3, 0, questionIcon, options, options[0]);
      pane.setInitialValue(options[0]);
      JDialog dialog = pane.createDialog(parentComponent, labelText.getString("warning"));
      pane.selectInitialValue();
      if (lastOptionPaneDialogDisplayed != null) {
         lastOptionPaneDialogDisplayed.dispose();
         lastOptionPaneDialogDisplayed = null;
      }

      lastOptionPaneDialogDisplayed = dialog;
      dialog.show();
      Object selectedValue = pane.getValue();
      KeyPad.removeModalKeyPadListener();
      if (SwingUtilities.isEventDispatchThread()) {
         lastOptionPaneDialogDisplayed = null;
      }

      if (selectedValue == null) {
         return -1;
      } else if (options == null) {
         return selectedValue instanceof Integer ? (Integer)selectedValue : -1;
      } else {
         int counter = 0;

         for(int maxCounter = options.length; counter < maxCounter; ++counter) {
            if (options[counter].equals(selectedValue)) {
               return counter;
            }
         }

         return -1;
      }
   }

   public static int showYesNoCancelOptionDialog(Component parentComponent, Object message) {
      ResourceBundle buttonText = FactoryTable.getI18NFactory().makeButtonLabelBundle();
      ResourceBundle labelText = FactoryTable.getI18NFactory().makeLabelBundle();
      int result = 0; //false
      String[] options = new String[]{buttonText.getString("Yes"), buttonText.getString("No"), buttonText.getString("Cancel")};
      JOptionPane pane = new SureviewOptionPane(message, 3, 0, questionIcon, options, options[0]);
      pane.setInitialValue(options[0]);
      JDialog dialog = pane.createDialog(parentComponent, labelText.getString("selectOption"));
      pane.selectInitialValue();
      if (lastOptionPaneDialogDisplayed != null) {
         lastOptionPaneDialogDisplayed.dispose();
         lastOptionPaneDialogDisplayed = null;
      }

      lastOptionPaneDialogDisplayed = dialog;
      dialog.show();
      Object selectedValue = pane.getValue();
      KeyPad.removeModalKeyPadListener();
      if (selectedValue == null) {
         return -1;
      } else if (options == null) {
         return selectedValue instanceof Integer ? (Integer)selectedValue : -1;
      } else {
         int counter = 0;

         for(int maxCounter = options.length; counter < maxCounter; ++counter) {
            if (options[counter].equals(selectedValue)) {
               return counter;
            }
         }

         return -1;
      }
   }

   public static int showMultipleOptionDialog(Component parentComponent, Icon icon, Object message, Object[] options) {
      return showMultipleOptionDialog(parentComponent, icon, message, options, 0, 0);
   }

   public static int showMultipleOptionDialog(Component parentComponent, Icon icon, Object message, Object[] options, int activeState, int activeSubState) {
      ResourceBundle buttonText = FactoryTable.getI18NFactory().makeButtonLabelBundle();
      ResourceBundle labelText = FactoryTable.getI18NFactory().makeLabelBundle();
      int result = 0; //false
      Object[] labels = null;
      if (options[0] instanceof SureviewAction) {
         labels = new Object[options.length];

         for(int i = 0; i < options.length; ++i) {
            labels[i] = ((SureviewAction)options[i]).getName().replace('\n', ' ');
         }
      } else {
         labels = options;
      }

      if (icon == null) {
         icon = questionIcon;
      }

      JOptionPane pane = new SureviewOptionPane(message, 3, 0, icon, labels, labels[0]);
      pane.setInitialValue(labels[0]);
      if (activeState != 0 || activeSubState != 0) {
         new ErrorMessageHandler(pane, true, activeState, activeSubState, true);
      }

      JDialog dialog = pane.createDialog(parentComponent, labelText.getString("selectOption"));
      pane.selectInitialValue();
      if (lastOptionPaneDialogDisplayed != null) {
         lastOptionPaneDialogDisplayed.dispose();
         lastOptionPaneDialogDisplayed = null;
      }

      lastOptionPaneDialogDisplayed = dialog;
      dialog.show();
      Object selectedValue = pane.getValue();
      KeyPad.removeModalKeyPadListener();
      if (SwingUtilities.isEventDispatchThread()) {
         lastOptionPaneDialogDisplayed = null;
      }

      if (selectedValue == null) {
         return -1;
      } else if (options == null) {
         return selectedValue instanceof Integer ? (Integer)selectedValue : -1;
      } else {
         int counter = 0;

         for(int maxCounter = options.length; counter < maxCounter; ++counter) {
            if (options[counter] instanceof SureviewAction && ((SureviewAction)options[counter]).getName().replace('\n', ' ').equals(selectedValue.toString()) || options[counter].equals(selectedValue)) {
               if (options[counter] instanceof SureviewAction) {
                  SureviewAction theAction = (SureviewAction)options[counter];
                  theAction.actionPerformed((ActionEvent)null);
                  return -100;
               } else {
                  return counter;
               }
            }
         }

         return -1;
      }
   }

   public static int showPromptListeningOptionDialog(Component parentComponent, Icon icon, Object message, Object[] options) {
      return showPromptListeningOptionDialog(parentComponent, icon, message, options, 0);
   }

   public static int showPromptListeningOptionDialog(Component parentComponent, Icon icon, Object message, Object[] options, int stateID) {
      return showPromptListeningOptionDialog(parentComponent, icon, message, options, stateID, 0);
   }

   public static int showPromptListeningOptionDialog(Component parentComponent, Icon icon, Object message, Object[] options, int stateID, int substateID) {
      ResourceBundle buttonText = FactoryTable.getI18NFactory().makeButtonLabelBundle();
      ResourceBundle labelText = FactoryTable.getI18NFactory().makeLabelBundle();
      int result = 0; //false
      Object[] labels = null;
      if (options[0] instanceof SureviewAction) {
         labels = new Object[options.length];

         for(int i = 0; i < options.length; ++i) {
            labels[i] = new ReferencedAction((SureviewAction)((SureviewAction)options[i]));
         }
      } else {
         labels = options;
      }

      if (icon == null) {
         icon = questionIcon;
      }

      JOptionPane pane = new SureviewOptionPane(message, 3, 0, icon, labels, labels[0]);
      pane.setInitialValue(options[0]);
      ErrorMessageHandler emh;
      if (stateID == 0) {
         emh = new ErrorMessageHandler(pane);
      } else {
         if (substateID == 0) {
            emh = new ErrorMessageHandler(pane, true, stateID);
         } else {
            emh = new ErrorMessageHandler(pane, true, stateID, substateID);
         }

         if (message != null) {
            emh.ignorePrompts(true);
         }
      }

      String msg = OperatorPromptData.getInstance().getDisplayableMessage();
      if (msg.trim().length() > 0) {
         if (message == null || message.toString().trim().equals("")) {
            pane.setMessage(OperatorPromptData.getInstance().getDisplayableMessage());
         }

         OperatorPromptData.getInstance().clear();
      }

      if (lastOptionPaneDialogDisplayed != null) {
         lastOptionPaneDialogDisplayed.dispose();
         lastOptionPaneDialogDisplayed = null;
      }

      JDialog dialog = pane.createDialog(parentComponent, labelText.getString("selectOption"));
      pane.selectInitialValue();
      lastOptionPaneDialogDisplayed = dialog;
      dialog.show();
      if (SwingUtilities.isEventDispatchThread()) {
         lastOptionPaneDialogDisplayed = null;
      }

      Object selectedValue = pane.getValue();
      emh.removeListener();
      emh = null;
      KeyPad.removeModalKeyPadListener();
      if (selectedValue == null) {
         return -1;
      } else if (labels == null) {
         return selectedValue instanceof Integer ? (Integer)selectedValue : -1;
      } else {
         int counter = 0;

         for(int maxCounter = options.length; counter < maxCounter; ++counter) {
            if (labels[counter].toString().equals(selectedValue.toString())) {
               if (selectedValue instanceof ReferencedAction) {
                  SureviewAction theAction = ((ReferencedAction)((ReferencedAction)selectedValue)).getAction();
                  theAction.actionPerformed((ActionEvent)null);
                  return -100;
               }

               if (selectedValue instanceof Integer) {
                  return (Integer)selectedValue;
               }

               return counter;
            }
         }

         return -1;
      }
   }
}
