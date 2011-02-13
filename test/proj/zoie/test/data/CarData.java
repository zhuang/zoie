package proj.zoie.test.data;

import java.io.Serializable;

public class CarData implements Serializable
{
   public String _color;
   public String _year;
   public String _price;
   public String _tags;
   public String _mileage;
   public String _category;
   public String _city;
   public String _makemodel;
   public String _id;
   
   /**
    * @param color
    * @param year
    * @param price
    * @param tags
    * @param mileage
    * @param category
    * @param city
    * @param makemodel
    */
   public CarData(String color, String year, String price, String tags,
         String mileage, String category, String city, String makemodel, String id) 
   {
      _color = color;
      _year = year;
      _price = price;
      _tags = tags;
      _mileage = mileage;
      _category = category;
      _city = city;
      _makemodel = makemodel;
      _id = id;
   }
}
