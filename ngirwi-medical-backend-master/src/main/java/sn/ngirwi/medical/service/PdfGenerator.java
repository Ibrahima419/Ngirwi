package sn.ngirwi.medical.service;


import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;


import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import sn.ngirwi.medical.domain.Hospitalisation;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;


public class PdfGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static byte[] generate(Hospitalisation h) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 40, 40, 60, 50);
            PdfWriter.getInstance(document, baos);
            document.open();

            // ---------- Styles ----------
            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font labelFont = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font valueFont = new Font(Font.HELVETICA, 11);

            // ---------- ENTÊTE ----------
            Paragraph title = new Paragraph("CLINIQUE NGIRWI - FICHE D'HOSPITALISATION", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph sub = new Paragraph("Document médical confidentiel", valueFont);
            sub.setAlignment(Element.ALIGN_CENTER);
            document.add(sub);

            document.add(new Paragraph("\n"));

            // ---------- INFOS PATIENT ----------
            document.add(new Paragraph("📋 Informations du Patient", headerFont));
            document.add(new Paragraph("\n"));

            PdfPTable patientTable = new PdfPTable(2);
            patientTable.setWidthPercentage(100);

            patientTable.addCell(cell("Nom & Prénom :", true));
            patientTable.addCell(cell(h.getPatient().getLastName() + " " + h.getPatient().getFirstName(), false));

            patientTable.addCell(cell("Sexe :", true));
            patientTable.addCell(cell(String.valueOf(h.getPatient().getGender()), false));

            patientTable.addCell(cell("Téléphone :", true));
            patientTable.addCell(cell(h.getPatient().getPhone(), false));

            patientTable.addCell(cell("Adresse :", true));
            patientTable.addCell(cell(h.getPatient().getAdress(), false));

            patientTable.addCell(cell("Groupe sanguin :", true));
            patientTable.addCell(cell(String.valueOf(h.getPatient().getBloodType()), false));

            patientTable.addCell(cell("Situation matrimoniale :", true));
            patientTable.addCell(cell(String.valueOf(h.getPatient().getMaritialStatus()), false));

            document.add(patientTable);
            document.add(new Paragraph("\n"));

            // ---------- INFOS HOSPITALISATION ----------
            document.add(new Paragraph("🏥 Details de l’Hospitalisation", headerFont));
            document.add(new Paragraph("\n"));

            PdfPTable hospTable = new PdfPTable(2);
            hospTable.setWidthPercentage(100);

            hospTable.addCell(cell("ID Hospitalisation:", true));
            hospTable.addCell(cell(String.valueOf(h.getId()), false));

            hospTable.addCell(cell("Médecin responsable :", true));
            hospTable.addCell(cell(h.getDoctorName(), false));

            hospTable.addCell(cell("Date d'entrée :", true));
            hospTable.addCell(cell(formatDate(h.getEntryDate()), false));

            hospTable.addCell(cell("Date de sortie :", true));
            hospTable.addCell(cell(formatDate(h.getReleaseDate()), false));

            hospTable.addCell(cell("Service :", true));
            hospTable.addCell(cell(h.getService(), false));

            hospTable.addCell(cell("Statut :", true));
            hospTable.addCell(cell(h.getStatus().name(), false));

            hospTable.addCell(cell("Motif d'admission :", true));
            hospTable.addCell(cell(h.getAdmissionReason(), false));

            document.add(hospTable);
            document.add(new Paragraph("\n"));

            // ---------- DIAGNOSTIC ----------
            document.add(new Paragraph("🩺 Diagnostic", headerFont));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Diagnostic d'entrée :", labelFont));
            document.add(new Paragraph(defaultValue(h.getEntryDiagnosis()), valueFont));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Diagnostic final :", labelFont));
            document.add(new Paragraph(defaultValue(h.getFinalDiagnosis()), valueFont));
            document.add(new Paragraph("\n\n"));

            // ---------- FOOTER ----------
            Paragraph disclaimer = new Paragraph("Ce document est strictement confidentiel et protégé par le secret médical.", valueFont);
            disclaimer.setAlignment(Element.ALIGN_CENTER);
            document.add(disclaimer);

            document.add(new Paragraph("\n\nFait le : " + DATE_FORMAT.format(java.time.Instant.now().atZone(java.time.ZoneId.systemDefault()))));
            document.add(new Paragraph("\n\nSignature et cachet du médecin\n\n____________________________"));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    // -------------------- Utilities --------------------

    private static PdfPCell cell(String text, boolean bold) {
        Font font = bold ? new Font(Font.HELVETICA, 11, Font.BOLD) : new Font(Font.HELVETICA, 11);
        PdfPCell c = new PdfPCell(new Phrase(defaultValue(text), font));
        c.setPadding(8);
        return c;
    }

    private static String formatDate(Instant instant) {
        return instant != null ? DATE_FORMAT.format(instant.atZone(java.time.ZoneId.systemDefault())) : "N/A";
    }

    private static String defaultValue(String v) {
        return (v == null || v.isBlank()) ? "N/A" : v;
    }
}

