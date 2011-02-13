package proj.zoie.test;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;

import proj.zoie.api.DocIDMapperFactory;
import proj.zoie.api.ZoieIndexReader;
import proj.zoie.api.DefaultZoieVersion;
import proj.zoie.api.impl.InRangeDocIDMapperFactory;
import proj.zoie.api.indexing.IndexReaderDecorator;
import proj.zoie.impl.indexing.ZoieSystem;
import proj.zoie.test.data.CarData;
import proj.zoie.test.data.TestCarDataInterpreter;
import proj.zoie.test.data.TestDataInterpreter;
import proj.zoie.test.data.TestInRangeDataInterpreter;
import junit.framework.TestCase;

import proj.zoie.api.ZoieVersionFactory;


public class ZoieTestCase extends TestCase
{
  static Logger log = Logger.getLogger(ZoieTestCase.class);
  static MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
  public void registerMBean(Object standardmbean, String mbeanname)
  {
    try
    {
      mbeanServer.registerMBean(standardmbean, new ObjectName("Zoie:name=" + mbeanname));
    } catch (Exception e)
    {
      log.warn(e);
    }
  }

  public void unregisterMBean(String mbeanname)
  {
    try
    {
      mbeanServer.unregisterMBean(new ObjectName("Zoie:name=" + mbeanname));
    } catch (Exception e)
    {
      log.warn(e);
    }
  }

  ZoieTestCase()
  {
    super();
    String confdir = System.getProperty("conf.dir");
    if (confdir==null) confdir="conf";
    try
    {
      org.apache.log4j.PropertyConfigurator.configure(confdir+"/log4j.properties");
    } catch(Exception e)
    {
      org.apache.log4j.PropertyConfigurator.configure((Properties)null);
    }
  }

  ZoieTestCase(String name)
  {
    super(name);
    String confdir = System.getProperty("conf.dir");
    if (confdir==null) confdir="conf";
    try
    {
      org.apache.log4j.PropertyConfigurator.configure(confdir+"/log4j.properties");
    } catch(Exception e)
    {
      org.apache.log4j.PropertyConfigurator.configure((Properties)null);
    }
  }

  @Override
  public void setUp()
  {
    System.out.println("executing test case: " + getName());
    log.info("\n\n\nexecuting test case: " + getName());
  }
  @Override
  public void tearDown()
  {
    deleteDirectory(getIdxDir());
  }
  protected static File getIdxDir()
  {
    File tmpDir=new File(System.getProperty("java.io.tmpdir"));
    File tempFile = new File(tmpDir, "test-idx");
    int i = 0;
    while (tempFile.exists())
    {
      if (i>10)
      {
        log.info("cannot delete");
        return tempFile;
      }
      log.info("deleting " + tempFile);
      deleteDirectory(tempFile);
//      tempFile.delete();
      try
      {
        Thread.sleep(50);
      } catch(Exception e)
      {
        log.error("thread interrupted in sleep in deleting file" + e);
      }
      i++;
    }
    return tempFile;
  }
  
  protected static File getDataFile()
  {
    File tempFile = new File(System.getProperty("test.data.file"));
    if (tempFile.exists())
    {
      return tempFile;
    }
    else
    {
      return null;
    }
  }

  protected static File getTmpDir()
  {
    return new File(System.getProperty("java.io.tmpdir"));
  }

  protected static ZoieSystem<IndexReader,String, DefaultZoieVersion> createZoie(File idxDir,boolean realtime, ZoieVersionFactory<DefaultZoieVersion> zoieVersionFactory)
  {
    return createZoie(idxDir, realtime, 20,zoieVersionFactory);
  }
  
  /**
   * @param idxDir
   * @param realtime
   * @param delay delay for interpreter (simulating a slow interpreter)
   * @param zoieVersionFactory
   * @return
   */
  protected static ZoieSystem<IndexReader,String, DefaultZoieVersion> createZoie(File idxDir,boolean realtime, long delay, ZoieVersionFactory<DefaultZoieVersion> zoieVersionFactory)
  {
    return createZoie(idxDir,realtime,delay,null,null,zoieVersionFactory);
  }

  protected static ZoieSystem<IndexReader,String, DefaultZoieVersion> createZoie(File idxDir,boolean realtime,DocIDMapperFactory docidMapperFactory,ZoieVersionFactory<DefaultZoieVersion> zoieVersionFactory)
  {
    return createZoie(idxDir, realtime, 2,null,docidMapperFactory, zoieVersionFactory);
  }

  /**
   * @param idxDir
   * @param realtime
   * @param delay delay for interpreter (simulating a slow interpreter)
   * @param analyzer
   * @param docidMapperFactory
   * @param zoieVersionFactory
   * @return
   */
  protected static ZoieSystem<IndexReader,String, DefaultZoieVersion> createZoie(File idxDir,boolean realtime, long delay,Analyzer analyzer,DocIDMapperFactory docidMapperFactory, ZoieVersionFactory<DefaultZoieVersion> zoieVersionFactory)
  {
    ZoieSystem<IndexReader,String,DefaultZoieVersion> idxSystem=new ZoieSystem<IndexReader, String, DefaultZoieVersion>(idxDir,new TestDataInterpreter(delay,analyzer),
        new TestIndexReaderDecorator(),docidMapperFactory, null,null,50,2000,realtime,zoieVersionFactory);
    return idxSystem;
  }


  protected static class TestIndexReaderDecorator implements IndexReaderDecorator<IndexReader>{
    public IndexReader decorate(ZoieIndexReader<IndexReader> indexReader) throws IOException {
      return indexReader;
    }

    public IndexReader redecorate(IndexReader decorated,ZoieIndexReader<IndexReader> copy,boolean withDeletes) throws IOException {
      return decorated;
    }


    public void setDeleteSet(IndexReader reader, DocIdSet docIds)
    {
      // do nothing
    }
  }

  protected static ZoieSystem<IndexReader,String,DefaultZoieVersion> createInRangeZoie(File idxDir,boolean realtime, InRangeDocIDMapperFactory docidMapperFactory, ZoieVersionFactory<DefaultZoieVersion> zoieVersionFactory)
  {
    ZoieSystem<IndexReader,String,DefaultZoieVersion> idxSystem=new ZoieSystem<IndexReader, String,DefaultZoieVersion>(idxDir,new TestInRangeDataInterpreter(20,null),
        new TestIndexReaderDecorator(),docidMapperFactory,null,null,50,2000,realtime,zoieVersionFactory);
    return idxSystem;
  }
  
  protected static ZoieSystem<IndexReader,CarData,DefaultZoieVersion> createCarZoie(File idxDir, ZoieVersionFactory<DefaultZoieVersion> zoieVersionFactory)
  {
	  ZoieSystem<IndexReader,CarData,DefaultZoieVersion> idxSystem =
		  new ZoieSystem<IndexReader, CarData, DefaultZoieVersion>
    		(idxDir,
    		new TestCarDataInterpreter(20),
    		new TestIndexReaderDecorator(),
    		null,
    		null,
    		null,
    		50,
    		2000,
    		true,
    		zoieVersionFactory);

	  return idxSystem;
  } 
  
  protected static boolean deleteDirectory(File path) {
    if( path.exists() ) {
      File[] files = path.listFiles();
      for(int i=0; i<files.length; i++) {
        if(files[i].isDirectory()) {
          deleteDirectory(files[i]);
        }
        else {
          files[i].delete();
        }
      }
    }
    return( path.delete() );
  }

  
  protected static class QueryThread extends Thread
  {
    public volatile boolean stop = false;
    public volatile boolean mismatch = false;
    public volatile String message = null;
    public Exception exception = null;
  }

}
