package walkingtechie.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@NamedQueries({
	@NamedQuery(name = "Employee.findAll", query = "select e from Employee e")
})
@Table(name="employee")
@Entity
public class Employee {
	@Id
	@Column(name = "employee_Id", columnDefinition = "INT")
	int employeeId;
	
	@Column(name = "employee_Name", columnDefinition = "VARCHAR")
	String employeeName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "check_in", columnDefinition = "DATETIME")
	Date checkIn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "check_out", columnDefinition = "DATETIME")
	Date checkOut;

	public int getId() {
		return employeeId;
	}

	public void setId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}

	public Date getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}
}