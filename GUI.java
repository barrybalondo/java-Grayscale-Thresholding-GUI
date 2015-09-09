import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GUI extends JFrame implements ActionListener{

    Grayscale grayscale;
    Thresholding thresholding;

    private String algoTitle;
    private JMenuBar menuBar;
    private JMenu file, gray, thre, submenu;
    private JMenuItem item;

    private JTextArea instruction;

    private ImageIcon mainPicture, grayPicture, threPicture;
    private BufferedImage imageLoad, grayLoad;
    private JScrollPane pictureFrame;

    GUI(){

        grayscale = new Grayscale(); // instantiate new grayscale object
        thresholding = new Thresholding(); // instantiate new thresholding object

        // initializing and adding menu bars and items
        menuBar = new JMenuBar();
        // menu for File
        file = new JMenu("File");
        menuBar.add(file);
        item = new JMenuItem("Open");
        item.addActionListener(this);
        file.add(item);
        item = new JMenuItem("Exit");
        file.add(item);
        item.addActionListener(this);

        // menu for Grayscale
        gray = new JMenu("Grayscale");
        gray.setVisible(false);
        menuBar.add(gray);
        item = new JMenuItem("Averaging");
        gray.add(item);
        item.addActionListener(this);
        item = new JMenuItem("Luminance");
        gray.add(item);
        item.addActionListener(this);
        item = new JMenuItem("Desaturation");
        gray.add(item);
        item.addActionListener(this);
        // submenu for Decomposition
        submenu = new JMenu("Decomposition");
        gray.add(submenu);
        item = new JMenuItem("Min");
        submenu.add(item);
        item.addActionListener(this);
        item = new JMenuItem("Max");
        submenu.add(item);
        item.addActionListener(this);

        // submenu for Single Color Channel
        submenu = new JMenu("Single Color Channel");
        gray.add(submenu);
        item = new JMenuItem("Red");
        submenu.add(item);
        item.addActionListener(this);
        item = new JMenuItem("Green");
        submenu.add(item);
        item.addActionListener(this);
        item = new JMenuItem("Blue");
        submenu.add(item);
        item.addActionListener(this);

        item.addActionListener(this);
        item = new JMenuItem("Custom # Shades of Grey");
        gray.add(item);
        item.addActionListener(this);
        item = new JMenuItem("Dithering");
        gray.add(item);
        item.addActionListener(this);
        item = new JMenuItem("No Filter");
        gray.add(item);
        item.addActionListener(this);

        // menu for tresholding
        thre = new JMenu("Treshold");
        thre.setVisible(false);
        menuBar.add(thre);
        item = new JMenuItem("Otsu's Method");
        thre.add(item);
        item.addActionListener(this);
        item = new JMenuItem("Simple Method");
        thre.add(item);
        item.addActionListener(this);
        item = new JMenuItem("K-means Method");
        thre.add(item);
        item.addActionListener(this);
        item = new JMenuItem("Mean Method");
        thre.add(item);
        item.addActionListener(this);
        item = new JMenuItem("MinMaxAverage Method");
        thre.add(item);
        item.addActionListener(this);
        item = new JMenuItem("Gaussian Mixture Model Method");
        thre.add(item);
        item.addActionListener(this);
        item = new JMenuItem("Max Entropy Method");
        thre.add(item);
        item.addActionListener(this);
        

        String instructionsString = " \n  Instructions: \n  Load File -> Choose Grayscale -> Choose Treshold";
        instruction = new JTextArea();
        instruction.append(instructionsString);
        instruction.setEditable(false);
        getContentPane().add(instruction);

        setJMenuBar(menuBar);
        setTitle("OCR Project#2");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setSize(340,120);

    }

    public static void main(String[] args){

        new GUI();

    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // event conditions for Menu Bar and its items
        if( command.equals("Open") ){

            // File chooser components with Open event
            JFileChooser fc = new JFileChooser();
            int result = fc.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {


                File file = fc.getSelectedFile();

                try {

                    imageLoad = ImageIO.read(file);

                    mainPicture = new ImageIcon(imageLoad);

                    pictureFrame = new JScrollPane(new JLabel(mainPicture));
                    pictureFrame.setSize(400, 400);
                    getContentPane().removeAll(); // refreshes the board for new image
                    getContentPane().add(pictureFrame);
                    gray.setVisible(true);
                    thre.setVisible(true);
                    thre.setEnabled(false);
                    setTitle("No Filter");

                    if(mainPicture.getIconWidth()>900 || mainPicture.getIconHeight()>600){
                        setSize(600, 600);
                    }
                    else if(mainPicture.getIconWidth()<200 ){
                        setSize(400,mainPicture.getIconHeight()+50);
                    }
                    else
                        pack();


                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }

            else if (result == JFileChooser.CANCEL_OPTION) {

                return;

            }
            getContentPane().revalidate();

        }

        if( command.equals("Exit") ){

            System.exit(0);

        }

        // event conditions for JComboBox
        if( command.equals("Averaging")){

            algoTitle = "Averaging";
           // grayPicture = new ImageIcon(grayscale.avg(imageLoad));
            grayLoad = grayscale.avg(imageLoad);
            grayPicture = new ImageIcon(grayLoad);
            pictureFrame.setViewportView(new JLabel(grayPicture));
            setTitle(algoTitle);
            thre.setEnabled(true);

        }

        if( command.equals("Luminance") ){

            algoTitle = "Luminance";
            grayLoad = grayscale.luminosity(imageLoad);
            grayPicture = new ImageIcon(grayLoad);
            pictureFrame.setViewportView(new JLabel(grayPicture));
            setTitle(algoTitle);
            thre.setEnabled(true);

        }

        if( command.equals("Desaturation") ){

            algoTitle = "Desaturation";
            grayLoad = grayscale.desaturation(imageLoad);
            grayPicture = new ImageIcon(grayLoad);
            pictureFrame.setViewportView(new JLabel(grayPicture));
            setTitle(algoTitle);
            thre.setEnabled(true);

        }

        if( command.equals("Max") ){

            algoTitle = "Decomposotion : Max";
            grayLoad = grayscale.decompMax(imageLoad);
            grayPicture = new ImageIcon(grayLoad);
            pictureFrame.setViewportView(new JLabel(grayPicture));
            setTitle(algoTitle);
            thre.setEnabled(true);

        }

        if( command.equals("Min") ){

            algoTitle = "Decomposotion : Min";
            grayLoad = grayscale.decompMin(imageLoad);
            grayPicture = new ImageIcon(grayLoad);
            pictureFrame.setViewportView(new JLabel(grayPicture));
            setTitle(algoTitle);
            thre.setEnabled(true);

        }

        if( command.equals("Red") ){

            algoTitle = "Single Color Channel : Red";
            grayLoad = grayscale.rgb(imageLoad, 0);
            grayPicture = new ImageIcon(grayLoad);
            pictureFrame.setViewportView(new JLabel(grayPicture));
            setTitle(algoTitle);
            thre.setEnabled(true);
        }

        if( command.equals("Green") ){

            algoTitle = "Single Color Channel : Green";
            grayLoad = grayscale.rgb(imageLoad, 1);
            grayPicture = new ImageIcon(grayLoad);
            pictureFrame.setViewportView(new JLabel(grayPicture));
            setTitle(algoTitle);
            thre.setEnabled(true);
        }

        if( command.equals("Blue") ){

            algoTitle = "Single Color Channel : Blue";
            grayLoad = grayscale.rgb(imageLoad, 2);
            grayPicture = new ImageIcon(grayLoad);
            pictureFrame.setViewportView(new JLabel(grayPicture));
            setTitle(algoTitle);
            thre.setEnabled(true);
        }


        if( command.equals("Custom # Shades of Grey") ) {

            int value = Integer.parseInt(JOptionPane.showInputDialog("Enter a number between 0-255"));
            while(value > 255 || value <  0){
                value = Integer.parseInt(JOptionPane.showInputDialog("Enter a number between 0-255"));
            }
            algoTitle = "Custom # Shades of Grey : " + value;
            grayLoad = grayscale.customGray(imageLoad, value);
            grayPicture = new ImageIcon(grayLoad);
            pictureFrame.setViewportView(new JLabel(grayPicture));
            setTitle(algoTitle);
            thre.setEnabled(true);
        }

        if( command.equals("Dithering")) {

            BufferedImage dithered = deepCopy(imageLoad);

            int value = Integer.parseInt(JOptionPane.showInputDialog("Enter a number between 0-255"));
            while(value > 255 || value <  0){
                value = Integer.parseInt(JOptionPane.showInputDialog("Enter a number between 0-255"));
            }
            algoTitle = "Dithering: " + value;
            grayLoad = grayscale.customGray(grayscale.dithering(dithered), value);
            grayPicture = new ImageIcon(grayLoad);
            pictureFrame.setViewportView(new JLabel(grayPicture));
            setTitle(algoTitle);
            thre.setEnabled(true);


        }

        if( command.equals("No Filter") ){

            algoTitle = ("No Filter");
            pictureFrame.setViewportView(new JLabel(mainPicture));
            setTitle(algoTitle);
            thre.setEnabled(false);
        }

        if( command.equals("Otsu's Method") ){

            threPicture = new ImageIcon(thresholding.binarize(grayLoad));
            pictureFrame.setViewportView(new JLabel(threPicture));
            setTitle(algoTitle + " | Otsu's Method");

        }

        if( command.equals("K-means Method") ){

            BufferedImage tempGray = deepCopy(grayLoad);
            KMeansAction means = new KMeansAction(tempGray, 2, thresholding.imageHistogram(tempGray), 2);
            threPicture = new ImageIcon(means.getResultImage());
            pictureFrame.setViewportView(new JLabel(threPicture));
            setTitle(algoTitle + " | K-means Method : 2 Clusters");

        }

        if( command.equals("Simple Method") ){

            threPicture = new ImageIcon(thresholding.dumbThres(grayLoad));
            pictureFrame.setViewportView(new JLabel(threPicture));
            setTitle(algoTitle + " | Simple Method");


        }
        if( command.equals("Mean Method") ){


            threPicture = new ImageIcon(thresholding.mean(grayLoad));
            pictureFrame.setViewportView(new JLabel(threPicture));
            setTitle(algoTitle + " | Otsu's Method");


        }

        if( command.equals("MinMaxAverage Method") ){

            threPicture = new ImageIcon(thresholding.colorMinMaxAverage(grayLoad));
            pictureFrame.setViewportView(new JLabel(threPicture));
            setTitle(algoTitle + " | MinMaxAverage Method");
            
        }
        
        if(command.equals("Gaussian Mixture Model Method")){
           // threPicture = new ImageIcon(thresholding.gaussianMixtureModel(grayLoad));
            pictureFrame.setViewportView(new JLabel(threPicture));
            setTitle(algoTitle + " | Gaussian Mixture Model Method");
        }
        
        if(command.equals("Max Entropy Method")){
            threPicture = new ImageIcon(thresholding.maxEntropy(grayLoad));
            pictureFrame.setViewportView(new JLabel(threPicture));
            setTitle(algoTitle + " | Max Entropy Method");
        }
        
        



    }

}

