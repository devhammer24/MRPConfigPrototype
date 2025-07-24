package com.apag.p2plus.management.plugins.mrp.ui;

import com.apag.p2plus.management.plugins.mrp.model.ConfigItem;
import com.apag.p2plus.management.plugins.mrp.model.Scenario;
import com.apag.p2plus.management.plugins.mrp.service.OperationalConfigService;
import com.apag.p2plus.management.plugins.mrp.service.ScenarioService;
import com.apag.p2plus.management.plugins.mrp.service.TechnicalConfigService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main panel of the MRP Configuration application
 */
public class MRPConfigPanel extends JPanel {

  private final ScenarioService scenarioService;
  private final TechnicalConfigService technicalConfigService;
  private final OperationalConfigService operationalConfigService;
  private JComboBox<Scenario> scenarioComboBox;
  private JPanel technicalConfigPanel;
  private JPanel operationalConfigPanel;
  private JSplitPane splitPane;
  private Map<String, JComponent> technicalConfigComponents;
  private Map<String, JComponent> operationalConfigComponents;

  public MRPConfigPanel() {
    this.scenarioService = new ScenarioService();
    this.technicalConfigService = new TechnicalConfigService();
    this.operationalConfigService = new OperationalConfigService();
    this.technicalConfigComponents = new HashMap<>();
    this.operationalConfigComponents = new HashMap<>();
    initializeComponents();
    layoutComponents();
    loadScenarios();
  }

  private void initializeComponents() {
    setLayout(new BorderLayout());

    // Scenario ComboBox
    scenarioComboBox = new JComboBox<>();
    scenarioComboBox.setPreferredSize(new Dimension(200, 25));

    // Technical configuration panel
    technicalConfigPanel = new JPanel();
    technicalConfigPanel.setLayout(new GridBagLayout());

    // Operational configuration panel
    operationalConfigPanel = new JPanel();
    operationalConfigPanel.setLayout(new GridBagLayout());
  }

  private void layoutComponents() {
    // Toolbar Panel
    JPanel toolbarPanel = createToolbarPanel();
    add(toolbarPanel, BorderLayout.NORTH);

    // Main content panel with split areas
    JPanel contentPanel = createContentPanel();
    add(contentPanel, BorderLayout.CENTER);
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

    // Right area with Create Scenario button
    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton createScenarioButton = new JButton("Create scenario");
    createScenarioButton.addActionListener(e -> onCreateScenarioClicked());
    rightPanel.add(createScenarioButton);

    // Add listener for scenario selection changes
    scenarioComboBox.addActionListener(e -> onScenarioSelectionChanged());

    toolbarPanel.add(leftPanel, BorderLayout.WEST);
    toolbarPanel.add(rightPanel, BorderLayout.EAST);

    return toolbarPanel;
  }

  private JPanel createContentPanel() {
    JPanel contentPanel = new JPanel(new BorderLayout());

    // Technical Config Panel
    JPanel technicalPanel = createTechnicalConfigPanel();

    // Operational Config Panel
    JPanel operationalPanel = createOperationalConfigPanel();

    // Split pane for vertically dividable areas
    splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, technicalPanel, operationalPanel);
    splitPane.setResizeWeight(0.5); // Equal distribution
    splitPane.setOneTouchExpandable(true);
    splitPane.setContinuousLayout(true);

    // Event listener for dynamic resizing
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        // Maintain proportional distribution
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

    // Create wrapper panel with BoxLayout for automatic size adjustment
    JPanel wrapperPanel = new JPanel();
    wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
    wrapperPanel.add(technicalConfigPanel);

    JScrollPane scrollPane = new JScrollPane(wrapperPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    panel.add(scrollPane, BorderLayout.CENTER);

    // Load technical configuration asynchronously
    loadTechnicalConfig();

    return panel;
  }

  private JPanel createConfigPanel(String title, JTextArea textArea) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createEmptyBorder(5, 5, 5, 5),
      new TitledBorder(title)
    ));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    panel.add(scrollPane, BorderLayout.CENTER);
    return panel;
  }

  private void loadScenarios() {
    // Show a loading indicator
    scenarioComboBox.addItem(new Scenario("loading", "Loading scenarios..."));
    scenarioComboBox.setEnabled(false);

    // Load scenarios asynchronously
    scenarioService.loadScenariosAsync().thenAccept(scenarios -> {
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

  /**
   * Returns the currently selected scenario
   * 
   * @return The selected scenario or null
   */
  public Scenario getSelectedScenario() {
    return (Scenario) scenarioComboBox.getSelectedItem();
  }

  /**
   * Loads technical configuration and builds dynamic UI
   */
  private void loadTechnicalConfig() {
    // Clear existing components
    technicalConfigPanel.removeAll();
    technicalConfigComponents.clear();

    // Add loading indicator
    JLabel loadingLabel = new JLabel("Loading technical configuration...");
    loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(2, 2, 2, 2);
    technicalConfigPanel.add(loadingLabel, gbc);
    technicalConfigPanel.revalidate();
    technicalConfigPanel.repaint();

    // Load configuration asynchronously
    technicalConfigService.loadTechnicalConfigAsync().thenAccept(configItems -> {
      SwingUtilities.invokeLater(() -> {
        buildTechnicalConfigUI(configItems);
      });
    }).exceptionally(throwable -> {
      SwingUtilities.invokeLater(() -> {
        technicalConfigPanel.removeAll();
        JLabel errorLabel = new JLabel("Error loading technical configuration");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.gridwidth = 2;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(2, 2, 2, 2);
        technicalConfigPanel.add(errorLabel, gbc2);
        technicalConfigPanel.revalidate();
        technicalConfigPanel.repaint();
      });
      return null;
    });
  }

  /**
   * Builds the dynamic UI based on technical configuration items
   */
  private void buildTechnicalConfigUI(List<ConfigItem> configItems) {
    technicalConfigPanel.removeAll();
    technicalConfigComponents.clear();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.anchor = GridBagConstraints.WEST;

    int row = 0;
    for (ConfigItem item : configItems) {
      // Add label
      JLabel label = new JLabel(item.getDescription() + ":");
      gbc.gridx = 0;
      gbc.gridy = row;
      gbc.gridwidth = 1;
      gbc.fill = GridBagConstraints.NONE;
      gbc.weightx = 0.0;
      technicalConfigPanel.add(label, gbc);

      // Add component based on type
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

    // Add spacer at the bottom
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

  /**
   * Creates appropriate UI component based on the item type
   */
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
        // For unknown types, use a text field
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

    // Create wrapper panel with BoxLayout for automatic size adjustment
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

  /**
   * Event handler for scenario selection changes
   */
  private void onScenarioSelectionChanged() {
    Scenario selectedScenario = getSelectedScenario();
    if (selectedScenario != null && !selectedScenario.getScenarioId().equals("loading") && !selectedScenario.getScenarioId().equals("error")) {
      loadOperationalConfig(selectedScenario.getScenarioId());
    }
  }

  /**
   * Loads operational configuration for the given scenario
   */
  private void loadOperationalConfig(String scenarioId) {
    // Clear existing components
    operationalConfigPanel.removeAll();
    operationalConfigComponents.clear();

    // Add loading indicator
    JLabel loadingLabel = new JLabel("Loading operational configuration...");
    loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(2, 2, 2, 2);
    operationalConfigPanel.add(loadingLabel, gbc);
    operationalConfigPanel.revalidate();
    operationalConfigPanel.repaint();

    // Load configuration asynchronously
    operationalConfigService.loadOperationalConfigAsync(scenarioId).thenAccept(configItems -> {
      SwingUtilities.invokeLater(() -> {
        buildOperationalConfigUI(configItems);
      });
    }).exceptionally(throwable -> {
      SwingUtilities.invokeLater(() -> {
        operationalConfigPanel.removeAll();
        JLabel errorLabel = new JLabel("Error loading operational configuration");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.gridwidth = 2;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(2, 2, 2, 2);
        operationalConfigPanel.add(errorLabel, gbc2);
        operationalConfigPanel.revalidate();
        operationalConfigPanel.repaint();
      });
      return null;
    });
  }

  /**
   * Builds the dynamic UI for operational configuration
   */
  private void buildOperationalConfigUI(List<ConfigItem> configItems) {
    operationalConfigPanel.removeAll();
    operationalConfigComponents.clear();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.anchor = GridBagConstraints.WEST;

    int row = 0;
    for (ConfigItem item : configItems) {
      // Add label
      JLabel label = new JLabel(item.getDescription() + ":");
      gbc.gridx = 0;
      gbc.gridy = row;
      gbc.gridwidth = 1;
      gbc.fill = GridBagConstraints.NONE;
      gbc.weightx = 0.0;
      operationalConfigPanel.add(label, gbc);

      // Add component based on type
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

    // Add spacer at the bottom
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

  /**
   * Event handler for the Create Scenario button
   */
  private void onCreateScenarioClicked() {
    // TODO: Implementation for creating new scenarios
    JOptionPane.showMessageDialog(
      this,
      "Create Scenario functionality will be implemented in a future version.",
      "Create Scenario",
      JOptionPane.INFORMATION_MESSAGE
    );
  }

  /**
   * Cleanup method for resources
   */
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