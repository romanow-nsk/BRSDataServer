package firefighter;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import firefighter.core.UniException;
import firefighter.core.utils.FileNameExt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class QRCode {
    public void createQRImage(FileNameExt fspec, String qrCodeText, int size) throws UniException {
        // Create the ByteMatrix for the QR-Code that encodes the given String
        //Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = null;
        try {
            byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
            } catch (WriterException e) { UniException.io(e); }
        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
        try {
            String fname = fspec.fullName();
            ImageIO.write(image, fspec.getExt(), new File(fname));
            } catch (IOException e) { UniException.io(e); }
        }

    public static void main(String[] args) throws UniException {
        String qrCodeText = "Как сообщает РИА Новости, российский лидер сделал несколько кругов по манежу верхом на гнедом коне";
        String filePath = "d:/temp/news.png";
        int size = 125;
        QRCode qr = new QRCode();
        qr.createQRImage(new FileNameExt(filePath), qrCodeText, size);
        }
}
