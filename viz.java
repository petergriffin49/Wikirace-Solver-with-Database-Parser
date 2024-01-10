import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class viz extends Frame implements ActionListener {
    JLabel title;
    JLabel sDesc;
    JLabel tDesc;
    JLabel names;
    JTextArea path;
    JButton sButton;
    JTextField source;
    JTextField target;

    private int windowWidth = 500;
    private int windowHeight = 400;

    // constructor
    viz() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        setLabels();
        setButtons();
        setTextFields();
        setBounds((int)size.getWidth()/2-windowWidth/2,(int)size.getHeight()/2-windowHeight/2,windowWidth,windowHeight);
        setLayout(null);
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
    }

    // checks if articles will exist
    public boolean checkArticleValidity(String s, String t) {
        boolean sConfirmed = s.isEmpty();
        boolean tConfirmed = t.isEmpty();

        if (!sConfirmed && !tConfirmed) return true;
        if (sConfirmed && tConfirmed) {
            path.setText("target and source does not exist");
            return false;
        }
        if (!tConfirmed) {
            path.setText("source does not exist");
            return false;
        }
        path.setText("target does not exist");
        return false;
    }

    // formats path for window
    public String formatPath(String[] path) {
        String p = "";
        for (int i = 0; i < path.length; i++) {
            if (i > 0){
                p += "\n";
            }
            p += path[i];
        }
        return p;
    }

    // Starts search for path once GO! is clicked
    public void actionPerformed(ActionEvent e) {
        try {
            String s = source.getText();
            String t = target.getText();
            String out = "";
            s  = s.substring(0, 1).toUpperCase() + s.substring(1);
            t  = t.substring(0, 1).toUpperCase() + t.substring(1);
            s = findFile.isFile(s);
            t = findFile.isFile(t);
            boolean hasPath = checkArticleValidity(s, t);
            if(hasPath) {
                String[] pathArr = Wikirace.run(s,t);
                String pathStr = formatPath(pathArr);
                path.setText(pathStr);

            }
            path.revalidate();
        } catch(Exception x){System.out.println(x);}
    }

    // set labels in window
    public void setLabels() {
        title = new JLabel("Wikipedia Path Finder");
        title.setBounds(200,50,130,30);
        add(title);

        sDesc = new JLabel("Insert Source Topic Here: ");
        sDesc.setBounds(25,100,150,30);
        add(sDesc);

        tDesc = new JLabel("Insert Target Topic Here: ");
        tDesc.setBounds(25,130,150,30);
        add(tDesc);

        names = new JLabel("By Ben Klein and John Sayles :)");
        names.setForeground(Color.LIGHT_GRAY);
        names.setBounds(windowWidth-200,windowHeight-40,180,20);
        add(names);

        path = new JTextArea("");
        path.setBounds(25,250,250,500);
        add(path);
    }

    // set buttons in window
    public void setButtons() {
        sButton = new JButton("GO!");
        sButton.setBounds(200,200,60,25);
        sButton.addActionListener(this);
        add(sButton);
    }

    // set text fields in window
    public void setTextFields() {
        source = new JTextField();
        source.setBounds(200,100,150,30);
        add(source);

        target = new JTextField();
        target.setBounds(200,130,150,30);
        add(target);
    }

    public static void main(String[] args) {
        new viz();
    }
}
