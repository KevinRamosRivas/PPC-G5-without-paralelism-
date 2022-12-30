/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Connection.ConnectionPool;
import Interface.Graficos;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.security.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author USUARIO
 */
public class GraficosCtr  implements ActionListener {
    
    public static String user_update = "";
    public Graficos graficosui;
    public DefaultTableModel model = new DefaultTableModel();
    public List<ModificateThread> hilos = new ArrayList<ModificateThread>();
    
    public int id_pc =-1;
    public String estado=null;
    public int id_labo= -1;
    public Timestamp mod=null;
    public String obs=null;

    public GraficosCtr(){
        graficosui = new Graficos();
        graficosui.botonGraficos.addActionListener(this);
        graficosui.tablaDatos = new JTable(model);
        graficosui.jScrollPane2.setViewportView(graficosui.tablaDatos);
        model.addColumn("Id PC");
        model.addColumn("Estado");
        model.addColumn("Id Laboratorio");
        model.addColumn("Fecha de Modificación");
        model.addColumn("Observación");
    }
    
    public void limpiarTabla(DefaultTableModel modelo ,JTable tabla){
        for (int i = 0; i < tabla.getRowCount(); i++) {
            modelo.removeRow(i);
            i-=1;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()== graficosui.botonGraficos){
            int operativoPie  = 0;
            int defectuosaPie = 0;
            int mantenimientoPie =0;
            long tiempoInicial = System.currentTimeMillis();
            limpiarTabla(model,graficosui.tablaDatos);
            
                    
                        List<Map<String, Object>> resultList = new ArrayList<>();
                        String sql = String.format(
                                "select id_pc, estado, id_lab, fecha_mod,obs from computer");
                        //System.out.println(sql);

                        try {
                            resultList = new ConnectionPool().makeConsult(sql);

                            for (int i=0;i<resultList.size();i++){
                                model.addRow(new Object[]{String.valueOf(resultList.get(i).get("id_pc")),
                                    String.valueOf(resultList.get(i).get("estado")),
                                    String.valueOf(resultList.get(i).get("id_lab")),
                                    String.valueOf(resultList.get(i).get("fecha_mod")),
                                    String.valueOf(resultList.get(i).get("obs"))
                                });

                            }
                             

                        } catch (SQLException ex) {
                            System.err.println("Error al llenar tabla"+ ex);
                            JOptionPane.showMessageDialog(null, "Error al mostrar información, Contactar al administrador");
                            //System.out.println(ex);
                        }
                        
                        
                        graficosui.jPanel1.removeAll();
                        graficosui.jPanel3.removeAll();
                        graficosui.jPanel2.removeAll();
                        
                        
                        for (int i=0;i<resultList.size();i++){
                            switch (resultList.get(i).get("estado").toString()) {
                                case "operativo":
                                    operativoPie++;
                                    break;
                                case "defectuosa":
                                    defectuosaPie++;
                                    break;
                                default:
                                    mantenimientoPie++;
                                    break;
                            }


                        }
                        DefaultPieDataset pieGraphic = new DefaultPieDataset();
                        pieGraphic.setValue("PC operativa",operativoPie);
                        pieGraphic.setValue("PC defectuosa",defectuosaPie);
                        pieGraphic.setValue("PC en mantenimiento",mantenimientoPie);

                        JFreeChart grafico_circular = ChartFactory.createPieChart(
                                "Estados de las PC",
                                pieGraphic,
                                true,
                               true,
                                true
                        );

                        ChartPanel panel1 = new ChartPanel(grafico_circular);
                        panel1.setMouseWheelEnabled(true);
                        panel1.setPreferredSize(new Dimension(400,200));
                        graficosui.jPanel1.setLayout(new BorderLayout());
                        graficosui.jPanel1.add(panel1,BorderLayout.NORTH);
             
                       
                        
                        int numPcLab1 = 0;
                        int numPcLab2 = 0;
                        int numPcLab3 = 0;
                        int numPcLab4 = 0;
                        int numPcLab5 = 0;
                        int numPcLab6 = 0;
                        int numPcLab7 = 0;

                        for (int i=0;i<resultList.size();i++){
                            switch (resultList.get(i).get("id_lab").toString()) {
                                case "1":
                                    numPcLab1++;
                                    break;
                                case "2":
                                    numPcLab2++;
                                    break;
                                case "3":
                                    numPcLab3++;
                                    break;
                                case "4":
                                    numPcLab4++;
                                    break;
                                case "5":
                                    numPcLab5++;
                                    break;
                                case "6":
                                    numPcLab6++;
                                    break;
                                case "7":
                                    numPcLab7++;
                                    break;                  
                            }


                        }
                        DefaultCategoryDataset datos1 = new DefaultCategoryDataset();
                        datos1.setValue(numPcLab1, "Lab 1", "Laboratorio 1");
                        datos1.setValue(numPcLab2, "Lab 2", "Laboratorio 2");
                        datos1.setValue(numPcLab3, "Lab 3", "Laboratorio 3");
                        datos1.setValue(numPcLab4, "Lab 4", "Laboratorio 4");
                        datos1.setValue(numPcLab5, "Lab 5", "Laboratorio 5");
                        datos1.setValue(numPcLab6, "Lab 6", "Laboratorio 6");
                        datos1.setValue(numPcLab7, "Lab 7", "Laboratorio 7");
                        JFreeChart grafico_barras = ChartFactory.createBarChart3D("Cantidad de computadoras por laboratorio",
                                "Laboratorio", 
                                "Cantidad", datos1, 
                                PlotOrientation.HORIZONTAL, true, 
                                true, false
                                );
                        ChartPanel panel2 = new ChartPanel(grafico_barras);
                        panel2.setMouseWheelEnabled(true);
                        panel2.setPreferredSize(new Dimension(400,250));

                        graficosui.jPanel3.setLayout(new BorderLayout());
                        graficosui.jPanel3.add(panel2,BorderLayout.NORTH);
 
                
                        int labo_1Operativo = 0;
                        int labo_2Operativo = 0;
                        int labo_3Operativo = 0;
                        int labo_4Operativo = 0;
                        int labo_5Operativo = 0;
                        int labo_6Operativo = 0;
                        int labo_7Operativo = 0;
                        int labo_1Defectuoso = 0;
                        int labo_2Defectuoso = 0;
                        int labo_3Defectuoso = 0;
                        int labo_4Defectuoso = 0;
                        int labo_5Defectuoso = 0;
                        int labo_6Defectuoso = 0;
                        int labo_7Defectuoso = 0;
                        int labo_1Mantenimiento = 0;
                        int labo_2Mantenimiento = 0;
                        int labo_3Mantenimiento = 0;
                        int labo_4Mantenimiento = 0;
                        int labo_5Mantenimiento = 0;
                        int labo_6Mantenimiento = 0;
                        int labo_7Mantenimiento = 0;
                        for (int i=0;i<resultList.size();i++){
                            switch (resultList.get(i).get("id_lab").toString()) {
                                case "1":
                                    switch (resultList.get(i).get("estado").toString()) {
                                        case "operativo":
                                            labo_1Operativo++;
                                            break;
                                        case "defectuosa":
                                            labo_1Defectuoso++;
                                            break;
                                        default:
                                            labo_1Mantenimiento++;
                                            break;
                                    }
                                    break;
                                case "2":
                                    switch (resultList.get(i).get("estado").toString()) {
                                        case "operativo":
                                            labo_2Operativo++;
                                            break;
                                        case "defectuosa":
                                            labo_2Defectuoso++;
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case "3":
                                    switch (resultList.get(i).get("estado").toString()) {
                                        case "operativo":
                                            labo_3Operativo++;
                                            break;
                                        case "defectuosa":
                                            labo_3Defectuoso++;
                                            break;
                                        default:
                                            labo_3Mantenimiento++;
                                            break;
                                    }
                                    break;
                                case "4":
                                    switch (resultList.get(i).get("estado").toString()) {
                                        case "operativo":
                                            labo_4Operativo++;
                                            break;
                                        case "defectuosa":
                                            labo_4Defectuoso++;
                                            break;
                                        default:
                                            labo_4Mantenimiento++;
                                            break;
                                    }
                                    break;
                                case "5":
                                    switch (resultList.get(i).get("estado").toString()) {
                                        case "operativo":
                                            labo_5Operativo++;
                                            break;
                                        case "defectuosa":
                                            labo_5Defectuoso++;
                                            break;
                                        default:
                                            labo_5Mantenimiento++;
                                            break;
                                    }
                                    break;
                                case "6":
                                    switch (resultList.get(i).get("estado").toString()) {
                                        case "operativo":
                                            labo_6Operativo++;
                                            break;
                                        case "defectuosa":
                                            labo_6Defectuoso++;
                                            break;
                                        default:
                                            labo_6Mantenimiento++;
                                            break;
                                    }
                                    break;
                                case "7":
                                    switch (resultList.get(i).get("estado").toString()) {
                                        case "operativo":
                                            labo_7Operativo++;
                                            break;
                                        case "defectuosa":
                                            labo_7Defectuoso++;
                                            break;
                                        default:
                                            labo_7Mantenimiento++;
                                            break;
                                    }
                                    break;                    
                            }


                        }
                        DefaultCategoryDataset datos3 = new DefaultCategoryDataset();
                        datos3.setValue(labo_1Operativo,"PC operativa","Laboratorio 1");
                        datos3.setValue(labo_1Defectuoso,"PC defectuosa","Laboratorio 1");
                        datos3.setValue(labo_1Mantenimiento,"PC en mantenimiento","Laboratorio 1");
                        datos3.setValue(labo_2Operativo,"PC operativa","Laboratorio 2");
                        datos3.setValue(labo_2Defectuoso,"PC defectuosa","Laboratorio 2");
                        datos3.setValue(labo_2Mantenimiento,"PC en mantenimiento","Laboratorio 2");
                        datos3.setValue(labo_3Operativo,"PC operativa","Laboratorio 3");
                        datos3.setValue(labo_3Defectuoso,"PC defectuosa","Laboratorio 3");
                        datos3.setValue(labo_3Mantenimiento,"PC en mantenimiento","Laboratorio 3");
                        datos3.setValue(labo_4Operativo,"PC operativa","Laboratorio 4");
                        datos3.setValue(labo_4Defectuoso,"PC defectuosa","Laboratorio 4");
                        datos3.setValue(labo_4Mantenimiento,"PC en mantenimiento","Laboratorio 4");
                        datos3.setValue(labo_5Operativo,"PC operativa","Laboratorio 5");
                        datos3.setValue(labo_5Defectuoso,"PC defectuosa","Laboratorio 5");
                        datos3.setValue(labo_5Mantenimiento,"PC en mantenimiento","Laboratorio 5");
                        datos3.setValue(labo_6Operativo,"PC operativa","Laboratorio 6");
                        datos3.setValue(labo_6Defectuoso,"PC defectuosa","Laboratorio 6");
                        datos3.setValue(labo_6Mantenimiento,"PC en mantenimiento","Laboratorio 6");
                        datos3.setValue(labo_7Operativo,"PC operativa","Laboratorio 7");
                        datos3.setValue(labo_7Defectuoso,"PC defectuosa","Laboratorio 7");
                        datos3.setValue(labo_7Mantenimiento,"PC en mantenimiento","Laboratorio 7");
                        JFreeChart grafico_barras3 = ChartFactory.createBarChart3D("Estado de las PC por laboratorio", 
                                "Laboratorio", 
                                "Cantidad", datos3, 
                                PlotOrientation.HORIZONTAL, true, 
                                true, false
                                );
                        ChartPanel panel3 = new ChartPanel(grafico_barras3);
                        panel3.setMouseWheelEnabled(true);
                        panel3.setPreferredSize(new Dimension(400,250));

                        graficosui.jPanel2.setLayout(new BorderLayout());
                        graficosui.jPanel2.add(panel3,BorderLayout.NORTH);

                        graficosui.pack();
                        graficosui.repaint();
                        long tiempoFinal = System.currentTimeMillis();
                        System.out.println("Tiempo de ejecucion: " + (tiempoFinal - tiempoInicial) + " ms");

                    
                    
        }
    }
}

   
