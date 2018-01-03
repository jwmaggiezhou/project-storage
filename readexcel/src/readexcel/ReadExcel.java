package readexcel;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ReadExcel {
  public static void getChangeList(String path) throws IOException, EncryptedDocumentException, InvalidFormatException{
    String desktop = System.getProperty("user.home") + "/Desktop/";
    InputStream inp;
    BufferedWriter bw;
      File f = new File("list.html");
      bw = new BufferedWriter(new FileWriter(f, false));
      bw.write("<!DOCTYPE html>" + "\n" +"<html>" +"\n"+"<body>"+"<p>");
      inp = new FileInputStream(desktop+"ECAB Change Report "+ path+" (FINAL).xlsx");
      //inp = new FileInputStream(path);
      Workbook wb;
      wb = WorkbookFactory.create(inp);
      Sheet sheet = wb.getSheet("Week1");
        
        //System.out.println(sheet.getFirstRowNum());
      int max = sheet.getLastRowNum();
      int pointer = 8;
      //System.out.println(sheet.getRow(124) == null);
      //System.out.println(sheet.getRow(10).getCell(1).getCellStyle().getFontIndex());
      try{
      for(int i=8; i< max; i++){
        pointer = i;
        if(sheet.getRow(i) != null){
          Cell c = sheet.getRow(i).getCell(1);
          if(c.getStringCellValue() != "" && (c.getCellStyle().getFillForegroundColor() == 64 ||c.getCellStyle().getFillForegroundColor() == 0)){
            if(c.getCellStyle().getFontIndex() == 132){
              bw.write("<br>"+ "<br>"+c.getStringCellValue()+"<br>");
            }else{
            bw.write("<br>"+ c.getStringCellValue().replace("\n", ""));
            }
          }
        }
      }
      bw.write("</p>"+"</body>"+"\n"+"</html>");
      bw.flush();
      bw.close();
      Desktop.getDesktop().browse(f.toURI());
      } catch (Exception ex){
        System.out.println("please check row "+ (pointer+1));
      }
   }


  public static void main(String[] args) {
    System.out.println("please enter the date of ECAB report:");
    Scanner sc = new Scanner(System.in);
    String path = sc.nextLine();
    while(true){
      try{
      getChangeList(path);
      sc.close();
      break;
    } catch (EncryptedDocumentException e) {
      System.out.println("EncryptedDocumentException\n");
      System.out.println("please re-enter the date with correct format:");
        path = sc.nextLine();
    } catch (InvalidFormatException e1) {
      System.out.println("InvalidFormatException\n");
      System.out.println("please re-enter the date with correct format:");
      path = sc.nextLine();
    } catch (IOException e2){
      System.out.println("IOException\n");
      System.out.println("please re-enter the date with correct format:");
      path = sc.nextLine();
    }
   }
  }

  }

