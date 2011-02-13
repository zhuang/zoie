package proj.zoie.test.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class TestCarData
{
	private final Vector<CarData> _data = new Vector<CarData>();

	/**
    * @param file
    */
   public TestCarData(File file) throws IOException
   {
      FileInputStream fin = null;
      try
      {           
         fin = new FileInputStream(file);
         BufferedReader br = new BufferedReader(new InputStreamReader(fin));
         String line;
         String carLine = "";
         int i = 0;
         while ((line = br.readLine()) != null)
         {
            if ("<EOD>".equals(line))
            {
               getCar(_data, carLine, i ++);
               carLine = "";
            }
            else
            {
               String[] pair = line.split(":");
               if (!"".equals(carLine))
               {
                  carLine += ";"; 
               }
               carLine += pair[1];
            }
         }
         br.close();
      }
      catch (IOException e)
      {
         System.err.println("Error: " + e);
      }
      finally
      {
         if (fin != null)
         {
            fin.close();
         }
      }
   }
   
   private void getCar(Vector<CarData> data, String carLine, int i)
   {
      /*
      color:yellow
      year:00000000000000001994
      price:00000000000000007500
      tags:hybrid,leather,moon-roof,reliable
      mileage:00000000000000014900
      category:compact
      makemodel:asian/acura/1.6el
      city:u.s.a./florida/tampa
      */

      String id = String.format("%020d", i);
      
      String[] parts = carLine.split(";");      
      data.add(new CarData(parts[0],
                           parts[1],
                           parts[2],
                           parts[3],
                           parts[4],
                           parts[5],
                           parts[6],
                           parts[7],
                           id));
   }

   /**
    * @return the data
    */
   public final Vector<CarData> getData() 
   {
      return _data;
   }
}
