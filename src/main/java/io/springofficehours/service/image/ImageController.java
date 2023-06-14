package io.springofficehours.service.image;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class ImageController {

    private static final String FONT = "HWT-Art-W00-Regular.ttf";
    private static final String STREAMYARD = "spring-office-hours-streamyard.png";
    private static final String TEMPLATE = "spring-office-hours-blank.png";

    private static final int LEFT_PAD = 60;
    private static final int BOTTOM_PAD = 60;

    @GetMapping("/template")
    public StreamingResponseBody templateImage(HttpServletResponse response) throws IOException {
        try (InputStream is = templateInputStream(TEMPLATE)) {
            byte[] image = is.readAllBytes();
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=blank.png");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(image.length));
            return outputStream -> outputStream.write(image);
        }
    }

    @GetMapping("/streamyard")
    public StreamingResponseBody streamyardImage(HttpServletResponse response) throws IOException {
        try (InputStream is = templateInputStream(STREAMYARD)) {
            byte[] image = is.readAllBytes();
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=streamyard.png");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(image.length));
            return outputStream -> outputStream.write(image);
        }
    }

    @GetMapping("/chatGptVersion")
    public StreamingResponseBody chatGptVersion() throws IOException, FontFormatException {
        // Read in the input image
        BufferedImage inputImage = ImageIO.read(templateInputStream(TEMPLATE));

        // Create a graphics context on the input image
        Graphics2D g = inputImage.createGraphics();

        // Set the font and draw the text
        Font font = getFont();
        g.setFont(font.deriveFont(15.0f));
        g.setColor(Color.BLACK);
        g.drawString("HELLO WORLD", (inputImage.getWidth() - g.getFontMetrics().stringWidth("HELLO WORLD")) / 2,
                (inputImage.getHeight() - g.getFontMetrics().getHeight()) / 2);

        // Dispose the graphics context
        g.dispose();

        // Write the output image
        return outputStream -> ImageIO.write(inputImage,"png", outputStream);

    }

    @PostMapping("/custom")
    public StreamingResponseBody customImage(HttpServletResponse response, @RequestBody String theText) throws IOException, FontFormatException {
        BufferedImage image = ImageIO.read(templateInputStream(TEMPLATE));
        // Set the font to the right size, before the text is overlayed onto the image
        Font font = fit(getFont(), theText, image);
        java.text.AttributedString attributedText = new java.text.AttributedString(theText);

        attributedText.addAttribute(TextAttribute.FONT, font);
        attributedText.addAttribute(TextAttribute.FOREGROUND, Color.BLACK);
        Graphics g = image.getGraphics();

        FontMetrics metrics = g.getFontMetrics(font);
        int positionY = (image.getHeight() - metrics.getHeight() - BOTTOM_PAD) + metrics.getAscent();

        g.drawString(attributedText.getIterator(), LEFT_PAD, positionY);

        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=blank.png");
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        ImageIO.write(image,"png",boas);
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(boas.toByteArray().length));
        return outputStream -> ImageIO.write(image,"png", outputStream);
    }

    private Font fit(Font baseFont, String text, BufferedImage image) {
        Font newFont = baseFont;

        FontMetrics ruler = image.getGraphics().getFontMetrics(baseFont);
        GlyphVector vector = baseFont.createGlyphVector(ruler.getFontRenderContext(), text + "___");

        Shape outline = vector.getOutline(0, 0);

        double expectedWidth = outline.getBounds().getWidth();
        double expectedHeight = outline.getBounds().getHeight();

        boolean textFits = image.getWidth() >= expectedWidth && image.getHeight() >= expectedHeight;

        if (!textFits) {
            double widthBasedFontSize = (baseFont.getSize2D() * image.getWidth()) / expectedWidth;
            double heightBasedFontSize = (baseFont.getSize2D() * image.getHeight()) / expectedHeight;

            double newFontSize = Math.min(widthBasedFontSize, heightBasedFontSize);
            newFont = baseFont.deriveFont(baseFont.getStyle(), (float) newFontSize);
        }
        return newFont;
    }

    private InputStream templateInputStream(String file) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(file);
    }


    private Font getFont() throws IOException, FontFormatException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream(FONT);
        assert is != null;
        Font ttf = Font.createFont(Font.TRUETYPE_FONT, is);
        return ttf.deriveFont(ttf.getSize2D() * 80);
    }

}
