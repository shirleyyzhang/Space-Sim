/**
 File Name: ButtonManager.java
 @author: Samuel Ho, Susie Choi, Shirley Zhang, Tony Li
 Date: January 17, 2022
 Description: This class manages Buttons for user interface.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class UIManager {

    /* Fields */
    private static final Font BUTTON_FONT = new Font("arial", Font.BOLD, 15); // class field: Button font
    private Map<String, JButton> buttons; // instance field: buttons to manage
    private Map<String, JTextField> textfields; // instance field: associated textfields
    private BodyManager bodyManager; // instance field: associated bodyManager
    private FileManager fileManager; // instance field: associated fileManager
    private Space space; // instance field: associated Space simulation
    private JTextArea display; // instance field: display text area for output

    Color textColor = new Color(0, 67, 87);
    Color blueBorderColor = new Color(0, 196, 255);
    Color blueDarkBorderColor = new Color(0, 149, 255);
    /* CONSTRUCTORS */

    /**
     * ButtonManager: Constructor that creates a new ButtonManager object,
     * corresponding Buttons and display screen
     *
     * @param space
     * @param bodyManager
     * @param fileManager
     */
    public UIManager(Space space, BodyManager bodyManager, FileManager fileManager) {

        buttons = new HashMap<String, JButton>();
        textfields = new HashMap<String, JTextField>();

        this.fileManager = fileManager;
        this.space = space;
        this.bodyManager = bodyManager;

        // DISPLAY FOR OUTPUT

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        display = new JTextArea("DISPLAY:");
        display.setBounds(10, 10, 200, Space.getScreenHeight() - 100);
        display.setMargin(new Insets(10, 10, 10, 10));
        display.setWrapStyleWord(true);
        display.setLineWrap(true);
        display.setEditable(false);
        display.setCaretPosition(display.getDocument().getLength());

        scrollPane.setPreferredSize(new Dimension(10, 10));
        scrollPane.setBounds(10, 10, 200, Space.getScreenHeight() - 100);
        scrollPane.getViewport().add(display);

        space.add(scrollPane);

        createButtons();
        createTextFields();
    }

    private void createButtons() {
        // MENU BUTTONS COLUMN 1

        // BUTTON COLOR OPTIONS

        buttons.put("menu", new JButton("Menu"));
        buttons.get("menu").setBounds(230, 50, 150, 50);
        buttons.get("menu").setFont(BUTTON_FONT);

        buttons.put("run", new JButton("Run Sim"));
        buttons.get("run").setBounds(230, 120, 150, 50);
        buttons.get("run").setFont(BUTTON_FONT);

        buttons.put("pause", new JButton("Pause Sim"));
        buttons.get("pause").setBounds(230, 190, 150, 50);
        buttons.get("pause").setFont(BUTTON_FONT);

        buttons.put("clear", new JButton("Clear Sim"));
        buttons.get("clear").setBounds(230, 260, 150, 50);
        buttons.get("clear").setFont(BUTTON_FONT);

        buttons.put("largestMoon", new JButton("Largest Moon"));
        buttons.get("largestMoon").setBounds(Space.getScreenWidth() - 180, 50, 150, 50);
        buttons.get("largestMoon").setFont(BUTTON_FONT);

        buttons.put("mostPopPlan", new JButton("Most Pop. Planet"));
        buttons.get("mostPopPlan").setBounds(Space.getScreenWidth() - 180, 120, 150, 50);
        buttons.get("mostPopPlan").setFont(BUTTON_FONT);

        buttons.put("oldestStar", new JButton("Oldest Star"));
        buttons.get("oldestStar").setBounds(Space.getScreenWidth() - 180, 190, 150, 50);
        buttons.get("oldestStar").setFont(BUTTON_FONT);

        buttons.put("fastestAsteroid", new JButton("Fastest Asteroid"));
        buttons.get("fastestAsteroid").setBounds(Space.getScreenWidth() - 180, 260, 150, 50);
        buttons.get("fastestAsteroid").setFont(BUTTON_FONT);

        buttons.put("displayStats", new JButton("Display Stats"));
        buttons.get("displayStats").setBounds(Space.getScreenWidth() - 180, 330, 150, 50);
        buttons.get("displayStats").setFont(BUTTON_FONT);

        buttons.put("listBodies", new JButton("List Bodies"));
        buttons.get("listBodies").setBounds(Space.getScreenWidth() - 180, 400, 150, 50);
        buttons.get("listBodies").setFont(BUTTON_FONT);

        buttons.put("avgArea", new JButton("Average Area"));
        buttons.get("avgArea").setBounds(Space.getScreenWidth() - 180, 470, 150, 50);
        buttons.get("avgArea").setFont(BUTTON_FONT);

        buttons.put("avgAge", new JButton("Average Age"));
        buttons.get("avgAge").setBounds(Space.getScreenWidth() - 180, 540, 150, 50);
        buttons.get("avgAge").setFont(BUTTON_FONT);

        // MENU BUTTONS SECOND COLUMN

        buttons.put("addFile", new JButton("Add File"));
        buttons.get("addFile").setBounds(Space.getScreenWidth() - 360, 50, 150, 50);
        buttons.get("addFile").setFont(BUTTON_FONT);

        buttons.put("load", new JButton("Load File"));
        buttons.get("load").setBounds(Space.getScreenWidth() - 360, 120, 150, 50);
        buttons.get("load").setFont(BUTTON_FONT);

        buttons.put("save", new JButton("Save File"));
        buttons.get("save").setBounds(Space.getScreenWidth() - 360, 190, 150, 50);
        buttons.get("save").setFont(BUTTON_FONT);

        buttons.put("fileNames", new JButton("File Names"));
        buttons.get("fileNames").setBounds(Space.getScreenWidth() - 360, 260, 150, 50);
        buttons.get("fileNames").setFont(BUTTON_FONT);

        buttons.put("supportLife", new JButton("Support Life"));
        buttons.get("supportLife").setBounds(Space.getScreenWidth() - 360, 330, 150, 50);
        buttons.get("supportLife").setFont(BUTTON_FONT);

        buttons.put("shortestPath", new JButton("Shortest Path"));
        buttons.get("shortestPath").setBounds(Space.getScreenWidth() - 360, 400, 150, 50);
        buttons.get("shortestPath").setFont(BUTTON_FONT);

        buttons.put("olderThan", new JButton("Older Than"));
        buttons.get("olderThan").setBounds(Space.getScreenWidth() - 360, 470, 150, 50);
        buttons.get("olderThan").setFont(BUTTON_FONT);

        buttons.put("addBody", new JButton("Add Body"));
        buttons.get("addBody").setBounds(Space.getScreenWidth() - 360, 540, 150, 50);
        buttons.get("addBody").setFont(BUTTON_FONT);

        // ADD BODY OPTION BUTTONS

        buttons.put("addStar", new JButton("Add Star"));
        buttons.get("addStar").setBounds(Space.getScreenWidth() - 180, 50, 150, 50);
        buttons.get("addStar").setFont(BUTTON_FONT);

        buttons.put("addPlanet", new JButton("Add Planet"));
        buttons.get("addPlanet").setBounds(Space.getScreenWidth() - 180, 120, 150, 50);
        buttons.get("addPlanet").setFont(BUTTON_FONT);

        buttons.put("addMoon", new JButton("Add Moon"));
        buttons.get("addMoon").setBounds(Space.getScreenWidth() - 180, 190, 150, 50);
        buttons.get("addMoon").setFont(BUTTON_FONT);

        buttons.put("addAsteroid", new JButton("Add Asteroid"));
        buttons.get("addAsteroid").setBounds(Space.getScreenWidth() - 180, 260, 150, 50);
        buttons.get("addAsteroid").setFont(BUTTON_FONT);

        // BUTTON BORDER OPTIONS

        Border border1 = new LineBorder(blueDarkBorderColor, 6, true);
        Border border2 = new LineBorder(blueBorderColor, 6, true);

        for (Entry<String, JButton> button : buttons.entrySet()) {
            String name = button.getKey();
            JButton b = button.getValue();
            b.setActionCommand(name);
            b.setForeground(textColor);
            b.setBackground(Color.WHITE);
            b.setFocusable(false);
            b.setBorder(border2);
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    b.setBorder(border1);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    b.setBorder(border2);
                }
            });

            space.add(b);
            b.setVisible(false);
            b.addActionListener(space);

            buttons.get("menu").setVisible(true);
        }
    }

    private void createTextFields() {
        textfields.put("addFile", new JTextField());
        textfields.get("addFile").setBounds(Space.getScreenWidth() - 500, 50, 100, 50);
        textfields.get("addFile").setName("Enter File Name");

        textfields.put("load", new JTextField());
        textfields.get("load").setBounds(Space.getScreenWidth() - 500, 120, 100, 50);
        textfields.get("load").setName("File Number");

        textfields.put("save", new JTextField());
        textfields.get("save").setBounds(Space.getScreenWidth() - 500, 190, 100, 50);
        textfields.get("save").setName("File Number");

        textfields.put("supportLife", new JTextField());
        textfields.get("supportLife").setBounds(Space.getScreenWidth() - 500, 330, 100, 50);
        textfields.get("supportLife").setName("Planet");

        textfields.put("shortestPathPlanet1", new JTextField());
        textfields.get("shortestPathPlanet1").setBounds(Space.getScreenWidth() - 500, 390, 100, 20);
        textfields.get("shortestPathPlanet1").setName("Planet 1");

        textfields.put("shortestPathPlanet2", new JTextField());
        textfields.get("shortestPathPlanet2").setBounds(Space.getScreenWidth() - 500, 415, 100, 20);
        textfields.get("shortestPathPlanet2").setName("Planet 2");

        textfields.put("shortestPathLength", new JTextField());
        textfields.get("shortestPathLength").setBounds(Space.getScreenWidth() - 500, 440, 100, 20);
        textfields.get("shortestPathLength").setName("Length");

        textfields.put("olderThan", new JTextField());
        textfields.get("olderThan").setBounds(Space.getScreenWidth() - 500, 470, 100, 50);
        textfields.get("olderThan").setName("Min Age");

        // TEXTFIELDS FOR ADD BODY

        textfields.put("name", new JTextField());
        textfields.get("name").setBounds(Space.getScreenWidth() - 400, 50, 200, 50);
        textfields.get("name").setName("Enter Name");

        textfields.put("mass", new JTextField());
        textfields.get("mass").setBounds(Space.getScreenWidth() - 400, 120, 200, 50);
        textfields.get("mass").setName("Enter Mass");

        textfields.put("age", new JTextField());
        textfields.get("age").setBounds(Space.getScreenWidth() - 400, 190, 200, 50);
        textfields.get("age").setName("Enter Age");

        textfields.put("radius", new JTextField());
        textfields.get("radius").setBounds(Space.getScreenWidth() - 400, 260, 200, 50);
        textfields.get("radius").setName("Enter Radius");

        textfields.put("orbitStar", new JTextField());
        textfields.get("orbitStar").setBounds(Space.getScreenWidth() - 400, 330, 200, 50);
        textfields.get("orbitStar").setName("Enter Orbit Star (only for Planet)");

        textfields.put("orbitPlanet", new JTextField());
        textfields.get("orbitPlanet").setBounds(Space.getScreenWidth() - 400, 400, 200, 50);
        textfields.get("orbitPlanet").setName("Enter Orbit Planet (only for Moon)");

        Color textColor = new Color(0, 67, 87);

        for (Entry<String, JTextField> textfield : textfields.entrySet()) {
            String name = textfield.getKey();
            JTextField t = textfield.getValue();
            t.setActionCommand(name);
            t.setText(t.getName());
            t.setForeground(textColor);
            t.setBackground(Color.WHITE);
            t.setFocusable(true);
            t.addFocusListener(new FocusListener() {

                @Override
                public void focusLost(FocusEvent e) {
                    if (t.getText().isEmpty()) {
                        t.setText(t.getName());
                    }
                }

                @Override
                public void focusGained(FocusEvent e) {
                    if (t.getText().equals(t.getName())) {
                        t.setText("");
                    }
                }
            });

            space.add(t);
            t.setVisible(false);
            t.addActionListener(space);
        }
    }

    /**
     * buttonFunction: Manages functions associated with each Button
     *
     * @param action
     */
    public void buttonFunction(String action) {

        String inName = "";
        double mass = 0;
        int age = 0;
        double radius = 0;

        switch (action) {
            case "menu":
                if (!buttons.get("addBody").isVisible()) {
                    for (Entry<String, JButton> button : buttons.entrySet()) {
                        JButton b = button.getValue();
                        b.setVisible(true);
                    }
                    buttons.get("addStar").setVisible(false);
                    buttons.get("addPlanet").setVisible(false);
                    buttons.get("addMoon").setVisible(false);
                    buttons.get("addAsteroid").setVisible(false);

                    for (Entry<String, JTextField> textfield : textfields.entrySet()) {
                        JTextField t = textfield.getValue();
                        t.setVisible(false);
                    }
                    textfields.get("addFile").setVisible(true);
                    textfields.get("load").setVisible(true);
                    textfields.get("save").setVisible(true);
                    textfields.get("supportLife").setVisible(true);
                    textfields.get("shortestPathPlanet1").setVisible(true);
                    textfields.get("shortestPathPlanet2").setVisible(true);
                    textfields.get("shortestPathLength").setVisible(true);
                    textfields.get("olderThan").setVisible(true);
                } else {
                    for (Entry<String, JButton> button : buttons.entrySet()) {
                        JButton b = button.getValue();
                        b.setVisible(false);
                        buttons.get("menu").setVisible(true);
                    }
                    for (Entry<String, JTextField> textfield : textfields.entrySet()) {
                        JTextField t = textfield.getValue();
                        t.setVisible(false);
                    }
                }
                break;
            case "run":
                space.setRunning(true);
                break;
            case "pause":
                space.setRunning(false);
                break;
            case "clear":
                space.clearSim();
                break;
            case "fileNames":
                display.append(fileManager.listFiles());
                break;
            case "addFile":
                try {
                    fileManager.addFile(textfields.get("addFile").getText());
                } catch (NumberFormatException nfe) {
                    display.append("\n\nPlease enter valid info into text field");
                }
                break;
            case "load":
                try {
                    fileManager.loadFile(Integer.parseInt(textfields.get("load").getText()));
                } catch (NumberFormatException nfe) {
                    display.append("\nPlease enter valid info into text field");
                } catch (NullPointerException npe) {
                    display.append("\nPlease enter a valid file number into text field");
                }
                break;
            case "save":
                try {
                    System.out.println(textfields.get("save").getText());
                    fileManager.saveFile(Integer.parseInt(textfields.get("save").getText()));
                } catch (NumberFormatException nfe) {
                    display.append("\n\nPlease enter valid info into text field");
                }
                break;
            case "displayStats":
                display.append(bodyManager.displayStats());
                break;
            case "listBodies":
                display.append(bodyManager.listBodies());
                break;
            case "avgArea":
                display.append("\n\nAverage Area: " + bodyManager.averageArea());
                break;
            case "avgAge":
                display.append("\n\nAverage Age: " + bodyManager.averageAge());
                break;
            case "largestMoon":
                display.append("\n\nLargest Moon: " + bodyManager.largestMoon());
                break;
            case "mostPopPlan":
                display.append("\n\nMost Populated Planet: " + bodyManager.mostPopPlanet());
                break;
            case "oldestStar":
                display.append("\n\nOldest Star: " + bodyManager.oldestStar());
                break;
            case "fastestAsteroid":
                display.append("\n\nFastest Asteroid: " + bodyManager.fastestAsteroid());
                break;
            case "supportLife":
                try {
                    String inPlanet = textfields.get("supportLife").getText();
                    if (bodyManager.supportLife(inPlanet)) {
                        display.append("\n\n" + inPlanet + " can support life");
                    } else {
                        display.append("\n\n" + inPlanet + " cannot support life");
                    }
                } catch (NumberFormatException nfe) {
                    display.append("\n\nPlease enter valid info into text fields");
                }
                break;
            case "shortestPath":
                try {
                    String planet1 = textfields.get("shortestPathPlanet1").getText();
                    String planet2 = textfields.get("shortestPathPlanet2").getText();
                    double length = Double.parseDouble(textfields.get("shortestPathLength").getText());
                    if (bodyManager.findShortestPath(planet1, planet2, length) != null) {
                        display.append(bodyManager.findShortestPath(planet1, planet2, length) + "");
                    } else {
                        display.append("\n\nIt is not possible to make this journey without surpassing distance " + length);
                    }
                } catch (NumberFormatException nfe) {
                    display.append("\n\nPlease enter valid info into text fields");
                }
                bodyManager.resetPlanetVisits();
                break;
            case "olderThan":
                try {
                    int inAge = Integer.parseInt(textfields.get("olderThan").getText());
                    display.append("\n\nCelestial Bodies older than " + inAge + ":\n" + bodyManager.bodiesOlderThan(inAge));
                    if (bodyManager.bodiesOlderThan(inAge) == "") {
                        display.append("none");
                    }
                } catch (NumberFormatException nfe) {
                    display.append("\n\nPlease enter valid info into text fields");
                }
                break;
            case "addBody":

                buttons.get("addStar").setVisible(true);
                buttons.get("addPlanet").setVisible(true);
                buttons.get("addMoon").setVisible(true);
                buttons.get("addAsteroid").setVisible(true);
                textfields.get("name").setVisible(true);
                textfields.get("mass").setVisible(true);
                textfields.get("age").setVisible(true);
                textfields.get("radius").setVisible(true);
                textfields.get("orbitStar").setVisible(true);
                textfields.get("orbitPlanet").setVisible(true);

                buttons.get("addBody").setVisible(false);
                buttons.get("load").setVisible(false);
                buttons.get("save").setVisible(false);
                buttons.get("displayStats").setVisible(false);
                buttons.get("listBodies").setVisible(false);
                buttons.get("avgArea").setVisible(false);
                buttons.get("avgAge").setVisible(false);
                buttons.get("largestMoon").setVisible(false);
                buttons.get("mostPopPlan").setVisible(false);
                buttons.get("oldestStar").setVisible(false);
                buttons.get("fastestAsteroid").setVisible(false);
                buttons.get("supportLife").setVisible(false);
                buttons.get("shortestPath").setVisible(false);
                buttons.get("addFile").setVisible(false);
                buttons.get("olderThan").setVisible(false);
                buttons.get("fileNames").setVisible(false);
                textfields.get("load").setVisible(false);
                textfields.get("save").setVisible(false);
                textfields.get("supportLife").setVisible(false);
                textfields.get("shortestPathPlanet1").setVisible(false);
                textfields.get("shortestPathPlanet2").setVisible(false);
                textfields.get("shortestPathLength").setVisible(false);
                textfields.get("olderThan").setVisible(false);
                textfields.get("addFile").setVisible(false);
                break;
            case "addStar":
                try {
                    inName = textfields.get("name").getText();
                    mass = Double.parseDouble((textfields.get("mass")).getText());
                    age = Integer.parseInt((textfields.get("age")).getText());
                    radius = Double.parseDouble((textfields.get("radius")).getText());
                } catch (NumberFormatException nfe) {
                    display.append("\n\nPlease enter valid info into text fields");
                }
                bodyManager.addStar(inName, mass, age, radius);
                break;
            case "addPlanet":
                String orbitStar = "";
                try {
                    inName = textfields.get("name").getText();
                    mass = Double.parseDouble((textfields.get("mass")).getText());
                    age = Integer.parseInt((textfields.get("age")).getText());
                    radius = Double.parseDouble((textfields.get("radius")).getText());
                    orbitStar = textfields.get("orbitStar").getText();
                } catch (NumberFormatException nfe) {
                    display.append("\n\nPlease enter valid info into text fields");
                }
                bodyManager.addPlanet(inName, mass, age, radius, orbitStar);
                break;
            case "addMoon":
                String orbitPlanet = "";
                try {
                    inName = textfields.get("name").getText();
                    mass = Double.parseDouble((textfields.get("mass")).getText());
                    age = Integer.parseInt((textfields.get("age")).getText());
                    radius = Double.parseDouble((textfields.get("radius")).getText());
                    orbitPlanet = textfields.get("orbitPlanet").getText();
                } catch (NumberFormatException nfe) {
                    display.append("\n\nPlease enter valid info into text fields");
                }
                bodyManager.addMoon(inName, mass, age, radius, orbitPlanet);
                break;
            case "addAsteroid":
                try {
                    inName = textfields.get("name").getText();
                    mass = Double.parseDouble((textfields.get("mass")).getText());
                } catch (NumberFormatException nfe) {
                    display.append("\n\nPlease enter valid info into text fields");
                }
                bodyManager.addAsteroid(inName, mass);
                break;

        }

    }

}