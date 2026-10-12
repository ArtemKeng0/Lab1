package com.lab1.config;

import com.lab1.income.EmploymentIncome;
import com.lab1.income.FinancialAidIncome;
import com.lab1.income.ForeignTransferIncome;
import com.lab1.income.GiftIncome;
import com.lab1.income.Income;
import com.lab1.income.PropertySaleIncome;
import com.lab1.income.RoyaltyIncome;
import com.lab1.tax.TaxDeclaration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.InputStream;

/**
 * Клас для завантаження та збереження параметрів ініціалізації у форматі XML.
 * Відповідає вимозі збереження параметрів у файлах без використання сторонніх бібліотек.
 */
public class XMLConfigLoader {

    /**
     * Завантажує податкову декларацію з файлу XML.
     *
     * @param file XML-файл з даними
     * @return ініціалізований об'єкт TaxDeclaration
     * @throws Exception у разі помилки читання чи парсингу
     */
    public TaxDeclaration loadFromFile(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Файл конфігурації не знайдено: " + file);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        return parseDocument(doc);
    }

    /**
     * Завантажує податкову декларацію з потоку InputStream (наприклад, з ресурсів).
     *
     * @param inputStream потік вхідних даних
     * @return ініціалізований об'єкт TaxDeclaration
     * @throws Exception у разі помилки читання чи парсингу
     */
    public TaxDeclaration loadFromStream(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("Вхідний потік не може бути null.");
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputStream);
        return parseDocument(doc);
    }

    /**
     * Парсить DOM-документ XML у об'єкт декларації.
     */
    private TaxDeclaration parseDocument(Document doc) {
        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();
        String taxpayerName = root.getAttribute("taxpayer");
        if (taxpayerName == null || taxpayerName.trim().isEmpty()) {
            taxpayerName = "Невідомий платник";
        }

        TaxDeclaration declaration = new TaxDeclaration(taxpayerName);
        NodeList incomeNodes = root.getElementsByTagName("incomes");
        if (incomeNodes.getLength() == 0) {
            return declaration;
        }

        Element incomesElement = (Element) incomeNodes.item(0);
        NodeList children = incomesElement.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element elem = (Element) node;
            Income income = parseIncomeElement(elem);
            if (income != null) {
                declaration.addIncome(income);
            }
        }

        return declaration;
    }

    private Income parseIncomeElement(Element elem) {
        String tagName = elem.getTagName();
        String source = elem.getAttribute("source");
        double amount = parseDouble(elem.getAttribute("amount"), 0.0);

        switch (tagName) {
            case "employment":
                boolean mainJob = Boolean.parseBoolean(elem.getAttribute("mainJob"));
                int children = (int) parseDouble(elem.getAttribute("children"), 0.0);
                return new EmploymentIncome(source, amount, mainJob, children);

            case "royalty":
                double expenses = parseDouble(elem.getAttribute("expenses"), 0.0);
                return new RoyaltyIncome(source, amount, expenses);

            case "propertySale":
                boolean firstSale = Boolean.parseBoolean(elem.getAttribute("firstSale"));
                return new PropertySaleIncome(source, amount, firstSale);

            case "gift":
                boolean immediateFamily = Boolean.parseBoolean(elem.getAttribute("immediateFamily"));
                return new GiftIncome(source, amount, immediateFamily);

            case "foreignTransfer":
                String currency = elem.getAttribute("currency");
                double exchangeRate = parseDouble(elem.getAttribute("exchangeRate"), 1.0);
                return new ForeignTransferIncome(source, amount, currency, exchangeRate);

            case "financialAid":
                double limit = parseDouble(elem.getAttribute("limit"), FinancialAidIncome.DEFAULT_NON_TAXABLE_LIMIT);
                return new FinancialAidIncome(source, amount, limit);

            default:
                return null;
        }
    }

    private double parseDouble(String value, double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Зберігає декларацію у файл XML.
     *
     * @param declaration об'єкт декларації
     * @param targetFile  цільовий файл для збереження
     * @throws Exception у разі помилки збереження
     */
    public void saveToFile(TaxDeclaration declaration, File targetFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("declaration");
        root.setAttribute("taxpayer", declaration.getTaxpayerName());
        doc.appendChild(root);

        Element incomesElem = doc.createElement("incomes");
        root.appendChild(incomesElem);

        for (Income income : declaration.getIncomes()) {
            Element itemElem = null;
            if (income instanceof EmploymentIncome emp) {
                itemElem = doc.createElement("employment");
                itemElem.setAttribute("mainJob", String.valueOf(emp.isMainJob()));
                itemElem.setAttribute("children", String.valueOf(emp.getChildrenCount()));
            } else if (income instanceof RoyaltyIncome roy) {
                itemElem = doc.createElement("royalty");
                itemElem.setAttribute("expenses", String.valueOf(roy.getDocumentedExpenses()));
            } else if (income instanceof PropertySaleIncome prop) {
                itemElem = doc.createElement("propertySale");
                itemElem.setAttribute("firstSale", String.valueOf(prop.isFirstSaleOfYear()));
            } else if (income instanceof GiftIncome gift) {
                itemElem = doc.createElement("gift");
                itemElem.setAttribute("immediateFamily", String.valueOf(gift.isImmediateFamily()));
            } else if (income instanceof ForeignTransferIncome foreign) {
                itemElem = doc.createElement("foreignTransfer");
                itemElem.setAttribute("currency", foreign.getCurrencyCode());
                itemElem.setAttribute("exchangeRate", String.valueOf(foreign.getExchangeRate()));
            } else if (income instanceof FinancialAidIncome aid) {
                itemElem = doc.createElement("financialAid");
                itemElem.setAttribute("limit", String.valueOf(aid.getNonTaxableLimit()));
            }

            if (itemElem != null) {
                itemElem.setAttribute("source", income.getSourceName());
                itemElem.setAttribute("amount", String.valueOf(income.getAmount()));
                incomesElem.appendChild(itemElem);
            }
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(doc), new StreamResult(targetFile));
    }
}
