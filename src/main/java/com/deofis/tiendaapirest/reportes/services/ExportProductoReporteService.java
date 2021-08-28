package com.deofis.tiendaapirest.reportes.services;

import net.sf.jasperreports.engine.JRException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ExportProductoReporteService {

    ByteArrayInputStream generarReporteExcel() throws IOException, JRException;
}
