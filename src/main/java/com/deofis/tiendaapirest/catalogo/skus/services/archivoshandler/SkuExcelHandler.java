package com.deofis.tiendaapirest.catalogo.skus.services.archivoshandler;

import com.deofis.tiendaapirest.catalogo.productos.exceptions.ProductoException;
import com.deofis.tiendaapirest.catalogo.skus.dto.SkuDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementación de {@link SkuArchivosHandler} basada en archivos EXCEL.
 */
@Service
public class SkuExcelHandler implements SkuArchivosHandler {

    @Override
    public List<SkuDto> importar(MultipartFile file) {
        List<SkuDto> skus;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            skus = new ArrayList<>();

            int rowNumber = 0;

            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                SkuDto sku = SkuDto.builder().build();
                int cellIndex = 0;

                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIndex) {
                        case 0:
                            sku.setId((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            sku.setNombre(currentCell.getStringCellValue());
                            break;
                        case 2:
                            sku.setValores(currentCell.getStringCellValue());
                            break;
                        case 3:
                            sku.setPrecio(currentCell.getNumericCellValue());
                            break;
                        case 4:
                            sku.setDisponibilidad((int) currentCell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }

                    cellIndex++;
                }

                // Si alguna fila no contiene nada (o el id esta vacio), se ignora.
                if (sku.getId() == null || sku.getId() == 0) continue;

                skus.add(sku);
            }

            return skus;
        } catch (IOException e) {
            throw new ProductoException("Error al importar el archivo EXCEL : " + e.getMessage());
        }

    }

}
