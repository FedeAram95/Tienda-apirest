package com.deofis.tiendaapirest.reportes.services;

import com.deofis.tiendaapirest.catalogo.productos.entities.Producto;
import com.deofis.tiendaapirest.catalogo.productos.repositories.ProductoRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ExportProductoReporteServiceImpl implements ExportProductoReporteService {

    private final ProductoRepository productoRepository;

    @Override
    public ByteArrayInputStream generarReporteExcel() throws IOException {
        String[] columns = {"Id", "Nombre", "Descripcion", "Stock"};

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Productos");
        Row row = sheet.createRow(0);

        for (int i=0; i < columns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columns[i]);
        }

        List<Producto> productos = this.productoRepository.findAll();
        int initRow = 1;

        for (Producto producto: productos) {
            row = sheet.createRow(initRow);
            row.createCell(0).setCellValue(producto.getId());
            row.createCell(1).setCellValue(producto.getNombre());
            row.createCell(2).setCellValue(producto.getDescripcion());
            row.createCell(3).setCellValue(producto.getDisponibilidadGeneral());

            initRow++;
        }

        workbook.write(stream);
        workbook.close();

        return new ByteArrayInputStream(stream.toByteArray());
    }
}
