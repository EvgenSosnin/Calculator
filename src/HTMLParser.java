import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import javax.swing.JOptionPane;
import java.io.IOException;

public class HTMLParser {
    private static String url = "https://promdesign.ua";
    private static float EUR = -1f;
    private HTMLParser(){

    }
    public static float getEUR_value(){
        if(EUR == -1f){
            Document doc;
            try {
                doc = Jsoup.connect(url).get();
                String value = doc.body().getElementsByClass("head-curency_value").first().text();
                EUR = Float.valueOf(value.replace(" UAH",""));
            } catch (NumberFormatException | IOException e) {
                EUR = 0f;
                while(EUR == 0f){
                        try{
                            EUR =Float.valueOf(JOptionPane.showInputDialog("Cant read EUR price (URL : " + url + ")\n Input current EUR price").replace(",","."));
                        }catch(NumberFormatException ee){
                            ee.printStackTrace();
                        }
                }
            }
        }
      return EUR;
    }
    public static String getUrl(){
        return url;
    }

}
