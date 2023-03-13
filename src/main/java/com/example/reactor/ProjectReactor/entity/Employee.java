package com.example.reactor.ProjectReactor.entity;

public class Employee {

    private int id;
    private String name;

    private int salary;

    private int organizationId;

    private int departmentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public Employee(int id, String name, int salary, int organizationId, int departmentId) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.organizationId = organizationId;
        this.departmentId = departmentId;
    }

    public Employee(int id, String name, int salary, int organizationId) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.organizationId = organizationId;
    }

    public Employee(int id, String name, int salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public Employee() {
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", organizationId=" + organizationId +
                ", departmentId=" + departmentId +
                '}';
    }
}
