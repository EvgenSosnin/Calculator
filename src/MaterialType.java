import javax.print.attribute.standard.SheetCollate;

public enum MaterialType {
    FILM("film"),
    BANNER("banner"),
    SHEET("sheet"),
    PAPER("paper");

    private String name;

    MaterialType(String name) {
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public static MaterialType getMaterialType(String name){
        if (name.equals("film")){
            return FILM;
        }else if (name.equals("banner")){
            return BANNER;
        }else if (name.equals("paper")){
            return PAPER;
        }else{
            return SHEET;
        }
    }
}
