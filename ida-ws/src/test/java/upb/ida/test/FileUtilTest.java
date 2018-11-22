package upb.ida.test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import upb.ida.constant.IDALiteral;

import upb.ida.Application;
import upb.ida.util.DataDumpUtil;
import upb.ida.util.FileUtil;
import upb.ida.fdg.FDG_Util;
@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {Application.class})

public class FileUtilTest {
	
	

	@Autowired
	private FileUtil dem;
	
	
	@Test
	public void fetchSysFilePathTest()
	{
		String filePath= "dataset/city/movehubcostoflivingtest.csv";
		File actual = new File(dem.fetchSysFilePath(filePath));
		File expected= new File("E:\\IDA_Workspace\\ida\\ida-ws\\target\\test-classes\\dataset\\city\\movehubcostoflivingtest.csv");
		assertEquals(actual,expected);
		
		
	}
	
}
	
	

