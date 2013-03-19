package org.seasar.s2jdbcmock.service;

import static junit.framework.Assert.*;
import static org.seasar.s2jdbcmock.S2JdbcUnit.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;
import org.seasar.s2jdbcmock.entity.Employee;
import org.seasar.s2jdbcmock.service.EmployeeService.StoredParam;

@RunWith(Seasar2.class)
public class EmployeeServiceTest {

	protected EmployeeService sampleService;

	@Before
	public void init(){
		initS2JdbcUnit();
	}

	@Test
	public void testGetEmployeeTx(){
		sampleService.getEmployee(1);
		verifySqlNumber(1);
		verifySqlByRegExp(0, "SELECT .* FROM EMPLOYEE .* WHERE .*EMP_ID = \\?", 1);
	}

	@Test
	public void testInsertTx(){
		Employee employee = new Employee();
		employee.name = "Naoki Takezoe";
		sampleService.insert(employee);

		verifySqlNumber(1);
		verifySqlByRegExp(0, "INSERT INTO EMPLOYEE .*", null, "Naoki Takezoe");
		assertEquals(new Integer(0), employee.empId);

		sampleService.insert(employee);
		verifySqlNumber(2);
		verifySqlByRegExp(1, "INSERT INTO EMPLOYEE .*", null, "Naoki Takezoe");
		assertEquals(new Integer(1), employee.empId);
	}

	@Test
	public void testInsertBatchTx(){
		List<Employee> employees = new ArrayList<Employee>();
		{
			Employee employee = new Employee();
			employee.name = "Naoki Takezoe";
			employees.add(employee);
		}
		{
			Employee employee = new Employee();
			employee.name = "Taro Yamada";
			employees.add(employee);
		}

		sampleService.insert(employees);

		verifySqlNumber(2);

		verifySqlByRegExp(0, "INSERT INTO EMPLOYEE .*", null, "Naoki Takezoe");
//		assertEquals(new Integer(0), employees.get(0).empId);

		verifySqlByRegExp(1, "INSERT INTO EMPLOYEE .*", null, "Taro Yamada");
//		assertEquals(new Integer(1), employees.get(1).empId);
	}

	@Test
	public void testUpdateTx(){
		Employee employee = new Employee();
		employee.empId = 1;
		employee.name = "Naoki Takezoe";
		sampleService.update(employee);
		verifySqlNumber(1);
		verifySqlByRegExp(0, "UPDATE EMPLOYEE .* WHERE .*EMP_ID = \\?", "Naoki Takezoe", 1);
	}

	@Test
	public void testUpdateBatchTx(){
		List<Employee> employees = new ArrayList<Employee>();
		{
			Employee employee = new Employee();
			employee.empId = 1;
			employee.name = "Naoki Takezoe";
			employees.add(employee);
		}
		{
			Employee employee = new Employee();
			employee.empId = 2;
			employee.name = "Taro Yamada";
			employees.add(employee);
		}

		sampleService.update(employees);

		verifySqlNumber(2);

		verifySqlByRegExp(0, "UPDATE EMPLOYEE .* WHERE .*EMP_ID = \\?", "Naoki Takezoe", 1);
//		assertEquals(new Integer(0), employees.get(0).empId);

		verifySqlByRegExp(1, "UPDATE EMPLOYEE .* WHERE .*EMP_ID = \\?", "Taro Yamada", 2);
//		assertEquals(new Integer(1), employees.get(1).empId);
	}

	@Test
	public void testDeleteEntityTx(){
		Employee employee = new Employee();
		employee.empId = 1;
		employee.name = "Naoki Takezoe";

		sampleService.delete(employee);
		verifySqlNumber(1);
		verifySql(0, "DELETE FROM EMPLOYEE WHERE EMP_ID = ?", 1);
	}

	@Test
	public void testDeleteBatchTx(){
		List<Employee> employees = new ArrayList<Employee>();
		{
			Employee employee = new Employee();
			employee.empId = 1;
			employee.name = "Naoki Takezoe";
			employees.add(employee);
		}
		{
			Employee employee = new Employee();
			employee.empId = 2;
			employee.name = "Taro Yamada";
			employees.add(employee);
		}

		sampleService.delete(employees);

		verifySqlNumber(2);
		verifySql(0, "DELETE FROM EMPLOYEE WHERE EMP_ID = ?", 1);
		verifySql(1, "DELETE FROM EMPLOYEE WHERE EMP_ID = ?", 2);
	}

	@Test
	public void testDeleteTx(){
		sampleService.delete(1);
		verifySqlNumber(1);
		verifySql(0, "DELETE FROM EMPLOYEE WHERE EMP_ID = ?", 1);
	}

	@Test
	public void testGetEmployeeCount(){
		sampleService.getEmployeeCount();
		verifySqlNumber(1);
		verifySql(0, "SELECT COUNT(T1_.EMP_ID) FROM EMPLOYEE T1_");
	}

	@Test
	public void testGetEmpName(){
		sampleService.getEmpName(1);
		verifySqlNumber(1);
		verifySql(0, "SELECT NAME FROM EMPLOYEE WHERE EMP_ID = ?", 1);
	}

	@Test
	public void testGetEmpIdList(){
		sampleService.getEmpIdList();
		verifySqlNumber(1);
		verifySql(0, "SELECT EMP_ID FROM EMPLOYEE ORDER BY EMP_ID");
	}

	@Test
	public void testGetGroupCount(){
		List<Integer> groupIdList = new ArrayList<Integer>();
		groupIdList.add(1);
		groupIdList.add(2);

		sampleService.getGroupCount(groupIdList);

		verifySqlNumber(1);
		verifySql(0, "SELECT " +
				"G.GROUP_ID AS GROUP_ID, " +
				"G.GROUP_NAME AS GROUP_NAME, " +
				"COUNT(*) AS COUNT " +
				"FROM EMPLOYEE E " +
				"INNER JOIN GROUP_INFO G ON E.GROUP_ID = G.GROUP_ID " +
				"WHERE G.GROUP_ID IN (?, ?) " +
				"GROUP BY G.GROUP_ID, G.GROUP_NAME", 1, 2);
	}

	@Test
	public void testCall(){
		StoredParam param = new StoredParam();
		param.empId = 1;

		sampleService.call(param);

		verifySqlNumber(1);
		verifySql(0, "{call procName(?)}", 1);
	}

	@Test
	public void testGetEmployeeByFunction(){
		Employee employee = new Employee();
		employee.empId = 1;
		employee.name = "Naoki Takezoe";
		addResult(employee);

		StoredParam param = new StoredParam();
		param.empId = 1;

		Employee result = sampleService.getEmployeeByFunction(param);

		verifySqlNumber(1);
		verifySql(0, "{? = call funcName(?)}", null, 1);
		assertSame(1, result.empId);
		assertSame("Naoki Takezoe", result.name);
	}

}
