package pkg2ddrawingapp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class Draw_Pane extends JFrame {

    private final Draw_Box drawBox; //a play on dropbox
    private final JButton undoButton;
    private final JButton clearButton;
    private final JButton color1Button;
    private final JButton color2Button;
    private final JCheckBox Filled;
    private final JCheckBox Gradient;
    private final JCheckBox Dashed;
    private final JComboBox<String> shapeChoice;
    private final JPanel optionLine1;
    private final JPanel optionLine2;
    private final JPanel optionLine;
    private final JLabel Shape;
    private final JLabel Width;
    private final JLabel Length;
    private final JLabel statusBar;
    private final JTextField LineWidth;
    private final JTextField DashLength;
    private static final String[] shapes = {"Line", "Oval", "Rectangle"};
    private Color color1;
    private Color color2;

    public Draw_Pane() {
        super("2D Drawing Application");
        setLayout(new BorderLayout());
        optionLine1 = new JPanel();
        optionLine2 = new JPanel();
        optionLine = new JPanel();
        optionLine.setLayout(new BorderLayout());
        drawBox = new Draw_Box();
        drawBox.setBackground(Color.WHITE);        

        Shape = new JLabel("Shape: ");
        Width = new JLabel("Line Width: ");
        Length = new JLabel("Dash Length: ");
        statusBar = new JLabel("Move outside JPanel");

        LineWidth = new JTextField("5",2);
        DashLength = new JTextField("5",2);

        undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (drawBox.itemsDrawn.size() != 0) {
                    drawBox.itemsDrawn.remove(drawBox.itemsDrawn.size() - 1);
                    repaint();
                }
            }
        }
        );
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawBox.itemsDrawn = new ArrayList<>();
                repaint();
            }
        }
        );
        color1Button = new JButton("1st Color...");
        color1Button.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                color1 = JColorChooser.showDialog(
                        Draw_Pane.this, "Chose a color", color1);

                if (color1 == null) {
                    color1 = Color.LIGHT_GRAY;
                }
            }
        });
        color2Button = new JButton("2nd Color...");
        color2Button.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                color2 = JColorChooser.showDialog(
                        Draw_Pane.this, "Chose a color", color2);

                if (color2 == null) {
                    color2 = Color.LIGHT_GRAY;
                }
            }
        });

        shapeChoice = new JComboBox<String>(shapes);

        Filled = new JCheckBox("Filled");
        Gradient = new JCheckBox("Use Gradient");
        Dashed = new JCheckBox("Dashed");

        optionLine1.add(undoButton);
        optionLine1.add(clearButton);
        optionLine1.add(Shape);
        optionLine1.add(shapeChoice);
        optionLine1.add(Filled);

        optionLine2.add(Gradient);
        optionLine2.add(color1Button);
        optionLine2.add(color2Button);
        optionLine2.add(Width);
        optionLine2.add(LineWidth);
        optionLine2.add(Length);
        optionLine2.add(DashLength);
        optionLine2.add(Dashed);

        optionLine.add(optionLine1, BorderLayout.NORTH);
        optionLine.add(optionLine2, BorderLayout.SOUTH);
        add(optionLine, BorderLayout.NORTH);
        add(drawBox, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    public JButton getColor1Button() {
        return color1Button;
    }

    public JButton getColor2Button() {
        return color2Button;
    }

    public boolean getFilled() {
        return Filled.isSelected();
    }

    public boolean getGradient() {
        return Gradient.isSelected();
    }

    public boolean getDashed() {
        return Dashed.isSelected();
    }

    public float getLineWidth() {
        return Float.parseFloat(LineWidth.getText());
    }

    public float getDashLength() {
        return Float.parseFloat(DashLength.getText());
    }
    
    private class Draw_Box extends JPanel {
        
        private ArrayList<Shapes> itemsDrawn = new ArrayList<Shapes>();
        
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            for (Shapes shape : itemsDrawn) {
                shape.draw (g);
            }
        }
        
        public Draw_Box() {
            MouseHandler handler1 = new MouseHandler();
            addMouseListener(handler1);
            addMouseMotionListener(handler1);
        }
        
        public class State {
            private final Color color1;
            private final Color color2;
            private final boolean gradient;
            private final boolean filled;
            private final boolean dashed;
            private final float LineWidth;
            private final float DashLength;
            private final Point start;

            public State(Color color1, Color color2, boolean gradient, boolean filled, boolean dashed, 
                        float lineWidth, float dashLength, Point start) {
                this.color1 = color1;
                this.color2 = color2;
                this.gradient = gradient;
                this.filled = filled;
                this.dashed = dashed;
                this.LineWidth = lineWidth;
                this.DashLength = dashLength;
                this.start = start;
            }

            public Color getColor1() {
                return color1;
            }

            public Color getColor2() {
                return color2;
            }

            public boolean isGradient() {
                return gradient;
            }

            public boolean isFilled() {
                return filled;
            }

            public boolean isDashed() {
                return dashed;
            }

            public float getLineWidth() {
                return LineWidth;
            }

            public float getDashLength() {
                return DashLength;
            }
            
            public int getStartX() {
                return (int) start.getX();
            }
            
            public int getStartY() {
                return (int) start.getY();
            }
        }
    
        public abstract class Shapes extends Object {
            
            public State state;
            private int endX = 0;
            private int endY = 0;

            public Shapes(State state) {
                this.state = state;
            }
            
            public State getState() {
                return state;
            }
            
            public abstract void draw(Graphics g);          
            
            public int getEndX() {
                return endX;
            }

            public void setEndX(int endX) {
                this.endX = endX;
            }

            public int getEndY() {
                return endY;
            }

            public void setEndY(int endY) {
                this.endY = endY;
            }
        }
        
        public class Line extends Shapes {
            public Line(State state) {
                super(state);
            }

            @Override
            public void draw (Graphics g) {
                g.setColor(state.color1);
                if (state.isDashed() == true) {
                    Graphics2D g2d = (Graphics2D) g;
                    float[] dash1 = {state.LineWidth};                    
                    final BasicStroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash1, 2f);
                    g2d.setStroke(dashed);
                    g2d.drawLine(state.getStartX(),state.getStartY(), getEndX(), getEndY());
                }
                else {
                    Graphics2D g2d = (Graphics2D) g;
                    final BasicStroke solid = new BasicStroke(state.DashLength, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, state.LineWidth);
                    g2d.setStroke(solid);
                    g2d.drawLine(state.getStartX(),state.getStartY(), getEndX(), getEndY());
                }
                if (state.isGradient() == true) {
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gradient = new GradientPaint(state.getStartX(), state.getStartY(), state.getColor1(), getEndX(), getEndY(), state.getColor2());
                    g2d.setPaint(gradient);
                    g2d.drawLine(state.getStartX(),state.getStartY(), getEndX(), getEndY());
                }
            }
        }

        public class Rectangle extends Shapes {
            public Rectangle(State state) {
                super(state);
            }
            
            @Override
            public void draw (Graphics g) {
                if (state.isDashed() == true) {
                    Graphics2D g2d = (Graphics2D) g;
                    final float dash1[] = {state.LineWidth};
                    final BasicStroke dashed = new BasicStroke(state.DashLength, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
                    g2d.setStroke(dashed);
                    g2d.drawRect(state.getStartX(),state.getStartY(), getEndX(), getEndY());
                } 
                else {
                    Graphics2D g2d = (Graphics2D) g;
                    final BasicStroke solid = new BasicStroke(state.DashLength, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, state.LineWidth);
                    g2d.setStroke(solid);
                    g2d.drawRect(state.getStartX(),state.getStartY(), getEndX(), getEndY());
                }
                if (state.isFilled() == true) {
                    g.fillRect(Math.min(state.getStartX(), getEndX()), Math.min(state.getStartY(), getEndY()),
                    Math.abs(state.getStartX() - getEndX()),
                    Math.abs(state.getStartY() - getEndY()));
                }
                else {
                    g.drawRect(Math.min(state.getStartX(), getEndX()), Math.min(state.getStartY(), getEndY()),
                    Math.abs(state.getStartX() - getEndX()),
                    Math.abs(state.getStartY() - getEndY()));
                }
                if (state.isGradient() == true) {
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gradient = new GradientPaint(state.getStartX(), state.getStartY(), state.getColor1(), getEndX(), getEndY(), state.getColor2());
                    g2d.setPaint(gradient);
                    g.fillRect(state.getStartX(), getEndX(), state.getStartY(), getEndY());
                }
            }
        }
        
        public class Oval extends Shapes { 
            public Oval(State state) {
                super(state);
            }

            @Override
            public void draw (Graphics g) {
                if (state.isDashed() == true) {
                    Graphics2D g2d = (Graphics2D) g;
                    final float dash1[] = {state.LineWidth};
                    final BasicStroke dashed = new BasicStroke(state.DashLength, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
                    g2d.setStroke(dashed);
                    g2d.drawOval(state.getStartX(),state.getStartY(), getEndX(), getEndY());
                }
                else {
                    Graphics2D g2d = (Graphics2D) g;
                    final BasicStroke solid = new BasicStroke(state.DashLength, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, state.LineWidth);
                    g2d.setStroke(solid);
                    g2d.drawOval(state.getStartX(),state.getStartY(), getEndX(), getEndY());
                }
                if (state.isFilled() == true) {
                    g.fillOval(Math.min(state.getStartX(), getEndX()), Math.min(state.getStartY(), getEndY()),
                    Math.abs(state.getStartX() - getEndX()),
                    Math.abs(state.getStartY() - getEndY()));
                }
                else {
                    g.drawOval(Math.min(state.getStartX(), getEndX()), Math.min(state.getStartY(), getEndY()),
                    Math.abs(state.getStartX() - getEndX()),
                    Math.abs(state.getStartY() - getEndY()));
                }
                if (state.isGradient() == true) {
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gradient = new GradientPaint(state.getStartX(), state.getStartY(), state.getColor1(), getEndX(), getEndY(), state.getColor2());
                    g2d.setPaint(gradient);
                    g.fillOval(state.getStartX(), getEndX(), state.getStartY(), getEndY());
                }
            }
        }

        public class MouseHandler extends MouseAdapter {
            
            Point start = new Point();
            Point end = new Point();
            
            @Override
            public void mousePressed(MouseEvent event) {
                //start.setLocation(event.getX(), event.getY());
                statusBar.setText(String.format("(%d, %d)", event.getX(), event.getY()));
                
                State newObject = new State(color1, color2, getGradient(), getFilled(), getDashed(), getDashLength(), getLineWidth(), start);
                Shapes currentShape1 = new Line(newObject);
                Shapes currentShape2 = new Rectangle(newObject);
                Shapes currentShape3 = new Oval(newObject);
                if(shapeChoice.getSelectedItem() == "Line") {
                    itemsDrawn.add(currentShape1);
                }
                else if(shapeChoice.getSelectedItem() == "Rectangle") {
                    itemsDrawn.add(currentShape2);
                }
                else if(shapeChoice.getSelectedItem() == "Oval") {
                    itemsDrawn.add(currentShape3);
                }
            }
            @Override
            public void mouseReleased(MouseEvent event) {
                statusBar.setText(String.format("(%d, %d)", event.getX(), event.getY()));
            }
            @Override
            public void mouseDragged(MouseEvent event) {
                statusBar.setText(String.format("(%d, %d)", event.getX(), event.getY()));
                
                itemsDrawn.get(itemsDrawn.size() - 1).setEndX(event.getX());
                itemsDrawn.get(itemsDrawn.size() - 1).setEndY(event.getY());
                repaint();
            }
            @Override
            public void mouseMoved(MouseEvent event) {
                statusBar.setText(String.format("(%d, %d)", event.getX(), event.getY()));
            }
        }
    }
}
