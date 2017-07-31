import java.io.FileInputStream;
import java.util.Iterator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

/**
 * Created by Katherine Liu on 7/16/2017.
 */
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Main {

    public static void main(String[] args) {
        //reading excel file
//        if (args.length != 1) {
//            System.out.println("Check file path syntax.");
//            return;
//        }
//
//        try {
//            FileInputStream excelFile = new FileInputStream(args[0]);
//            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
//            readWriteFile(workbook);
//        }
//        catch (Exception e) {
//            System.out.println("Invalid file path.");
//            return;
//        }

        System.setProperty("webdriver.chrome.driver", "C:\\Projects\\scoretracker-bot\\chromedriver.exe");
        WebDriver driver=new ChromeDriver();
        driver.get("https://onlinescoretracker.com/user-panel/students");
        if (driver.getTitle().equals("Online Score Tracker")) {
            String prefix = args[1];
            String uname = args[2];
            String pw = args[3];
            login(driver, prefix, uname, pw);
            driver.get("https://onlinescoretracker.com/user-panel/students");
        }

        WebElement searchbox = driver.findElement(By.id("form-search-input"));
        searchbox.sendKeys("fone");

        //ABOVE THIS LINE IS CLEARED.


        //*[@id="studentDataTable"]/tbody/tr/td[2]/a
        //driver.findElement(By.xpath("//*[@id=\"studentDataTable\"]/tbody/tr/td[2]/a")).click();
        //WebElement studentTable = driver.findElement(By.xpath("//*[@id=\"studentDataTable\"]/tbody/tr/td[2]/a"));
        List<WebElement> elems = driver.findElements(By.xpath("//*[@id=\"studentDataTable\"]"));
        System.out.println("num rows: " + elems.size());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Thread sleep failed");
        }
        WebElement student = elems.get(0).findElement(By.xpath("//a[contains(@href, \"studentSummary\")]"));
        System.out.println(student.getText());
        student.click();

//        System.out.println(driver.findElement(By.xpath("//*[@id=\"studentDataTable\"]/tbody/tr/td[2]/a")).getText());
//        System.out.println(elems.get(0).findElement(By.xpath("//*[@id=\"studentDataTable\"]/tbody/tr/td[2]/a")).getText());
//        WebElement studentTable = driver.findElement(By.xpath("//*[@id=\"studentDataTable\"]"));
//        List<WebElement> rows = studentTable.findElements(By.tagName("tr"));
//        System.out.println("num rows: " + rows.size());
//        for (WebElement row : rows) {
//            List<WebElement> cols = row.findElements(By.tagName("td"));
//            for (WebElement col : cols) {
//                System.out.print(col.getText());
//            }
//            System.out.println();
//        }


// for (WebElement trElem : elems) {
//            List<WebElement> tds = trElem.findElements(By.xpath("td"));
//            //*[@id="studentDataTable"]/tbody/tr/td[2]/a
//            System.out.println("num cols: " + tds.size());
//            for (WebElement tdElem : tds) {
//                System.out.print(tdElem.getText());
//            }
//            System.out.println();
//        }
//        List<WebElement> elems = driver.findElements(By.xpath("//*[@id='studentDataTable']"));
//        System.out.println("elems size: " + elems.size());

        //*[@id="studentDataTable"]
//        List<WebElement> cells = elems.get(0).findElements(By.tagName("td"));
//        System.out.println("cells size: " + cells.size());
//        cells.get(1).click();
        //elems.get(1).click();
//        driver.findElement(By.xpath("//img[@ src='images/icon_edit.png']")).click();

//        WebElement q = driver.findElement(By.name("q"));
//        q.sendKeys("hello world");
//        q.submit();
    }

    private static void login(WebDriver driver, String prefix, String uname, String pw) {
        WebElement prefixBox = driver.findElement(By.name("uname_prefix"));
        prefixBox.sendKeys(prefix);

        WebElement unameBox = driver.findElement(By.name("username"));
        unameBox.sendKeys(uname);

        WebElement pwBox = driver.findElement(By.name("password"));
        pwBox.sendKeys(pw);

        WebElement submit = driver.findElement(By.id("submit"));
        submit.submit();
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
