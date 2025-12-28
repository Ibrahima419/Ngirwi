package sn.ngirwi.medical.service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import sn.ngirwi.medical.domain.Hospitalisation;

import static sn.ngirwi.medical.utils.PdfGenerator.safe;

public class PdfGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");



    public static byte[] generate(Hospitalisation h) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 25, 25, 20, 20);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            /* ================= WATERMARK ================= */
            InputStream is = PdfGenerator.class
                .getResourceAsStream("/static/images/NgirwiLogo.png");

            Image watermark = Image.getInstance(is.readAllBytes());
            watermark.scaleAbsolute(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            watermark.setAbsolutePosition(0, 0);

            PdfContentByte under = writer.getDirectContentUnder();
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.8f);
            under.saveState();
            under.setGState(gs);
            under.addImage(watermark);
            under.restoreState();

            /* ================= FONTS ================= */
            Font header = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font section = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 9);

            /* ================= HEADER ================= */
            Paragraph headerInfo = new Paragraph(
                "Tel : 777777777\nAdresse : Medina Gounass/Guediawaye\n\n",
                normal
            );
            headerInfo.setAlignment(Element.ALIGN_CENTER);




            /* ================= LOGO HOPITAL (EN HAUT) ================= */


            InputStream logoStream = PdfGenerator.class
                .getResourceAsStream("/static/images/logo.jpg");

            if (logoStream == null) {
                throw new RuntimeException("Logo hôpital introuvable");
            }

            Image logoTop = Image.getInstance(logoStream.readAllBytes());

            // Taille du logo (ajustable au millimètre)
            logoTop.scaleAbsolute(150, 70);

            // Position : centré en haut
            logoTop.setAlignment(Image.ALIGN_CENTER);

            // Espacement bas (pour ne pas coller le texte)
            logoTop.setSpacingAfter(10f);

            document.add(logoTop);

            document.add(headerInfo);

            LineSeparator ls = new LineSeparator();
            document.add(ls);

            Paragraph title = new Paragraph(
                "\nDOCUMENT DE SORTIE D’HOSPITALISATION\n\n",
                header
            );
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            /* ================= PATIENT INFO ================= */
            document.add(new Paragraph("INFORMATIONS DU PATIENT :", section));

            PdfPTable patient = new PdfPTable(2);
            patient.setWidthPercentage(100);
            patient.setSpacingBefore(5);
            patient.setSpacingAfter(10);
            patient.setWidths(new float[]{30, 70});

            cell(patient, "Nom :", h.getPatient().getLastName(), normal);
            cell(patient, "Prénom :", h.getPatient().getFirstName(), normal);
            cell(patient, "Date de naissance :", "-", normal);
            cell(patient, "Adresse :", "-", normal);

            document.add(patient);

            /* ================= HOSPITALISATION INFO ================= */
            document.add(new Paragraph("INFORMATIONS DE L’HOSPITALISATION :", section));

            PdfPTable hosp = new PdfPTable(2);
            hosp.setWidthPercentage(100);
            hosp.setSpacingBefore(5);
            hosp.setSpacingAfter(10);
            hosp.setWidths(new float[]{35, 65});

            cell(hosp, "Jour d’admission :", String.valueOf(h.getEntryDate()), normal);
            cell(hosp, "Motif :", safe(h.getAdmissionReason()), normal);
            cell(hosp, "Médecin Traitant :", h.getDoctorName(), normal);
            cell(hosp, "Service :", "-", normal);
            cell(hosp, "Diagnostic Final :", safe(h.getFinalDiagnosis()), normal);

            document.add(hosp);

            /* ================= MEDICAMENTS ================= */
            document.add(new Paragraph("Medicaments Administrés", section));

            PdfPTable meds = new PdfPTable(4);
            meds.setWidthPercentage(100);
            meds.setSpacingBefore(5);
            meds.setSpacingAfter(10);

            th(meds, "Nom");
            th(meds, "Quantité");
            th(meds, "Prix");
            th(meds, "Date");

            h.getSurveillanceSheets().forEach(s ->
                s.getMedications().forEach(m -> {
                    td(meds, m.getNom());
                    td(meds, String.valueOf(m.getQuantite()));
                    td(meds, "1000");
                    td(meds, s.getSheetDate().toString());
                })
            );

            document.add(meds);

            /* ================= ACTES ================= */
            document.add(new Paragraph("Actes Réalisés", section));

            PdfPTable actes = new PdfPTable(4);
            actes.setWidthPercentage(100);

            th(actes, "Acte");
            th(actes, "Commentaire");
            th(actes, "Prix");
            th(actes, "Date");

            h.getSurveillanceSheets().forEach(s ->
                s.getActs().forEach(a -> {
                    td(actes, a.getNom());
                    td(actes, "-");
                    td(actes, "1000");
                    td(actes, s.getSheetDate().toString());
                })
            );

            document.add(actes);

            /* ================= TOTAL ================= */
            document.add(new Paragraph("\nMONTANT TOTAL DE L’HOSPITALISATION :", section));
            document.add(new Paragraph("TOTAL = XXXXX FCFA\n\n", header));

            document.add(new Paragraph("SIGNATURE", section));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* ================= HELPERS ================= */

    private static void cell(PdfPTable t, String l, String v, Font f) {
        t.addCell(new PdfPCell(new Phrase(l, f)));
        t.addCell(new PdfPCell(new Phrase(v != null ? v : "-", f)));
    }

    private static void th(PdfPTable t, String txt) {
        PdfPCell c = new PdfPCell(new Phrase(txt));
        c.setBackgroundColor(Color.LIGHT_GRAY);
        t.addCell(c);
    }

    private static void td(PdfPTable t, String txt) {
        t.addCell(new PdfPCell(new Phrase(txt != null ? txt : "-")));
    }



    private static String formatBP(Integer sys, Integer dia) {
        if (sys == null && dia == null) return "-";
        return (sys != null ? sys : "?") + "/" + (dia != null ? dia : "?");
    }

    private static String extractMedicationNames(sn.ngirwi.medical.domain.SurveillanceSheet sheet) {
        if (sheet.getMedications() == null || sheet.getMedications().isEmpty()) return "-";
        return sheet.getMedications().stream()
            .map(m -> m.getNom() + " (" + m.getQuantite() + ")")
            .reduce((a,b) -> a + ", " + b)
            .orElse("-");
    }

    private static String extractActsNames(sn.ngirwi.medical.domain.SurveillanceSheet sheet) {
        if (sheet.getActs() == null || sheet.getActs().isEmpty()) return "-";
        return sheet.getActs().stream()
            .map(a -> a.getNom() + " (" + a.getQuantite() + ")")
            .reduce((a,b) -> a + ", " + b)
            .orElse("-");
    }

}
