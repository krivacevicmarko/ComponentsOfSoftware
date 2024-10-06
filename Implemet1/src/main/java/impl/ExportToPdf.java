package impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import spec.Pdf;
import spec.Termin;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ExportToPdf extends Pdf {

    private static final float PAGE_HEIGHT = 750; // A4 page height in points
    private static final float LINE_HEIGHT = 12; // Adjust as needed


    private static float calculateTextHeight(Termin appointment) {
        float height = LINE_HEIGHT * 5; // Base height for Start Time, End Time, Room, and two extra lines

        if (appointment.getOstalo() != null) {
            height += LINE_HEIGHT * appointment.getOstalo().size();
        }

        return height;
    }

    @Override
    public void exportToPdf(List<Termin> list, String s) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            String fontPath = "C:\\Users\\Krivi\\Desktop\\projekat\\dejavu-fonts-ttf-2.37\\ttf\\DejaVuSans.ttf";
            PDType0Font font = PDType0Font.load(document,new File(fontPath));

            float yPosition = PAGE_HEIGHT - LINE_HEIGHT;

            for (Termin appointment : list) {
                float textHeight = calculateTextHeight(appointment);
                float remainingHeight = yPosition - textHeight;

                if (remainingHeight < 0) {
                    // Not enough space on the current page, add a new page
                    page = new PDPage();
                    document.addPage(page);
                    yPosition = PAGE_HEIGHT - LINE_HEIGHT;
                }

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {

                    contentStream.setFont(font, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPosition);

                    contentStream.showText("Date: " + appointment.getDatum().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    contentStream.newLineAtOffset(0,-LINE_HEIGHT);
                    contentStream.showText("Start Time: " + appointment.getStart());
                    contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                    contentStream.showText("End Time: " + appointment.getKraj());
                    contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                    contentStream.showText("Room: " + appointment.getUcionica().getNaziv());
                    contentStream.newLineAtOffset(0, -LINE_HEIGHT);

                    if (appointment.getOstalo() != null) {
                        for (Map.Entry<String, String> entry : appointment.getOstalo().entrySet()) {
                            contentStream.showText(entry.getKey() + ": " + entry.getValue());
                            contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                        }
                    }

                    contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                    contentStream.endText();
                }

                yPosition -= textHeight;
            }

            document.save(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}