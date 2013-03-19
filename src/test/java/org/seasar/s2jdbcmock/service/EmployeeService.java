package org.seasar.s2jdbcmock.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.s2jdbcmock.entity.Employee;

public class EmployeeService {

	@Resource
	protected JdbcManager jdbcManager;

	public Employee getEmployee(Integer empId){
		return jdbcManager.from(Employee.class).id(empId).getSingleResult();
	}

	public void insert(List<Employee> employees){
		jdbcManager.insertBatch(employees).execute();
	}

	public void insert(Employee employee){
		jdbcManager.insert(employee).execute();
	}

	public void update(List<Employee> employee){
		jdbcManager.updateBatch(employee).execute();
	}

	public void update(Employee employee){
		jdbcManager.update(employee).execute();
	}

	public void delete(List<Employee> employees){
		jdbcManager.deleteBatch(employees).execute();
	}

	public void delete(Employee employee){
		jdbcManager.delete(employee).execute();
	}

	public void delete(Integer empId){
		Employee employee = new Employee();
		employee.empId = empId;
		jdbcManager.delete(employee).execute();
	}

	public long getEmployeeCount(){
		return jdbcManager.from(Employee.class).getCount();
	}

	public String getEmpName(Integer empId){
		return jdbcManager.selectBySql(String.class,
				"SELECT NAME FROM EMPLOYEE WHERE EMP_ID = ?", empId)
			.getSingleResult();
	}

	public List<Integer> getEmpIdList(){
		return jdbcManager.selectBySql(Integer.class,
				"SELECT EMP_ID FROM EMPLOYEE ORDER BY EMP_ID")
			.getResultList();
	}

	public List<GroupCountDto> getGroupCount(List<Integer> groupIdList){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("groupIdList", groupIdList);

		return jdbcManager.selectBySqlFile(GroupCountDto.class,
				"org/seasar/s2jdbcmock/service/SampleService_getGroupCount.sql", param)
			.getResultList();
	}

	public void call(StoredParam param){
		jdbcManager.call("procName", param).execute();
	}

	public Employee getEmployeeByFunction(StoredParam param){
		return jdbcManager.call(Employee.class, "funcName", param).getSingleResult();
	}

	public static class StoredParam {
		public Integer empId;
	}

	public static class GroupCountDto {
		public Integer groupId;
		public String groupName;
		public Integer count;
	}
}
