package com.apag.p2plus.management.plugins.mrp;

import com.apag.p2plus.management.plugins.mrp.ui.MRPConfigPanel;
import com.apag.p2plus.management.plugins.mrp.service.ScenarioService;
import com.apag.p2plus.management.plugins.mrp.service.TechnicalConfigService;
import com.apag.p2plus.management.plugins.mrp.service.OperationalConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main application class for MRP Configuration Prototype
 */
public class MRPConfigApplication extends JFrame {

  private static final Logger logger = LoggerFactory.getLogger(MRPConfigApplication.class);
  private static final String APPLICATION_TITLE = "MRP Config Prototype";
  private static final int DEFAULT_WIDTH = 1024;
  private static final int DEFAULT_HEIGHT = 768;

  private MRPConfigPanel mainPanel;

  public MRPConfigApplication() {
    logger.info("Starting MRP Configuration Application");
    initializeFrame();
    createComponents();
    setupEventHandlers();
  }

  private void initializeFrame() {
    setTitle(APPLICATION_TITLE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    setLocationRelativeTo(null);

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      logger.warn("Could not set system look and feel: {}", e.getMessage());
    }
  }

  private void createComponents() {
    // Create services directly
    mainPanel = new MRPConfigPanel(
        new ScenarioService(),
        new TechnicalConfigService(),
        new OperationalConfigService()
    );
    
    add(mainPanel, BorderLayout.CENTER);
    add(createStatusBar(), BorderLayout.SOUTH);
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
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        logger.info("Application closing");
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

  public static void main(String[] args) {
    // Configure Swing for better performance
    System.setProperty("swing.aatext", "true");
    System.setProperty("awt.useSystemAAFontSettings", "on");

    SwingUtilities.invokeLater(() -> {
      try {
        new MRPConfigApplication().setVisible(true);
        logger.info("Application started successfully");
      } catch (Exception e) {
        logger.error("Failed to start application", e);
        JOptionPane.showMessageDialog(
          null,
          "Could not start application:\n" + e.getMessage(),
          "Startup Error",
          JOptionPane.ERROR_MESSAGE
        );
        System.exit(1);
      }
    });
  }
} 