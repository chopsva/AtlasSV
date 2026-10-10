package com.ibm.sureview.guiComponent;

import com.ibm.sureview.application.SureviewApplication;
import com.ibm.sureview.application.WorkstationStatus;
import com.ibm.sureview.resource.SVProperties;
import com.ibm.sureview.screen.ViewController;
import com.ibm.sureview.screen.WindowImplementation;
import com.ibm.sureview.util.SureviewUtilities;
import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Stack;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SureviewDialog extends JDialog {
   static final String COPYRIGHT = "\n\nLicensed Materials - Property of Toshiba Global Commerce Solutions, Inc.\nCopyright © 2004, 2016 Toshiba Global Commerce Solutions, Inc. All Rights Reserved.\nUS Government Users Restricted Rights - Use, duplication or\ndisclosure restricted by GSA ADP Schedule Contract with Toshiba Global Commerce Solutions, Inc.\n\n";
   private static Log log;
   protected static Stack dialogStack;
   protected static SureviewDialog lastDisplayedDialog;
   protected static WindowAdapter aWindowAdapter;
   public static final String SPECIAL_MODALITY_KEY = "SureviewDialog.specialModality.enabled";
   public static final String DISPOSE_ON_SCREEN_CHANGE_DEFAULT_KEY = "SureviewDialog.closeOnScreenChange.default";
   public static final String DIALOG_RELOCATE_DIRECTION = "SureviewDialog.relocateDirection";
   public static final String MOVE_DIALOG_UP = "UP";
   public static final String MOVE_DIALOG_DOWN = "DOWN";
   protected boolean specialModalityInProgress;
   protected boolean performSpecialModality;
   protected boolean closeIfScreenChanges = false;
   protected String relocateDirection;
   protected Frame parent;

   static String copyright() {
      return "\n\nLicensed Materials - Property of Toshiba Global Commerce Solutions, Inc.\nCopyright © 2004, 2016 Toshiba Global Commerce Solutions, Inc. All Rights Reserved.\nUS Government Users Restricted Rights - Use, duplication or\ndisclosure restricted by GSA ADP Schedule Contract with Toshiba Global Commerce Solutions, Inc.\n\n";
   }

   public SureviewDialog(Frame frame) {
      super(frame, true);
      this.parent = frame;
      this.initializeProcess();
   }

   public SureviewDialog(Frame frame, boolean modal) {
      super(frame, modal);
      this.parent = frame;
      this.initializeProcess();
   }

   public SureviewDialog(Frame frame, String title, boolean modal) {
      super(frame, title, modal);
      this.parent = frame;
      this.initializeProcess();
      //KEN
      this.setBackground(Color.DARK_GRAY);
      this.setForeground(Color.WHITE);
   }

   public void initializeProcess() {
      this.setDefaultCloseOperation(0);
      if (aWindowAdapter == null) {
         aWindowAdapter = new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
               TouchButton.setButtonEnabled(true);
            }
         };
      }

      if (SVProperties.is4690 && !SureviewApplication.getInstance().isXPDMode()) {
         if (log.isDebugEnabled()) {
            log.debug("focus listener added: " + this.getName());
         }

         this.addWindowFocusListener(SureviewApplication.getInstance());
      }

      boolean defaultBehavior = SVProperties.getBooleanValue("SureviewDialog.closeOnScreenChange.default");
      String classname = this.getClass().getName();
      String key = "SureviewDialog.closeOnScreenChange." + classname.substring(classname.lastIndexOf(".") + 1);
      this.closeIfScreenChanges = SVProperties.getBooleanValue(key, defaultBehavior);
      this.relocateDirection = SVProperties.getStringValue("SureviewDialog.relocateDirection");
      if (System.getProperty("java.version").startsWith("1.4")) {
         this.setSpecialModalityEnabled(SVProperties.getBooleanValue("SureviewDialog.specialModality.enabled", false));
      } else {
         this.setSpecialModalityEnabled(false);
      }

      if (!this.isBaseCursorVisible()) {
         this.setCursor(SureviewUtilities.getTransparentCursor());
      }

   }

   protected void centerDialog() {
      Dimension screenSize;
      Point startingPoint;
      if (this.parent != null) {
         screenSize = this.parent.getSize();
         startingPoint = this.parent.getLocation();
      } else {
         Component parent = ViewController.getInstance().getWindowImplementation().getLocalWindowComponent();
         screenSize = parent.getSize();
         startingPoint = parent.getLocationOnScreen();
      }

      Dimension dialogSize = this.getInitialSize();
      this.setSize(dialogSize);
      startingPoint.x += screenSize.width / 2 - dialogSize.width / 2;
      startingPoint.y += screenSize.height / 2 - dialogSize.height / 2;
      this.setLocation(startingPoint);
   }

   public Dimension getInitialSize() {
      short w;
      short h;
      if (SVProperties.screenSize == 1) {
         w = 300;
         h = 170;
      } else if (SVProperties.screenSize == 2) {
         w = 380;
         h = 200;
      } else if (SVProperties.screenSize == 3) {
         w = 510;
         h = 200;
      } else if (SVProperties.screenSize == 4) {
         w = 715;
         h = 300;
      } else {
         w = 240;
         h = 160;
      }

      return new Dimension(w, h);
   }

   public void setSpecialModalityEnabled(boolean enableSpecial) {
      this.performSpecialModality = enableSpecial;
   }

   public boolean isSpecialModalityEnabled() {
      return this.performSpecialModality;
   }

   public boolean isModal() {
      return super.isModal() && (!this.isSpecialModalityEnabled() || !System.getProperty("java.version").startsWith("1.4") && !System.getProperty("java.version").startsWith("1.5"));
   }

   public void show() {
      if (!SwingUtilities.isEventDispatchThread()) {
         try {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  SureviewDialog.this.showFromSwing();
               }
            });
         } catch (Exception var2) {
            SureviewUtilities.caught("WindowImplementation cannot start screen saver e = ", var2);
         }
      } else {
         this.showFromSwing();
      }

   }

   public void showFromSwing() {
      SureviewApplication.blockInput(false);
      ViewController.getInstance().getWindowImplementation().resetScreenSaver();
      boolean isAlreadyShown = false;
      synchronized(dialogStack) {
         if (dialogStack.size() != 0 && dialogStack.peek().getClass().getName().equals(this.getClass().getName()) && this.getTitle() != null && this.getTitle().equals(((SureviewDialog)dialogStack.peek()).getTitle())) {
            isAlreadyShown = true;
         }

         dialogStack.notify();
      }

      if (!isAlreadyShown) {
         this.addToStack(this);
         this.addWindowListener(aWindowAdapter);
         if (this.closeIfScreenChanges) {
            SureviewApplication.getInstance().registerVisibleDialog(this);
         }

         this.processScaleOverlayPrevention();
         if (log.isTraceEnabled()) {
            log.trace("Are we modal and Java 1.6: " + (this.isModal() && System.getProperty("java.version").startsWith("1.6")));
         }

         if (this.isModal() && System.getProperty("java.version").startsWith("1.6")) {
            try {
               Object mtObject = null;
               Class[] classes = Dialog.class.getClasses();

               for(int i = 0; i < classes.length; ++i) {
                  log.trace("Class(" + i + ") = " + classes[i].getName());
                  if (classes[i].getName().endsWith("ModalityType")) {
                     mtObject = classes[i];
                     break;
                  }
               }

               if (mtObject != null) {
                  Object dtObject = SureviewUtilities.invokeMethod((Object)mtObject, "valueOf", new Class[]{String.class}, new Object[]{"DOCUMENT_MODAL"});
                  SureviewUtilities.invokeMethod((Object)this, "setModalityType", new Class[]{dtObject.getClass()}, new Object[]{dtObject});
               }
            } catch (Throwable var5) {
               log.error("Error while setting Java6 modality type: " + var5.getMessage(), var5);
            }
         }

         super.show();
         if (this.isSpecialModalityEnabled() && !this.isModal()) {
            this.performSpecialModality();
         }

      }
   }

   protected void performSpecialModality() {
      this.specialModalityInProgress = true;
      log.debug("Performing special modality logic with: " + this.getParent());
      if (this.getParent() instanceof JFrame) {
         JFrame parent = (JFrame)this.getParent();
         Component oldOverlay = parent.getGlassPane();
         oldOverlay.setVisible(false);
         SureviewDialogOverlay mouseStealer = new SureviewDialogOverlay("MouseStealer", new Hashtable());
         mouseStealer.setEnforcedWindow(this);
         parent.setGlassPane(mouseStealer);
         mouseStealer.setVisible(true);
         mouseStealer.setActive(true);
         log.trace("Got parent: " + parent.getClass() + " -> " + parent.toString());
         parent.setEnabled(false);

         try {
            if (SwingUtilities.isEventDispatchThread()) {
               log.trace("Performing Event Dispatcher Thread wait logic...");
               EventQueue theQueue = this.getToolkit().getSystemEventQueue();

               while(this.isVisible()) {
                  AWTEvent event = theQueue.getNextEvent();
                  Object source = event.getSource();
                  if (event instanceof ActiveEvent) {
                     ((ActiveEvent)event).dispatch();
                  } else if (source instanceof Component) {
                     ((Component)source).dispatchEvent(event);
                  } else if (source instanceof MenuComponent) {
                     ((MenuComponent)source).dispatchEvent(event);
                  } else {
                     log.info("Unable to dispatch: " + event);
                  }
               }
            } else {
               log.trace("Performing regular logic...  Has this class been extended?");

               while(this.isVisible()) {
                  synchronized(this.getTreeLock()) {
                     this.getTreeLock().wait();
                  }
               }
            }
         } catch (Exception var9) {
            log.error("An error has occurred while performing special modality", var9);
         }

         parent.setEnabled(true);
         parent.toFront();
         mouseStealer.setActive(false);
         mouseStealer.setVisible(false);
         oldOverlay.setVisible(true);
         parent.setGlassPane(oldOverlay);
         this.specialModalityInProgress = false;
      }

      log.debug("Special modality handling complete.");
   }

   public void addToStack(SureviewDialog obj) {
      synchronized(dialogStack) {
         dialogStack.push(obj);
         dialogStack.notify();
      }
   }

   public void setCloseIfScreenChanges(boolean value) {
      if (!this.isShowing()) {
         this.closeIfScreenChanges = value;
      }

   }

   public void setVisible(boolean b) {
      super.setVisible(b);
      synchronized(this.getTreeLock()) {
         this.getTreeLock().notifyAll();
      }
   }

   public void dispose() {
      this.setDefaultCloseOperation(2);
      synchronized(dialogStack) {
         if (!dialogStack.empty()) {
            dialogStack.pop();
         } else {
            KeyPad.removeModalKeyPadListener();
         }
      }

      TouchButton.setButtonEnabled(true);
      if (this.closeIfScreenChanges) {
         SureviewApplication.getInstance().unregisterVisibleDialog(this);
      }

      super.dispose();
   }

   public static boolean dialogShowing() {
      boolean showing = false;
      synchronized(dialogStack) {
         if (!dialogStack.empty()) {
            showing = true;
         }

         return showing;
      }
   }

   public void setLocation(int x, int y) {
      super.setLocation(x, y);
      this.processScaleOverlayPrevention();
   }

   public void setBounds(int x, int y, int width, int height) {
      super.setBounds(x, y, width, height);
      this.processScaleOverlayPrevention();
   }

   protected void processScaleOverlayPrevention() {
      Rectangle scaleBounds = null;

      try {
         scaleBounds = (Rectangle)SureviewApplication.getInstance().getWorkstationStatus().get("SCALE_FRAME_ABSOLUTE_BOUNDS");
      } catch (Throwable var5) {
      }

      if (scaleBounds != null) {
         Rectangle dialogRect = new Rectangle(super.getLocation(), this.getSize());
         Rectangle sharedSpace = null;
         if ((sharedSpace = dialogRect.intersection(scaleBounds)) != null && !sharedSpace.isEmpty()) {
            if (this.relocateDirection.equals("UP")) {
               if (dialogRect.y - sharedSpace.height < 0) {
                  super.setBounds(dialogRect.x, 0, dialogRect.width, dialogRect.height + (dialogRect.y - sharedSpace.height));
               } else {
                  super.setLocation(dialogRect.x, dialogRect.y - sharedSpace.height);
               }
            } else if (this.relocateDirection.equals("DOWN")) {
               int screenBottomPosition = WindowImplementation.getWindowComponent().getLocationOnScreen().y + WindowImplementation.getWindowComponent().getHeight();
               if (scaleBounds.y + scaleBounds.height + dialogRect.height > screenBottomPosition) {
                  super.setBounds(dialogRect.x, scaleBounds.y + scaleBounds.height, dialogRect.width, screenBottomPosition - (scaleBounds.y + scaleBounds.height));
               } else {
                  super.setLocation(dialogRect.x, dialogRect.y + dialogRect.height + (scaleBounds.height - sharedSpace.height));
               }
            }
         }
      }

   }

   protected boolean isBaseCursorVisible() {
      boolean cursorVisible = true;
      if (WindowImplementation.getWindowComponent() != null) {
         WorkstationStatus workStat = SureviewApplication.getInstance().getWorkstationStatus();
         String curVisible = (String)((String)workStat.get(WorkstationStatus.CURSOR_VISIBLE));
         cursorVisible = Boolean.valueOf(curVisible);
      }

      return cursorVisible;
   }

   static {
      log = LogFactory.getLog(SureviewDialog.class);
      dialogStack = new Stack();
      lastDisplayedDialog = null;
      aWindowAdapter = null;
   }
}
