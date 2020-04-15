
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PriceType implements Serializable {
    private static File fPriceTypes=  new File("PriceTypes.dat");
    private String name = "";
    private HashMap<String,Float[]> mapPaper = new HashMap<>();
    private HashMap<String,Float[]> mapFilm = new HashMap<>();
    private HashMap<String,Float[]> mapBanner = new HashMap<>();
    private HashMap<String,Float[]> mapSheet = new HashMap<>();
    private HashMap<Layers,Float> mapLayers = new HashMap<>();
    private HashMap<MaterialType,HashMap<String,Float[]>> mapMaterials = new HashMap<>();
    private Float cutPrice = 0f;
    private Float eyeletPrice = 0f;
    private Float laminationPrice = 0f;
    private Float solderingPrice = 0f;

    public enum Layers {
        Layer1, Layer2, Layer3
    }

    public PriceType(String name){
        this.name = name;
        StandardMapMaterialsInitialize();
    }

    private  void  StandardMapMaterialsInitialize(){
        mapPaper.put("city_150",new Float[]{100f,2f});
        mapPaper.put("blueBack",new Float[]{70f,3f});
        mapPaper.put("billBordPaper",new Float[]{50f,22f});
        mapPaper.put("scrollPaper",new Float[]{120f,22f});
        mapSheet.put("pvc_3mm",new Float[]{90f,22f});
        mapSheet.put("pvc_4mm",new Float[]{100f,22f});
        mapSheet.put("akril_pr_3mm",new Float[]{180f,22f});
        mapFilm.put("avery",new Float[]{110f,22f});
        mapFilm.put("promoFilm",new Float[]{90f,22f});
        mapFilm.put("perf",new Float[]{70f,22f});
        mapBanner.put("440",new Float[]{110f,22f});
        mapBanner.put("lam",new Float[]{100f,22f});
        mapLayers.put(Layers.Layer1,150f);
        mapLayers.put(Layers.Layer2,240f);
        mapLayers.put(Layers.Layer3,150f+240f);

        cutPrice = 3f;
        eyeletPrice = 3f;
        laminationPrice = 70f;
        solderingPrice = 5f;

        mapMaterials.put(MaterialType.BANNER,mapBanner);
        mapMaterials.put(MaterialType.FILM,mapFilm);
        mapMaterials.put(MaterialType.SHEET,mapSheet);
        mapMaterials.put(MaterialType.PAPER,mapPaper);

    }

    public HashMap<String,Float[]> getMaterials(MaterialType type){
        return mapMaterials.get(type);
    }
    public String getName(){
        return this.name;
    }
    public Float getSolderingPrice(){
        return this.solderingPrice;
    }
    public Float getLaminationPrice(){
        return laminationPrice;
    }
    public Float getEyeletPrice(){
        return eyeletPrice;
    }
    public Float getCutPrice(){
        return cutPrice;
    }
    public Float getLayerPrice(Layers l){
        return mapLayers.get(l);
    }
    public void setLayerPrice(Layers l,Float price) {
        mapLayers.put(l,price);
    }
    public void setSolderingPrice(Float solderingPrice){
        this.solderingPrice = solderingPrice;
    }
    public void setLaminationPrice(Float laminationPrice){
        this.laminationPrice = laminationPrice;
    }
    public void setCutPrice(Float f){
        cutPrice = f;
    }
    public void setEyeletPrice(Float f){
        this.eyeletPrice = f;
    }
    public static void saveAllPrices(ArrayList<PriceType> arrPrices){
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fPriceTypes));
            for(PriceType pt : arrPrices){
                out.writeObject(pt);
            }
            out.flush();
            out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static ArrayList<PriceType> loadAllPrices() {
        ArrayList<PriceType> arr = new ArrayList<PriceType>();
        try{
            ObjectInputStream in =  new ObjectInputStream(new FileInputStream(fPriceTypes));
            try{
                while(true){
                    arr.add((PriceType)in.readObject());
                }
            }catch(EOFException e){
               //its ok
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        if(arr.size() == 0)
            arr.add(new PriceType("retail"));

        return arr;
    }

}
