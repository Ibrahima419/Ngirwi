package sn.ngirwi.medical.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sn.ngirwi.medical.domain.Hospital;
import sn.ngirwi.medical.domain.Hospitalisation;
import sn.ngirwi.medical.service.dto.HospitalisationResumeDTO;

/**
 * PDF Generator for Hospitalisation summaries.
 * Generates a professional PDF document with hospital logo, patient info,
 * hospitalisation details, medications, acts, and billing summary.
 */
public class PdfGenerator {

    private static final Logger log = LoggerFactory.getLogger(PdfGenerator.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
        .ofPattern("dd/MM/yyyy HH:mm")
        .withZone(ZoneId.of("Africa/Dakar"));
    private static final NumberFormat FCFA_FORMAT = NumberFormat.getInstance(Locale.FRANCE);

    /**
     * Generates a PDF for hospitalisation summary.
     *
     * @param h        The hospitalisation entity with surveillanceSheets loaded
     * @param hospital The hospital entity (for dynamic logo), can be null
     * @param resume   The billing resume DTO with calculated totals, can be null
     * @return byte[] containing the PDF
     */
    public static byte[] generate(Hospitalisation h, Hospital hospital, HospitalisationResumeDTO resume) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 25, 25, 20, 20);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            addWatermark(writer);

            Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font sectionFont = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 9);
            Font smallFont = new Font(Font.HELVETICA, 8);
            Font totalFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            addHeader(document, hospital, normalFont, headerFont);
            document.add(new LineSeparator());

            Paragraph title = new Paragraph("\nDOCUMENT DE SORTIE D'HOSPITALISATION\n\n", headerFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("INFORMATIONS DU PATIENT", sectionFont));

            PdfPTable patientTable = new PdfPTable(2);
            patientTable.setWidthPercentage(100);
            patientTable.setSpacingBefore(5);
            patientTable.setSpacingAfter(10);
            patientTable.setWidths(new float[]{30, 70});

            if (h.getPatient() != null) {
                cell(patientTable, "Nom :", safe(h.getPatient().getLastName()), normalFont);
                cell(patientTable, "Prénom :", safe(h.getPatient().getFirstName()), normalFont);
                cell(patientTable, "Téléphone :", safe(h.getPatient().getPhone()), normalFont);
                cell(patientTable, "Adresse :", safe(h.getPatient().getAdress()), normalFont);
            } else {
                cell(patientTable, "Patient :", "Non renseigné", normalFont);
            }
            document.add(patientTable);

            document.add(new Paragraph("INFORMATIONS DE L'HOSPITALISATION", sectionFont));

            PdfPTable hospTable = new PdfPTable(2);
            hospTable.setWidthPercentage(100);
            hospTable.setSpacingBefore(5);
            hospTable.setSpacingAfter(10);
            hospTable.setWidths(new float[]{35, 65});

            cell(hospTable, "N° Hospitalisation :", "H" + h.getId(), normalFont);
            cell(hospTable, "Service :", safe(h.getService()), normalFont);
            cell(hospTable, "Médecin traitant :", safe(h.getDoctorName()), normalFont);
            cell(hospTable, "Date d'entrée :", formatDate(h.getEntryDate()), normalFont);
            cell(hospTable, "Date de sortie :", formatDate(h.getReleaseDate()), normalFont);
            cell(hospTable, "Durée (jours) :", resume != null ? String.valueOf(resume.getNumberOfDays()) : "-", normalFont);
            cell(hospTable, "Motif d'admission :", safe(h.getAdmissionReason()), normalFont);
            cell(hospTable, "Diagnostic d'entrée :", safe(h.getEntryDiagnosis()), normalFont);
            cell(hospTable, "Diagnostic final :", safe(h.getFinalDiagnosis()), normalFont);

            document.add(hospTable);

            document.add(new Paragraph("MÉDICAMENTS ADMINISTRÉS", sectionFont));

            PdfPTable medsTable = new PdfPTable(5);
            medsTable.setWidthPercentage(100);
            medsTable.setSpacingBefore(5);
            medsTable.setSpacingAfter(10);
            medsTable.setWidths(new float[]{25, 15, 15, 20, 25});

            th(medsTable, "Nom");
            th(medsTable, "Quantité");
            th(medsTable, "Prix Unit.");
            th(medsTable, "Total");
            th(medsTable, "Date");

            boolean hasMeds = false;
            if (h.getSurveillanceSheets() != null) {
                for (var sheet : h.getSurveillanceSheets()) {
                    if (sheet.getMedications() != null) {
                        for (var m : sheet.getMedications()) {
                            hasMeds = true;
                            td(medsTable, safe(m.getNom()));
                            td(medsTable, m.getQuantite() != null ? String.valueOf(m.getQuantite()) : "-");
                            td(medsTable, formatFcfa(m.getPrixUnitaire()));
                            td(medsTable, formatFcfa(m.getTotal()));
                            td(medsTable, sheet.getSheetDate() != null ? sheet.getSheetDate().toString() : "-");
                        }
                    }
                }
            }

            if (!hasMeds) {
                PdfPCell emptyCell = new PdfPCell(new Phrase("Aucun médicament enregistré", smallFont));
                emptyCell.setColspan(5);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell.setPadding(10);
                medsTable.addCell(emptyCell);
            }

            document.add(medsTable);

            document.add(new Paragraph("ACTES RÉALISÉS", sectionFont));

            PdfPTable actsTable = new PdfPTable(5);
            actsTable.setWidthPercentage(100);
            actsTable.setSpacingBefore(5);
            actsTable.setSpacingAfter(10);
            actsTable.setWidths(new float[]{25, 15, 15, 20, 25});

            th(actsTable, "Acte");
            th(actsTable, "Quantité");
            th(actsTable, "Prix Unit.");
            th(actsTable, "Total");
            th(actsTable, "Date");

            boolean hasActs = false;
            if (h.getSurveillanceSheets() != null) {
                for (var sheet : h.getSurveillanceSheets()) {
                    if (sheet.getActs() != null) {
                        for (var a : sheet.getActs()) {
                            hasActs = true;
                            td(actsTable, safe(a.getNom()));
                            td(actsTable, a.getQuantite() != null ? String.valueOf(a.getQuantite()) : "-");
                            td(actsTable, formatFcfa(a.getPrixUnitaire()));
                            td(actsTable, formatFcfa(a.getTotal()));
                            td(actsTable, sheet.getSheetDate() != null ? sheet.getSheetDate().toString() : "-");
                        }
                    }
                }
            }

            if (!hasActs) {
                PdfPCell emptyCell = new PdfPCell(new Phrase("Aucun acte enregistré", smallFont));
                emptyCell.setColspan(5);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell.setPadding(10);
                actsTable.addCell(emptyCell);
            }

            document.add(actsTable);

            document.add(new Paragraph("RÉSUMÉ DE FACTURATION", sectionFont));

            PdfPTable billingTable = new PdfPTable(2);
            billingTable.setWidthPercentage(60);
            billingTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            billingTable.setSpacingBefore(5);
            billingTable.setSpacingAfter(10);

            if (resume != null) {
                cell(billingTable, "Forfait séjour :", formatFcfa(resume.getForfaitSejour()), normalFont);
                cell(billingTable, "Frais de confort :", formatFcfa(resume.getComfortFees()), normalFont);
                cell(billingTable, "Dépassements :", formatFcfa(resume.getFeeOverrun()), normalFont);
                cell(billingTable, "Médicaments :", formatFcfa(resume.getMedsTotal()), normalFont);
                cell(billingTable, "Actes médicaux :", formatFcfa(resume.getActsTotal()), normalFont);
                cell(billingTable, "Sous-total :", formatFcfa(resume.getSubtotal()), normalFont);
                cell(billingTable, "Couverture assurance :", 
                    (resume.getInsuranceCoveragePercent() != null ? resume.getInsuranceCoveragePercent() : BigDecimal.ZERO) + " %", 
                    normalFont);
            } else {
                cell(billingTable, "Statut :", "Facturation non finalisée", normalFont);
            }

            document.add(billingTable);

            Paragraph totalPara = new Paragraph();
            totalPara.setSpacingBefore(10);
            totalPara.add(new Chunk("MONTANT TOTAL À PAYER : ", sectionFont));
            totalPara.add(new Chunk(
                resume != null ? formatFcfa(resume.getTotalAmount()) : "Non calculé",
                totalFont
            ));
            document.add(totalPara);

            document.add(new Paragraph("\n\n"));
            document.add(new LineSeparator());

            Paragraph signature = new Paragraph("\nSignature du médecin : _______________________\n\n", normalFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            Paragraph footer = new Paragraph();
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.add(new Chunk("Document généré automatiquement - Propulsé par NGIRWI S.A.R.L\n", smallFont));
            footer.add(new Chunk("www.ngirwisarl.com", smallFont));
            document.add(footer);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("PDF generation failed: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la génération du PDF: " + e.getMessage(), e);
        }
    }

    private static void addHeader(Document document, Hospital hospital, Font normalFont, Font headerFont)
            throws DocumentException {
        Image logo = null;
        if (hospital != null && hospital.getLogo() != null && hospital.getLogo().length > 0) {
            try {
                logo = Image.getInstance(hospital.getLogo());
                logo.scaleToFit(120, 60);
                logo.setAlignment(Image.ALIGN_CENTER);
                logo.setSpacingAfter(8f);
                document.add(logo);
            } catch (Exception e) {
                log.warn("Could not load hospital logo from database: {}", e.getMessage());
                logo = null;
            }
        }

        if (logo == null) {
            try {
                InputStream logoStream = PdfGenerator.class.getResourceAsStream("/static/images/logo.jpg");
                if (logoStream != null) {
                    logo = Image.getInstance(logoStream.readAllBytes());
                    logo.scaleToFit(120, 60);
                    logo.setAlignment(Image.ALIGN_CENTER);
                    logo.setSpacingAfter(8f);
                    document.add(logo);
                }
            } catch (Exception e) {
                log.warn("Could not load static logo: {}", e.getMessage());
            }
        }

        String hospitalName = hospital != null && hospital.getName() != null ? hospital.getName() : "Établissement Médical";
        String hospitalPhone = hospital != null && hospital.getPhone() != null ? "Tél : " + hospital.getPhone() : "";
        String hospitalAddress = hospital != null && hospital.getAdress() != null ? hospital.getAdress() : "";

        Paragraph hospitalInfo = new Paragraph();
        hospitalInfo.setAlignment(Element.ALIGN_CENTER);
        hospitalInfo.add(new Chunk(hospitalName + "\n", headerFont));
        if (!hospitalPhone.isEmpty()) {
            hospitalInfo.add(new Chunk(hospitalPhone + "\n", normalFont));
        }
        if (!hospitalAddress.isEmpty()) {
            hospitalInfo.add(new Chunk(hospitalAddress + "\n", normalFont));
        }
        hospitalInfo.add(new Chunk("\n"));
        document.add(hospitalInfo);
    }

    private static void addWatermark(PdfWriter writer) {
        try {
            InputStream is = PdfGenerator.class.getResourceAsStream("/static/images/NgirwiLogo.png");
            if (is != null) {
                Image watermark = Image.getInstance(is.readAllBytes());
                watermark.scaleAbsolute(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                watermark.setAbsolutePosition(0, 0);

                PdfContentByte under = writer.getDirectContentUnder();
                PdfGState gs = new PdfGState();
                gs.setFillOpacity(0.08f);
                under.saveState();
                under.setGState(gs);
                under.addImage(watermark);
                under.restoreState();
            }
        } catch (Exception e) {
            log.warn("Could not add watermark: {}", e.getMessage());
        }
    }

    private static void cell(PdfPTable table, String label, String value, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPaddingBottom(3);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "-", font));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPaddingBottom(3);
        table.addCell(valueCell);
    }

    private static void th(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 9, Font.BOLD)));
        cell.setBackgroundColor(new Color(220, 220, 220));
        cell.setPadding(5);
        table.addCell(cell);
    }

    private static void td(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "-", new Font(Font.HELVETICA, 8)));
        cell.setPadding(4);
        table.addCell(cell);
    }

    /**
     * Returns "Non renseigné" if text is null or blank.
     */
    public static String safe(String text) {
        return text == null || text.isBlank() ? "Non renseigné" : text;
    }

    private static String formatDate(java.time.Instant instant) {
        if (instant == null) return "-";
        return DATE_FORMAT.format(instant);
    }

    private static String formatFcfa(BigDecimal amount) {
        if (amount == null) return "0 FCFA";
        return FCFA_FORMAT.format(amount.longValue()) + " FCFA";
    }
}
