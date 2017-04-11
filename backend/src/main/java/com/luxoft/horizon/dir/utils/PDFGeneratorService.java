package com.luxoft.horizon.dir.utils;

import com.luxoft.horizon.dir.entities.app.Screenshot;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by bogatp on 13.04.16.
 */
@Service
public class PDFGeneratorService {

    public static final int MARGIN = 40;
    public static final int FONT_SIZE = 16;

    public void createPDFFromImage(List<Screenshot> images, String outputFile)
            throws IOException {
        // the document
        PDDocument doc = new PDDocument();
        try {
            int pageCounter = 1;
            for (Screenshot screenshot : images) {
                //create image from byte array
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(screenshot.getData()));
                PDImageXObject pdImage = LosslessFactory.createFromImage(doc, image);
                PDRectangle rectangle = new PDRectangle(pdImage.getWidth() + MARGIN, pdImage.getHeight() + MARGIN);
                PDPage page = new PDPage(rectangle);
                doc.addPage(page);
                //draw image
                PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.OVERWRITE, false);
                contentStream.drawImage(pdImage, MARGIN / 2, MARGIN / 2, pdImage.getWidth(), pdImage.getHeight());
                PDFont font = PDType1Font.HELVETICA_BOLD;
                // draw page number
                contentStream.beginText();
                contentStream.setFont(font, FONT_SIZE);
                contentStream.newLineAtOffset(pdImage.getWidth() - MARGIN / 2, MARGIN / 2);
                contentStream.showText(String.valueOf(pageCounter++));
                contentStream.endText();
                contentStream.close();
            }
        } finally {
            doc.save(outputFile);
            if (doc != null) {
                doc.close();
            }
        }
    }
}
