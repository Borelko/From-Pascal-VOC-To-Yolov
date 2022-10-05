import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.lang.Math.abs;
import java.nio.file.Paths;
import java.util.regex.*;

public class Xml_To_Txt {
   public static void main(String[] args) throws IOException {

           File file = new File("*.xml"); // Choose Path with your Xml file

           boolean ex = file.exists("*.xml"); // Checking if file exists
           if (!ex) continue;

           try (FileWriter writer = new FileWriter("*.txt")) { // Initialize FileWriter and indicate Path to save Txt file
               File img = new File("*.png"); // Choose Path with your image file 
               boolean ex_img = img.exists(); // Checking if file exists
               if (!ex_img) continue;
               BufferedImage bimg = ImageIO.read(img); // Getting width and height of your image
               int r_width = bimg.getWidth();
               int r_height = bimg.getHeight();

                   moveFile("*.png", "*.jpg"); // Moving image


               DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
               DocumentBuilder db = dbf.newDocumentBuilder();
               Document doc = db.parse(new File(String.valueOf(file)));
               doc.getDocumentElement().normalize();
               NodeList list = doc.getElementsByTagName("object"); // Choosing branch in Xml file

               for (int i = 0; i < list.getLength(); i++) {
                   Node node = list.item(i);
                   if (node.getNodeType() == Node.ELEMENT_NODE) {
                       Element element = (Element) node.getChildNodes();
                       String clas = element.getElementsByTagName("name").item(0).getTextContent(); // Getting your parameter
                       String xmin = element.getElementsByTagName("xmin").item(0).getTextContent(); // Getting your parameter
                       String ymin = element.getElementsByTagName("ymin").item(0).getTextContent(); // Getting your parameter
                       String xmax = element.getElementsByTagName("xmax").item(0).getTextContent(); // Getting your parameter
                       String ymax = element.getElementsByTagName("ymax").item(0).getTextContent(); // Getting your parameter

                       if (clas.equals("human")) { // Changing classes in YOLO format

                           clas = clas.replace("human", "0");
                       } else {
                           if (clas.equals("boat")) {

                               clas = clas.replace("boat", "6");
                           } else {
                               if (clas.equals("hiddenboat")) {

                                   clas = clas.replace("hiddenboat", "6");
                               } else {
                                   if (clas.equals("heavy boat")) {

                                       clas = clas.replace("heavy boat", "6");
                                   } else {
                                       if (clas.equals("heavy_boat")) {

                                           clas = clas.replace("heavy_boat", "6");
                                       } else {
                                           if (clas.equals("bicycle")) {

                                               clas = clas.replace("bicycle", "1");
                                           } else {
                                               if (clas.equals("car")) {

                                                   clas = clas.replace("car", "2");
                                               } else {
                                                   if (clas.equals("motorcycle")) {

                                                       clas = clas.replace("motorcycle", "3");
                                                   } else {
                                                       if (clas.equals("bus")) {

                                                           clas = clas.replace("bus", "4");
                                                       } else {
                                                           if (clas.equals("track")) {

                                                               clas = clas.replace("track", "5");
                                                           } else {
                                                               if (clas.equals("dog")) {

                                                                   clas = clas.replace("dog", "7");
                                                               } else {
                                                                   System.out.println(file);
                                                                   System.exit(0);
                                                               }
                                                           }
                                                       }
                                                   }
                                               }
                                           }
                                       }
                                   }
                               }
                           }
                       }

                       int klas = Integer.parseInt(clas);
                       double x1 = Double.parseDouble(xmin);
                       double y1 = Double.parseDouble(ymin);
                       double x2 = Double.parseDouble(xmax);
                       double y2 = Double.parseDouble(ymax);
                       double x_center = ((x2 + x1) / 2); // count x_center
                       double y_center = ((y2 + y1) / 2); // count y_center
                       double width = (abs(x2 - x1)); // count width
                       double height = (abs(y2 - y1)); // count height
                       if (width >= 6 && height >= 6) {
                           writer.write(klas + " "); // writing klas
                           writer.write((x_center / r_width) + " "); // writing x center
                           writer.write((y_center / r_height) + " "); // writing y center
                           writer.write((width / r_width) + " "); // Writing width
                           writer.write((height / r_height) + " "); // Writing height
                           writer.write(System.lineSeparator()); // Move Writer to the next line
                       }
                   }
               }

               System.out.println("Annotation moved From:" + file + \n + " To :" + writer + " Resolution = " + r_height + " x " + r_width); // Print information about process

           } catch (ParserConfigurationException | SAXException | IOException e) {
               e.printStackTrace();
           }
   }

    private static void moveFile(String src, String dest ) { // Moving script
        Path result = null;
        try {
            result =  Files.move(Paths.get(src), Paths.get(dest));
        } catch (IOException e) {
            System.out.println("Exception while moving file: " + e.getMessage());
        }
        if(result != null) {
            System.out.println("File moved from" + src +" To " + dest + "   Success");
        }else{
            System.out.println("File movement from" + src +" To " + dest +"  Failed.");
        }
    }
