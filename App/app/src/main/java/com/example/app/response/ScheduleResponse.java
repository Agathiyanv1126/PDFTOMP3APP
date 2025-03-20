package com.example.app.response;

public class ScheduleResponse {
    private boolean success;
    private String message;
    private Bus bus;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Bus getBus() {
        return bus;
    }

    public static class Bus {
        private String _id;
        private String date;
        private String bus_name;
        private String bus_number;

        public String getId() {
            return _id;
        }

        public String getDate() {
            return date;
        }

        public String getBusName() {
            return bus_name;
        }

        public String getBusNumber() {
            return bus_number;
        }
    }
}

