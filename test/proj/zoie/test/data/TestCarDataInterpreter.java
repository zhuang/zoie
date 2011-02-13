package proj.zoie.test.data;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

import proj.zoie.api.indexing.ZoieIndexable;
import proj.zoie.api.indexing.ZoieIndexableInterpreter;

import proj.zoie.test.data.CarData;

public class TestCarDataInterpreter implements ZoieIndexableInterpreter<CarData>
{

   long _delay;

   public TestCarDataInterpreter()
   {
      this(0);
   }

   public TestCarDataInterpreter(long delay)
   {
      _delay = delay;
   }

   public ZoieIndexable interpret(final CarData src)
   {
      return new ZoieIndexable()
      {
         public Document buildDocument()
         {
            Document doc = new Document();
            doc.add(new Field("color", src._color,         Store.NO, Index.NOT_ANALYZED));
            doc.add(new Field("year", src._year,           Store.NO, Index.NOT_ANALYZED));
            doc.add(new Field("price", src._price,         Store.NO, Index.NOT_ANALYZED));
            doc.add(new Field("tags", src._tags,           Store.NO, Index.NOT_ANALYZED));
            doc.add(new Field("mileage", src._mileage,     Store.NO, Index.NOT_ANALYZED));
            doc.add(new Field("category", src._category,   Store.NO, Index.NOT_ANALYZED));
            doc.add(new Field("city", src._city,           Store.NO, Index.NOT_ANALYZED));
            doc.add(new Field("makemodel", src._makemodel, Store.NO, Index.NOT_ANALYZED));
            try
            {
               Thread.sleep(_delay);
            }
            catch (InterruptedException e)
            {
            }
            return doc;
         }

         public IndexingReq[] buildIndexingReqs()
         {
            return new IndexingReq[]{ new IndexingReq(buildDocument(), getAnalyzer()) };
         }

         public long getUID()
         {
            return Long.parseLong(src._id);
         }

         public Analyzer getAnalyzer()
         {
            return null;
         }

         public boolean isDeleted()
         {
            return false;
         }

         public boolean isSkip()
         {
            return false;
         }
      };
   }

   public ZoieIndexable convertAndInterpret(CarData src)
   {
      return interpret(src);
   }

}
