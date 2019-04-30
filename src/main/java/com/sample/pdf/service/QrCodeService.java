package com.sample.pdf.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;

@Service
public class QrCodeService {

  private final static String FILE_TYPE = "png";

  public String qrCodeGenerator(String text) {
    String fileName = "test.png";
    String path = "files/qr";
    int size = 250;
    File file = new File(path + "/" + fileName);
    if(!file.exists())
      file.mkdirs();

    try {
      Map<EncodeHintType, Object> option = new EnumMap<>(EncodeHintType.class);
      option.put(EncodeHintType.CHARACTER_SET, "UTF-8");
      option.put(EncodeHintType.MARGIN, 1);
      option.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      BitMatrix byteMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size, option);
      int qrCodeWidth = byteMatrix.getWidth();
      BufferedImage image = new BufferedImage(qrCodeWidth, qrCodeWidth, BufferedImage.TYPE_INT_RGB);
      image.createGraphics();

      Graphics2D graphics = (Graphics2D) image.getGraphics();
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, qrCodeWidth, qrCodeWidth);
      graphics.setColor(Color.BLACK);

      for (int i = 0; i < qrCodeWidth; i++) {
        for (int j = 0; j < qrCodeWidth; j++) {
          if (byteMatrix.get(i, j)) {
            graphics.fillRect(i, j, 1, 1);
          }
        }
      }

      ImageIO.write(image, FILE_TYPE, file);

      return file.getPath();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

}
