package com.deofis.tiendaapirest.reportes.services;

import com.deofis.tiendaapirest.operaciones.dto.OperacionDTO;
import com.deofis.tiendaapirest.operaciones.entities.Operacion;
import com.deofis.tiendaapirest.operaciones.repositories.OperacionRepository;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Deprecated
public class ExportOperacionReporteServiceImpl implements ExportOperacionReporteService {

    private final OperacionRepository operacionRepository;

    @Override
    public byte[] generarReportPDF() throws FileNotFoundException, JRException {
        byte[] bytes;

        List<OperacionDTO> operaciones = this.operacionRepository.findAllByOrderByFechaOperacionDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        File archivo = ResourceUtils.getFile("classpath:operaciones.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(archivo.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(operaciones);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "DEOFIS");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        bytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return bytes;
    }

    @Override
    public ByteArrayInputStream generarReportEXCEL() throws IOException {
        String[] columns = {"N° Operación", "Estado", "Creado En", "Cliente", "Pago", "Total"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Operaciones");
        Row row = sheet.createRow(0);

        for (int i=0; i < columns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columns[i]);
        }

        List<OperacionDTO> operaciones = this.operacionRepository.findAllByOrderByFechaOperacionDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        int initRow = 1;

        for (OperacionDTO operacion: operaciones) {
            row = sheet.createRow(initRow);
            row.createCell(0).setCellValue(operacion.getNroOperacion());
            row.createCell(1).setCellValue(operacion.getEstado());
            row.createCell(2).setCellValue(String.valueOf(operacion.getFechaOperacion()));
            row.createCell(3).setCellValue(operacion.getCliente());
            row.createCell(4).setCellValue(String.valueOf(operacion.getMedioPago()));
            row.createCell(5).setCellValue("$ " + operacion.getTotal());

            initRow++;
        }

        workbook.write(stream);
        workbook.close();
        return new ByteArrayInputStream(stream.toByteArray());
        /*
        ##### Reporte con JASPER

        byte[] bytes = null;

        List<OperacionDTO> operaciones = this.operacionRepository.findAllByOrderByFechaOperacionDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        File archivo = ResourceUtils.getFile("classpath:operaciones.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(archivo.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(operaciones);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "DEOFIS");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        var input = new SimpleExporterInput(jasperPrint);

        try (var byteArray = new ByteArrayOutputStream()){
            var output = new SimpleOutputStreamExporterOutput(byteArray);
            var exporter = new JRXlsxExporter();
            exporter.setExporterInput(input);
            exporter.setExporterOutput(output);
            exporter.exportReport();

            bytes = byteArray.toByteArray();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
         */
    }

    private OperacionDTO mapToDto(Operacion operacion) {
        return OperacionDTO.builder()
                .nroOperacion(operacion.getNroOperacion())
                .estado(operacion.getEstado().toString())
                .fechaOperacion(operacion.getFechaOperacion())
                .fechaEnvio(operacion.getFechaEnvio())
                .fechaEntrega(operacion.getFechaEntrega())
                .cliente(operacion.getCliente().getApellido() + ", " + operacion.getCliente().getNombre())
                .medioPago(operacion.getMedioPago().getNombre())
                .total(operacion.getTotal())
                .build();
    }
}
