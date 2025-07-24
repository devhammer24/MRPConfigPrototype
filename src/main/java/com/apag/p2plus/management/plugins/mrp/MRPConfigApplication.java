package com.apag.p2plus.management.plugins.mrp;

import com.apag.p2plus.management.plugins.mrp.ui.MRPConfigPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main application class for MRP Configuration Prototype
 */
public class MRPConfigApplication extends JFrame {

  private static final String APPLICATION_TITLE = "MRP Config Prototype";
  private static final int DEFAULT_WIDTH = 1024;
  private static final int DEFAULT_HEIGHT = 768;

  private MRPConfigPanel mainPanel;

  public MRPConfigApplication() {
    initializeFrame();
    createComponents();
    setupEventHandlers();
  }

  private void initializeFrame() {
    setTitle(APPLICATION_TITLE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    setLocationRelativeTo(null); // Center the window

    // Set application icon (optional)
    try {
      setLookAndFeel();
    } catch (Exception e) {
      System.err.println("Warning: Default Look-and-Feel could not be set: " + e.getMessage());
    }
  }

  private void setLookAndFeel() throws Exception {
    // Use system Look-and-Feel for better integration
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  }

  private void createComponents() {
    mainPanel = new MRPConfigPanel();
    add(mainPanel, BorderLayout.CENTER);

    // Status bar (optional for future extensions)
    JPanel statusBar = createStatusBar();
    add(statusBar, BorderLayout.SOUTH);
  }

  private JPanel createStatusBar() {
    JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
    statusBar.setBorder(BorderFactory.createLoweredBevelBorder());

    JLabel statusLabel = new JLabel("Ready");
    statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
    statusBar.add(statusLabel);

    return statusBar;
  }

  private void setupEventHandlers() {
    // Cleanup on window closing
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        cleanup();
        System.exit(0);
      }
    });
  }

  private void cleanup() {
    if (mainPanel != null) {
      mainPanel.cleanup();
    }
  }

  /**
   * Main method of the application
   * 
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    // Configure system properties for better Swing performance
    System.setProperty("swing.aatext", "true");
    System.setProperty("awt.useSystemAAFontSettings", "on");

    // Start the application in the Event Dispatch Thread
    SwingUtilities.invokeLater(() -> {
      try {
        MRPConfigApplication app = new MRPConfigApplication();
        app.setVisible(true);

        System.out.println("MRP Configuration Prototype started");
        System.out.println("Window size: " + DEFAULT_WIDTH + "x" + DEFAULT_HEIGHT);

      } catch (Exception e) {
        System.err.println("Error starting the application: " + e.getMessage());
        e.printStackTrace();

        // Show error dialog
        JOptionPane.showMessageDialog(
          null,
          "The application could not be started:\n" + e.getMessage(),
          "Startup Error",
          JOptionPane.ERROR_MESSAGE
        );

        System.exit(1);
      }
    });
  }
} 