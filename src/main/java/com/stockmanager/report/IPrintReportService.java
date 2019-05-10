package com.stockmanager.report;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public interface IPrintReportService {

    public JasperPrint printReport(String reportName, HashMap<String, Object> map, Collection maCollection);

    public String exportReportToPdf(JasperPrint jp, String fichierPDF);

    public void exportReportToPdfStream(JasperPrint jp, OutputStream outputStream);

    public JasperPrint printSQLReport(String reportName, HashMap<String, Object> map, Connection sqlConnection);

    public JasperPrint printSQLReport(JasperReport jasperReport, HashMap<String, Object> map, Connection sqlConnection);
}
