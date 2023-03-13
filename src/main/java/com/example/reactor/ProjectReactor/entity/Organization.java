package com.example.reactor.ProjectReactor.entity;

import java.util.List;

public class Organization {

    private int id;
    private String name;

    private List<Employee> employees;

    private List<Department> departments;

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

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public Organization(int id, String name, List<Employee> employees, List<Department> departments) {
        this.id = id;
        this.name = name;
        this.employees = employees;
        this.departments = departments;
    }

    public Organization(int id, String name, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public Organization(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Organization() {
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employees=" + employees +
                ", departments=" + departments +
                '}';
    }
}
