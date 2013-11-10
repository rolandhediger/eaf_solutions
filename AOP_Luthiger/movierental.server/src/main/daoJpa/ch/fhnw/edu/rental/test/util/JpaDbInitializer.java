package ch.fhnw.edu.rental.test.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class JpaDbInitializer implements DbInitializer {

//	@Override
//	public ApplicationContext resetData(ApplicationContext context) throws Exception {
//		// populate test datatbase
//		// EntityManagerFactory emf = Persistence.createEntityManagerFactory("movierental");
//		
//		//context = new ClassPathXmlApplicationContext("datasource.xml");
//		
//		EntityManagerFactory emf = (EntityManagerFactory)context.getBean("entityManagerFactory");
//		EntityManager em = emf.createEntityManager();
//
//		HibernateEntityManager hem = (HibernateEntityManager) em;
//		// initialize the database connection
//		Session session = hem.getSession();
//		IDatabaseConnection connection = new DatabaseConnection(session.connection());
//		DatabaseConfig config = connection.getConfig();
//		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
//				new HsqldbDataTypeFactory());
//
//		// initialize the dataset
//		InputStream stream = TestUtil.class.getResourceAsStream("dataset.xml");
//		ReplacementDataSet dataSet = new ReplacementDataSet(new FlatXmlDataSet(stream)); 
//		dataSet.addReplacementObject("[NULL]", null);
//		try {
//			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
//		} finally {
//			connection.close();
//			em.close();
//			emf.close();
//		}
//		return context;
//	}

	private static String TEST_DATA_FILE = "src/main/resources/dataset.xml";
	
	private String testDataFile = TEST_DATA_FILE;

	public void setTestDataFile(String testDataFile) {
		this.testDataFile = testDataFile;
	}

	public void resetData(ApplicationContext context) throws Exception {
		DataSource ds = (DataSource) context.getBean("dataSource");
		Connection conn = ds.getConnection();
	
		try {
			IDatabaseConnection connection = new DatabaseConnection(conn);
			DatabaseConfig config = connection.getConfig();
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
					new HsqldbDataTypeFactory());
			
			InputStream stream = new FileInputStream(testDataFile);
			FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
			IDataSet dataSet = builder.build(stream);
			
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		}
		finally {
			DataSourceUtils.releaseConnection(conn, ds);
		}
	}

}
