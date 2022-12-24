/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import Connection.ConnectionPool;
import Interface.PrintReport;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
/**
 *
 * @author ramos
 */
public class PrintReportCtr implements ActionListener{
    PrintReport printnui;
    public PrintReportCtr(){
        printnui = new PrintReport();
        printnui.btnImprimir.addActionListener(this);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.printnui.btnImprimir){
            if(this.printnui.jCheckBox1.isSelected()){
              Writer("1");
            }
            if(this.printnui.jCheckBox2.isSelected()){
              Writer("2");
            }
            if(this.printnui.jCheckBox3.isSelected()){
              Writer("3");
            }
            if(this.printnui.jCheckBox4.isSelected()){
              Writer("4");
            }
            if(this.printnui.jCheckBox5.isSelected()){
              Writer("5");
            } 
        }
        
    }
    public void Writer(String nameLab){
        Document documento = new Document();
        long s = System.currentTimeMillis();
        try{
            String ruta = System.getProperty("user.home");
            PdfWriter.getInstance(documento, new FileOutputStream(ruta+
                    "/Downloads/ReporteLaboratorio_"+nameLab+".pdf"));
            documento.open();
            PdfPTable tabla = new PdfPTable(5);
            tabla.addCell("id_pc");
            tabla.addCell("estado");
            tabla.addCell("id_lab");
            tabla.addCell("fecha_mod");
            tabla.addCell("obs");
            List<Map<String, Object>> resultList = new ArrayList<>();
            String sql = String.format("SELECT * FROM computer where id_lab=%s",
                    nameLab);
            try {
                resultList = new ConnectionPool().makeConsult(sql);
                for (int i=0;i<resultList.size();i++){
                    tabla.addCell(String.valueOf(resultList.get(i).get("id_pc")));
                    tabla.addCell(String.valueOf(resultList.get(i).get("estado")));
                    tabla.addCell(String.valueOf(resultList.get(i).get("id_lab")));
                    tabla.addCell(String.valueOf(resultList.get(i).get("fecha_mod")));
                    tabla.addCell(String.valueOf(resultList.get(i).get("obs")));
                }
                documento.add(tabla);

            } catch (SQLException ex) {
                System.out.println(ex);
            }
            documento.close();
            JOptionPane.showMessageDialog(null, "Informe del laboratorio: "+nameLab+" fue creado con exito");

        }catch(Exception exc){
            System.out.println(exc);
        }
        System.out.println( "Tiempo: " + (System.currentTimeMillis() - s) + " s. "  );
    }
    
}
