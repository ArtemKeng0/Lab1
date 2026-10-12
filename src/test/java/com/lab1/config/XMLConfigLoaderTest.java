package com.lab1.config;

import com.lab1.income.EmploymentIncome;
import com.lab1.income.PropertySaleIncome;
import com.lab1.tax.TaxDeclaration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XMLConfigLoaderTest {

    private XMLConfigLoader loader;

    @BeforeEach
    void setUp() {
        loader = new XMLConfigLoader();
    }

    @Test
    void testLoadFromStreamValidXml() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <declaration taxpayer="Peter Smith">
                    <incomes>
                        <employment source="IT Corp" amount="100000.0" mainJob="true" children="1"/>
                        <propertySale source="Car" amount="200000.0" firstSale="false"/>
                    </incomes>
                </declaration>
                """;

        ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        TaxDeclaration declaration = loader.loadFromStream(is);

        assertNotNull(declaration);
        assertEquals("Peter Smith", declaration.getTaxpayerName());
        assertEquals(2, declaration.getIncomes().size());
    }

    @Test
    void testSaveAndLoadRoundTrip(@TempDir Path tempDir) throws Exception {
        File file = tempDir.resolve("test_taxes.xml").toFile();

        TaxDeclaration original = new TaxDeclaration("Anna Brown");
        original.addIncome(new EmploymentIncome("School", 50000.0, true, 2));
        original.addIncome(new PropertySaleIncome("Apartment", 800000.0, true));

        loader.saveToFile(original, file);
        assertTrue(file.exists());

        TaxDeclaration loaded = loader.loadFromFile(file);
        assertEquals(original.getTaxpayerName(), loaded.getTaxpayerName());
        assertEquals(2, loaded.getIncomes().size());
        assertEquals(original.calculateTotalTax(), loaded.calculateTotalTax());
    }

    @Test
    void testLoadNonExistentFileThrowsException() {
        File nonExistent = new File("non_existent_file_12345.xml");
        assertThrows(IllegalArgumentException.class, () -> loader.loadFromFile(nonExistent));
    }

    @Test
    void testLoadNullStreamThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> loader.loadFromStream(null));
    }
}
