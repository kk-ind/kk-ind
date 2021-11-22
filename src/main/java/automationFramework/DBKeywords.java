package automationFramework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import baseClass.BaseClass;
public class DBKeywords extends GenericKeywords{

	public Connection connection = null;
	public  int numberOfAttributes;
	public  int numberOfAttrb;
	public Statement statement;
	public static ResultSet resultSet;

	public DBKeywords(BaseClass obj)
	{
		super(obj);
	}
	public DBKeywords()
	{

	}
	// @ To Connect Database
	public void connectToDatabase(String dbModule, String dbHost, String dbPort, String dbName,
			String dbUsername, String dbPassword) {
		try {
			String className;
			String connectionString = null;

			switch (dbModule.toLowerCase()) {
			case "mysql":
				className = "com.mysql.jdbc.Driver";
				connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/";
				break;

			case "postgre":
				className = "org.postgresql.Driver";
				connectionString = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/";
				break;
			case "oracle":
				className = "oracle.jdbc.driver.OracleDriver";
				connectionString = "jdbc:oracle:thin:@" + dbHost + ":" + dbPort + ":";
				break;
			default:
				className = "Invalid Database";
				break;
			}

			Class.forName(className);
			connection = DriverManager.getConnection(connectionString + dbName, dbUsername, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
			testStepFailed("",e.getClass().getName() + ": " + e.getMessage());
			testStepFailed("Database Connection Failed","");
		}
	}

	// @ To Terminate Database
	public void disconnectFromDatabase() {
		try {
			connection.close();
			testStepPassed("Closed database successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// @ To execute single hit database query
	public void executeSqlScript(String sqlScriptFileName) {

		Statement stmt = null;
		try {
			sqlScriptFileName = sqlScriptFileName.concat(";");
			stmt = connection.createStatement();
			// stmt.executeUpdate(sqlScriptFileName);
			stmt.executeUpdate(sqlScriptFileName);
			testStepPassed("'" + sqlScriptFileName + "' query is executed successfully");
			stmt.close();
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
			testStepFailed(sqlScriptFileName + " query execution failed","");
		}
	}

	public void deleteAllRowsFromTable(String tableName) {
		String sqlQuery = "TRUNCATE " + tableName.toUpperCase() + ";";
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			// stmt.executeUpdate(sqlScriptFileName);
			stmt.executeUpdate(sqlQuery);
			testStepPassed("'" + sqlQuery + "' query is executed successfully");
			stmt.close();
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
			testStepFailed(sqlQuery + " query execution failed","");
		}
	}

	// @ method for selecting the table
	public void query(String selectStmt) {
		String str = null;
		String selectStatement = selectStmt + ";";
		numberOfAttributes = 0;
		if (!selectStatement.substring(7, 8).equalsIgnoreCase("*")) {
			String myArr[] = selectStatement.toLowerCase().split(" from ");
			String myArrCols[] = myArr[0].split(",");

			if (myArrCols.length == 0) {
				numberOfAttributes = 1;
			} else {
				numberOfAttributes = myArrCols.length;
			}
		}
		Statement statement = null;
		try {

			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(selectStatement);
			if (rs != null) {
				while (rs.next()) {

					if (numberOfAttributes != 0) {
						for (int i = 1; i <= numberOfAttributes; i++) {
							str = rs.getString(i);
							writeToLogFile("INFO", str);
							// System.out.println(rs.getString(i));

						}
					} else {
						int i = 1;
						while (true) {
							try {
								// System.out.println(rs.getString(i));
								str = rs.getString(i);
								writeToLogFile("INFO", str);

							} catch (Exception e) {
								break;
							} finally {
								i++;
							}
						}
					}

					System.out.println();
				}
			}
			rs.close();
			statement.close();
			testStepPassed("Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage() + "Select Statement Failed");
			System.exit(0);

		}
	}

	public  boolean checkIfExistsInDatabase(String selectStatment) {
		selectStatment = selectStatment + ";";
		numberOfAttributes = 0;
		boolean flag = false;
		if (!selectStatment.substring(7, 8).equalsIgnoreCase("*")) {
			String myArr[] = selectStatment.toLowerCase().split(" from ");
			String myArrCols[] = myArr[0].split(",");
			if (myArrCols.length == 0) {
				numberOfAttributes = 1;
			} else {
				numberOfAttributes = myArrCols.length;
			}
		}
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectStatment);
			if (rs != null) {
				writeToLogFile("INFO", "Result Set: " + rs.next());
				flag = true;
			}
			rs.close();
			stmt.close();
			testStepPassed("checkIfExistInDatabase :: Select Statement Passed");

		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage()
			+ "checkIfExistInDatabase :: Select Statement Failed");
			System.exit(0);

		}
		return flag;
	}

	public  Boolean checkIfNotExistsInDatabase(String selectStatment) {
		selectStatment = selectStatment + ";";
		numberOfAttributes = 0;
		Boolean flag = false;
		if (!selectStatment.substring(7, 8).equalsIgnoreCase("*")) {
			String myArr[] = selectStatment.toLowerCase().split(" from ");
			String myArrCols[] = myArr[0].split(",");

			if (myArrCols.length == 0) {
				numberOfAttributes = 1;
			} else {
				numberOfAttributes = myArrCols.length;
			}
		}
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectStatment);
			writeToLogFile("INFO", "Result Set:: " + rs.next());
			if (rs.equals(null)) {

				flag = true;
			}
			rs.close();
			stmt.close();
			testStepPassed("checkIfNotExistInDatabase :: Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage()
			+ "checkIfNotExistInDatabase :: Select Statement Failed");
			System.exit(0);

		}
		return flag;
	}

	public  Boolean tableExist(String tableName) {
		Boolean flag = false;
		String sqlScriptFileName;
		Statement stmt = null;
		try {
			sqlScriptFileName = "SHOW TABLES LIKE '" + tableName + "';";
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlScriptFileName);
			testStepPassed("TableExist :: Select Statement Executed");
			if (rs != null) {
				flag = true;
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage() + "TableExist :: Select Statement Failed");
			System.exit(0);
		}
		return flag;
	}

	public  void rowCount(String tableName) {
		String selectStatement = "Select COUNT(*) FROM " + tableName + ";";
		numberOfAttributes = 0;
		if (!selectStatement.substring(7, 8).equalsIgnoreCase("*")) {
			String myArr[] = selectStatement.toLowerCase().split(" from ");
			String myArrCols[] = myArr[0].split(",");

			if (myArrCols.length == 0) {
				numberOfAttributes = 1;
			} else {
				numberOfAttributes = myArrCols.length;
			}
		}
		Statement statement = null;
		try {

			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(selectStatement);
			if (rs != null) {
				while (rs.next()) {

					if (numberOfAttributes != 0) {
						for (int i = 1; i <= numberOfAttributes; i++) {
							writeToLogFile("INFO", "Row Count :: " + rs.getString(i));
							// System.out.println(rs.getString(i));

						}
					} else {
						int i = 1;
						while (true) {
							try {
								// System.out.println(rs.getString(i));
								writeToLogFile("INFO", "Row Count :: " + rs.getString(i));

							} catch (Exception e) {
								break;
							} finally {
								i++;
							}
						}
					}

					System.out.println();
				}
			}
			rs.close();
			statement.close();
			testStepPassed("rowCount :: Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage() + "rowCount :: Select Statement Failed");
			System.exit(0);

		}
	}

	public  void rowCount(String tableName, String condition) {
		String sqlQuery = "Select COUNT(*) FROM " + tableName + " where " + condition + ";";
		numberOfAttributes = 0;
		if (!sqlQuery.substring(7, 8).equalsIgnoreCase("*")) {
			String myArr[] = sqlQuery.toLowerCase().split(" from ");
			String myArrCols[] = myArr[0].split(",");

			if (myArrCols.length == 0) {
				numberOfAttributes = 1;
			} else {
				numberOfAttributes = myArrCols.length;
			}
		}
		Statement statement = null;
		try {

			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sqlQuery);
			if (rs != null) {
				while (rs.next()) {

					if (numberOfAttributes != 0) {
						for (int i = 1; i <= numberOfAttributes; i++) {
							writeToLogFile("INFO", "Row Count :: " + rs.getString(i));
							// System.out.println(rs.getString(i));

						}
					} else {
						int i = 1;
						while (true) {
							try {
								// System.out.println(rs.getString(i));
								writeToLogFile("INFO", "Row Count :: " + rs.getString(i));

							} catch (Exception e) {
								break;
							} finally {
								i++;
							}
						}
					}

					System.out.println();
				}
			}
			rs.close();
			statement.close();
			testStepPassed("rowCount :: Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage() + "rowCount :: Select Statement Failed");
			System.exit(0);

		}

	}

	public  boolean rowCountIsZero(String tableName) {
		String sqlQuery = "Select COUNT(*) FROM " + tableName + ";";
		boolean flag = false;
		Statement stmt = null;
		int rowCount = 0;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			rs.next();
			{
				rowCount = rs.getInt(1);
				if (rowCount == 0) {
					flag = true;

				} else {
					flag = false;
				}
			}
			rs.close();
			stmt.close();
			testStepPassed("rowCountIsZero :: Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage() + "rowCountIsZero :: Select Statement Failed");
			System.exit(0);

		}
		return flag;

	}

	public  boolean rowCountIsZero(String tableName, String condition) {

		String selectStatement = "SELECT COUNT(*) FROM " + tableName + " where " + condition + ";";
		boolean flag = false;
		Statement stmt = null;
		int rowCount = 0;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectStatement);
			rs.next();
			{
				rowCount = rs.getInt(1);
				if (rowCount == 0) {
					flag = true;

				} else {
					flag = false;
				}
			}
			rs.close();
			stmt.close();
			testStepPassed("rowCountIsZero :: Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage() + "rowCountIsZero :: Select Statement Failed");
			System.exit(0);

		}
		return flag;
	}

	public  boolean rowCountIsEqualToX(String tableName, String rowCounts) {
		String sqlQuery = "Select COUNT(*) FROM " + tableName + ";";
		int x = Integer.parseInt(rowCounts);
		boolean flag = false;
		Statement stmt = null;
		int rowCount = 0;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			rs.next();
			{
				rowCount = rs.getInt(1);
				if (rowCount == x) {
					flag = true;
				} else {
					flag = false;
				}
			}
			rs.close();
			stmt.close();
			testStepPassed("rowCountIsEqualToX :: Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage()
			+ "rowCountIsEqualToX :: Select Statement Failed");
			System.exit(0);

		}
		return flag;

	}

	public  boolean rowCountIsEqualToX(String tableName, String condition, String rowCounts) {

		String sqlQuery = "SELECT COUNT(*) FROM " + tableName + " where " + condition + ";";
		int x = Integer.parseInt(rowCounts);
		System.out.println(sqlQuery);
		boolean flag = false;
		Statement stmt = null;
		int rowCount = 0;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			rs.next();
			{
				rowCount = rs.getInt(1);
				if (rowCount == x) {
					flag = true;
				} else {
					flag = false;
				}
			}
			rs.close();
			stmt.close();
			testStepPassed("rowCountIsEqualToX :: Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage()
			+ "rowCountIsEqualToX :: Select Statement Failed");
			System.exit(0);

		}
		return flag;
	}

	public  boolean rowCountIsGreaterToX(String tableName, String rowCounts) {
		String sqlQuery = "Select COUNT(*) FROM " + tableName + ";";
		int x = Integer.parseInt(rowCounts);
		boolean flag = false;
		Statement stmt = null;
		int rowCount = 0;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			rs.next();
			{
				rowCount = rs.getInt(1);
				if (rowCount > x) {
					flag = true;
				} else {
					flag = false;
				}
			}
			rs.close();
			stmt.close();
			testStepPassed("rowCountIsGreaterToX :: Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage()
			+ "rowCountIsGreaterToX :: Select Statement Failed");
			System.exit(0);
		}
		return flag;
	}

	public  boolean rowCountIsGreaterToX(String tableName, String condition, String rowCounts) {

		String sqlQuery = "SELECT COUNT(*) FROM " + tableName + " where " + condition + ";";
		int x = Integer.parseInt(rowCounts);
		boolean flag = false;
		Statement stmt = null;
		int rowCount = 0;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			rs.next();
			{
				rowCount = rs.getInt(1);
				if (rowCount > x) {
					flag = true;
				} else {
					flag = false;
				}
			}
			rs.close();
			stmt.close();
			testStepPassed("rowCountIsGreaterToX :: Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage()
			+ "rowCountIsGreaterToX :: Select Statement Failed");
			System.exit(0);
		}
		return flag;
	}

	public  boolean rowCountIsLessToX(String tableName, String rowCounts) {
		String sqlQuery = "Select COUNT(*) FROM " + tableName + ";";
		System.out.println(sqlQuery);
		int x = Integer.parseInt(rowCounts);
		boolean flag = false;
		Statement stmt = null;
		int rowCount = 0;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			rs.next();
			{
				rowCount = rs.getInt(1);
				if (rowCount < x) {
					flag = true;
				} else {
					flag = false;
				}
			}
			rs.close();
			stmt.close();
			testStepPassed("rowCountIsLessToX :: Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage()
			+ "rowCountIsLessToX :: Select Statement Failed");
			System.exit(0);
		}
		return flag;

	}

	public  boolean rowCountIsLessToX(String tableName, String condition, String rowCounts) {
		Statement stmt = null;
		int x = Integer.parseInt(rowCounts);
		int rowCount = 0;
		boolean flag = false;
		String query = "SELECT COUNT(*) FROM " + tableName + " where " + condition + ";";

		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			{

				rowCount = rs.getInt(1);
				if (rowCount < x) {
					flag = true;
				} else {
					flag = false;
				}
			}
			rs.close();
			stmt.close();
			testStepPassed("rowCountIsLessToX :: Select Statement Passed");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage()
			+ "rowCountIsLessToX :: Select Statement Failed");
			System.exit(0);

		}
		return flag;
	}

	public void description(String selectQuery) {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selectQuery);
			if (rs != null) {
				while (rs.next()) {
					if (numberOfAttrb != 0) {
						for (int i = 1; i <= numberOfAttrb; i++) {
							System.out.println(rs.getString(i));
						}
					} else {
						int i = 1;
						while (true) {
							try {
								System.out.println(rs.getString(i));
							} catch (Exception e) {
								break;
							} finally {
								i++;
							}
						}
					}
					System.out.println();
				}
			}
			rs.close();
			stmt.close();
			testStepPassed("description :: Operation executed successfully");
		} catch (Exception e) {
			testStepFailed("",e.getClass().getName() + ":" + e.getMessage() + "description :: Operation failed");
			System.exit(0);
		}

	}

	//TODO Keywords for IRIDIUM DB

	public void executeQuery(String query){
		try{
			testStepInfo("Executing Query: "+query);
			statement = connection.createStatement(); 
			resultSet = statement.executeQuery(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getValueFromDB(String columnValue){
		try{
			String value = "";
			while(resultSet.next())  
			{
				return resultSet.getString(columnValue);
			}
			return value;
		}
		catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	public String printResultSet(){
		try{
			String temp = "";
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			writeToLogFile("INFO" , "Column count: "+columnsNumber);
			while (resultSet.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					String columnValue = resultSet.getString(i);
					temp = temp.concat("{'"+rsmd.getColumnName(i)+"' - '"+columnValue+"'}");
				}
				temp = temp.concat("   ");
			}
			return temp;
		}catch(Exception e){
			e.printStackTrace();
			stepFailed("Failed to get the Resultset from DB");
			return null;
		}
	}

}
