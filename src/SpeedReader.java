import java.io.IOException;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.text.AttributedString;
import javax.swing.JOptionPane;
import java.lang.Math.*;

public class SpeedReader{

  /**
   * Set up the Graphics
   * @param width width of the speedReader Interface
   * @param height height of the speedReader Interface
   * @param fontSize font size
   * @param wpm words per minute the speedReader will display
   * @return Graphics
   */
  public static Graphics displayPanel(int width, int height, int fontSize) {
      DrawingPanel panel = new DrawingPanel(width, height);
      Graphics g = panel.getGraphics();
      Font f = new Font("Courier", Font.BOLD, fontSize);
      g.setFont(f);
      return g;
  }


  /**
   * Display the Panel
   * @param fileName file suppose to be displayed
   * @param width width of the speedReader
   * @param height height of the speedReader
   * @param fontSize font size
   * @param interval the interval between two words, in milliseconds
   * @throws InterruptedException if thread.sleep goes run, it will throw out hte InterruptedException
   * @throws IOException if the file cannot be found, it will throw out the IOException
   */
  public static void Panel(String fileName,int width, int height, int font_size, int interval) throws InterruptedException, IOException
  {
    Scanner text = new Scanner(new File(fileName));
    int wordsInSentence=0;
    ArrayList<String> words=new ArrayList<String>();//All the words in the file
    ArrayList<Integer> wordsCount=new ArrayList<Integer>();//the number of words in each sentence

    //Read all the words in the file
    while(text.hasNext()){
      String[] line=text.nextLine().split(" ");
      Collections.addAll(words,line);
      for (String str:line){
        wordsInSentence+=1;
        char ch=str.charAt(str.length()-1);
        if(ch=='.' || ch=='?' || ch=='!'){
          wordsCount.add(wordsInSentence);
          wordsInSentence=0;
        }//if
      }//for
    }//while



    Graphics g = displayPanel(width, height, font_size);
    // get metrics from the graphics
    FontMetrics metrics = g.getFontMetrics(g.getFont());
    int hgt = metrics.getHeight();


    g.setColor(Color.WHITE);
    g.drawLine(0,height/2+hgt,width,height/2+hgt);
    g.drawLine(0,height/2-hgt,width,height/2-hgt);

    int wordsCovered=0;

    for (int i=0;i<wordsCount.size();i++){
      int currentSentence=wordsCount.get(i);
      for(int j=0;j<wordsCount.get(i);j++){
        g.clearRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.drawLine(0,height/2+hgt*2,width,height/2+hgt*2);
        g.drawLine(0,height/2-hgt*2,width,height/2-hgt*2);
        g.drawLine(width/2-metrics.stringWidth("1")/2,height/2+hgt*2,width/2-metrics.stringWidth("1")/2,height/2+hgt*1);
        g.drawLine(width/2-metrics.stringWidth("1")/2,height/2-hgt*2,width/2-metrics.stringWidth("1")/2,height/2-hgt*1);
        //Display this is a long sentence.
        if(currentSentence>10){
          g.setColor(Color.RED);
          g.drawString("Long Sentence Warning!", width/3, height/4);
        }//if

        String str=words.get(wordsCovered+j);
        // get the advance of my text in this font and render context
        int length=str.length();
        String str1=str.substring(0,Math.max(0,(int)length/2-1));
        int length1 = metrics.stringWidth(str1);
        String str2=str.substring(Math.max(0,(int)length/2-1),(int)length/2);
        int length2 = metrics.stringWidth(str2);
        String str3=str.substring((int)length/2,(int)length);
        g.setColor(Color.WHITE);
        g.drawString(str1, width/2-length1-length2, height/2);
        g.setColor(Color.RED);
        g.drawString(str2, width/2-length2, height/2);
        g.setColor(Color.WHITE);
        g.drawString(str3, width/2, height/2);
        //Slow down the diplay if the current sentence has more than 10 words
        if(currentSentence>10){
          Thread.sleep(interval*2);
        }
        else
          Thread.sleep(interval);
      }//for
      wordsCovered+=wordsCount.get(i);
    }//for
  }//Panel


  /**
   *
   * @param args
   * @throws InterruptedException if thread.sleep goes run, it will throw out hte InterruptedException
   * @throws IOException if the file cannot be found, it will throw out the IOException
   */
  public static void main(String args[]) throws InterruptedException, IOException{
    String fileName=JOptionPane.showInputDialog("Enter the filename you are gonna read:");
    int width = Integer.parseInt(JOptionPane.showInputDialog("Enter the width of your interface:"));
    int height = Integer.parseInt(JOptionPane.showInputDialog("Enter the height of your interface:"));
    int font_size = Integer.parseInt(JOptionPane.showInputDialog("Enter the font:"));
    int wpm=Integer.parseInt(JOptionPane.showInputDialog("Enter the speed(word per minute):"));
    int interval = (int) (1.0 / wpm * 60 * 1000);

    SpeedReader obj=new SpeedReader();

    obj.Panel(fileName,width,height,font_size,interval);
    System.out.println("end");
  }

}
