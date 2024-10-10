import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class SelenideFilesTest {

    private ClassLoader cl = SelenideFilesTest.class.getClassLoader();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Проверка csv файла zip архива")
    void zipCsvFileParsingTest() throws Exception {

        CSVReader csv = null;

        try (ZipInputStream zip = new ZipInputStream(
                cl.getResourceAsStream("qaguru.zip")
        )) {
            ZipEntry entry;

            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().equals("students.csv")) {
                    csv = new CSVReader(new InputStreamReader(zip));
                    break;
                }
            }

            List<String[]> data = csv.readAll();

            assertEquals(2, data.size());
            assertArrayEquals(new String[]{"Ivanov", " Ivan", "[for='gender-radio-1']", " 7965151489"}, data.get(0));
            assertArrayEquals(new String[]{"Ivanova", " Inna", "[for='gender-radio-2']", " 7908514551"}, data.get(1));
        }

    }

    @Test
    @DisplayName("Проверка xlsx файла zip архива")
    void zipXlsFileParsingTest() throws Exception {
        XLS xls = null;

        try (ZipInputStream zip = new ZipInputStream(
                cl.getResourceAsStream("qaguru.zip")
        )) {
            ZipEntry entry;

            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().equals("Книга.xlsx")) {
                    xls = new XLS(zip);
                    break;
                }
            }

            assertEquals("Имя", xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());

        }

    }

    @Test
    @DisplayName("Проверка pdf файла zip архива")
    void zipPdfFileParsingTest() throws Exception {
        PDF pdf = null;

        try (ZipInputStream zip = new ZipInputStream(
                cl.getResourceAsStream("qaguru.zip")
        )) {
            ZipEntry entry;

            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().equals("Документ.pdf")) {
                    pdf = new PDF(zip);
                    break;
                }

            }

            assertTrue(pdf.text.contains("На дворе декабрь"), "Текст не содержит ожидаемое сообщение");

        }
    }

    @Test
    @DisplayName("Проверка json файла")
    void jsonFileParsingTest() throws Exception {
        try (Reader reader = new InputStreamReader(
                cl.getResourceAsStream("students.json")
        )) {
            JsonNode jsonNode = objectMapper.readTree(reader);

            assertEquals(1, jsonNode.get("students").get(0).get("id").asInt());
            assertEquals("John Doe", jsonNode.get("students").get(0).get("name").asText());
            assertEquals(21, jsonNode.get("students").get(0).get("age").asInt());
            assertEquals("john.doe@example.com", jsonNode.get("students").get(0).get("email").asText());

            assertEquals(2, jsonNode.get("students").get(1).get("id").asInt());
            assertEquals("Jane Smith", jsonNode.get("students").get(1).get("name").asText());
            assertEquals(22, jsonNode.get("students").get(1).get("age").asInt());
            assertEquals("jane.smith@example.com", jsonNode.get("students").get(1).get("email").asText());

            assertEquals(1, jsonNode.get("students").get(0).get("id").asInt());
            assertEquals("John Doe", jsonNode.get("students").get(0).get("name").asText());
            assertEquals(21, jsonNode.get("students").get(0).get("age").asInt());
            assertEquals("john.doe@example.com", jsonNode.get("students").get(0).get("email").asText());

            assertEquals(3, jsonNode.get("students").get(2).get("id").asInt());
            assertEquals("Emily Johnson", jsonNode.get("students").get(2).get("name").asText());
            assertEquals(20, jsonNode.get("students").get(2).get("age").asInt());
            assertEquals("emily.johnson@example.com", jsonNode.get("students").get(2).get("email").asText());
        }

    }

}
