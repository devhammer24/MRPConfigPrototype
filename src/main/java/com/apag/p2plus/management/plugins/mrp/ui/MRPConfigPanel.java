package com.apag.p2plus.management.plugins.mrp.ui;

import com.apag.p2plus.management.plugins.mrp.model.ConfigItem;
import com.apag.p2plus.management.plugins.mrp.model.Scenario;
import com.apag.p2plus.management.plugins.mrp.service.OperationalConfigService;
import com.apag.p2plus.management.plugins.mrp.service.ScenarioService;
import com.apag.p2plus.management.plugins.mrp.service.TechnicalConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main panel of the MRP Configuration application
 */
public class MRPConfigPanel extends JPanel {

  private static final Logger logger = LoggerFactory.getLogger(MRPConfigPanel.class);
  
  // Dialog messages
  private static final String SCENARIO_CREATED_SUCCESS = "The new scenario has been created successfully!";
  private static final String SCENARIO_CREATED_TITLE = "Scenario Created";
  private static final String SCENARIO_CREATION_ERROR = "The scenario could not be created. Please try again.";
  private static final String SCENARIO_CREATION_ERROR_TITLE = "Creation Error";
  private static final String SCENARIO_INPUT_VALIDATION = "Please enter both a Scenario ID and a Scenario Name.";
  private static final String INPUT_REQUIRED_TITLE = "Input Required";
  private static final String CREATE_SCENARIO_TITLE = "Create New Scenario";
  private static final String ERROR_TITLE = "Error";
  
  private final ScenarioService scenarioService;
  private final TechnicalConfigService technicalConfigService;
  private final OperationalConfigService operationalConfigService;
  
  private JComboBox<Scenario> scenarioComboBox;
  private JPanel technicalConfigPanel;
  private JPanel operationalConfigPanel;
  private JSplitPane splitPane;
  private Map<String, JComponent> technicalConfigComponents;
  private Map<String, JComponent> operationalConfigComponents;

  public MRPConfigPanel(ScenarioService scenarioService,
                       TechnicalConfigService technicalConfigService,
                       OperationalConfigService operationalConfigService) {
    this.scenarioService = scenarioService;
    this.technicalConfigService = technicalConfigService;
    this.operationalConfigService = operationalConfigService;
    this.technicalConfigComponents = new HashMap<>();
    this.operationalConfigComponents = new HashMap<>();
    
    initializeComponents();
    layoutComponents();
    loadScenarios();
  }

  private void initializeComponents() {
    setLayout(new BorderLayout());

    scenarioComboBox = new JComboBox<>();
    scenarioComboBox.setPreferredSize(new Dimension(200, 25));

    technicalConfigPanel = new JPanel(new GridBagLayout());
    operationalConfigPanel = new JPanel(new GridBagLayout());
  }

  private void layoutComponents() {
    add(createToolbarPanel(), BorderLayout.NORTH);
    add(createContentPanel(), BorderLayout.CENTER);
  }

  private JPanel createToolbarPanel() {
    JPanel toolbarPanel = new JPanel(new BorderLayout());
    toolbarPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    // Left area with scenario selection
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel scenarioLabel = new JLabel("Scenario:");
    scenarioLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    leftPanel.add(scenarioLabel);
    leftPanel.add(scenarioComboBox);

    // Right area with buttons
    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
    JButton createScenarioButton = new JButton("➕ Create Scenario");
    createScenarioButton.addActionListener(e -> onCreateScenarioClicked());
    
    JButton saveButton = new JButton("💾 Save");
    saveButton.addActionListener(e -> onSaveConfigurationClicked());
    
    rightPanel.add(createScenarioButton);
    rightPanel.add(saveButton);

    scenarioComboBox.addActionListener(e -> onScenarioSelectionChanged());

    toolbarPanel.add(leftPanel, BorderLayout.WEST);
    toolbarPanel.add(rightPanel, BorderLayout.EAST);

    return toolbarPanel;
  }

  private JPanel createContentPanel() {
    JPanel contentPanel = new JPanel(new BorderLayout());

    JPanel technicalPanel = createTechnicalConfigPanel();
    JPanel operationalPanel = createOperationalConfigPanel();

    splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, technicalPanel, operationalPanel);
    splitPane.setResizeWeight(0.5);
    splitPane.setOneTouchExpandable(true);
    splitPane.setContinuousLayout(true);

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        splitPane.setDividerLocation(0.5);
      }
    });

    contentPanel.add(splitPane, BorderLayout.CENTER);
    return contentPanel;
  }

  private JPanel createTechnicalConfigPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createEmptyBorder(5, 5, 5, 5),
      new TitledBorder("Technical")
    ));

    JPanel wrapperPanel = new JPanel();
    wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
    wrapperPanel.add(technicalConfigPanel);

    JScrollPane scrollPane = new JScrollPane(wrapperPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    panel.add(scrollPane, BorderLayout.CENTER);
    loadTechnicalConfig();

    return panel;
  }

  private void loadScenarios() {
    scenarioComboBox.addItem(new Scenario("loading", "Loading scenarios..."));
    scenarioComboBox.setEnabled(false);

    scenarioService.loadAsync().thenAccept(scenarios -> {
      SwingUtilities.invokeLater(() -> {
        scenarioComboBox.removeAllItems();

        if (scenarios != null && !scenarios.isEmpty()) {
          for (Scenario scenario : scenarios) {
            scenarioComboBox.addItem(scenario);
          }
        } else {
          scenarioComboBox.addItem(new Scenario("error", "Error loading scenarios"));
        }

        scenarioComboBox.setEnabled(true);
      });
    }).exceptionally(throwable -> {
      SwingUtilities.invokeLater(() -> {
        scenarioComboBox.removeAllItems();
        scenarioComboBox.addItem(new Scenario("error", "Network error"));
        scenarioComboBox.setEnabled(true);
      });
      return null;
    });
  }

  public Scenario getSelectedScenario() {
    return (Scenario) scenarioComboBox.getSelectedItem();
  }

  private void loadTechnicalConfig() {
    technicalConfigPanel.removeAll();
    technicalConfigComponents.clear();

    addLoadingLabel(technicalConfigPanel, "Loading technical configuration...");

    technicalConfigService.loadAsync().thenAccept(configItems -> {
      SwingUtilities.invokeLater(() -> {
        if (configItems != null) {
          buildTechnicalConfigUI(configItems);
        } else {
          showErrorInPanel(technicalConfigPanel, "No technical configuration available");
        }
      });
    }).exceptionally(throwable -> {
      SwingUtilities.invokeLater(() -> {
        showErrorInPanel(technicalConfigPanel, "Error loading technical configuration");
      });
      return null;
    });
  }

  private void addLoadingLabel(JPanel panel, String text) {
    JLabel loadingLabel = new JLabel(text);
    loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(2, 2, 2, 2);
    panel.add(loadingLabel, gbc);
    panel.revalidate();
    panel.repaint();
  }

  private void showErrorInPanel(JPanel panel, String message) {
    panel.removeAll();
    JLabel errorLabel = new JLabel(message);
    errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
    errorLabel.setForeground(Color.RED);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(2, 2, 2, 2);
    panel.add(errorLabel, gbc);
    panel.revalidate();
    panel.repaint();
  }

  private void buildTechnicalConfigUI(List<ConfigItem> configItems) {
    technicalConfigPanel.removeAll();
    technicalConfigComponents.clear();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.anchor = GridBagConstraints.WEST;

    int row = 0;
    for (ConfigItem item : configItems) {
      JLabel label = new JLabel(item.getDescription() + ":");
      gbc.gridx = 0;
      gbc.gridy = row;
      gbc.gridwidth = 1;
      gbc.fill = GridBagConstraints.NONE;
      gbc.weightx = 0.0;
      technicalConfigPanel.add(label, gbc);

      JComponent component = createComponentForType(item);
      if (component != null) {
        technicalConfigComponents.put(item.getName(), component);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        technicalConfigPanel.add(component, gbc);
      }

      row++;
    }

    // Add spacer
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    technicalConfigPanel.add(new JPanel(), gbc);

    technicalConfigPanel.revalidate();
    technicalConfigPanel.repaint();
  }

  private JComponent createComponentForType(ConfigItem item) {
    switch (item.getType().toLowerCase()) {
      case "string":
        JTextField textField = new JTextField(item.getValueAsString());
        textField.setName(item.getName());
        return textField;

      case "password":
        JPasswordField passwordField = new JPasswordField(item.getValueAsString());
        passwordField.setName(item.getName());
        return passwordField;

      case "boolean":
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(item.getValueAsBoolean());
        checkBox.setName(item.getName());
        return checkBox;

      default:
        JTextField defaultField = new JTextField(item.getValueAsString());
        defaultField.setName(item.getName());
        return defaultField;
    }
  }

  private JPanel createOperationalConfigPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createEmptyBorder(5, 5, 5, 5),
      new TitledBorder("Operational")
    ));

    JPanel wrapperPanel = new JPanel();
    wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
    wrapperPanel.add(operationalConfigPanel);

    JScrollPane scrollPane = new JScrollPane(wrapperPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
  }

  private void onScenarioSelectionChanged() {
    Scenario selectedScenario = getSelectedScenario();
    if (selectedScenario != null && !selectedScenario.getScenarioId().equals("loading") && !selectedScenario.getScenarioId().equals("error")) {
      loadOperationalConfig(selectedScenario.getScenarioId());
    }
  }

  private void loadOperationalConfig(String scenarioId) {
    operationalConfigPanel.removeAll();
    operationalConfigComponents.clear();

    addLoadingLabel(operationalConfigPanel, "Loading operational configuration...");

    operationalConfigService.loadAsync(scenarioId).thenAccept(configItems -> {
      SwingUtilities.invokeLater(() -> {
        if (configItems != null) {
          buildOperationalConfigUI(configItems);
        } else {
          showErrorInPanel(operationalConfigPanel, "No operational configuration available");
        }
      });
    }).exceptionally(throwable -> {
      SwingUtilities.invokeLater(() -> {
        showErrorInPanel(operationalConfigPanel, "Error loading operational configuration");
      });
      return null;
    });
  }

  private void buildOperationalConfigUI(List<ConfigItem> configItems) {
    operationalConfigPanel.removeAll();
    operationalConfigComponents.clear();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.anchor = GridBagConstraints.WEST;

    int row = 0;
    for (ConfigItem item : configItems) {
      JLabel label = new JLabel(item.getDescription() + ":");
      gbc.gridx = 0;
      gbc.gridy = row;
      gbc.gridwidth = 1;
      gbc.fill = GridBagConstraints.NONE;
      gbc.weightx = 0.0;
      operationalConfigPanel.add(label, gbc);

      JComponent component = createComponentForType(item);
      if (component != null) {
        operationalConfigComponents.put(item.getName(), component);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        operationalConfigPanel.add(component, gbc);
      }

      row++;
    }

    // Add spacer
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    operationalConfigPanel.add(new JPanel(), gbc);

    operationalConfigPanel.revalidate();
    operationalConfigPanel.repaint();
  }

  private void onCreateScenarioClicked() {
    try {
      // Create custom dialog for scenario input
      ScenarioInputResult result = showCreateScenarioDialog();
      
      if (result == null) {
        // User cancelled
        return;
      }
      
      // Create new scenario object
      Scenario newScenario = new Scenario(result.scenarioId, result.scenarioName);
      
      // Call service to create scenario
      boolean success = scenarioService.createScenario(newScenario);
      
      if (success) {
        // Show success dialog
        JOptionPane.showMessageDialog(
          this,
          SCENARIO_CREATED_SUCCESS,
          SCENARIO_CREATED_TITLE,
          JOptionPane.INFORMATION_MESSAGE
        );
        
        // Reload scenarios to include the new one
        loadScenarios();
        
        logger.info("Scenario '{}' created successfully", newScenario.getScenarioId());
      } else {
        // Show error dialog
        JOptionPane.showMessageDialog(
          this,
          SCENARIO_CREATION_ERROR,
          SCENARIO_CREATION_ERROR_TITLE,
          JOptionPane.ERROR_MESSAGE
        );
      }
      
    } catch (Exception e) {
      logger.error("Error creating scenario", e);
      JOptionPane.showMessageDialog(
        this,
        "Error creating scenario: " + e.getMessage(),
        ERROR_TITLE,
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  /**
   * Shows a custom dialog for creating a new scenario
   * 
   * @return ScenarioInputResult with user input, or null if cancelled
   */
  private ScenarioInputResult showCreateScenarioDialog() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    
    // Scenario ID field
    JLabel idLabel = new JLabel("Scenario ID:");
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    panel.add(idLabel, gbc);
    
    JTextField idField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    panel.add(idField, gbc);
    
    // Scenario Name field
    JLabel nameLabel = new JLabel("Scenario Name:");
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0.0;
    panel.add(nameLabel, gbc);
    
    JTextField nameField = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    panel.add(nameField, gbc);
    
    // Show dialog
    int result = JOptionPane.showConfirmDialog(
      this,
      panel,
      CREATE_SCENARIO_TITLE,
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.PLAIN_MESSAGE
    );
    
    if (result == JOptionPane.OK_OPTION) {
      String scenarioId = idField.getText().trim();
      String scenarioName = nameField.getText().trim();
      
      // Validate input
      if (scenarioId.isEmpty() || scenarioName.isEmpty()) {
        JOptionPane.showMessageDialog(
          this,
          SCENARIO_INPUT_VALIDATION,
          INPUT_REQUIRED_TITLE,
          JOptionPane.WARNING_MESSAGE
        );
        return null;
      }
      
      return new ScenarioInputResult(scenarioId, scenarioName);
    }
    
    return null; // User cancelled
  }

  /**
   * Helper class to hold scenario input results
   */
  private static class ScenarioInputResult {
    final String scenarioId;
    final String scenarioName;
    
    ScenarioInputResult(String scenarioId, String scenarioName) {
      this.scenarioId = scenarioId;
      this.scenarioName = scenarioName;
    }
  }

  private void onSaveConfigurationClicked() {
    try {
      // Collect technical configuration from UI components
      List<ConfigItem> technicalConfigItems = collectTechnicalConfigFromUI();
      
      if (technicalConfigItems.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No technical configuration to save.", "Info", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      
      // Save technical configuration
      technicalConfigService.saveTechnicalConfig(technicalConfigItems);
      
      logger.info("Technical configuration saved successfully");
      JOptionPane.showMessageDialog(this, "Technical configuration saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
      
    } catch (Exception e) {
      logger.error("Error saving configuration", e);
      JOptionPane.showMessageDialog(this, "Failed to save configuration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Collects the current technical configuration from UI components
   * 
   * @return List of ConfigItem objects with current values from UI
   */
  private List<ConfigItem> collectTechnicalConfigFromUI() {
    List<ConfigItem> configItems = new ArrayList<>();
    
    for (Map.Entry<String, JComponent> entry : technicalConfigComponents.entrySet()) {
      String name = entry.getKey();
      JComponent component = entry.getValue();
      
      try {
        ConfigItem item = createConfigItemFromComponent(name, component);
        if (item != null) {
          configItems.add(item);
        }
      } catch (Exception e) {
        logger.warn("Error reading value for {}: {}", name, e.getMessage());
      }
    }
    
    return configItems;
  }

  /**
   * Creates a ConfigItem from a UI component
   * 
   * @param name Component name (configuration parameter name)
   * @param component UI component containing the value
   * @return ConfigItem with current value from UI component
   */
  private ConfigItem createConfigItemFromComponent(String name, JComponent component) {
    if (component instanceof JTextField) {
      JTextField textField = (JTextField) component;
      return new ConfigItem(name, "string", textField.getText(), null);
      
    } else if (component instanceof JPasswordField) {
      JPasswordField passwordField = (JPasswordField) component;
      char[] password = passwordField.getPassword();
      String passwordValue = new String(password);
      // Clear password from memory for security
      java.util.Arrays.fill(password, ' ');
      return new ConfigItem(name, "password", passwordValue, null);
      
    } else if (component instanceof JCheckBox) {
      JCheckBox checkBox = (JCheckBox) component;
      return new ConfigItem(name, "boolean", checkBox.isSelected(), null);
      
    } else {
      logger.warn("Unknown component type for {}: {}", name, component.getClass().getSimpleName());
      return null;
    }
  }

  public void cleanup() {
    if (scenarioService != null) {
      scenarioService.close();
    }
    if (technicalConfigService != null) {
      technicalConfigService.close();
    }
    if (operationalConfigService != null) {
      operationalConfigService.close();
    }
  }
}