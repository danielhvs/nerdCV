import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfTest {

    private static final Color SIDEBAR_TEXT_COLOR = java.awt.Color.lightGray;
    private static final Color SIDEBAR_BACKGROUND = java.awt.Color.blue;

    @Test
    void generatePdf() {
        Font font8 = FontFactory.getFont(FontFactory.HELVETICA, 8);
        Document document = new Document(PageSize.A4);
        try {
            // step 2
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream("tables.pdf"));
            float width = document.getPageSize().getWidth();
            float height = document.getPageSize().getHeight();
            // step 3
            document.open();

            // step 4
            float[] columnDefinitionSize = { 30F, 70F };

            float pos = height / 2;

            PdfPTable mainTable = new PdfPTable(columnDefinitionSize);
            mainTable.getDefaultCell().setBorder(0);
            mainTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            mainTable.setWidthPercentage(100);
            // table.setLockedWidth(true);

            // Equivalent to clj-pdf :pdf-table
            PdfPTable sideBarTable = new PdfPTable(1);
            mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            mainTable.getDefaultCell().setBackgroundColor(SIDEBAR_BACKGROUND);

            // Equivalent to clj-pdf :pdf-cell with an image
            Image profileImage = Image.getInstance("/home/danielhabib/cv/profile.jpg");
            profileImage.scalePercent(25f);
            PdfPCell cell = new PdfPCell(profileImage, true);
            cell.setBorder(Rectangle.TOP);
            cell.setBorderColor(SIDEBAR_BACKGROUND);
            sideBarTable.addCell(cell);

            // Equivalent to clj-pdf :pdf-cell with a heading for the name
            Font nameFont = new Font(Font.TIMES_ROMAN, 20, Font.BOLD, SIDEBAR_TEXT_COLOR);
            Paragraph nameParagraph = new Paragraph("name:daniel???", nameFont);
            nameParagraph.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(nameParagraph);
            cell.setBorder(Rectangle.TOP);
            cell.setBorderColor(SIDEBAR_BACKGROUND);
            sideBarTable.addCell(cell);

            // Equivalent to clj-pdf :pdf-cell with the "Software Engineer" heading
            Font jobFont = new Font(Font.TIMES_ROMAN, 14, Font.BOLD, SIDEBAR_TEXT_COLOR);
            Paragraph jobParagraph = new Paragraph("Software Engineer", jobFont);
            jobParagraph.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(jobParagraph);
            cell.setBorder(Rectangle.TOP);
            cell.setBorderColor(SIDEBAR_BACKGROUND);
            sideBarTable.addCell(cell);

            PdfPTable contentTable = new PdfPTable(1);
            contentTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            contentTable.addCell(new PdfPCell(new Paragraph("Bla bla", jobFont)));

            mainTable.addCell(sideBarTable);
            mainTable.addCell(contentTable);
            document.add(mainTable);
        }

        catch (DocumentException | IOException de) {
            throw new RuntimeException(de);
        } finally {
            document.close();
        }
    }
}
