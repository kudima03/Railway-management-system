package application;

import clientConnectionModule.interfaces.AdminAccess;
import com.ibm.icu.text.Transliterator;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import databaseEntities.Classes.Ticket;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

public class PDFManager {

    private static final String ticketsFolder = "";
    private static final String reportsFolder = "";

    private static String transliteration(String strInCyrillic) {

        var CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        return toLatinTrans.transliterate(strInCyrillic);
    }

    private static void createTicket(Ticket ticket) throws DocumentException, IOException {

        if (ticket == null) return;
        var passenger = ticket.getPassenger();

        BaseFont baseFont = BaseFont.createFont(Objects.requireNonNull(Application.class.getResource(
                        "/application/styles/fonts/ChampagneAndLimousinesBoldItalic-dqex.ttf")).toString(),
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        String content = "Ticket #" + ticket.getId() + '\n' +
                "Departure station: " + ticket.getDepartureStation().getName() + '\n' +
                "Arrival station: " + ticket.getArrivalStation().getName() + '\n' +
                "Route number: " + ticket.getRoute().getNumber() + '\n' +
                "Cost: " + ticket.getCost() + '\n' +
                "Passenger : " + passenger.getSurname() + " " + passenger.getName() + " " + passenger.getPatronymic() + '\n' +
                "Date of birth : " + passenger.getDateOfBirth().getDate() + "." +
                (passenger.getDateOfBirth().getMonth() + 1) + "." +
                (passenger.getDateOfBirth().getYear() + 1900) + '\n' +
                "Document number: " + ticket.getPassenger().getDocumentNumber();

        content = transliteration(content);

        try {
            Document document = new Document(new Rectangle(135, 135));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(ticketsFolder + "Билет №" + ticket.getId() + ".pdf"));
            document.open();
            BarcodeQRCode my_code = new BarcodeQRCode(content, 1, 1, null);
            Image qr_image = my_code.getImage();
            qr_image.setAlignment(Element.ALIGN_CENTER);
            document.add(qr_image);
            document.close();
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка", e.getMessage());
        }
    }

    public static void openPdfTicketByDefaultSystemViewer(Ticket ticket) {

        Properties properties;
        try {
            properties = Application.getPropertiesFromConfig();
        } catch (IOException e) {
            AlertManager.showErrorAlert("Ошибка", "Невозможно получить данные из файла конфигурации");
            return;
        }
        //TODO: from config (encoding error)

        String path = ticketsFolder + "Билет №" + ticket.getId() + ".pdf";
        File file = new File(path);
        if (!file.exists()) {
            try {
                PDFManager.createTicket(ticket);
            } catch (DocumentException | IOException e) {
                AlertManager.showErrorAlert("Ошибка создания PDF", e.getMessage());
            }
        }
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (IOException ex) {
            AlertManager.showErrorAlert("Нет приложения для просмотра pdf", "");
        }
    }

    public static void createRoutesPopularityReport(AdminAccess access) throws Exception {

        Date date = new Date();
        var dateStr = date.getDate() + "." + (date.getMonth() + 1) + "." + (date.getYear() + 1900);
        String filePath = reportsFolder + "Отчет о количестве проданных билетов на " + dateStr + ".pdf";
        Document document = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        BaseFont baseFont = BaseFont.createFont(Objects.requireNonNull(Application.class.getResource(
                        "/application/styles/fonts/ChampagneAndLimousinesBoldItalic-dqex.ttf")).toString(),
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


        var title = new Paragraph("Отчет популярности направлений за последние 30 дней", new Font(baseFont, 18));
        title.setAlignment(Element.ALIGN_CENTER);

        float columnWidth = 520F;
        float[] columns = {columnWidth, columnWidth};

        PdfPTable table = new PdfPTable(columns);
        table.setSpacingBefore(40F);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        var nameText = new Paragraph("Название маршрута", new Font(baseFont, 14));
        nameText.setAlignment(Element.ALIGN_CENTER);
        var routeNameColumnCell = new PdfPCell(nameText);

        var amountText = new Paragraph("Количество купленных билетов", new Font(baseFont, 14));
        amountText.setAlignment(Element.ALIGN_CENTER);
        var amountOfTicketsColumnCell = new PdfPCell(amountText);

        table.addCell(routeNameColumnCell);
        table.addCell(amountOfTicketsColumnCell);

        var routes = access.getAllRoutes();

        for (var route : routes) {

            var routeStations = access.getAllRouteStationsFromRoute(route.getId());
            if (routeStations.size() == 0) continue;

            var startStation = routeStations.stream().min((var element1, var element2) ->
                    element1.getOrdinalNumber() > element2.getOrdinalNumber() ? 1 : -1).get().getStation();

            var stopStation = routeStations.stream().min((var element1, var element2) ->
                    element1.getOrdinalNumber() < element2.getOrdinalNumber() ? 1 : -1).get().getStation();

            var cellContent = route.getNumber() +
                    " " + startStation.getName()
                    + "--" + stopStation.getName();
            table.addCell(new Paragraph(cellContent, new Font(baseFont, 14)));

            int amount = access.getRouteTicketsAmountForLastMonth(route.getId());
            table.addCell(String.valueOf(amount));
        }

        document.open();

        document.add(title);
        document.add(table);

        document.close();
        pdfWriter.close();

        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException ex) {
            AlertManager.showErrorAlert("Нет приложения для просмотра pdf", "");
        }

    }

}
