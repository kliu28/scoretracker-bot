import java.io.FileInputStream;
import java.util.Iterator;

/**
 * Created by Katherine Liu on 7/16/2017.
 */
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Check file path syntax.");
            return;
        }

        try {
            FileInputStream excelFile = new FileInputStream(args[0]);
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
            readWriteFile(workbook);
        }
        catch (Exception e) {
            System.out.println("Invalid file path.");
            return;
        }
    }

    private static void readWriteFile(XSSFWorkbook workbook) {
        //adapted from https://www.mkyong.com/java/apache-poi-reading-and-writing-excel-file-in-java/
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();

        while (iterator.hasNext()) {
            Row currRow = iterator.next();
            Iterator<Cell> cells = currRow.iterator();

            while (cells.hasNext()) {
                Cell currCell = cells.next();
                System.out.println(currCell.toString());
            }
        }
    }

}
