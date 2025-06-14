package Term2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ThemeManager {
    // Light theme colors
    public static final Color LIGHT_BACKGROUND = Color.WHITE;
    public static final Color LIGHT_FOREGROUND = Color.BLACK;
    public static final Color LIGHT_PANEL = new Color(248, 249, 250);
    public static final Color LIGHT_ACCENT = new Color(0, 102, 204);
    public static final Color LIGHT_BORDER = new Color(220, 220, 220);
    public static final Color LIGHT_BUTTON_BG = new Color(0, 102, 204);
    public static final Color LIGHT_BUTTON_FG = Color.WHITE;
    
    // Dark theme colors
    public static final Color DARK_BACKGROUND = new Color(33, 37, 41);
    public static final Color DARK_FOREGROUND = new Color(248, 249, 250);
    public static final Color DARK_PANEL = new Color(52, 58, 64);
    public static final Color DARK_ACCENT = new Color(0, 123, 255);
    public static final Color DARK_BORDER = new Color(73, 80, 87);
    public static final Color DARK_BUTTON_BG = new Color(0, 123, 255);
    public static final Color DARK_BUTTON_FG = Color.WHITE;
    
    private static boolean isDarkMode = false;
    private static final List<Component> registeredComponents = new ArrayList<>();
    
    public static boolean isDarkMode() {
        return isDarkMode;
    }
    
    public static void toggleTheme(Component rootComponent) {
        isDarkMode = !isDarkMode;
        animateThemeChange(rootComponent);
    }
    
    public static void registerComponent(Component component) {
        if (!registeredComponents.contains(component)) {
            registeredComponents.add(component);
        }
    }
    
    private static void animateThemeChange(Component rootComponent) {
        // Animation parameters
        int animationDuration = 200; // milliseconds - shorter for smoother feel
        int animationSteps = 15;
        int stepDelay = animationDuration / animationSteps;
        
        // Get current and target colors - Fixed the direction
        Color currentBg = isDarkMode ? LIGHT_BACKGROUND : DARK_BACKGROUND;
        Color targetBg = isDarkMode ? DARK_BACKGROUND : LIGHT_BACKGROUND;
        
        // Create animation timer
        Timer animationTimer = new Timer(stepDelay, null);
        final int[] step = {0};
        
        animationTimer.addActionListener(e -> {
            step[0]++;
            float progress = (float) step[0] / animationSteps;
            
            // Apply animated colors to components
            applyAnimatedTheme(rootComponent, currentBg, targetBg, progress);
            
            if (step[0] >= animationSteps) {
                animationTimer.stop();
                // Final application to ensure all components are updated
                applyTheme(rootComponent);
                // Force a complete repaint
                if (rootComponent instanceof JFrame) {
                    ((JFrame) rootComponent).revalidate();
                }
                rootComponent.repaint();
            }
        });
        
        animationTimer.start();
    }
    
    private static Color interpolateColor(Color start, Color end, float progress) {
        progress = Math.max(0, Math.min(1, progress)); // Clamp between 0 and 1
        
        int r = (int) (start.getRed() + (end.getRed() - start.getRed()) * progress);
        int g = (int) (start.getGreen() + (end.getGreen() - start.getGreen()) * progress);
        int b = (int) (start.getBlue() + (end.getBlue() - start.getBlue()) * progress);
        
        return new Color(r, g, b);
    }
    
    private static void applyAnimatedTheme(Component component, Color currentBg, Color targetBg, float progress) {
        Color currentFg = isDarkMode ? LIGHT_FOREGROUND : DARK_FOREGROUND;
        Color targetFg = isDarkMode ? DARK_FOREGROUND : LIGHT_FOREGROUND;
        Color currentPanel = isDarkMode ? LIGHT_PANEL : DARK_PANEL;
        Color targetPanel = isDarkMode ? DARK_PANEL : LIGHT_PANEL;
        
        if (component instanceof JFrame) {
            JFrame frame = (JFrame) component;
            frame.getContentPane().setBackground(interpolateColor(currentBg, targetBg, progress));
        }
        
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            // Check if it's a header/accent panel
            Color panelBg = panel.getBackground();
            if (panelBg != null && (
                panelBg.equals(new Color(0, 102, 204)) || 
                panelBg.equals(new Color(0, 123, 255)) ||
                panelBg.equals(LIGHT_ACCENT) ||
                panelBg.equals(DARK_ACCENT))) {
                // Keep accent color during animation
                panel.setBackground(isDarkMode ? DARK_ACCENT : LIGHT_ACCENT);
            } else {
                panel.setBackground(interpolateColor(currentBg, targetBg, progress));
            }
        }
        
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            // Check if this label is on an accent-colored background
            Container parent = label.getParent();
            boolean isOnAccentBackground = false;
            
            if (parent != null) {
                Color parentBg = parent.getBackground();
                if (parentBg != null && (
                    parentBg.equals(isDarkMode ? DARK_ACCENT : LIGHT_ACCENT) ||
                    parentBg.equals(LIGHT_ACCENT) ||
                    parentBg.equals(DARK_ACCENT) ||
                    parentBg.equals(new Color(0, 102, 204)) ||
                    parentBg.equals(new Color(0, 123, 255)))) {
                    isOnAccentBackground = true;
                }
            }
            
            // Keep white text on accent backgrounds, otherwise animate color
            if (isOnAccentBackground) {
                label.setForeground(Color.WHITE);
            } else {
                label.setForeground(interpolateColor(currentFg, targetFg, progress));
            }
        }
        
        if (component instanceof JTextField) {
            JTextField field = (JTextField) component;
            field.setBackground(interpolateColor(currentBg, targetBg, progress));
            field.setForeground(interpolateColor(currentFg, targetFg, progress));
        }
        
        if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setBackground(interpolateColor(currentBg, targetBg, progress));
            table.setForeground(interpolateColor(currentFg, targetFg, progress));
        }
        
        if (component instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) component;
            comboBox.setBackground(interpolateColor(currentBg, targetBg, progress));
            comboBox.setForeground(interpolateColor(currentFg, targetFg, progress));
        }
        
        // Handle other text components during animation
        if (component instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) component;
            checkBox.setForeground(interpolateColor(currentFg, targetFg, progress));
            checkBox.setBackground(interpolateColor(currentBg, targetBg, progress));
        }
        
        if (component instanceof JRadioButton) {
            JRadioButton radioButton = (JRadioButton) component;
            radioButton.setForeground(interpolateColor(currentFg, targetFg, progress));
            radioButton.setBackground(interpolateColor(currentBg, targetBg, progress));
        }
        
        if (component instanceof JTextArea) {
            JTextArea textArea = (JTextArea) component;
            textArea.setBackground(interpolateColor(currentBg, targetBg, progress));
            textArea.setForeground(interpolateColor(currentFg, targetFg, progress));
        }
        
        if (component instanceof JPasswordField) {
            JPasswordField passwordField = (JPasswordField) component;
            passwordField.setBackground(interpolateColor(currentBg, targetBg, progress));
            passwordField.setForeground(interpolateColor(currentFg, targetFg, progress));
        }
        
        // Handle titled borders
        if (component instanceof JComponent) {
            JComponent jComponent = (JComponent) component;
            if (jComponent.getBorder() instanceof javax.swing.border.TitledBorder) {
                javax.swing.border.TitledBorder titledBorder = 
                    (javax.swing.border.TitledBorder) jComponent.getBorder();
                titledBorder.setTitleColor(getForegroundColor());
            }
        }
        
        // Recursively apply to children
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                applyAnimatedTheme(child, currentBg, targetBg, progress);
            }
        }
        
        component.repaint();
    }
    
    public static void applyTheme(Component component) {
        if (component instanceof JFrame) {
            JFrame frame = (JFrame) component;
            frame.getContentPane().setBackground(getBackgroundColor());
        }
        
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            Color currentBg = panel.getBackground();
            // Check if it's a header panel (accent color panels)
            if (currentBg != null && (
                currentBg.equals(new Color(0, 102, 204)) || 
                currentBg.equals(new Color(0, 123, 255)) ||
                currentBg.equals(LIGHT_ACCENT) ||
                currentBg.equals(DARK_ACCENT))) {
                panel.setBackground(getAccentColor());
            } else {
                panel.setBackground(getBackgroundColor());
            }
        }
        
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            // Check if this label is on an accent-colored background (header)
            Container parent = label.getParent();
            boolean isOnAccentBackground = false;
            
            if (parent != null) {
                Color parentBg = parent.getBackground();
                if (parentBg != null && (
                    parentBg.equals(getAccentColor()) ||
                    parentBg.equals(LIGHT_ACCENT) ||
                    parentBg.equals(DARK_ACCENT) ||
                    parentBg.equals(new Color(0, 102, 204)) ||
                    parentBg.equals(new Color(0, 123, 255)))) {
                    isOnAccentBackground = true;
                }
            }
            
            // Keep white text on accent backgrounds, otherwise use theme foreground
            if (isOnAccentBackground) {
                label.setForeground(Color.WHITE);
            } else {
                label.setForeground(getForegroundColor());
            }
        }
        
        if (component instanceof JTextField) {
            JTextField field = (JTextField) component;
            field.setBackground(getBackgroundColor());
            field.setForeground(getForegroundColor());
            field.setCaretColor(getForegroundColor()); // Fix cursor color
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getAccentColor()),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        }
        
        if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setBackground(getBackgroundColor());
            table.setForeground(getForegroundColor());
            table.setGridColor(getBorderColor());
            table.setSelectionBackground(getAccentColor());
            table.setSelectionForeground(Color.WHITE);
            if (table.getTableHeader() != null) {
                table.getTableHeader().setBackground(getAccentColor());
                table.getTableHeader().setForeground(Color.WHITE);
            }
        }
        
        if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            scrollPane.getViewport().setBackground(getBackgroundColor());
            scrollPane.setBackground(getBackgroundColor());
            // Style the scrollbars
            scrollPane.getVerticalScrollBar().setBackground(getPanelColor());
            scrollPane.getHorizontalScrollBar().setBackground(getPanelColor());
        }
        
        if (component instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) component;
            comboBox.setBackground(getBackgroundColor());
            comboBox.setForeground(getForegroundColor());
            // Make combo box popup match theme
            if (comboBox.getUI() instanceof javax.swing.plaf.basic.BasicComboBoxUI) {
                comboBox.setBorder(BorderFactory.createLineBorder(getAccentColor()));
            }
        }
        
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            // Only style if it's not already styled (to avoid overriding custom styles)
            if (button.getBackground().equals(javax.swing.UIManager.getColor("Button.background")) ||
                button.getBackground().equals(getButtonBackgroundColor()) ||
                button.getBackground().equals(LIGHT_BUTTON_BG) ||
                button.getBackground().equals(DARK_BUTTON_BG)) {
                button.setBackground(getButtonBackgroundColor());
                button.setForeground(getButtonForegroundColor());
            }
        }
        
        // Recursively apply to child components
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                applyTheme(child);
            }
        }
        
        component.repaint();
    }
    
    /**
     * Apply theme to a newly created component
     * Use this when creating new UI components to ensure they match the current theme
     */
    public static void applyThemeToNewComponent(Component component) {
        applyTheme(component);
    }
    
    /**
     * Force refresh of all components in a container
     * Use this after theme changes to ensure all components are updated
     */
    public static void refreshContainer(Container container) {
        applyTheme(container);
        container.revalidate();
        container.repaint();
        
        // Recursively refresh child containers
        for (Component child : container.getComponents()) {
            if (child instanceof Container) {
                refreshContainer((Container) child);
            }
        }
    }
    
    /**
     * Set application to light mode
     */
    public static void setLightMode() {
        if (isDarkMode) {
            isDarkMode = false;
        }
    }
    
    /**
     * Set application to dark mode  
     */
    public static void setDarkMode() {
        if (!isDarkMode) {
            isDarkMode = true;
        }
    }
    
    /**
     * Get text color that contrasts well with current background
     */
    public static Color getContrastTextColor() {
        return isDarkMode ? DARK_FOREGROUND : LIGHT_FOREGROUND;
    }
    
    public static Color getBackgroundColor() {
        return isDarkMode ? DARK_BACKGROUND : LIGHT_BACKGROUND;
    }
    
    public static Color getForegroundColor() {
        return isDarkMode ? DARK_FOREGROUND : LIGHT_FOREGROUND;
    }
    
    public static Color getPanelColor() {
        return isDarkMode ? DARK_PANEL : LIGHT_PANEL;
    }
    
    public static Color getAccentColor() {
        return isDarkMode ? DARK_ACCENT : LIGHT_ACCENT;
    }
    
    public static Color getBorderColor() {
        return isDarkMode ? DARK_BORDER : LIGHT_BORDER;
    }
    
    public static Color getButtonBackgroundColor() {
        return isDarkMode ? DARK_BUTTON_BG : LIGHT_BUTTON_BG;
    }
    
    public static Color getButtonForegroundColor() {
        return isDarkMode ? DARK_BUTTON_FG : LIGHT_BUTTON_FG;
    }
    
    /**
     * Force update of all text colors in a component tree
     * Use this specifically when text colors aren't updating properly
     */
    public static void forceTextColorUpdate(Component component) {
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            // Check if parent has accent background
            Container parent = label.getParent();
            boolean isOnAccentBackground = false;
            
            if (parent != null) {
                Color parentBg = parent.getBackground();
                if (parentBg != null && (
                    parentBg.equals(getAccentColor()) ||
                    parentBg.equals(LIGHT_ACCENT) ||
                    parentBg.equals(DARK_ACCENT))) {
                    isOnAccentBackground = true;
                }
            }
            
            // Force color update
            label.setForeground(isOnAccentBackground ? Color.WHITE : getForegroundColor());
        }
        
        // Force update for all text components
        if (component instanceof JTextField) {
            ((JTextField) component).setForeground(getForegroundColor());
        }
        if (component instanceof JTextArea) {
            ((JTextArea) component).setForeground(getForegroundColor());
        }
        if (component instanceof JCheckBox) {
            ((JCheckBox) component).setForeground(getForegroundColor());
        }
        if (component instanceof JRadioButton) {
            ((JRadioButton) component).setForeground(getForegroundColor());
        }
        
        // Recursively update children
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                forceTextColorUpdate(child);
            }
        }
        
        component.repaint();
    }
}