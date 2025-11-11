package com.bajajfinserv.service;

import org.springframework.stereotype.Service;

@Service
public class SqlProblemSolverService {
    
    public String solveSqlProblem(String regNo) {
        int lastTwoDigits = extractLastTwoDigits(regNo);
        
        if (lastTwoDigits % 2 == 1) {
            return solveQuestion1();
        } else {
            return solveQuestion2();
        }
    }
    
    private int extractLastTwoDigits(String regNo) {
        try {
            if (regNo != null && regNo.length() >= 2) {
                String lastTwoChars = regNo.substring(regNo.length() - 2);
                return Integer.parseInt(lastTwoChars);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing registration number, defaulting to odd");
        }
        return 47;
    }
    
    private String solveQuestion1() {
        return "SELECT " +
               "    p.AMOUNT AS SALARY, " +
               "    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
               "    TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
               "    d.DEPARTMENT_NAME " +
               "FROM " +
               "    PAYMENTS p " +
               "JOIN " +
               "    EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
               "JOIN " +
               "    DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
               "WHERE " +
               "    DAY(p.PAYMENT_TIME) != 1 " +
               "    AND p.AMOUNT = ( " +
               "        SELECT MAX(AMOUNT) " +
               "        FROM PAYMENTS " +
               "        WHERE DAY(PAYMENT_TIME) != 1 " +
               "    )";
    }
    
    private String solveQuestion2() {
        return "SELECT " +
               "    e.EMP_ID, " +
               "    e.FIRST_NAME, " +
               "    e.LAST_NAME, " +
               "    d.DEPARTMENT_NAME, " +
               "    (SELECT COUNT(*) " +
               "     FROM EMPLOYEE e2 " +
               "     WHERE e2.DEPARTMENT = e.DEPARTMENT " +
               "     AND TIMESTAMPDIFF(YEAR, e2.DOB, CURDATE()) < TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) " +
               "    ) AS YOUNGER_EMPLOYEES_COUNT " +
               "FROM " +
               "    EMPLOYEE e " +
               "JOIN " +
               "    DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
               "ORDER BY " +
               "    e.EMP_ID DESC";
    }
}