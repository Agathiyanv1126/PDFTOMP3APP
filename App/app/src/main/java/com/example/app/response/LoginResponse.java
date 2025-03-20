package com.example.app.response;

public class LoginResponse {
    private boolean success;
    private String message;
    private Student student;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Student getStudent() {
        return student;
    }

    public static class Student {
        private String id;
        private String register_number;
        private String name;
        private String password;

        public String getId() {
            return id;
        }

        public String getRegisterNumber() {
            return register_number;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }
    }
}
