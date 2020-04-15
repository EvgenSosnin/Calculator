import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PanelBody extends JPanel {
    private float total = 0f;
    private float totalCut = 0f;
    private float totalEyelet = 0f;
    private float totalRolling = 0f;
    private float eyeletsCount = 0f;
    private float totalLamination = 0f;
    private float totalSoldering = 0f;
    private float totalTucks = 0f;
    private float selectedMaterialEurPrice = 0f;
    private float selectedMaterialUahPrice = 0f;
    private float selectedUfPrintPrice = 0f;
    private float sizeArea = 0f;
    private float EUR;

    private DecimalFormat df = new DecimalFormat("#.##");

    private final MaterialType[] materialsTypes = MaterialType.values();
    private final Printer[] printersName = Printer.values();
    private final HashMap<String,String> mapSide = new HashMap<String,String>(){
        {
            put("perimeter","2:2:2:0");
            put("top-bot","2:0:0:0");
            put("left-right","0:2:0:0");
            put("top-right(top-left)","1:1:1:0");
            put("bot-right(bot-left)","1:1:1:0");
            put("top(bot)","1:0:0:0");
            put("right(left)","0:1:0:0");
        }
    };

    private final ArrayList<PriceType> arrPricesTypes = new ArrayList<>();
    private ArrayList<PrintOrder> arrPrintOrders = new ArrayList<>();

    private PriceType selectedPriceType;

    //private JComponents
    private JComboBox<Printer> cbPrinters = new JComboBox<>(printersName);
    private JComboBox<MaterialType> cbMaterialsType = new JComboBox<>(materialsTypes);
    private JComboBox<String> cbMaterial =  new JComboBox<>();
    private JComboBox<String> cbSize = new JComboBox<>();
    private JComboBox<String> cbMainPrice = new JComboBox<>();
    private JComboBox<String> cbEyeletsSides = new JComboBox<>();
    private JComboBox<String> cbRolling = new JComboBox<>();
    private JComboBox<String> cbTuckSides =  new JComboBox<>();
    private JComboBox<String> cbSoldering = new JComboBox<>(new String[]{"vertical","horizontal","vertical+tucks","horizontal+tucks"});
    private JComboBox<PriceType.Layers> cbLayers = new JComboBox<>(PriceType.Layers.values());

    private JButton btnAddMainPrice = new JButton("add");
    private JButton btnDeleteMainPrice = new JButton("delete");
    private JButton btnAdd = new JButton("add");
    private JButton btnDelete = new JButton("delete");
    private JButton btnChange = new JButton("change price");
    private JButton btnAddSize = new JButton("add");
    private JButton btnDeleteSize = new JButton("delete");
    private JButton btnChangeCutPrice = new JButton("change price");
    private JButton btnChangeEyeletsPrice = new JButton("change price");
    private JButton btnChangeLaminationPrice = new JButton("change price");
    private JButton btnChangeSizeArea = new JButton("change");
    private JButton btnChangeUfLayerPrice = new JButton("change");
    private JButton btnChangeSolderingPrice = new JButton("change");

    private JCheckBox checkCut = new JCheckBox("cut");
    private JCheckBox checkEyelets = new JCheckBox("eyelets");
    private JCheckBox checkLamination = new JCheckBox("lamination");
    private JCheckBox checkRolling  = new JCheckBox("rolling");
    private JCheckBox checkTucks = new JCheckBox("tucks");
    private JCheckBox checkSoldering  = new JCheckBox("soldering");

    private JTextField tfSize = new JTextField();
    private JTextField tfEyeletStep = new JTextField("300");
    private JTextField tfTucksSize = new JTextField("40");
    private JTextField tfSolderingLines = new JTextField("1");

    private JLabel labTotalOut = new JLabel("total : ");
    private JLabel labLayers = new JLabel("Print layers : ");
    private JLabel labCutInfo = new JLabel("");
    private JLabel labEyeletsInfo = new JLabel(getEmptyLabText());
    private JLabel labLamination = new JLabel("");
    private JLabel labRollingPrice = new JLabel(getEmptyLabText());
    private JLabel labMaterialPrice = new JLabel("");
    private JLabel labMaterialUfArea = new JLabel("material area : 0 м²");
    private JLabel labSolderingPrice = new JLabel(getEmptyLabText());


    public PanelBody(float eur){
        this.EUR = eur;
        setLayout(new MigLayout());
        setSize(420,700);

        //load arrPricesTypes
        arrPricesTypes.addAll(PriceType.loadAllPrices());
        fillCheckBoxMainPrice();

        // fill cb's with unimportant information, which would then be overwritten
        cbMaterial.addItem("unimportant");

        //line head
        add(labTotalOut,"span 3 1, wrap");
        add(getBlueButton(),"growx, span 3 1, height ::5, wrap");

        //line 1
        add(new JLabel("Main price is : "));
        add(cbMainPrice,"growx, span 2 1,wrap");
        cbMainPrice.addActionListener(e->{
            String s = cbMainPrice.getSelectedItem().toString();
            for ( PriceType pt : arrPricesTypes){
                if (pt.getName().equals(s))
                    selectedPriceType = pt;
            }
            cbMaterialsType.setSelectedItem(MaterialType.FILM.getName());
            fillCheckBoxMaterial(MaterialType.FILM);
            refreshInfo();
        });

        //line 2
        add(btnAddMainPrice," span 2 1 , gapleft 170, growx");
        btnAddMainPrice.addActionListener(e-> {
                String name = JOptionPane.showInputDialog(null,"New price name: ","Enter new price name",JOptionPane.INFORMATION_MESSAGE);
                PriceType ptNew = new PriceType(name);
                arrPricesTypes.add(ptNew);
                cbMainPrice.addItem(ptNew.getName());
        });
        add(btnDeleteMainPrice, "wrap, growx");
        btnDeleteMainPrice.addActionListener(e ->{
                if (cbMainPrice.getItemCount() > 1){
                    int i = cbMainPrice.getSelectedIndex();
                    cbMainPrice.removeItemAt(i);
                    arrPricesTypes.remove(i);
                }else{
                    JOptionPane.showMessageDialog(null,"Sorry, can not delete last main price!","Error",JOptionPane.INFORMATION_MESSAGE);
                }

        });
        add(getBlueButton(),"growx, span 3 1, height ::5, wrap");

        //line 3
        add(new JLabel("Printer :"));
        add(cbPrinters,"growx, width 100::, span 2 1, wrap");
        cbPrinters.addActionListener(e->{
                JComboBox cb = cbMaterialsType;
                MaterialType mt =(MaterialType) cb.getSelectedItem();
                fillCheckBoxMaterial(mt);
        });

        //line 4
        add(new JLabel("Material type :"));
        fillCheckBoxMaterial(MaterialType.FILM);
        add(cbMaterialsType,"growx, width 100::, top, wrap, span 2 1");
        cbMaterialsType.addActionListener(e->{
                if( cbMaterialsType.getItemCount() > 0)
                    fillCheckBoxMaterial((MaterialType) cbMaterialsType.getSelectedItem());
        });
        add(getBlueButton(),"growx, span 3 1, height ::5, wrap");

        //line 5
        add(labLayers);
        add(cbLayers,"growx");
        cbLayers.addActionListener(e->refreshInfo());
        add(btnChangeUfLayerPrice,"growx, wrap");
        btnChangeUfLayerPrice.addActionListener(e->{
                PriceType.Layers layer =(PriceType.Layers) cbLayers.getSelectedItem();
                    try{
                        String massage = "Input new price for " + ((PriceType.Layers)cbLayers.getSelectedItem()).name() +"\n" +
                                " Old price is: " + selectedPriceType.getLayerPrice(layer);
                        float newPrice =Float.parseFloat(JOptionPane.showInputDialog(massage,JOptionPane.YES_OPTION));
                        selectedPriceType.setLayerPrice(layer,newPrice);
                        refreshInfo();
                    }catch( NumberFormatException er){
                        JOptionPane.showMessageDialog(null,"Wrong input","Error", JOptionPane.ERROR_MESSAGE);
                    }
        });
        add(getBlueButton(),"growx, span 3 1, height ::5, wrap");

        //line6
        add(btnAdd,"growx");
        btnAdd.addActionListener(e->{
                String answerName = JOptionPane.showInputDialog("New material name is : ");
                float answerUAH = 0f,answerEUR = 0f;
                try{
                    if(cbMaterialsType.getSelectedItem() != MaterialType.SHEET){
                        answerUAH =Float.parseFloat( JOptionPane.showInputDialog("New material price is : (UAH) ").replace(",","."));
                    }
                    answerEUR = Float.parseFloat( JOptionPane.showInputDialog("New material price is : (EUR) ").replace(",","."));
                }catch(NumberFormatException er){
                    er.printStackTrace();
                }
                MaterialType mt =(MaterialType) cbMaterialsType.getSelectedItem();

                selectedPriceType.getMaterials(mt).put(answerName,new Float[]{answerUAH,answerEUR});
                fillCheckBoxMaterial(mt);
        });
        add(cbMaterial,"growx, width 100::, top");
        cbMaterial.addActionListener(e->{
                if (cbMaterial.getItemCount() >0)
                    refreshInfo();
        });
        add(btnDelete,"growx, wrap");
        btnDelete.addActionListener(e->{
            if (cbMaterial.getItemCount() == 1){
                JOptionPane.showMessageDialog(null,"Sorry, can not delete last material!","Error",JOptionPane.INFORMATION_MESSAGE);
            }else{
                int deleteIndex = cbMaterial.getSelectedIndex();
                String massage = "Delete "
                        + cbMaterial.getItemAt(deleteIndex) + "?";
                int answer = JOptionPane.showConfirmDialog(null,massage,"",JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION){
                    MaterialType matType =(MaterialType) cbMaterialsType.getSelectedItem();
                    for(MaterialType mt : materialsTypes){
                        if (mt.equals(matType)){
                            selectedPriceType.getMaterials(matType).remove(cbMaterial.getItemAt(deleteIndex));
                            fillCheckBoxMaterial(matType);
                        }
                    }

                }
            }
        });

        //line 7
        add(labMaterialPrice,"growx,width 100::, span 2 1");
        add(btnChange,"growx, wrap");
        btnChange.addActionListener(e->{
                if(cbMaterial.getSelectedIndex() > -1){
                    String mat = cbMaterial.getSelectedItem().toString();
                    MaterialType matType =(MaterialType) cbMaterialsType.getSelectedItem();
                    Float[] arr = selectedPriceType.getMaterials(matType).get(mat);
                    Float answerUAH = arr[0];
                    Float answerEUR = arr[1];

                    String massageUAH = mat + " - " + answerUAH + " \n Enter new price : (UAH)";
                    String massageEUR = mat + " - " + answerEUR + " \n Enter new price : (EUR)";

                    try{
                        if (cbPrinters.getSelectedItem() == Printer.DilyUF){
                            answerEUR =  Float.parseFloat(JOptionPane.showInputDialog(massageEUR).replace(",","."));
                        }else{
                            answerUAH =  Float.parseFloat(JOptionPane.showInputDialog(massageUAH ).replace(",","."));
                        }
                    }catch(NumberFormatException er){
                        JOptionPane.showMessageDialog(null,"Wrong type","Error",JOptionPane.INFORMATION_MESSAGE);
                    }catch (NullPointerException er){
                        er.printStackTrace();
                    }


                    selectedPriceType.getMaterials(matType).put(mat,new Float[]{answerUAH,answerEUR});
                    fillCheckBoxMaterial(matType);
                }
        });
        add(getBlueButton(),"growx, span 3 1, height ::5, wrap");

        //line 8
        JLabel labSize = new JLabel("Size : ");
        labSize.setToolTipText("width(mm) х height(mm) x (.pcs)\n 150mm x 150mm x 2pieces");
        add(labSize);
        add(tfSize,"width 200::");
        tfSize.addActionListener(new ActionBtnAddSize());
        tfSize.setToolTipText("width(mm) х height(mm) x (.pcs)");
        tfSize.setOpaque(false);
        add(btnAddSize,"growx, wrap");
        btnAddSize.addActionListener(new ActionBtnAddSize());

        //line 9
        add(labMaterialUfArea,"growx,span 2 1,gapleft 87");
        add(btnChangeSizeArea,"growx, wrap");
        btnChangeSizeArea.addActionListener(e->{
                try{
                    sizeArea = Float.parseFloat(JOptionPane.showInputDialog("Input new SizeArea (м²) "));
                    labMaterialUfArea.setText("material area : " + sizeArea + " м²");
                    refreshInfo();
                }catch(NumberFormatException er){
                    JOptionPane.showMessageDialog(null,"Wrong type","Erorr",JOptionPane.ERROR_MESSAGE);
                }
        });

        //line 10
        add(cbSize,"growx, width 200::, span 2 1");
        add(btnDeleteSize,"growx, wrap");
        btnDeleteSize.addActionListener(e->{
                if (cbSize.getItemCount() > 0 ){
                    arrPrintOrders.remove(cbSize.getSelectedIndex());
                    cbSize.removeItemAt(cbSize.getSelectedIndex());
                    refreshInfo();
                }
                sizeArea = 0f;
                for(PrintOrder po : arrPrintOrders){
                    sizeArea = sizeArea + ( (po.getSizeH() * po.getSizeW())* po.getCopies());
                }
                labMaterialUfArea.setText("material area : " + sizeArea + " м²");
                refreshInfo();
        });
        add(getBlueButton(),"growx, span 3 1, height ::5, wrap");

        //line 11
        add(checkLamination);
        add(labLamination);
        add(btnChangeLaminationPrice,"wrap");
        btnChangeLaminationPrice.setVisible(false);
        btnChangeLaminationPrice.addActionListener(e->{
                try{
                    selectedPriceType.setLaminationPrice(Float.parseFloat(JOptionPane.showInputDialog("Enter new price : ").replace(",",".")));
                    refreshInfo();
                }catch(NumberFormatException er){
                    JOptionPane.showMessageDialog(null,"Wrong price type","ERROR", JOptionPane.INFORMATION_MESSAGE);
                }
        });
        checkLamination.addItemListener(e->refreshInfo());

        //line 12
        add(checkRolling);
        checkRolling.addActionListener(e->refreshInfo());

        add(cbRolling,"span 2 1,growx, wrap");
        cbRolling.setVisible(false);
        add(labRollingPrice,"span 3 1, wrap");
        cbRolling.addActionListener(e->refreshInfo());
        add(getBlueButton(),"growx, span 3 1, height ::5, wrap");

        //line 13
        add(checkCut);
        checkCut.addItemListener(e->refreshInfo());
        add(labCutInfo);
        add(btnChangeCutPrice,"growx, wrap");
        btnChangeCutPrice.setVisible(false);
        btnChangeCutPrice.addActionListener(e->{
                try{
                    selectedPriceType.setCutPrice(Float.parseFloat(JOptionPane.showInputDialog("Enter new price : ").replace(",",".")));
                    refreshInfo();
                }catch(NumberFormatException er){
                    JOptionPane.showMessageDialog(null,"Wrong price type","Error",JOptionPane.INFORMATION_MESSAGE);
                }
        });
        add(getBlueButton(),"growx, span 3 1, height ::5, wrap");

        //line 14
        add(checkEyelets);
        checkEyelets.addItemListener(e->refreshInfo());
        fillComboBoxSides(cbEyeletsSides);
        add(cbEyeletsSides,"growx");
        cbEyeletsSides.addActionListener(e->refreshInfo());
        add(tfEyeletStep,"growx, wrap");
        tfEyeletStep.setToolTipText("eyelets step .mm");
        tfEyeletStep.addActionListener(e->refreshInfo());

        //line 15
        add(labEyeletsInfo,"span 2 1");
        add(btnChangeEyeletsPrice,"growx, wrap");
        btnChangeEyeletsPrice.addActionListener(e->{
                try{
                    selectedPriceType.setEyeletPrice(Float.parseFloat(JOptionPane.showInputDialog("Enter new price : ").replace(",",".")));
                    refreshInfo();
                }catch(NumberFormatException er){
                    JOptionPane.showMessageDialog(null,"Wrong price type","ERROR",JOptionPane.INFORMATION_MESSAGE);
                }
        });

        //line 16
        add(checkTucks);
        checkTucks.addActionListener(e->refreshInfo());
        add(cbTuckSides,"growx");
        cbTuckSides.addActionListener(e->refreshInfo());
        add(tfTucksSize,"growx,wrap");
        tfTucksSize.setOpaque(false);
        tfTucksSize.setToolTipText("tusks size .mm");
        tfTucksSize.addActionListener(e-> refreshInfo());
        fillComboBoxSides(cbTuckSides);

        //line 17
        add(checkSoldering,"growx");
        checkSoldering.addActionListener(e->refreshInfo());
        add(cbSoldering,"growx");
        cbSoldering.addActionListener(e->refreshInfo());
        add(tfSolderingLines,"growx, wrap");
        tfSolderingLines.addActionListener(e->refreshInfo());
        tfSolderingLines.setOpaque(false);

        //line 18
        add(labSolderingPrice,"span 2 1");
        add(btnChangeSolderingPrice,"growx, wrap");
        btnChangeSolderingPrice.addActionListener(e->{
            String massage = "Soldering price is " + selectedPriceType.getSolderingPrice() + " UAH\n" +
                    "Input new Soldering price";
            try{
                selectedPriceType.setSolderingPrice(Float.parseFloat(JOptionPane.showInputDialog(massage)));
                refreshInfo();
            }catch(NumberFormatException exp){
                JOptionPane.showMessageDialog(null,"Wrong price type!","Error",JOptionPane.ERROR_MESSAGE);
            }

        });

        //final settings
        refreshInfo();

    }

    public ArrayList<PriceType> getArrPricesTypes() {
        return arrPricesTypes;
    }
    public void fillCheckBoxMaterial(MaterialType mt){
        cbMaterial.removeAllItems();
        HashMap<String,Float[]> arr = selectedPriceType.getMaterials(mt);

        for (Map.Entry<String,Float[]> entry : arr.entrySet()){
            cbMaterial.addItem(entry.getKey());
        }

        refreshPriceInfo();
        refreshLayers();
    }
    public JButton getBlueButton(){
        JButton bb = new JButton("");
        bb.setBackground( new Color(27,27,209));
        bb.setSize(1,200);
        bb.setEnabled(false);
        return bb;
    }
    private Float getEyeletsCount(){
        float count = 0;
        float step;
        try{
            step = Float.parseFloat( tfEyeletStep.getText() );
            if (step <= 0) return count;
        }catch(NumberFormatException  e){
            return count;
        }

        String[] arrIncrement = mapSide.get(cbEyeletsSides.getSelectedItem()).split(":");
        float x = Float.parseFloat(arrIncrement[0]), y = Float.parseFloat(arrIncrement[1]);
        float incrementX = Float.parseFloat(arrIncrement[2]),incrementY = Float.parseFloat(arrIncrement[3]);

        for(PrintOrder po : arrPrintOrders){
            float h = po.getSizeH() *po.getMaterialConvertor() -40;
            float w = po.getSizeW() *po.getMaterialConvertor() -40;
            float copies = po.getCopies();
            if (h<step) h = step;
            if (w<step) w = step;
            h= Math.round(h /step) +1 -incrementY;
            w= Math.round(w /step) +1 -incrementX;
            if( h <1) h = 0;
            if (w <1) w = 0;
            h *= y;
            w *= x;
            count += (h +w) *copies;
        }
        return count;
    }
    private String getEmptyLabText(){
        return " .  .  .  .  .  .  .  .  .  .  .  .  .  . " +
                " .  .  .  .  .  .  .  .  .  .  .  .  .  .  . " +
                " .  .";
    }
    private void fillComboBoxSides(JComboBox<String> cb){
        for(Map.Entry<String,String> entry : mapSide.entrySet()){
            cb.addItem(entry.getKey());
        }

        cb.setSelectedItem("perimeter");

    }
    private void fillCheckBoxMainPrice(){
        for(PriceType pt : arrPricesTypes ){
            cbMainPrice.addItem(pt.getName());
        }
        selectedPriceType = arrPricesTypes.get(0);
    }
    private void refreshInfo(){
        refreshLayers();
        refreshPriceInfo();
        refreshLaminationInfo();
        refreshEyeletInfo();
        refreshCutInfo();
        refreshRollingInfo();
        refreshTucksInfo();
        refreshSolderingInfo();
        refreshTotal();
    }
    private void refreshLayers(){
        if(cbPrinters.getSelectedItem() == Printer.DilyUF){
            cbLayers.setEnabled(true);
            PriceType.Layers layer =(PriceType.Layers) cbLayers.getSelectedItem();
            selectedUfPrintPrice = selectedPriceType.getLayerPrice(layer);
            btnChangeUfLayerPrice.setVisible(true);
        }else{
            cbLayers.setSelectedItem(PriceType.Layers.Layer1);
            cbLayers.setEnabled(false);
            btnChangeUfLayerPrice.setVisible(false);
        }
    }
    private void refreshPriceInfo(){
        Float[] arr =  selectedPriceType.getMaterials(((MaterialType)cbMaterialsType.getSelectedItem())).
                get(cbMaterial.getSelectedItem());
        String strUf = "";
        if (arr != null){
            selectedMaterialUahPrice = arr[0];
            selectedMaterialEurPrice = arr[1];
            if(((Printer)cbPrinters.getSelectedItem()).equals(Printer.DilyUF)){
                selectedMaterialUahPrice = selectedMaterialEurPrice * EUR;
                strUf =  " | " + selectedUfPrintPrice + " UF";
            }
            labMaterialPrice.setText(selectedMaterialUahPrice+ " UAH | " + selectedMaterialEurPrice +" EUR" + strUf);
        }
    }
    private void refreshLaminationInfo(){
        totalLamination = 0;
        if(checkLamination.isSelected()){
            totalLamination = selectedPriceType.getLaminationPrice();
            labLamination.setText("price is : " + totalLamination);
            btnChangeLaminationPrice.setVisible(true);
        }else{
            labLamination.setText("");
            btnChangeLaminationPrice.setVisible(false);
        }
    }
    private void refreshEyeletInfo(){
        totalEyelet = 0;
        eyeletsCount = 0;
        boolean visible;
        if (checkEyelets.isSelected()){
            eyeletsCount = getEyeletsCount();
            visible = true;
            labEyeletsInfo.setText("eyelets price:" + selectedPriceType.getEyeletPrice() + " | count: " + eyeletsCount);
            totalEyelet = eyeletsCount * selectedPriceType.getEyeletPrice();
        }else{
            visible = false;
            labEyeletsInfo.setText(getEmptyLabText());
        }
        cbEyeletsSides.setVisible(visible);
        btnChangeEyeletsPrice.setVisible(visible);
        tfEyeletStep.setVisible(visible);
    }
    private void refreshRollingInfo(){
        totalRolling = 0;
        cbRolling.removeAllItems();

        if (checkRolling.isSelected()){

            for(Map.Entry<String,Float[]> entry: selectedPriceType.getMaterials(MaterialType.SHEET).entrySet()){
                cbRolling.addItem(entry.getKey());
            }

            try{
                float poArea = 0f;
                for(PrintOrder po : arrPrintOrders){
                    poArea = poArea + ( (po.getSizeH() * po.getSizeW())* po.getCopies());
                }
                Float[] arrPrices = selectedPriceType.getMaterials(MaterialType.SHEET).get(cbRolling.getSelectedItem());
                totalRolling = ((arrPrices[1] * EUR) * sizeArea) + (poArea * selectedPriceType.getLaminationPrice()  );
                labRollingPrice.setText("Material price: " + df.format(arrPrices[1] * EUR) + " UAH | " +
                        "LamPrice: " + df.format(selectedPriceType.getLaminationPrice()) + " UAH | " +
                        "Area: " + df.format(poArea) + " м²");
                cbRolling.setVisible(true);
            }catch(NullPointerException e){
                labRollingPrice.setText(getEmptyLabText());
                e.printStackTrace();
            }

        }else{
            cbRolling.setVisible(false);
            labRollingPrice.setText(getEmptyLabText());
        }
    }
    private void refreshCutInfo(){
        float cutPrice = selectedPriceType.getCutPrice();
        totalCut=0;
        float poLength;
        float poTotalLength = 0f;
        if (checkCut.isSelected()){
            for(PrintOrder po : arrPrintOrders){
                poLength = ((po.getSizeH() * 2) +  (po.getSizeW() *2))* po.getCopies();
                poTotalLength +=poLength;
                totalCut = totalCut + (poLength *cutPrice);
            }
            labCutInfo.setText("Length: " + df.format(poTotalLength) + " m" + " | Price: " + cutPrice);
            btnChangeCutPrice.setVisible(true);
        }else{
            btnChangeCutPrice.setVisible(false);
            labCutInfo.setText("");
        }
    }
    private void refreshTotal(){
        float price;
        boolean checkUF = cbPrinters.getSelectedItem().equals(Printer.DilyUF);
        if(checkUF){
            price = selectedUfPrintPrice;
        }else{
            price = selectedMaterialUahPrice;
        }
        price += totalLamination;

        total = 0f;
        float localSizeArea = 0f;
        for(PrintOrder po : arrPrintOrders){
            localSizeArea = localSizeArea + ( (po.getSizeH() * po.getSizeW())* po.getCopies());
        }
        total = localSizeArea * price;
        if(checkUF){
            total = total + (this.sizeArea * selectedMaterialUahPrice);
        }
        labTotalOut.setText("total : " + df.format((total + totalCut + totalEyelet + totalRolling + totalTucks + totalSoldering)));
    }
    private void refreshTucksInfo(){
        totalTucks = 0f;
        boolean visible = false;
        if(checkTucks.isSelected()){
            visible= true;
            float tuckSize;
            try{
                tuckSize = Float.parseFloat( tfTucksSize.getText() );
                if (tuckSize <= 0) return;

                String[] arrIncrement = mapSide.get(cbTuckSides.getSelectedItem()).split(":");
                float x = Float.parseFloat(arrIncrement[0]), y = Float.parseFloat(arrIncrement[1]);

                for(PrintOrder po : arrPrintOrders){
                    float copies = po.getCopies();
                    float h = ((po.getSizeH() * (tuckSize / po.getMaterialConvertor())) * y) * copies;
                    float w = ((po.getSizeW() * (tuckSize / po.getMaterialConvertor())) * x) * copies;
                    totalTucks += (h + w) * (selectedMaterialEurPrice * EUR);
                    //System.out.println("totalTucks: " + totalTucks + " | h: " + h + " | w: " + w + " | selectedMaterialEurPrice: " + selectedMaterialEurPrice * EUR);
                }
            }catch(NumberFormatException  e){
                return;
            }


        }
        cbTuckSides.setVisible(visible);
        tfTucksSize.setVisible(visible);
    }
    private void refreshSolderingInfo(){
        totalSoldering = 0f;
        float price = selectedPriceType.getSolderingPrice();
        boolean visible = false, isTucks = cbTuckSides.isVisible(), needTuck = false;
        try{
            float linesCount =Float.parseFloat(tfSolderingLines.getText());
            if(checkSoldering.isSelected()){
                visible = true;

                String type =(String) cbSoldering.getSelectedItem();
                needTuck = type.contains("+");
                if(!isTucks & needTuck) cbTuckSides.setVisible(true);

                float area = 0f;
                String[] arrIncrement = mapSide.get(cbTuckSides.getSelectedItem()).split(":");
                float x = Float.parseFloat(arrIncrement[0]), y = Float.parseFloat(arrIncrement[1]);

                for(PrintOrder po : arrPrintOrders) {
                    if (type.contains("vertical")) {
                        area += po.getSizeH() * po.getCopies() * linesCount;
                    } else {
                        area += po.getSizeW() * po.getCopies() * linesCount;
                    }
                    if (needTuck) {
                        area += (po.getSizeH() * y) * po.getCopies();
                        area += (po.getSizeW() * x) * po.getCopies();
                    }
                }

                totalSoldering = area * price;
                labSolderingPrice.setText("price: " + df.format(price) + "  |  soldering length: " +  df.format(area) + " m  |  total: " + df.format(totalSoldering));
            }else{
                labSolderingPrice.setText(getEmptyLabText());
            }
        }catch(NumberFormatException exp){
            tfSolderingLines.setText("0");
        }

        if(!isTucks) cbTuckSides.setVisible(needTuck);
        cbSoldering.setVisible(visible);
        btnChangeSolderingPrice.setVisible(visible);
        tfSolderingLines.setVisible(visible);
    }

    private class ActionBtnAddSize implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String[] arr = tfSize.getText().replace(",",".").split("[x х*]");
            if(arr.length ==3 || (arr.length ==2)){
                try{
                    float sizeW = Float.parseFloat(arr[0]);
                    float sizeH = Float.parseFloat(arr[1]);
                    int copies = 1;
                    if(arr.length == 3) copies = Integer.parseInt(arr[2]);
                    PrintOrder printOrder= new PrintOrder(sizeW,sizeH,copies);
                    arrPrintOrders.add(printOrder);
                    cbSize.addItem(printOrder.toString());
                    cbSize.setSelectedItem(printOrder.toString());
                    tfSize.setText("");

                    sizeArea = 0f;
                    for(PrintOrder po : arrPrintOrders){
                        sizeArea = sizeArea + ( (po.getSizeH() * po.getSizeW())* po.getCopies());
                    }
                    labMaterialUfArea.setText("material area : " + sizeArea + " м²");

                    refreshTotal();
                }catch(NumberFormatException er){
                    JOptionPane.showMessageDialog(null,"Wrong Format","Wrong format",JOptionPane.ERROR_MESSAGE);
                    tfSize.setText("");
                }
                refreshInfo();
            }
        }
    }
}