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


public class ReadXmlDomParser {
    public static void main(String[] args){
        for (int num = 1; num < 10000; num++) {

            String From_Path = "E:/Work/train/1 (" + num + ")";
            File file = new File(From_Path + ".xml"); //Input .xml file
            File img = new File(From_Path + ".jpg");  //Input .jpg file
            String Tag = "test"; // Pattern name of output file
            String To_Path_label = "C:/labled/Train/labels/" + Tag + "_" + num + ".txt"; // (for example C:/labled/Train/labels/test_(number).txt)
            String To_Path_image = "C:/labled/Train/images/" + Tag + "_" + num + ".jpg"; // (for example C:/labled/Train/images/test_(number).jpg)
            boolean ex_file = file.exists();
            if (!ex_file) continue;  // Check for existing xml file
            boolean ex_img = img.exists();
            if (!ex_img) continue;  // Check for existing jpg file

            Writer(file,img,To_Path_label,To_Path_image);

        }
    }

        public static void Writer(File file, File img, String To_Path_label, String To_Path_image){

        try (FileWriter writer = new FileWriter(To_Path_label)) {

            BufferedImage bimg = ImageIO.read(img);
            int r_width = bimg.getWidth();
            int r_height = bimg.getHeight();
            moveFile (String.valueOf(img), To_Path_image);

            NodeList list = GET_OBJ_FROM_XML(file);

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node.getChildNodes();

                    String clas = element.getElementsByTagName("name").item(0).getTextContent();
                    String xmin = element.getElementsByTagName("xmin").item(0).getTextContent();
                    String ymin = element.getElementsByTagName("ymin").item(0).getTextContent();
                    String xmax = element.getElementsByTagName("xmax").item(0).getTextContent();
                    String ymax = element.getElementsByTagName("ymax").item(0).getTextContent();

                    String ccls = Change_class(clas);

                    if (!ccls.equals("SUS")) {
                        int klas = Integer.parseInt(ccls);
                        double x1 = Double.parseDouble(xmin);
                        double y1 = Double.parseDouble(ymin);
                        double x2 = Double.parseDouble(xmax);
                        double y2 = Double.parseDouble(ymax);
                        double x_center = ((x2 + x1) / 2);
                        double y_center = ((y2 + y1) / 2);
                        double width = (abs(x2 - x1));
                        double height = (abs(y2 - y1));

                        if (width >= 6 && height >= 6) {
                            if (((x_center / r_width) > 0) && ((y_center / r_height) > 0) && ((width / r_width) > 0) && ((height / r_height) > 0) && ((x_center / r_width) < 1) && ((y_center / r_height) < 1) && ((width / r_width) < 1) && ((height / r_height) < 1)) {
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
            }
            System.out.println("Annotation moved From:" + file + " Resolution = " + r_height + " x " + r_width);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void moveFile(String src, String dest) {
        Path result = null;
        try {
            result = Files.move(Paths.get(src), Paths.get(dest));
        } catch (IOException e) {
            System.out.println("Exception while moving file: " + e.getMessage());
        }
        if (result != null) {
            System.out.println("File moved from" + src + " To " + dest + "   Success");
        } else {
            System.out.println("File movement from" + src + " To " + dest + "  Failed.");
        }
    }
    public static NodeList GET_OBJ_FROM_XML(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(String.valueOf(file)));
        doc.getDocumentElement().normalize();
        NodeList list = doc.getElementsByTagName("object");
        return list;
    }
    public static String Change_class(String clas) {
        if (clas.equals("human")) {

            clas = clas.replace("human", "0");
        } else {
            if (clas.equals("person")) {

                clas = clas.replace("person", "0");
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
                                    if (clas.equals("cyclist")) {

                                        clas = clas.replace("cyclist", "1");
                                    } else {
                                        if (clas.equals("car")) {

                                            clas = clas.replace("car", "2");
                                        } else {
                                            if (clas.equals("motorcycle")) {

                                                clas = clas.replace("motorcycle", "3");

                                            } else {
                                                if (clas.equals("motorcyle")) {

                                                    clas = clas.replace("motorcyle", "3");

                                                } else {
                                                    if (clas.equals("bike")) {

                                                        clas = clas.replace("bike", "1");

                                                    } else {
                                                        if (clas.equals("bus")) {

                                                            clas = clas.replace("bus", "4");
                                                        } else {
                                                            if (clas.equals("buss")) {

                                                                clas = clas.replace("buss", "4");
                                                            } else {
                                                                if (clas.equals("truck")) {

                                                                    clas = clas.replace("truck", "5");
                                                                } else {
                                                                    if (clas.equals("track")) {

                                                                        clas = clas.replace("track", "5");
                                                                    } else {
                                                                        if (clas.equals("Van")) {

                                                                            clas = clas.replace("Van", "5");
                                                                        } else {
                                                                            if (clas.equals("Bolan")) {

                                                                                clas = clas.replace("Bolan", "5");
                                                                            } else {
                                                                                if (clas.equals("dog")) {

                                                                                    clas = clas.replace("dog", "7");
                                                                                } else {

                                                                                    System.out.println("????????????????????" + clas);
                                                                                    clas = "SUS";
                                                                                    //System.exit(0);
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
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return clas;
    }
}
