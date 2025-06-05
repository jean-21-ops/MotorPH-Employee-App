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
        int animationDuration = 300; // milliseconds
        int animationSteps = 20;
        int stepDelay = animationDuration / animationSteps;
        
        // Get current and target colors
        Color currentBg = isDarkMode ? LIGHT_BACKGROUND : DARK_BACKGROUND;
        Color targetBg = isDarkMode ? DARK_BACKGROUND : LIGHT_BACKGROUND;
        
        // Create animation timer
        Timer animationTimer = new Timer(stepDelay, null);
        final int[] step = {0};
        
        animationTimer.addActionListener(e -> {
            step[0]++;
            float progress = (float) step[0] / animationSteps;
            
            // Apply colors to components
            applyThemeToComponent(rootComponent, progress);
            
            if (step[0] >= animationSteps) {
                animationTimer.stop();
                // Final application to ensure all components are updated
                applyTheme(rootComponent);
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
    
    private static void applyThemeToComponent(Component component, float progress) {
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            Color currentBg = isDarkMode ? LIGHT_BACKGROUND : DARK_BACKGROUND;
            Color targetBg = isDarkMode ? DARK_BACKGROUND : LIGHT_BACKGROUND;
            panel.setBackground(interpolateColor(currentBg, targetBg, progress));
        }
        
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                applyThemeToComponent(child, progress);
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
            // Check if it's a header panel (blue background)
            if (panel.getBackground().equals(new Color(0, 102, 204)) || 
                panel.getBackground().equals(new Color(0, 123, 255))) {
                panel.setBackground(getAccentColor());
            } else {
                panel.setBackground(getBackgroundColor());
            }
        }
        
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            // Don't change white text on blue headers
            if (label.getForeground().equals(Color.WHITE)) {
                label.setForeground(Color.WHITE);
            } else {
                label.setForeground(getForegroundColor());
            }
        }
        
        if (component instanceof JTextField) {
            JTextField field = (JTextField) component;
            field.setBackground(getBackgroundColor());
            field.setForeground(getForegroundColor());
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getAccentColor()),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        }
        
        if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setBackground(getBackgroundColor());
            table.setForeground(getForegroundColor());
            table.setGridColor(getBorderColor());
            table.getTableHeader().setBackground(getAccentColor());
            table.getTableHeader().setForeground(Color.WHITE);
        }
        
        if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            scrollPane.getViewport().setBackground(getBackgroundColor());
            scrollPane.setBackground(getBackgroundColor());
        }
        
        if (component instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) component;
            comboBox.setBackground(getBackgroundColor());
            comboBox.setForeground(getForegroundColor());
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
}