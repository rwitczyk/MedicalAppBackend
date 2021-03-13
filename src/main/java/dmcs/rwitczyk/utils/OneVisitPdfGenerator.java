package dmcs.rwitczyk.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import dmcs.rwitczyk.domains.DoctorEntity;
import dmcs.rwitczyk.domains.OneVisitEntity;
import dmcs.rwitczyk.domains.PatientEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

@Slf4j
@RequiredArgsConstructor(staticName = "getInstance")
public class OneVisitPdfGenerator {

    private final OneVisitEntity oneVisitEntity;

    public ByteArrayOutputStream generatePdf() {
        log.info("Generuje pdfa dla wizyty od id: " + oneVisitEntity.getId());
        ByteArrayOutputStream outputPdf = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, outputPdf);
            document.open();
            addMetaData(document);
            addContent(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputPdf;
    }

    private void addContent(Document document) throws DocumentException {
        DoctorEntity doctorEntity = oneVisitEntity.getDoctorEntity();
        PatientEntity patientEntity = oneVisitEntity.getPatientEntity();

        Paragraph p = new Paragraph();
        p.add("Doktor: " + doctorEntity.getFirstName() + " " + doctorEntity.getLastName() + " " + doctorEntity.getSpecialization());
        addEmptyLine(p, 2);
        document.add(p);

        Paragraph p2 = new Paragraph();
        p2.add("Pacjent: " + patientEntity.getFirstName() + " " + patientEntity.getLastName() + ", Telefon: " + patientEntity.getPhoneNumber());
        addEmptyLine(p2, 2);
        document.add(p2);

        Paragraph p3 = new Paragraph();
        p3.add("Wizyta:");
        document.add(p3);

        Paragraph p4 = new Paragraph();
        p4.add("Data i czas: " + oneVisitEntity.getDate() + " " + oneVisitEntity.getFromTime() + " - " + oneVisitEntity.getToTime());
        document.add(p4);

        Paragraph p5 = new Paragraph();
        p5.add("Opis wizyty: " + oneVisitEntity.getDescription());
        document.add(p5);

        Paragraph p6 = new Paragraph();
        p6.add("Cena: " + oneVisitEntity.getPrice() + "PLN");
        document.add(p6);
    }

    private void addMetaData(Document document) {
        document.addTitle("E-PACJENT");
        document.addSubject("Rachunek za wizyte");
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
