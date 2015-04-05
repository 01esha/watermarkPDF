/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package watermarkpdf;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 * @author olandol
 */
public class FXMLDocumentController implements Initializable {

    @FXML private TextField tfPhrase;
    @FXML private TextField tf_append;
    @FXML private TextField tf_PagePrint;
    @FXML private TextField tf_PathFile;
    @FXML private Label lbl_Result;
    @FXML private CheckBox cbox_LU;
    @FXML private Button btn_Go;
    
    

    @FXML
    private void handleButtonAction(ActionEvent event) throws DocumentException, IOException {
        if (tfPhrase.getLength() > 0) {
            lbl_Result.setText("");
            PdfReader reader = new PdfReader(tf_PathFile.getText());        
             Rectangle mediabox = reader.getPageSize(1);        
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(tf_PathFile.getText().substring(0,tf_PathFile.getLength()-4)+"_out.pdf"));
            stamper.setRotateContents(false);
            BaseFont bf = BaseFont.createFont("TNR.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf);
            int iPos = tfPhrase.getText().indexOf('#');            
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                PdfContentByte canvas = stamper.getOverContent(i);
                if (iPos >= 0) {                    
                    if (cbox_LU.isSelected()) {
                        switch (i) {
                            case 1:
                                ColumnText.showTextAligned(canvas,
                                        Element.ALIGN_LEFT, new Phrase((tfPhrase.getText().substring(0, iPos - 5) 
                                                + "Лист утверждения " 
                                                + tfPhrase.getText().substring(iPos + 1)), font), mediabox.getRight()/3, mediabox.getTop()-40, 0);
                                break;
                            case 2:
                                ColumnText.showTextAligned(canvas,
                                        Element.ALIGN_LEFT, new Phrase((tfPhrase.getText().substring(0, iPos - 5) 
                                                + "Титульный лист " 
                                                + tfPhrase.getText().substring(iPos + 1)), font), mediabox.getRight()/3, mediabox.getTop()-40, 0);
                                break;
                            default:
                                ColumnText.showTextAligned(canvas,
                                        Element.ALIGN_LEFT, new Phrase((tfPhrase.getText().substring(0, iPos) 
                                                + (i - 1) 
                                                + tfPhrase.getText().substring(iPos + 1)), font), mediabox.getRight()/3, mediabox.getTop()-40, 0);
                        }
                    } else {
                        if (i==1)
                                ColumnText.showTextAligned(canvas,
                                        Element.ALIGN_LEFT, new Phrase((tfPhrase.getText().substring(0, iPos - 5) 
                                                + "Титульный лист " 
                                                + tfPhrase.getText().substring(iPos + 1)), font), mediabox.getRight()/3, mediabox.getTop()-40, 0);
                        else
                                ColumnText.showTextAligned(canvas,
                                        Element.ALIGN_LEFT, new Phrase((tfPhrase.getText().substring(0, iPos) + (i) 
                                                + tfPhrase.getText().substring(iPos + 1)), font), mediabox.getRight()/3, mediabox.getTop()-40, 0);
                    }

                } else {
                   ColumnText.showTextAligned(canvas,
                        Element.ALIGN_LEFT, new Phrase(tfPhrase.getText(), font), mediabox.getRight()/3, mediabox.getTop()-40, 0);
                }                
                 ColumnText.showTextAligned(canvas,
                            Element.ALIGN_LEFT, new Phrase(tf_append.getText(), font), mediabox.getRight()*2/3, mediabox.getTop()-20, 0);
            }                
            stamper.close();
            reader.close();
             if (tf_PagePrint.getLength()>0){
             reader = new PdfReader(tf_PathFile.getText().substring(0,tf_PathFile.getLength()-4)+"_out.pdf");
             reader.selectPages(tf_PagePrint.getText());
             stamper = new PdfStamper(reader, new FileOutputStream(tf_PathFile.getText().substring(0,tf_PathFile.getLength()-4)+"_out_cut.pdf"));             
             stamper.close();
             reader.close();
            }
            lbl_Result.setText("Обработка документа завершена");
        }
    }

    @FXML
    private void ButtonOpenFile(ActionEvent event){
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть файл");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );        
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF файлы", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            tf_PathFile.setText(selectedFile.getPath());
            btn_Go.setDisable(false);
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
