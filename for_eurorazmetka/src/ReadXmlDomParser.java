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



public class ReadXmlDomParser {
   public static void main(String[] args) throws IOException {

       /*String name = "Kaggle";
       int humans = 0;
       int boats = 0;
       int heavy_boats = 0;
       int k=44;*/

       for (int num = 1; num <= 300; num++) {

           File file = new File("E:/Work/Nastya_P/VOCdevkit/VOC/Annotations/1 (" + num + ").xml");

           boolean ex = file.exists();
           if (!ex) continue;

           try (FileWriter writer = new FileWriter("E:/Work/Razmetka_final/Train/labels/1 (" + num + ").txt")) {
               File img = new File("E:/Work/Nastya_P/VOCdevkit/VOC/JPEGImages/1 (" + num + ").jpg");
               boolean ex_img = img.exists();
               if (!ex_img) continue;
               BufferedImage bimg = ImageIO.read(img);
               int r_width = bimg.getWidth();
               int r_height = bimg.getHeight();

                   moveFile("E:/Work/Nastya_P/VOCdevkit/VOC/JPEGImages/1 (" + num + ").jpg", "E:/Work/Razmetka_final/Train/images/1 (" + num + ").jpg");


               DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
               DocumentBuilder db = dbf.newDocumentBuilder();
               Document doc = db.parse(new File(String.valueOf(file)));
               doc.getDocumentElement().normalize();
               NodeList list = doc.getElementsByTagName("object");

               for (int i = 0; i < list.getLength(); i++) {
                   Node node = list.item(i);
                   if (node.getNodeType() == Node.ELEMENT_NODE) {
                       Element element = (Element) node.getChildNodes();
                       //String res_width = element.getElementsByTagName("width").item(0).getTextContent();
                       //String res_height = element.getElementsByTagName("height").item(0).getTextContent();
                       String clas = element.getElementsByTagName("name").item(0).getTextContent();
                       String xmin = element.getElementsByTagName("xmin").item(0).getTextContent();
                       String ymin = element.getElementsByTagName("ymin").item(0).getTextContent();
                       String xmax = element.getElementsByTagName("xmax").item(0).getTextContent();
                       String ymax = element.getElementsByTagName("ymax").item(0).getTextContent();
                       if (clas.equals("human")) {

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
                       double x_center = ((x2 + x1) / 2);
                       double y_center = ((y2 + y1) / 2);
                       double width = (abs(x2 - x1));
                       double height = (abs(y2 - y1));
                       if (width >= 6 && height >= 6) {
                                   if (klas == 0) {
                                       if ((x_center > 920 && y_center < 688 && x_center < 1178)  || (y_center < 648) ||  (x_center> 1586 && y_center< 711 && x_center < 1848)) {

                                       }else {
                                           writer.write(klas + " ");
                                           writer.write((x_center / r_width) + " ");
                                           writer.write((y_center / r_height) + " ");
                                           writer.write((width / r_width) + " ");
                                           writer.write((height / r_height) + " ");
                                           writer.write(System.lineSeparator());
                                       }
                                   }else{

                           writer.write(klas + " ");
                           writer.write((x_center / r_width) + " ");
                           writer.write((y_center / r_height) + " ");
                           writer.write((width / r_width) + " ");
                           writer.write((height / r_height) + " ");
                           writer.write(System.lineSeparator());
                           }

                       }
                   }
               }

               System.out.println("Annotation moved From:" + file + " Resolution = " + r_height + " x " + r_width);


           } catch (ParserConfigurationException | SAXException | IOException e) {
               e.printStackTrace();
           }

       }


   }

    private static void moveFile(String src, String dest ) {
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
