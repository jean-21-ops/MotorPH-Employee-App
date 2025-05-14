package Term2;
public class LeaveRequest {
    private int requestID;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String status;
    private int employeeID;

    public int getRequestID() {
        return requestID;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public void submitRequest() {
    }

    public void approveRequest() {
    }

    public void rejectRequest() {
    }
}
