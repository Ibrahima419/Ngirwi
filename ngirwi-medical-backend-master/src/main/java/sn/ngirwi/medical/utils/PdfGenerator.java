package sn.ngirwi.medical.utils;

import java.io.ByteArrayOutputStream;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import sn.ngirwi.medical.domain.Hospitalisation;

public class PdfGenerator {

    public static byte[] generate(Hospitalisation h) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            document.add(new Paragraph("RÉSUMÉ D'HOSPITALISATION", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("ID : " + h.getId(), labelFont));
            document.add(new Paragraph("Patient : " + (h.getPatient() != null ? h.getPatient() : "-")));
            document.add(new Paragraph("Date d'entrée : " + h.getEntryDate()));
            document.add(new Paragraph("Date de sortie : " + h.getReleaseDate()));
            document.add(new Paragraph("Statut : " + h.getStatus()));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Motif d'admission : " + safe(h.getAdmissionReason())));
            document.add(new Paragraph("Diagnostic d'entrée : " + safe(h.getEntryDiagnosis())));
            document.add(new Paragraph("Diagnostic final : " + safe(h.getFinalDiagnosis())));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Merci pour votre confiance.", labelFont));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur PDF: " + e.getMessage(), e);
        }
    }

    public static String safe(String text) {
        return text == null ? "Non renseigné" : text;
    }
}
