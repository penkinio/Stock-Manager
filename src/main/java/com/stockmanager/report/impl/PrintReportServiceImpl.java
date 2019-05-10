package com.stockmanager.report.impl;

import com.stockmanager.report.IPrintReportService;
import java.io.File;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class PrintReportServiceImpl implements IPrintReportService {

    JasperReport jasperReport;
    JasperPrint jasperPrint;

    private static final String REPORT_DIR = System.getProperty("user.dir") + "/src/main/resources/";
    private static final String SUBREPORT_DIR = System.getProperty("user.dir") + "/src/main/resources/";
    private static final String EXTENSION = ".jasper";

    public Boolean isValideReport(String reportName) {
        boolean bool;
        int nbrePoint = reportName.split(".").length;

        switch (nbrePoint) {
            case 0:
                reportName = reportName + EXTENSION;
                break;
            case 1:
                if (!(reportName.substring(reportName.length() - 6).equalsIgnoreCase(EXTENSION))) {
                    return false;
                }
                break;
            default:
                return false;
        }

        File file = new File(getReportName(reportName));
        bool = (file.exists()) && (!file.isDirectory());

        return bool;
    }

    public String getReportName(String reportName) {
        return REPORT_DIR + reportName;
    }

    public JasperPrint printSQLReport(String reportName, HashMap<String, Object> map, Connection sqlConnection) {

        if (!(new File(REPORT_DIR).exists())) {
            new File(REPORT_DIR).mkdirs();
        }

        map.put("SUBREPORT_DIR", SUBREPORT_DIR);

        File file = new File(getReportName(reportName));
        //File file = new File(reportName);

        try {
            jasperPrint = JasperFillManager.fillReport(file.getPath(), map, sqlConnection);
            //jasperPrint = JasperFillManager.fillReport(getReportName(reportName), map, sqlConnection);
        } catch (JRException e) {
            Logger.getLogger(PrintReportServiceImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return jasperPrint;
    }

    @SuppressWarnings("rawtypes")
    public JasperPrint printReport(String reportName, HashMap<String, Object> map, Collection maCollection) {

        if (!(new File(REPORT_DIR).exists())) {
            new File(REPORT_DIR).mkdirs();
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(maCollection);
        map.put("SUBREPORT_DIR", SUBREPORT_DIR);
        try {
            jasperPrint = JasperFillManager.fillReport(reportName, map, dataSource);
        } catch (Exception e) {
            Logger.getLogger(PrintReportServiceImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return jasperPrint;
    }

    public String exportReportToPdf(JasperPrint jp, String fichierPDF) {

        if (!fichierPDF.contains(".pdf")) {
            fichierPDF += ".pdf";
        }
        try {
            JasperExportManager.exportReportToPdfFile(jp, fichierPDF);
            System.out.println(fichierPDF);
            return fichierPDF;
        } catch (Exception e) {
            return null;
        }

    }

    public void exportReportToPdfStream(JasperPrint jp, OutputStream outputStream) {
        try {
            JasperExportManager.exportReportToPdfStream(jp, outputStream);
        } catch (Exception e) {
            Logger.getLogger(PrintReportServiceImpl.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public JasperPrint printSQLReport(JasperReport jasperReport, HashMap<String, Object> map, Connection sqlConnection) {
        if (!(new File(REPORT_DIR).exists())) {
            new File(REPORT_DIR).mkdirs();
        }

        map.put("SUBREPORT_DIR", SUBREPORT_DIR);

        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, map, sqlConnection);
        } catch (Exception e) {
            Logger.getLogger(PrintReportServiceImpl.class.getName()).log(Level.SEVERE, null, e);
        }

        return jasperPrint;
    }

}
