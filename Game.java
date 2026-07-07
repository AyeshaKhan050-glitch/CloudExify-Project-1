/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.game;

/**
 *
 * @author Ayesha
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Number Guessing Game - GUI Version (Java Swing)
 * CloudExify Java Month 1 - Project 1
 *
 * Demonstrates: Classes, Objects, Constructor, Methods, Variables,
 * Random, If-Else, Loops, Exception Handling,
 * File Handling, Object Creation, Encapsulation, Event Handling.
 */
public class Game extends JFrame {

    // ---------- Colors (colorful theme) ----------
    private static final Color BG_COLOR = new Color(198, 226, 255);
    private static final Color CARD_COLOR = new Color(255, 250, 220);
    private static final Color FIELD_COLOR = new Color(255, 244, 79);
    private static final Color PRIMARY_BLUE = new Color(41, 98, 173);
    private static final Color SUCCESS_GREEN = new Color(30, 130, 76);
    private static final Color ERROR_RED = new Color(200, 60, 60);
    private static final Color TEXT_DARK = new Color(50, 50, 60);
    private static final Color NEON_BLUE = new Color(0, 180, 255);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FIELD_FONT = new Font("SansSerif", Font.BOLD, 20);

    // ---------- Custom panel that paints a soft diffused glow background with floating numbers and space decorations ----------
    private static class GradientPanel extends JPanel {
        private final java.util.List<NumberSpec> numbers = new java.util.ArrayList<>();
        private final boolean showDecorations;

        GradientPanel() {
            this(false);
        }

        GradientPanel(boolean showDecorations) {
            this.showDecorations = showDecorations;
            Random rnd = new Random();
            for (int i = 0; i < 26; i++) {
                int digit = rnd.nextInt(10);
                float xFrac = rnd.nextFloat();
                float yFrac = rnd.nextFloat();
                int size = 35 + rnd.nextInt(95);   // 35 - 130 px
                int alpha = 25 + rnd.nextInt(60);  // soft translucency
                numbers.add(new NumberSpec(digit, xFrac, yFrac, size, alpha));
            }
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            // Base darker purple canvas
            g2d.setColor(new Color(65, 42, 98));
            g2d.fillRect(0, 0, w, h);

            // Overlapping soft glow blobs create the diffused/cloudy look
            paintGlow(g2d, w, h, w * 0.75f, h * 0.25f, Math.max(w, h) * 0.65f, new Color(160, 135, 185));
            paintGlow(g2d, w, h, w * 0.15f, h * 0.75f, Math.max(w, h) * 0.55f, new Color(130, 100, 160));
            paintGlow(g2d, w, h, w * 0.5f, h * 0.5f, Math.max(w, h) * 0.4f, new Color(175, 155, 200));
            paintGlow(g2d, w, h, w * 0.85f, h * 0.85f, Math.max(w, h) * 0.35f, new Color(85, 58, 118));

            // Floating translucent numbers scattered across the background
            for (NumberSpec n : numbers) {
                g2d.setFont(new Font("Serif", Font.BOLD, n.size));
                g2d.setColor(new Color(255, 255, 255, n.alpha));
                int x = (int) (n.xFrac * w);
                int y = (int) (n.yFrac * h);
                g2d.drawString(String.valueOf(n.digit), x, y);
            }

            if (showDecorations) {
                drawAlien(g2d, (int) (w * 0.18f), (int) (h * 0.16f), 130);
                drawSunBurst(g2d, (int) (w * 0.82f), (int) (h * 0.14f), 65, 30, 10);
                drawMoon(g2d, (int) (w * 0.5f), (int) (h * 0.1f), 45);
                drawPlanet(g2d, (int) (w * 0.9f), (int) (h * 0.6f), 55);
                drawRocket(g2d, (int) (w * 0.08f), (int) (h * 0.7f), 90);
                drawSparkle(g2d, (int) (w * 0.06f), (int) (h * 0.45f), 20, Color.WHITE);
                drawSparkle(g2d, (int) (w * 0.93f), (int) (h * 0.4f), 26, Color.WHITE);
                drawSparkle(g2d, (int) (w * 0.35f), (int) (h * 0.08f), 18, Color.WHITE);
                drawSparkle(g2d, (int) (w * 0.7f), (int) (h * 0.55f), 24, Color.WHITE);
                drawSparkle(g2d, (int) (w * 0.1f), (int) (h * 0.85f), 22, Color.WHITE);
                drawSparkle(g2d, (int) (w * 0.9f), (int) (h * 0.85f), 20, Color.WHITE);
                drawSparkle(g2d, (int) (w * 0.45f), (int) (h * 0.9f), 16, Color.WHITE);
                drawSparkle(g2d, (int) (w * 0.25f), (int) (h * 0.35f), 14, Color.WHITE);
                drawSparkle(g2d, (int) (w * 0.6f), (int) (h * 0.15f), 15, Color.WHITE);
            }
        }

        private void paintGlow(Graphics2D g2d, int w, int h, float cx, float cy, float radius, Color color) {
            if (w <= 0 || h <= 0) return;
            radius = Math.max(radius, 1f);
            Color transparent = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
            Color solid = new Color(color.getRed(), color.getGreen(), color.getBlue(), 190);
            RadialGradientPaint paint = new RadialGradientPaint(
                    new Point2D.Float(cx, cy), radius,
                    new float[]{0f, 1f},
                    new Color[]{solid, transparent});
            g2d.setPaint(paint);
            g2d.fillRect(0, 0, w, h);
        }

        // ---------- Draws a friendly cartoon alien sitting in a UFO ----------
        private void drawAlien(Graphics2D g2d, int cx, int cy, int size) {
            // UFO saucer body
            g2d.setColor(new Color(90, 80, 110));
            g2d.fillOval(cx - size, cy + size / 3, size * 2, size / 2);
            g2d.setColor(new Color(190, 170, 210));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(cx - size, cy + size / 3, size * 2, size / 2);

            // Saucer dome
            g2d.setColor(new Color(160, 220, 235, 200));
            g2d.fillOval(cx - size / 2, cy + size / 8, size, size / 2);

            // Little lights along the saucer rim
            g2d.setColor(new Color(255, 230, 120));
            int lightSize = size / 10;
            for (int i = -3; i <= 3; i++) {
                int lx = cx + i * (size / 4) - lightSize / 2;
                int ly = cy + size / 2 + size / 6;
                g2d.fillOval(lx, ly, lightSize, lightSize);
            }

            // Alien head
            g2d.setColor(new Color(120, 210, 130));
            g2d.fillOval(cx - size / 3, cy - size / 3, (int) (size * 0.66), (int) (size * 0.6));

            // Alien eyes (big black almond eyes)
            g2d.setColor(Color.BLACK);
            g2d.fillOval(cx - size / 4, cy - size / 6, size / 6, size / 4);
            g2d.fillOval(cx + size / 24, cy - size / 6, size / 6, size / 4);

            // Eye shine
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillOval(cx - size / 5, cy - size / 8, size / 20, size / 20);
            g2d.fillOval(cx + size / 12, cy - size / 8, size / 20, size / 20);

            // Antennae
            g2d.setColor(new Color(120, 210, 130));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(cx - size / 8, cy - size / 3, cx - size / 6, cy - size / 2);
            g2d.fillOval(cx - size / 6 - 4, cy - size / 2 - 4, 8, 8);
        }

        // ---------- Draws a bright sunburst / explosion star shape ----------
        private void drawSunBurst(Graphics2D g2d, int cx, int cy, int outerR, int innerR, int points) {
            Polygon star = new Polygon();
            for (int i = 0; i < points * 2; i++) {
                double angle = Math.PI * i / points;
                int r = (i % 2 == 0) ? outerR : innerR;
                int x = cx + (int) (r * Math.sin(angle));
                int y = cy - (int) (r * Math.cos(angle));
                star.addPoint(x, y);
            }
            g2d.setColor(new Color(255, 210, 80));
            g2d.fillPolygon(star);
            g2d.setColor(new Color(255, 140, 40));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawPolygon(star);

            g2d.setColor(new Color(255, 245, 200));
            g2d.fillOval(cx - innerR, cy - innerR, innerR * 2, innerR * 2);
        }

        // ---------- Draws a soft crescent moon ----------
        private void drawMoon(Graphics2D g2d, int cx, int cy, int radius) {
            g2d.setColor(new Color(240, 240, 225));
            g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
            // Cut a crescent by painting an offset circle in the base background color
            g2d.setColor(new Color(65, 42, 98));
            g2d.fillOval(cx - radius + radius / 2, cy - radius - radius / 4, radius * 2, radius * 2);
        }

        // ---------- Draws a ringed planet (Saturn-style) ----------
        private void drawPlanet(Graphics2D g2d, int cx, int cy, int radius) {
            g2d.setStroke(new BasicStroke(4));

            // Ring behind the planet
            g2d.setColor(new Color(230, 200, 140, 180));
            g2d.drawOval(cx - radius * 2, cy - radius / 3, radius * 4, radius * 2 / 3);

            // Planet body
            g2d.setColor(new Color(220, 140, 90));
            g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
            g2d.setColor(new Color(190, 110, 70));
            g2d.fillOval(cx - radius, cy - radius / 4, radius * 2, radius);

            // Ring in front of the planet
            g2d.setColor(new Color(250, 220, 160, 200));
            g2d.drawOval(cx - radius * 2, cy + radius / 6, radius * 4, radius * 2 / 3);
        }

        // ---------- Draws a small cartoon rocket ----------
        private void drawRocket(Graphics2D g2d, int cx, int cy, int size) {
            // Body
            g2d.setColor(new Color(235, 235, 240));
            g2d.fillRoundRect(cx - size / 6, cy - size / 2, size / 3, size, size / 6, size / 6);

            // Nose cone
            Polygon nose = new Polygon();
            nose.addPoint(cx, cy - size / 2 - size / 4);
            nose.addPoint(cx - size / 6, cy - size / 2);
            nose.addPoint(cx + size / 6, cy - size / 2);
            g2d.setColor(new Color(220, 70, 70));
            g2d.fillPolygon(nose);

            // Window
            g2d.setColor(new Color(120, 190, 230));
            int windowSize = size / 6;
            g2d.fillOval(cx - windowSize / 2, cy - size / 6, windowSize, windowSize);

            // Fins
            g2d.setColor(new Color(220, 70, 70));
            Polygon leftFin = new Polygon();
            leftFin.addPoint(cx - size / 6, cy + size / 3);
            leftFin.addPoint(cx - size / 6 - size / 5, cy + size / 2);
            leftFin.addPoint(cx - size / 6, cy + size / 2);
            g2d.fillPolygon(leftFin);

            Polygon rightFin = new Polygon();
            rightFin.addPoint(cx + size / 6, cy + size / 3);
            rightFin.addPoint(cx + size / 6 + size / 5, cy + size / 2);
            rightFin.addPoint(cx + size / 6, cy + size / 2);
            g2d.fillPolygon(rightFin);

            // Flame
            Polygon flame = new Polygon();
            flame.addPoint(cx - size / 10, cy + size / 2);
            flame.addPoint(cx, cy + size / 2 + size / 4);
            flame.addPoint(cx + size / 10, cy + size / 2);
            g2d.setColor(new Color(255, 170, 50));
            g2d.fillPolygon(flame);
        }

        // ---------- Draws a small 4-point sparkle star ----------
        private void drawSparkle(Graphics2D g2d, int cx, int cy, int size, Color color) {
            Polygon star = new Polygon();
            star.addPoint(cx, cy - size);
            star.addPoint(cx + size / 4, cy - size / 4);
            star.addPoint(cx + size, cy);
            star.addPoint(cx + size / 4, cy + size / 4);
            star.addPoint(cx, cy + size);
            star.addPoint(cx - size / 4, cy + size / 4);
            star.addPoint(cx - size, cy);
            star.addPoint(cx - size / 4, cy - size / 4);
            g2d.setColor(color);
            g2d.fillPolygon(star);
        }

        private static class NumberSpec {
            final int digit, size, alpha;
            final float xFrac, yFrac;

            NumberSpec(int digit, float xFrac, float yFrac, int size, int alpha) {
                this.digit = digit;
                this.xFrac = xFrac;
                this.yFrac = yFrac;
                this.size = size;
                this.alpha = alpha;
            }
        }
    }

    // ---------- File names ----------
    private static final String BEST_SCORE_FILE = "bestscore.txt";
    private static final String PLAYERS_FILE = "players.txt";

    // ---------- Game state fields (Encapsulation) ----------
    private Random random;
    private String playerName;
    private int playerAge;
    private String level;
    private int rangeMax;
    private int secretNumber;
    private int attempts;
    private int maxAttempts;
    private int currentScore;
    private int bestScore;

    // ---------- UI Components ----------
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JTextField nameField;
    private JTextField ageField;
    private JLabel startErrorLabel;
    private JLabel newGameSubtitleLabel;

    private JLabel playerInfoLabel;
    private JLabel levelInfoLabel;
    private JLabel rangeInfoLabel;
    private JLabel bestScoreInfoLabel;
    private JLabel attemptsInfoLabel;
    private JTextField guessField;
    private JLabel feedbackLabel;
    private JButton guessButton;
    private JButton saveToPcButton;

    private JLabel resultTitleLabel;
    private JLabel resultDetailsLabel;
    private JLabel motivationLabel;
    private JLabel playAgainPromptLabel;
    private JButton yesButton;
    private JButton noButton;

    private static final String[] WIN_MESSAGES = {
            "Amazing! You have sharp instincts!",
            "Fantastic guessing skills!",
            "You nailed it perfectly!",
            "Brilliant! Keep it up!",
            "Great job, you're a natural!"
    };
    private static final String[] LOSE_MESSAGES = {
            "Better Luck Next Time!",
            "Don't give up, try again!",
            "Practice makes perfect!",
            "So close! Give it another shot!",
            "Every attempt makes you sharper!"
    };

    // ---------- Constructor ----------
    public Game() {
        random = new Random();
        maxAttempts = 10;
        bestScore = loadBestScore();
        initUI();
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.setVisible(true);
        });
    }

    // ---------- Build the whole window ----------
    private void initUI() {
        setTitle("Number Guessing Game");
        setSize(480, 470);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG_COLOR);

        cardPanel.add(buildPlayerInfoPanel(), "PLAYER_INFO");
        cardPanel.add(buildGamePanel(), "GAME");
        cardPanel.add(buildResultPanel(), "RESULT");

        add(cardPanel);
        cardLayout.show(cardPanel, "PLAYER_INFO");
    }

    // ---------- Screen 1: Player Name & Age ----------
    private JPanel buildPlayerInfoPanel() {
        JPanel panel = new GradientPanel(true);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("🎯 NUMBER GUESSING GAME 🎯");
        title.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 24));
        title.setForeground(Color.BLACK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        newGameSubtitleLabel = new JLabel(" ");
        newGameSubtitleLabel.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 16));
        newGameSubtitleLabel.setForeground(NEON_BLUE);
        newGameSubtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel introLine1 = new JLabel("🎲 This is the Number Guessing Game! 🎮");
        introLine1.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 20));
        introLine1.setForeground(Color.BLACK);
        introLine1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel introLine2 = new JLabel("😄 Once you play it, I'm sure you'll want to play it again! 🔁");
        introLine2.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 20));
        introLine2.setForeground(Color.BLACK);
        introLine2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("👤 Player Name:");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 16));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(320, 45));
        nameField.setPreferredSize(new Dimension(320, 45));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setFont(FIELD_FONT);
        nameField.setBackground(FIELD_COLOR);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_BLUE, 2),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JLabel ageLabel = new JLabel("🎂 Player Age:");
        ageLabel.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 16));
        ageLabel.setForeground(Color.BLACK);
        ageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ageField = new JTextField();
        ageField.setMaximumSize(new Dimension(320, 45));
        ageField.setPreferredSize(new Dimension(320, 45));
        ageField.setAlignmentX(Component.CENTER_ALIGNMENT);
        ageField.setFont(FIELD_FONT);
        ageField.setBackground(FIELD_COLOR);
        ageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_BLUE, 2),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        startErrorLabel = new JLabel(" ");
        startErrorLabel.setForeground(ERROR_RED);
        startErrorLabel.setFont(LABEL_FONT);
        startErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("🚀 Let's Start Game");
        startButton.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 18));
        startButton.setBackground(NEON_BLUE);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(this::startGame);

        // Enter key moves Name -> Age -> Start
        nameField.addActionListener(e -> ageField.requestFocus());
        ageField.addActionListener(this::startGame);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(newGameSubtitleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(introLine1);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(introLine2);
        panel.add(Box.createRigidArea(new Dimension(0, 22)));
        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(nameField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(ageField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(startErrorLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(startButton);

        return panel;
    }

    // ---------- Screen 2: The actual guessing game ----------
    private JPanel buildGamePanel() {
        JPanel panel = new GradientPanel(true);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel infoCard = new JPanel(new GridLayout(4, 1, 0, 4));
        infoCard.setBackground(CARD_COLOR);
        infoCard.setBorder(new EmptyBorder(12, 15, 12, 15));
        infoCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoCard.setMaximumSize(new Dimension(400, 110));

        playerInfoLabel = new JLabel();
        playerInfoLabel.setFont(BOLD_FONT);
        playerInfoLabel.setForeground(Color.BLACK);
        levelInfoLabel = new JLabel();
        levelInfoLabel.setFont(LABEL_FONT);
        levelInfoLabel.setForeground(Color.BLACK);
        rangeInfoLabel = new JLabel();
        rangeInfoLabel.setFont(LABEL_FONT);
        rangeInfoLabel.setForeground(Color.BLACK);
        bestScoreInfoLabel = new JLabel();
        bestScoreInfoLabel.setFont(LABEL_FONT);
        bestScoreInfoLabel.setForeground(Color.BLACK);

        infoCard.add(playerInfoLabel);
        infoCard.add(levelInfoLabel);
        infoCard.add(rangeInfoLabel);
        infoCard.add(bestScoreInfoLabel);

        attemptsInfoLabel = new JLabel();
        attemptsInfoLabel.setFont(BOLD_FONT);
        attemptsInfoLabel.setForeground(Color.BLACK);
        attemptsInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel guessPrompt = new JLabel("🔢 Enter Your Guess:");
        guessPrompt.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 16));
        guessPrompt.setForeground(Color.BLACK);
        guessPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        guessField = new JTextField();
        guessField.setMaximumSize(new Dimension(260, 50));
        guessField.setPreferredSize(new Dimension(260, 50));
        guessField.setAlignmentX(Component.CENTER_ALIGNMENT);
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setFont(new Font("SansSerif", Font.BOLD, 22));
        guessField.setBackground(FIELD_COLOR);
        guessField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_BLUE, 2),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        guessField.addActionListener(this::handleGuess);

        guessButton = new JButton("🎯 Guess");
        guessButton.setFont(BOLD_FONT);
        guessButton.setBackground(PRIMARY_BLUE);
        guessButton.setForeground(Color.WHITE);
        guessButton.setFocusPainted(false);
        guessButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        guessButton.addActionListener(this::handleGuess);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        saveToPcButton = new JButton("💾 Save to My PC");
        saveToPcButton.setFont(LABEL_FONT);
        saveToPcButton.setBackground(CARD_COLOR);
        saveToPcButton.setForeground(PRIMARY_BLUE);
        saveToPcButton.setFocusPainted(false);
        saveToPcButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveToPcButton.addActionListener(this::saveRecordToPc);

        panel.add(attemptsInfoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(guessPrompt);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(guessField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(guessButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(feedbackLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(saveToPcButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(infoCard);

        return panel;
    }

    // ---------- Screen 3: In-window result screen (win/lose + play again) ----------
    private JPanel buildResultPanel() {
        JPanel panel = new GradientPanel(true);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel centerBox = new JPanel();
        centerBox.setOpaque(false);
        centerBox.setLayout(new BoxLayout(centerBox, BoxLayout.Y_AXIS));

        resultTitleLabel = new JLabel(" ");
        resultTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        resultTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultDetailsLabel = new JLabel(" ");
        resultDetailsLabel.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 20));
        resultDetailsLabel.setForeground(Color.BLACK);
        resultDetailsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultDetailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        motivationLabel = new JLabel(" ");
        motivationLabel.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 20));
        motivationLabel.setForeground(new Color(142, 36, 170));
        motivationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playAgainPromptLabel = new JLabel("Do you want to play again?");
        playAgainPromptLabel.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 18));
        playAgainPromptLabel.setForeground(Color.BLACK);
        playAgainPromptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        yesButton = new JButton("✅ YES");
        yesButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        yesButton.setBackground(SUCCESS_GREEN);
        yesButton.setForeground(Color.WHITE);
        yesButton.setFocusPainted(false);
        yesButton.setPreferredSize(new Dimension(140, 55));
        yesButton.setMaximumSize(new Dimension(140, 55));
        yesButton.addActionListener(this::onPlayAgainYes);

        noButton = new JButton("❌ NO");
        noButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        noButton.setBackground(ERROR_RED);
        noButton.setForeground(Color.WHITE);
        noButton.setFocusPainted(false);
        noButton.setPreferredSize(new Dimension(140, 55));
        noButton.setMaximumSize(new Dimension(140, 55));
        noButton.addActionListener(this::onPlayAgainNo);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonRow.add(yesButton);
        buttonRow.add(noButton);

        centerBox.add(Box.createVerticalGlue());
        centerBox.add(resultTitleLabel);
        centerBox.add(Box.createRigidArea(new Dimension(0, 20)));
        centerBox.add(motivationLabel);
        centerBox.add(Box.createRigidArea(new Dimension(0, 35)));
        centerBox.add(playAgainPromptLabel);
        centerBox.add(Box.createRigidArea(new Dimension(0, 15)));
        centerBox.add(buttonRow);
        centerBox.add(Box.createVerticalGlue());

        JPanel bottomBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomBox.setOpaque(false);
        bottomBox.add(resultDetailsLabel);

        panel.add(centerBox, BorderLayout.CENTER);
        panel.add(bottomBox, BorderLayout.SOUTH);

        return panel;
    }

    // ---------- Yes: loop to player info screen for next player ----------
    private void onPlayAgainYes(ActionEvent e) {
        nameField.setText("");
        ageField.setText("");
        startErrorLabel.setText(" ");
        newGameSubtitleLabel.setText("New Game — Enter Next Player's Details");
        cardLayout.show(cardPanel, "PLAYER_INFO");
        nameField.requestFocus();
    }

    // ---------- No: say goodbye in the same window, then close ----------
    private void onPlayAgainNo(ActionEvent e) {
        resultTitleLabel.setForeground(NEON_BLUE);
        resultTitleLabel.setText("👋 Thank You For Playing!");
        resultDetailsLabel.setText(" ");
        motivationLabel.setText("See you again soon! 😊");
        playAgainPromptLabel.setText(" ");
        yesButton.setVisible(false);
        noButton.setVisible(false);

        Timer closeTimer = new Timer(1800, ev -> System.exit(0));
        closeTimer.setRepeats(false);
        closeTimer.start();
    }

    // ---------- Validate player info & begin the game ----------
    private void startGame(ActionEvent e) {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();

        if (name.isEmpty()) {
            startErrorLabel.setText("Invalid Input! Name cannot be empty.");
            return;
        }
        if (name.matches(".*\\d.*")) {
            startErrorLabel.setText("Invalid Input! Name should not contain numbers.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            startErrorLabel.setText("Invalid Input! Please enter numbers only.");
            return;
        }
        if (age <= 0 || age > 120) {
            startErrorLabel.setText("Invalid Input! Please enter a realistic age.");
            return;
        }

        playerName = name;
        playerAge = age;
        startErrorLabel.setText(" ");

        determineDifficulty();
        resetRound();
        updateGameInfoDisplay();
        cardLayout.show(cardPanel, "GAME");
        guessField.requestFocus();
    }

    // ---------- Select difficulty automatically based on age ----------
    private void determineDifficulty() {
        if (playerAge >= 7 && playerAge <= 12) {
            level = "Easy";
            rangeMax = 20;
        } else if (playerAge >= 13 && playerAge <= 16) {
            level = "Medium";
            rangeMax = 50;
        } else if (playerAge >= 17) {
            level = "Hard";
            rangeMax = 100;
        } else {
            level = "Easy";
            rangeMax = 20;
        }
    }

    // ---------- Reset variables for a new round (keeps best score) ----------
    private void resetRound() {
        secretNumber = random.nextInt(rangeMax) + 1;
        attempts = 0;
        currentScore = 0;
        feedbackLabel.setText(" ");
        guessField.setText("");
    }

    private void updateGameInfoDisplay() {
        playerInfoLabel.setText("Player: " + playerName + "   |   Age: " + playerAge);
        levelInfoLabel.setText("Level: " + level);
        rangeInfoLabel.setText("Range: 1 - " + rangeMax);
        bestScoreInfoLabel.setText("Best Score: " + (bestScore == Integer.MIN_VALUE ? "None yet" : bestScore));
        attemptsInfoLabel.setText("Attempts: " + attempts + " / " + maxAttempts);
    }

    // ---------- Handle a guess submission ----------
    private void handleGuess(ActionEvent e) {
        String text = guessField.getText().trim();
        int guess;

        try {
            guess = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            feedbackLabel.setForeground(ERROR_RED);
            feedbackLabel.setText("Invalid Input! Numbers only.");
            guessField.setText("");
            return;
        }

        if (guess < 1 || guess > rangeMax) {
            feedbackLabel.setForeground(ERROR_RED);
            feedbackLabel.setText("Enter a number within 1-" + rangeMax + "!");
            guessField.setText("");
            return;
        }

        attempts++;
        guessField.setText("");

        if (guess < secretNumber) {
            feedbackLabel.setForeground(ERROR_RED);
            feedbackLabel.setText("Too Low!");
            checkAttemptsLeft();
        } else if (guess > secretNumber) {
            feedbackLabel.setForeground(ERROR_RED);
            feedbackLabel.setText("Too High!");
            checkAttemptsLeft();
        } else {
            currentScore = calculateScore(attempts);
            feedbackLabel.setForeground(SUCCESS_GREEN);
            feedbackLabel.setText("You Win! Score: " + currentScore);
            endRound(true);
        }

        attemptsInfoLabel.setText("Attempts: " + attempts + " / " + maxAttempts);
    }

    // ---------- Check if attempts ran out ----------
    private void checkAttemptsLeft() {
        if (attempts >= maxAttempts) {
            currentScore = 0;
            endRound(false);
        }
    }

    // ---------- Scoring: fewer attempts + harder level = higher score ----------
    private int calculateScore(int attemptsUsed) {
        int difficultyBonus;
        if (level.equals("Hard")) {
            difficultyBonus = 3;
        } else if (level.equals("Medium")) {
            difficultyBonus = 2;
        } else {
            difficultyBonus = 1;
        }
        int base = (maxAttempts - attemptsUsed + 1) * 10;
        return Math.max(base * difficultyBonus, 0);
    }

    // ---------- Handle end of round: win or lose ----------
    private void endRound(boolean won) {
        boolean newBest = false;
        if (won && currentScore > bestScore) {
            bestScore = currentScore;
            saveBestScore(bestScore);
            newBest = true;
        }
        savePlayerRecord();
        bestScoreInfoLabel.setText("Best Score: " + bestScore);

        yesButton.setVisible(true);
        noButton.setVisible(true);
        playAgainPromptLabel.setText("Do you want to play again?");

        String randomMotivation;
        if (won) {
            resultTitleLabel.setForeground(SUCCESS_GREEN);
            resultTitleLabel.setText("🎉 YOU WIN! 🏆");
            resultDetailsLabel.setText("<html><div style='text-align:center;'>"
                    + "Player: " + playerName + "<br>"
                    + "Attempts Used: " + attempts + "<br>"
                    + "Score: " + currentScore + "<br>"
                    + "Best Score: " + bestScore
                    + (newBest ? "<br><b>New Best Score!</b>" : "")
                    + "</div></html>");
            randomMotivation = WIN_MESSAGES[random.nextInt(WIN_MESSAGES.length)];
        } else {
            resultTitleLabel.setForeground(ERROR_RED);
            resultTitleLabel.setText("😢 GAME OVER");
            resultDetailsLabel.setText("<html><div style='text-align:center;'>"
                    + "Correct Number: " + secretNumber + "<br>"
                    + "Player Score: " + currentScore + "<br>"
                    + "Best Score: " + bestScore
                    + "</div></html>");
            randomMotivation = LOSE_MESSAGES[random.nextInt(LOSE_MESSAGES.length)];
        }
        motivationLabel.setText(randomMotivation);

        cardLayout.show(cardPanel, "RESULT");
    }

    // ---------- Lets the player save their current game record anywhere on their PC ----------
    private void saveRecordToPc(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game Record");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
        fileChooser.setSelectedFile(new File(playerName + "_GameRecord.txt"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File chosenFile = fileChooser.getSelectedFile();
            if (!chosenFile.getName().toLowerCase().endsWith(".txt")) {
                chosenFile = new File(chosenFile.getAbsolutePath() + ".txt");
            }

            try (FileWriter writer = new FileWriter(chosenFile)) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                writer.write("===== NUMBER GUESSING GAME RECORD =====\r\n");
                writer.write("Player Name : " + playerName + "\r\n");
                writer.write("Age         : " + playerAge + "\r\n");
                writer.write("Level       : " + level + "\r\n");
                writer.write("Range       : 1 - " + rangeMax + "\r\n");
                writer.write("Attempts    : " + attempts + " / " + maxAttempts + "\r\n");
                writer.write("Score       : " + currentScore + "\r\n");
                writer.write("Best Score  : " + bestScore + "\r\n");
                writer.write("Saved On    : " + timestamp + "\r\n");

                JOptionPane.showMessageDialog(this,
                        "Saved successfully to:\n" + chosenFile.getAbsolutePath(),
                        "Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving file!", "File Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // ---------- File Handling: Best Score ----------
    private int loadBestScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BEST_SCORE_FILE))) {
            String line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
                return Integer.parseInt(line.trim());
            }
        } catch (IOException e) {
            // File doesn't exist yet - fine, no best score recorded
        } catch (NumberFormatException e) {
            System.out.println("Warning: bestscore.txt was corrupted, resetting.");
        }
        return Integer.MIN_VALUE; // No score yet
    }

    private void saveBestScore(int score) {
        try (FileWriter writer = new FileWriter(BEST_SCORE_FILE)) {
            writer.write(String.valueOf(score));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving best score!", "File Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ---------- File Handling: Player Records ----------
    private void savePlayerRecord() {
        try (FileWriter writer = new FileWriter(PLAYERS_FILE, true)) { // append mode
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            writer.write(playerName + "," + playerAge + "," + attempts + "," + currentScore + "," + timestamp + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving player record!", "File Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}