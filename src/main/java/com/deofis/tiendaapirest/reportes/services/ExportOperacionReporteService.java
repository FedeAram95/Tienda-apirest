package com.deofis.tiendaapirest.reportes.services;

import net.sf.jasperreports.engine.JRException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Deprecated
public interface ExportOperacionReporteService {

    byte[] generarReportPDF() throws FileNotFoundException, JRException;

    ByteArrayInputStream generarReportEXCEL() throws IOException, JRException;
}
