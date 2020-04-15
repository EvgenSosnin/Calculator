public class PrintOrder {
    private float sizeW;
    private float sizeH;
    private int copies;
    private float metersConvertor = 1000f;

    public PrintOrder(float sizeW , float sizeH , int copies){
        this.copies = copies;
        this.sizeH = sizeH;
        this.sizeW = sizeW;
    }

    public float getMaterialConvertor(){
        return this.metersConvertor;
    }
    public float getSizeW() {
        return sizeW / metersConvertor;
    }
    public float getSizeH() {
        return sizeH / metersConvertor;
    }
    public int getCopies() {
        return copies;
    }

    @Override
    public String toString() {
        return sizeW + "mm x " + sizeH + "mm | " + copies + " .pcs";
    }

}
