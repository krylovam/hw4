package fintech4;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.*;
import java.util.*;

import static org.apache.http.HttpHeaders.USER_AGENT;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Helper {
    public static void writeToExcel(ArrayList<User> UserMas, int count, String[] header)throws Exception{
        Workbook Excel = new HSSFWorkbook();
        Sheet Sheet = Excel.createSheet("MyWork");
        FileOutputStream fos = new FileOutputStream("my.xls");
        try {

            for (int i = 0; i <= count; ++i) {
                Row row = Sheet.createRow(i);
                if (i == 0) {
                    for (int j = 0; j < 14; j++) {
                        row.createCell(j).setCellValue(header[j]);
                    }
                } else {
                    row.createCell(0).setCellValue(UserMas.get(i - 1).getLastName());
                    row.createCell(1).setCellValue(UserMas.get(i - 1).getFirstName());
                    row.createCell(2).setCellValue(UserMas.get(i-1).getTitle());
                    row.createCell(3).setCellValue(UserMas.get(i - 1).getAge());
                    row.createCell(4).setCellValue(UserMas.get(i - 1).getGender());
                    GregorianCalendar gc = UserMas.get(i-1).getDob();
                    row.createCell(5).setCellValue(String.format("%s%s%s%s%s",gc.get(GregorianCalendar.DAY_OF_MONTH),".",gc.get(GregorianCalendar.MONTH),".",gc.get(GregorianCalendar.YEAR)));
                    row.createCell(6).setCellValue(UserMas.get(i - 1).getInn());
                    row.createCell(7).setCellValue(UserMas.get(i - 1).getPostcode());
                    row.createCell(8).setCellValue(UserMas.get(i-1).getCountry());
                    row.createCell(9).setCellValue(UserMas.get(i - 1).getState());
                    row.createCell(10).setCellValue(UserMas.get(i - 1).getCity());
                    row.createCell(11).setCellValue(UserMas.get(i - 1).getStreet());
                    row.createCell(12).setCellValue(UserMas.get(i - 1).getHouse());
                    row.createCell(13).setCellValue(UserMas.get(i - 1).getFlat());
                }

            }
            Excel.write(fos);
            fos.close();
            System.out.println(String.format("%s%s%s%s","Файл создан. Путь:",System.getProperty("user.dir"), File.separator,"my.xls"));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("error creating Excel file");
        }
        finally{
            fos.close();
        }
    }
    public void writeToPdf(ArrayList<fintech4.User> UserMas, int count, String[] header) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("my.pdf"));
            document.open();
            BaseFont bf =
                    BaseFont.createFont("C:\\Windows\\Fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf);

            PdfPTable table = new PdfPTable(14);
            table.setWidthPercentage(100);
            table.setSpacingBefore(0f);
            table.setSpacingAfter(0f);

            for (int i = 0; i < 14; i++) {
                table.addCell(new Paragraph(header[i], font));
            }
            for (int i = 0; i < count; i++) {
                table.addCell(new Paragraph(UserMas.get(i).getLastName(), font));
                table.addCell(new Paragraph(UserMas.get(i).getFirstName(), font));
                table.addCell(new Paragraph(UserMas.get(i).getTitle(), font));
                table.addCell(new Paragraph(String.valueOf(UserMas.get(i).getAge()), font));
                table.addCell(new Paragraph(UserMas.get(i).getGender(), font));
                GregorianCalendar gc = UserMas.get(i).getDob();
                table.addCell(new Paragraph(String.format("%s%s%s%s%s",gc.get(Calendar.DAY_OF_MONTH),".",gc.get(Calendar.MONTH),".",gc.get(Calendar.YEAR))));
                table.addCell(new Paragraph(UserMas.get(i).getInn(), font));
                table.addCell(new Paragraph(UserMas.get(i).getPostcode()));
                table.addCell(new Paragraph(UserMas.get(i).getCountry()));
                table.addCell(new Paragraph(UserMas.get(i).getState(), font));
                table.addCell(new Paragraph(UserMas.get(i).getCity(), font));
                table.addCell(new Paragraph(UserMas.get(i).getStreet(), font));
                table.addCell(new Paragraph(String.valueOf(UserMas.get(i).getHouse()), font));
                table.addCell(new Paragraph(String.valueOf(UserMas.get(i).getFlat()), font));

            }

            document.add(table);
            System.out.println(String.format("%s%s%s%s", "Файл создан. Путь:", System.getProperty("user.dir"), File.separator, "my.pdf"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error creating pdf file");
        }finally{
            document.close();
        }
    }
    public ArrayList getNumbers(int countPart, int count) {
        ArrayList<Integer> ArrayRandom = new ArrayList();
        int i = 0;

        while (i < countPart) {
            Random rand = new Random();
            int x = 1 + rand.nextInt(count);
            if (!ArrayRandom.contains(x)) {
                ArrayRandom.add(x);
                ++i;
            }
        }

        return ArrayRandom;
    }


    public ArrayList getLineByLine(ArrayList Numbers, String filename) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(beginReading(filename));
        ArrayList<String> Lines = new ArrayList();

        for (Integer pointLine = 1; bufferedReader.ready(); pointLine = pointLine + 1) {
            String line = bufferedReader.readLine();
            boolean f = false;

            for (int i = 0; i < Numbers.size(); ++i) {
                if (pointLine == Numbers.get(i)) {
                    f = true;
                }
            }

            if (f) {
                Lines.add(line);
            }
        }

        Collections.shuffle(Lines);
        return Lines;
    }

    private BufferedReader beginReading(String filename) throws Exception {
        File file = new File(filename);
        String encoding = System.getProperty("console.encoding", "Cp1251");
        Reader fileReader = new InputStreamReader(new FileInputStream(file), encoding);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }


    public HttpResponse getResponse() throws Exception {
        try {
            String url = "https://randomuser.me/api/";
            URIBuilder builder = new URIBuilder(url);
            HttpClient client = HttpClients.createDefault();
            builder.setParameter("first", "0").setParameter("last", "20");
            HttpGet request = new HttpGet(builder.build());
            request.addHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(request);
            if (response!= null){
                return response;}
        } catch (IOException e) {

            System.out.println("no connection");
        }
        return null;
    }
    public ArrayList<User> getUserFromFiles(int constCount,int count,int countMen,int countWomen)throws Exception{
        Helper helper = new Helper();
        ArrayList<User> UserMas = new ArrayList<>();
        String dir = String.format("%s%s%s%s%s%s%s%s", System.getProperty("user.dir"), File.separator, "src", File.separator, "main", File.separator, "resources", File.separator);
        ArrayList<Integer> NumbersFIOMen = new ArrayList(helper.getNumbers(countMen, constCount));
        ArrayList<Integer> NumbersFIOWomen = new ArrayList(helper.getNumbers(countWomen, constCount));
        ArrayList<Integer> NumbersAddress = new ArrayList(helper.getNumbers(count, constCount));
        ArrayList<String> SurnameMen = new ArrayList<String>(helper.getLineByLine(NumbersFIOMen, String.format("%s%s", dir, "SurnameMen.txt")));
        ArrayList<String> NameMen = new ArrayList<String>(helper.getLineByLine(NumbersFIOMen, String.format("%s%s", dir, "NameMen.txt")));
        ArrayList<String> PatronymicMen = new ArrayList<String>(helper.getLineByLine(NumbersFIOMen, String.format("%s%s", dir, "PatronymicMen.txt")));
        ArrayList<String> SurnameWomen = new ArrayList<String>(helper.getLineByLine(NumbersFIOWomen, String.format("%s%s", dir, "SurnameWomen.txt")));
        ArrayList<String> NameWomen = new ArrayList<String>(helper.getLineByLine(NumbersFIOWomen, String.format("%s%s", dir, "NameWomen.txt")));
        ArrayList<String> PatronymicWomen = new ArrayList<String>(helper.getLineByLine(NumbersFIOWomen, String.format("%s%s", dir, "PatronymicWomen.txt")));
        for (int i = 0; i < count; i++) {
            UserMas.add(new fintech4.User());
            UserMas.get(i).setCountry(fintech4.User.getAddress(String.format("%s%S", dir, "Country.txt"), constCount));
            UserMas.get(i).setState("-");
            UserMas.get(i).setCity(fintech4.User.getAddress(String.format("%s%s", dir, "City.txt"), constCount));
            UserMas.get(i).setStreet(fintech4.User.getAddress(String.format("%s%s", dir, "Street.txt"), constCount));
            UserMas.get(i).setPostcode();
            UserMas.get(i).setHouse();
            UserMas.get(i).setFlat();
            UserMas.get(i).setINN();
            UserMas.get(i).setDayOfBirth();
            UserMas.get(i).setAge(UserMas.get(i).getDob());
        }
        for (int i = 0; i < countMen; i++) {
            UserMas.get(i).setLastName(SurnameMen.get(i));
            UserMas.get(i).setFirstName(NameMen.get(i));
            try {
                UserMas.get(i).setTitle(PatronymicMen.get(i));
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                System.out.println("troubles with patrinymics");
            }
            UserMas.get(i).setGender("M");
        }
        for (int i = 0; i < countWomen; i++) {
            UserMas.get(i + countMen).setLastName(SurnameWomen.get(i));
            UserMas.get(i + countMen).setFirstName(NameWomen.get(i));
            try {
                UserMas.get(i + countMen).setTitle(PatronymicWomen.get(i));
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                System.out.println("troubles with patrinymics");
            }
            UserMas.get(i + countMen).setGender("Ж");
        }
        return UserMas;
    }


}
