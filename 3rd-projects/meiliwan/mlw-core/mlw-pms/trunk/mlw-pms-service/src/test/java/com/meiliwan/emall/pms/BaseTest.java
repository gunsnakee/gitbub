/**
 *
 */
package com.meiliwan.emall.pms;


import java.io.File;
import java.io.FileWriter;

import javax.sql.DataSource;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 单元测试基类，spring配置文件为oms-integration-test.xml，该文件包含现有所有的bean配置文件， 可根据自己的需要去修改bean的配置文件。
 * 
 */
@ContextConfiguration(locations = { "classpath:conf/spring/pms-integration-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public abstract class BaseTest extends AbstractTestNGSpringContextTests {
	
		private BaseDbunitTest baseDbunitTest = new BaseDbunitTest();
		
		public void setBaseDbunitTest(BaseDbunitTest baseDbunitTest) {
			this.baseDbunitTest = baseDbunitTest;
		}

		
		protected IDatabaseConnection getConnection() throws Exception {
			return baseDbunitTest.getConnection();
		}
		
		protected IDataSet getDataSet(String file) throws Exception {
			return baseDbunitTest.getDataSet(file);
		}
		
		
		/**
		 * 备份数据库数据，获取初始化数据集并通过文件初始化db测试库
		 * @param initDabfile
		 */
		protected void init(String initDbFile, String backupDbFile, String[] backupTables) {
			if (backupTables == null || backupTables.length == 0) {
				throw new IllegalArgumentException("backupTables should not be null!"); 
			}
			baseDbunitTest.setInitDbFile(initDbFile);
			baseDbunitTest.setBackupDbFile(backupDbFile);
			// 对数据库中的操作对象表 pro_product 进行备份
			
			try {
				QueryDataSet backupDataSet = new QueryDataSet(this.getConnection());
				for (String t : backupTables) {
					backupDataSet.addTable(t);
				}
				// do backup
				File file = new File(backupDbFile);
				FileWriter fw = new FileWriter(file);
				FlatXmlDataSet.write(backupDataSet, fw, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// db数据库初始化
			try {
				DatabaseOperation.CLEAN_INSERT.execute(this.getConnection(), baseDbunitTest.getDataSet());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
    
		/**
		 * 销毁数据库连接，恢复数据\库数据到备份数据
		 */
		protected void tearDown() {
			// db数据库数据恢复
			try {
				DatabaseOperation.CLEAN_INSERT.execute(this.getConnection(),
						baseDbunitTest.getBackupDataSet());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		public static void main(String[] args) {
			char currentChar = '红';
			 if ((currentChar > 0x7f) )
             {
				 System.out.println("&#" + String.valueOf((int) currentChar) + ";");
             }
		}
}
