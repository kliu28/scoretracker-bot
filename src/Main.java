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
        //checks arguments and gives a reminder for order of arguments.
        if (args.length != 6) {
            System.out.println("Check input syntax. Order is Excel file path, sleep time (ms), test ID " +
                    "account prefix, username, password; separated by spaces. " +
                    "\nSpaces in the file path are not allowed. " +
                    "\nExcel file should contain columns: " +
                    "first name, last name, reading, analysis, writing; in this order, no column headers");
            return;
        }

        //tries to parse arguments
        XSSFWorkbook workbook;
        try {
            FileInputStream excelFile = new FileInputStream(args[0]);
            workbook = new XSSFWorkbook(excelFile);
        } catch (Exception e) {
            System.out.println("Invalid file path.");
            return;
        }
        long TIME;
        String testID, prefix, uname, pw;
        try {
            TIME = Long.parseLong(args[1]);
            testID = "#" + args[2];
            prefix = args[3];
            uname = args[4];
            pw = args[5];
        } catch (Exception e) {
            System.out.println("Check input syntax for account prefix, username, password, and/or testID." +
                    " Enter 0 arguments for syntax reminder.");
            return;
        }

        //initiates login and opening of webpage
        System.setProperty("webdriver.chrome.driver", "C:\\Projects\\scoretracker-bot\\chromedriver.exe");
        WebDriver driver=new ChromeDriver();
        driver.get("https://onlinescoretracker.com/user-panel/students");
        if (driver.getCurrentUrl().contains("login")) {
            login(driver, prefix, uname, pw);
            driver.get("https://onlinescoretracker.com/user-panel/students");
            sleep(TIME * 3);
            if (driver.getCurrentUrl().contains("login")) {
                System.out.println("Incorrect login information.");
                return;
            }
        }

        //scores are entered.
        enterScores(driver, workbook, TIME, testID);

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

    //commonly used sleep to ensure buttons are clickable.
    private static void sleep(long time) {
        try{
            Thread.sleep(time);
        } catch (Exception e) { System.out.println("Thread sleep failed"); }
    }

    //logs into the page based on the onlinescoretracker homepage.
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

    //tester method to see how Apache POI works.
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

    //sets up a loop to iterate through Excel data and input scores
    private static void enterScores(WebDriver driver, XSSFWorkbook workbook, long TIME, String testID) {
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();

        while (iterator.hasNext()) {
            if (!driver.getCurrentUrl().equals("https://onlinescoretracker.com/user-panel/students"))
                driver.get("https://onlinescoretracker.com/user-panel/students");

            Row currRow = iterator.next();
            Iterator<Cell> cells = currRow.iterator();

            //gets and sets all necessary parameters
            String studentFName = cells.next().toString();
            String studentLName = cells.next().toString();
            String studentReading = cells.next().toString();
            String studentAnalysis = cells.next().toString();
            String studentWriting = cells.next().toString();

            String studentSearch = studentFName.substring(0,1) + studentLName;
            studentSearch = studentSearch.toLowerCase();

            //searches for a student
            WebElement searchbox = driver.findElement(By.id("form-search-input"));
            searchbox.sendKeys(studentSearch);

            //selects student
            List<WebElement> elems = driver.findElements(By.xpath("//*[@id=\"studentDataTable\"]"));
            sleep(TIME);
            WebElement student = elems.get(0).findElement(By.xpath("//a[contains(@href, \"studentSummary\")]"));
            student.click();
            //http://www.testerlogic.com/handling-dynamic-elements-in-selenium-webdriver/

            //finds correct test
            sleep(TIME);
            List<WebElement> rows = driver.findElements(By.className("students-lastname"));
            WebElement row = null;
            for (WebElement i : rows) {
                if (i.getText().contains(testID)) {
                    row = i;
                    break;
                }
            }

            //enters data if the test exists
            if (row == null) {
                System.out.println("Score data for student " + studentFName + " " + studentLName + " with username " + studentSearch
                        + " and test ID " + testID + " not found.");
            }
            else {
                WebElement edit = row.findElement(By.xpath(".//a[contains(@href, \"editStudentResponse\")]"));
                edit.click();

                sleep(TIME);
                WebElement essay = driver.findElement(By.xpath(".//*[@id=\"tabs1\"]/ul/li[5]/a"));
                essay.click();

                sleep(TIME);
                WebElement scoresBlock = driver.findElement(By.className("essay_score_block"));
                List<WebElement> scoresEntry = scoresBlock.findElements(By.xpath(".//input[@type='text']"));

                WebElement reading = scoresEntry.get(0);
                WebElement analysis = scoresEntry.get(1);
                WebElement writing = scoresEntry.get(2);

                if (reading.getAttribute("value").isEmpty()
                        && analysis.getAttribute("value").isEmpty()
                        && writing.getAttribute("value").isEmpty()) {
                    reading.sendKeys(studentReading);
                    analysis.sendKeys(studentAnalysis);
                    writing.sendKeys(studentWriting);
                }
                else {
                    System.out.println("Scores for student named " + studentFName + " " + studentLName
                        + " already exists.");
                    continue;
                }

                WebElement submit = driver.findElement(By.xpath("//input[@type='submit']"));
                submit.click();
            }
            System.out.println("Data entry complete.");
        }
    }
}
