package com.sample.pdf.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class PdfService {

  @Autowired
  private QrCodeService qrCodeService;

  public boolean pdfGenerator(String userID, String walletData, String pwData) {
    String fileName = "test.pdf";
    String path = "files/pdf";
    File dir = new File(path);
    if(!dir.exists())
      dir.mkdirs();

    Document document = new Document();
    try {
      // header table
      PdfPTable headerTable = new PdfPTable(2);
      headerTable.setWidthPercentage(100);
      headerTable.setTotalWidth(new float[]{ 57, 43 });

      Font titleFont = new Font();
      titleFont.setColor(BaseColor.BLUE);
      titleFont.setSize(20);

      Font subjectFont = new Font();
      subjectFont.setSize(16);

      PdfPCell titleCell = new PdfPCell();
      titleCell.setBorder(Rectangle.NO_BORDER);
      titleCell.addElement(new Phrase("Talken", titleFont));

      PdfPCell subjectCell = new PdfPCell();
      subjectCell.setBorder(Rectangle.NO_BORDER);
      //subjectCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
      subjectCell.addElement(new Phrase("Document for Wallet Recovery", subjectFont));

      headerTable.addCell(titleCell);
      headerTable.addCell(subjectCell);

      // warning table
      PdfPTable warningTable = new PdfPTable(1);
      warningTable.setSpacingBefore(15);
      warningTable.setWidthPercentage(100);
      warningTable.getDefaultCell().setFixedHeight(23);
      warningTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

      Font warningFont = new Font();
      warningFont.setColor(BaseColor.RED);
      warningFont.setSize(15);
      warningTable.addCell(new Phrase("CAUTION!! Print this document or keep it securely offline.", warningFont));

      // info table
      PdfPTable infoTable = new PdfPTable(1);
      infoTable.setSpacingBefore(15);
      infoTable.setWidthPercentage(100);
      infoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
      infoTable.addCell("Wallet info :");
      infoTable.addCell("Created on " + new Date() + " by " + UUID.randomUUID().toString().replace("-", ""));

      // wallet recovery code table
      PdfPTable walletTable = new PdfPTable(2);
      walletTable.setWidthPercentage(100);
      walletTable.setTotalWidth(new float[]{ 20, 80 });

      PdfPCell walletCell = new PdfPCell(new Phrase("Wallet Recovery Code"));
      walletCell.setColspan(2);
      walletCell.setPaddingTop(10);
      walletCell.setPaddingLeft(20);
      walletCell.setPaddingBottom(5);
      walletCell.setBorder(Rectangle.NO_BORDER);

      PdfPCell walletQrCell = new PdfPCell();
      walletQrCell.addElement(Image.getInstance(qrCodeService.qrCodeGenerator(walletData)));
      walletQrCell.setPaddingLeft(10);
      walletQrCell.setBorder(Rectangle.NO_BORDER);

      PdfPCell walletDataCell = new PdfPCell(new Phrase("Data:" + "\n" + walletData));
      walletDataCell.setPaddingLeft(10);
      walletDataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      walletDataCell.setBorder(Rectangle.NO_BORDER);

      walletTable.addCell(walletCell);
      walletTable.addCell(walletQrCell);
      walletTable.addCell(walletDataCell);

      // wallet recovery code border table
      PdfPTable walletBorderTable = new PdfPTable(1);
      walletBorderTable.setSpacingBefore(10);
      walletBorderTable.setWidthPercentage(100);
      walletBorderTable.addCell(walletTable);

      // pw recovery code table
      PdfPTable pwTable = new PdfPTable(2);
      pwTable.setWidthPercentage(100);
      pwTable.setTotalWidth(new float[]{ 20, 80 });

      PdfPCell pwCell = new PdfPCell(new Phrase("PW Recovery Code"));
      pwCell.setColspan(2);
      pwCell.setPaddingTop(10);
      pwCell.setPaddingLeft(20);
      pwCell.setPaddingBottom(5);
      pwCell.setBorder(Rectangle.NO_BORDER);

      PdfPCell pwQrCell = new PdfPCell();
      pwQrCell.addElement(Image.getInstance(qrCodeService.qrCodeGenerator(pwData)));
      pwQrCell.setPaddingLeft(10);
      pwQrCell.setBorder(Rectangle.NO_BORDER);

      PdfPCell pwDataCell = new PdfPCell(new Phrase("Data:" + "\n" + pwData));
      pwDataCell.setPaddingLeft(10);
      pwDataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      pwDataCell.setBorder(Rectangle.NO_BORDER);

      pwTable.addCell(pwCell);
      pwTable.addCell(pwQrCell);
      pwTable.addCell(pwDataCell);

      // pw recovery code border table
      PdfPTable pwBorderTable = new PdfPTable(1);
      pwBorderTable.setSpacingBefore(10);
      pwBorderTable.setWidthPercentage(100);
      pwBorderTable.addCell(pwTable);

      // notice table
      PdfPTable noticeTable = new PdfPTable(1);
      noticeTable.setSpacingBefore(20);
      noticeTable.setWidthPercentage(100);
      noticeTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
      noticeTable.addCell("Notice");
      noticeTable.addCell(" - This document...........");

      FileOutputStream out = new FileOutputStream (path + "/" + fileName);
      PdfWriter.getInstance(document, out);

      document.open();
      document.add(headerTable);
      document.add(warningTable);
      document.add(infoTable);
      document.add(walletBorderTable);
      document.add(pwBorderTable);
      document.add(noticeTable);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      document.close();
    }

    return true;
  }

}
